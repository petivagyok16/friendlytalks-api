package com.friendlytalks.friendlytalksapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendlytalks.friendlytalksapi.common.ErrorMessages;
import com.friendlytalks.friendlytalksapi.exceptions.UserNotFoundException;
import com.friendlytalks.friendlytalksapi.model.Credentials;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.mongodb.util.JSONParseException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


import static com.friendlytalks.friendlytalksapi.security.SecurityConstants.EXPIRATION_TIME;
import static com.friendlytalks.friendlytalksapi.security.SecurityConstants.SECRET;
import static com.friendlytalks.friendlytalksapi.security.SecurityConstants.HEADER_STRING;
import static com.friendlytalks.friendlytalksapi.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.setFilterProcessesUrl("/api/v1/auth/signin");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
																							HttpServletResponse res) throws AuthenticationException {
		try {
			Credentials credentials = new ObjectMapper()
							.readValue(req.getInputStream(), Credentials.class);

			return authenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(
											credentials.getUsername(),
											credentials.getPassword(),
											new ArrayList<>())
			);
		} catch (IOException | JSONParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
																					HttpServletResponse res,
																					FilterChain chain,
																					Authentication auth) throws IOException, ServletException {

		String username = ((User) auth.getPrincipal()).getUsername();

		String token = Jwts.builder()
						.setSubject(username)
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
						.compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

		Optional<com.friendlytalks.friendlytalksapi.model.User> user = this.userRepository.findByUsername(username);
		ServletOutputStream responseOutputStream = res.getOutputStream();

		try {
			if (user.isPresent()) {
				String JSONUser = new ObjectMapper().writeValueAsString(user.get());
				responseOutputStream.print(JSONUser);
			} else {
				throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
			}
		} finally {
			responseOutputStream.close();
		}
	}
}
