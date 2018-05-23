package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.EditedMessage;
import com.friendlytalks.friendlytalksapi.model.RateMessageRequestBody;
import com.friendlytalks.friendlytalksapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
@Validated
@CrossOrigin(origins = "*")
public class MessageController {

	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<HttpResponseWrapper<List<Message>>> getAllMessage() {
		return this.messageService.getAllMessage();
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity> addNew(@Valid @RequestBody Message message) {
		return this.messageService.addNew(message);
	}

	@DeleteMapping(value = "/{id}")
	public Mono<ResponseEntity> deleteMessage(@NotNull @PathVariable("id") String id) {
		return this.messageService.deleteMessage(id);
	}

	/**
	 * We use PATCH here because client will send only the particular messageID and the changed content.
	 * We do not want to transfer huge message objects via the wire if not necessary.
	 * @param id
	 * @param newMessageContent The new message content
	 */
	@PatchMapping(value = "/{id}")
	public Mono<ResponseEntity> editMessage(@NotNull @PathVariable("id") String id, @RequestBody EditedMessage newMessageContent) {
		return this.messageService.editMessage(id, newMessageContent);
	}

	@PatchMapping(value = "/rate/{messageId}")
	public Mono<ResponseEntity> rateMessage(@NotNull @PathVariable("messageId") String messageId, @Valid @RequestBody RateMessageRequestBody rateMessageRequestBody) {
		return this.messageService.rateMessage(messageId, rateMessageRequestBody);
	}
}
