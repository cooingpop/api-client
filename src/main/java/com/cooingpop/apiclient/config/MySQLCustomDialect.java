/*
 * @(#) MySQLCustomDialect.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.config;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * @author 박준영
 **/
public class MySQLCustomDialect extends MySQL5Dialect {
	@Override
	public String getTableTypeString() {
		return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci";
	}
}
