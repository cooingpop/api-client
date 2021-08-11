/*
 * @(#) SecurityConfig.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.cooingpop.apiclient.common.JwtTokenBlockList;
import com.cooingpop.apiclient.config.hanlder.CustomLoginSuccessHandler;
import com.cooingpop.apiclient.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

/**
 * spring security 설정
 * @author 박준영
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) {
		// 정적 자원 Security 설정 안함
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		web.ignoring()
			.antMatchers(
				"/lib/**",
				"/v2/api-docs",
				"/swagger-resources/**",
				"/swagger**",
				"/webjars/**"
			);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.httpBasic().disable()	// security에서 기본으로 생성하는 login페이지 사용 안 함
			.csrf().disable()	// csrf 사용 안 함, REST API 사용하기 때문에
			.authorizeRequests().anyRequest().permitAll()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)	// 토큰을 활용하면 세션이 필요 없으므로 ALWAYS 설정하여 Session을 사용하지 않는다.
			.and()
			.formLogin().disable();
	}

	@Bean
	public CustomLoginSuccessHandler customLoginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Bean
	public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
		return new SimpleUrlLogoutSuccessHandler() {
			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
				super.onLogoutSuccess(request, response, authentication);
				JwtTokenBlockList.insertBlockToken(JwtTokenUtil.resolveToken(request));
			}
		};
	}

	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
		authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
	}
}
