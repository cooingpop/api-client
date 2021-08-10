/*
 * @(#) JwtTokenInterceptor.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cooingpop.apiclient.common.AuthConstants;
import com.cooingpop.apiclient.util.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
	@Override public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws
		IOException {
		final String header = request.getHeader(AuthConstants.AUTH_HEADER);
		if (header != null) {
			final String token = JwtTokenUtil.getTokenFromHeader(header);
			if (JwtTokenUtil.validToken(token)) { return true; }
		}
		response.sendRedirect("/error/unauthorized");

		return false;
	}
}
