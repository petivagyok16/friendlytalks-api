package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@Data
@NoArgsConstructor
public class Ratings {

	private RatingContainer my = new RatingContainer();
	private RatingContainer given = new RatingContainer();

	public Ratings(
					@JsonProperty("my") RatingContainer my,
					@JsonProperty("given") RatingContainer given
	) {
		this.my = my;
		this.given = given;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class RatingContainer {

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
}
