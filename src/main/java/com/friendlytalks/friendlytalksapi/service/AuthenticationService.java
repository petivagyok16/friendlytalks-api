package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.InvalidTokenException;
import com.friendlytalks.friendlytalksapi.exceptions.UserAlreadyExistsException;

import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;
import com.friendlytalks.friendlytalksapi.exceptions.WrongCredentialsException;
import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationRequest;
import com.friendlytalks.friendlytalksapi.security.JwtTokenUtil;
import com.friendlytalks.friendlytalksapi.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class AuthenticationService {

	private final BCryptPasswordEncoder passwordEncryptor;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public AuthenticationService(
					BCryptPasswordEncoder passwordEncryptor,
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
								// TODO: If this exception throws, ResponseStatus is 500, not 403 as it should be
								throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST, ErrorMessages.USER_ALREADY_EXISTS);
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

							if (this.checkPassword(authenticationRequest.getPassword(), user.getPassword())) {
								return Mono.just(
												ResponseEntity.ok()
																.contentType(MediaType.APPLICATION_JSON_UTF8)
																.body(new HttpResponseWrapper<>(user, this.jwtTokenUtil.generateToken(user)))
												);
							} else {
								// TODO: wrap this into ResponseEntity with proper HttpStatus and info
								throw new WrongCredentialsException("Wrong Credentials!");
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
		String username = null;
		String authToken;

		if (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX + " ")) {
			authToken = bearerToken.substring(7);
		} else {
			throw new InvalidTokenException("Invalid Token!");
		}

		if (jwtTokenUtil.validateToken(authToken)) {
			username = jwtTokenUtil.getUsernameFromToken(authToken);
		}

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
	 * @param inputPassword the password in the request
	 * @param encryptedPassword the password in the database
	 * @return true if passwords are matching
	 */
	private boolean checkPassword(String inputPassword, String encryptedPassword) {
		return this.passwordEncryptor.matches(inputPassword, encryptedPassword);
	}
}

