package com.friendlytalks.friendlytalksapi.controller;

import com.friendlytalks.friendlytalksapi.model.EditedUser;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path = "/api/v1/user", produces = { APPLICATION_JSON_UTF8_VALUE })
@CrossOrigin(origins = "*")
@Validated
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

	@GetMapping(path = "/following/{userId}")
	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getUserFollowings(@NotNull @PathVariable("userId") String userId) {
		return this.userService.getUserFollowings(userId);
	}

	@GetMapping(path = "/following-messages/{userId}")
	public Mono<ResponseEntity<HttpResponseWrapper<List<Message>>>> getFollowingMessages(@NotNull @PathVariable("userId") String userId) {
		return this.userService.getFollowingMessages(userId);
	}

	@PatchMapping(path = "/edit/{userId}")
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> editUser(@NotNull @PathVariable("userId") String userId, @Valid @RequestBody EditedUser editedUser, @RequestHeader(value = "Authorization") String bearerToken) {
		return this.userService.editUser(userId, editedUser, bearerToken);
	}

	@GetMapping(path = "/find/{nameFragment}")
	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> findUser(@NotNull @PathVariable("nameFragment") String nameFragment) {
		return this.userService.findUser(nameFragment);
	}
}
