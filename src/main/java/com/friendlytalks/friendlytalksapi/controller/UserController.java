package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path = "/api/v1/user", produces = { APPLICATION_JSON_UTF8_VALUE })
@CrossOrigin(origins = "*")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getAllUser() {
		return this.userService.getAllUser();
	}

	@GetMapping(value = "/{id}")
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getUserProfile(@NotNull @PathVariable("id") String id) {
		return this.userService.getUserProfile(id);
	}

	@PatchMapping(path = "/follower/{followerId}/toFollow/{toFollowId}")
	public Mono<ResponseEntity> followUser(@NotNull @PathVariable("followerId") String followerId, @NotNull @PathVariable("toFollowId") String toFollowId) {
		return this.userService.followUser(followerId, toFollowId);
	}

	@GetMapping(path = "/followers/{userId}")
	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getUserFollowers(@NotNull @PathVariable("userId") String userId) {
		return this.userService.getUserFollowers(userId);
	}
}
