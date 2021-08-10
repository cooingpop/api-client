/*
 * @(#) User.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cooingpop.apiclient.api.common.domain.Common;
import com.cooingpop.apiclient.common.UserRole;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 박준영
 **/
@Entity
@Table(name = "USER",
	uniqueConstraints={
		@UniqueConstraint(
			columnNames={"email"}
		)
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Common implements Serializable {

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false, length = 30)
	private String nickname;

	@Setter
	@Column(nullable = false)
	private String pwd;

	@Column(nullable = false, length = 20)
	private String mobile;

	@Column(nullable = false, length = 100)
	private String email;

	@Column(nullable = true)
	private String gender;

	@Setter
	@Column(nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private UserRole role;
}
