package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService {
	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllUser() {
		return userRepository.findAll();
	}
}
