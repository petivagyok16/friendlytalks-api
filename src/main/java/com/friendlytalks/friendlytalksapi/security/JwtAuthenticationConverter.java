package com.friendlytalks.friendlytalksapi.security;

import com.friendlytalks.friendlytalksapi.exceptions.GetUsernameFromTokenException;
import com.friendlytalks.friendlytalksapi.exceptions.InvalidTokenException;
import com.friendlytalks.friendlytalksapi.service.ReactiveUserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
public class JwtAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

	private final ReactiveUserDetailsServiceImpl userDetailsService;
	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public JwtAuthenticationConverter(ReactiveUserDetailsServiceImpl userDetailsService, JwtTokenUtil jwtTokenUtil) {
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
					return Mono.error(new GetUsernameFromTokenException("An error occurred during validating the token!"));
				} catch (Exception e) {
					return Mono.error(new InvalidTokenException("Token is expired!"));
				}
			} else {
				log.warn("couldn't find bearer string, will ignore the header");
				return Mono.error(new InvalidTokenException("You can't access the requested resource due to invalid or missing authentication token!"));
			}

			log.info("checking authentication for user " + username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				return Mono.just(new JwtPreAuthenticationToken(authToken, bearerRequestHeader, username));
			}

			return Mono.empty();
		} catch (Exception e) {
			throw new InvalidTokenException("Invalid token...");
		}
	}
}
