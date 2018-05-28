package com.friendlytalks.friendlytalksapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ReactiveWebSecurity {

	private ServerAuthenticationEntryPoint entryPoint = new UnauthorizedAuthenticationEntryPoint();
	private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;

	private static final String[] AUTH_WHITELIST = {
					"/resources/**",
					"/webjars/**",
					"/auth/**"
	 };


	@Autowired
	public ReactiveWebSecurity(JwtAuthenticationWebFilter jwtAuthenticationWebFilter) {
		this.jwtAuthenticationWebFilter = jwtAuthenticationWebFilter;
	}

	/**
	 * For Spring Security webflux, a chain of filters will provide user authentication
	 * and authorization, we add custom filters to enable JWT token approach.
	 * This chain will authenticate with Basic HTTP authentication on the AUTH_ROUTE path and
	 * authenticate with Bearer token authentication on the API_ROUTE path.
	 *
	 * @param http An initial object to build common filter scenarios.
	 *             Customized filters are added here.
	 * @return SecurityWebFilterChain A filter chain for web exchanges that will
	 * provide security
	 */
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		// We must override AuthenticationEntryPoint because if AuthenticationWebFilter didn't kicked in
		// (i.e. there are no required headers) then default behavior is to display HttpBasicAuth,
		// so we just return unauthorized to override it.
		// Filter tries to authenticate each request if it contains required headers.
		// Finally, we disable all default security.
		http
			.exceptionHandling()
			.authenticationEntryPoint(entryPoint)
			.and()
			.addFilterAt(this.jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
			.authorizeExchange()
			.pathMatchers(HttpMethod.OPTIONS).permitAll()
			.pathMatchers(AUTH_WHITELIST).permitAll()
			.anyExchange().authenticated()
			.and()
			.httpBasic().disable()
			.formLogin().disable()
			.csrf().disable()
			.logout().disable();
		return http.build();
	}

}
