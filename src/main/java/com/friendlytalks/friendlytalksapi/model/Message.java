package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "messages")
@Data
@NoArgsConstructor
public class Message {

	@Id
	private String id;

	@NotBlank(message = "Content cannot be empty!")
	private String content;

	@PastOrPresent
	private Date created_at;

	@NotBlank(message = "No user was attached to the message!")
	private String userId;

	private Meta meta;

	public Message(
					@JsonProperty("id") String id,
					@JsonProperty("content") String content,
					@JsonProperty("created_at") Date created_at,
					@JsonProperty("meta") Meta meta,
					@JsonProperty("userId") String userId) {
		this.id = id;
		this.content = content;
		this.created_at = created_at;
		this.userId = userId;

		if (meta == null) {
			this.meta = new Meta();
		} else {
			this.meta = meta;
		}
	}

	// Dislikes and Likes stored as an inline Class inside Message
	@Data
	public static class Meta {

		private Set<String> dislikes;
		private Set<String> likes;

		public Meta() {
				this.likes = new HashSet<>();
				this.dislikes = new HashSet<>();
		}

		public Meta(
						@JsonProperty("dislikes") Set<String> dislikes,
						@JsonProperty("likes") Set<String> likes) {
				this.likes = likes;
				this.dislikes = dislikes;
		}
	}
}
