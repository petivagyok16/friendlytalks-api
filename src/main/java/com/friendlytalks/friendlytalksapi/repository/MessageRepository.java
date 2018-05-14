package com.friendlytalks.friendlytalksapi.repository;

import com.friendlytalks.friendlytalksapi.model.Message;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

	@Query("{ 'userId' : ?0}")
	Flux<Message> findMessagesByUserId(Iterable userId);
}
