package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongCredentialsException extends ResponseStatusException {

	public WrongCredentialsException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
