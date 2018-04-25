package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFound;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class MessageService {

	private final MessageRepository messageRepository;

	@Autowired
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public Flux<Message> getAllMessage() {
		return messageRepository.findAll();
	}

	public Mono<Void> addNew(Message message) {
		return this.messageRepository.insert(message).then();
	}

	public Mono<Void> deleteMessage(String id) {
		try {
			return this.messageRepository.deleteById(id).then();
		} catch (RuntimeException e) {
			throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
		}
	}

	public Mono<Void> editMessage(String id, String newContent) {
		Message message = this.messageRepository.findById(id).block();

		if (message != null) {
			message.setContent(newContent);
			return this.messageRepository.save(message).then();
		} else {
			throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
		}
	}

	public Flux<Message> getDefaultMessage(){
		return Flux.just(new Message("asd", "First message", new Date(), null, null));
	}

}
