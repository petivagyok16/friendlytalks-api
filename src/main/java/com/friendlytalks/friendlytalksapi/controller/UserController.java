package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.AuthenticationService;
import com.friendlytalks.friendlytalksapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private UserService userService;
	private AuthenticationService authService;

	@Autowired
	public UserController(UserService userService, AuthenticationService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@RequestMapping(
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<User> getAllUser() {
		return this.userService.getAllUser();
	}
}
