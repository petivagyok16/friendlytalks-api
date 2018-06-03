package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * PartialUser is contained by messages
 */
@Data
public class PartialUser {

	@NotBlank(message = "No user was attached to the message!")
	private String id;

	@NotBlank(message = "No pictureUrl was attached to the message!")
	private String pictureUrl;

	@NotBlank(message = "No username was attached to the message!")
	private String username;

	public PartialUser(
					@JsonProperty("id") String id,
					@JsonProperty("pictureUrl") String pictureUrl,
					@JsonProperty("username") String username) {
		this.id = id;
		this.pictureUrl = pictureUrl;
		this.username = username;
	}
}
