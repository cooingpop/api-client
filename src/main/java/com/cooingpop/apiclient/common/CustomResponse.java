/*
 * @(#) CustomResponse.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 공통 응답 클래스
 *
 * @author 박준영
 **/
@Data
@AllArgsConstructor
@Builder
public class CustomResponse<T> {
	private int status;
	private String message;
	private T data;

	public CustomResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.data = null;
	}

	public static<T> CustomResponse<T> res(int status, String message) {
		return res(status, message, null);
	}

	public static<T> CustomResponse<T> res(int status, String message, T t) {
		return CustomResponse.<T>builder()
			.data(t)
			.status(status)
			.message(message)
			.build();
	}
}
