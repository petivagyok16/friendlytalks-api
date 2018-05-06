package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.UserAlreadyExistsException;
import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;

import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationRequest;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationResponse;
import com.friendlytalks.friendlytalksapi.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
						.flatMap(username -> this.userRepository.findUserByUsername(username))
						.defaultIfEmpty(new User())
						.flatMap(user -> {
							// TODO: refactor defaultEmpty() and flatMap() to a better solution
							return (user.getUsername() != null) ? Mono.just(true) : Mono.just(false);
						})
						.flatMap(exists -> {
							if (exists) {
								// TODO: If this exception throws, ResponseStatus is 500, not 403 as should be
								throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST, ErrorMessages.USER_ALREADY_EXISTS);
							}

							return this.userRepository.save(newUser).map(savedUser ->
											ResponseEntity.created(URI.create(String.format("users/%s", savedUser.getId()))).build());
						});
	}

	public Mono<ResponseEntity<JwtAuthenticationResponse>> signIn(JwtAuthenticationRequest authenticationRequest) {
		return this.userRepository.findByUsername(authenticationRequest.getUsername())
						.map(user -> ResponseEntity.ok()
										.contentType(MediaType.APPLICATION_JSON_UTF8)
										.body(new JwtAuthenticationResponse(this.jwtTokenUtil.generateToken(user), user.getUsername()))
						)
						.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	public Mono<UserDetails> getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserDetails authenticatedUser = this.userRepository.findByUsername(auth.getName()).block();

		if (authenticatedUser != null) {
			return Mono.just(authenticatedUser);
		} else {
			throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
		}
	}

	private boolean checkPassword(String inputPassword, String encryptedPassword) {
		return this.passwordEncryptor.matches(inputPassword, encryptedPassword);
	}
}

