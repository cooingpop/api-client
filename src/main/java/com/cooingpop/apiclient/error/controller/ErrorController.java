/*
 * @(#) ErrorController.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.error.common.ExceptionEnum;
import com.cooingpop.apiclient.error.exception.ApiException;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 오류 관리하는 컨트롤러
 * @author 박준영
 **/
@RestController
@RequestMapping(value = "/error")
public class ErrorController {
	@Operation(summary = "인증 오류", description = "인증 오류를 반환합니다.")
	@GetMapping(value = "/unauthorized")
	public String unauthorized() {
		throw new AccessDeniedException(ExceptionEnum.SECURITY_01.getMessage());
	}

	@Operation(summary = "유효하지 않은 토큰", description = "유효하지 않은 토큰입니다. 오류를 반환합니다.")
	@GetMapping(value = "/invalid-token")
	public String invalidToken() {
		throw new ApiException(ExceptionEnum.INVALID_TOKEN);
	}
}
