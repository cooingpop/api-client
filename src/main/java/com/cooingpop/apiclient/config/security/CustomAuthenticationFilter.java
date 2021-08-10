/*
 * @(#) CustomAuthenticationFilter.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.error.exception.InputNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 박준영
 **/
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws
		AuthenticationException {
		final UsernamePasswordAuthenticationToken authRequest;
		try{
			final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			authRequest = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPwd());
		} catch (IOException exception){
			throw new InputNotFoundException(exception);
		}

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
