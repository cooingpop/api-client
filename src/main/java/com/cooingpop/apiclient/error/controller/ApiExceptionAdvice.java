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
import org.springframework.web.bind.annotation.RestController;

import com.cooingpop.apiclient.error.common.ApiExceptionEntity;
import com.cooingpop.apiclient.error.common.ExceptionEnum;
import com.cooingpop.apiclient.error.exception.ApiException;

/**
 * @author 박준영
 **/
@RestController
public class ApiExceptionAdvice {
	@ExceptionHandler({ApiException.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final ApiException e) {
		//e.printStackTrace();
		return ResponseEntity
			.status(e.getExceptionEnum().getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(e.getExceptionEnum().getCode())
				.errorMessage(e.getExceptionEnum().getMessage())
				.build());
	}

	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
		e.printStackTrace();
		return ResponseEntity
			.status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
				.errorMessage(e.getMessage())
				.build());
	}

	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {
		e.printStackTrace();
		return ResponseEntity
			.status(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getCode())
				.errorMessage(e.getMessage())
				.build());
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final Exception e) {
		e.printStackTrace();
		return ResponseEntity
			.status(ExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
			.body(ApiExceptionEntity.builder()
				.errorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
				.errorMessage(e.getMessage())
				.build());
	}
}
