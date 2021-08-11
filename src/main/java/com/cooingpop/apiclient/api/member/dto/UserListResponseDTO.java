/*
 * @(#) UserListResponseDTO.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import java.util.List;

import com.cooingpop.apiclient.api.member.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 회원 목록 Response
 * @author 박준영
 **/
@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserListResponseDTO {
	private final List<User> userList;
}
