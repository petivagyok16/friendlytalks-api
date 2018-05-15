package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EditUserNotAllowed extends ResponseStatusException {
	public EditUserNotAllowed(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}
}
