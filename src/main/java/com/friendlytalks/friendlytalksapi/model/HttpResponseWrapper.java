package com.friendlytalks.friendlytalksapi.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class HttpResponseWrapper<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private T payload;

	public HttpResponseWrapper(T payload) {
		this.payload = payload;
	}
}
