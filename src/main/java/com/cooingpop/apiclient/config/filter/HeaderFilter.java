/*
 * @(#) HeaderFilter.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 박준영
 **/
public class HeaderFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "GET, POST");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader(
			"Access-Control-Allow-Headers",
			"X-Requested-With, Content-Type, Authorization, X-XSRF-token"
		);
		res.setHeader("Access-Control-Allow-Credentials", "false");

		chain.doFilter(request, response);
	}
}
