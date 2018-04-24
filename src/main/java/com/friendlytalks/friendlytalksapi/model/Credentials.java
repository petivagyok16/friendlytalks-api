package com.friendlytalks.friendlytalksapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Credentials {
	@NotEmpty private String username;
	@NotEmpty private String password;
}
