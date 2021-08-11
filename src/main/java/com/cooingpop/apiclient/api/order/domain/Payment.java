/*
 * @(#) Order.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.order.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cooingpop.apiclient.api.common.domain.Common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문
 * @author 박준영
 **/
@Schema(description = "주문")
@Entity
@Table(name = "payment",
	uniqueConstraints={
		@UniqueConstraint(
			columnNames={"paymentNo"}
		)
	})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends Common implements Serializable {
	@AttributeOverrides({
		@AttributeOverride(name="paymentSeq", column=@Column(name="payment_seq")),
		@AttributeOverride(name="userSeq", column=@Column(name="user_seq")),
		@AttributeOverride(name="paymentNo", column=@Column(name="pm_no")),
		@AttributeOverride(name="productName", column=@Column(name="prod_nm")),
	})

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long paymentSeq; // 고유번호

	@Schema(description = "주문번호")
	@Column(nullable = false, length = 12)
	private String paymentNo;

	@Schema(description = "emoji를 포함한 모든 문자")
	@Column(nullable = false, length = 100)
	private String productName;

	@Schema(description = "Timezone 을 고려한 시간 정보")
	@Column(nullable = false)
	private LocalDateTime paymentAt;

	@Schema(description = "주문한 회원 번호")
	@Column(nullable = false)
	private Long userSeq;
}
