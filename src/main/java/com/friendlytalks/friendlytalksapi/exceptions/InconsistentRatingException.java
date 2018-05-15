package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InconsistentRatingException extends ResponseStatusException {
	public InconsistentRatingException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
