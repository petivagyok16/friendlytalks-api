package com.friendlytalks.friendlytalksapi.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

	public JwtAuthenticationWebFilter(final CustomReactiveAuthenticationManager authenticationManager,
																		final JwtAuthenticationConverter converter,
																		final UnauthorizedAuthenticationEntryPoint entryPoint) {
		super(authenticationManager);
		setAuthenticationConverter(converter);
		setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
		setRequiresAuthenticationMatcher(new JwtHeadersExchangeMatcher());
		setRequiresAuthenticationMatcher(new PathPatternParserServerWebExchangeMatcher(SecurityConstants.API_ROUTE));

	}

	private static class JwtHeadersExchangeMatcher implements ServerWebExchangeMatcher {

		@Override
		public Mono<MatchResult> matches(final ServerWebExchange exchange) {
			return Mono.just(exchange)
							.map(ServerWebExchange::getRequest)
							.map(ServerHttpRequest::getHeaders)
							.filter(h -> h.containsKey(SecurityConstants.TOKEN_HEADER))
							.flatMap($ -> MatchResult.match())
							.switchIfEmpty(MatchResult.notMatch());
		}
	}
}
