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
	private String status;
	private int code;
	private String message;
	private T data;

	public CustomResponse(int code, String message) {
		this.code = code;
		this.message = message;
		this.data = null;
	}

	public static<T> CustomResponse<T> res(int code, String message) {
		return res(code, message, null);
	}

	public static<T> CustomResponse<T> res(int code, String message, T t) {
		return CustomResponse.<T>builder()
			.data(t)
			.code(code)
			.message(message)
			.build();
	}

	public static<T> CustomResponse<T> res(String status, int code, String message, T t) {
		return CustomResponse.<T>builder()
			.data(t)
			.status(status)
			.code(code)
			.message(message)
			.build();
	}
}
