package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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

	public List<String> getFollowers() {
		return followers;
	}

	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}

	public List<String> getFollowing() {
		return following;
	}

	public void setFollowing(List<String> following) {
		this.following = following;
	}
}
