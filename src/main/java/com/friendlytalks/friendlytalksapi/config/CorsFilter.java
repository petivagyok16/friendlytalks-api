package com.friendlytalks.friendlytalksapi.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorsFilter implements WebFilter {

	@Override
	public Mono<Void> filter(final ServerWebExchange serverWebExchange, final WebFilterChain webFilterChain) {
		// Adapted from https://sandstorm.de/de/blog/post/cors-headers-for-spring-boot-kotlin-webflux-reactor-project.html
		serverWebExchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
		serverWebExchange.getResponse().getHeaders().add("Access-Control-Allow-Credentials", "true");
		serverWebExchange.getResponse().getHeaders().add("Access-Control-Allow-Methods",
						"GET, PUT, POST, PATCH, DELETE, OPTIONS");
		serverWebExchange.getResponse().getHeaders().add("Access-Control-Allow-Headers",
						"Access-Control-Allow-Origin, Authorization, Access-Control-Allow-Credentials, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method");
		if (serverWebExchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
			serverWebExchange.getResponse().getHeaders().add("Access-Control-Max-Age", "1728000");
			serverWebExchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
			return Mono.empty();
		} else {
			serverWebExchange.getResponse().getHeaders().add("Access-Control-Expose-Headers",
							"DNT, Access-Control-Allow-Origin, X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With," +
											"If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range");
			return webFilterChain.filter(serverWebExchange);
		}
	}
}
