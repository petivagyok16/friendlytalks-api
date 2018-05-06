package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

	@Id
	private String id;

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
	private List<String> roles = new ArrayList<>();
	private boolean enabled;

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

		// TODO: authentication works currently with roles, so that all the users will get admin role, but we dont use roles in the client application
		this.roles = Arrays.asList("ROLE_ADMIN");
		this.enabled = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(roles.toArray(new String[roles.size()]));
	}

	//@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
}
