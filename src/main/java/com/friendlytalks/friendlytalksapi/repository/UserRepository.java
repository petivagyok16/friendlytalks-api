package com.friendlytalks.friendlytalksapi.repository;

import com.friendlytalks.friendlytalksapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, String> {

}
