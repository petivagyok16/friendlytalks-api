package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RateMessageRequestBody {

	@NotBlank(message = "Rating cannot be empty!")
	private int rating;

	@NotBlank(message = "RaterUserId cannot be empty!")
	private String raterUserId;

	@NotBlank(message = "PrevRating cannot be empty!")
	private int prevRating;

	public RateMessageRequestBody(@JsonProperty("rating") int rating,
																@JsonProperty("raterUserId") String raterUserId,
																@JsonProperty("prevRating") int prevRating) {
		this.rating = rating;
		this.raterUserId = raterUserId;
		this.prevRating = prevRating;
	}
}
