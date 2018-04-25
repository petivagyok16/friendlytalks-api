package com.friendlytalks.friendlytalksapi;

import com.friendlytalks.friendlytalksapi.security.JWTReactiveAuthenticationManager;
import com.friendlytalks.friendlytalksapi.security.ServerHttpBearerAuthenticationConverter;
import com.friendlytalks.friendlytalksapi.security.WebFilterChainServerJWTAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@SpringBootApplication
@EnableWebFluxSecurity
public class FriendlytalksApiApplication {

	private static final String LOGIN_ROUTE = "/login";
	private static final String API_ROUTE = "/api/**";

	@Value("${DEFAULT_SECRET}")
	private String secret;

	@Value("${jwt.expiration_time}")
	private long expirationTime;

	@Value("${ISSUER}")
	private String issuer;


	public static void main(String[] args) {
		SpringApplication.run(FriendlytalksApiApplication.class, args);
	}

	/**
	 * A custom UserDetailsService to provide quick user rights for Spring Security,
	 * more formal implementations may be added as separated files and annotated as
	 * a Spring stereotype.
	 *
	 * @return MapReactiveUserDetailsService an InMemory implementation of user details
	 */
	@Bean
	public MapReactiveUserDetailsService userDetailsRepository() {
		UserDetails user = User.withDefaultPasswordEncoder()
						.username("user")
						.password("user")
						.roles("USER", "ADMIN")
						.build();
		return new MapReactiveUserDetailsService(user);
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
						.authorizeExchange()
						.pathMatchers(HttpMethod.POST, LOGIN_ROUTE)
						.authenticated()
						.pathMatchers(API_ROUTE)
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
	 * @return
	 */
	@Bean
	public AuthenticationWebFilter basicToJwtAuthenticationFilter() {
		AuthenticationWebFilter webFilter =
						new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepository()));
		webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, LOGIN_ROUTE));
		webFilter.setAuthenticationSuccessHandler(new WebFilterChainServerJWTAuthenticationSuccessHandler(secret, expirationTime, issuer));
		return webFilter;
	}

	/**
	 * An {@link AuthenticationWebFilter} which validates the user's Authorization token
	 * and gives access if the validation was successful.
	 *
	 * @return
	 */
	@Bean
	public AuthenticationWebFilter jwtAuthenticationFilter() {
		AuthenticationWebFilter webFilter = new AuthenticationWebFilter(new JWTReactiveAuthenticationManager());
		webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(API_ROUTE));
		webFilter.setAuthenticationConverter(new ServerHttpBearerAuthenticationConverter(secret));
		return webFilter;
	}
}
