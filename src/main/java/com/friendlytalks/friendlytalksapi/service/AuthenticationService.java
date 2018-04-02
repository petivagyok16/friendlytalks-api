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

import java.util.Optional;

@Service("authService")
public class AuthenticationService {

	private BCryptPasswordEncoder passwordEncryptor;
	private UserRepository userRepository;

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

	public User signIn(Credentials credentials) {
		Optional<User> user = this.userRepository.findByUsername(credentials.getUsername());

		if (user.isPresent() && this.checkPassword(credentials.getPassword(), user.get().getPassword())) {
			return user.get();
		} else {
			throw new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
		}
	}

	public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Optional<User> authenticatedUser = this.userRepository.findByUsername(auth.getName());

		if (authenticatedUser.isPresent()) {
			return authenticatedUser.get();
		} else {
			throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
		}
	}

	private boolean checkPassword(String inputPassword, String encryptedPassword) {
		return this.passwordEncryptor.matches(inputPassword, encryptedPassword);
	}
}

