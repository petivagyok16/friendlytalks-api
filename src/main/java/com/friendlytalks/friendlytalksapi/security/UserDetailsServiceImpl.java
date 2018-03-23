package com.friendlytalks.friendlytalksapi.security;

import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired private UserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<com.friendlytalks.friendlytalksapi.model.User> applicationUser = applicationUserRepository.findByUsername(username);
		if (!applicationUser.isPresent()) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.get().getUsername(), applicationUser.get().getPassword(), emptyList());
	}
}
