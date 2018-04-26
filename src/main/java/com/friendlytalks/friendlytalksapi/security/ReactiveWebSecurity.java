package com.friendlytalks.friendlytalksapi.security;

import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@EnableWebFluxSecurity
public class ReactiveWebSecurity {

	private ReactiveUserDetailsService reactiveUserDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserRepository userRepository;

	@Autowired
	public ReactiveWebSecurity(ReactiveUserDetailsService reactiveUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
		this.reactiveUserDetailsService = reactiveUserDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
	}

	/**
	 * For Spring Security webflux, a chain of filters will provide user authentication
	 * and authorization, we add custom filters to enable JWT token approach.
	 * This chain will authenticate with Basic HTTP authentication on the LOGIN_ROUTE path and
	 * authenticate with Bearer token authentication on the API_ROUTE path.
	 *
	 * @param http An initial object to build common filter scenarios.
	 *             Customized filters are added here.
	 * @return SecurityWebFilterChain A filter chain for web exchanges that will
	 * provide security
	 */
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
						.addFilterAt(basicToJwtAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
						.addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
						.authorizeExchange().pathMatchers(HttpMethod.GET, "/api/**").permitAll()
						.and()
						.authorizeExchange()
						.pathMatchers(HttpMethod.POST, SecurityConstants.SIGN_IN_URL)
						.authenticated()
						.pathMatchers(SecurityConstants.API_ROUTE)
						.authenticated()
						.and()
						.authorizeExchange()
						.anyExchange()
						.denyAll()
						.and()
						.csrf().disable()
						.build();
	}

	/**
	 * An {@link AuthenticationWebFilter} which authenticates the user with Http Basic authentication
	 * and issues a token afterwards.
	 *
	 * @return basic to JWT AuthenticationWebFilter
	 */
	@Bean
	protected AuthenticationWebFilter basicToJwtAuthenticationFilter() {
		AuthenticationWebFilter webFilter =
						new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(this.reactiveUserDetailsService));
		webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, SecurityConstants.SIGN_IN_URL));
		webFilter.setAuthenticationSuccessHandler(
						new WebFilterChainServerJWTAuthenticationSuccessHandler(
								SecurityConstants.SECRET,
								SecurityConstants.EXPIRATION_TIME,
								SecurityConstants.ISSUER
		));
		return webFilter;
	}

	/**
	 * An {@link AuthenticationWebFilter} which validates the user's Authorization token
	 * and gives access if the validation was successful.
	 *
	 * @return JWT AuthenticationWebFilter
	 */
	@Bean
	protected AuthenticationWebFilter jwtAuthenticationFilter() {
		AuthenticationWebFilter webFilter = new AuthenticationWebFilter(new JWTReactiveAuthenticationManager());
		webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(SecurityConstants.API_ROUTE));
		webFilter.setAuthenticationConverter(new ServerHttpBearerAuthenticationConverter(SecurityConstants.SECRET));
		return webFilter;
	}
}
