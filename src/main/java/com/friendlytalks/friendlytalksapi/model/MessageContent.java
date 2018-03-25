package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A wrapper class for editing existing messages.
 * The HttpRequestBody will only hold the new message content.
 */
public class MessageContent {
	private String content;

	public MessageContent(@JsonProperty("content") String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}
