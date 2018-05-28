package com.friendlytalks.friendlytalksapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	private static final String ALLOWED_HEADERS = "Access-Control-Allow-Origin, Authorization, Access-Control-Allow-Credentials, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method";
	private static final String ALLOWED_METHODS = "GET, PUT, POST, PATCH, DELETE, OPTIONS";
	private static final String ALLOWED_ORIGIN = "*";
	private static final Long MAX_AGE = 3600L;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
						.addResourceLocations("/resources", "classpath:/static/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
						.allowedOrigins(ALLOWED_ORIGIN)
						.allowedMethods(ALLOWED_METHODS)
						.allowedHeaders(ALLOWED_HEADERS)
						.maxAge(MAX_AGE);
	}

}
