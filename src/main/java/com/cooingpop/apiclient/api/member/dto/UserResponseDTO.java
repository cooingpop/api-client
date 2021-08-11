/*
 * @(#) UserResponseDTO.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import com.cooingpop.apiclient.common.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 단일 회원 Response
 * @author 박준영
 **/
@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserResponseDTO {
	private final Long userSeq;
	private final String name;
	private final String nickname;
	private final String mobile;
	private final String email;
	private final String gender;
	private final UserRole role;
}
