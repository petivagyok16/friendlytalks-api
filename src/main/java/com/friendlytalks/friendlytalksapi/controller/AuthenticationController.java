package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationRequest;
import com.friendlytalks.friendlytalksapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthenticationController {

	private final AuthenticationService authService;

	@Autowired
	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping(
					value = "/signin",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@CrossOrigin("*")
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> signIn(@Valid @RequestBody JwtAuthenticationRequest authenticationRequest) {
		return this.authService.signIn(authenticationRequest);
	}

	@PostMapping(
					value = "/signup",
					consumes =  MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity> signUp(@Valid @RequestBody User user) {
		return this.authService.signUp(user);
	}

	@GetMapping(
					value = "/me",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getAuthenticatedUser(@RequestHeader(value = "Authorization") String bearerToken) {
		return this.authService.getAuthenticatedUser(bearerToken);
	}
}

