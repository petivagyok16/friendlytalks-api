package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class Rating {

	private RatingContainer my;
	private RatingContainer given;

	public Rating(
					@JsonProperty("my") RatingContainer my,
					@JsonProperty("given") RatingContainer given
	) {
		this.my = my;
		this.given = given;
	}

	@Getter
	@Setter
	public static class RatingContainer {

		private List<String> likes;
		private List<String> dislikes;

		public RatingContainer(
						@JsonProperty("likes") List<String> likes,
						@JsonProperty("dislikes") List<String> dislikes
		) {
			this.likes = likes;
			this.dislikes = dislikes;
		}

		public List<String> getLikes() {
			return likes;
		}

		public void setLikes(List<String> likes) {
			this.likes = likes;
		}

		public List<String> getDislikes() {
			return dislikes;
		}

		public void setDislikes(List<String> dislikes) {
			this.dislikes = dislikes;
		}
	}
}
