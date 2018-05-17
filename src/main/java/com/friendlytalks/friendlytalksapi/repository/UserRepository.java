package com.friendlytalks.friendlytalksapi.repository;

import com.friendlytalks.friendlytalksapi.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	Mono<UserDetails> findByUsername(String username);

	Mono<User> findUserByUsername(String username);

	@Query("{'username' : { $regex: ?0 } }")
	Flux<User> findUsersByRegexpUsername(String regex);

	@Query("{'messages' : ?0}")
	Mono<User> findUserByMessage(String messageId);
}
