/*
 * @(#) CustomTokenResponse.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 박준영
 **/
@Data
@AllArgsConstructor
@Builder
public class JwtTokenResponse implements Serializable {
	private String access_token;
	private long expired_at;
}
