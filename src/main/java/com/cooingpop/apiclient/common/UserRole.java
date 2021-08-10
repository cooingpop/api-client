package com.cooingpop.apiclient.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 박준영
 **/

@RequiredArgsConstructor
@Getter
public enum UserRole {
	ROLE_USER("ROLE_USER"),
	ROLE_ADMIN("ROLE_ADMIN");

	private final String value;
}
