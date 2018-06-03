package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

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

	private Meta meta;

	private PartialUser partialUser;

	public Message(
					@JsonProperty("id") String id,
					@JsonProperty("content") String content,
					@JsonProperty("created_at") Date created_at,
					@JsonProperty("meta") Meta meta,
					@JsonProperty("partialUser") PartialUser partialUser
	) {
		this.id = id;
		this.content = content;
		this.created_at = created_at;
		this.partialUser = partialUser;

		if (meta == null) {
			this.meta = new Meta();
		} else {
			this.meta = meta;
		}
	}
}
