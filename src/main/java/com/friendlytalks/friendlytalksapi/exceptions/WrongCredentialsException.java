package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)

public class WrongCredentialsException extends RuntimeException {

	public WrongCredentialsException(String message) {
		super(message);
	}
}
