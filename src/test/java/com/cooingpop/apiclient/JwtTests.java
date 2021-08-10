/*
 * @(#) JwtTests.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.common.UserRole;
import com.cooingpop.apiclient.util.JwtTokenUtil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidParameterException;

import io.jsonwebtoken.Claims;

/**
 * @author 박준영
 **/
@ExtendWith(SpringExtension.class)
public class JwtTests {
	@Test
	@DisplayName("토큰 정상 발급")
	void successTest() {
		// given
		// when
		User user = User.builder()
			.name("테스트")
			.nickname("테스트")
			.pwd("테스트")
			.mobile("01039993969")
			.email("cooingpop@gmail.com")
			.gender("남")
			.role(UserRole.ROLE_USER)
			.build();
		
		String username = "테스트";
		String accessToken = JwtTokenUtil.generateAccessToken(user);
		System.out.println(accessToken);

		Claims claims = JwtTokenUtil.getClaimsFormToken(accessToken);
		System.out.println("claims = " + claims);
		String findUsername = claims.get("name", String.class);

		// then
		assertThat(username).isEqualTo(JwtTokenUtil.getUserNameFromToken(accessToken));
		assertThat(username).isEqualTo(findUsername);
	}


	// @Test
	@DisplayName("토큰유효시간 over")
	void expireTokenTest() throws InterruptedException {
		// given

		User user = User.builder()
			.name("테스트")
			.nickname("테스트")
			.pwd("테스트")
			.mobile("01039993969")
			.email("cooingpop@gmail.com")
			.gender("남")
			.role(UserRole.ROLE_USER)
			.build();

		String accessToken = JwtTokenUtil.generateAccessToken(user);

		Thread.sleep(20000l);

		// when
		InvalidParameterException ex = assertThrows(
			InvalidParameterException.class
			, () -> JwtTokenUtil.getClaimsFormToken(accessToken));

		// then
		assertThat(ex.getMessage()).isEqualTo("유효하지 않은 토큰입니다");
	}
}
