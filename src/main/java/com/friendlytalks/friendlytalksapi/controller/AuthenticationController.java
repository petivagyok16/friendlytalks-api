package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.Credentials;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired private AuthenticationService authService;

	@RequestMapping(
					value = "/signup",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.CREATED)
	public void signUp(@RequestBody User user) {
		this.authService.signUp(user);
	}

	@RequestMapping(
					value = "/signin",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.OK)
	public User signIn(@RequestBody Credentials credentials) {
		return this.authService.signIn(credentials);
	}

	@RequestMapping(
					value = "/me",
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public User getAuthenticatedUser() {
		return this.authService.getAuthenticatedUser();
	}
}

