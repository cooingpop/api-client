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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cooingpop.apiclient.api.common.domain.Common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문
 * @author 박준영
 **/
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
		@AttributeOverride(name="paymentNo", column=@Column(name="pm_no")),
		@AttributeOverride(name="productName", column=@Column(name="prod_nm")),
	})

	@Column(nullable = false, length = 12)
	private String paymentNo;

	@Column(nullable = false, length = 100)
	private String productName;

	@Column(nullable = false)
	private LocalDateTime paymentAt;
}
