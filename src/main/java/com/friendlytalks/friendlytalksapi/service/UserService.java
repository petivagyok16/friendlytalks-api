package com.friendlytalks.friendlytalksapi.service;

import com.friendlytalks.friendlytalksapi.model.HttpResponseWrapper;
import com.friendlytalks.friendlytalksapi.model.User;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.xml.ws.Response;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getAllUser() {
		return this.userRepository.findAll().collectList()
						.filter(users -> users.size() > 0)
						.map(users -> ResponseEntity.ok(new HttpResponseWrapper<>(users)))
						.defaultIfEmpty(ResponseEntity.noContent().build());
	}
}
