package com.bestvalue.ultimateutils.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BvasConFatory {

	private String jdbcDriver;
	private String mysqlUrl;
	private static final String LOCAL_DB = "jdbc:mysql://localhost:3306/bvasdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=register";
	private static final String NY_DB = "jdbc:mysql://192.168.3.254/bvasdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=bvas&password=bvasdat39";
	private static final String CH_DB = "jdbc:mysql://localhost:3306/bvasdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=register";
	private static final String AM_DB = "jdbc:mysql://192.168.4.244/amsdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=001FS740";
	private static final String GR_DB = "jdbc:mysql://192.168.2.244/bvasdb?user=root&password=001FS740";
	private static final String LOCAL_AM = "jdbc:mysql://localhost:3306/amsdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=001FS740";
	private static final String NY2_DB =
		      "jdbc:mysql://192.168.5.244:3306/nysdb?user=root&password=001FS740";
	public BvasConFatory(String dbpointer) {

		if (dbpointer.equalsIgnoreCase("NY")) {
			this.jdbcDriver = "com.mysql.jdbc.Driver";
			this.mysqlUrl = NY_DB;
		} else if (dbpointer.equalsIgnoreCase("CH")) {
			this.jdbcDriver = "com.mysql.jdbc.Driver";
			this.mysqlUrl = CH_DB;
		} else if (dbpointer.equalsIgnoreCase("AM")) {
			this.jdbcDriver = "com.mysql.jdbc.Driver";
			this.mysqlUrl = AM_DB;
		} else if (dbpointer.equalsIgnoreCase("GR")) {
			this.jdbcDriver = "com.mysql.jdbc.Driver";
			this.mysqlUrl = GR_DB;
		} else if (dbpointer.equalsIgnoreCase("local")) {
			this.jdbcDriver = "com.mysql.jdbc.Driver";
			this.mysqlUrl = LOCAL_DB;
		} else if (dbpointer.equalsIgnoreCase("localam")) {
			this.jdbcDriver = "com.mysql.jdbc.Driver";
			this.mysqlUrl = LOCAL_AM;
		} else if (dbpointer.equalsIgnoreCase("NY2")) {
	        this.jdbcDriver = "com.mysql.jdbc.Driver";
	        this.mysqlUrl = NY2_DB;
	      } else {
			this.mysqlUrl = "";
		}
	}

	public Connection getConnection() throws SQLException {
		Connection con = null;
		try {
			Class.forName(jdbcDriver).newInstance();
			con = DriverManager.getConnection(mysqlUrl);
		} catch (InstantiationException ex) {
			System.out.println("Exception---" + ex);
		} catch (IllegalAccessException ex) {
			System.out.println("Exception---" + ex);
		} catch (ClassNotFoundException ex) {
			System.out.println("Exception---" + ex);
		} catch (SQLException ex) {
			System.out.println("Exception---" + ex);
		}

		return con;

	}

	public void setMysqlUrl(String mysqlUrl) {
		this.mysqlUrl = mysqlUrl;
	}

	public String getMysqlUrl() {
		return mysqlUrl;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
}