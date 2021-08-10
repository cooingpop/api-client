/*
 * @(#) UserRepository.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooingpop.apiclient.api.member.domain.User;

/**
 * @author 박준영
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<Boolean> findExistByEmail(String email);
	Optional<User> findByEmail(String email);
	Optional<User> findUserByEmail(String email);
}
