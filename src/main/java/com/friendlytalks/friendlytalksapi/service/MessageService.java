package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.common.RatingEnum;
import com.friendlytalks.friendlytalksapi.exceptions.InconsistentRatingException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
						.flatMap(messages -> Mono.just(new HttpResponseWrapper<>(messages)));
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
		int prevRating = rateMessageRequestBody.getPrevRating();

		// TODO: Refactor the reactive flow to more advanced/readable
		// TODO: Refactor like,dislike Sets to ArrayList, they cannot be Sets
		return this.userRepository.findById(raterUserId)
						.publishOn(Schedulers.parallel())
						.flatMap(raterUser -> Mono.just(raterUser)
						.then(
							this.messageRepository.findById(messageId)
							.publishOn(Schedulers.parallel())
							.single()
							.doOnError(this::messageNotFound)
							.flatMap(ratedMessage -> this.userRepository.findById(ratedMessage.getUserId())
											.publishOn(Schedulers.parallel())
											// TODO: handle if user does not exist anymore
											.flatMap(messageOwner -> this.rateMessage(rating, prevRating, raterUserId, messageId, messageOwner, raterUser, ratedMessage))
							)
							.then(Mono.just(ResponseEntity.ok().build()))));
	}

	private Mono<?> rateMessage(int rating, int prevRating, String raterUserId, String messageId, User messageOwner, User raterUser, Message ratedMessage) {
		switch (RatingEnum.values()[rating]) {
			case NO_RATING: {

				if (prevRating == RatingEnum.LIKE.getValue()) {
					messageOwner.getRatings().getMy().getLikes().remove(raterUserId);
					ratedMessage.getMeta().getLikes().remove(raterUserId);
					raterUser.getRatings().getGiven().getLikes().remove(messageId);
				}

				if (prevRating == RatingEnum.DISLIKE.getValue()) {
					messageOwner.getRatings().getMy().getDislikes().remove(raterUserId);
					ratedMessage.getMeta().getDislikes().remove(raterUserId);
					raterUser.getRatings().getGiven().getDislikes().remove(messageId);
				}
				break;
			}

			case LIKE: {

				if (prevRating == RatingEnum.NO_RATING.getValue() && !raterUser.getRatings().getGiven().getLikes().contains(messageId)) {
					messageOwner.getRatings().getMy().getLikes().add(raterUserId);
					ratedMessage.getMeta().getLikes().add(raterUserId);
					raterUser.getRatings().getGiven().getLikes().add(messageId);
				}

				if (prevRating == RatingEnum.DISLIKE.getValue()) {
					// Removing previous ratings
					messageOwner.getRatings().getMy().getDislikes().remove(raterUserId);
					ratedMessage.getMeta().getDislikes().remove(raterUserId);
					raterUser.getRatings().getGiven().getDislikes().remove(messageId);

					// Adding new ratings
					messageOwner.getRatings().getMy().getLikes().add(raterUserId);
					ratedMessage.getMeta().getLikes().add(raterUserId);
					raterUser.getRatings().getGiven().getLikes().add(messageId);
				}

				break;
			}

			case DISLIKE: {

				if (prevRating == RatingEnum.NO_RATING.getValue()) {
					messageOwner.getRatings().getMy().getDislikes().add(raterUserId);
					ratedMessage.getMeta().getDislikes().add(raterUserId);
					raterUser.getRatings().getGiven().getDislikes().add(messageId);
				}

				if (prevRating == RatingEnum.LIKE.getValue()) {
					// Removing previous ratings
					messageOwner.getRatings().getMy().getLikes().add(raterUserId);
					ratedMessage.getMeta().getLikes().remove(raterUserId);
					raterUser.getRatings().getGiven().getLikes().remove(messageId);

					// Adding new ratings
					messageOwner.getRatings().getMy().getDislikes().add(raterUserId);
					ratedMessage.getMeta().getDislikes().add(raterUserId);
					raterUser.getRatings().getGiven().getDislikes().add(messageId);
				}

				break;
			}

			default: {
				throw new InconsistentRatingException(ErrorMessages.INCONSISTENT_RATING);
			}
		}

		List<User> usersToSave = new ArrayList<>();
		usersToSave.add(raterUser);
		usersToSave.add(messageOwner);

		return this.userRepository.saveAll(usersToSave)
						.then(this.messageRepository.save(ratedMessage));
	}

	private void messageNotFound(Throwable error) {
		throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
	}

}
