package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.common.ExceptionThrower;
import com.friendlytalks.friendlytalksapi.common.RatingBuilder;
import com.friendlytalks.friendlytalksapi.common.RatingEnum;
import com.friendlytalks.friendlytalksapi.exceptions.InconsistentRatingException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.net.URI;
import java.util.ArrayList;
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

		return messageRepository.findAll()
						.collectList()
						.map(messages -> new HttpResponseWrapper<>(messages));
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
						.doOnError(ExceptionThrower::messageNotFound)
						.flatMap(message ->
										this.messageRepository.deleteById(messageId)
										.then(this.userRepository.findById(message.getUserId())
														.single()
														.doOnError(ExceptionThrower::messageWasDeletedButUserNotFound)
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
						.doOnError(ExceptionThrower::messageNotFound)
						.flatMap(message -> {
							message.setContent(newContent);

							return this.messageRepository.save(message).then(Mono.just(ResponseEntity.ok().build()));
						});
	}

	public Mono<ResponseEntity> rateMessage(String messageId, RateMessageRequestBody rateMessageRequestBody) {
		int rating = rateMessageRequestBody.getRating();
		String raterUserId = rateMessageRequestBody.getRaterUserId();

		return Flux.zip(
								this.messageRepository.findById(messageId),
								this.userRepository.findById(raterUserId),
								this.userRepository.findUserByMessage(messageId))
						.single()
						.doOnError(ExceptionThrower::emptySourceError)
						.publishOn(Schedulers.parallel())
						.flatMap(publisherList -> this.rateMessage(rating, raterUserId, messageId, publisherList))
						.then(Mono.just(ResponseEntity.ok().build()));

	}

	private Mono<List<Tuple2<User, Message>>> rateMessage(int rating, String raterUserId, String messageId, Tuple3<Message, User, User> publisherList) {

		switch (RatingEnum.values()[rating]) {
			case NO_RATING: {
				RatingBuilder.addNoRating(raterUserId, messageId, publisherList);

				break;
			}

			case LIKE: {
				RatingBuilder.addLike(raterUserId, messageId, publisherList);

				break;
			}

			case DISLIKE: {
				RatingBuilder.addDislike(raterUserId, messageId, publisherList);

				break;
			}

			default: {
				throw new InconsistentRatingException(ErrorMessages.INCONSISTENT_RATING);
			}
		}

		// Note that publisherList contains the publishers in the same order as we loaded into the Flux.zip() above.
		Message ratedMessage = publisherList.getT1();
		User raterUser = publisherList.getT2();
		User messageOwner = publisherList.getT3();

		List<User> usersToSave = new ArrayList<>();
		usersToSave.add(raterUser);
		usersToSave.add(messageOwner);

		return Flux.zip(
							this.userRepository.saveAll(usersToSave),
							this.messageRepository.save(ratedMessage))
						.publishOn(Schedulers.parallel())
						.collectList();
	}

}
