package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
@Getter
@Setter
public class User {

	@Id private String id;
	private String username;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String city;
	private String pictureUrl;
	private List<String> messages;
	private Relations relations;
	private Rating ratings;

	public User(
					@JsonProperty("id") String id,
					@JsonProperty("username") String username,
					@JsonProperty("email") String email,
					@JsonProperty("password") String password,
					@JsonProperty("firstName") String firstName,
					@JsonProperty("lastName") String lastName,
					@JsonProperty("city") String city,
					@JsonProperty("pictureUrl") String pictureUrl,
					@JsonProperty("messages") List<String> messages,
					@JsonProperty("relations") Relations relations,
					@JsonProperty("ratings") Rating ratings
	) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName= lastName;
		this.city = city;
		this.pictureUrl = pictureUrl;
		this.messages = messages;
		this.relations = relations;
		this.ratings = ratings;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
}
