package com.friendlytalks.friendlytalksapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
						.addResourceLocations("/resources", "classpath:/static/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/auth/token");
	}

}
