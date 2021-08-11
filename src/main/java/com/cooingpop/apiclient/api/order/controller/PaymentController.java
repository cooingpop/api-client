/*
 * @(#) PaymentController.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@Tag(name = "payment", description = "결제")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/payment")
@Slf4j
public class PaymentController {
	// 새로운 기능 추가시
}
