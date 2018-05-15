package com.friendlytalks.friendlytalksapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditedUser {

	@NotBlank(message = "Email cannot be empty!")
	@Email(message = "Email should be valid!")
	private String email;

	@NotBlank(message = "First name cannot be empty!")
	private String firstName;

	@NotBlank(message = "Last name cannot be empty!")
	private String lastName;

	@NotBlank(message = "City cannot be empty!")
	private String city;
	private String pictureUrl;
}
