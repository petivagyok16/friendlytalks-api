package com.friendlytalks.friendlytalksapi.security;

import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.Assert;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ReactiveWebSecurity {
	private ServerAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

	private final ReactiveUserDetailsService reactiveUserDetailsService;
	private final CustomAuthenticationConverter customAuthenticationConverter;


	@Autowired
	public ReactiveWebSecurity(
					ReactiveUserDetailsService reactiveUserDetailsService,
					UserRepository userRepository,
					CustomAuthenticationConverter customAuthenticationConverter
	) {
		Assert.notNull(reactiveUserDetailsService, "userDetailsService cannot be null");
		Assert.notNull(customAuthenticationConverter, "customAuthenticationConverter cannot be null");
		this.reactiveUserDetailsService = reactiveUserDetailsService;
		this.customAuthenticationConverter = customAuthenticationConverter;

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

				// Disable default security.
				http.httpBasic().disable();
				http.formLogin().disable();
				http.csrf().disable();
				http.logout().disable();

				// Add custom security.
				http.authenticationManager(authenticationManager());

				// Disable authentication for `/resources/**` routes.
				http.authorizeExchange().pathMatchers("/resources/**").permitAll();
				http.authorizeExchange().pathMatchers("/webjars/**").permitAll();

				//Disable authentication for `/test/**` routes.
				http.authorizeExchange().pathMatchers("/test/**").permitAll();

				// Disable authentication for `/auth/**` routes.
				http.authorizeExchange().pathMatchers("/auth/**").permitAll();

				http.securityContextRepository(securityContextRepository());

				http.authorizeExchange().anyExchange().authenticated();
				//.and().httpBasic().disable();

				http.addFilterAt(apiAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
				// .httpBasic().disable().csrf().disable();

				return http.build();
	}

	private ReactiveAuthenticationManager authenticationManager() {
		CustomReactiveAuthenticationManager customReactiveAuthenticationManager =  new CustomReactiveAuthenticationManager(this.reactiveUserDetailsService);
		return customReactiveAuthenticationManager;
	}

	private AuthenticationWebFilter apiAuthenticationWebFilter() {
		try {
			AuthenticationWebFilter apiAuthenticationWebFilter = new AuthenticationWebFilter(authenticationManager());
			apiAuthenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(this.entryPoint));
			apiAuthenticationWebFilter.setAuthenticationConverter(this.customAuthenticationConverter);
			apiAuthenticationWebFilter.setRequiresAuthenticationMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"));

			// Setting the Context Repo helped, not sure if I need this
			apiAuthenticationWebFilter.setSecurityContextRepository(securityContextRepository());

			return apiAuthenticationWebFilter;
		} catch (Exception e) {
			throw new BeanInitializationException("Could not initialize AuthenticationWebFilter apiAuthenticationWebFilter.", e);
		}
	}

	@Bean
	public WebSessionServerSecurityContextRepository securityContextRepository() {
		return new WebSessionServerSecurityContextRepository();
	}

	@Bean
	@Primary
	public PasswordEncoder encoder() {
		return new CustomPasswordEncoder();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
