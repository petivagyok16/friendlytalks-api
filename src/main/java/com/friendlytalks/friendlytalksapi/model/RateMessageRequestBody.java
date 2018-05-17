package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class RateMessageRequestBody {

	@NotNull(message = "Rating cannot be empty!")
	@Min(value = 0, message = "Rating must be between 0-2")
	@Max(value = 2, message = "Rating must be between 0-2")
	private int rating;

	@NotBlank(message = "RaterUserId cannot be empty!")
	private String raterUserId;

	public RateMessageRequestBody(@JsonProperty("rating") int rating,
																@JsonProperty("raterUserId") String raterUserId) {
		this.rating = rating;
		this.raterUserId = raterUserId;
	}
}
