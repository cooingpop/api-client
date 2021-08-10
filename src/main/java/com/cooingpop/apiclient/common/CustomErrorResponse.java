/*
 * @(#) CustomErrorResponse.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 박준영
 **/
@Data
@AllArgsConstructor
@Builder
public class CustomErrorResponse {
	private HttpStatus status;
	private int errorCode;
	private String errorMessage;

}
