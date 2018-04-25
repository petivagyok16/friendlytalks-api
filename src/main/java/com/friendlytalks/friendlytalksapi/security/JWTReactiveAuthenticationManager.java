package com.friendlytalks.friendlytalksapi.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * An authentication manager intended to authenticate a JWT exchange
 * JWT tokens contain all information within the token itself
 * so an authentication manager is not necessary but we provide this
 * implementation to follow a standard.
 * Invalid tokens are filtered one previous step
 */
public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager {

	/**
	 * Successfully authenticate an Authentication object
	 *
	 * @param authentication A valid authentication object
	 * @return authentication A valid authentication object
	 */
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication);
	}
}
