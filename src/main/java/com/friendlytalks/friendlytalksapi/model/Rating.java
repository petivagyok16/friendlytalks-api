package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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

	public RatingContainer getMy() {
		return my;
	}

	public void setMy(RatingContainer my) {
		this.my = my;
	}

	public RatingContainer getGiven() {
		return given;
	}

	public void setGiven(RatingContainer given) {
		this.given = given;
	}


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
