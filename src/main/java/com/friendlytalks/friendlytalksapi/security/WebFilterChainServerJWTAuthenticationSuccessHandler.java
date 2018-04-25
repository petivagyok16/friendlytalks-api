package com.friendlytalks.friendlytalksapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * On success authentication a signed JWT object is serialized and added
 * in the authorization header as a bearer token
 */
public class WebFilterChainServerJWTAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
	private static final Logger LOG = LoggerFactory.getLogger(WebFilterChainServerJWTAuthenticationSuccessHandler.class);

	private static final String BEARER_PREFIX = "Bearer ";

	private String secret;
	private long expirationTime;
	private String issuer;

	public WebFilterChainServerJWTAuthenticationSuccessHandler(String secret, long expirationTime, String issuer) {
		this.secret = secret;
		this.expirationTime = expirationTime;
		this.issuer = issuer;
	}

	/**
	 * A successful authentication object is used to create a JWT object and
	 * added in the authorization header of the current WebExchange
	 *
	 * @param webFilterExchange
	 * @param authentication
	 * @return
	 */
	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		ServerWebExchange exchange = webFilterExchange.getExchange();
		exchange.getResponse()
						.getHeaders()
						.add(HttpHeaders.AUTHORIZATION, generateJwtToken(authentication));
		return webFilterExchange.getChain().filter(exchange);
	}

	private String generateJwtToken(Authentication authentication) {
		String jwtToken = null;
		try {
			jwtToken = JWT.create()
							.withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
							.withSubject(authentication.getName())
							.withIssuer(issuer)
							.withArrayClaim("auths", getPermissionsFrom(authentication))
							.sign(Algorithm.HMAC512(secret));
		} catch (UnsupportedEncodingException e) {
			LOG.error("Fatal error during JWT creation", e);
		}
		return BEARER_PREFIX + jwtToken;
	}

	private String[] getPermissionsFrom(Authentication authentication) {
		return authentication.getAuthorities()
						.stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList())
						.toArray(new String[authentication.getAuthorities().size()]);
	}
}
