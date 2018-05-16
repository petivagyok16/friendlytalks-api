package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

		private List<String> likes = new ArrayList<>();
		private List<String> dislikes = new ArrayList<>();

		public RatingContainer(
						@JsonProperty("likes") List<String> likes,
						@JsonProperty("dislikes") List<String> dislikes
		) {

			if (likes == null) {
				this.likes = new ArrayList<>();
			} else {
				this.likes = likes;
			}

			if (dislikes == null) {
				this.dislikes = new ArrayList<>();
			} else {
				this.dislikes = dislikes;
			}
		}
	}
}
