package com.bvas.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;

public class FinMaintInv {
  private static final Logger logger = Logger.getLogger(FinMaintInv.class);

  public Hashtable getInvoices(String custId) {
    Hashtable invoices = new Hashtable();

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      long timeNow = System.currentTimeMillis();
      // 1000 * 60 * 60 * 24 * 120
      long period = 10368000000L;
      // Six Months back Date
      long oldDate = timeNow - period;
      java.util.Date dd = new java.util.Date(oldDate);
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
      String ddDate = sdf.format(dd);
      // logger.error("DDDATE=" + ddDate);

      String sql = "";
      sql +=
          "SELECT InvoiceNumber, OrderDate FROM Invoice WHERE CustomerId='" + custId
              + "' and OrderDate > '" + DateUtils.convertUSToMySQLFormat(ddDate)
              + "' order by InvoiceNumber";
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        int invNo = rs.getInt("InvoiceNumber");
        String ordDate = rs.getString("OrderDate");
        String invNoStr = invNo + "";

        invNoStr = padSpaces(invNoStr, 24);
        ordDate = padSpaces(ordDate, 48);

        String valu = invNoStr + "|" + ordDate;

        invoices.put(invNo + "", valu);

      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In FinMaintInv - Unable to get the Invoices -" + e);
    }

    return invoices;

  }

  public String padSpaces(String orig, int spaces) {
    String finStr = "";
    String oneSpace = "&nbsp;";

    try {
      finStr = orig.trim();
    } catch (Exception e) {
      logger.error(e);
      finStr = "";
    }

    while (finStr.length() <= spaces) {
      finStr += oneSpace;
    }

    return finStr;
  }

  public String getCustId(int invoiceNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    String custId = "";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT CustomerId FROM Invoice WHERE InvoiceNumber=" + invoiceNo);
      custId = rs.getString("CustomerId");
      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error("In FinMaintInv - Unable to get the Customer");
    }

    return custId;
  }
}
