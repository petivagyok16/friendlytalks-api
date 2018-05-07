package com.friendlytalks.friendlytalksapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
public class HttpResponseObject<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private T payload;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String token;

	public HttpResponseObject(T payload) {
		this.payload = payload;
	}

	public HttpResponseObject(T payload, String token) {
		this.payload = payload;
		this.token = token;
	}
}
