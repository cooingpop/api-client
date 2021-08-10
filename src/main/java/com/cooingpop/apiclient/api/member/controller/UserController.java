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

import com.cooingpop.apiclient.api.member.dto.SignInDTO;
import com.cooingpop.apiclient.api.member.dto.SignUpDTO;
import com.cooingpop.apiclient.api.member.dto.UserListResponseDTO;
import com.cooingpop.apiclient.api.member.model.User;
import com.cooingpop.apiclient.api.member.service.UserService;
import com.cooingpop.apiclient.common.CustomResponse;
import com.cooingpop.apiclient.common.ResponseMessage;
import com.cooingpop.apiclient.util.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@Api(tags = {"1. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	@ApiOperation(value="회원가입", notes = "회원가입을 합니다.")
	@PostMapping(value = "/signUp")
	public ResponseEntity<String> signUp(@RequestBody final SignUpDTO signUpDTO) {
		try {
			return userService.duplicatedEmail(signUpDTO.getEmail()).orElse(false) ? ResponseEntity.badRequest().build()
				: new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNUP_SUCCESS, JwtTokenUtil.generateAccessToken(userService.signUp(signUpDTO))), HttpStatus.OK);
			//ResponseEntity.ok(JwtTokenUtil.generateAccessToken(userService.signUp(signUpDTO)));
		} catch (Exception ex) {
			log.error("signUp Exception" + ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

	// 로그인
	@PostMapping("/signIn")
	public ResponseEntity<String> login(@RequestBody final SignInDTO signInDTO) {
		try {
			User user = userService.signIn(signInDTO)
				.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
			if (!passwordEncoder.matches(signInDTO.getPwd(), user.getPwd())) {
				throw new IllegalArgumentException("잘못된 비밀번호입니다.");
			}

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SIGNIN_SUCCESS, JwtTokenUtil.generateAccessToken(user)), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("signUp Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SIGNIN_SUCCESS, ex.getMessage()), HttpStatus.BAD_REQUEST);
			// return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(value = "/list")
	public ResponseEntity<UserListResponseDTO> findAll() {
		final UserListResponseDTO userListResponseDTO = UserListResponseDTO.builder()
			.userList(userService.findAll()).build();

		return ResponseEntity.ok(userListResponseDTO);
	}
}
