package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

/**
 * A wrapper class for editing existing messages.
 * The HttpRequestBody will only hold the new message content.
 */
public class EditedMessage {

	@NotBlank
	private String content;

	public EditedMessage(@JsonProperty("content") String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}
