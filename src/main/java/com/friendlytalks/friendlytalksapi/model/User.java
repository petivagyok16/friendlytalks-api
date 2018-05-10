package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

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

	@NotBlank(message = "Username cannot be empty!")
	private String username;

	@NotBlank(message = "Email cannot be empty!")
	@Email(message = "Email should be valid!")
	private String email;

	@Size(min = 4, message = "Password must be at least 4 characters long!")
	private String password;

	@NotBlank(message = "First name cannot be empty!")
	private String firstName;

	@NotBlank(message = "Last name cannot be empty!")
	private String lastName;

	@NotBlank(message = "City cannot be empty!")
	private String city;
	private String pictureUrl;

	@Builder.Default
	private Set<String> messages = new HashSet<>();

	@Builder.Default
	private Relations relations = new Relations();

	@Builder.Default
	private Rating ratings = new Rating();

	@Builder.Default
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
					@JsonProperty("messages") Set<String> messages,
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

		if (messages == null) {
			this.messages = new HashSet<>();
		} else {
			this.messages = messages;
		}

		if (relations == null) {
			this.relations = new Relations();
		} else {
			this.relations = relations;
		}

		if (ratings == null) {
			this.ratings = new Rating();
		} else {
			this.ratings = ratings;
		}

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
