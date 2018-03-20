package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.Credentials;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<User> getAllStudents() {
		return this.userService.getAllUser();
	}

	@RequestMapping(
					value = "/signup",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.CREATED)
	public void signUp(@RequestBody User user) {
		this.userService.signUp(user);
	}

	@RequestMapping(
					value = "/signin",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(HttpStatus.OK)
	public void signIn(@RequestBody Credentials credentials) {
		this.userService.signIn(credentials);
	}
}
