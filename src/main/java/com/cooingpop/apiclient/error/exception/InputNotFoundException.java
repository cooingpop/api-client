/*
 * @(#) InputNotFoundException.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.error.exception;

/**
 * @author 박준영
 **/
public class InputNotFoundException extends RuntimeException {
	public InputNotFoundException(Exception exception){
		super(exception);
	}
}
