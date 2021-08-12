/*
 * @(#) AuthController.java 2021. 08. 11.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.common.CustomResponse;
import com.cooingpop.apiclient.common.ResponseMessage;
import com.cooingpop.apiclient.util.JwtTokenBlockUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@Tag(name = "auth", description = "권한 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class AuthController {

	// 실제 서비스 환경에서는 제거
	@Operation(summary = "로그아웃한 토큰을 관리", description = "로그아웃한 토큰을 관리합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping(value = "/auth/tokens", headers = { "Content-type=application/json" }, produces = "application/json", consumes = "application/json")
	public ResponseEntity<CustomResponse<Map<String, Object>>> getTokenBlockList() {
		return new ResponseEntity(CustomResponse.res(HttpStatus.OK.value(), ResponseMessage.SEARCH_SUCCESS, JwtTokenBlockUtil.tokenBlockList), HttpStatus.OK);
	}
}
