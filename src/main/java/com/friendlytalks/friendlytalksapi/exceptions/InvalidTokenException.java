package com.friendlytalks.friendlytalksapi.exceptions;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(String message) {
		super(message);
	}
}
