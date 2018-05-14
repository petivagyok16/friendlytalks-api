package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFound;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFoundAtUser;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
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
						.flatMap(message1 -> this.userRepository.findById(message1.getUserId())
										.flatMap(user -> {
											user.getMessages().add(message1.getId());
											return this.userRepository.save(user)
															.then(Mono.just(ResponseEntity.created(URI.create(String.format("messages/%s", message.getId()))).build()));
										}));
	}

	public Mono<ResponseEntity> deleteMessage(String messageId) {
		return this.messageRepository.findById(messageId)
						.defaultIfEmpty(new Message())
						.flatMap(message -> {
							if (message.getId() == null) {
								throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
							}

							return this.messageRepository.deleteById(messageId)
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
											);
						});
	}

	public Mono<ResponseEntity> editMessage(String id, String newContent) {

		return this.messageRepository.findById(id)
						.defaultIfEmpty(new Message())
						.flatMap(message -> {

							if (message.getId() == null) {
								throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
							}

							message.setContent(newContent);

							return this.messageRepository.save(message).then(Mono.just(ResponseEntity.ok().build()));
						});
	}

}
