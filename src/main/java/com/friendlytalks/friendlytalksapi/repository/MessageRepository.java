package com.friendlytalks.friendlytalksapi.repository;

import com.friendlytalks.friendlytalksapi.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

}
