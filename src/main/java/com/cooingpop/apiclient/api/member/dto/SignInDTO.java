/*
 * @(#) SignInDTO.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import lombok.Data;

/**
 * @author 박준영
 **/
@Data
public class SignInDTO {
	private String email;
	private String pwd;
}
