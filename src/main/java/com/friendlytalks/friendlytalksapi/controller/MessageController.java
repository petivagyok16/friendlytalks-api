package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.MessageContent;
import com.friendlytalks.friendlytalksapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
	@Autowired MessageService messageService;

	@RequestMapping(
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<Message> getAllMessage() {
		return this.messageService.getAllMessage();
	}

	@RequestMapping(
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.CREATED)
	public void addNew(@RequestBody Message message) {
		this.messageService.addNew(message);
	}

	@RequestMapping(
					value = "/{id}",
					method = RequestMethod.DELETE
	)
	@ResponseStatus(HttpStatus.OK)
	public void deleteMessage(@PathVariable("id") String id) {
		this.messageService.deleteMessage(id);
	}

	/**
	 * We use PATCH here because client will send only the particular messageID and the changed content.
	 * We do not want to transfer huge message objects via the wire if not necessary.
	 * @param id
	 * @param newMessageContent
	 */
	@RequestMapping(
					value = "/{id}",
					method = RequestMethod.PATCH
	)
	@ResponseStatus(HttpStatus.OK)
	public void editMessage(@PathVariable("id") String id, @RequestBody MessageContent newMessageContent) {
		this.messageService.editMessage(id, newMessageContent.getContent());
	}
}
