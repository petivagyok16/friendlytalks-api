package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.HttpResponseObject;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationRequest;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationResponse;
import com.friendlytalks.friendlytalksapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

/**
 * Signin endpoint is not necessary here because JWTAuthenticationFilter handles the /api/v1/auth/signin endpoint.
 * Signin flow happens there
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final AuthenticationService authService;

	@Autowired
	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping(
					value = "/signin",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@CrossOrigin("*")
	public Mono<ResponseEntity<JwtAuthenticationResponse>> signing(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
		return this.authService.signIn(authenticationRequest);
	}

	@PostMapping(
					value = "/signup",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<HttpResponseObject<User>>> signUp(@RequestBody User user) {
		return this.authService.signUp(user);
	}

	@GetMapping(
					value = "/me",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<UserDetails> getAuthenticatedUser() {
		return this.authService.getAuthenticatedUser();
	}
}

