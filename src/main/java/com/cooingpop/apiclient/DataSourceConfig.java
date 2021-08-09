/*
 * @(#) DataSourceConfig.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 박준영
 **/
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	@Bean("publicDataSource")
	@ConfigurationProperties(prefix = "datasource.public")
	public DataSource publicDataSource() {
		return DataSourceBuilder.create().type(BasicDataSource.class).build();
	}

	public SqlSessionFactory publicSqlSessionFactory(ApplicationContext applicationContext)
		throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(publicDataSource());
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/*.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name="publicTM")
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(publicDataSource());
		transactionManager.setGlobalRollbackOnParticipationFailure(true);
		return transactionManager;
	}

	@Bean(name="public")
	public SqlSessionTemplate publicSqlSessionTemplate(ApplicationContext applicationContext) throws Exception {
		return new SqlSessionTemplate(publicSqlSessionFactory(applicationContext));
	}
}
