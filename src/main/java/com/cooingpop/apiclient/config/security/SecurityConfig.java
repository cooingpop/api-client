/*
 * @(#) SecurityConfig.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cooingpop.apiclient.config.filter.HeaderFilter;
import com.cooingpop.apiclient.config.hanlder.CustomLoginSuccessHandler;
import com.cooingpop.apiclient.config.interceptor.JwtTokenInterceptor;
import com.cooingpop.apiclient.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author 박준영
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
	private final UserDetailsService userDetailsService;
	private final JwtTokenUtil jwtTokenProvider;
	private static final String LOGOUT_URL = "/logout";

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
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable()	// security에서 기본으로 생성하는 login페이지 사용 안 함
			.csrf().disable()	// csrf 사용 안 함, REST API 사용하기 때문에
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	// 토큰을 활용하면 세션이 필요 없으므로 STATELESS로 설정하여 Session을 사용하지 않는다.
			.and()
			.formLogin().disable()
			.authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
			.anyRequest().permitAll()
			.and()
			.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

		http.logout().logoutUrl(LOGOUT_URL).permitAll().logoutSuccessHandler(logoutSuccessHandler());
	}

	@Bean
	public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/user/login");
		customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
		customAuthenticationFilter.afterPropertiesSet();
		return customAuthenticationFilter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return authenticationManager();
	}

	protected class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
		}
	}

	@Bean
	public PasswordEncoder servicePasswordEncoder() {
		return new ServicePasswordEncoder();
	}

	protected class JwtAuthenticationFilter extends GenericFilterBean {
		private JwtTokenUtil jwtTokenProvider;

		public JwtAuthenticationFilter(JwtTokenUtil jwtTokenProvider) {
			this.jwtTokenProvider = jwtTokenProvider;
		}

		// Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록
		@Override
		public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
			if (token != null && jwtTokenProvider.validToken(token)) { // token 검증
				Authentication auth = jwtTokenProvider.getAuthentication(token); // 인증 객체 생성
				SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 인증 객체 저장
			}
			filterChain.doFilter(servletRequest, servletResponse);
		}
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

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtTokenInterceptor())
			.addPathPatterns("/user/findAll");
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
