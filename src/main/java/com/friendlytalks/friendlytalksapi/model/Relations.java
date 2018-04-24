package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class Relations {

	private List<String> followers;
	private List<String> following;

	public Relations(
					@JsonProperty("followers") List<String> followers,
					@JsonProperty("following") List<String> following
	) {
		this.followers = followers;
		this.following = following;
	}
}
