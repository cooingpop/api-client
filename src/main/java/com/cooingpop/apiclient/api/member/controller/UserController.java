/*
 * @(#) UserController.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.api.member.dto.LogInRequestDTO;
import com.cooingpop.apiclient.api.member.dto.SearchRequestDTO;
import com.cooingpop.apiclient.api.member.dto.SignUpRequestDTO;
import com.cooingpop.apiclient.api.member.dto.UserListResponseDTO;
import com.cooingpop.apiclient.api.member.dto.UserResponseDTO;
import com.cooingpop.apiclient.api.member.service.UserService;
import com.cooingpop.apiclient.api.order.dto.PaymentListResponseDTO;
import com.cooingpop.apiclient.api.order.service.PaymentService;
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
@Tag(name = "user", description = "회원 API")
@RequiredArgsConstructor
@RequestMapping(value ="/v1")
@RestController
@Slf4j
public class UserController {
	private final UserService userService;
	private final PaymentService paymentService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Operation(summary = "회원가입", description = "기본 회원정보를 이용하여 회원가입합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "이미 가입된 이메일 정보가 있습니다.")})
	@PostMapping(value = "/api/user/signup", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
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
	@PostMapping(value = "/api/user/login", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<JwtTokenResponse>> login(@Schema(implementation = LogInRequestDTO.class) @RequestBody final LogInRequestDTO logInRequestDTO) {
		try {
			User user = userService.logIn(logInRequestDTO.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

			if (!passwordEncoder.matches(logInRequestDTO.getPwd(), user.getPwd())) {
				throw new IllegalArgumentException("잘못된 비밀번호입니다.");
			}

			String accessToken = JwtTokenUtil.generateAccessToken(user);

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.LOGIN_SUCCESS, JwtTokenResponse
				.builder().access_token(accessToken).expired_at(JwtTokenUtil.getTokenExpiredAt(accessToken)).build()), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("login Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.LOGIN_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "로그아웃", description = "로그아웃합니다.")
	@ApiResponse(responseCode = "200", description = "로그아웃 성공")
	@PostMapping(value = "/api/user/logout", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<String>> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		try {
			if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
				JwtTokenBlockUtil.insertBlockToken(JwtTokenUtil.getTokenFromHeader(authorizationHeader));
			}

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.LOGOUT_SUCCESS), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("logout Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.LOGIN_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "단일 회원 상세 정보 조회", description = "회원 이메일로 단일 회원 상세 정보 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "단일 회원 상세 정보 조회 성공"),
		@ApiResponse(responseCode = "204", description = "단일 회원 상세 정보 조회 결과 없음"),
		@ApiResponse(responseCode = "400", description = "단일 회원 상세 정보 조회 오류")})
	@GetMapping(value = "/api/user", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<UserResponseDTO>> findUserByEmail(@RequestBody final SearchRequestDTO searchRequestDTO) {
		try {
			Optional<User> user = userService.findUserByEmail(searchRequestDTO.getEmail());
			if (user.isPresent()) {
				return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, UserResponseDTO.builder().user(user.get()).build()), HttpStatus.OK);
			} else {
				return new ResponseEntity(CustomResponse.res(HttpStatus.NO_CONTENT.value(), ResponseMessage.USER_NOT_FOUND), HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("findUserByEmail Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SEARCH_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "단일 회원의 주문목록 조회", description = "단일 회원의 주문목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "단일 회원의 주문목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "단일 회원의 주문목록 조회 실패")})
	@GetMapping(value = "/api/user/{userSeq}/payments", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<PaymentListResponseDTO>> getUserPayments(@PathVariable Long userSeq) {
		try {
			final PaymentListResponseDTO paymentListResponseDTO = PaymentListResponseDTO.builder()
				.paymentList(paymentService.findAllPaymentByUserSeq(userSeq))
				.build();

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, paymentListResponseDTO), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("getUserPayments Exception" + ex.getMessage(), ex);
			return new ResponseEntity(
				CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SEARCH_FAIL, ex.getMessage()),
				HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "여러 회원 목록 조회", description = "여러 회원 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "여러 회원 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "여러 회원 목록 조회 오류")})
	@GetMapping(value = "/api/users", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<CustomResponse<UserListResponseDTO>> findAllUser(@RequestBody(required = false) final SearchRequestDTO searchRequestDTO, Pageable pageable) {
		try {
			final UserListResponseDTO userListResponseDTO = UserListResponseDTO.builder()
				.users(userService.findAll(searchRequestDTO, pageable)).build();

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, userListResponseDTO), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("findAllUser Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SEARCH_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
