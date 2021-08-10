/*
 * @(#) ApiException.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.exception;

import com.cooingpop.apiclient.error.common.ExceptionEnum;

import lombok.Getter;

/**
 * @author 박준영
 **/
@Getter
public class ApiException extends RuntimeException {
	private ExceptionEnum exceptionEnum;
	public ApiException(ExceptionEnum e) {
		super(e.getMessage());
		this.exceptionEnum = e;
	}
}