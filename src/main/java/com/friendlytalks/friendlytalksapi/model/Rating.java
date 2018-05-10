package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Data
@NoArgsConstructor
public class Rating {

	private RatingContainer my = new RatingContainer();
	private RatingContainer given = new RatingContainer();

	public Rating(
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

		private Set<String> likes = new HashSet<>();
		private Set<String> dislikes = new HashSet<>();

		public RatingContainer(
						@JsonProperty("likes") Set<String> likes,
						@JsonProperty("dislikes") Set<String> dislikes
		) {
			this.likes = likes;
			this.dislikes = dislikes;
		}
	}
}
