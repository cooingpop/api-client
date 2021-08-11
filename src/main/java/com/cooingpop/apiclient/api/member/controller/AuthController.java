/*
 * @(#) AuthController.java 2021. 08. 11.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.api.member.dto.SignInRequestDTO;
import com.cooingpop.apiclient.api.member.dto.SignUpRequestDTO;
import com.cooingpop.apiclient.api.member.service.UserService;
import com.cooingpop.apiclient.common.CustomResponse;
import com.cooingpop.apiclient.common.JwtTokenResponse;
import com.cooingpop.apiclient.common.ResponseMessage;
import com.cooingpop.apiclient.util.JwtTokenBlockUtil;
import com.cooingpop.apiclient.util.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@Tag(name = "auth", description = "권한 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Operation(summary = "회원가입", description = "기본 회원정보를 이용하여 회원가입합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "이미 가입된 이메일 정보가 있습니다.")})
	@PostMapping(value = "/signup", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<JwtTokenResponse>> signUp(@RequestBody final SignUpRequestDTO signUpRequestDTO) {
		try {
			if (userService.duplicatedEmail(signUpRequestDTO.getEmail()).orElse(0) > 0) {
				return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.DUPLICATE_EMAIL), HttpStatus.BAD_REQUEST);
			} else {
				String accessToken = JwtTokenUtil.generateAccessToken(userService.signUp(signUpRequestDTO));

				return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNUP_SUCCESS, JwtTokenResponse
					.builder().access_token(accessToken).expired_at(JwtTokenUtil.getTokenExpiredAt(accessToken)).build()) , HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("signup Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNUP_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "로그인", description = "이메일을 이용하여 로그인합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "400", description = "로그인 실패")})
	@PostMapping(value = "/signin", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<JwtTokenResponse>> signIn(@Schema(implementation = SignInRequestDTO.class) @RequestBody final SignInRequestDTO signInRequestDTO) {
		try {
			User user = userService.signIn(signInRequestDTO.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

			if (!passwordEncoder.matches(signInRequestDTO.getPwd(), user.getPwd())) {
				throw new IllegalArgumentException("잘못된 비밀번호입니다.");
			}

			String accessToken = JwtTokenUtil.generateAccessToken(user);

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNIN_SUCCESS, JwtTokenResponse
					.builder().access_token(accessToken).expired_at(JwtTokenUtil.getTokenExpiredAt(accessToken)).build()), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("signin Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNIN_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "로그아웃", description = "로그아웃합니다.")
	@ApiResponse(responseCode = "200", description = "로그아웃 성공")
	@GetMapping(value = "/signout", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<String>> signOut(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		try {
			if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
				JwtTokenBlockUtil.insertBlockToken(JwtTokenUtil.getTokenFromHeader(authorizationHeader));
			}

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNOUT_SUCCESS), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("signout Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNIN_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "로그아웃한 토큰을 관리", description = "로그아웃한 토큰을 관리합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping(value = "/token", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<Map<String, Object>>> getTokenBlockList() {
		return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, JwtTokenBlockUtil.tokenBlockList), HttpStatus.OK);
	}
}
