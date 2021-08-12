/*
 * @(#) UserResponseDTO.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import java.util.Optional;

import com.cooingpop.apiclient.api.member.domain.User;

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
	private final User user;
}
