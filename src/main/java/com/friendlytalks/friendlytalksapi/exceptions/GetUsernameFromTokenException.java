package com.friendlytalks.friendlytalksapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GetUsernameFromTokenException extends ResponseStatusException {
	public GetUsernameFromTokenException(String reason) {
		super(HttpStatus.FORBIDDEN, reason);
	}
}
