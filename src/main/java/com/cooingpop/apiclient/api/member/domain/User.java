/*
 * @(#) User.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cooingpop.apiclient.api.common.domain.Common;
import com.cooingpop.apiclient.common.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원
 *
 * @author 박준영
 **/
@Schema(description = "회원")
@Entity
@Table(name = "user",
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
	@AttributeOverrides({
		@AttributeOverride(name="userSeq", column=@Column(name="user_seq"))
	})

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long userSeq; // 고유번호

	@Schema(description = "이름")
	@Column(nullable = false, length = 20)
	private String name;

	@Schema(description = "별명")
	@Column(nullable = false, length = 30)
	private String nickname;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 응답결과 포함시키지 않고, 사용할때만 포함하도록
	@Schema(description = "비밀번호")
	@Setter
	@Column(nullable = false)
	private String pwd;

	@Schema(description = "전화번호")
	@Column(nullable = false, length = 20)
	private String mobile;

	@Schema(description = "이메일", nullable = false, example = "abc@jiniworld.me")
	@Column(nullable = false, length = 100)
	private String email;

	@Schema(description = "성별")
	@Column(nullable = true)
	private String gender;

	@Schema(description = "권한")
	@Setter
	@Column(nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private UserRole role;
}
