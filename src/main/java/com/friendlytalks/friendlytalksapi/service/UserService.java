package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.exceptions.WrongCredentialsException;
import com.friendlytalks.friendlytalksapi.model.Credentials;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService {

//	final Logger logger = LoggerFactory.getLogger(UserService.class);

	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	@Autowired private UserRepository userRepository;

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public void signUp(User user) {
		user.setPassword(passwordEncryptor.encryptPassword(user.getPassword()));
		this.userRepository.insert(user);
	}

	public User signIn(Credentials credentials) {
		Optional<User> user = this.userRepository.findByUsername(credentials.getUsername());

		if (user.isPresent() && this.checkPassword(credentials.getPassword(), user.get().getPassword())) {
			return user.get();
		} else {
			throw new WrongCredentialsException("Wrong username or password!");
		}
	}

	private boolean checkPassword(String inputPassword, String encryptedPassword) {
		return this.passwordEncryptor.checkPassword(inputPassword, encryptedPassword);
	}
}
