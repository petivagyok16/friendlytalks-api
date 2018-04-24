package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.UserAlreadyExistsException;
import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;
import com.friendlytalks.friendlytalksapi.exceptions.WrongCredentialsException;
import com.friendlytalks.friendlytalksapi.model.Credentials;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

	private final BCryptPasswordEncoder passwordEncryptor;
	private final UserRepository userRepository;

	@Autowired
	public AuthenticationService(BCryptPasswordEncoder passwordEncryptor, UserRepository userRepository) {
		this.passwordEncryptor = passwordEncryptor;
		this.userRepository = userRepository;
	}

	public void signUp(User user) {
		user.setPassword(passwordEncryptor.encode(user.getPassword()));

		try {
			this.userRepository.insert(user);
		} catch (RuntimeException e) {
			throw new UserAlreadyExistsException(ErrorMessages.USER_ALREADY_EXISTS);
		}
	}

	public Mono<User> signIn(Credentials credentials) {
		User user = this.userRepository.findByUsername(credentials.getUsername()).block();

		if (user != null && this.checkPassword(credentials.getPassword(), user.getPassword())) {
			return Mono.just(user);
		} else {
			throw new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
		}
	}

	public Mono<User> getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User authenticatedUser = this.userRepository.findByUsername(auth.getName()).block();

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

