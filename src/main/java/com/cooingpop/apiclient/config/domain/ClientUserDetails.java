/*
 * @(#) ClientUserDetails.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cooingpop.apiclient.api.member.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * @author 박준영
 **/
@RequiredArgsConstructor
@Getter
public class ClientUserDetails implements UserDetails {
	@Delegate
	private final User user;
	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPwd();
	}

	@Override
	public String getUsername() {
		return user.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return user.getIsEnable();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.getIsEnable();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return user.getIsEnable();
	}

	@Override
	public boolean isEnabled() {
		return user.getIsEnable();
	}

}