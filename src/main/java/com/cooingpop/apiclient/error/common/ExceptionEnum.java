package com.cooingpop.apiclient.error.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * @author 박준영
 **/
@Getter
public enum ExceptionEnum {
	RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name()),
	ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.name()),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name()),

	SECURITY_01(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.name(), "권한이 없습니다.");

	private final HttpStatus status;
	private final String code;
	private String message;

	ExceptionEnum(HttpStatus status, String code) {
		this.status = status;
		this.code = code;
	}

	ExceptionEnum(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
