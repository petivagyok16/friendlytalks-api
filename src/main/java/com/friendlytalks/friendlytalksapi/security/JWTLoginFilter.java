package com.friendlytalks.friendlytalksapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendlytalks.friendlytalksapi.model.Credentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(
					HttpServletRequest req, HttpServletResponse res)
					throws AuthenticationException, IOException, ServletException {

		Credentials credentials = new ObjectMapper().readValue(req.getInputStream(), Credentials.class);

		return getAuthenticationManager().authenticate(
						new UsernamePasswordAuthenticationToken(
										credentials.getUsername(),
										credentials.getPassword(),
										Collections.emptyList()
						)
		);
	}

	@Override
	protected void successfulAuthentication(
					HttpServletRequest req,
					HttpServletResponse res, FilterChain chain,
					Authentication auth
	) throws IOException, ServletException {
		TokenAuthenticationService.addAuthentication(res, auth.getName());
	}
}
