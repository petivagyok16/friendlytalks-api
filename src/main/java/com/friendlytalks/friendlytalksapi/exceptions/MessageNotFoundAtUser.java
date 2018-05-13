package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageNotFoundAtUser extends ResponseStatusException {
	public MessageNotFoundAtUser(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
