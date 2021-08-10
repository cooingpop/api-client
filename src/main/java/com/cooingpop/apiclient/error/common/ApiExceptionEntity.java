/*
 * @(#) ApiExceptionEntity.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.common;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 박준영
 **/
@Getter
@ToString
public class ApiExceptionEntity {
	private String errorCode;
	private String errorMessage;

	@Builder
	public ApiExceptionEntity(HttpStatus status, String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
