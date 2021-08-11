/*
 * @(#) UserController.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.api.member.dto.SearchRequestDTO;
import com.cooingpop.apiclient.api.member.dto.UserListResponseDTO;
import com.cooingpop.apiclient.api.member.dto.UserResponseDTO;
import com.cooingpop.apiclient.api.member.service.UserService;
import com.cooingpop.apiclient.api.order.dto.PaymentListResponseDTO;
import com.cooingpop.apiclient.api.order.service.PaymentService;
import com.cooingpop.apiclient.common.CustomResponse;
import com.cooingpop.apiclient.common.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
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
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
	private final UserService userService;
	private final PaymentService paymentService;

	@Operation(summary = "단일 회원 상세 정보 조회", description = "회원 이메일로 단일 회원 상세 정보 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "단일 회원 상세 정보 조회 성공"),
		@ApiResponse(responseCode = "204", description = "단일 회원 상세 정보 조회 결과 없음"),
		@ApiResponse(responseCode = "400", description = "단일 회원 상세 정보 조회 오류")})
	@GetMapping(value = "/search", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<UserResponseDTO>> findUserByEmail(@RequestBody final SearchRequestDTO searchRequestDTO) {
		try {
			Optional<User> user = userService.findUserByEmail(searchRequestDTO.getEmail());

			if (user.isPresent()) {
				final UserResponseDTO userResponseDTO = UserResponseDTO.builder()
					.email(user.get().getEmail())
					.name(user.get().getName())
					.nickname(user.get().getNickname())
					.mobile(user.get().getMobile())
					.gender(user.get().getGender())
					.role(user.get().getRole())
					.userSeq(user.get().getUserSeq()).build();

				return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, userResponseDTO), HttpStatus.OK);
			} else {
				return new ResponseEntity(CustomResponse.res(HttpStatus.NO_CONTENT.value(), ResponseMessage.USER_NOT_FOUND), HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("findUserByEmail Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SEARCH_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "여러 회원 목록 조회", description = "여러 회원 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "여러 회원 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "단일 회원 상세 정보 조회 오류")})
	@GetMapping(value = "/list", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<UserListResponseDTO>> findAllUser() {
		try {
			final UserListResponseDTO userListResponseDTO = UserListResponseDTO.builder()
				.userList(userService.findAll()).build();

			return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, userListResponseDTO), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("findAllUser Exception" + ex.getMessage(), ex);
			return new ResponseEntity(CustomResponse.res(HttpStatus.BAD_REQUEST.value(), ResponseMessage.SEARCH_FAIL, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "단일 회원의 주문목록 조회", description = "단일 회원의 주문목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "단일 회원의 주문목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "단일 회원의 주문목록 조회 실패")})
	@GetMapping(value = "/{userSeq}/payments", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
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
}
