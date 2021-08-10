/*
 * @(#) ServicePasswordEncoder.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 박준영
 **/
public class ServicePasswordEncoder implements PasswordEncoder {
	private static final String ALGORITHM = "SHA-256";

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}

	public String encode(CharSequence rawPassword) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
			StringBuilder sbEncrypt = new StringBuilder();

			byte[] digest = messageDigest.digest(rawPassword.toString().getBytes("UTF-8"));

			for (int i = 0; i < digest.length; i++) {
				sbEncrypt.append(Integer.toHexString(digest[i] & 0xFF).toUpperCase());
			}

			return sbEncrypt.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm [" + ALGORITHM + "]");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}
	}
}
