package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

/**
 * A wrapper class for editing existing messages.
 * The HttpRequestBody will only hold the new message content.
 */
public class EditedMessage {

	@NotBlank
	private String content;

	@NotBlank
	@PastOrPresent
	private Date created_at;

	public EditedMessage(@JsonProperty("content") String content, @JsonProperty("created_at") Date created_at) {
		this.content = content;
		this.created_at = created_at;
	}

	public String getContent() {
		return content;
	}

	public Date getCreated_at() {
		return this.created_at;
	}
}
