package com.friendlytalks.friendlytalksapi.security;

class SecurityConstants {
	static final String SECRET = "SecretKeyToGenJWTs";
	static final int EXPIRATION_TIME = 864_000_000; // 10 days
	static final String TOKEN_PREFIX = "Bearer";
	static final String TOKEN_HEADER = "Authorization";
	static final String ISSUER = "friendlytalksapi";
	static final String TOKEN_PARAM= "token";
	static final String API_ROUTE = "/api/**";

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_AUDIENCE = "aud";
	static final String CLAIM_KEY_CREATED = "iat";

	static final String AUDIENCE_UNKNOWN = "unknown";
	static final String AUDIENCE_WEB = "http://localhost:4200";
	static final String AUDIENCE_MOBILE = "mobile";
	static final String AUDIENCE_TABLET = "tablet";
}
