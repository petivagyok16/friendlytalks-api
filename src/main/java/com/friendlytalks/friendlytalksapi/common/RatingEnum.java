package com.friendlytalks.friendlytalksapi.common;

public enum RatingEnum {
	NO_RATING(0),
	LIKE(1),
	DISLIKE(2);

	private final int value;

	RatingEnum(final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}
}
