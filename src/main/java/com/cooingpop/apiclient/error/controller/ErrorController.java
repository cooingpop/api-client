/*
 * @(#) ErrorController.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 박준영
 **/
@RestController
@RequestMapping(value = "/error")
public class ErrorController {
	@GetMapping(value = "/unauthorized")
	public ResponseEntity<Void> unauthorized() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
