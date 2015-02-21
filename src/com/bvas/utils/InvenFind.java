package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class InvenFind {
  private static final Logger logger = Logger.getLogger(InvenFind.class);

  static Connection mySQLCon = null;

  static Connection accessCon = null;

  static final String jdbcDriver = "com.mysql.jdbc.Driver";

  public static void main(String[] args) {

    mySQLCon = getMySQLConnection();
    try {
      Statement stmt = mySQLCon.createStatement();
      String sql =
          "SELECT a.PartNo, a.year, b.MakeModelName, a.PartDescription, a.UnitsInStock, c.ManufacturerName FROM PartsBack a, MakeModelBack b, Manufacturer c WHERE a.UnitsInStock > 0 AND a.MakeModelCode = b.MakeModelCode AND b.ManufacturerId = c.ManufacturerId ORDER BY 6 ASC, 3 ASC, 1 ASC";
      ResultSet rs = stmt.executeQuery(sql);
      File file1 = new File("Invent.txt");
      FileWriter ft = new FileWriter(file1);
      while (rs.next()) {
        String partNo = rs.getString(1);
        String year = rs.getString(2);
        String mmName = rs.getString(3);
        String desc = rs.getString(4);
        String units = rs.getString(5);
        String manufac = rs.getString(6);
        ft.write(padSpaces(partNo, 6) + "\t" + padSpaces(manufac, 11) + "\t"
            + padSpaces(mmName, 35) + "\t" + padSpaces(year, 6) + "\t" + padSpaces(desc, 35) + "\t"
            + units + "\n");

      }
      ft.close();
    } catch (Exception e) {
      logger.error(e);
    }

  }

  static Connection getMySQLConnection() {
    Connection con = null;

    try {
      Class.forName(jdbcDriver).newInstance();
      con =
          DriverManager
              .getConnection("jdbc:mysql://localhost/bvasdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8&user=root&password=001FS740");
    } catch (InstantiationException ex) {
      logger.error("Exception---" + ex);
    } catch (IllegalAccessException ex) {
      logger.error("Exception---" + ex);
    } catch (ClassNotFoundException ex) {
      logger.error("Exception---" + ex);
    } catch (SQLException ex) {
      logger.error("Exception---" + ex);
    }

    return con;
  }

  public static String padSpaces(String str, int len) {
    if (str == null || str.trim().equals("null"))
      str = "";
    str = str.trim();
    int len1 = str.length();
    while (len1 < len) {
      str += " ";
      len1++;
    }
    str += " ";
    return str;
  }

}
