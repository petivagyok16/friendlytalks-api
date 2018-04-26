package com.friendlytalks.friendlytalksapi.security;

public class SecurityConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/api/v1/auth/signup";
	public static final String SIGN_IN_URL = "/api/v1/auth/signin";
	public static final String API_ROUTE = "/api/**";
	public static final String ISSUER = "friendlytalksapi";
}
