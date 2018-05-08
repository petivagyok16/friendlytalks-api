package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageNotFound extends ResponseStatusException {
	public MessageNotFound(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
