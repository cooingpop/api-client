/*
 * @(#) SwaggerConfiguration.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.external;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 * 접속시 domain:8080/swagger-ui/
 * @author 박준영
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	/**
	 * Swagger API 문서 생성
	 * @return
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30) // open api spec 3.0
			.apiInfo(swaggerInfo()) // swagger 정보 등록
			.select()
			.apis(RequestHandlerSelectors.any()) //  모든 컨트롤러 패키지 탐색시
			// RequestHandlerSelectors.basePackage("com.cooingpop.apiclient.api.member.controller") 특정 패키지
			.paths(PathSelectors.any())
			.build()
			.useDefaultResponseMessages(true); //기본으로 세팅되는 200, 401, 메시지 표시
	}
	/**
	 * Swagger Infomation
	 * @return
	 */
	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder().title("Spring Boot API Documentation")
			.description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다")
			.license("cooingpop").licenseUrl("https://vitalholic.tistory.com/").version("1.0.0").build();
	}
}
