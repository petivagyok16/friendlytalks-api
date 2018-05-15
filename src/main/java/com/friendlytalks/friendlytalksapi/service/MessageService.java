package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.common.RatingEnum;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFound;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFoundAtUser;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.RateMessageRequestBody;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class MessageService {

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;

	@Autowired
	public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}

	public Mono<HttpResponseWrapper<List<Message>>> getAllMessage() {
		return messageRepository.findAll().collectList().flatMap(messages -> Mono.just(new HttpResponseWrapper<>(messages)));
	}

	public Mono<ResponseEntity> addNew(Message message) {

		return this.messageRepository.save(message)
						.flatMap(savedMessage -> this.userRepository.findById(savedMessage.getUserId())
										.flatMap(user -> {
											user.getMessages().add(savedMessage.getId());
											return this.userRepository.save(user)
															.then(Mono.just(ResponseEntity.created(URI.create(String.format("messages/%s", message.getId()))).build()));
										}));
	}

	public Mono<ResponseEntity> deleteMessage(String messageId) {
		return this.messageRepository.findById(messageId)
						.single()
						.doOnError(this::messageNotFound)
						.flatMap(message -> this.messageRepository.deleteById(messageId)
										.then(this.userRepository.findById(message.getUserId())
														.flatMap(user -> {
																if (user.getMessages().contains(messageId)) {
																	user.getMessages().remove(messageId);
																	return this.userRepository.save(user)
																					.then(Mono.just(ResponseEntity.ok().build()));
																} else {
																	throw new MessageNotFoundAtUser(ErrorMessages.MESSAGE_NOT_FOUND_AT_USER);
																}
														})
										));
	}

	public Mono<ResponseEntity> editMessage(String id, String newContent) {

		return this.messageRepository.findById(id)
						.single()
						.doOnError(this::messageNotFound)
						.flatMap(message -> {
							message.setContent(newContent);

							return this.messageRepository.save(message).then(Mono.just(ResponseEntity.ok().build()));
						});
	}

	public Mono<ResponseEntity> rateMessage(String messageId, RateMessageRequestBody rateMessageRequestBody) {
		int rating = rateMessageRequestBody.getRating();
		String raterUserId = rateMessageRequestBody.getRaterUserId();
		User raterUser = this.userRepository.findById(raterUserId).block();
		int prevRating = rateMessageRequestBody.getPrevRating();

		return this.messageRepository.findById(messageId)
						.single()
						.doOnError(this::messageNotFound)
						.flatMap(ratedMessage -> this.userRepository.findById(ratedMessage.getUserId())
										.flatMap(messageOwner -> {

											switch (RatingEnum.values()[rating]) {
												case NO_RATING: {

													if (prevRating == RatingEnum.LIKE.getValue()) {
														messageOwner.getRatings().getMy().getLikes().remove(raterUserId);
													}

													break;
												}

												case LIKE: {

													break;
												}

												case DISLIKE: {

													break;
												}
											}
										})
						)
						.then(ResponseEntity.ok().build());

		return null;
	}

	private void messageNotFound(Throwable error) {
		throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
	}

}
