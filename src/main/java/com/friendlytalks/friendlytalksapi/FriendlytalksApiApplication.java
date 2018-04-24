package com.friendlytalks.friendlytalksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.friendlytalks.friendlytalksapi"} , exclude = JpaRepositoriesAutoConfiguration.class)
public class FriendlytalksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendlytalksApiApplication.class, args);
	}
}
