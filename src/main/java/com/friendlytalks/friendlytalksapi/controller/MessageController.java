package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.MessageContent;
import com.friendlytalks.friendlytalksapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping(
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Flux<Message> getAllMessage() {
		return this.messageService.getAllMessage();
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> addNew(@RequestBody Message message) {
		return this.messageService.addNew(message);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> deleteMessage(@PathVariable("id") String id) {
		return this.messageService.deleteMessage(id);
	}

	/**
	 * We use PATCH here because client will send only the particular messageID and the changed content.
	 * We do not want to transfer huge message objects via the wire if not necessary.
	 * @param id
	 * @param newMessageContent
	 */
	@PatchMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> editMessage(@PathVariable("id") String id, @RequestBody MessageContent newMessageContent) {
		return this.messageService.editMessage(id, newMessageContent.getContent());
	}

	/**
	 * Common login endpoint is also available for basic authentication
	 *
	 * @return A publisher serving a message stating successful log in
	 */
	@GetMapping("/test")
	public Flux<Message> login() {
		return messageService.getDefaultMessage();
	}
}
