package com.friendlytalks.friendlytalksapi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

@Component
@Slf4j
public class CustomAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

	private final ReactiveUserDetailsService userDetailsService;
	private final JwtTokenUtil jwtTokenUtil;

	public CustomAuthenticationConverter(ReactiveUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
		Assert.notNull(userDetailsService, "userDetailsService cannot be null");
		Assert.notNull(userDetailsService, "userDetailsService cannot be null");

		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public Mono<Authentication> apply(ServerWebExchange exchange) throws BadCredentialsException {
		ServerHttpRequest request = exchange.getRequest();
		try {

			Authentication authentication = null;
			String authToken = null;
			String username = null;

			String bearerRequestHeader = exchange.getRequest().getHeaders().getFirst(SecurityConstants.TOKEN_HEADER);

			if (bearerRequestHeader != null && bearerRequestHeader.startsWith(SecurityConstants.TOKEN_PREFIX + " ")) {
				authToken = bearerRequestHeader.substring(7);
			}

			if (authToken == null && request.getQueryParams() != null && !request.getQueryParams().isEmpty()) {
				String authTokenParam = request.getQueryParams().getFirst(SecurityConstants.TOKEN_PARAM);
				if (authTokenParam != null) authToken = authTokenParam;
			}

			if (authToken != null) {
				try {
					username = jwtTokenUtil.getUsernameFromToken(authToken);
				} catch (IllegalArgumentException e) {
					log.error("an error occured during getting username from token", e);
				} catch (Exception e) {
					log.warn("the token is expired and not valid anymore", e);
				}
			} else {
				log.warn("couldn't find bearer string, will ignore the header");
			}

			log.info("checking authentication for user " + username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (jwtTokenUtil.validateToken(authToken)) {
					log.info("authenticated user " + username + ", setting security context");
					final String token = authToken;
					return this.userDetailsService.findByUsername(username)
									.publishOn(Schedulers.parallel())
									.switchIfEmpty(Mono.error(new BadCredentialsException("Invalid Credentials")))
									.map(u -> new JwtAuthenticationToken(token, u.getUsername(), u.getAuthorities()));
				}
			}

			return Mono.just(authentication);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid token...");
		}
	}
}
