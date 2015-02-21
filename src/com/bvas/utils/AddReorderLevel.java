package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class AddReorderLevel {

  static Connection localCon = null;

  private static final Logger logger = Logger.getLogger(AddReorderLevel.class);

  public static void main(String[] args) {
    long stTime = System.currentTimeMillis();

    localCon = getLocalConnection();
    try {
      // **** PROCESSING FOR MAIN ITEMS ****

      File returns1 = new File("c:/bvaschicago/Data/AddReOrders.txt");
      FileWriter rep = null;
      rep = new FileWriter(returns1);

      rep.write("RE-ORDER   -   START\n");
      Statement stmt = null;
      Statement stmt2 = localCon.createStatement();
      String sql2 =
          "Select PartNo, sum(quantity) from VendorOrderedItems Where Orderno=-4 or OrderNo=-5 or OrderNo=-1 Group by 1 Order by 2 ";
      ResultSet rs2 = stmt2.executeQuery(sql2);

      // PreparedStatement pstmt2 =
      // localCon.prepareStatement("Update Parts Set ReorderLevel=ReorderLevel+? Where PartNo=? or Interchangeno=? ");
      int regCnt = 0;
      int regSkip = 0;

      int sqlCount = 0;
      String sqlStr = "";
      int oldReLevel = 0;

      while (rs2.next()) {
        // Ststement stmt = localCon.createStatement();
        String pNo = rs2.getString(1);
        int reLevel = rs2.getInt(2);
        // logger.error.print(pNo + "\t");
        // logger.error(reLevel);
        if (reLevel <= 0) {
          regSkip++;
          continue;
        } else if (oldReLevel != reLevel) {
          if (sqlCount > 0) {
            sqlCount = 0;
            stmt = localCon.createStatement();
            stmt.execute(sqlStr);
            // logger.error(sqlStr);
            sqlStr = "";
          }
          sqlCount++;
          sqlStr =
              "Update Parts Set ReorderLevel=ReorderLevel+" + reLevel + " Where PartNo='" + pNo
                  + "' or Interchangeno='" + pNo + "' ";
          oldReLevel = reLevel;
        } else if (oldReLevel == reLevel) {
          if (sqlCount == 50) {
            sqlCount = 1;
            stmt = localCon.createStatement();
            stmt.execute(sqlStr);
            // logger.error(sqlStr);
            sqlStr =
                "Update Parts Set ReorderLevel=ReorderLevel+" + reLevel + " Where PartNo='" + pNo
                    + "' or Interchangeno='" + pNo + "' ";
          } else {
            sqlCount++;
            sqlStr += " or PartNo='" + pNo + "' or Interchangeno='" + pNo + "' ";
          }
        }

        regCnt++;

        rs2.close();
        stmt2.close();

      }
      if (sqlStr != null && !sqlStr.trim().equals("")) {
        stmt = localCon.createStatement();
        stmt.execute(sqlStr);
      }
      rep.write("Main Items Changed : " + regCnt + "\n");
      rep.write("Main Items Skipped : " + regSkip + "\n");
      rep.write("Total Time : " + (System.currentTimeMillis() - stTime) + "\n");
      rep.close();

      stmt.close();
      localCon.close();
    }

    catch (Exception e) {
      logger.error(e);
    }

    /*
     * long stTime = System.currentTimeMillis();
     * 
     * localCon = getLocalConnection(); try { // **** PROCESSING FOR MAIN ITEMS ****
     * 
     * Statement stmt2 = localCon.createStatement(); String sql2 =
     * "Select PartNo, sum(quantity) from VendorOrderedItems Where Orderno=-4 or OrderNo=-5 or OrderNo=-1 Group by 1 Order by 1 "
     * ; ResultSet rs2 = stmt2.executeQuery(sql2);
     * 
     * PreparedStatement pstmt2 = localCon.prepareStatement(
     * "Update Parts Set ReorderLevel=ReorderLevel+? Where PartNo=? or Interchangeno=? " ); int
     * regCnt = 0; int regSkip = 0;
     * 
     * while (rs2.next()) { String pNo = rs2.getString(1); int reLevel = rs2.getInt(2); if (reLevel
     * <= 0) { regSkip++; continue; } regCnt++; pstmt2.clearParameters(); pstmt2.setInt(1, reLevel);
     * pstmt2.setString(2, pNo); pstmt2.setString(3, pNo);
     * 
     * pstmt2.execute();
     * 
     * } logger.error("Main Items Changed : " + regCnt); logger.error("Main Items Skipped : " +
     * regSkip); } catch (Exception e) { logger.error(e); }
     * 
     * logger.error("Total Time : " + (System.currentTimeMillis() - stTime));
     */
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
