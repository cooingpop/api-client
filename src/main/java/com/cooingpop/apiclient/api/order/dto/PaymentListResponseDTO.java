/*
 * @(#) PaymentResponseDTO.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.order.dto;

import java.util.List;

import com.cooingpop.apiclient.api.order.domain.Payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author 박준영
 **/
@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PaymentListResponseDTO {
	private final List<Payment> paymentList;
}
