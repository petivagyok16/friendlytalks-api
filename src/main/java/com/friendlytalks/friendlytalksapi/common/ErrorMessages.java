package com.friendlytalks.friendlytalksapi.common;

public class ErrorMessages {
	public static final String USER_NOT_FOUND = "User not found!";
	public static final String USER_ALREADY_EXISTS = "User already exists";
	public static final String WRONG_CREDENTIALS = "Wrong username or password!";
	public static final String INVALID_TOKEN = "Invalid authentication token!";
	public static final String MESSAGE_NOT_FOUND = "Message not found!";
	public static final String MESSAGE_NOT_FOUND_AT_USER = "Message was deleted but the User who owned the message did not had the message for some reason.";
	public static final String EDIT_USER_NOT_ALLOWED = "Editing the user is not allowed!";
	public static final String INCONSISTENT_RATING = "Rating does not correspond with the Rating Cases!";
	public static final String DATABASE_ERROR = "Something went wrong during the process. Please try again later!";
	public static final String MESSAGE_DELETED_BUT_USER_NOT_FOUND = "Message was deleted but it's owner is not exists anymore!";
}
