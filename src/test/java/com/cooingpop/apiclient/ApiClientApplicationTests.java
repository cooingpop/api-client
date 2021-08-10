package com.cooingpop.apiclient;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


import static org.assertj.core.api.Assertions.assertThat;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

@Import(EnableEncryptablePropertiesConfiguration.class)
@SpringBootTest
class ApiClientApplicationTests {

	// @Value("${datasource.public.username}")
	private String datasourceUserName;

	// @Test
	void contextLoads() {
	}

	// @Test
	void test() {
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("cooingpop"); //2번 설정의 암호화 키를 입력
		String des = pbeEnc.encrypt("admin");
		System.out.println("des = " + des);

	}

	// @DisplayName("datasource properties 암호화,복호화")
	// @ParameterizedTest
	// @CsvSource(value = {"VamDgshz9aXBCFNZYxuAcw==:3", "EnOHH9crdNkn7mtPOY6BpnYkA0WrHgoM:0"}, delimiter = ':')
	void datasource(String input, String expected) {
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("cooingpop"); //2번 설정의 암호화 키를 입력
		String des = pbeEnc.decrypt(datasourceUserName);
		System.out.println("des = " + des);
		assertThat(pbeEnc.decrypt(input)).isEqualTo(expected);
	}

}
