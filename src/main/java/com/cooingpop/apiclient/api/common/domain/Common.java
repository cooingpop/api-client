/*
 * @(#) Common.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 박준영
 **/
@MappedSuperclass
@Getter
@NoArgsConstructor
public class Common implements Serializable {
	@AttributeOverrides({
		@AttributeOverride(name="createdAt", column=@Column(name="created_at")),
		@AttributeOverride(name="updatedAt", column=@Column(name="updated_at")),
	})

	@CreationTimestamp
	@Column(nullable = false, length = 20, updatable = false)
	private LocalDateTime createdAt; // 등록 일자

	@UpdateTimestamp
	@Column(length = 20)
	private LocalDateTime updatedAt; // 수정 일자

	@Setter
	@Column(nullable = false)
	private Boolean isEnable = true; // 사용 여부
}
