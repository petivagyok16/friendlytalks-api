package com.friendlytalks.friendlytalksapi.converters;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface Converter<S, T> {

	@Nullable
	T convert(S var1, T var2);
}
