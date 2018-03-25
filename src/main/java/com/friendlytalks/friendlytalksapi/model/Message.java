package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "messages")
public class Message {
	@Id private String id;
	private String content;
	private Date created_at;
	private String user;
	private Meta meta;

	public Message(
					@JsonProperty("id") String id,
					@JsonProperty("content") String content,
					@JsonProperty("created_at") Date created_at,
					@JsonProperty("meta") Meta meta,
					@JsonProperty("user") String user) {
		this.id = id;
		this.content = content;
		this.created_at = created_at;
		this.meta = meta;
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	// Dislikes and Likes stored as an inline Class inside Message
	public static class Meta {
		private List<String> dislikes;
		private List<String> likes;

		public Meta(
						@JsonProperty("dislikes") List<String> dislikes,
						@JsonProperty("likes") List<String> likes) {
			this.dislikes = dislikes;
			this.likes = likes;
		}

		public List<String> getDislikes() {
			return dislikes;
		}

		public void setDislikes(List<String> dislikes) {
			this.dislikes = dislikes;
		}

		public List<String> getLikes() {
			return likes;
		}

		public void setLikes(List<String> likes) {
			this.likes = likes;
		}
	}
}
