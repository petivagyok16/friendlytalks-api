package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
public class User {

	@Id private String id;
	private String username;
	private String email;
	private String password;
	//	private List<String> tokens; TODO: implement tokens later!
	private Name name;
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
					@JsonProperty("name") Name name,
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
		this.name = name;
		this.city = city;
		this.pictureUrl = pictureUrl;
		this.messages = messages;
		this.relations = relations;
		this.ratings = ratings;
	}

	// Inner static class for Name property because its unfortunately a different namespace
	public static class Name {

		private String first;
		private String last;

		public Name(
						@JsonProperty("first") String first,
						@JsonProperty("last") String last
		) {
			this.first = first;
			this.last = last;
		}

		public String getFirst() {
			return first;
		}

		public void setFirst(String first) {
			this.first = first;
		}

		public String getLast() {
			return last;
		}

		public void setLast(String last) {
			this.last = last;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Relations getRelations() {
		return relations;
	}

	public void setRelations(Relations relations) {
		this.relations = relations;
	}

	public Rating getRatings() {
		return ratings;
	}

	public void setRatings(Rating ratings) {
		this.ratings = ratings;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}
}
