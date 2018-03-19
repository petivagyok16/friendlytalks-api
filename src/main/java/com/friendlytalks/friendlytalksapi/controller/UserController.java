package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
