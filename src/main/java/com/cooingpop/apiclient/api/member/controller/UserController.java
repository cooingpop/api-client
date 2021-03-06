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
 * @author ?????????
 **/
@Tag(name = "user", description = "?????? API")
@RequiredArgsConstructor
@RequestMapping(value ="/v1")
@RestController
@Slf4j
public class UserController {
	private final UserService userService;
	private final PaymentService paymentService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Operation(summary = "????????????", description = "?????? ??????????????? ???????????? ?????????????????????.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "???????????? ??????"),
		@ApiResponse(responseCode = "400", description = "?????? ????????? ????????? ????????? ????????????.")})
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

	@Operation(summary = "?????????", description = "???????????? ???????????? ??????????????????.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "????????? ??????"),
		@ApiResponse(responseCode = "400", description = "????????? ??????")})
	@PostMapping(value = "/api/user/login", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<JwtTokenResponse>> login(@Schema(implementation = LogInRequestDTO.class) @RequestBody final LogInRequestDTO logInRequestDTO) {
		try {
			User user = userService.logIn(logInRequestDTO.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("???????????? ?????? E-MAIL ?????????."));

			if (!passwordEncoder.matches(logInRequestDTO.getPwd(), user.getPwd())) {
				throw new IllegalArgumentException("????????? ?????????????????????.");
			}

			String accessToken = JwtTokenUtil.generateAccessToken(user);

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.LOGIN_SUCCESS, JwtTokenResponse
				.builder().access_token(accessToken).expired_at(JwtTokenUtil.getTokenExpiredAt(accessToken)).build()), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("login Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.LOGIN_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "????????????", description = "?????????????????????.")
	@ApiResponse(responseCode = "200", description = "???????????? ??????")
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

	@Operation(summary = "?????? ?????? ?????? ?????? ??????", description = "?????? ???????????? ?????? ?????? ?????? ?????? ???????????????.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "?????? ?????? ?????? ?????? ?????? ??????"),
		@ApiResponse(responseCode = "204", description = "?????? ?????? ?????? ?????? ?????? ?????? ??????"),
		@ApiResponse(responseCode = "400", description = "?????? ?????? ?????? ?????? ?????? ??????")})
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

	@Operation(summary = "?????? ????????? ???????????? ??????", description = "?????? ????????? ??????????????? ???????????????.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "?????? ????????? ???????????? ?????? ??????"),
		@ApiResponse(responseCode = "400", description = "?????? ????????? ???????????? ?????? ??????")})
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

	@Operation(summary = "?????? ?????? ?????? ??????", description = "?????? ?????? ????????? ???????????????.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "?????? ?????? ?????? ?????? ??????"),
		@ApiResponse(responseCode = "400", description = "?????? ?????? ?????? ?????? ??????")})
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
