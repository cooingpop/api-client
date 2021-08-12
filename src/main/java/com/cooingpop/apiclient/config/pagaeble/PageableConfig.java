/*
 * @(#) PageableConfig.java 2021. 08. 12.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.pagaeble;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;

/**
 * Pagaeble 커스터마이징 클래스
 *
 * @author 박준영
 **/
@Configuration
public class PageableConfig {

	/**
	 * 정렬 핸들러 커스터마이징
	 *
	 * url/?page=1&size=5&sortBy=email-desc
	 *
	 * @return
	 */
	@Bean
	public SortHandlerMethodArgumentResolverCustomizer sortHandlerMethodArgumentResolverCustomizer() {
		//
		return s -> {
			s.setSortParameter("sortBy");  // default "sort"
			s.setPropertyDelimiter("-"); // default ";"
		};
	}

	/**
	 * 페이징 처리 커스터마이징
	 *
	 * @return
	 */
	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer pageableHandlerMethodArgumentResolverCustomizer() {
		return p -> {
			p.setOneIndexedParameters(true); // default > 1페이지가 0으로 인식되기때문에 페이지 기본 값을 1로 설정
			p.setMaxPageSize(10); // 페이지 요청이 한번에 많을 경우 대비하여, 최대 요청 가능한 size
			p.setFallbackPageable(PageRequest.of(0, 10));
		};
	}
}
