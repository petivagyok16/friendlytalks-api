package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * When a user tries to authenticate (login) this service is used inside
 * @AuthenticationManagerBuilder and retrieves a User object for SpringBoot Security, checks the password etc.
 *
 * Important note that the User constructor here is not the User we use throughout the application, but a User object
 * user only inside the security framework. It holds only username and password.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired private UserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) throw new UsernameNotFoundException("Username is empty");

		Optional<com.friendlytalks.friendlytalksapi.model.User> applicationUser = applicationUserRepository.findByUsername(username);

		if (!applicationUser.isPresent()) {
			throw new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND + " - " + username);
		}
		return new User(applicationUser.get().getUsername(), applicationUser.get().getPassword(), emptyList());
	}
}
