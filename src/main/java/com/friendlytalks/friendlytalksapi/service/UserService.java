package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.common.ExceptionThrower;
import com.friendlytalks.friendlytalksapi.converters.UserConverter;
import com.friendlytalks.friendlytalksapi.exceptions.EditUserNotAllowed;
import com.friendlytalks.friendlytalksapi.model.EditedUser;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.friendlytalks.friendlytalksapi.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final MessageRepository messageRepository;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserConverter userConverter;

	@Autowired
	public UserService(
					UserRepository userRepository,
					MessageRepository messageRepository,
					JwtTokenUtil jwtTokenUtil,
					UserConverter userConverter
	) {
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userConverter = userConverter;
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
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> Mono.just(ResponseEntity.ok().body(new HttpResponseWrapper<>(user))));
	}

	public Mono<ResponseEntity> followUser(String followerId, String toFollowId) {
		return this.userRepository.findById(followerId)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> {
							if (user.getRelations().getFollowing().contains(toFollowId)) {
								// Un-follow logic (user already followed the other user)
								user.getRelations().getFollowing().remove(toFollowId);
								return this.userRepository.save(user)
												.then(this.userRepository.findById(toFollowId))
												.single()
												.doOnError(ExceptionThrower::userNotFound)
												.flatMap(followedUser -> {
													followedUser.getRelations().getFollowers().remove(followerId);
													return this.userRepository.save(followedUser);
												})
												.then(Mono.just(ResponseEntity.ok().build()));
							} else {
								// Follow logic
								user.getRelations().getFollowing().add(toFollowId);
								return this.userRepository.save(user)
												.then(this.userRepository.findById(toFollowId))
												.single()
												.doOnError(ExceptionThrower::userNotFound)
												.flatMap(followedUser -> {
													followedUser.getRelations().getFollowers().add(followerId);
													return this.userRepository.save(followedUser);
												})
												.then(Mono.just(ResponseEntity.ok().build()));
							}

						});
	}

	public Mono<ResponseEntity<HttpResponseWrapper<User>>> editUser(String userId, EditedUser editedUser, String bearerToken) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> {
							if (this.checkUsernames(bearerToken, user.getUsername())) {
								return Mono.just(user);
							} else {
								throw new EditUserNotAllowed(ErrorMessages.EDIT_USER_NOT_ALLOWED);
							}
						})
						.flatMap(user -> this.userRepository.save(this.userConverter.convert(editedUser, user)))
						.map(user -> ResponseEntity.ok().body(new HttpResponseWrapper<>(user)));
	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getUserFollowers(String userId) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> this.userRepository.findAllById(user.getRelations().getFollowers()).collectList())
						.map(users -> ResponseEntity.ok().body(new HttpResponseWrapper<>(users)));

	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getUserFollowings(String userId) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> this.userRepository.findAllById(user.getRelations().getFollowing()).collectList())
						.map(users -> ResponseEntity.ok().body(new HttpResponseWrapper<>(users)));

	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<Message>>>> getFollowingMessages(String userId) {
		return this.userRepository.findById(userId)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> this.messageRepository.findAllMessageByUserId(user.getRelations().getFollowing()).collectList())
						.map(messages -> ResponseEntity.ok().body(new HttpResponseWrapper<>(messages)));
	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> findUser(String nameFragment) {
		String usernameRegex = ".*" + nameFragment + "*.";
		return this.userRepository.findUsersByRegexpUsername(usernameRegex)
						.collectList()
						.map(users -> ResponseEntity.ok().body(new HttpResponseWrapper<>(users)));

	}

	private boolean checkUsernames(String bearerToken, String username) {
		return this.jwtTokenUtil.getUsernameFromToken(this.jwtTokenUtil.formatToken(bearerToken)).equals(username);
	}
}
