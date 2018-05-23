package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
