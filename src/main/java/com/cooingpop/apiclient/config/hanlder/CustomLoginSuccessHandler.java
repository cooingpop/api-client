/*
 * @(#) CustomLoginSuccessHandler.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.hanlder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.cooingpop.apiclient.api.member.domain.ClientUserDetails;
import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.common.AuthConstants;
import com.cooingpop.apiclient.util.JwtTokenUtil;

/**
 * @author 박준영
 **/
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
		final Authentication authentication) {
		final User user = ((ClientUserDetails) authentication.getPrincipal()).getUser();
		final String token = JwtTokenUtil.generateAccessToken(user);
		response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
	}
}
