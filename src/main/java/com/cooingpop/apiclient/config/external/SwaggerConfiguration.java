/*
 * @(#) SwaggerConfiguration.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config.external;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Swagger Configuration
 * 접속시 domain:8080/swagger-ui/
 * @author 박준영
 **/
@Configuration
public class SwaggerConfiguration {
	@Value("${api.version}")
	String appVersion;
	@Value("${api.url}")
	String url;
	@Value("${spring.profiles.active}")
	String active;

	/**
	 * Swagger API 설정
	 * @return
	 */
	@Bean
	public OpenAPI openAPI() {
		List<Server> servers = Arrays.asList(new Server().url(url).description("api (" + active +")"));

		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER).name("Authorization");
		SecurityRequirement schemaRequirement = new SecurityRequirement().addList("bearerAuth");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
			.addSecurityItem(schemaRequirement)
			.security(Arrays.asList(schemaRequirement))
			.info(swaggerInfo())
			.servers(servers);
	}

	/**
	 * Swagger Infomation
	 * @return
	 */
	private Info swaggerInfo () {
		return new Info().title("API - " + active).version(appVersion)
			.description("Spring Boot를 이용한 RST API")
			.termsOfService("http://swagger.io/terms/")
			.contact(new Contact().name("cooingpop").url("https://vitalholic.tistory.com/").email("cooingpop@gmail.com"))
			.license(new License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));
	}
}
