/*
 * @(#) SearchDTO.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.dto;

import lombok.Data;

/**
 * 검색 request DTO
 * @author 박준영
 **/
@Data
public class SearchRequestDTO {
	private String email;
	private String name;
}
