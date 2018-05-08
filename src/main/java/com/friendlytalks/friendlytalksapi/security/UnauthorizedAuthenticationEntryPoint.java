package com.friendlytalks.friendlytalksapi.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Component
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
		return Mono.fromRunnable(() -> serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
	}
}
