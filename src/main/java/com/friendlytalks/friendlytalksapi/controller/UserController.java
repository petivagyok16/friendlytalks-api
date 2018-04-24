package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.AuthenticationService;
import com.friendlytalks.friendlytalksapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	private final AuthenticationService authService;

	@Autowired
	public UserController(UserService userService, AuthenticationService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<User> getAllUser() {
		return this.userService.getAllUser();
	}
}
