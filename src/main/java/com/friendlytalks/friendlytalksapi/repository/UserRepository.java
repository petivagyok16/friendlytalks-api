package com.friendlytalks.friendlytalksapi.repository;

import com.friendlytalks.friendlytalksapi.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	Mono<User> findByUsername(String username);

}
