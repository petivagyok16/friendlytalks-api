package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageWasDeletedButUserNotFound extends ResponseStatusException {
	public MessageWasDeletedButUserNotFound(String message) {
		super(HttpStatus.NOT_FOUND, message);

	}
}
