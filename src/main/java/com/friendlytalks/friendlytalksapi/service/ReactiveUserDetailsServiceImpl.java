package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import static java.util.Collections.emptyList;

/**
 * When a user tries to authenticate (sign in) this service is used inside
 * @AuthenticationManagerBuilder and retrieves a User object for SpringBoot Security, checks the password etc.
 *
 * Important note that the User constructor here is not the User we use throughout the application, but a User object
 * user only inside the security framework. It holds only username and password.
 */
@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

	private final UserRepository applicationUserRepository;

	@Autowired
	public ReactiveUserDetailsServiceImpl(UserRepository applicationUserRepository) {
		this.applicationUserRepository = applicationUserRepository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) throw new UsernameNotFoundException("Username is empty");

		UserDetails applicationUser = this.applicationUserRepository.findByUsername(username).block();

		if (applicationUser == null) {
			throw new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND + " - " + username);
		}
		return Mono.just(new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList()));
	}

}
