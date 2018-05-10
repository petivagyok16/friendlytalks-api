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
public class Relations {

	private Set<String> followers = new HashSet<>();
	private Set<String> following = new HashSet<>();

	public Relations(
					@JsonProperty("followers") Set<String> followers,
					@JsonProperty("following") Set<String> following
	) {

		if (followers == null) {
			this.followers = new HashSet<>();
		} else {
			this.followers = followers;
		}

		if (following == null) {
			this.following = new HashSet<>();
		} else {
			this.following = following;
		}
	}
}
