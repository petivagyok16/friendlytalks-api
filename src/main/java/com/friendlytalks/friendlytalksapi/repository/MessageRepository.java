package com.friendlytalks.friendlytalksapi.repository;

import com.friendlytalks.friendlytalksapi.model.Message;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

	// "{ 'userId' : { $in : ?0 } }" --> search messages by their userId property by looking at the first parameter (?0) of the method (Iterable<String> userId)
	// $in indicates that the method parameter is an Iterable/Collection and mongo needs to iterate over it
	@Query("{ 'userId' : { $in : ?0 } }")
	Flux<Message> findAllMessageByUserId(Iterable<String> userId);

}
