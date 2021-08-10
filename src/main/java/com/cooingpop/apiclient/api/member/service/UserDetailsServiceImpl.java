/*
 * @(#) UserDetailsServiceImpl.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cooingpop.apiclient.api.member.domain.ClientUserDetails;
import com.cooingpop.apiclient.api.member.repository.UserRepository;
import com.cooingpop.apiclient.error.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @author 박준영
 **/

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
			.map(m -> new ClientUserDetails(m, Collections.singleton(new SimpleGrantedAuthority(m.getRole().getValue()))))
			.orElseThrow(() -> new UserNotFoundException(email));
	}
}
