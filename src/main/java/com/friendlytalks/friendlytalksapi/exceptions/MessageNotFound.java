package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MessageNotFound extends RuntimeException {
	public MessageNotFound(String message) {
		super(message);
	}
}
