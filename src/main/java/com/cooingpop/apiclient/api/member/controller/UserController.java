/*
 * @(#) UserController.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.api.member.dto.SearchDTO;
import com.cooingpop.apiclient.api.member.dto.SignInDTO;
import com.cooingpop.apiclient.api.member.dto.SignUpDTO;
import com.cooingpop.apiclient.api.member.dto.UserListResponseDTO;
import com.cooingpop.apiclient.api.member.dto.UserResponseDTO;
import com.cooingpop.apiclient.api.member.service.UserService;
import com.cooingpop.apiclient.common.CustomResponse;
import com.cooingpop.apiclient.common.CustomTokenResponse;
import com.cooingpop.apiclient.common.ResponseMessage;
import com.cooingpop.apiclient.util.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@Tag(name = "User", description = "회원")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Operation(summary = "회원가입", description = "기본 회원정보를 이용하여 회원가입합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "이미 가입된 이메일 정보가 있습니다.")})
	@PostMapping(value = "/signUp", headers = { "Content-type=application/json" })
	public ResponseEntity<String> signUp(@Schema(implementation = SignUpDTO.class) @RequestBody final SignUpDTO signUpDTO) {
		try {
			if (userService.duplicatedEmail(signUpDTO.getEmail()).orElse(false)) {
				return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.DUPLICATE_EMAIL), HttpStatus.BAD_REQUEST);
			} else {
				String accessToken = JwtTokenUtil.generateAccessToken(userService.signUp(signUpDTO));

				return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNUP_SUCCESS, CustomTokenResponse.builder().access_token(accessToken).expired_at(JwtTokenUtil.getTokenExpiredAt(accessToken)).build()) , HttpStatus.OK);
			}
		} catch (Exception ex) {
			log.error("signUp Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNUP_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "로그인", description = "이메일을 이용하여 로그인합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "로그인 실패")})
	@PostMapping(value = "/signIn", headers = { "Content-type=application/json" })
	public ResponseEntity<String> signIn(@RequestBody final SignInDTO signInDTO) {
		try {
			User user = userService.signIn(signInDTO)
				.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

			if (!passwordEncoder.matches(signInDTO.getPwd(), user.getPwd())) {
				throw new IllegalArgumentException("잘못된 비밀번호입니다.");
			}

			String accessToken = JwtTokenUtil.generateAccessToken(user);

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNIN_SUCCESS,
				CustomTokenResponse.builder().access_token(accessToken).expired_at(JwtTokenUtil.getTokenExpiredAt(accessToken)).build()), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("signIn Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNIN_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "단일 회원 상세 정보 조회", description = "회원 이메일로 단일 회원 상세 정보 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "단일 회원 상세 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
		@ApiResponse(responseCode = "400", description = "단일 회원 상세 정보 조회 오류")})
	@GetMapping(value = "/search", headers = { "Content-type=application/json" })
	public ResponseEntity<UserResponseDTO> findUserByEmail(@RequestBody final SearchDTO searchDTO) {
		try {
			final UserResponseDTO userResponseDTO = UserResponseDTO.builder().user(userService.findUserByEmail(searchDTO)).build();

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNIN_SUCCESS, userResponseDTO), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("findUserByEmail Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNIN_SUCCESS, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "여러 회원 목록 조회", description = "여러 회원 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "여러 회원 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserListResponseDTO.class))),
		@ApiResponse(responseCode = "400", description = "단일 회원 상세 정보 조회 오류")})
	@GetMapping(value = "/list", headers = { "Content-type=application/json" })
	public ResponseEntity<UserListResponseDTO> findAllUser() {
		try {
			final UserListResponseDTO userListResponseDTO = UserListResponseDTO.builder()
				.userList(userService.findAll()).build();

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNIN_SUCCESS, userListResponseDTO), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("findAllUser Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNIN_SUCCESS, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
