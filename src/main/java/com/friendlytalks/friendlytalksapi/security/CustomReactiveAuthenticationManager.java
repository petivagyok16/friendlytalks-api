package com.friendlytalks.friendlytalksapi.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	public CustomReactiveAuthenticationManager() { }

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		final String username = authentication.getName();
		return Mono.just(authentication);
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
	}
}
