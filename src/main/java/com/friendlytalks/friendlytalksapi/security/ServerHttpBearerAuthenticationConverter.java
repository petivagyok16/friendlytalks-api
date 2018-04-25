package com.friendlytalks.friendlytalksapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * This converter extracts a bearer token from a WebExchange and
 * returns an Authentication object if the JWT token is valid.
 * Validity means is well formed and signature is correct
 */
public class ServerHttpBearerAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

	private static final String BEARER = "Bearer ";

	private String secret;

	@Autowired
	public ServerHttpBearerAuthenticationConverter(String secret) {
		this.secret = secret;
	}

	/**
	 * Apply this function to the current WebExchange, an Authentication object
	 * is returned when completed.
	 *
	 * @param serverWebExchange
	 * @return
	 */
	@Override
	public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
		Mono<Authentication> result = Mono.empty();
		String jwtPayload = extractJwtPayload(serverWebExchange);
		if (jwtPayload.length() > BEARER.length()) {
			String token = jwtPayload.substring(BEARER.length(), jwtPayload.length());
			try {
				String subject = getJwtVerifier().verify(token).getSubject();
				result = Mono.just(new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>()));
			} catch (JWTVerificationException e) {
				result = Mono.empty();
			}
		}
		return result;
	}

	private String extractJwtPayload(ServerWebExchange exchange) {
		String header = exchange
						.getRequest()
						.getHeaders()
						.getFirst(HttpHeaders.AUTHORIZATION);

		return header != null
						? header
						: "";
	}

	public JWTVerifier getJwtVerifier() {
		JWTVerifier verifier = null;

		try {
			verifier = JWT.require(Algorithm.HMAC512(secret)).build();
		} catch (UnsupportedEncodingException ignored) {
			// never happens
		}

		return verifier;
	}
}
