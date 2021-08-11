/*
 * @(#) SignInDTO.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import lombok.Data;

/**
 * 로그인 request
 * @author 박준영
 **/
@Data
public class SignInRequestDTO {
	private String email;
	private String pwd;
}
