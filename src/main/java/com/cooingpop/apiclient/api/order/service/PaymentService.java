/*
 * @(#) PaymentService.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooingpop.apiclient.api.order.domain.Payment;
import com.cooingpop.apiclient.api.order.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 박준영
 **/
@RequiredArgsConstructor
@Service
public class PaymentService {
	private final PaymentRepository paymentRepository;

	/**
	 * 단일 회원의 주문 목록 조회
	 * @param userSeq
	 * @return
	 */
	public List<Payment> findAllPaymentByUserSeq(final Long userSeq) {
		return paymentRepository.findAllPaymentByUserSeq(userSeq);
	}
}
