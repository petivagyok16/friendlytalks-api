package com.friendlytalks.friendlytalksapi.common;

import com.friendlytalks.friendlytalksapi.exceptions.MessageNotFound;
import com.friendlytalks.friendlytalksapi.exceptions.MessageWasDeletedButUserNotFound;
import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class ExceptionThrower {

	public static void messageNotFound(Throwable error) {
		throw new MessageNotFound(ErrorMessages.MESSAGE_NOT_FOUND);
	}

	public static void messageWasDeletedButUserNotFound(Throwable error) {
		throw new MessageWasDeletedButUserNotFound(ErrorMessages.MESSAGE_DELETED_BUT_USER_NOT_FOUND);
	}

	public static void userNotFound(Throwable error) {
		log.error("User not found: " + error);
		throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
	}

	public static void handleDatabaseError(Throwable error) {
		// TODO: make this more specified (if message/user not found show that error, if other db error throws show that)
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.DATABASE_ERROR);
	}

}
