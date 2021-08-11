/*
 * @(#) ApiExceptionAdvice.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooingpop.apiclient.error.common.ApiExceptionEntity;
import com.cooingpop.apiclient.error.common.ExceptionEnum;
import com.cooingpop.apiclient.error.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

/**
 * 공통 에러 처리
 * @author 박준영
 **/
@RestControllerAdvice
@Slf4j
public class ApiExceptionAdvice {
	@ExceptionHandler({ApiException.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final ApiException e) {
		log.error("ApiException " + e.getMessage(), e);
		return ResponseEntity
			.status(e.getExceptionEnum().getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(e.getExceptionEnum().getCode())
				.errorMessage(e.getExceptionEnum().getMessage())
				.build());
	}

	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
		log.error("RuntimeException " + e.getMessage(), e);
		return ResponseEntity
			.status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
				.errorMessage(e.getMessage())
				.build());
	}

	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {
		log.error("AccessDeniedException " + e.getMessage(), e);
		return ResponseEntity
			.status(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getCode())
				.errorMessage(e.getMessage())
				.build());
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final Exception e) {
		log.error("Exception " + e.getMessage(), e);
		return ResponseEntity
			.status(ExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
				.errorMessage(e.getMessage())
				.build());
	}
}
