package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
public class RatingContainer {

	private LinkedList<String> likes = new LinkedList<>();
	private LinkedList<String> dislikes = new LinkedList<>();

	public RatingContainer(
					@JsonProperty("likes") LinkedList<String> likes,
					@JsonProperty("dislikes") LinkedList<String> dislikes
	) {

		if (likes == null) {
			this.likes = new LinkedList<>();
		} else {
			this.likes = likes;
		}

		if (dislikes == null) {
			this.dislikes = new LinkedList<>();
		} else {
			this.dislikes = dislikes;
		}
	}
}
