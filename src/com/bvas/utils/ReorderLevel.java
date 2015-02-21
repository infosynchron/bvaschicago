package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class ReorderLevel {
  private static final Logger logger = Logger.getLogger(ReorderLevel.class);

  static Connection mySQLCon = null;

  static Connection accessCon = null;

  public static void main(String[] args) {
    long stTime = System.currentTimeMillis();

    mySQLCon = getMySQLConnection();
    FileWriter rep = null;
    try {
      File returns1 = new File("c:/bvaschicago/Data/ReOrders.txt");
      rep = new FileWriter(returns1);
      rep.write("RE-ORDER   -   START\n");

      Statement stmt1 = mySQLCon.createStatement();
      String sql1 = " Update Parts Set UnitsOnOrder=0, ReOrderLevel=0";
//    	  +", ReOrderLevelDate=0 ";
      stmt1.execute(sql1);

      long currTime = System.currentTimeMillis();
      long diffPeriod = 9072000000L;
      long oldTime = currTime - diffPeriod;
      java.util.Date dd = new java.util.Date(oldTime);
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
      String ddDate = sdf.format(dd);
      rep.write("Got Date : " + ddDate + "\n");

      // **** PROCESSING FOR MAIN ITEMS ****

      Statement stmt2 = mySQLCon.createStatement();
      String sql2 =
          "Select a.PartNumber, sum(a.Quantity) from InvoiceDetails a, Invoice b, Parts c where a.InvoiceNUmber=b.InvoiceNumber and b.OrderDate>'"
              + DateUtils.convertUSToMySQLFormat(ddDate)
              + "' and a.PartNumber=c.PartNo and c.InterChangeNo='' Group by a.PartNumber Order by 2, 1 ";
      ResultSet rs2 = stmt2.executeQuery(sql2);

      rep.write("RE-ORDER   -   GOT PARTS FROM INVOICE DETAILS\n");
      // PreparedStatement pstmt2 =
      // mySQLCon.prepareStatement("Update Parts Set ReOrderLevelDate=" +
      // currTime +
      // ", ReorderLevel=? Where PartNo=? or Interchangeno=? ");
      String newSQL = "";
      int regCnt = 0;
      int regSkip = 0;
      int curCnt = 0;
      int curReOrder = 0;
      Statement newStmt = mySQLCon.createStatement();

      while (rs2.next()) {
        String pNo = rs2.getString(1);
        int reLevel = rs2.getInt(2);
        if (reLevel == 0) {
          regSkip++;
          continue;
        }
        regCnt++;

        rep.write("RE-ORDER   " + reLevel + "   DOING PART NO:::" + pNo + "\n");

        if (curCnt == 0) {
          newSQL =
              "Update Parts Set " 
//        	  + "ReOrderLevelDate=" + currTime + "," 
        	  + " ReorderLevel=" + reLevel
                  + " Where PartNo='" + pNo + "' or Interchangeno='" + pNo + "'";
          curReOrder = reLevel;
          curCnt++;
        } else if (curCnt < 50 && curReOrder == reLevel) {
          newSQL += " or PartNo='" + pNo + "' or Interchangeno='" + pNo + "'";
          curCnt++;
        } else {
          newStmt.execute(newSQL);
          newSQL = "";
          newSQL =
              "Update Parts Set " +
//              "ReOrderLevelDate=" + currTime + "," +
              		" ReorderLevel=" + reLevel
                  + " Where PartNo='" + pNo + "' or Interchangeno='" + pNo + "'";
          curCnt = 1;
          curReOrder = reLevel;
        }

        /*
         * pstmt2.clearParameters(); pstmt2.setInt(1, reLevel); pstmt2.setString(2, pNo);
         * pstmt2.setString(3, pNo);
         * 
         * pstmt2.execute();
         */

      }

      newStmt.execute(newSQL);
      logger.error("Done Main Items");

      rep.write("Main Items Changed : " + regCnt + "\n");
      rep.write("Main Items Skipped : " + regSkip + "\n");

      // **** PROCESSING FOR INTERCHANGEABLE ITEMS ****

      Statement stmt3 = mySQLCon.createStatement();
      String sql3 =
          "Select c.InterchangeNo, sum(a.Quantity) from InvoiceDetails a, Invoice b, Parts c where a.InvoiceNUmber=b.InvoiceNumber and b.OrderDate>'"
              + DateUtils.convertUSToMySQLFormat(ddDate)
              + "' and a.PartNumber=c.PartNo and c.InterChangeNo!='' Group by 1 Order by 2, 1 ";
      ResultSet rs3 = stmt3.executeQuery(sql3);

      rep.write("RE-ORDER   -   GOT INTERCHANGE PARTS\n");
      // PreparedStatement pstmt3 =
      // mySQLCon.prepareStatement("Update Parts Set ReOrderLevelDate=" +
      // currTime +
      // ", ReorderLevel=ReorderLevel+? Where PartNo=? OR InterChangeNo=? ");
      newSQL = "";
      int intCnt = 0;
      int intSkip = 0;
      curCnt = 0;
      curReOrder = 0;
      Statement newInterStmt = mySQLCon.createStatement();

      while (rs3.next()) {
        String pNo = rs3.getString(1);
        int reLevel = rs3.getInt(2);
        if (reLevel == 0) {
          intSkip++;
          continue;
        }
        /*
         * Statement stmt4 = mySQLCon.createStatement(); ResultSet rs4 = stmt4.executeQuery(
         * "Select b.partNo, b.ReOrderLevel From Parts a, Parts b Where a.PartNo='" + pNo +
         * "' and a.InterChangeNo=b.PartNo "); String mainNo = ""; if (rs4.next()) { mainNo =
         * rs4.getString(1); reLevel += rs4.getInt(2); }
         */

        intCnt++;

        if (curCnt == 0) {
          newSQL =
              "Update Parts Set " +
//              "ReOrderLevelDate=" + currTime + "," +
              		" ReorderLevel=ReorderLevel+"
                  + reLevel + " Where PartNo='" + pNo + "' or Interchangeno='" + pNo + "'";
          curReOrder = reLevel;
          curCnt++;
        } else if (curCnt < 50 && curReOrder == reLevel) {
          newSQL += " or PartNo='" + pNo + "' or Interchangeno='" + pNo + "'";
          curCnt++;
        } else {
          newInterStmt.execute(newSQL);
          newSQL = "";
          newSQL =
              "Update Parts Set " +
//              "ReOrderLevelDate=" + currTime + "," +
              		" ReorderLevel=ReorderLevel+"
                  + reLevel + " Where PartNo='" + pNo + "' or Interchangeno='" + pNo + "'";
          curCnt = 1;
          curReOrder = reLevel;
        }

        /*
         * pstmt3.clearParameters(); pstmt3.setInt(1, reLevel); pstmt3.setString(2, pNo);
         * pstmt3.setString(3, pNo);
         * 
         * pstmt3.execute();
         */

      }

      newInterStmt.execute(newSQL);

      rep.write("InterChange Items Changed : " + intCnt + "\n");
      rep.write("InterChange Items Skipped : " + intSkip + "\n");

      Statement stmtT = mySQLCon.createStatement();
      ResultSet rsT =
          stmtT
              .executeQuery("Select PartNo, sum(Quantity) from VendorOrderedItems a, VendorOrder b Where a.orderno=b.orderno and b.isFinal='Y' and b.updatedInventory='N' Group by 1 ");
      rep.write("RE-ORDER   -   GOT PARTS FOR UNITS ON ORDER\n");
      while (rsT.next()) {
        String pNo = rsT.getString(1);
        int units = rsT.getInt(2);
        if (pNo == null || pNo.trim().equals("")) {
          continue;
        }
        Statement stmtY = mySQLCon.createStatement();
        stmtY.execute("Update Parts Set UnitsOnOrder=" + units + " Where PartNo='" + pNo
            + "' or InterChangeNo='" + pNo + "'");
      }
      rep.write("RE-ORDER   -   UNITS ON ORDER --- DONE\n");
      rep.write("Total Time : " + (System.currentTimeMillis() - stTime) + "\n");

    } catch (Exception e) {
      try {
        rep.write(e + "\n");
      } catch (Exception ex) {
      }
    } finally {
      try {
        rep.close();
      } catch (Exception ex) {
      }
    }

  }

  static Connection getMySQLConnection() {
    Connection con = null;

    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      con =
          DriverManager.getConnection("jdbc:mysql://localhost/bvasDB?user=bvas&password=bvasdat39");
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
