package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFound;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("messageService")
public class MessageService {

	private final MessageRepository messageRepository;

	@Autowired
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public List<Message> getAllMessage() {
		return messageRepository.findAll();
	}

	public void addNew(Message message) {
		this.messageRepository.insert(message);
	}

	public void deleteMessage(String id) {
		try {
			this.messageRepository.deleteById(id);
		} catch (RuntimeException e) {
			throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
		}
	}

	public void editMessage(String id, String newContent) {
		Optional<Message> message = this.messageRepository.findById(id);

		if (message.isPresent()) {
			message.get().setContent(newContent);
			this.messageRepository.save(message.get());
		} else {
			throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
		}
	}

}
