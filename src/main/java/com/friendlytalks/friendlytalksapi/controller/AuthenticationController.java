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

	private final AuthenticationService authService;

	@Autowired
	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping(
					value = "/signup",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.CREATED)
	public void signUp(@RequestBody User user) {
		this.authService.signUp(user);
	}

	@GetMapping(
					value = "/me",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public User getAuthenticatedUser() {
		return this.authService.getAuthenticatedUser();
	}
}

