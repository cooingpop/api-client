/*
 * @(#) UserService.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cooingpop.apiclient.api.member.dto.SignInDTO;
import com.cooingpop.apiclient.api.member.dto.SignUpDTO;
import com.cooingpop.apiclient.api.member.model.User;
import com.cooingpop.apiclient.api.member.repository.UserRepository;
import com.cooingpop.apiclient.common.UserRole;

import lombok.RequiredArgsConstructor;

/**
 * @author 박준영
 **/
@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public User signUp(final SignUpDTO signUpDTO) {
		final User user = User.builder()
			.name(signUpDTO.getName())
			.nickname(signUpDTO.getNickname())
			.pwd(passwordEncoder.encode(signUpDTO.getPwd()))
			.mobile(signUpDTO.getMobile())
			.email(signUpDTO.getEmail())
			.gender(signUpDTO.getGender())
			.role(UserRole.ROLE_USER)
			.build();

		return userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<Boolean> duplicatedEmail(final String email) {
		return userRepository.findExistByEmail(email);
	}

	public Optional<User>  signIn(SignInDTO signInDTO) {
		final User user = User.builder()
			.email(signInDTO.getEmail())
			.role(UserRole.ROLE_USER)
			.build();

		return userRepository.findByEmail(signInDTO.getEmail());
	}
}
