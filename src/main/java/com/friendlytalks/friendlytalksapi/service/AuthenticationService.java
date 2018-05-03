package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.UserAlreadyExistsException;
import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.friendlytalks.friendlytalksapi.model.HttpResponseObject;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationRequest;
import com.friendlytalks.friendlytalksapi.security.JwtAuthenticationResponse;
import com.friendlytalks.friendlytalksapi.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
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

	public Mono<ResponseEntity<HttpResponseObject<User>>> signUp(User user) {
		user.setPassword(passwordEncryptor.encode(user.getPassword()));

		try {
			return this.userRepository.insert(user).map(savedUser -> ok()
							.contentType(MediaType.APPLICATION_JSON_UTF8)
							.body(new HttpResponseObject<>(savedUser)));
		} catch (RuntimeException e) {
			throw new UserAlreadyExistsException(ErrorMessages.USER_ALREADY_EXISTS);
		}
	}

	public Mono<ResponseEntity<JwtAuthenticationResponse>> signIn(JwtAuthenticationRequest authenticationRequest) {
		return this.userRepository.findByUsername(authenticationRequest.getUsername())
						.map(user -> ok()
										.contentType(MediaType.APPLICATION_JSON_UTF8)
										.body(new JwtAuthenticationResponse(this.jwtTokenUtil.generateToken(user), user.getUsername()))
						)
						.defaultIfEmpty(notFound().build());
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

