/*
 * @(#) UserService.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cooingpop.apiclient.api.member.domain.User;
import com.cooingpop.apiclient.api.member.dto.SearchRequestDTO;
import com.cooingpop.apiclient.api.member.dto.SignUpRequestDTO;
import com.cooingpop.apiclient.api.member.repository.UserRepository;
import com.cooingpop.apiclient.api.member.specs.UserSpecification;
import com.cooingpop.apiclient.common.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * 회원 관련 서비스
 * 
 * @author 박준영
 **/
@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * 회원가입
	 * @param signUpRequestDTO
	 * @return
	 */
	@Transactional
	public User signUp(final SignUpRequestDTO signUpRequestDTO) {
		final User user = User.builder()
			.name(signUpRequestDTO.getName())
			.nickname(signUpRequestDTO.getNickname())
			.pwd(passwordEncoder.encode(signUpRequestDTO.getPwd()))
			.mobile(signUpRequestDTO.getMobile())
			.email(signUpRequestDTO.getEmail())
			.gender(signUpRequestDTO.getGender())
			.role(UserRole.ROLE_USER)
			.build();

		return userRepository.save(user);
	}

	/**
	 * 회원 목록 조회 (검색기능 > 이름,이메일 )
	 * @return
	 */
	public Page<User> findAll(final SearchRequestDTO searchRequestDTO, final Pageable pageable) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> searchMap = objectMapper.convertValue(searchRequestDTO, Map.class);

		Map<UserSpecification.SearchKey, Object> searchKeys = new HashMap<>();
		for (String key : searchMap.keySet()) {
			if (searchMap.get(key) != null) {
				searchKeys.put(UserSpecification.SearchKey.valueOf(key.toUpperCase()), searchMap.get(key));
			}
		}

		return searchMap.isEmpty() ? userRepository.findAll(pageable) : userRepository.findAll(UserSpecification.searchWith(searchKeys), pageable);
	}

	/**
	 * 이메일 중복 체크
	 * @param email
	 * @return
	 */
	public Optional<Integer> duplicatedEmail(final String email) {
		return userRepository.countByEmail(email);
	}

	/**
	 * 로그인
	 * @param email
	 * @return
	 */
	public Optional<User> logIn(final String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * 이메일로 회원 검색
	 * @param email
	 * @return
	 */
	public Optional<User> findUserByEmail(final String email) {
		return userRepository.findUserByEmail(email);
	}
}
