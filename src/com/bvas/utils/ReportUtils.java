package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.beans.CustomerBean;
import com.bvas.beans.Driver;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.InvoiceDetailsBean;
import com.bvas.beans.InvoiceDetailsOurPrice;
import com.bvas.beans.LocalOrderBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;

public class ReportUtils {
  private static final Logger logger = Logger.getLogger(ReportUtils.class);

  public static Hashtable showDaysSales(UserBean user, String fromDate, String toDate,
      String salesPerson) throws UserException {
    Hashtable toShowSales = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    try {

      String fileName = "ShowSales" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[4][2];

      con = DBInterfaceLocal.getSQLConnection();
      stmt = con.createStatement();
      boolean getReturn = false;
      String sql =
          "SELECT OrderDate, SUM(InvoiceTotal), SUM(Discount), SUM(Tax) FROM Invoice WHERE ";
      if (!user.getRole().trim().equalsIgnoreCase("High")
          && !user.getRole().trim().equalsIgnoreCase("Medium")
          &&
          // !user.getRole().trim().equalsIgnoreCase("Acct") &&
          !user.getUsername().trim().equalsIgnoreCase("CorrinaNY")
          && !user.getUsername().trim().equalsIgnoreCase("Nancy")
          && !user.getUsername().trim().equalsIgnoreCase("Joses")) {
        sql += " SalesPerson='" + user.getUsername() + "' AND ";
        getReturn = true;
      } else if (salesPerson != null && !salesPerson.trim().equals("")) {
        sql += " SalesPerson='" + salesPerson.trim() + "' AND ";
        getReturn = true;
      }
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sql += " Group BY OrderDate ";
      // logger.error(sql);

      rs = stmt.executeQuery(sql);

      /*
       * double totInvoiceTotal = 0.0; double totDiscount = 0.0; double totTax = 0.0;
       */

      BigDecimal totInvoiceTotal = new BigDecimal("0.0");
      BigDecimal totDiscount = new BigDecimal("0.0");
      BigDecimal totTax = new BigDecimal("0.0");

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Report For The Date " + toDate.trim();
      } else {
        mainHeading = "Sales Report For The Period From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Order Date");
      subHeadings.addElement("Invoice Total");
      subHeadings.addElement("Discount");
      subHeadings.addElement("Tax");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        String orderDate = rs.getString(1);
        double invoiceTotal = rs.getDouble(2);
        double discount = rs.getDouble(3);
        double tax = rs.getDouble(4);

        String invoiceTotalStr = invoiceTotal + "";
        String discountStr = discount + "";
        String taxStr = tax + "";
        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }
        if (discountStr.indexOf(".") == discountStr.length() - 2) {
          discountStr += "0";
        }
        if (taxStr.indexOf(".") == taxStr.length() - 2) {
          taxStr += "0";
        }

        /*
         * totInvoiceTotal += invoiceTotal; totDiscount += discount; totTax += tax;
         */
        totInvoiceTotal = totInvoiceTotal.add(new BigDecimal(invoiceTotal));
        totDiscount = totDiscount.add(new BigDecimal(discount));
        totTax = totTax.add(new BigDecimal(tax));

        Hashtable totData = new Hashtable();
        totData.put("Order Date", DateUtils.convertMySQLToUSFormat(orderDate));
        totData.put("Invoice Total", invoiceTotalStr);
        totData.put("Discount", discountStr);
        totData.put("Tax", taxStr);

        data.addElement(totData);

      }

      if (getReturn) {

        String retSql =
            "SELECT a.OrderDate, SUM(a.InvoiceTotal), SUM(a.Discount), SUM(a.Tax) FROM Invoice a, Invoice b WHERE a.ReturnedInvoice!=0 and a.ReturnedInvoice=b.InvoiceNumber and ";
        if (!user.getRole().trim().equalsIgnoreCase("High")
            && !user.getRole().trim().equalsIgnoreCase("Medium")
            && !user.getRole().trim().equalsIgnoreCase("Acct")
            && !user.getUsername().trim().equalsIgnoreCase("Eddie")) {
          retSql +=
              " b.SalesPerson='" + user.getUsername() + "' AND a.SalesPerson!='"
                  + user.getUsername() + "' AND ";
        } else if (salesPerson != null && !salesPerson.trim().equals("")) {
          retSql +=
              " b.SalesPerson='" + salesPerson.trim() + "' AND a.SalesPerson!='"
                  + user.getUsername() + "' AND ";
        }
        if (fromDate.trim().equals(toDate.trim())) {
          retSql += " a.OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        } else {
          retSql +=
              " a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                  + "' AND a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        }
        retSql += " Group BY a.OrderDate ";

        rs1 = stmt.executeQuery(retSql);

        while (rs1.next()) {
          if (toShowSales == null) {
            toShowSales = new Hashtable();
          }

          String orderDate = rs1.getString(1);
          double invoiceTotal = rs1.getDouble(2);
          double discount = rs1.getDouble(3);
          double tax = rs1.getDouble(4);

          String invoiceTotalStr = invoiceTotal + "";
          String discountStr = discount + "";
          String taxStr = tax + "";
          if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
            invoiceTotalStr += "0";
          }
          if (discountStr.indexOf(".") == discountStr.length() - 2) {
            discountStr += "0";
          }
          if (taxStr.indexOf(".") == taxStr.length() - 2) {
            taxStr += "0";
          }

          /*
           * totInvoiceTotal += invoiceTotal; totDiscount += discount; totTax += tax;
           */
          totInvoiceTotal = totInvoiceTotal.add(new BigDecimal(invoiceTotal));
          totDiscount = totDiscount.add(new BigDecimal(discount));
          totTax = totTax.add(new BigDecimal(tax));

          Hashtable totData = new Hashtable();
          totData.put("Order Date", DateUtils.convertMySQLToUSFormat(orderDate));
          totData.put("Invoice Total", invoiceTotalStr);
          totData.put("Discount", discountStr);
          totData.put("Tax", taxStr);

          data.addElement(totData);

        }
      }

      if (toShowSales == null) {
        throw new UserException(" No Sales For This Period ");
      }

      totInvoiceTotal = totInvoiceTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
      totDiscount = totDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
      totTax = totTax.setScale(2, BigDecimal.ROUND_HALF_UP);
      String totInvoiceTotalStr = totInvoiceTotal.toString();
      String totDiscountStr = totDiscount.toString();
      String totTaxStr = totTax.toString();
      BigDecimal grossTotal = totInvoiceTotal.add(totTax);
      grossTotal = grossTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
      BigDecimal netTotal = (grossTotal.subtract(totTax)).subtract(totDiscount);
      netTotal = netTotal.setScale(2, BigDecimal.ROUND_HALF_UP);

      totals[0][0] = "Gross Total";
      totals[0][1] = grossTotal.toString();
      totals[1][0] = "Discount";
      totals[1][1] = totDiscountStr;
      totals[2][0] = "Tax";
      totals[2][1] = totTaxStr;
      totals[3][0] = "Net Total";
      totals[3][1] = netTotal.toString();

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      createReport(toShowSales);
      rs.close();

      stmt.close();

      con.close();
    } catch (SQLException e) {
      logger.error(e);;
      // throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showSalesForPers(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "ShowSales" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[5][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmtX = con.createStatement();
      String sqlX = "SELECT Count(Distinct OrderDate) FROM Invoice WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sqlX += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sqlX +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }

      ResultSet rsX = stmtX.executeQuery(sqlX);

      int noOfDays = 0;

      if (rsX.next()) {
        noOfDays = rsX.getInt(1);
      }
      if (noOfDays == 0) {
        noOfDays = 1;
      }

      Statement stmt = con.createStatement();
      String sql =
          "SELECT InvoiceNumber, SalesPerson, InvoiceTotal, Tax, Discount, ReturnedInvoice FROM Invoice WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      System.out.println(sql);
      ResultSet rs = stmt.executeQuery(sql);

      /*
       * double totInvoiceTotal = 0.0; double totDiscount = 0.0; double totTax = 0.0; double totAvg
       * = 0.0;
       */

      BigDecimal totInvoiceTotal = new BigDecimal("0.0");
      BigDecimal totDiscount = new BigDecimal("0.0");
      BigDecimal totTax = new BigDecimal("0.0");
      BigDecimal totAvg = new BigDecimal("0.0");

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Report For The Date " + toDate.trim();
      } else {
        mainHeading = "Sales Report For The Period From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Invoice Total");
      subHeadings.addElement("Discount");
      subHeadings.addElement("Tax");
      subHeadings.addElement("Average");

      String[][] arrSales = new String[50][4];
      for (int i = 0; i < 50; i++) {
        arrSales[i][0] = "";
        arrSales[i][1] = "0.0";
        arrSales[i][2] = "0.0";
        arrSales[i][3] = "0.0";
      }

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        int invNo = rs.getInt(1);
        String salesPerson = rs.getString(2);
        double invoiceTotal = rs.getDouble(3);
        double discount = rs.getDouble(5);
        double tax = rs.getDouble(4);
        int retInv = rs.getInt(6);

        salesPerson = salesPerson.trim();

        int currCnt = -1;
        String currPers = "";
        if (retInv == 0) {
          currPers = salesPerson;
        } else {
          Statement stmtXX = con.createStatement();
          ResultSet rsXX =
              stmtXX.executeQuery("Select SalesPerson From Invoice Where InvoiceNumber=" + retInv);
          if (rsXX.next()) {
            currPers = rsXX.getString(1);
          }
          /*
           * InvoiceBean retBean = InvoiceBean.getInvoice(retInv); if (
           * retBean.getSalesPerson().trim().equals(salesPerson.trim() )) { currPers = salesPerson;
           * } else { currPers = retBean.getSalesPerson(); }
           */
          // 10/12/2013 : Changes done as a part of review : Begin
          stmtXX.close();
          // 10/12/2013 : Changes done as a part of review : End
        }

        int alreadyIn = 0;
        for (int i = 0; i < 50; i++) {
          if (!arrSales[i][0].trim().equals("")) {
            alreadyIn++;
          }
          if (arrSales[i][0].trim().equals(currPers)) {
            currCnt = i;
          }
        }

        if (currCnt == -1) {
          arrSales[alreadyIn][0] = currPers;
          arrSales[alreadyIn][1] = invoiceTotal + "";
          arrSales[alreadyIn][2] = discount + "";
          arrSales[alreadyIn][3] = tax + "";
          // logger.error("First Time Adding For " + currPers);
        } else {
          arrSales[currCnt][1] = invoiceTotal + Double.parseDouble(arrSales[currCnt][1]) + "";
          arrSales[currCnt][2] = discount + Double.parseDouble(arrSales[currCnt][2]) + "";
          arrSales[currCnt][3] = tax + Double.parseDouble(arrSales[currCnt][3]) + "";
        }

      }

      for (int i = 0; i < 50; i++) {
        String salesPers = arrSales[i][0];
        if (salesPers.trim().equals("")) {
          break;
        }

        double avg = 0.0;

        Statement stmtY = con.createStatement();
        String sqlY =
            "SELECT Count(Distinct OrderDate) FROM Invoice WHERE SalesPerson='" + salesPers
                + "' and ";
        if (fromDate.trim().equals(toDate.trim())) {
          sqlY += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        } else {
          sqlY +=
              " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                  + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        }

        ResultSet rsY = stmtY.executeQuery(sqlY);

        int noOfIndDays = 0;

        if (rsY.next()) {
          noOfIndDays = rsY.getInt(1);
        }
        if (noOfIndDays == 0) {
          noOfIndDays = 1;
        }

        avg = Double.parseDouble(arrSales[i][1]) / noOfIndDays;

        String invoiceTotalStr = Double.parseDouble(arrSales[i][1]) + "";
        String discountStr = Double.parseDouble(arrSales[i][2]) + "";
        String taxStr = Double.parseDouble(arrSales[i][3]) + "";
        String avgStr = avg + "";

        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }
        if (discountStr.indexOf(".") == discountStr.length() - 2) {
          discountStr += "0";
        }
        if (taxStr.indexOf(".") == taxStr.length() - 2) {
          taxStr += "0";
        }
        if (avgStr.indexOf(".") == avgStr.length() - 2) {
          avgStr += "0";
        }
        invoiceTotalStr = NumberUtils.cutFractions(invoiceTotalStr);
        discountStr = NumberUtils.cutFractions(discountStr);
        taxStr = NumberUtils.cutFractions(taxStr);
        avgStr = NumberUtils.cutFractions(avgStr);

        /*
         * totInvoiceTotal += Double.parseDouble(arrSales[i][1]); totDiscount +=
         * Double.parseDouble(arrSales[i][2]); totTax += Double.parseDouble(arrSales[i][3]);
         */
        totInvoiceTotal = totInvoiceTotal.add(new BigDecimal(Double.parseDouble(arrSales[i][1])));
        totDiscount = totDiscount.add(new BigDecimal(Double.parseDouble(arrSales[i][2])));
        totTax = totTax.add(new BigDecimal(Double.parseDouble(arrSales[i][3])));

        Hashtable totData = new Hashtable();
        totData.put("Sales Person", salesPers);
        totData.put("Invoice Total", invoiceTotalStr);
        totData.put("Discount", discountStr);
        totData.put("Tax", taxStr);
        totData.put("Average", avgStr);

        data.addElement(totData);

        // 10/12/2013 : Changes done as a part of review : Begin
        stmtY.close();
        // 10/12/2013 : Changes done as a part of review : End

      }

      if (toShowSales == null) {
        throw new UserException(" No Sales For This Period ");
      }

      totInvoiceTotal = totInvoiceTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
      totDiscount = totDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
      totTax = totTax.setScale(2, BigDecimal.ROUND_HALF_UP);

      /*
       * String totInvoiceTotalStr = totInvoiceTotal+""; String totDiscountStr = totDiscount+"";
       * String totTaxStr = totTax + ""; String netTotal = totInvoiceTotal - totDiscount + totTax +
       * "";
       */

      String totInvoiceTotalStr = totInvoiceTotal.add(totTax).toString();
      String totDiscountStr = totDiscount.toString();
      String totTaxStr = totTax.toString();
      BigDecimal netTotal = totInvoiceTotal.subtract(totDiscount);

      /*
       * totAvg = totInvoiceTotal / noOfDays; String totAvgStr = totAvg + "";
       */

      totAvg = totInvoiceTotal.divide(new BigDecimal(noOfDays), MathContext.DECIMAL32);
      totAvg = totAvg.setScale(2, BigDecimal.ROUND_HALF_UP);
      String totAvgStr = totAvg.toString();
      netTotal = netTotal.setScale(2, BigDecimal.ROUND_HALF_UP);

      /*
       * if (totInvoiceTotalStr.indexOf(".") == totInvoiceTotalStr.length()-2) { totInvoiceTotalStr
       * += "0"; } if (totDiscountStr.indexOf(".") == totDiscountStr.length()-2) { totDiscountStr +=
       * "0"; } if (totTaxStr.indexOf(".") == totTaxStr.length()-2) { totTaxStr += "0"; } if
       * (netTotal.indexOf(".") == netTotal.length()-2) { netTotal += "0"; } if
       * (totAvgStr.indexOf(".") == totAvgStr.length()-2) { totAvgStr += "0"; }
       */

      /*
       * totInvoiceTotalStr = NumberUtils.cutFractions(totInvoiceTotalStr); totDiscountStr =
       * NumberUtils.cutFractions(totDiscountStr); totTaxStr = NumberUtils.cutFractions(totTaxStr);
       * netTotal = NumberUtils.cutFractions(netTotal); totAvgStr =
       * NumberUtils.cutFractions(totAvgStr);
       */

      totals[0][0] = "Gross Total";
      totals[0][1] = totInvoiceTotalStr;
      totals[1][0] = "Discount";
      totals[1][1] = totDiscountStr;
      totals[2][0] = "Tax";
      totals[2][1] = totTaxStr;
      totals[3][0] = "Average";
      totals[3][1] = totAvgStr;
      totals[4][0] = "Net Total";
      // totals[4][1] = netTotal;
      totals[4][1] = netTotal.toString();

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      createReport(toShowSales);
      rs.close();

      stmt.close();

      // 10/12/2013 : Changes done as a part of review : Begin
      stmtX.close();
      // 10/12/2013 : Changes done as a part of review : End

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showSalesForRt(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "ShowRtSales" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      double totalSalesRt = 0.0;

      Statement stmt = con.createStatement();
      String sql = "SELECT Region, Sum(InvoiceTotal) FROM Invoice a, Address b WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " a.OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sql += " and a.CustomerId=b.Id and b.type='Standard' Group by Region ";

      ResultSet rs = stmt.executeQuery(sql);

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Report By Route " + toDate.trim();
      } else {
        mainHeading = "Sales Report By Route From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Route No");
      subHeadings.addElement("Amount");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        String rtNo = rs.getString(1);
        double amt = rs.getDouble(2);
        totalSalesRt += amt;

        Hashtable totData = new Hashtable();
        totData.put("Route No", rtNo);
        totData.put("Amount", amt + "");

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Sales For This Period ");
      }

      String totalSalesRtStr = totalSalesRt + "";

      if (totalSalesRtStr.indexOf(".") == totalSalesRtStr.length() - 2) {
        totalSalesRtStr += "0";
      }

      totalSalesRtStr = NumberUtils.cutFractions(totalSalesRtStr);

      totals[0][0] = "Total Sales";
      totals[0][1] = totalSalesRtStr;

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      createReport(toShowSales);
      rs.close();

      stmt.close();

      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showReturns(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowReturns = null;
    try {

      String fileName = "ShowReturns" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[5][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmtX = con.createStatement();
      String sqlX = "SELECT Count(Distinct OrderDate) FROM Invoice WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sqlX += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sqlX +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }

      ResultSet rsX = stmtX.executeQuery(sqlX);

      int noOfDays = 0;

      if (rsX.next()) {
        noOfDays = rsX.getInt(1);
      }
      if (noOfDays == 0) {
        noOfDays = 1;
      }

      Statement stmt = con.createStatement();
      String sql =
          "SELECT InvoiceNumber, SalesPerson, InvoiceTotal, Tax, Discount, ReturnedInvoice FROM Invoice WHERE ReturnedInvoice!=0 and ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }

      ResultSet rs = stmt.executeQuery(sql);

      double totInvoiceTotal = 0.0;
      double totDiscount = 0.0;
      double totTax = 0.0;
      double totAvg = 0.0;

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Returns For The Date " + toDate.trim();
      } else {
        mainHeading = "Sales Returns For The Period From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Invoice Total");
      subHeadings.addElement("Discount");
      subHeadings.addElement("Tax");
      subHeadings.addElement("Average");

      String[][] arrSales = new String[50][4];
      for (int i = 0; i < 50; i++) {
        arrSales[i][0] = "";
        arrSales[i][1] = "0.0";
        arrSales[i][2] = "0.0";
        arrSales[i][3] = "0.0";
      }

      while (rs.next()) {
        if (toShowReturns == null) {
          toShowReturns = new Hashtable();
        }

        int invNo = rs.getInt(1);
        String salesPerson = rs.getString(2);
        double invoiceTotal = rs.getDouble(3);
        double discount = rs.getDouble(5);
        double tax = rs.getDouble(4);
        int retInv = rs.getInt(6);

        salesPerson = salesPerson.trim();

        int currCnt = -1;
        String currPers = "";
        if (retInv == 0) {
          currPers = salesPerson;
        } else {
          Statement stmtXX = con.createStatement();
          ResultSet rsXX =
              stmtXX.executeQuery("Select SalesPerson From Invoice Where InvoiceNumber=" + retInv);
          if (rsXX.next()) {
            currPers = rsXX.getString(1);
          }
          /*
           * InvoiceBean retBean = InvoiceBean.getInvoice(retInv); if (
           * retBean.getSalesPerson().trim().equals(salesPerson.trim() )) { currPers = salesPerson;
           * } else { currPers = retBean.getSalesPerson(); }
           */

          // 10/12/2013 : Changes done as a part of review : Begin
          stmtXX.close();
          // 10/12/2013 : Changes done as a part of review : End

        }

        int alreadyIn = 0;
        for (int i = 0; i < 50; i++) {
          if (!arrSales[i][0].trim().equals("")) {
            alreadyIn++;
          }
          if (arrSales[i][0].trim().equals(currPers)) {
            currCnt = i;
          }
        }

        if (currCnt == -1) {
          arrSales[alreadyIn][0] = currPers;
          arrSales[alreadyIn][1] = invoiceTotal + "";
          arrSales[alreadyIn][2] = discount + "";
          arrSales[alreadyIn][3] = tax + "";
          // logger.error("First Time Adding For " + currPers);
        } else {
          arrSales[currCnt][1] = invoiceTotal + Double.parseDouble(arrSales[currCnt][1]) + "";
          arrSales[currCnt][2] = discount + Double.parseDouble(arrSales[currCnt][2]) + "";
          arrSales[currCnt][3] = tax + Double.parseDouble(arrSales[currCnt][3]) + "";
        }

      }

      for (int i = 0; i < 50; i++) {
        String salesPers = arrSales[i][0];
        if (salesPers.trim().equals("")) {
          break;
        }

        double avg = 0.0;

        Statement stmtY = con.createStatement();
        String sqlY =
            "SELECT Count(Distinct OrderDate) FROM Invoice WHERE SalesPerson='" + salesPers
                + "' and ";
        if (fromDate.trim().equals(toDate.trim())) {
          sqlY += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        } else {
          sqlY +=
              " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                  + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        }

        ResultSet rsY = stmtY.executeQuery(sqlY);

        int noOfIndDays = 0;

        if (rsY.next()) {
          noOfIndDays = rsY.getInt(1);
        }
        if (noOfIndDays == 0) {
          noOfIndDays = 1;
        }

        avg = Double.parseDouble(arrSales[i][1]) / noOfIndDays;

        String invoiceTotalStr = Double.parseDouble(arrSales[i][1]) + "";
        String discountStr = Double.parseDouble(arrSales[i][2]) + "";
        String taxStr = Double.parseDouble(arrSales[i][3]) + "";
        String avgStr = avg + "";

        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }
        if (discountStr.indexOf(".") == discountStr.length() - 2) {
          discountStr += "0";
        }
        if (taxStr.indexOf(".") == taxStr.length() - 2) {
          taxStr += "0";
        }
        if (avgStr.indexOf(".") == avgStr.length() - 2) {
          avgStr += "0";
        }
        invoiceTotalStr = NumberUtils.cutFractions(invoiceTotalStr);
        discountStr = NumberUtils.cutFractions(discountStr);
        taxStr = NumberUtils.cutFractions(taxStr);
        avgStr = NumberUtils.cutFractions(avgStr);

        totInvoiceTotal += Double.parseDouble(arrSales[i][1]);
        totDiscount += Double.parseDouble(arrSales[i][2]);
        totTax += Double.parseDouble(arrSales[i][3]);

        Hashtable totData = new Hashtable();
        totData.put("Sales Person", salesPers);
        totData.put("Invoice Total", invoiceTotalStr);
        totData.put("Discount", discountStr);
        totData.put("Tax", taxStr);
        totData.put("Average", avgStr);

        data.addElement(totData);

        // 10/12/2013 : Changes done as a part of review : Begin
        stmtY.close();
        // 10/12/2013 : Changes done as a part of review : End

      }

      if (toShowReturns == null) {
        throw new UserException(" No Sales For This Period ");
      }

      String totInvoiceTotalStr = totInvoiceTotal + "";
      String totDiscountStr = totDiscount + "";
      String totTaxStr = totTax + "";
      String netTotal = totInvoiceTotal - totDiscount + totTax + "";

      totAvg = totInvoiceTotal / noOfDays;
      String totAvgStr = totAvg + "";

      if (totInvoiceTotalStr.indexOf(".") == totInvoiceTotalStr.length() - 2) {
        totInvoiceTotalStr += "0";
      }
      if (totDiscountStr.indexOf(".") == totDiscountStr.length() - 2) {
        totDiscountStr += "0";
      }
      if (totTaxStr.indexOf(".") == totTaxStr.length() - 2) {
        totTaxStr += "0";
      }
      if (netTotal.indexOf(".") == netTotal.length() - 2) {
        netTotal += "0";
      }
      if (totAvgStr.indexOf(".") == totAvgStr.length() - 2) {
        totAvgStr += "0";
      }

      totInvoiceTotalStr = NumberUtils.cutFractions(totInvoiceTotalStr);
      totDiscountStr = NumberUtils.cutFractions(totDiscountStr);
      totTaxStr = NumberUtils.cutFractions(totTaxStr);
      netTotal = NumberUtils.cutFractions(netTotal);
      totAvgStr = NumberUtils.cutFractions(totAvgStr);

      totals[0][0] = "Gross Total";
      totals[0][1] = totInvoiceTotalStr;
      totals[1][0] = "Discount";
      totals[1][1] = totDiscountStr;
      totals[2][0] = "Tax";
      totals[2][1] = totTaxStr;
      totals[3][0] = "Average";
      totals[3][1] = totAvgStr;
      totals[4][0] = "Net Total";
      totals[4][1] = netTotal;

      toShowReturns.put("FileName", fileName);
      toShowReturns.put("BackScreen", "TodaysOrders");
      toShowReturns.put("MainHeading", mainHeading);
      toShowReturns.put("SubHeadings", subHeadings);
      toShowReturns.put("Data", data);
      toShowReturns.put("Totals", totals);

      createReport(toShowReturns);
      rs.close();

      // 10/12/2013 : Changes done as a part of review : Begin
      stmtX.close();
      // 10/12/2013 : Changes done as a part of review : End

      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowReturns;
  }

  public static Hashtable showSalesForOldPers(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "ShowSales" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[5][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmtX = con.createStatement();
      String sqlX = "SELECT Count(Distinct OrderDate) FROM Invoice WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sqlX += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sqlX +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }

      ResultSet rsX = stmtX.executeQuery(sqlX);

      int noOfDays = 0;

      if (rsX.next()) {
        noOfDays = rsX.getInt(1);
      }
      if (noOfDays == 0) {
        noOfDays = 1;
      }

      Statement stmt = con.createStatement();
      String sql =
          "SELECT SalesPerson, SUM(InvoiceTotal), SUM(Discount), SUM(Tax) FROM Invoice WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sql += " Group BY SalesPerson Order By SalesPerson ";

      ResultSet rs = stmt.executeQuery(sql);

      double totInvoiceTotal = 0.0;
      double totDiscount = 0.0;
      double totTax = 0.0;
      double totAvg = 0.0;

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Report For The Date " + toDate.trim();
      } else {
        mainHeading = "Sales Report For The Period From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Invoice Total");
      subHeadings.addElement("Discount");
      subHeadings.addElement("Tax");
      subHeadings.addElement("Average");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        String salesPerson = rs.getString(1);
        double invoiceTotal = rs.getDouble(2);
        double discount = rs.getDouble(3);
        double tax = rs.getDouble(4);
        double avg = 0.0;

        Statement stmtY = con.createStatement();
        String sqlY =
            "SELECT Count(Distinct OrderDate) FROM Invoice WHERE SalesPerson='" + salesPerson
                + "' and ";
        if (fromDate.trim().equals(toDate.trim())) {
          sqlY += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        } else {
          sqlY +=
              " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                  + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
        }

        ResultSet rsY = stmtY.executeQuery(sqlY);

        int noOfIndDays = 0;

        if (rsY.next()) {
          noOfIndDays = rsY.getInt(1);
        }
        if (noOfIndDays == 0) {
          noOfIndDays = 1;
        }

        avg = invoiceTotal / noOfIndDays;

        String invoiceTotalStr = invoiceTotal + "";
        String discountStr = discount + "";
        String taxStr = tax + "";
        String avgStr = avg + "";

        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }
        if (discountStr.indexOf(".") == discountStr.length() - 2) {
          discountStr += "0";
        }
        if (taxStr.indexOf(".") == taxStr.length() - 2) {
          taxStr += "0";
        }
        if (avgStr.indexOf(".") == avgStr.length() - 2) {
          avgStr += "0";
        }
        avgStr = NumberUtils.cutFractions(avgStr);

        totInvoiceTotal += invoiceTotal;
        totDiscount += discount;
        totTax += tax;

        Hashtable totData = new Hashtable();
        totData.put("Sales Person", salesPerson);
        totData.put("Invoice Total", invoiceTotalStr);
        totData.put("Discount", discountStr);
        totData.put("Tax", taxStr);
        totData.put("Average", avgStr);

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Sales For This Period ");
      }

      String totInvoiceTotalStr = totInvoiceTotal + "";
      String totDiscountStr = totDiscount + "";
      String totTaxStr = totTax + "";
      String netTotal = totInvoiceTotal - totDiscount + totTax + "";

      totAvg = totInvoiceTotal / noOfDays;
      String totAvgStr = totAvg + "";

      if (totInvoiceTotalStr.indexOf(".") == totInvoiceTotalStr.length() - 2) {
        totInvoiceTotalStr += "0";
      }
      if (totDiscountStr.indexOf(".") == totDiscountStr.length() - 2) {
        totDiscountStr += "0";
      }
      if (totTaxStr.indexOf(".") == totTaxStr.length() - 2) {
        totTaxStr += "0";
      }
      if (netTotal.indexOf(".") == netTotal.length() - 2) {
        netTotal += "0";
      }
      if (totAvgStr.indexOf(".") == totAvgStr.length() - 2) {
        totAvgStr += "0";
      }

      totInvoiceTotalStr = NumberUtils.cutFractions(totInvoiceTotalStr);
      totDiscountStr = NumberUtils.cutFractions(totDiscountStr);
      totTaxStr = NumberUtils.cutFractions(totTaxStr);
      netTotal = NumberUtils.cutFractions(netTotal);
      totAvgStr = NumberUtils.cutFractions(totAvgStr);

      totals[0][0] = "Gross Total";
      totals[0][1] = totInvoiceTotalStr;
      totals[1][0] = "Discount";
      totals[1][1] = totDiscountStr;
      totals[2][0] = "Tax";
      totals[2][1] = totTaxStr;
      totals[3][0] = "Average";
      totals[3][1] = totAvgStr;
      totals[4][0] = "Net Total";
      totals[4][1] = netTotal;

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      createReport(toShowSales);
      rs.close();
      rsX.close();
      stmt.close();
      stmtX.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showSalesForCust(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "ShowCustSales" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[3][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmtX = con.createStatement();
      String sqlX =
          "SELECT a.CustomerId, b.CompanyName, Sum(a.InvoiceTotal), b.CustomerLevel, Max(a.OrderDate) FROM Invoice a, Customer b WHERE a.CustomerId=b.CustomerId AND ";
      if (fromDate.trim().equals(toDate.trim())) {
        sqlX += " OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sqlX +=
            " OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sqlX += " Group By a.CustomerId Order By 3 Desc ";

      ResultSet rsX = stmtX.executeQuery(sqlX);

      double totInvoiceTotal = 0.0;
      int noOfCust = 0;
      int totalLvlCust = 0;

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Report By Customers For The Date " + toDate.trim();
      } else {
        mainHeading = "Sales Report By Customers For The Period From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Customer Id");
      subHeadings.addElement("Company Name");
      subHeadings.addElement("Last Order");
      subHeadings.addElement("Amount Bought");
      subHeadings.addElement("Level");

      while (rsX.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        noOfCust++;

        String custId = rsX.getString(1);
        String companyName = rsX.getString(2);
        double invoiceTotal = rsX.getDouble(3);
        int lvl = rsX.getInt(4);
        String orderDate = DateUtils.convertMySQLToUSFormat(rsX.getString(5));

        if (lvl != 0) {
          totalLvlCust++;
        }

        String invoiceTotalStr = invoiceTotal + "";

        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }

        totInvoiceTotal += invoiceTotal;

        Hashtable totData = new Hashtable();
        totData.put("Customer Id", custId);
        totData.put("Company Name", companyName);
        totData.put("Last Order", orderDate);
        totData.put("Amount Bought", invoiceTotalStr);
        totData.put("Level", lvl + "");

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Sales For This Period ");
      }

      String totInvoiceTotalStr = totInvoiceTotal + "";

      if (totInvoiceTotalStr.indexOf(".") == totInvoiceTotalStr.length() - 2) {
        totInvoiceTotalStr += "0";
      }

      totInvoiceTotalStr = NumberUtils.cutFractions(totInvoiceTotalStr);

      totals[0][0] = "Total Amount";
      totals[0][1] = totInvoiceTotalStr;
      totals[1][0] = "No Of Customers";
      totals[1][1] = noOfCust + "";
      totals[2][0] = "No Of Leveled Customers";
      totals[2][1] = totalLvlCust + "";

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      createReport(toShowSales);

      rsX.close();
      stmtX.close();
      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showPartsSold(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "ShowPartsSales" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[8][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmtX = con.createStatement();
      String sqlX =
          "SELECT a.InvoiceNumber, a.PartNumber, a.Quantity, a.SoldPrice, a.ActualPrice, c.CostPrice, c.ActualPrice, c.UnitsInStock, c.UnitsOnOrder, c.ReorderLevel" +
         // ", c.CompPrice1, c.CompPrice2" +
          " FROM InvoiceDetails a, Invoice b, Parts c WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sqlX += " b.OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sqlX +=
            " b.OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND b.OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sqlX += " and b.InvoiceNumber=a.InvoiceNumber and a.PartNumber=c.PartNo Order By 1, 2 ";

      ResultSet rsX = stmtX.executeQuery(sqlX);

      int totalNoOfParts = 0;
      int totalNoOfItems = 0;
      double totalSoldPrice = 0.0;
      double totalInvActual = 0.0;
      double totalPartsCost = 0.0;
      double totalPartsActual = 0.0;
      double totalActualPerc = 0.0;
      double partsPerc = 0.0;

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Sales Report By Parts For The Date " + toDate.trim();
      } else {
        mainHeading = "Sales Report By Parts For The Period From " + fromDate + " To " + toDate;
      }
      subHeadings.addElement("Part / Inv");
      subHeadings.addElement("Qty/Sold/Act");
      subHeadings.addElement("Cost/Act");
      subHeadings.addElement("Units");
      //subHeadings.addElement("Comp");
      subHeadings.addElement("Perc");
      subHeadings.addElement("Rmks");

      while (rsX.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        int invNo = rsX.getInt(1);
        String partNo = rsX.getString(2);
        if ((partNo.startsWith("XX") || partNo.startsWith("xx")) && partNo.length() == 7) {
          continue;
        }
        int qty = rsX.getInt(3);
        double sold = rsX.getDouble(4);
        double invAct = rsX.getDouble(5);
        double partCost = rsX.getDouble(6);
        double partAct = rsX.getDouble(7);
        int units = rsX.getInt(8);
        int onOrder = rsX.getInt(9);
        int reorder = rsX.getInt(10);
//        double comp1 = rsX.getDouble(11);
//        double comp2 = rsX.getDouble(12);

        double perc = 0.0;
        double shouldBePerc = 0.0;
        String remarks = "&nbsp;";

        if (partAct == 0 && invAct != 0) {
          partAct = invAct;
        }

        if (sold > 0 && invAct > 0) {
          perc = (sold - invAct) * 100 / sold;
          perc = Math.rint(perc);
        } else {
          perc = 0;
        }

        if (invAct > 0) {
          if (invAct < 1) {
            shouldBePerc = 70;
          } else if (invAct < 2) {
            shouldBePerc = 67;
          } else if (invAct < 3) {
            shouldBePerc = 65;
          } else if (invAct < 4) {
            shouldBePerc = 63;
          } else if (invAct < 5) {
            shouldBePerc = 61;
          } else if (invAct < 8) {
            shouldBePerc = 59;
          } else if (invAct < 11) {
            shouldBePerc = 57;
          } else if (invAct < 14) {
            shouldBePerc = 55;
          } else if (invAct < 18) {
            shouldBePerc = 53;
          } else if (invAct < 23) {
            shouldBePerc = 51;
          } else if (invAct < 28) {
            shouldBePerc = 49;
          } else if (invAct < 35) {
            shouldBePerc = 47;
          } else if (invAct < 42) {
            shouldBePerc = 45;
          } else if (invAct < 50) {
            shouldBePerc = 43;
          } else if (invAct < 60) {
            shouldBePerc = 41;
          } else if (invAct < 70) {
            shouldBePerc = 39;
          } else if (invAct < 80) {
            shouldBePerc = 37;
          } else if (invAct < 90) {
            shouldBePerc = 35;
          } else if (invAct < 125) {
            shouldBePerc = 30;
          } else if (invAct < 150) {
            shouldBePerc = 25;
          } else {
            shouldBePerc = 20;
          }
        } else {
          shouldBePerc = 0;
        }

        if (perc > shouldBePerc + 10) {
          remarks = "High&nbsp;Price";
        } else if (perc < shouldBePerc - 10) {
          remarks = "Low&nbsp;Price";
        }
        totalNoOfParts++;
        totalNoOfItems += qty;
        totalSoldPrice += (sold * qty);
        totalInvActual += (invAct * qty);
        totalPartsCost += (partCost * qty);
        totalPartsActual += (partAct * qty);

        String partNoInvNo = partNo + "&nbsp;/&nbsp;" + invNo;
        String qtySoldAct = qty + "&nbsp;/&nbsp;" + sold + "&nbsp;/&nbsp;" + invAct;
        String costAct = partCost + "&nbsp;/&nbsp;" + partAct;
        String unitsOrderReorder = units + "&nbsp;/&nbsp;" + onOrder + "&nbsp;/&nbsp;" + reorder;
//        String comp1Comp2 = comp1 + "&nbsp;/&nbsp;" + comp2;
        String percShouldBePerc = perc + "&nbsp;/&nbsp;" + shouldBePerc;

        Hashtable totData = new Hashtable();
        totData.put("Part / Inv", partNoInvNo);
        totData.put("Qty/Sold/Act", qtySoldAct);
        totData.put("Cost/Act", costAct);
        totData.put("Units", unitsOrderReorder);
//        totData.put("Comp", comp1Comp2);
        totData.put("Perc", percShouldBePerc);
        totData.put("Rmks", remarks);

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Sales For This Period ");
      }

      double actualPerc = 0.0;
      double shouldBePerc = 0.0;

      String totalSoldPriceStr = totalSoldPrice + "";
      if (totalSoldPriceStr.indexOf(".") == totalSoldPriceStr.length() - 2) {
        totalSoldPriceStr += "0";
      }
      totalSoldPriceStr = NumberUtils.cutFractions(totalSoldPriceStr);

      String totalInvActualStr = totalInvActual + "";
      if (totalInvActualStr.indexOf(".") == totalInvActualStr.length() - 2) {
        totalInvActualStr += "0";
      }
      totalInvActualStr = NumberUtils.cutFractions(totalInvActualStr);

      String totalPartsCostStr = totalPartsCost + "";
      if (totalPartsCostStr.indexOf(".") == totalPartsCostStr.length() - 2) {
        totalPartsCostStr += "0";
      }
      totalPartsCostStr = NumberUtils.cutFractions(totalPartsCostStr);

      String totalPartsActualStr = totalPartsActual + "";
      if (totalPartsActualStr.indexOf(".") == totalPartsActualStr.length() - 2) {
        totalPartsActualStr += "0";
      }
      totalPartsActualStr = NumberUtils.cutFractions(totalPartsActualStr);

      totalActualPerc = (totalSoldPrice - totalInvActual) * 100 / totalSoldPrice;
      totalActualPerc = Math.rint(totalActualPerc);
      partsPerc = (totalPartsCost - totalPartsActual) * 100 / totalPartsCost;
      partsPerc = Math.rint(partsPerc);

      totals[0][0] = "Total No Of Parts";
      totals[0][1] = totalNoOfParts + "";
      totals[1][0] = "Total No Of Items";
      totals[1][1] = totalNoOfItems + "";
      totals[2][0] = "Total Sold Price";
      totals[2][1] = totalSoldPriceStr;
      totals[3][0] = "Total Invoice Actual";
      totals[3][1] = totalInvActualStr;
      totals[4][0] = "Total Parts Cost";
      totals[4][1] = totalPartsCostStr;
      totals[5][0] = "Total Parts Actual";
      totals[5][1] = totalPartsActualStr;
      totals[6][0] = "Actual Perc";
      totals[6][1] = totalActualPerc + "";
      totals[7][0] = "Perc On Parts";
      totals[7][1] = partsPerc + "";

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      createReport(toShowSales);

      rsX.close();
      stmtX.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showInvoices(UserBean user, String invoiceFromDate, String invoiceToDate,
      String salesPerson) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowInvoices" + invoiceFromDate.trim() + invoiceToDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[4][2];

      boolean getReturns = false;

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.SalesPerson, a.CustomerId, b.CompanyName, a.InvoiceTotal, a.Discount, a.Tax FROM Invoice a, Customer b WHERE ";
      if (!user.getRole().trim().equalsIgnoreCase("High")
          && !user.getRole().trim().equalsIgnoreCase("Medium")
          && !user.getRole().trim().equalsIgnoreCase("Acct")
          && !user.getUsername().trim().equalsIgnoreCase("Marcie")
          && !user.getUsername().trim().equalsIgnoreCase("Nancy")
          && !user.getUsername().trim().equalsIgnoreCase("Eddie")) {
        sql += " a.SalesPerson='" + user.getUsername() + "' AND ";
        getReturns = true;
      } else if (salesPerson != null && !salesPerson.trim().equals("")) {
        sql += " a.SalesPerson='" + salesPerson.trim() + "' AND ";
        getReturns = true;
      }
      if (invoiceFromDate.trim().equals(invoiceToDate)) {
        sql += " a.OrderDate='" + DateUtils.convertUSToMySQLFormat(invoiceFromDate.trim()) + "'";
      } else {
        sql += " a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(invoiceFromDate.trim()) + "'";
        sql += " AND a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(invoiceToDate.trim()) + "'";
      }
      sql += " AND a.CustomerId=b.CustomerId Order By 1 ";
      // logger.error(sql);

      ResultSet rs = stmt.executeQuery(sql);

      double totInvoiceTotal = 0.0;
      double totDiscount = 0.0;
      double totTax = 0.0;

      if (invoiceFromDate.trim().equals(invoiceToDate)) {
        mainHeading = "Invoice Orders For The Date " + invoiceFromDate.trim();
      } else {
        mainHeading =
            "Invoice Orders From " + invoiceFromDate.trim() + " To " + invoiceToDate.trim();
      }
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Inv. Total");
      subHeadings.addElement("Discount");
      subHeadings.addElement("Tax");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String invoiceNo = rs.getString(1);
        String newSalesPerson = rs.getString(2);
        String custId = rs.getString(3);
        String custName = rs.getString(4);
        double invoiceTotal = rs.getDouble(5);
        double discount = rs.getDouble(6);
        double tax = rs.getDouble(7);

        String invoiceTotalStr = invoiceTotal + "";
        String discountStr = discount + "";
        String taxStr = tax + "";
        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }
        if (discountStr.indexOf(".") == discountStr.length() - 2) {
          discountStr += "0";
        }
        if (taxStr.indexOf(".") == taxStr.length() - 2) {
          taxStr += "0";
        }

        totInvoiceTotal += invoiceTotal;
        totDiscount += discount;
        totTax += tax;

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);
        totData.put("Inv. Total", invoiceTotalStr);
        totData.put("Discount", discountStr);
        totData.put("Tax", taxStr);

        data.addElement(totData);

      }

      if (getReturns) {

        String retSql =
            "SELECT a.InvoiceNumber, a.ReturnedInvoice, a.CustomerId, b.CompanyName, a.InvoiceTotal, a.Discount, a.Tax FROM Invoice a, Customer b, Invoice c WHERE a.ReturnedInvoice!=0 and a.ReturnedInvoice=c.InvoiceNumber AND ";
        if (!user.getRole().trim().equalsIgnoreCase("High")
            && !user.getRole().trim().equalsIgnoreCase("Medium")
            && !user.getRole().trim().equalsIgnoreCase("Acct")
            && !user.getUsername().trim().equalsIgnoreCase("Eddie")) {
          retSql +=
              " c.SalesPerson='" + user.getUsername() + "' AND a.SalesPerson!='"
                  + user.getUsername() + "' AND ";
        } else if (salesPerson != null && !salesPerson.trim().equals("")) {
          retSql +=
              " c.SalesPerson='" + salesPerson.trim() + "' AND a.SalesPerson!='"
                  + user.getUsername() + "' AND ";
        }
        if (invoiceFromDate.trim().equals(invoiceToDate)) {
          retSql +=
              " a.OrderDate='" + DateUtils.convertUSToMySQLFormat(invoiceFromDate.trim()) + "'";
        } else {
          retSql +=
              " a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(invoiceFromDate.trim()) + "'";
          retSql +=
              " AND a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(invoiceToDate.trim()) + "'";
        }
        retSql += " AND a.CustomerId=b.CustomerId Order By 1 ";

        ResultSet rs1 = stmt.executeQuery(retSql);

        while (rs1.next()) {
          if (toShowInvoices == null) {
            toShowInvoices = new Hashtable();
          }

          String invoiceNo = rs1.getString(1);
          String retInvNo = rs1.getString(2);
          String custId = rs1.getString(3);
          String custName = rs1.getString(4);
          double invoiceTotal = rs1.getDouble(5);
          double discount = rs1.getDouble(6);
          double tax = rs1.getDouble(7);

          String invoiceTotalStr = invoiceTotal + "";
          String discountStr = discount + "";
          String taxStr = tax + "";
          if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
            invoiceTotalStr += "0";
          }
          if (discountStr.indexOf(".") == discountStr.length() - 2) {
            discountStr += "0";
          }
          if (taxStr.indexOf(".") == taxStr.length() - 2) {
            taxStr += "0";
          }

          totInvoiceTotal += invoiceTotal;
          totDiscount += discount;
          totTax += tax;

          Hashtable totData = new Hashtable();
          totData.put("Inv. No.", invoiceNo);
          totData.put("Sales Person", retInvNo);
          totData.put("Cust Id", custId);
          totData.put("Cust Name", custName);
          totData.put("Inv. Total", invoiceTotalStr);
          totData.put("Discount", discountStr);
          totData.put("Tax", taxStr);

          data.addElement(totData);

        }

      }

      if (toShowInvoices == null) {
        throw new UserException(" No Sales For This Period ");
      }

      String totInvoiceTotalStr = totInvoiceTotal + "";
      String totDiscountStr = totDiscount + "";
      String totTaxStr = totTax + "";
      String netTotal = totInvoiceTotal - totDiscount + totTax + "";
      if (totInvoiceTotalStr.indexOf(".") == totInvoiceTotalStr.length() - 2) {
        totInvoiceTotalStr += "0";
      }
      if (totDiscountStr.indexOf(".") == totDiscountStr.length() - 2) {
        totDiscountStr += "0";
      }
      if (totTaxStr.indexOf(".") == totTaxStr.length() - 2) {
        totTaxStr += "0";
      }
      if (netTotal.indexOf(".") == netTotal.length() - 2) {
        netTotal += "0";
      }
      totInvoiceTotalStr = NumberUtils.cutFractions(totInvoiceTotalStr);
      totDiscountStr = NumberUtils.cutFractions(totDiscountStr);
      totTaxStr = NumberUtils.cutFractions(totTaxStr);
      netTotal = NumberUtils.cutFractions(netTotal);

      if (user.getRole().trim().equalsIgnoreCase("Acct")) {
        totInvoiceTotalStr = "0.0";
        totDiscountStr = "0.0";
        totTaxStr = "0.0";
        netTotal = "0.0";
      }
      totals[0][0] = "Gross Total";
      totals[0][1] = totInvoiceTotalStr;
      totals[1][0] = "Discount";
      totals[1][1] = totDiscountStr;
      totals[2][0] = "Tax";
      totals[2][1] = totTaxStr;
      totals[3][0] = "Net Total";
      totals[3][1] = netTotal;

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable analyseInvoices(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowInvoices = null;
    try {

      if (!user.getRole().trim().equalsIgnoreCase("High")) {
        throw new UserException("YOU ARE NOT AUTHORIZED TO VIEW THIS REPORT");
      }

      String fileName = "";
      if (fromDate.trim().equals(toDate.trim())) {
        fileName = "AI" + fromDate.trim() + ".html";
      } else {
        fileName = "AI" + fromDate.trim() + toDate.trim() + ".html";
      }

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[23][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql = "SELECT InvoiceNumber, InvoiceTotal, Discount FROM Invoice WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " OrderDate = '" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "' ";
      } else {
        sql +=
            " OrderDate >= '" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND OrderDate <= '" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "' ";
      }
      sql += " Order By 1 ";
      // logger.error(sql);

      ResultSet rs = stmt.executeQuery(sql);

      double totInvoiceTotal = 0.0;
      double totDiscount = 0.0;
      double totOverseasPrice = 0;
      double totOverseasSoldPrice = 0;
      double totLocalPrice = 0;
      double totLocalSoldPrice = 0;
      double totTotalPrice = 0;
      double totTotalSoldPrice = 0;
      double totMargin = 0;
      double totPercent = 0;
      int totLocalItems = 0;
      int totOverseasItems = 0;
      int totNoPriceItems = 0;
      double totNoPriceItemsSoldPrice = 0;

      if (fromDate.trim().equals(toDate.trim())) {
        mainHeading = "Analyzing Invoices For The Date " + toDate.trim();
      } else {
        mainHeading =
            "Analyzing Invoices For The Dates From " + fromDate.trim() + " To " + toDate.trim();
      }
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Total");
      subHeadings.addElement("Discount");
      subHeadings.addElement("Overseas Price");
      subHeadings.addElement("Overseas Sold Price");
      subHeadings.addElement("Local Price");
      subHeadings.addElement("Local Sold Price");
      subHeadings.addElement("Percent");
      subHeadings.addElement("Remarks");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        int invoiceNo = rs.getInt(1);
        double invoiceTotal = rs.getDouble(2);
        double discount = rs.getDouble(3);
        double overseasPrice = 0;
        double overseasSoldPrice = 0;
        double localPrice = 0;
        double localSoldPrice = 0;
        double percent = 0;

        String invoiceTotalStr = invoiceTotal + "";
        String discountStr = discount + "";
        if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
          invoiceTotalStr += "0";
        }
        if (discountStr.indexOf(".") == discountStr.length() - 2) {
          discountStr += "0";
        }
        String overseasPriceStr = "";
        String overseasSoldPriceStr = "";
        String localPriceStr = "";
        String localSoldPriceStr = "";
        String percentStr = "";
        String remarks = "";

        InvoiceBean invoice = InvoiceBean.getInvoice(invoiceNo, con);
        Vector<InvoiceDetailsBean> invoiceDetails = invoice.getInvoiceDetails();
        if (invoiceDetails.size() != 0) {
          Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
          while (ennum.hasMoreElements()) {
            InvoiceDetailsBean invoiceDetailsBean = ennum.nextElement();
            String partNo = invoiceDetailsBean.getPartNumber();
            int qty = invoiceDetailsBean.getQuantity();
            int remainItems = 0;
            double price = invoiceDetailsBean.getSoldPrice();
            int supplierId = 0;
            String vendorInvNo = "";
            LocalOrderBean orderBean =
                LocalOrderBean.getLocalOrder(supplierId, invoiceNo, partNo, vendorInvNo, con);
            if (invoiceDetailsBean.getActualPrice() != 0) {
              if (orderBean != null) {
                totLocalItems += qty;
                localPrice += (invoiceDetailsBean.getActualPrice() * qty);
                localSoldPrice += (price * qty);
              } else {
                totOverseasItems += qty;
                overseasPrice += (invoiceDetailsBean.getActualPrice() * qty);
                overseasSoldPrice += (price * qty);
              }
            } else {
              if (orderBean != null) {
                // Got this Part From Local Vendor
                if (qty <= orderBean.getQuantity()) {
                  remainItems = orderBean.getQuantity() - qty;
                  totLocalItems += qty;
                  localPrice += (orderBean.getPrice() * qty);
                  localSoldPrice += (price * qty);
                } else {
                  remainItems = qty - orderBean.getQuantity();
                }
              } else {
                remainItems = qty;
              }
              if (remainItems != 0) {
                // Got This Part From Overseas
                PartsBean part = PartsBean.getPart(partNo, con);
                if (part != null) {
                  if (part.getActualPrice() != 0) {
                    totOverseasItems += remainItems;
                    overseasPrice += (part.getActualPrice() * remainItems);
                    overseasSoldPrice += (price * remainItems);
                  } else if (!part.getInterchangeNo().trim().equals("")
                      && PartsBean.getPart(part.getInterchangeNo(), con).getActualPrice() != 0) {
                    totOverseasItems += remainItems;
                    overseasPrice +=
                        (PartsBean.getPart(part.getInterchangeNo(), con).getActualPrice() * remainItems);
                    overseasSoldPrice += (price * remainItems);
                  } else {
                    Statement stmt1 = con.createStatement();
                    ResultSet rs1 =
                        stmt1
                            .executeQuery("Select PartNo, ActualPrice From Parts Where InterchangeNo='"
                                + part.getPartNo() + "'");
                    double actPrice = 0;
                    while (rs1.next()) {
                      actPrice = rs1.getDouble("ActualPrice");
                      if (actPrice != 0) {
                        break;
                      }
                    }
                    if (actPrice != 0) {
                      totOverseasItems += remainItems;
                      overseasPrice += (actPrice * remainItems);
                      overseasSoldPrice += (price * remainItems);
                    } else {
                      totNoPriceItemsSoldPrice += (price * remainItems);
                      totNoPriceItems += remainItems;
                      remarks += "No Price:" + partNo + "\n";
                    }
                  }

                  // logger.error(overseasSoldPrice);
                  part = null;
                } else {
                  remarks += "No Part:" + partNo + "\n";
                }
              }
            }
            invoiceDetailsBean = null;
            orderBean = null;
          }
          invoice = null;
        } else {
          overseasPriceStr = "";
          overseasSoldPriceStr = "";
          localPriceStr = "";
          localSoldPriceStr = "";
          percentStr = "";
          remarks = "No Items";
        }

        double totPrice = overseasPrice + localPrice;
        double totSoldPrice = overseasSoldPrice + localSoldPrice;
        if (totPrice != 0) {
          totPrice = Double.parseDouble(NumberUtils.cutFractions(totPrice + ""));
        }
        if (totSoldPrice != 0) {
          totSoldPrice = Double.parseDouble(NumberUtils.cutFractions(totSoldPrice + ""));
        }
        // if (totSoldPrice != invoiceTotal) {
        // remarks += "Not Matching\n";
        // }
        if (totSoldPrice != 0) {
          percent = (totSoldPrice - totPrice - discount) * 100 / totSoldPrice;
        } else {
          percent = 0;
        }

        if (remarks.trim().equals("")) {
          remarks = "&nbsp;";
        }

        percent = Math.rint(percent);

        totInvoiceTotal += invoiceTotal;
        totDiscount += discount;

        if (overseasPrice != 0) {
          totOverseasPrice += overseasPrice;
          overseasPriceStr = overseasPrice + "";
        } else {
          overseasPriceStr = "&nbsp;";
        }
        if (overseasSoldPrice != 0) {
          totOverseasSoldPrice += overseasSoldPrice;
          overseasSoldPriceStr = overseasSoldPrice + "";
        } else {
          overseasSoldPriceStr = "&nbsp;";
        }
        if (localPrice != 0) {
          totLocalPrice += localPrice;
          localPriceStr = localPrice + "";
        } else {
          localPriceStr = "&nbsp;";
        }
        if (localSoldPrice != 0) {
          totLocalSoldPrice += localSoldPrice;
          localSoldPriceStr = localSoldPrice + "";
        } else {
          localSoldPriceStr = "&nbsp;";
        }
        if (percent != 0) {
          percentStr = percent + "";
        } else {
          percentStr = "&nbsp;";
        }

        if (overseasPriceStr.indexOf(".") == overseasPriceStr.length() - 2) {
          overseasPriceStr += "0";
        }
        if (overseasSoldPriceStr.indexOf(".") == overseasSoldPriceStr.length() - 2) {
          overseasSoldPriceStr += "0";
        }
        if (localPriceStr.indexOf(".") == localPriceStr.length() - 2) {
          localPriceStr += "0";
        }
        if (localSoldPriceStr.indexOf(".") == localSoldPriceStr.length() - 2) {
          localSoldPriceStr += "0";
        }

        overseasPriceStr = NumberUtils.cutFractions(overseasPriceStr);
        overseasSoldPriceStr = NumberUtils.cutFractions(overseasSoldPriceStr);
        localPriceStr = NumberUtils.cutFractions(localPriceStr);
        localSoldPriceStr = NumberUtils.cutFractions(localSoldPriceStr);

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo + "");
        totData.put("Inv. Total", invoiceTotalStr);
        totData.put("Discount", discountStr);
        totData.put("Overseas Price", overseasPriceStr);
        totData.put("Overseas Sold Price", overseasSoldPriceStr);
        totData.put("Local Price", localPriceStr);
        totData.put("Local Sold Price", localSoldPriceStr);
        totData.put("Percent", percentStr);
        totData.put("Remarks", remarks);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No Sales For This Period ");
      }

      // String totInvoiceTotalStr = totInvoiceTotal+"";
      String totDiscountStr = totDiscount + "";
      String netTotal = totInvoiceTotal - totDiscount + "";
      // logger.error("TotOverseasPrice10: " + totOverseasPrice);
      String totOverseasPriceStr = totOverseasPrice + "";
      // logger.error("TotOverseasPriceStr10: " +
      // totOverseasPriceStr);
      String totOverseasSoldPriceStr = totOverseasSoldPrice + "";
      String totLocalPriceStr = totLocalPrice + "";
      String totLocalSoldPriceStr = totLocalSoldPrice + "";
      String totPriceStr = "";
      String totSoldPriceStr = "";
      String overseasMarginStr = "";
      String overseasPercentStr = "";
      String localMarginStr = "";
      String localPercentStr = "";
      String totMarginStr = "";
      String totPercentStr = "";
      String totNoPriceItemsSoldPriceStr = "";

      double overseasMargin = 0;
      double overseasPercent = 0;
      double localMargin = 0;
      double localPercent = 0;

      totTotalPrice = totOverseasPrice + totLocalPrice;
      totTotalSoldPrice = totOverseasSoldPrice + totLocalSoldPrice + totNoPriceItemsSoldPrice;

      overseasMargin = totOverseasSoldPrice - totOverseasPrice;
      localMargin = totLocalSoldPrice - totLocalPrice;
      if (totOverseasSoldPrice != 0) {
        overseasPercent = overseasMargin * 100 / totOverseasSoldPrice;
      } else {
        overseasPercent = 0;
      }
      if (totLocalSoldPrice != 0) {
        localPercent = localMargin * 100 / totLocalSoldPrice;
      } else {
        localPercent = 0;
      }
      overseasPercent = Math.rint(overseasPercent);
      localPercent = Math.rint(localPercent);
      totMargin = totTotalSoldPrice - totDiscount - totTotalPrice;
      if (totOverseasSoldPrice + totLocalSoldPrice != 0) {
        totPercent = (totMargin * 100) / (totOverseasSoldPrice + totLocalSoldPrice);
      } else {
        totPercent = 0;
      }
      totPercent = Math.rint(totPercent);

      totPriceStr = totTotalPrice + "";
      totSoldPriceStr = totTotalSoldPrice + "";
      totNoPriceItemsSoldPriceStr = totNoPriceItemsSoldPrice + "";
      overseasMarginStr = overseasMargin + "";
      localMarginStr = localMargin + "";
      if (overseasPercent != 0) {
        overseasPercentStr = overseasPercent + "";
      } else {
        overseasPercentStr = "&nbsp;";
      }
      if (localPercent != 0) {
        localPercentStr = localPercent + "";
      } else {
        localPercentStr = "&nbsp;";
      }
      totMarginStr = totMargin + "";
      if (totPercent != 0) {
        totPercentStr = totPercent + "";
      } else {
        totPercentStr = "&nbsp;";
      }

      // if (totInvoiceTotalStr.indexOf(".") ==
      // totInvoiceTotalStr.length()-2) {
      // totInvoiceTotalStr += "0";
      // }
      if (totDiscountStr.indexOf(".") == totDiscountStr.length() - 2) {
        totDiscountStr += "0";
      }
      if (netTotal.indexOf(".") == netTotal.length() - 2) {
        netTotal += "0";
      }
      if (totLocalPriceStr.indexOf(".") == totLocalPriceStr.length() - 2) {
        totLocalPriceStr += "0";
      }
      if (totLocalSoldPriceStr.indexOf(".") == totLocalSoldPriceStr.length() - 2) {
        totLocalSoldPriceStr += "0";
      }
      if (localMarginStr.indexOf(".") == localMarginStr.length() - 2) {
        localMarginStr += "0";
      }
      if (totOverseasPriceStr.indexOf(".") == totOverseasPriceStr.length() - 2) {
        totOverseasPriceStr += "0";
      }
      if (totOverseasSoldPriceStr.indexOf(".") == totOverseasSoldPriceStr.length() - 2) {
        totOverseasSoldPriceStr += "0";
      }
      if (overseasMarginStr.indexOf(".") == overseasMarginStr.length() - 2) {
        overseasMarginStr += "0";
      }
      if (totPriceStr.indexOf(".") == totPriceStr.length() - 2) {
        totPriceStr += "0";
      }
      if (totSoldPriceStr.indexOf(".") == totSoldPriceStr.length() - 2) {
        totSoldPriceStr += "0";
      }
      if (totMarginStr.indexOf(".") == totMarginStr.length() - 2) {
        totMarginStr += "0";
      }
      if (totNoPriceItemsSoldPriceStr.indexOf(".") == totNoPriceItemsSoldPriceStr.length() - 2) {
        totNoPriceItemsSoldPriceStr += "0";
      }

      totLocalPriceStr = NumberUtils.cutFractions(totLocalPriceStr);
      totLocalSoldPriceStr = NumberUtils.cutFractions(totLocalSoldPriceStr);
      localMarginStr = NumberUtils.cutFractions(localMarginStr);
      totOverseasPriceStr = NumberUtils.cutFractions(totOverseasPriceStr);
      totOverseasSoldPriceStr = NumberUtils.cutFractions(totOverseasSoldPriceStr);
      overseasMarginStr = NumberUtils.cutFractions(overseasMarginStr);
      totPriceStr = NumberUtils.cutFractions(totPriceStr);
      totSoldPriceStr = NumberUtils.cutFractions(totSoldPriceStr);
      totMarginStr = NumberUtils.cutFractions(totMarginStr);
      totNoPriceItemsSoldPriceStr = NumberUtils.cutFractions(totNoPriceItemsSoldPriceStr);

      // totInvoiceTotalStr =
      // NumberUtils.cutFractions(totInvoiceTotalStr);
      totDiscountStr = NumberUtils.cutFractions(totDiscountStr);
      netTotal = NumberUtils.cutFractions(netTotal);

      totals[0][0] = "Total No. Of Items Bought Locally";
      totals[0][1] = totLocalItems + "";
      totals[1][0] = "Local Price";
      totals[1][1] = totLocalPriceStr;
      totals[2][0] = "Our Sold Price";
      totals[2][1] = totLocalSoldPriceStr;
      totals[3][0] = "Margin On Local Items";
      totals[3][1] = localMarginStr;
      totals[4][0] = "Percentage of Margin";
      totals[4][1] = localPercentStr + " %";

      totals[5][0] = "&nbsp;";
      totals[5][1] = "&nbsp;";
      totals[6][0] = "&nbsp;";
      totals[6][1] = "&nbsp;";

      totals[7][0] = "Total No. Of Items Bought From Overseas";
      totals[7][1] = totOverseasItems + "";
      totals[8][0] = "Overseas Price";
      totals[8][1] = totOverseasPriceStr;
      totals[9][0] = "Our Sold Price";
      totals[9][1] = totOverseasSoldPriceStr;
      totals[10][0] = "Margin On Overseas Items";
      totals[10][1] = overseasMarginStr;
      totals[11][0] = "Percentage of Margin";
      totals[11][1] = overseasPercentStr + " %";

      totals[12][0] = "&nbsp;";
      totals[12][1] = "&nbsp;";
      totals[13][0] = "&nbsp;";
      totals[13][1] = "&nbsp;";
      totals[14][0] = "Items With No Price";
      totals[14][1] = totNoPriceItems + "";
      totals[15][0] = "No Price Items Sold Price";
      totals[15][1] = totNoPriceItemsSoldPriceStr + "";
      totals[16][0] = "&nbsp;";
      totals[16][1] = "&nbsp;";

      totals[17][0] = "Total No. Of Items";
      totals[17][1] = totLocalItems + totOverseasItems + totNoPriceItems + "";
      totals[18][0] = "Total Price";
      totals[18][1] = totPriceStr;
      totals[19][0] = "Total Sold Price";
      totals[19][1] = totSoldPriceStr;
      totals[20][0] = "Total Discount Given";
      totals[20][1] = totDiscountStr;
      totals[21][0] = "Margin On All Items";
      totals[21][1] = totMarginStr;
      totals[22][0] = "Percentage of Margin";
      totals[22][1] = totPercentStr + " %";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);

      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable inventoryOnHand(UserBean user, boolean bySubCat) throws UserException {
    Hashtable toShowInven = null;
    try {

      String fileName = "ShowInven" + DateUtils.getNewUSDate() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[4][2];

      mainHeading = "Inventory On Hand On " + DateUtils.getNewUSDate();
      subHeadings.addElement("CATEGORY");
      subHeadings.addElement("No. Of Parts");
      subHeadings.addElement("No. Of Items");
      subHeadings.addElement("COST");

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql1 =
          "SELECT c.CategoryName, COUNT(*), SUM(UnitsInStock), sum(UnitsInStock*ActualPrice) FROM Parts a, SubCategory b, Category c WHERE ";
      sql1 +=
          " UnitsInStock > 0 and a.SubCategory=b.SubCategoryCode and b.CategoryCode=c.CategoryCode Group By 1 Order By 1 ";
      String sql2 =
          "SELECT b.SubCategoryName, COUNT(*), SUM(UnitsInStock), sum(UnitsInStock*ActualPrice) FROM Parts a, SubCategory b WHERE ";
      sql2 += " UnitsInStock > 0 and a.SubCategory=b.SubCategoryCode Group By 1 Order By 1 ";
      // logger.error(sql);

      ResultSet rs = null;
      if (bySubCat) {
        rs = stmt.executeQuery(sql2);
      } else {
        rs = stmt.executeQuery(sql1);
      }

      int cntCat = 0;
      int totItems = 0;
      int totUnits = 0;
      double totCost = 0.0;

      while (rs.next()) {
        if (toShowInven == null) {
          toShowInven = new Hashtable();
        }

        String cat = rs.getString(1);
        int noItems = rs.getInt(2);
        int noUnits = rs.getInt(3);
        double cost = rs.getDouble(4);

        cntCat++;

        totItems += noItems;
        totUnits += noUnits;
        totCost += cost;

        String totCostStr = cost + "";
        if (totCostStr.indexOf(".") == totCostStr.length() - 2) {
          totCostStr += "0";
        }
        totCostStr = NumberUtils.cutFractions(totCostStr);

        Hashtable totData = new Hashtable();
        totData.put("CATEGORY", cat);
        totData.put("No. Of Parts", noItems + "");
        totData.put("No. Of Items", noUnits + "");
        totData.put("COST", totCostStr);

        data.addElement(totData);
      }

      String totActualStr = totCost + "";
      if (totActualStr.indexOf(".") == totActualStr.length() - 2) {
        totActualStr += "0";
      }
      totActualStr = NumberUtils.cutFractions(totActualStr);

      totals[0][0] = "No Of Categories";
      totals[0][1] = cntCat + "";
      totals[1][0] = "Total No. Of Parts";
      totals[1][1] = totItems + "";
      totals[2][0] = "total No. Of Units";
      totals[2][1] = totUnits + "";
      totals[3][0] = "Total Cost Of Inventory";
      totals[3][1] = totActualStr;

      toShowInven.put("FileName", fileName);
      toShowInven.put("BackScreen", "TodaysOrders");
      toShowInven.put("MainHeading", mainHeading);
      toShowInven.put("SubHeadings", subHeadings);
      toShowInven.put("Data", data);
      toShowInven.put("Totals", totals);

      createReport(toShowInven);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInven;
  }

  public static Hashtable costOfGoodsInvoices(UserBean user, String fromDate, String toDate)
     throws UserException {
   Hashtable toShowInvoices = null;
   try {

     if (!user.getRole().trim().equalsIgnoreCase("High")) {
       throw new UserException("YOU ARE NOT AUTHORIZED TO VIEW THIS REPORT");
     }

     String fileName = "";
     if (fromDate.trim().equals(toDate.trim())) {
       fileName = "COGS" + fromDate.trim() + ".html";
     } else {
       fileName = "COGS" + fromDate.trim() + toDate.trim() + ".html";
     }
     //
     String targetdDB = "CH";
     List<InvoiceDetailsOurPrice> missingourpricelist = UpdateOurPrice.getMissingOurPrice(targetdDB);
     System.err.println("****"+missingourpricelist.size());
     if(missingourpricelist!=null && missingourpricelist.size() > 0)
     UpdateOurPrice.updateOurPrices(missingourpricelist, targetdDB);
     //
     String mainHeading = "";
     Vector subHeadings = new Vector();
     Vector<Hashtable> data = new Vector<Hashtable>();
     String[][] totals = new String[8][2];

     Connection con = DBInterfaceLocal.getSQLConnection();
     Statement stmt = con.createStatement();
     String sql = "SELECT InvoiceNumber, InvoiceTotal, Discount FROM Invoice WHERE ";
     if (fromDate.trim().equals(toDate.trim())) {
       sql += " OrderDate = '" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "' ";
     } else {
       sql +=
           " OrderDate >= '" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
               + "' AND OrderDate <= '" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "' ";
     }
     sql += " Order By 1 ";

     logger.error("   Before calling query...");

     ResultSet rs = stmt.executeQuery(sql);

     logger.error("   After calling query..." + rs.getFetchSize());

     //double totInvoiceTotal = 0.0;
     double totDiscount = 0.0;
     double totInvoicePrice = 0;
     double totInvoiceSoldPrice = 0;      
     double totTotalSoldPrice = 0;
     double totMargin = 0;
     double totPercent = 0;
     int totItems = 0;
             double invoicePrice = 0;
       double invoiceSoldPrice = 0;
       double margin = 0;

     if (fromDate.trim().equals(toDate.trim())) {
       mainHeading = "Cost Of Goods Report For The Date " + toDate.trim();
     } else {
       mainHeading =
           "Cost Of Goods Report For The Dates From " + fromDate.trim() + " To " + toDate.trim();
     }
     subHeadings.addElement("Inv. No.");
     subHeadings.addElement("Inv. Total");
     subHeadings.addElement("Discount");
     subHeadings.addElement("Our Cost");
     subHeadings.addElement("Sold Price");
     subHeadings.addElement("Margin");
     subHeadings.addElement("Remarks");

     logger.error("   Before processing of invoice...");

     while (rs.next()) {
       if (toShowInvoices == null) {
         toShowInvoices = new Hashtable();
       }

       int invoiceNo = rs.getInt(1);
       double invoiceTotal = rs.getDouble(2);
       double discount = rs.getDouble(3);

       String invoiceTotalStr = invoiceTotal + "";
       
       String discountStr = discount + "";
       
       if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
         invoiceTotalStr += "0";
       }
       if (discountStr.indexOf(".") == discountStr.length() - 2) {
         discountStr += "0";
       }
       StringBuilder invoicePriceStr = new StringBuilder();
       StringBuilder invoiceSoldPriceStr = new StringBuilder();
       StringBuilder marginStr = new StringBuilder();
       StringBuilder remarks = new StringBuilder();

       // logger.error("   Before calling invoce bean ...  ");
       InvoiceBean invoice =new InvoiceBean();
       invoice.setInvoiceNumber(rs.getInt("InvoiceNumber"));
       invoice.setDiscount(rs.getDouble("Discount"));
       invoice.setInvoiceTotal(rs.getDouble("InvoiceTotal"));
       // logger.error("   After calling invoce bean ...  ");
       InvoiceDetailsBean invoiceDet=InvoiceDetailsBean.getInvoiceTotalDetails(con,invoiceNo);
       totItems += invoiceDet.getTotalItems();        

       invoicePrice = invoiceDet.getTotalOurPrice();       
       invoiceSoldPrice = invoiceDet.getTotalSoldPrice();
       
       if (invoicePrice != 0) {
         invoicePrice = Double.parseDouble(NumberUtils.cutFractions(invoicePrice + ""));
       }
       if (invoiceSoldPrice != 0) {
         invoiceSoldPrice = Double.parseDouble(NumberUtils.cutFractions(invoiceSoldPrice + ""));
       }
       if (invoiceSoldPrice != 0) {
         margin = (invoiceSoldPrice - invoicePrice - discount);
       } else {
         margin = 0;
       }

       if (remarks.toString().equals("")) {
         remarks.append( "&nbsp;");
       }
       
        //if needed uncomment it
       //totInvoiceTotal += invoiceTotal;
       totDiscount += discount;

       if (invoicePrice != 0) {
         totInvoicePrice += invoicePrice;
         invoicePriceStr.append( invoicePrice );
       } else {
         invoicePriceStr.append("&nbsp;");
       }
       if (invoiceSoldPrice != 0) {
         totInvoiceSoldPrice += invoiceSoldPrice;
         invoiceSoldPriceStr.append( invoiceSoldPrice );
       } else {
         invoiceSoldPriceStr.append( "&nbsp;");
       }
       if (margin != 0) {
         marginStr.append( margin );
       } else {
         marginStr.append( "&nbsp;");
       }

       if (invoicePriceStr.indexOf(".") == invoicePriceStr.length() - 2) {
         invoicePriceStr.append( "0");
       }
       if (invoiceSoldPriceStr.indexOf(".") == invoiceSoldPriceStr.length() - 2) {
         invoiceSoldPriceStr.append( "0");
       }
       if (marginStr.indexOf(".") == marginStr.length() - 2) {
         marginStr.append( "0");
       }

       invoicePriceStr = new StringBuilder( NumberUtils.cutFractions(invoicePriceStr.toString()));
       invoiceSoldPriceStr = new StringBuilder( NumberUtils.cutFractions(invoiceSoldPriceStr.toString() ));
       marginStr = new StringBuilder(NumberUtils.cutFractions(marginStr.toString()));

       Hashtable totData = new Hashtable();
       totData.put("Inv. No.", invoiceNo + "");
       totData.put("Inv. Total", invoiceTotalStr);
       totData.put("Discount", discountStr);
       totData.put("Our Cost", invoicePriceStr.toString());
       totData.put("Sold Price", invoiceSoldPriceStr.toString());
       totData.put("Margin", marginStr.toString());
       totData.put("Remarks", remarks.toString());

       data.addElement(totData);

     }

     logger.error("   After processing of invoice...");

     if (toShowInvoices == null) {
       throw new UserException(" No Sales For This Period ");
     }

     String totDiscountStr = totDiscount + "";     
     String totPriceStr ;
     String totSoldPriceStr ;
     String totMarginStr ;
     String totPercentStr ;

     //totTotalPrice = totInvoicePrice;
     totTotalSoldPrice = totInvoiceSoldPrice;

     totMargin = totTotalSoldPrice - totDiscount - totInvoicePrice;
     if (totTotalSoldPrice != 0) {
       totPercent = (totMargin * 100) / (totTotalSoldPrice);
     } else {
       totPercent = 0;
     }
     totPercent = Math.rint(totPercent);

     totPriceStr = totInvoicePrice + "";
     totSoldPriceStr = totTotalSoldPrice + "";
     totMarginStr = totMargin + "";
     if (totPercent != 0) {
       totPercentStr = totPercent + "";
     } else {
       totPercentStr = "&nbsp;";
     }

     if (totDiscountStr.indexOf(".") == totDiscountStr.length() - 2) {
       totDiscountStr += "0";
     }
     if (totPriceStr.indexOf(".") == totPriceStr.length() - 2) {
       totPriceStr += "0";
     }
     if (totSoldPriceStr.indexOf(".") == totSoldPriceStr.length() - 2) {
       totSoldPriceStr += "0";
     }
     if (totMarginStr.indexOf(".") == totMarginStr.length() - 2) {
       totMarginStr += "0";
     }

     totPriceStr = NumberUtils.cutFractions(totPriceStr);
     totSoldPriceStr = NumberUtils.cutFractions(totSoldPriceStr);
     totMarginStr = NumberUtils.cutFractions(totMarginStr);

     totDiscountStr = NumberUtils.cutFractions(totDiscountStr);

     totals[0][0] = "&nbsp;";
     totals[0][1] = "&nbsp;";
     totals[1][0] = "&nbsp;";
     totals[1][1] = "&nbsp;";

     totals[2][0] = "Total No. Of Items";
     totals[2][1] = totItems + "";
     totals[3][0] = "Total Price";
     totals[3][1] = totPriceStr;
     totals[4][0] = "Total Sold Price";
     totals[4][1] = totSoldPriceStr;
     totals[5][0] = "Total Discount Given";
     totals[5][1] = totDiscountStr;
     totals[6][0] = "Margin On All Items";
     totals[6][1] = totMarginStr;
     totals[7][0] = "Percentage of Margin";
     totals[7][1] = totPercentStr + " %";

     toShowInvoices.put("FileName", fileName);
     toShowInvoices.put("BackScreen", "TodaysOrders");
     toShowInvoices.put("MainHeading", mainHeading);
     toShowInvoices.put("SubHeadings", subHeadings);
     toShowInvoices.put("Data", data);
     toShowInvoices.put("Totals", totals);

     createReport(toShowInvoices);
     rs.close();
     stmt.close();

     con.close();
   } catch (Exception e) {
     logger.error(e);
     throw new UserException(e.getMessage());
   }
   return toShowInvoices;
 }
 
/*
  public static int updateTotals(String sqlwhere){
	  int resultcount=0;
	  try{
		  Connection con = DBInterfaceLocal.getSQLConnection();
		    Statement stmt = con.createStatement();
		    resultcount= stmt.executeUpdate("update invoice v set " +
		    		"sumSoldPrice=(select sum(SoldPrice*Quantity)from invoicedetails where InvoiceNumber=v.InvoiceNumber ) " +
		    		" , sumQty=(select sum(Quantity) from invoicedetails where InvoiceNumber=v.InvoiceNumber ) ," +
		    		" sumActualPrice=(select sum(ActualPrice*Quantity) from invoicedetails where InvoiceNumber=v.InvoiceNumber )" +
		    		" "+sqlwhere);
	  }catch(SQLException ex){
		  ex.printStackTrace();
	  }
	  return resultcount;
  }

  public static Hashtable costOfGoodsInvoices(UserBean user, String fromDate, String toDate)
    throws UserException {
  Hashtable toShowInvoices = null;
  try {

    if (!user.getRole().trim().equalsIgnoreCase("High")) {
      throw new UserException("YOU ARE NOT AUTHORIZED TO VIEW THIS REPORT");
    }

    String fileName = "";
    if (fromDate.trim().equals(toDate.trim())) {
      fileName = "COGS" + fromDate.trim() + ".html";
    } else {
      fileName = "COGS" + fromDate.trim() + toDate.trim() + ".html";
    }

    String mainHeading = "";
    Vector subHeadings = new Vector();
    Vector<Hashtable> data = new Vector<Hashtable>();
    String[][] totals = new String[8][2];

    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = con.createStatement();
    String sql = "SELECT InvoiceNumber, InvoiceTotal, Discount,sumActualPrice,sumSoldPrice,sumQty FROM Invoice  ";
    String sqlWhere="";
    
    if (fromDate.trim().equals(toDate.trim())) {
      sqlWhere += " where OrderDate = '" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "' ";
    } else {
    	sqlWhere +=
          " where OrderDate >= '" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
              + "' AND OrderDate <= '" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "' ";
    }
    sql = sql+sqlWhere+ " Order By 1 ";
    updateTotals(sqlWhere);
    logger.error("   Before calling query...");

    ResultSet rs = stmt.executeQuery(sql);

    logger.error("   After calling query..." + rs.getFetchSize());

    //double totInvoiceTotal = 0.0;
    double totDiscount = 0.0;
    double totInvoicePrice = 0;
    double totInvoiceSoldPrice = 0;
    double totTotalPrice = 0;
    double totTotalSoldPrice = 0;
    double totMargin = 0;
    double totPercent = 0;
    int totItems = 0;

    if (fromDate.trim().equals(toDate.trim())) {
      mainHeading = "Cost Of Goods Report For The Date " + toDate.trim();
    } else {
      mainHeading =
          "Cost Of Goods Report For The Dates From " + fromDate.trim() + " To " + toDate.trim();
    }
    subHeadings.addElement("Inv. No.");
    subHeadings.addElement("Inv. Total");
    subHeadings.addElement("Discount");
    subHeadings.addElement("Our Cost");
    subHeadings.addElement("Sold Price");
    subHeadings.addElement("Margin");
    subHeadings.addElement("Remarks");

    logger.error("   Before processing of invoice...");

    while (rs.next()) {
      if (toShowInvoices == null) {
        toShowInvoices = new Hashtable();
      }

      int invoiceNo = rs.getInt(1);
      double invoiceTotal = rs.getDouble(2);
      double discount = rs.getDouble(3);       
      double totSoldPrice =rs.getDouble("sumSoldPrice");
      double totPrice = rs.getDouble("sumActualPrice");
      double sumQty = rs.getDouble("sumQty");
      totItems+=sumQty;
      double margin = 0;

      StringBuilder invoiceTotalStr = new StringBuilder();
             invoiceTotalStr.append(invoiceTotal + "");
      
      String discountStr = discount + "";
      
      if (invoiceTotalStr.indexOf(".") == invoiceTotalStr.length() - 2) {
        invoiceTotalStr.append("0");
      }
      if (discountStr.indexOf(".") == discountStr.length() - 2) {
        discountStr += "0";
      }
      StringBuilder invoicePriceStr = new StringBuilder();
      StringBuilder invoiceSoldPriceStr = new StringBuilder();
      StringBuilder marginStr = new StringBuilder();
      StringBuilder remarks = new StringBuilder();

      // logger.error("   Before calling invoce bean ...  ");       

     // double totPrice = invoicePrice;
      //double totSoldPrice = invoiceSoldPrice;
      if (totPrice != 0) {
        totPrice = Double.parseDouble(NumberUtils.cutFractions(totPrice + ""));
      }
      if (totSoldPrice != 0) {
        totSoldPrice = Double.parseDouble(NumberUtils.cutFractions(totSoldPrice + ""));
      }
      if (totSoldPrice != 0) {
        margin = totSoldPrice - totPrice - discount;
      } 

      if (remarks.toString().equals("")) {
        remarks = new StringBuilder("&nbsp;");
      }

      //totInvoiceTotal += invoiceTotal;
      totDiscount += discount;

      if (totPrice != 0) {
        totInvoicePrice += totPrice;
        invoicePriceStr.append( totPrice );
      } else {
        invoicePriceStr.append( "&nbsp;" );
      }
      if (totSoldPrice != 0) {
        totInvoiceSoldPrice += totSoldPrice;
        invoiceSoldPriceStr.append( totSoldPrice );
      } else {
        invoiceSoldPriceStr.append( "&nbsp;");
      }
      if (margin != 0) {
        marginStr.append( margin );
      } else {
        marginStr.append( "&nbsp;");
      }

      if (invoicePriceStr.indexOf(".") == invoicePriceStr.length() - 2) {
        invoicePriceStr.append( "0");
      }
      if (invoiceSoldPriceStr.indexOf(".") == invoiceSoldPriceStr.length() - 2) {
        invoiceSoldPriceStr.append("0");
      }
      if (marginStr.indexOf(".") == marginStr.length() - 2) {
        marginStr.append( "0" );
      }

     // invoicePriceStr = NumberUtils.cutFractions(invoicePriceStr);
    //  invoiceSoldPriceStr = NumberUtils.cutFractions(invoiceSoldPriceStr);
   //   marginStr = NumberUtils.cutFractions(marginStr);

      Hashtable totData = new Hashtable();
      totData.put("Inv. No.", invoiceNo + "");
      totData.put("Inv. Total", invoiceTotalStr.toString());
      totData.put("Discount", discountStr);
      totData.put("Our Cost", invoicePriceStr.toString());
      totData.put("Sold Price", invoiceSoldPriceStr.toString());
      totData.put("Margin", marginStr.toString());
      totData.put("Remarks", remarks.toString());

      data.addElement(totData);

    }

    logger.error("   After processing of invoice...");

    if (toShowInvoices == null) {
      throw new UserException(" No Sales For This Period ");
    }

    String totDiscountStr = totDiscount + "";
    String totPriceStr = "";
    String totSoldPriceStr = "";
    String totMarginStr = "";
    String totPercentStr = "";

    totTotalPrice = totInvoicePrice;
    totTotalSoldPrice = totInvoiceSoldPrice;

    totMargin = totTotalSoldPrice - totDiscount - totTotalPrice;
    if (totTotalSoldPrice != 0) {
      totPercent = (totMargin * 100) / (totTotalSoldPrice);
    } else {
      totPercent = 0;
    }
    totPercent = Math.rint(totPercent);

    totPriceStr = totTotalPrice + "";
    totSoldPriceStr = totTotalSoldPrice + "";
    totMarginStr = totMargin + "";
    if (totPercent != 0) {
      totPercentStr = totPercent + "";
    } else {
      totPercentStr = "&nbsp;";
    }

    if (totDiscountStr.indexOf(".") == totDiscountStr.length() - 2) {
      totDiscountStr += "0";
    }
    if (totPriceStr.indexOf(".") == totPriceStr.length() - 2) {
      totPriceStr += "0";
    }
    if (totSoldPriceStr.indexOf(".") == totSoldPriceStr.length() - 2) {
      totSoldPriceStr += "0";
    }
    if (totMarginStr.indexOf(".") == totMarginStr.length() - 2) {
      totMarginStr += "0";
    }

    totPriceStr = NumberUtils.cutFractions(totPriceStr);
    totSoldPriceStr = NumberUtils.cutFractions(totSoldPriceStr);
    totMarginStr = NumberUtils.cutFractions(totMarginStr);

    totDiscountStr = NumberUtils.cutFractions(totDiscountStr);

    totals[0][0] = "&nbsp;";
    totals[0][1] = "&nbsp;";
    totals[1][0] = "&nbsp;";
    totals[1][1] = "&nbsp;";

    totals[2][0] = "Total No. Of Items";
    totals[2][1] = totItems + "";
    totals[3][0] = "Total Price";
    totals[3][1] = totPriceStr;
    totals[4][0] = "Total Sold Price";
    totals[4][1] = totSoldPriceStr;
    totals[5][0] = "Total Discount Given";
    totals[5][1] = totDiscountStr;
    totals[6][0] = "Margin On All Items";
    totals[6][1] = totMarginStr;
    totals[7][0] = "Percentage of Margin";
    totals[7][1] = totPercentStr + " %";

    toShowInvoices.put("FileName", fileName);
    toShowInvoices.put("BackScreen", "TodaysOrders");
    toShowInvoices.put("MainHeading", mainHeading);
    toShowInvoices.put("SubHeadings", subHeadings);
    toShowInvoices.put("Data", data);
    toShowInvoices.put("Totals", totals);

    createReport(toShowInvoices);
    rs.close();
    stmt.close();

    con.close();
  } catch (Exception e) {
    logger.error(e);
    throw new UserException(e.getMessage());
  }
  return toShowInvoices;
}
*/
  public static Hashtable<String, String> detailedReceivable() throws UserException {
    long tt = System.currentTimeMillis();
    Hashtable toShowAR = null;
    try {
      // select customerid, sum(balance) from invoice group by customerid
      // order by 2 desc
      String fileName = "DR" + DateUtils.getNewUSDate().trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[12][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "select a.customerid, b.companyname, sum(a.balance), b.PaymentTerms from invoice a, customer b where a.balance!=0 and a.Status!='C' and a.Status!='W' and a.customerid=b.customerid group by a.customerid order by 3 desc";
      // logger.error(sql);

      ResultSet rs = stmt.executeQuery(sql);

      int totCustPending = 0;
      double totBalance = 0.0;
      double totalSplitBalance = 0.0;
      double totalCurrent = 0.0;
      double total30Days = 0.0;
      double total60Days = 0.0;
      double total90Days = 0.0;
      double totBCAmt = 0.0;

      mainHeading = "Ageing Accounts Receivable As On " + DateUtils.getNewUSDate();
      subHeadings.addElement("ID");
      subHeadings.addElement("Customer");
      subHeadings.addElement("Terms");
      subHeadings.addElement("Total");
      subHeadings.addElement("Current");
      subHeadings.addElement("30 Days");
      subHeadings.addElement("60 Days");
      subHeadings.addElement("90 Days");
      subHeadings.addElement("BC Chks");

      Statement stmtXX = con.createStatement();
      String sqlXX =
          "Select CustomerId, Sum(Balance) From BouncedChecks Where Balance!=0 Group By CustomerId Order By 1 ";
      Hashtable<String, String> bcChecks = null;
      ResultSet rsXX = stmtXX.executeQuery(sqlXX);
      while (rsXX.next()) {
        if (bcChecks == null) {
          bcChecks = new Hashtable<String, String>();
        }
        String cstId = rsXX.getString(1);
        String bcAmt = rsXX.getString(2);
        bcChecks.put(cstId, bcAmt);
      }

      while (rs.next()) {
        if (toShowAR == null) {
          toShowAR = new Hashtable();
        }

        String custId = rs.getString(1);
        // logger.error("Detail:" + custId);
        String companyName = rs.getString(2);
        double pendingAmount = rs.getDouble(3);
        String terms = rs.getString(4);
        String bcAmtStr = "";

        if (terms == null) {
          terms = "&nbsp;";
        } else if (terms.trim().equals("")) {
          terms = "&nbsp;";
        } else if (terms.trim().equalsIgnoreCase("C")) {
          terms = "Cod";
        } else if (terms.trim().equalsIgnoreCase("O")) {
          terms = "Cash";
        } else if (terms.trim().equalsIgnoreCase("W")) {
          terms = "Wkly";
        } else if (terms.trim().equalsIgnoreCase("B")) {
          terms = "Bi-Wk";
        } else if (terms.trim().equalsIgnoreCase("M")) {
          terms = "Mthly";
        }

        /*
         * Vector v = BouncedChecksBean.getAllBouncedChecks(custId); if (v != null) { Enumeration
         * ennumX = v.elements(); while (ennumX.hasMoreElements()) { BouncedChecksBean bcBean =
         * (BouncedChecksBean) ennumX.nextElement(); pendingAmount += bcBean.getBouncedAmount(); } }
         */
        if (bcChecks != null && bcChecks.get(custId) != null) {
          bcAmtStr = bcChecks.get(custId);
          bcChecks.remove(custId);
          pendingAmount += Double.parseDouble(bcAmtStr);
        }

        if (pendingAmount == 0) {
          continue;
        } else {
          totBalance += pendingAmount;
          totCustPending++;
        }

        double curr = 0.0;
        double x30days = 0.0;
        double x60days = 0.0;
        double x90days = 0.0;

        String sql1 =
            "select InvoiceNumber, OrderDate, Balance from invoice where balance!=0 and Status!='C' and Status!='W' and customerid='"
                + custId + "' order by OrderDate ";
        Statement stmt1 = con.createStatement();
        ResultSet rs1 = stmt1.executeQuery(sql1);

        while (rs1.next()) {
          String orderDate = DateUtils.convertMySQLToUSFormat(rs1.getString(2));
          double noOfDays = 0;

          try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
            java.util.Date dd = sdf.parse(orderDate);
            long timeDiff = System.currentTimeMillis() - dd.getTime();
            noOfDays = Math.rint(timeDiff / 86400000);
            // java.util.Date ddX = new java.util.Date(dd.getTime()
            // + (1000 * 60 * 60 * 24 * (long)terms));
          } catch (Exception e) {
            logger.error(e.getMessage());
          }

          double bal = rs1.getDouble(3);
          // logger.error(orderDate + "--" + noOfDays + "--" +
          // bal);

          if (noOfDays <= 30) {
            curr += bal;
          } else if (noOfDays <= 60) {
            x30days += bal;
          } else if (noOfDays <= 90) {
            x60days += bal;
          } else {
            x90days += bal;
          }
        }
        if (rs1 != null) {
          rs1.close();
        }
        if (stmt1 != null) {
          stmt1.close();
        }

        totalCurrent += curr;
        total30Days += x30days;
        total60Days += x60days;
        total90Days += x90days;
        if (!bcAmtStr.trim().equals("")) {
          totBCAmt += Double.parseDouble(bcAmtStr);
        } else {
          bcAmtStr = "&nbsp;";
        }

        String pendingAmountStr = pendingAmount + "";
        String currStr = "";
        String x30daysStr = "";
        String x60daysStr = "";
        String x90daysStr = "";

        if (curr == 0) {
          currStr = "&nbsp;";
        } else {
          currStr = curr + "";
          if (currStr.indexOf(".") == currStr.length() - 2) {
            currStr += "0";
          }
          currStr = NumberUtils.cutFractions(currStr);
        }
        if (x30days == 0) {
          x30daysStr = "&nbsp;";
        } else {
          x30daysStr = x30days + "";
          if (x30daysStr.indexOf(".") == x30daysStr.length() - 2) {
            x30daysStr += "0";
          }
          x30daysStr = NumberUtils.cutFractions(x30daysStr);
        }
        if (x60days == 0) {
          x60daysStr = "&nbsp;";
        } else {
          x60daysStr = x60days + "";
          if (x60daysStr.indexOf(".") == x60daysStr.length() - 2) {
            x60daysStr += "0";
          }
          x60daysStr = NumberUtils.cutFractions(x60daysStr);
        }
        if (x90days == 0) {
          x90daysStr = "&nbsp;";
        } else {
          x90daysStr = x90days + "";
          if (x90daysStr.indexOf(".") == x90daysStr.length() - 2) {
            x90daysStr += "0";
          }
          x90daysStr = NumberUtils.cutFractions(x90daysStr);
        }

        if (pendingAmountStr.indexOf(".") == pendingAmountStr.length() - 2) {
          pendingAmountStr += "0";
        }
        pendingAmountStr = NumberUtils.cutFractions(pendingAmountStr);

        Hashtable totData = new Hashtable();
        totData.put("ID", custId);
        totData.put("Customer", companyName);
        totData.put("Terms", terms);
        totData.put("Total", pendingAmountStr);
        totData.put("Current", currStr);
        totData.put("30 Days", x30daysStr);
        totData.put("60 Days", x60daysStr);
        totData.put("90 Days", x90daysStr);
        totData.put("BC Chks", bcAmtStr);

        data.addElement(totData);

      }

      if (bcChecks != null && bcChecks.size() > 0) {
        // logger.error("BC Checks Left: " + bcChecks.size());
        Enumeration<String> ennum = bcChecks.keys();
        while (ennum.hasMoreElements()) {
          String cstId = ennum.nextElement();
          double amt = Double.parseDouble(bcChecks.get(cstId));
          totBCAmt += amt;
          totBalance += amt;

          Hashtable totData = new Hashtable();
          totData.put("ID", cstId);
          totData.put("Customer", CustomerBean.getCompanyName(cstId));
          totData.put("Terms", "&nbsp;");
          totData.put("Total", amt + "");
          totData.put("Current", "&nbsp;");
          totData.put("30 Days", "&nbsp;");
          totData.put("60 Days", "&nbsp;");
          totData.put("90 Days", "&nbsp;");
          totData.put("BC Chks", amt + "");

          data.addElement(totData);
          totCustPending++;

        }
      }

      if (toShowAR == null) {
        throw new UserException(" No Customers have Pending Balances ");
      }

      totalSplitBalance = totalCurrent + total30Days + total60Days + total90Days + totBCAmt;

      String totBalanceStr = totBalance + "";
      String totalSplitBalanceStr = totalSplitBalance + "";
      String totalCurrentStr = totalCurrent + "";
      String total30DaysStr = total30Days + "";
      String total60DaysStr = total60Days + "";
      String total90DaysStr = total90Days + "";
      String totBCAmtStr = totBCAmt + "";
      String currPercStr =
          Math.rint(100 - ((totalSplitBalance - totalCurrent) * 100 / totalSplitBalance)) + "";
      String x30DaysPercStr =
          Math.rint(100 - ((totalSplitBalance - total30Days) * 100 / totalSplitBalance)) + "";
      String x60DaysPercStr =
          Math.rint(100 - ((totalSplitBalance - total60Days) * 100 / totalSplitBalance)) + "";
      String x90DaysPercStr =
          Math.rint(100 - ((totalSplitBalance - total90Days) * 100 / totalSplitBalance)) + "";

      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      if (totalSplitBalanceStr.indexOf(".") == totalSplitBalanceStr.length() - 2) {
        totalSplitBalanceStr += "0";
      }
      if (totalCurrentStr.indexOf(".") == totalCurrentStr.length() - 2) {
        totalCurrentStr += "0";
      }
      if (total30DaysStr.indexOf(".") == total30DaysStr.length() - 2) {
        total30DaysStr += "0";
      }
      if (total60DaysStr.indexOf(".") == total60DaysStr.length() - 2) {
        total60DaysStr += "0";
      }
      if (total90DaysStr.indexOf(".") == total90DaysStr.length() - 2) {
        total90DaysStr += "0";
      }
      if (totBCAmtStr.indexOf(".") == totBCAmtStr.length() - 2) {
        totBCAmtStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);
      totalSplitBalanceStr = NumberUtils.cutFractions(totalSplitBalanceStr);
      totalCurrentStr = NumberUtils.cutFractions(totalCurrentStr);
      total30DaysStr = NumberUtils.cutFractions(total30DaysStr);
      total60DaysStr = NumberUtils.cutFractions(total60DaysStr);
      total90DaysStr = NumberUtils.cutFractions(total90DaysStr);
      totBCAmtStr = NumberUtils.cutFractions(totBCAmtStr);

      totals[0][0] = "Total No. Of Customers Have Payments ";
      totals[0][1] = totCustPending + "";
      totals[1][0] = "Total Payments Receivable ";
      totals[1][1] = totBalanceStr;
      totals[2][0] = "Total Split Payments Receivable ";
      totals[2][1] = totalSplitBalanceStr;
      totals[3][0] = "Current Payments ";
      totals[3][1] = totalCurrentStr;
      totals[4][0] = "% of Current ";
      totals[4][1] = currPercStr;
      totals[5][0] = "Over 30 Days Payments ";
      totals[5][1] = total30DaysStr;
      totals[6][0] = "% of 30 Days ";
      totals[6][1] = x30DaysPercStr;
      totals[7][0] = "Over 60 Days Payments ";
      totals[7][1] = total60DaysStr;
      totals[8][0] = "% of 60 Days ";
      totals[8][1] = x60DaysPercStr;
      totals[9][0] = "Over 90 Days Payments ";
      totals[9][1] = total90DaysStr;
      totals[10][0] = "% of 90 Days ";
      totals[10][1] = x90DaysPercStr;
      totals[11][0] = "Total Bounced Checks Amount ";
      totals[11][1] = totBCAmtStr;

      toShowAR.put("FileName", fileName);
      toShowAR.put("BackScreen", "AcctMenu");
      toShowAR.put("MainHeading", mainHeading);
      toShowAR.put("SubHeadings", subHeadings);
      toShowAR.put("Data", data);
      toShowAR.put("Totals", totals);

      createReport(toShowAR);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    tt = System.currentTimeMillis() - tt;
    double xx = tt / (1000 * 60);
    logger.error("AGEING RECEIVABLES TIME - OLDER VERSION:::" + xx + " Min");
    return toShowAR;
  }

  public static Hashtable<String, String> detailedReceivableNew() throws UserException {
    long tt = System.currentTimeMillis();
    Hashtable toShowAR = null;
    try {
      // select customerid, sum(balance) from invoice group by customerid
      // order by 2 desc
      String fileName = "DR" + DateUtils.getNewUSDate().trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[12][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmtXX = con.createStatement();
      String sqlXX =
          "Select CustomerId, Sum(Balance) From BouncedChecks Where Balance!=0 Group By CustomerId Order By 1 ";
      Hashtable<String, String> bcChecks = null;
      ResultSet rsXX = stmtXX.executeQuery(sqlXX);
      int noOfBCChecks = 0;
      while (rsXX.next()) {
        if (bcChecks == null) {
          bcChecks = new Hashtable<String, String>();
        }
        String cstId = rsXX.getString(1);
        String bcAmt = rsXX.getString(2);
        bcChecks.put(cstId, bcAmt);
        noOfBCChecks++;
      }

      Statement stmt = con.createStatement();
      String sql =
          "select count(distinct customerid) from invoice where balance!=0 and Status!='C' and Status!='W' ";
      ResultSet rs = stmt.executeQuery(sql);
      int noOfBalances = 0;
      if (rs.next()) {
        noOfBalances = rs.getInt(1);
      }

      String[][] list = new String[noOfBalances + noOfBCChecks][9];

      double totBalance = 0.0;
      double totalSplitBalance = 0.0;
      double totalCurrent = 0.0;
      double total30Days = 0.0;
      double total60Days = 0.0;
      double total90Days = 0.0;
      double totBCAmt = 0.0;

      mainHeading = "Ageing Accounts Receivable As On " + DateUtils.getNewUSDate();
      subHeadings.addElement("ID");
      subHeadings.addElement("Customer");
      subHeadings.addElement("Terms");
      subHeadings.addElement("Total");
      subHeadings.addElement("Current");
      subHeadings.addElement("30 Days");
      subHeadings.addElement("60 Days");
      subHeadings.addElement("90 Days");
      subHeadings.addElement("BC Chks");

      String sql1 =
          "select a.CustomerId, b.CompanyName, b.PaymentTerms, a.OrderDate, a.Balance from invoice a, customer b where a.balance!=0 and a.Status!='C' and a.Status!='W' and a.customerid=b.customerId order by 1 ";
      Statement stmt1 = con.createStatement();
      ResultSet rs1 = stmt1.executeQuery(sql1);

      int cntBalances = 0;
      double totalBalance = 0.0;
      double curr = 0.0;
      double x30days = 0.0;
      double x60days = 0.0;
      double x90days = 0.0;

      String currCustId = "";
      while (rs1.next()) {
        String custId = rs1.getString(1);
        String companyName = rs1.getString(2);
        String terms = rs1.getString(3);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs1.getString(4));
        // String orderDate = "2013-09-30";
        double pendingAmount = rs1.getDouble(5);
        double noOfDays = 0;

        if (currCustId.trim().equals("")) {
          currCustId = custId;

          String bcAmtStr = "";
          if (bcChecks != null && bcChecks.get(custId) != null) {
            bcAmtStr = bcChecks.get(custId);
            bcChecks.remove(custId);
            double amt = Double.parseDouble(bcAmtStr);
            totalBalance += amt;
            totBCAmt += amt;
            totBalance += amt;
          }

          list[cntBalances][0] = custId;
          list[cntBalances][1] = companyName;
          list[cntBalances][2] = terms;
          list[cntBalances][8] = bcAmtStr;

        } else if (currCustId.trim().equals(custId)) {
          if (list[cntBalances][3] == null || list[cntBalances][3].trim().equals("")) {
            totalBalance = 0.0;
          } else {
            totalBalance = Double.parseDouble(list[cntBalances][3]);
          }
          if (list[cntBalances][4] == null || list[cntBalances][4].trim().equals("")) {
            curr = 0.0;
          } else {
            curr = Double.parseDouble(list[cntBalances][4]);
          }
          if (list[cntBalances][5] == null || list[cntBalances][5].trim().equals("")) {
            x30days = 0.0;
          } else {
            x30days = Double.parseDouble(list[cntBalances][5]);
          }
          if (list[cntBalances][6] == null || list[cntBalances][6].trim().equals("")) {
            x60days = 0.0;
          } else {
            x60days = Double.parseDouble(list[cntBalances][6]);
          }
          if (list[cntBalances][7] == null || list[cntBalances][7].trim().equals("")) {
            x90days = 0.0;
          } else {
            x90days = Double.parseDouble(list[cntBalances][7]);
          }
        } else if (!currCustId.trim().equals(custId)) {
          currCustId = custId;
          cntBalances++;
          totalBalance = 0;
          curr = 0;
          x30days = 0;
          x60days = 0;
          x90days = 0;

          String bcAmtStr = "";
          if (bcChecks != null && bcChecks.get(custId) != null) {
            bcAmtStr = bcChecks.get(custId);
            bcChecks.remove(custId);
            double amt = Double.parseDouble(bcAmtStr);
            totalBalance = amt;
            totBCAmt += amt;
            totBalance += amt;
          }

          list[cntBalances][0] = custId;
          list[cntBalances][1] = companyName;
          list[cntBalances][2] = terms;
          list[cntBalances][8] = bcAmtStr;

        }

        try {
          java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
          java.util.Date dd = sdf.parse(orderDate);
          long timeDiff = System.currentTimeMillis() - dd.getTime();
          noOfDays = Math.rint(timeDiff / 86400000);
          // java.util.Date ddX = new java.util.Date(dd.getTime() +
          // (1000 * 60 * 60 * 24 * (long)terms));
        } catch (Exception e) {
          logger.error(e.getMessage());
        }

        double bal = pendingAmount;
        // logger.error(orderDate + "--" + noOfDays + "--" + bal);

        if (noOfDays <= 30) {
          curr += bal;
          totalCurrent += bal;
        } else if (noOfDays <= 60) {
          x30days += bal;
          total30Days += bal;
        } else if (noOfDays <= 90) {
          x60days += bal;
          total60Days += bal;
        } else {
          x90days += bal;
          total90Days += bal;
        }

        totalBalance += bal;
        totBalance += bal;

        list[cntBalances][3] = totalBalance + "";
        list[cntBalances][4] = curr + "";
        list[cntBalances][5] = x30days + "";
        list[cntBalances][6] = x60days + "";
        list[cntBalances][7] = x90days + "";

      }

      if (bcChecks != null && bcChecks.size() > 0) {
        // logger.error("BC Checks Left: " + bcChecks.size());
        Enumeration<String> ennum = bcChecks.keys();
        while (ennum.hasMoreElements()) {
          String cstId = ennum.nextElement();
          double amt = Double.parseDouble(bcChecks.get(cstId));
          totBCAmt += amt;
          totBalance += amt;

          list[cntBalances][0] = cstId;
          list[cntBalances][1] = CustomerBean.getCompanyName(cstId);
          list[cntBalances][2] = "&nbsp;";
          list[cntBalances][3] = amt + "";
          list[cntBalances][4] = "0";
          list[cntBalances][5] = "0";
          list[cntBalances][6] = "0";
          list[cntBalances][7] = "0";
          list[cntBalances][8] = amt + "";

          cntBalances++;

        }
      }

      int outCnt = 0;
      int inCnt = 0;
      for (outCnt = cntBalances - 1; outCnt > 1; outCnt--) {
        for (inCnt = 0; inCnt < outCnt; inCnt++) {
          if (Double.parseDouble(list[inCnt][3]) < Double.parseDouble(list[inCnt + 1][3])) {
            String t0 = list[inCnt][0];
            String t1 = list[inCnt][1];
            String t2 = list[inCnt][2];
            String t3 = list[inCnt][3];
            String t4 = list[inCnt][4];
            String t5 = list[inCnt][5];
            String t6 = list[inCnt][6];
            String t7 = list[inCnt][7];
            String t8 = list[inCnt][8];

            list[inCnt][0] = list[inCnt + 1][0];
            list[inCnt][1] = list[inCnt + 1][1];
            list[inCnt][2] = list[inCnt + 1][2];
            list[inCnt][3] = list[inCnt + 1][3];
            list[inCnt][4] = list[inCnt + 1][4];
            list[inCnt][5] = list[inCnt + 1][5];
            list[inCnt][6] = list[inCnt + 1][6];
            list[inCnt][7] = list[inCnt + 1][7];
            list[inCnt][8] = list[inCnt + 1][8];

            list[inCnt + 1][0] = t0;
            list[inCnt + 1][1] = t1;
            list[inCnt + 1][2] = t2;
            list[inCnt + 1][3] = t3;
            list[inCnt + 1][4] = t4;
            list[inCnt + 1][5] = t5;
            list[inCnt + 1][6] = t6;
            list[inCnt + 1][7] = t7;
            list[inCnt + 1][8] = t8;

            // swap(in, in+1);
            // long temp = a[one];
            // a[one] = a[two];
            // a[two] = temp;
          }
        }
      }

      for (int i = 0; i < cntBalances; i++) {
        if (toShowAR == null) {
          toShowAR = new Hashtable();
        }

        String custId = list[i][0];
        // logger.error("Detail:" + custId);
        String companyName = list[i][1];
        String terms = list[i][2];
        double pendingAmount = Double.parseDouble(list[i][3]);
        curr = Double.parseDouble(list[i][4]);
        x30days = Double.parseDouble(list[i][5]);
        x60days = Double.parseDouble(list[i][6]);
        x90days = Double.parseDouble(list[i][7]);
        String bcAmtStr = list[i][8];

        if (terms == null) {
          terms = "&nbsp;";
        } else if (terms.trim().equals("")) {
          terms = "&nbsp;";
        } else if (terms.trim().equalsIgnoreCase("C")) {
          terms = "Cod";
        } else if (terms.trim().equalsIgnoreCase("O")) {
          terms = "Cash";
        } else if (terms.trim().equalsIgnoreCase("W")) {
          terms = "Wkly";
        } else if (terms.trim().equalsIgnoreCase("B")) {
          terms = "Bi-Wk";
        } else if (terms.trim().equalsIgnoreCase("M")) {
          terms = "Mthly";
        }

        String pendingAmountStr = pendingAmount + "";
        String currStr = "";
        String x30daysStr = "";
        String x60daysStr = "";
        String x90daysStr = "";

        if (curr == 0) {
          currStr = "&nbsp;";
        } else {
          currStr = curr + "";
          if (currStr.indexOf(".") == currStr.length() - 2) {
            currStr += "0";
          }
          currStr = NumberUtils.cutFractions(currStr);
        }
        if (x30days == 0) {
          x30daysStr = "&nbsp;";
        } else {
          x30daysStr = x30days + "";
          if (x30daysStr.indexOf(".") == x30daysStr.length() - 2) {
            x30daysStr += "0";
          }
          x30daysStr = NumberUtils.cutFractions(x30daysStr);
        }
        if (x60days == 0) {
          x60daysStr = "&nbsp;";
        } else {
          x60daysStr = x60days + "";
          if (x60daysStr.indexOf(".") == x60daysStr.length() - 2) {
            x60daysStr += "0";
          }
          x60daysStr = NumberUtils.cutFractions(x60daysStr);
        }
        if (x90days == 0) {
          x90daysStr = "&nbsp;";
        } else {
          x90daysStr = x90days + "";
          if (x90daysStr.indexOf(".") == x90daysStr.length() - 2) {
            x90daysStr += "0";
          }
          x90daysStr = NumberUtils.cutFractions(x90daysStr);
        }

        if (pendingAmountStr.indexOf(".") == pendingAmountStr.length() - 2) {
          pendingAmountStr += "0";
        }
        pendingAmountStr = NumberUtils.cutFractions(pendingAmountStr);

        if (bcAmtStr.trim().equals("")) {
          bcAmtStr = "&nbsp;";
        }

        Hashtable totData = new Hashtable();
        totData.put("ID", custId);
        totData.put("Customer", companyName);
        totData.put("Terms", terms);
        totData.put("Total", pendingAmountStr);
        totData.put("Current", currStr);
        totData.put("30 Days", x30daysStr);
        totData.put("60 Days", x60daysStr);
        totData.put("90 Days", x90daysStr);
        totData.put("BC Chks", bcAmtStr);

        data.addElement(totData);

      }

      // ---------------------------------------------------------------
      // ---------------------------------------------------------------
      if (toShowAR == null) {
        throw new UserException(" No Customers have Pending Balances ");
      }

      totalSplitBalance = totalCurrent + total30Days + total60Days + total90Days + totBCAmt;

      String totBalanceStr = totBalance + "";
      String totalSplitBalanceStr = totalSplitBalance + "";
      String totalCurrentStr = totalCurrent + "";
      String total30DaysStr = total30Days + "";
      String total60DaysStr = total60Days + "";
      String total90DaysStr = total90Days + "";
      String totBCAmtStr = totBCAmt + "";
      String currPercStr =
          Math.rint(100 - ((totalSplitBalance - totalCurrent) * 100 / totalSplitBalance)) + "";
      String x30DaysPercStr =
          Math.rint(100 - ((totalSplitBalance - total30Days) * 100 / totalSplitBalance)) + "";
      String x60DaysPercStr =
          Math.rint(100 - ((totalSplitBalance - total60Days) * 100 / totalSplitBalance)) + "";
      String x90DaysPercStr =
          Math.rint(100 - ((totalSplitBalance - total90Days) * 100 / totalSplitBalance)) + "";

      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      if (totalSplitBalanceStr.indexOf(".") == totalSplitBalanceStr.length() - 2) {
        totalSplitBalanceStr += "0";
      }
      if (totalCurrentStr.indexOf(".") == totalCurrentStr.length() - 2) {
        totalCurrentStr += "0";
      }
      if (total30DaysStr.indexOf(".") == total30DaysStr.length() - 2) {
        total30DaysStr += "0";
      }
      if (total60DaysStr.indexOf(".") == total60DaysStr.length() - 2) {
        total60DaysStr += "0";
      }
      if (total90DaysStr.indexOf(".") == total90DaysStr.length() - 2) {
        total90DaysStr += "0";
      }
      if (totBCAmtStr.indexOf(".") == totBCAmtStr.length() - 2) {
        totBCAmtStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);
      totalSplitBalanceStr = NumberUtils.cutFractions(totalSplitBalanceStr);
      totalCurrentStr = NumberUtils.cutFractions(totalCurrentStr);
      total30DaysStr = NumberUtils.cutFractions(total30DaysStr);
      total60DaysStr = NumberUtils.cutFractions(total60DaysStr);
      total90DaysStr = NumberUtils.cutFractions(total90DaysStr);
      totBCAmtStr = NumberUtils.cutFractions(totBCAmtStr);

      totals[0][0] = "Total No. Of Customers Have Payments ";
      totals[0][1] = cntBalances + "";
      totals[1][0] = "Total Payments Receivable ";
      totals[1][1] = totBalanceStr;
      totals[2][0] = "Total Split Payments Receivable ";
      totals[2][1] = totalSplitBalanceStr;
      totals[3][0] = "Current Payments ";
      totals[3][1] = totalCurrentStr;
      totals[4][0] = "% of Current ";
      totals[4][1] = currPercStr;
      totals[5][0] = "Over 30 Days Payments ";
      totals[5][1] = total30DaysStr;
      totals[6][0] = "% of 30 Days ";
      totals[6][1] = x30DaysPercStr;
      totals[7][0] = "Over 60 Days Payments ";
      totals[7][1] = total60DaysStr;
      totals[8][0] = "% of 60 Days ";
      totals[8][1] = x60DaysPercStr;
      totals[9][0] = "Over 90 Days Payments ";
      totals[9][1] = total90DaysStr;
      totals[10][0] = "% of 90 Days ";
      totals[10][1] = x90DaysPercStr;
      totals[11][0] = "Total Bounced Checks Amount ";
      totals[11][1] = totBCAmtStr;

      toShowAR.put("FileName", fileName);
      toShowAR.put("BackScreen", "AcctMenu");
      toShowAR.put("MainHeading", mainHeading);
      toShowAR.put("SubHeadings", subHeadings);
      toShowAR.put("Data", data);
      toShowAR.put("Totals", totals);

      createReport(toShowAR);
      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    tt = System.currentTimeMillis() - tt;
    double xx = tt / (1000 * 60);
    logger.error("AGEING RECEIVABLES TIME - NEW VERSION:::" + xx + " Min");
    return toShowAR;
  }

  public static Hashtable<String, String> accountsReceivable() throws UserException {
    Hashtable toShowAR = null;
    try {
      // select customerid, sum(balance) from invoice group by customerid
      // order by 2 desc
      String fileName = "AR" + DateUtils.getNewUSDate().trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "select a.customerid, b.companyname, sum(a.balance) from invoice a, customer b where a.balance!=0 and a.Status!='C' and a.Status!='W' and a.customerid=b.customerid group by a.customerid order by 3 desc";
      // logger.error(sql);

      ResultSet rs = stmt.executeQuery(sql);

      int totCustPending = 0;
      double totBalance = 0.0;

      mainHeading = "Accounts Receivable As On " + DateUtils.getNewUSDate();
      subHeadings.addElement("Customer Id");
      subHeadings.addElement("Company Name");
      subHeadings.addElement("Receivable Amount");

      Statement stmtXX = con.createStatement();
      String sqlXX =
          "Select CustomerId, Sum(Balance) From BouncedChecks Where Balance!=0 Group By CustomerId Order By 1 ";
      Hashtable<String, String> bcChecks = null;
      ResultSet rsXX = stmtXX.executeQuery(sqlXX);
      while (rsXX.next()) {
        if (bcChecks == null) {
          bcChecks = new Hashtable<String, String>();
        }
        String cstId = rsXX.getString(1);
        String bcAmt = rsXX.getString(2);
        bcChecks.put(cstId, bcAmt);
      }

      while (rs.next()) {
        if (toShowAR == null) {
          toShowAR = new Hashtable();
        }

        String custId = rs.getString(1);
        String companyName = rs.getString(2);
        double pendingAmount = rs.getDouble(3);

        /*
         * Vector v = BouncedChecksBean.getAllBouncedChecks(custId); if (v != null) { Enumeration
         * ennumX = v.elements(); while (ennumX.hasMoreElements()) { BouncedChecksBean bcBean =
         * (BouncedChecksBean) ennumX.nextElement(); pendingAmount += bcBean.getBalance(); } }
         */

        if (bcChecks != null && bcChecks.get(custId) != null) {
          String bcAmtStr = bcChecks.get(custId);
          bcChecks.remove(custId);
          pendingAmount += Double.parseDouble(bcAmtStr);
        }

        if (pendingAmount == 0) {
          continue;
        } else if (pendingAmount < 0) {
          continue;
        } else if (pendingAmount > 0) {
          totBalance += pendingAmount;
          totCustPending++;
        }

        String pendingAmountStr = pendingAmount + "";

        if (pendingAmountStr.indexOf(".") == pendingAmountStr.length() - 2) {
          pendingAmountStr += "0";
        }
        pendingAmountStr = NumberUtils.cutFractions(pendingAmountStr);

        Hashtable totData = new Hashtable();
        totData.put("Customer Id", custId);
        totData.put("Company Name", companyName);
        totData.put("Receivable Amount", pendingAmountStr);

        data.addElement(totData);

      }

      if (bcChecks != null && bcChecks.size() > 0) {
        // logger.error("BC Checks Left: " + bcChecks.size());
        Enumeration<String> ennum = bcChecks.keys();
        while (ennum.hasMoreElements()) {
          String cstId = ennum.nextElement();
          double amt = Double.parseDouble(bcChecks.get(cstId));
          if (amt > 0) {
            totBalance += amt;
          } else {
            continue;
          }

          Hashtable totData = new Hashtable();
          totData.put("Customer Id", cstId);
          totData.put("Company Name", CustomerBean.getCompanyName(cstId));
          totData.put("Receivable Amount", amt + "");

          data.addElement(totData);
          totCustPending++;

        }
      }

      if (toShowAR == null) {
        throw new UserException(" No Customers have Pending Balances ");
      }

      String totBalanceStr = totBalance + "";

      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);

      totals[0][0] = "Total No. Of Customers Have Payments ";
      totals[0][1] = totCustPending + "";
      totals[1][0] = "Total Payments Receivable ";
      totals[1][1] = totBalanceStr;

      toShowAR.put("FileName", fileName);
      toShowAR.put("BackScreen", "AcctMenu");
      toShowAR.put("MainHeading", mainHeading);
      toShowAR.put("SubHeadings", subHeadings);
      toShowAR.put("Data", data);
      toShowAR.put("Totals", totals);

      createReport(toShowAR);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowAR;
  }

  public static Hashtable<String, String> accountsPayable() throws UserException {
    Hashtable toShowAP = null;
    try {
      // select customerid, sum(balance) from invoice group by customerid
      // order by 2 desc
      String fileName = "AP" + DateUtils.getNewUSDate().trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "select a.customerid, b.companyname, sum(a.balance) from invoice a, customer b where a.balance!=0 and a.Status!='C' and a.Status!='W' and a.customerid=b.customerid group by a.customerid order by 3 desc";
      // logger.error(sql);

      ResultSet rs = stmt.executeQuery(sql);

      int totCreditCustPending = 0;
      double totCreditBalance = 0.0;

      mainHeading = "Accounts Payable As On " + DateUtils.getNewUSDate();
      subHeadings.addElement("Customer Id");
      subHeadings.addElement("Company Name");
      subHeadings.addElement("Amount Payable");

      Statement stmtXX = con.createStatement();
      String sqlXX =
          "Select CustomerId, Sum(Balance) From BouncedChecks Where Balance!=0 Group By CustomerId Order By 1 ";
      Hashtable<String, String> bcChecks = null;
      ResultSet rsXX = stmtXX.executeQuery(sqlXX);
      while (rsXX.next()) {
        if (bcChecks == null) {
          bcChecks = new Hashtable<String, String>();
        }
        String cstId = rsXX.getString(1);
        String bcAmt = rsXX.getString(2);
        bcChecks.put(cstId, bcAmt);
      }

      while (rs.next()) {
        if (toShowAP == null) {
          toShowAP = new Hashtable();
        }

        String custId = rs.getString(1);
        String companyName = rs.getString(2);
        double pendingAmount = rs.getDouble(3);

        /*
         * Vector v = BouncedChecksBean.getAllBouncedChecks(custId); if (v != null) { Enumeration
         * enumX = v.elements(); while (enumX.hasMoreElements()) { BouncedChecksBean bcBean =
         * (BouncedChecksBean) enumX.nextElement(); pendingAmount += bcBean.getBalance(); } }
         */

        if (bcChecks != null && bcChecks.get(custId) != null) {
          String bcAmtStr = bcChecks.get(custId);
          bcChecks.remove(custId);
          pendingAmount += Double.parseDouble(bcAmtStr);
        }

        if (pendingAmount == 0) {
          continue;
        } else if (pendingAmount > 0) {
          continue;
        } else if (pendingAmount < 0) {
          totCreditBalance += pendingAmount;
          totCreditCustPending++;
        }

        String pendingAmountStr = pendingAmount + "";

        if (pendingAmountStr.indexOf(".") == pendingAmountStr.length() - 2) {
          pendingAmountStr += "0";
        }
        pendingAmountStr = NumberUtils.cutFractions(pendingAmountStr);

        Hashtable totData = new Hashtable();
        totData.put("Customer Id", custId);
        totData.put("Company Name", companyName);
        totData.put("Amount Payable", pendingAmountStr);

        data.addElement(totData);

      }

      if (bcChecks != null && bcChecks.size() > 0) {
        // logger.error("BC Checks Left: " + bcChecks.size());
        Enumeration<String> ennum = bcChecks.keys();
        while (ennum.hasMoreElements()) {
          String cstId = ennum.nextElement();
          double amt = Double.parseDouble(bcChecks.get(cstId));
          if (amt < 0) {
            totCreditBalance += amt;
          } else {
            continue;
          }

          Hashtable totData = new Hashtable();
          totData.put("Customer Id", cstId);
          totData.put("Company Name", CustomerBean.getCompanyName(cstId));
          totData.put("Receivable Amount", amt + "");

          data.addElement(totData);
          totCreditCustPending++;

        }
      }

      if (toShowAP == null) {
        throw new UserException(" No Customers have Pending Balances ");
      }

      String totCreditBalanceStr = totCreditBalance + "";

      if (totCreditBalanceStr.indexOf(".") == totCreditBalanceStr.length() - 2) {
        totCreditBalanceStr += "0";
      }
      totCreditBalanceStr = NumberUtils.cutFractions(totCreditBalanceStr);

      totals[0][0] = "Total No. Of Customers Have Credit ";
      totals[0][1] = totCreditCustPending + "";
      totals[1][0] = "Total Credits Payable ";
      totals[1][1] = totCreditBalanceStr;

      toShowAP.put("FileName", fileName);
      toShowAP.put("BackScreen", "AcctMenu");
      toShowAP.put("MainHeading", mainHeading);
      toShowAP.put("SubHeadings", subHeadings);
      toShowAP.put("Data", data);
      toShowAP.put("Totals", totals);

      createReport(toShowAP);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowAP;
  }

  public static Hashtable<String, String> showPendingInvoices() throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowPendInv" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.OrderDate, a.SalesPerson, a.CustomerId, b.CompanyName, a.Balance FROM Invoice a, Customer b WHERE ";
      sql +=
          " a.CustomerId=b.CustomerId and a.Balance!=0 and a.Status!='C' and a.Status!='W' Order by 1 ";

      ResultSet rs = stmt.executeQuery(sql);

      double totBalance = 0.0;
      int totCnt = 0;

      mainHeading = "PENDING INVOICES AS ON " + DateUtils.getNewUSDate();
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Date");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Balance");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String invoiceNo = rs.getString(1);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String newSalesPerson = rs.getString(3);
        String custId = rs.getString(4);
        String custName = rs.getString(5);
        double balance = rs.getDouble(6);

        String balanceStr = balance + "";
        if (balanceStr.indexOf(".") == balanceStr.length() - 2) {
          balanceStr += "0";
        }

        totBalance += balance;

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Inv. Date", orderDate);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);
        totData.put("Balance", balanceStr);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      String totBalanceStr = totBalance + "";
      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);

      totals[0][0] = "Total Balance";
      totals[0][1] = totBalanceStr;
      totals[1][0] = "Total No Of Invoices";
      totals[1][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "AcctMenu");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable codPendingInvoices(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowCODPendInv" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.OrderDate, a.SalesPerson, a.CustomerId, b.CompanyName, a.Balance FROM Invoice a, Customer b WHERE ";
      sql +=
          " a.CustomerId=b.CustomerId and a.Balance!=0 and a.Status!='C' and a.Status!='W' and (b.PaymentTerms='C' or b.PaymentTerms='O') ";
      if (toDate.trim().equals("")) {
        sql +=
            " and a.OrderDate='" + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate()) + "'";
      } else if (fromDate.trim().equals(toDate.trim())) {
        sql += " and a.OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      } else {
        sql +=
            " and a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate)
                + "' and a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      }
      sql += " Order By 1 ";

      ResultSet rs = stmt.executeQuery(sql);

      double totBalance = 0.0;
      int totCnt = 0;

      mainHeading = "PENDING COD & CASH INVOICES AS ON " + DateUtils.getNewUSDate();
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Date");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Balance");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String invoiceNo = rs.getString(1);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String newSalesPerson = rs.getString(3);
        String custId = rs.getString(4);
        String custName = rs.getString(5);
        double balance = rs.getDouble(6);

        String balanceStr = balance + "";
        if (balanceStr.indexOf(".") == balanceStr.length() - 2) {
          balanceStr += "0";
        }

        totBalance += balance;

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Inv. Date", orderDate);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);
        totData.put("Balance", balanceStr);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      String totBalanceStr = totBalance + "";
      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);

      if (user.getRole().trim().equalsIgnoreCase("Acct")) {
        totBalanceStr = "0.0";
      }

      totals[0][0] = "Total Balance";
      totals[0][1] = totBalanceStr;
      totals[1][0] = "Total No Of Invoices";
      totals[1][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable otherPendingInvoices(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowOtherPendInv" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.OrderDate, a.SalesPerson, a.CustomerId, b.CompanyName, a.Balance FROM Invoice a, Customer b WHERE ";
      sql +=
          " a.CustomerId=b.CustomerId and a.Balance!=0 and a.Status!='C' and a.Status!='W' and b.PaymentTerms!='C' and b.PaymentTerms!='O' ";
      if (toDate.trim().equals("")) {
        sql +=
            " and a.OrderDate='" + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate()) + "'";
      } else if (fromDate.trim().equals(toDate.trim())) {
        sql += " and a.OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      } else {
        sql +=
            " and a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate)
                + "' and a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      }
      sql += " Order By 1 ";

      ResultSet rs = stmt.executeQuery(sql);

      double totBalance = 0.0;
      int totCnt = 0;

      mainHeading = "PENDING ACCOUNTS INVOICES AS ON " + DateUtils.getNewUSDate();
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Date");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Balance");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String invoiceNo = rs.getString(1);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String newSalesPerson = rs.getString(3);
        String custId = rs.getString(4);
        String custName = rs.getString(5);
        double balance = rs.getDouble(6);

        String balanceStr = balance + "";
        if (balanceStr.indexOf(".") == balanceStr.length() - 2) {
          balanceStr += "0";
        }

        totBalance += balance;

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Inv. Date", orderDate);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);
        totData.put("Balance", balanceStr);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      String totBalanceStr = totBalance + "";
      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);

      if (user.getRole().trim().equalsIgnoreCase("Acct")) {
        totBalanceStr = "0.0";
      }

      totals[0][0] = "Total Balance";
      totals[0][1] = totBalanceStr;
      totals[1][0] = "Total No Of Invoices";
      totals[1][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable allPendingInvoices(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowAllPendInv" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.OrderDate, a.SalesPerson, a.CustomerId, b.CompanyName, a.Balance, b.PaymentTerms FROM Invoice a, Customer b WHERE ";
      sql += " a.CustomerId=b.CustomerId and a.Balance!=0 and a.Status!='C' and a.Status!='W' ";
      if (toDate.trim().equals("")) {
        sql +=
            " and a.OrderDate='" + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate()) + "'";
      } else if (fromDate.trim().equals(toDate.trim())) {
        sql += " and a.OrderDate='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      } else {
        sql +=
            " and a.OrderDate>='" + DateUtils.convertUSToMySQLFormat(fromDate)
                + "' and a.OrderDate<='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      }
      sql += " Order By 1 ";

      ResultSet rs = stmt.executeQuery(sql);

      double totBalance = 0.0;
      int totCnt = 0;

      mainHeading = "PENDING INVOICES AS ON " + DateUtils.getNewUSDate();
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Date");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Balance");
      subHeadings.addElement("Terms");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String invoiceNo = rs.getString(1);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String newSalesPerson = rs.getString(3);
        String custId = rs.getString(4);
        String custName = rs.getString(5);
        double balance = rs.getDouble(6);
        String terms = rs.getString(7);

        String balanceStr = balance + "";
        if (balanceStr.indexOf(".") == balanceStr.length() - 2) {
          balanceStr += "0";
        }

        totBalance += balance;

        if (terms == null || terms.trim().equals("")) {
          terms = "&nbsp;";
        } else if (terms.trim().equalsIgnoreCase("C")) {
          terms = "COD";
        } else if (terms.trim().equalsIgnoreCase("O")) {
          terms = "CASH";
        } else if (terms.trim().equalsIgnoreCase("B")) {
          terms = "BI-WK";
        } else if (terms.trim().equalsIgnoreCase("W")) {
          terms = "WKLY";
        } else if (terms.trim().equalsIgnoreCase("M")) {
          terms = "MTHLY";
        }

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Inv. Date", orderDate);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);
        totData.put("Balance", balanceStr);
        totData.put("Terms", terms);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      String totBalanceStr = totBalance + "";
      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);

      if (user.getRole().trim().equalsIgnoreCase("Acct")) {
        totBalanceStr = "0.0";
      }

      totals[0][0] = "Total Balance";
      totals[0][1] = totBalanceStr;
      totals[1][0] = "Total No Of Invoices";
      totals[1][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable showDeposits(String fromDate, String toDate) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowDepo" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql = "SELECT AppliedDate, sum(AppliedAmount) FROM AppliedAmounts WHERE ";
      if (toDate.trim().equals("")) {
        sql += " AppliedDate='" + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate()) + "'";
      } else if (fromDate.trim().equals(toDate.trim())) {
        sql += " AppliedDate='" + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      } else {
        sql +=
            " AppliedDate>='" + DateUtils.convertUSToMySQLFormat(fromDate) + "' and AppliedDate<='"
                + DateUtils.convertUSToMySQLFormat(toDate) + "'";
      }
      sql += " Group By AppliedDate Order By 1 ";

      ResultSet rs = stmt.executeQuery(sql);

      // double totDeposit = 0.0;
      BigDecimal totDeposit = new BigDecimal("0.00");
      totDeposit = totDeposit.setScale(2, BigDecimal.ROUND_CEILING);
      int totCnt = 0;

      mainHeading = "DEPOSITS FROM " + fromDate + " TO " + toDate;
      subHeadings.addElement("Date");
      subHeadings.addElement("Deposit");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String dd = DateUtils.convertMySQLToUSFormat(rs.getString(1));
        BigDecimal depoAmount = rs.getBigDecimal(2);

        totDeposit = totDeposit.add(depoAmount);

        Hashtable totData = new Hashtable();
        totData.put("Date", dd);
        if (depoAmount != null) {
          totData.put("Deposit", depoAmount.toString());
        }

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      String totDepositStr = "";

      // System.out.println("totDeposit.toString()..."+totDeposit.toString());

      if (totDeposit != null) {
        totDepositStr = totDeposit.toString();

        // System.out.println("totDepositStr..."+totDepositStr);
      }

      /*
       * String totDepositStr = totDeposit + ""; if (totDepositStr.indexOf(".") ==
       * totDepositStr.length() - 2) { totDepositStr += "0"; } totDepositStr =
       * NumberUtils.cutFractions(totDepositStr);
       */
      totals[0][0] = "Total Deposit Amount";
      totals[0][1] = totDepositStr;
      totals[1][0] = "Total No Of Deposits";
      totals[1][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable showInvoicesNotPrinted(UserBean user) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowNonPrintInv" + user.getUsername() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.OrderDate, a.SalesPerson, a.CustomerId, b.CompanyName FROM Invoice a, Customer b WHERE ";
      sql += " a.CustomerId=b.CustomerId and IsPrinted!='Y' and IsDelivered!='Y' and a.Balance!=0 ";
      if (!user.getRole().trim().equalsIgnoreCase("High")
          && !user.getRole().trim().equalsIgnoreCase("Acct")
          && !user.getUsername().trim().equalsIgnoreCase("Marcie")) {
        sql += " AND a.SalesPerson='" + user.getUsername() + "' ";
      }

      ResultSet rs = stmt.executeQuery(sql);

      int totCnt = 0;

      mainHeading = "INVOICES NOT PRINTED AS ON " + DateUtils.getNewUSDate();
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Date");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String invoiceNo = rs.getString(1);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String newSalesPerson = rs.getString(3);
        String custId = rs.getString(4);
        String custName = rs.getString(5);

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Inv. Date", orderDate);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Inovices ");
      }

      totals[0][0] = "Total No Of Invoices";
      totals[0][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable showInvoicesNotPickedUp(UserBean user, boolean cods) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowNonPrintInv" + user.getUsername() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.InvoiceNumber, a.OrderDate, a.SalesPerson, a.CustomerId, b.CompanyName, a.Balance FROM Invoice a, Customer b WHERE ";
      sql +=
          " a.Status!='C' and a.Status!='W' and a.Balance>0 and a.ShipVia='Pick-Up' and a.CustomerId=b.CustomerId ";
      if (cods) {
        sql += " and b.PaymentTerms!='M' and b.PaymentTerms!='B' and b.PaymentTerms!='W' ";
      } else {
        sql += " and b.PaymentTerms!='C' and b.PaymentTerms!='O' ";
      }
      if (!user.getRole().trim().equalsIgnoreCase("High")
          && !user.getRole().trim().equalsIgnoreCase("Acct")
          && !user.getUsername().trim().equalsIgnoreCase("Marcie")) {
        sql += " AND a.SalesPerson='" + user.getUsername() + "' ";
      }
      sql += " Order by 1 desc ";

      ResultSet rs = stmt.executeQuery(sql);

      int totCnt = 0;

      if (cods) {
        mainHeading = "PENDING PICKUP COD & CASH INVOICES AS ON " + DateUtils.getNewUSDate();
      } else {
        mainHeading = "PENDING PICKUP ACCOUNTS INVOICES AS ON " + DateUtils.getNewUSDate();
      }
      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Inv. Date");
      subHeadings.addElement("Sales Person");
      subHeadings.addElement("Cust Id");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Balance");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String invoiceNo = rs.getString(1);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String newSalesPerson = rs.getString(3);
        String custId = rs.getString(4);
        String custName = rs.getString(5);
        double balance = rs.getDouble(6);

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Inv. Date", orderDate);
        totData.put("Sales Person", newSalesPerson);
        totData.put("Cust Id", custId);
        totData.put("Cust Name", custName);
        totData.put("Balance", balance + "");

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Inovices ");
      }

      totals[0][0] = "Total No Of Invoices";
      totals[0][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "TodaysOrders");
      toShowInvoices.put("MainHeading", mainHeading);
      toShowInvoices.put("SubHeadings", subHeadings);
      toShowInvoices.put("Data", data);
      toShowInvoices.put("Totals", totals);

      createReport(toShowInvoices);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return toShowInvoices;
  }

  public static Hashtable createHistoryReport(int invNo) throws UserException {
    Hashtable invoiceHistory = null;
    try {

      String fileName = "Hist" + invNo + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "SELECT * From InvoiceHistory Where InvoiceNumber=" + invNo + " Order By ModifiedOrder ";

      ResultSet rs = stmt.executeQuery(sql);

      mainHeading = "History for the Invoice " + invNo;

      subHeadings.addElement("Modified Date");
      subHeadings.addElement("Modified By");
      subHeadings.addElement("Remarks");

      int totalChanges = 0;

      while (rs.next()) {
        if (invoiceHistory == null) {
          invoiceHistory = new Hashtable();
        }

        totalChanges++;

        String modifiedBy = rs.getString("ModifiedBy");
        String modifiedDate = rs.getString("ModifiedDate");
        String remarks =
            rs.getString("Remarks1") + rs.getString("Remarks2") + rs.getString("Remarks3")
                + rs.getString("Remarks4") + rs.getString("Remarks5");

        Hashtable totData = new Hashtable();
        totData.put("Modified Date", DateUtils.convertMySQLToUSFormat(modifiedDate));
        totData.put("Modified By", modifiedBy);
        totData.put("Remarks", remarks);

        data.addElement(totData);

      }

      if (invoiceHistory == null) {
        throw new UserException(" No Changes On This Invoice ");
      }

      totals[0][0] = "Total No. Of Changes Found";
      totals[0][1] = totalChanges + "";

      invoiceHistory.put("FileName", fileName);
      invoiceHistory.put("BackScreen", "CloseInvoices");
      invoiceHistory.put("MainHeading", mainHeading);
      invoiceHistory.put("SubHeadings", subHeadings);
      invoiceHistory.put("Data", data);
      invoiceHistory.put("Totals", totals);

      createReport(invoiceHistory);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return invoiceHistory;
  }

  public static Hashtable createHistoryReport(String fromDate, String toDate) throws UserException {
    Hashtable invoiceHistory = null;
    try {

      String fileName = "Hist" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      if (fromDate == null) {
        fromDate = "";
      }

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql = "SELECT * From InvoiceHistory Where ";

      mainHeading = "Invoice History Report :";
      if (fromDate.trim().equals("")) {
        mainHeading += " For " + toDate.trim();
        sql +=
            " ModifiedDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim())
                + "' And Remarks1 Not like 'Printed%' Order By InvoiceNumber ";
      } else if (fromDate.trim().equals(toDate.trim())) {
        mainHeading += " For " + toDate.trim();
        sql +=
            " ModifiedDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim())
                + "' And Remarks1 Not like 'Printed%' Order By InvoiceNumber ";
      } else {
        mainHeading += " From " + fromDate.trim() + " To " + toDate.trim();
        sql +=
            " ModifiedDate >= '" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' and ModifiedDate <= '" + DateUtils.convertUSToMySQLFormat(toDate.trim())
                + "' And Remarks1 Not like 'Printed%' Order By ModifiedDate, InvoiceNumber ";
      }

      ResultSet rs = stmt.executeQuery(sql);

      subHeadings.addElement("Inv. No");
      subHeadings.addElement("Date");
      subHeadings.addElement("Who");
      subHeadings.addElement("Remarks");

      int totalChanges = 0;

      while (rs.next()) {
        if (invoiceHistory == null) {
          invoiceHistory = new Hashtable();
        }

        totalChanges++;

        int invNo = rs.getInt("InvoiceNumber");
        String modifiedBy = rs.getString("ModifiedBy");
        String modifiedDate = rs.getString("ModifiedDate");
        String remarks =
            rs.getString("Remarks1") + rs.getString("Remarks2") + rs.getString("Remarks3")
                + rs.getString("Remarks4") + rs.getString("Remarks5");

        Hashtable totData = new Hashtable();
        totData.put("Inv. No", invNo + "");
        totData.put("Date", DateUtils.convertMySQLToUSFormat(modifiedDate));
        totData.put("Who", modifiedBy);
        totData.put("Remarks", remarks);

        data.addElement(totData);

      }

      if (invoiceHistory == null) {
        throw new UserException(" No Changes On This Invoice ");
      }

      totals[0][0] = "Total No. Of Changes Found";
      totals[0][1] = totalChanges + "";

      invoiceHistory.put("FileName", fileName);
      invoiceHistory.put("BackScreen", "TodaysOrders");
      invoiceHistory.put("MainHeading", mainHeading);
      invoiceHistory.put("SubHeadings", subHeadings);
      invoiceHistory.put("Data", data);
      invoiceHistory.put("Totals", totals);

      createReport(invoiceHistory);
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      throw new UserException(e.getMessage());
    }
    return invoiceHistory;
  }

  public static void createReport(Hashtable toShowSales) {
    try {

      String fileName = (String) toShowSales.get("FileName");
      String mainHeading = (String) toShowSales.get("MainHeading");
      Vector subHeadings = (Vector) toShowSales.get("SubHeadings");
      int arraySize = subHeadings.size();
      String[] strSubHeads = new String[arraySize];
      Vector data = (Vector) toShowSales.get("Data");
      String[][] totals = (String[][]) toShowSales.get("Totals");

      File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/" + fileName);
      FileWriter ft = new FileWriter(fileHtml);
      // ft.write(getHeaders("General Reports Printer"));
      ft.write(getHeaders(""));
      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td colspan='" + arraySize + "' align='center' style='font-size: 20pt '>");
      ft.write("<B>Best Value Auto Body Supply Inc.</B><BR/>");
      ft.write("</td>");
      ft.write("<tr>");
      ft.write("<td colspan='" + arraySize + "' align='center' style='font-size: 16pt '>");
      ft.write("<B>" + mainHeading + "</B>");
      ft.write("<BR/>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/><BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr style='border: thin;'>");
      Enumeration enumSubHead = subHeadings.elements();
      int cnt = 0;
      while (enumSubHead.hasMoreElements()) {
        String subH = (String) enumSubHead.nextElement();
        strSubHeads[cnt] = subH;
        cnt++;
        ft.write("<td><B>");
        ft.write(padSpaces(subH, 10));
        ft.write("</B></td>");
      }
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='" + arraySize + "'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Enumeration enumData = data.elements();
      while (enumData.hasMoreElements()) {
        Hashtable innerData = (Hashtable) enumData.nextElement();

        ft.write("<tr>");

        for (int i = 0; i < strSubHeads.length; i++) {
          ft.write("<td>");
          ft.write(padSpaces((String) innerData.get(strSubHeads[i]), 10));
          ft.write("</td>");
        }

        ft.write("</tr>");
      }

      ft.write("<tr>");
      ft.write("<td colspan='" + arraySize + "'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='right' colspan='" + arraySize + "'>");
      ft.write("<table>");

      for (int i = 0; i < totals.length; i++) {
        ft.write("<tr>");
        ft.write("<TD><B>" + totals[i][0] + "</B></TD>");
        ft.write("<TD><B>" + totals[i][1] + "</B></TD>");
        ft.write("</tr>");
      }

      ft.write("</table>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='" + arraySize + "'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");
      ft.write(getFooters());
      ft.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static String getHeaders() {
    StringBuffer headers = new StringBuffer("");
    headers
        .append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
    headers
        .append("<HTML><HEAD><meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
    // headers.append("<STYLE TYPE='text/css'> <!-- p { font-size: 13pt; } --> </STYLE> ");
    headers.append("<TITLE>Print Invoice</TITLE></HEAD>");
    headers
        .append("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
    headers.append("<BODY onload='PrintPage()'>");
    return headers.toString();
  }

  public static String getHeaders(String title) {
    StringBuffer headers = new StringBuffer("");
    headers
        .append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
    headers
        .append("<HTML><HEAD><meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
    // headers.append("<STYLE TYPE='text/css'> <!-- p { font-size: 13pt; } --> </STYLE> ");
    headers.append("<TITLE>" + title + "</TITLE></HEAD>");
    headers
        .append("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
    headers.append("<BODY onload='PrintPage()'>");
    return headers.toString();
  }

  public static String getFooters() {
    StringBuffer footers = new StringBuffer("");
    footers.append("</BODY></HTML>");

    return footers.toString();
  }

  public static String padSpaces(String str, int len) {
    if (str == null || str.trim().equals("null"))
      str = "";
    str = str.trim();
    int len1 = str.length();
    while (len1 < len) {
      str += "&nbsp;";
      len1++;
    }
    str += "&nbsp;";
    return str;
  }

  public static String newLines(String str) {
    int len = 0;
    if (str != null && (len = str.indexOf("\n")) != -1) {
      str = str.substring(0, len) + "<BR/>" + newLines(str.substring(len + 1));
    }
    return str;
  }

  public static String cutModel(String model) {
    int len = 0;
    if (model.length() > 15) {
      model = model.substring(0, 15);
    }
    if ((len = model.indexOf("(")) != -1) {
      model = model.substring(0, len);
    }
    return model;
  }

  public static int calLen(String str, int totSize) {
    int len = str.length();
    totSize = totSize - (len * 2);
    for (int i = 0; i < 6; i++) {
      if (str.indexOf(" ") != -1) {
        totSize = totSize - 1;
        str = str.substring(str.indexOf(" ") + 1);
      }
    }
    return totSize;
  }

  public static void getVendorHeader(FileWriter ft, int supId, boolean woPrice) throws IOException {
    ft.write("<table border='1' cellspacing='0' style='font-size: 10pt'>");
    ft.write("<tr>");
    ft.write("<td align='center'><B>");
    ft.write("BV Part No");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("ITEM NO");
    ft.write("</B></td>");
    if (supId != 1 && supId != 4 && supId != 5 && supId != 6 && supId != 8) {
      ft.write("<td align='center'><B>");
      ft.write("Desc 1");
      ft.write("</B></td>");
    }
    ft.write("<td align='center'><B>");
    ft.write("Desc 2");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("P/L Number");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("OEM Number");
    ft.write("</B></td>");
    if (woPrice) {
      ft.write("<td align='center'><B>");
      ft.write("NR");
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write("CUFT");
      ft.write("</B></td>");
    }
    ft.write("<td align='center'><B>");
    ft.write("Order");
    ft.write("</B></td>");
    ft.write("</tr>");
  }
  public static List populateDrivers() {
      
      List<Driver> drvrList = new ArrayList<Driver>();
      
      Connection con = null;
      Statement stmtDrvr = null;
      ResultSet rsDrvr = null;

      try {
          con = DBInterfaceLocal.getSQLConnection();
          stmtDrvr = con.createStatement();
          rsDrvr = stmtDrvr.executeQuery("Select serial,drivername From driver Where active=1");
          while (rsDrvr.next()) {
              Driver drvr = new Driver();
              drvr.setSerial(rsDrvr.getString("serial"));
              drvr.setDriverName(rsDrvr.getString("drivername"));
              drvrList.add(drvr);
          }
           stmtDrvr.close();
          rsDrvr.close();
          con.close();
      } catch (SQLException e) {
          logger.error(e);
      } finally{
         
      }
      return drvrList;
  }
}
