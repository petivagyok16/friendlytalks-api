package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.InvalidTokenException;
import com.friendlytalks.friendlytalksapi.exceptions.UserAlreadyExistsException;

import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;
import com.friendlytalks.friendlytalksapi.exceptions.WrongCredentialsException;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.friendlytalks.friendlytalksapi.security.CustomPasswordEncoder;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationRequest;
import com.friendlytalks.friendlytalksapi.security.JwtTokenUtil;
import com.friendlytalks.friendlytalksapi.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class AuthenticationService {

	private final CustomPasswordEncoder passwordEncryptor;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public AuthenticationService(
					CustomPasswordEncoder passwordEncryptor,
					UserRepository userRepository,
					JwtTokenUtil jwtTokenUtil
	) {
		this.passwordEncryptor = passwordEncryptor;
		this.userRepository = userRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	/**
	 * Signing up a new user.
	 *
	 * @param newUser
	 *            The user to create.
	 *
	 * @return HTTP 201, the header Location contains the URL of the created
	 *         user.
	 */
	public Mono<ResponseEntity> signUp(User newUser) {
		newUser.setPassword(passwordEncryptor.encode(newUser.getPassword()));

		return Mono.justOrEmpty(newUser.getUsername())
						.flatMap(this.userRepository::findUserByUsername)
						.defaultIfEmpty(new User())
						.flatMap(user -> {
							// TODO: refactor defaultEmpty() and flatMap() to a better solution
							return (user.getUsername() != null) ? Mono.just(true) : Mono.just(false);
						})
						.flatMap(exists -> {
							if (exists) {
								throw new UserAlreadyExistsException(ErrorMessages.USER_ALREADY_EXISTS);
							}

							return this.userRepository.save(newUser).map(savedUser ->
											ResponseEntity.created(URI.create(String.format("users/%s", savedUser.getId()))).build());
						});
	}

	/**
	 * Signing up a new user.
	 *
	 * @param authenticationRequest
	 *            Credentials to create authentication
	 *
	 * @return HTTP 200, with Jwt token and user information
	 */
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> signIn(JwtAuthenticationRequest authenticationRequest) {
		return this.userRepository.findUserByUsername(authenticationRequest.getUsername())
						.flatMap(user -> {

							if (this.passwordEncryptor.matches(authenticationRequest.getPassword(), user.getPassword())) {
								return Mono.just(
												ResponseEntity.ok()
																.contentType(MediaType.APPLICATION_JSON_UTF8)
																.body(new HttpResponseWrapper<>(user, this.jwtTokenUtil.generateToken(user)))
												);
							} else {
								throw new WrongCredentialsException("Invalid Credentials");
							}
						})
						.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	/**
	 * Getting the authenticated user by looking at the Jwt token
	 *
	 * @param bearerToken
	 *            Jwt token which contains some information about the user and can be used to find the user it belongs to.
	 *
	 * @return HTTP 200, with user information in payload
	 */
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getAuthenticatedUser(String bearerToken) {
		String username = this.validateToken(bearerToken);

		return this.userRepository.findUserByUsername(username)
						.defaultIfEmpty(new User())
						.flatMap(user -> {
							// TODO: refactor defaultEmpty() and flatMap() to a better solution
							if (user.getUsername() != null) {
								return Mono.just(ResponseEntity.ok().body(new HttpResponseWrapper<>(user)));
							} else {
								throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
							}
						});
	}

	/**
	 *
	 * @param bearerToken
	 * @return username if the Token is valid
	 */
	private String validateToken(String bearerToken) {
		String authToken;

		if (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX + " ")) {
			authToken = bearerToken.substring(7);
		} else {
			throw new InvalidTokenException(ErrorMessages.INVALID_TOKEN);
		}

		if (jwtTokenUtil.validateToken(authToken)) {
			return jwtTokenUtil.getUsernameFromToken(authToken);
		}

		return null;
	}
}

