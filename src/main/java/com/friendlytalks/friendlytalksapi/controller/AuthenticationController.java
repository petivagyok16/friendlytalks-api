package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Signin endpoint is not necessary here because JWTAuthenticationFilter handles the /api/v1/auth/signin endpoint.
 * Signin flow happens there
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private AuthenticationService authService;

	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

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
					value = "/me",
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public User getAuthenticatedUser() {
		return this.authService.getAuthenticatedUser();
	}
}

