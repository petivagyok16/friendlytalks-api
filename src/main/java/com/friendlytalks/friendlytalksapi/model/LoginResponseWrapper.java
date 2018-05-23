package com.friendlytalks.friendlytalksapi.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponseWrapper<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private T payload;
	private String token;

	public LoginResponseWrapper(T payload, String token) {
		this.payload = payload;
		this.token = token;
	}
}
