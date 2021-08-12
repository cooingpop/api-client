/*
 * @(#) CustomAuthenticationProvider.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cooingpop.apiclient.config.domain.ClientUserDetails;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security 인증절차
 * @author 박준영
 **/
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		// AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
		final String userEmail = token.getName();
		final String userPw = (String) token.getCredentials();
		// UserDetailsService를 통해 DB에서 아이디로 사용자 조회
		final ClientUserDetails clientUserDetails = (ClientUserDetails) userDetailsService.loadUserByUsername(userEmail);
		if (!passwordEncoder.matches(userPw, clientUserDetails.getPassword())) {
			throw new BadCredentialsException(clientUserDetails.getUsername() + "Invalid password");
		}

		return new UsernamePasswordAuthenticationToken(clientUserDetails, userPw, clientUserDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
