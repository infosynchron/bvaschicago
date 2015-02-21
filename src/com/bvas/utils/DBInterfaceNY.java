package com.bvas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBInterfaceNY {
  private static final Logger logger = Logger.getLogger(DBInterfaceLocal.class);

  private static final String jdbcDriver = "com.mysql.jdbc.Driver";

  private static final String LOCAL_DB =
      "jdbc:mysql://192.168.3.254/bvasdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=001FS740";

  private static Connection connection;

  public static Connection getSQLConnection() {

    try {
      Class.forName(jdbcDriver).newInstance();
      connection = DriverManager.getConnection(LOCAL_DB);
    } catch (InstantiationException ex) {
      logger.error("Exception---" + ex);
    } catch (IllegalAccessException ex) {
      logger.error("Exception---" + ex);
    } catch (ClassNotFoundException ex) {
      logger.error("Exception---" + ex);
    } catch (SQLException ex) {
      logger.error("Exception---" + ex);
    }

    return connection;
  }

  public static String getJdbcdriver() {
    return jdbcDriver;
  }

}
