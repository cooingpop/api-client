/*
 * @(#) UserNotFoundException.java 2021. 08. 09.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.exception;

/**
 * @author 박준영
 **/
public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String userEmail){
		super(userEmail + " NotFoundException");
	}
}
