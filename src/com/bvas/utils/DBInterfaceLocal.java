package com.bvas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBInterfaceLocal {
  private static final Logger logger = Logger.getLogger(DBInterfaceLocal.class);

  private static final String jdbcDriver = "com.mysql.jdbc.Driver";

  private static final String LOCAL_DB =
      "jdbc:mysql://localhost:3306/bvasdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=001FS740";

  private static Connection connection;

  public static Connection getSQLConnection() {
    try {
      Class.forName(jdbcDriver).newInstance();
      connection = DriverManager.getConnection(LOCAL_DB);
    } catch (InstantiationException e) {
      logger.error("InstantiationException:" + e.toString());
    } catch (IllegalAccessException e) {
      logger.error("IllegalAccessException:" + e.toString());
    } catch (ClassNotFoundException e) {
      logger.error("ClassNotFoundException:" + e.toString());
    } catch (SQLException e) {
      logger.error("SQLException:" + e.toString());
    }
    return connection;
    /*
     * Connection connection = findingSQLConnection(); if(connection != null) { return connection;
     * }else { return getSQLConnection(); }
     */

  }

  private static Connection findingSQLConnection() {
    try {
      Class.forName(jdbcDriver).newInstance();
      connection = DriverManager.getConnection(LOCAL_DB);
    } catch (InstantiationException e) {
      logger.error("InstantiationException:" + e.toString());
    } catch (IllegalAccessException e) {
      logger.error("IllegalAccessException:" + e.toString());
    } catch (ClassNotFoundException e) {
      logger.error("ClassNotFoundException:" + e.toString());
    } catch (SQLException e) {
      logger.error("SQLException:" + e.toString());
    }
    return connection;
  }

  public static String getJdbcdriver() {
    return jdbcDriver;
  }

}
