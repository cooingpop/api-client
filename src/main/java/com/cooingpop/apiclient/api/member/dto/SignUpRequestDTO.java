/*
 * @(#) SignUpDTO.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import lombok.Data;

/**
 * 회원가입 request
 * @author 박준영
 **/
@Data
public class SignUpRequestDTO {
	private String name;
	private String nickname;
	private String email;
	private String pwd;
	private String mobile;
	private String gender;
}
