/*
 * @(#) SecurityConfig.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cooingpop.apiclient.config.filter.HeaderFilter;
import com.cooingpop.apiclient.config.interceptor.JwtTokenInterceptor;

/**
 *
 * @author 박준영
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtTokenInterceptor())
			.addPathPatterns("/*/api/*")
			.excludePathPatterns("/*/api/user/signup")
			.excludePathPatterns("/*/api/user/login")
			.excludePathPatterns("/*/api/user/logout");
	}

	@Bean
	public FilterRegistrationBean<HeaderFilter> getFilterRegistrationBean() {
		FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>(createHeaderFilter());
		registrationBean.setOrder(Integer.MIN_VALUE);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Bean
	public HeaderFilter createHeaderFilter() {
		return new HeaderFilter();
	}

	@Bean
	public JwtTokenInterceptor jwtTokenInterceptor() {
		return new JwtTokenInterceptor();
	}
}
