package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

// Dislikes and Likes stored in Message class
@Data
public class Meta {

	private Set<String> dislikes;
	private Set<String> likes;

	public Meta() {
		this.likes = new HashSet<>();
		this.dislikes = new HashSet<>();
	}

	public Meta(
					@JsonProperty("dislikes") Set<String> dislikes,
					@JsonProperty("likes") Set<String> likes) {
		this.likes = likes;
		this.dislikes = dislikes;
	}
}
