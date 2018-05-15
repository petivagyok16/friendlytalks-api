package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;
import com.friendlytalks.friendlytalksapi.model.EditedUser;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final MessageRepository messageRepository;

	@Autowired
	public UserService(UserRepository userRepository, MessageRepository messageRepository) {
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getAllUser() {
		return this.userRepository.findAll().collectList()
						.filter(users -> users.size() > 0)
						.map(users -> ResponseEntity.ok(new HttpResponseWrapper<>(users)))
						.defaultIfEmpty(ResponseEntity.noContent().build());
	}

	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getUserProfile(String id) {
		return this.userRepository.findById(id)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> Mono.just(ResponseEntity.ok().body(new HttpResponseWrapper<>(user))));
	}

	public Mono<ResponseEntity> followUser(String followerId, String toFollowId) {
		return this.userRepository.findById(followerId)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> {
							if (user.getRelations().getFollowing().contains(toFollowId)) {
								// Un-follow logic (user already followed the other user)
								user.getRelations().getFollowing().remove(toFollowId);
								return this.userRepository.save(user)
												.then(
													this.userRepository.findById(toFollowId)
														.flatMap(followerUser -> {
															followerUser.getRelations().getFollowers().remove(followerId);
															return this.userRepository.save(followerUser).then(Mono.just(ResponseEntity.ok().build()));
														})
												);
							} else {
								// Follow logic
								user.getRelations().getFollowing().add(toFollowId);
								return this.userRepository.save(user)
												.then(
													this.userRepository.findById(toFollowId)
														.flatMap(followerUser -> {
															followerUser.getRelations().getFollowers().add(followerId);
															return this.userRepository.save(followerUser).then(Mono.just(ResponseEntity.ok().build()));
														})
												);
							}

						});
	}

	public Mono<ResponseEntity<HttpResponseWrapper<User>>> editUser(String userId, EditedUser editedUser) {
		// TODO: Check auth token whether the usernames match
		return this.userRepository.findById(userId)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> {
							user.setCity(editedUser.getCity());
							user.setEmail(editedUser.getEmail());
							user.setFirstName(editedUser.getFirstName());
							user.setLastName(editedUser.getLastName());
							user.setPictureUrl(editedUser.getPictureUrl());
							return this.userRepository.save(user);
						})
						.map(user -> ResponseEntity.ok().body(new HttpResponseWrapper<>(user)));
	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getUserFollowers(String userId) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> this.userRepository.findAllById(user.getRelations().getFollowers()).collectList()
										.map(users -> ResponseEntity.ok().body(new HttpResponseWrapper<>(users))));

	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getUserFollowings(String userId) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> this.userRepository.findAllById(user.getRelations().getFollowing()).collectList()
										.map(users -> ResponseEntity.ok().body(new HttpResponseWrapper<>(users))));

	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<Message>>>> getFollowingMessages(String userId) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> this.messageRepository.findAllMessageByUserId(user.getRelations().getFollowing()).collectList())
						.map(messages -> ResponseEntity.ok().body(new HttpResponseWrapper<>(messages)));

	}
}
