package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class GRReorderLevel {
  private static final Logger logger = Logger.getLogger(GRReorderLevel.class);

  static Connection mySQLCon = null;

  static Connection localCon = null;

  public static void main(String[] args) {
    long stTime = System.currentTimeMillis();

    mySQLCon = getMySQLConnection();
    localCon = getLocalConnection();
    try {
      File returns1 = new File("c:/bvaschicago/Data/GRReOrders.txt");
      FileWriter rep = null;
      rep = new FileWriter(returns1);

      rep.write("RE-ORDER   -   START\n");

      long currTime = System.currentTimeMillis();
      long diffPeriod = 10368000000L;
      long oldTime = currTime - diffPeriod;
      java.util.Date dd = new java.util.Date(oldTime);
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
      String ddDate = sdf.format(dd);
      rep.write("Got Date : " + ddDate + "\n");

      // **** PROCESSING FOR MAIN ITEMS ****

      Statement stmt2 = mySQLCon.createStatement();
      String sql2 =
          "Select a.PartNumber, sum(a.Quantity), c.InterChangeNo, UnitsinStock+Unitsonorder from InvoiceDetails a, Invoice b, Parts c where a.InvoiceNUmber=b.InvoiceNumber and b.OrderDate>'"
              + DateUtils.convertUSToMySQLFormat(ddDate)
              + "' and a.PartNumber=c.PartNo Group by a.PartNumber Order by 1 ";
      ResultSet rs2 = stmt2.executeQuery(sql2);

      Statement stmtLoc = localCon.createStatement();
      stmtLoc.executeUpdate("Delete from VendorOrderedItems Where orderno=-4 ");

      int noOrder = 0;
      PreparedStatement pstmt2 =
          localCon
              .prepareStatement("Insert into VendorOrderedItems (PartNo, OrderNo, NoOrder, Quantity, price) Values (?, -4, ?, ?, 0) ");
      int regCnt = 0;
      int regSkip = 0;

      while (rs2.next()) {
        String pNo = rs2.getString(1);
        int reLevel = rs2.getInt(2);
        String interNo = rs2.getString(3);
        int stock = rs2.getInt(4);
        reLevel = reLevel - stock + 1;
        if (reLevel < 1) {
          regSkip++;
          continue;
        }
        regCnt++;
        noOrder++;
        pstmt2.clearParameters();
        if (interNo != null && !interNo.trim().equals("")) {
          pstmt2.setString(1, interNo);
        } else {
          pstmt2.setString(1, pNo);
        }
        pstmt2.setInt(2, noOrder);
        pstmt2.setInt(3, reLevel);

        pstmt2.execute();

      }
      rep.write("Main Items Changed : " + regCnt + "\n");
      rep.write("Main Items Skipped : " + regSkip + "\n");

      // **** PROCESSING FOR INTERCHANGEABLE ITEMS ****

      Statement stmt3 = mySQLCon.createStatement();
      String sql3 =
          " select a.partno, a.quantity, b.InterChangeNo from vendorordereditems a, parts b where a.orderno=-1 and a.partno=b.partno and b.reorderlevel<1 and unitsonorder=0 and unitsinstock<1 and partdescription not like 'z%' order by 1 ";
      ResultSet rs3 = stmt3.executeQuery(sql3);

      PreparedStatement pstmt3 =
          localCon
              .prepareStatement("Insert into VendorOrderedItems (PartNo, OrderNo, NoOrder, Quantity, price) Values (?, -4, ?, ?, 0) ");
      int intCnt = 0;
      int intSkip = 0;

      while (rs3.next()) {
        String pNo = rs3.getString(1);
        int reLevel = rs3.getInt(2);
        String interNo = rs3.getString(3);
        if (reLevel < 1) {
          intSkip++;
          continue;
        }
        intCnt++;
        noOrder++;
        pstmt3.clearParameters();
        if (interNo != null && !interNo.trim().equals("")) {
          pstmt3.setString(1, interNo);
        } else {
          pstmt3.setString(1, pNo);
        }
        pstmt3.setInt(2, noOrder);
        pstmt3.setInt(3, reLevel);

        pstmt3.execute();

      }
      rep.write("InterChange Items Changed : " + intCnt + "\n");
      rep.write("InterChange Items Skipped : " + intSkip + "\n");
      rep.write("Total Time : " + (System.currentTimeMillis() - stTime) + "\n");
      rep.close();

    } catch (Exception e) {
      logger.error(e);
    }

  }

  static Connection getMySQLConnection() {
    Connection con = null;

    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      con =
          DriverManager
              .getConnection("jdbc:mysql://192.168.2.254/bvasDB?user=ram&password=raravisaa");
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

  static Connection getLocalConnection() {
    Connection con = null;

    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      con =
          DriverManager.getConnection("jdbc:mysql://localhost/bvasDB?user=ram&password=raravisaa");
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

}
