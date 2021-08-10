/*
 * @(#) AuthConstants.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author 박준영
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {
	public static final String AUTH_HEADER = "Authorization";
	public static final String TOKEN_TYPE = "BEARER";
}
