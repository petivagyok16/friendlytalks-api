package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFound;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
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

	@Autowired
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public Mono<HttpResponseWrapper<List<Message>>> getAllMessage() {
		return messageRepository.findAll().collectList().flatMap(messages -> Mono.just(new HttpResponseWrapper<>(messages)));
	}

	public Mono<ResponseEntity> addNew(Message message) {

		return this.messageRepository.save(message)
						.flatMap(message1 -> Mono.just(ResponseEntity.created(URI.create(String.format("messages/%s", message.getId()))).build()));
	}

	public Mono<ResponseEntity> deleteMessage(String id) {

		return this.messageRepository.findById(id)
						.defaultIfEmpty(new Message())
						.flatMap(message -> {
							if (message.getId() == null) {
								throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
							}

							return this.messageRepository.deleteById(id).then(Mono.just(ResponseEntity.ok().build()));
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
