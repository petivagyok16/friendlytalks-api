package com.friendlytalks.friendlytalksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FriendlytalksApiApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FriendlytalksApiApplication.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}
}
