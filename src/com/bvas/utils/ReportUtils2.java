package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ReportUtils2 {
  private static final Logger logger = Logger.getLogger(ReportUtils.class);

  public static Hashtable showAllCategories(int catCode) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowCat" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql1 = "SELECT CategoryCode, CategoryName FROM Category";
      String sql2 =
          "SELECT SubCategoryCode, SubCategoryName FROM SubCategory Where CategoryCode='" + catCode
              + "'";
      String sql3 = "SELECT SubCategoryCode, SubCategoryName FROM SubCategory ";

      ResultSet rs = null;
      if (catCode == 999) {
        rs = stmt.executeQuery(sql3);
        mainHeading = "SUB CATEGORIES";
      } else if (catCode == 0) {
        rs = stmt.executeQuery(sql1);
        mainHeading = "CATEGORIES";
      } else {
        rs = stmt.executeQuery(sql2);
        mainHeading = "CATEGORIES FOR " + catCode;
      }

      int totCnt = 0;

      subHeadings.addElement("Code");
      subHeadings.addElement("Description");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        totCnt++;
        String code = rs.getString(1);
        String name = rs.getString(2);

        Hashtable totData = new Hashtable();
        totData.put("Code", code);
        totData.put("Description", name);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      totals[0][0] = "Total No Of Categories";
      totals[0][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      if (catCode == 999) {
        toShowInvoices.put("BackScreen", "SubCategory");
      } else {
        toShowInvoices.put("BackScreen", "Category");
      }
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

  public static Hashtable showPartsForCat(int catCode, boolean inventory) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowPartsCat" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "Select PartNo, ManufacturerName, MakeModelName, Year, PartDescription, UnitsInStock, Location From Parts a, MakeModel b, Manufacturer c Where a.SubCategory='"
              + catCode
              + "' and a.MakeModelCode=b.MakeModelCode and b.ManufacturerId=c.ManufacturerId Order By 2, 3, 5 ";

      ResultSet rs = null;
      rs = stmt.executeQuery(sql);
      mainHeading = "CATEGORIES";

      int totCnt = 0;

      subHeadings.addElement("Make");
      subHeadings.addElement("Model");
      subHeadings.addElement("Year");
      subHeadings.addElement("Description");
      subHeadings.addElement("Part No");
      if (inventory) {
        subHeadings.addElement("Stock");
      }
      subHeadings.addElement("Correct");
      subHeadings.addElement("Location");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String make = rs.getString("ManufacturerName");
        String model = rs.getString("MakeModelName");
        String year = rs.getString("Year");
        String desc = rs.getString("PartDescription");
        String partNo = rs.getString("PartNo");
        String stock = rs.getString("UnitsInStock");
        String location = rs.getString("Location");

        Hashtable totData = new Hashtable();
        totData.put("Make", make);
        totData.put("Model", model);
        totData.put("Year", year);
        totData.put("Description", desc);
        totData.put("Part No", partNo);
        int stck = 0;
        try {
          stck = Integer.parseInt(stock);
        } catch (Exception e) {
          stck = 0;
        }
        if (inventory) {
          if (stck != 0) {
            totData.put("Stock", stck + "");
          } else {
            continue;
          }
        } else {
          if (stck == 0) {
            totData.put("Stock", stck + "");
          } else {
            continue;
          }
        }

        if (location.trim().equals("")) {
          location = "&nbsp;";
        }
        totData.put("Correct", "&nbsp;");
        totData.put("Location", location);

        totCnt++;
        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      totals[0][0] = "Total No Of Parts";
      totals[0][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "SubCategory");
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

  public static Hashtable showDamagedParts(String partNo, boolean isHigh) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowDamagedParts" + partNo + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "Select a.PartNo, a.PartOrder, a.DateEntered, a.CostPrice, a.ActualPrice, a.SuggestedDiscount, a.DamagedDesc, b.PartDescription, b.Year, c.MakeModelName from DamagedParts a, Parts b, MakeModel c ";
      if (partNo != null && !partNo.trim().equals("")) {
        sql +=
            " Where a.PartNo='" + partNo
                + "' and a.PartNo=b.PartNo and b.MakeModelCode=c.MakeModelCode ";
      } else {
        sql += " Where a.PartNo=b.PartNo and b.MakeModelCode=c.MakeModelCode ";
      }

      sql += " Order By PartNo, PartOrder ";

      ResultSet rs = null;
      rs = stmt.executeQuery(sql);
      mainHeading = "PARTS DAMAGED";

      int totCnt = 0;

      subHeadings.addElement("Part No");
      subHeadings.addElement("Order");
      subHeadings.addElement("Description");
      subHeadings.addElement("Date");
      subHeadings.addElement("Cost");
      if (isHigh) {
        subHeadings.addElement("Actual");
      }
      subHeadings.addElement("Discount");
      subHeadings.addElement("Damaged Desc");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String pNo = rs.getString("PartNo");
        String partOrder = rs.getString("PartOrder");
        String dd = rs.getString("DateEntered");
        String cst = rs.getString("CostPrice");
        String act = "";
        if (isHigh) {
          act = rs.getString("ActualPrice");
        }
        String sugg = rs.getString("SuggestedDiscount");
        String damDesc = rs.getString("DamagedDesc");
        String desc =
            rs.getString("MakeModelName") + "  " + rs.getString("Year") + "  "
                + rs.getString("PartDescription");

        Hashtable totData = new Hashtable();
        totData.put("Part No", pNo);
        totData.put("Order", partOrder);
        totData.put("Description", desc);
        totData.put("Date", dd);
        totData.put("Cost", cst);
        if (isHigh) {
          totData.put("Actual", act);
        }
        totData.put("Discount", sugg);
        totData.put("Damaged Desc", damDesc);

        totCnt++;
        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      totals[0][0] = "Total No Of Parts";
      totals[0][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "DamagedParts");
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

  public static Hashtable showMissingParts(String partNo, boolean isHigh) throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowMissingParts" + partNo + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "Select a.PartNo, a.PartOrder, a.DateEntered, a.CostPrice, a.ActualPrice, a.Quantity, b.PartDescription, b.Year, c.MakeModelName from MissingParts a, Parts b, MakeModel c ";
      if (partNo != null && !partNo.trim().equals("")) {
        sql +=
            " Where a.PartNo='" + partNo
                + "' and a.PartNo=b.PartNo and b.MakeModelCode=c.MakeModelCode ";
      } else {
        sql += " Where a.PartNo=b.PartNo and b.MakeModelCode=c.MakeModelCode ";
      }

      sql += " Order By PartNo, PartOrder ";

      ResultSet rs = null;
      rs = stmt.executeQuery(sql);
      mainHeading = "PARTS MISSING";

      int totCnt = 0;

      subHeadings.addElement("Part No");
      subHeadings.addElement("Order");
      subHeadings.addElement("Description");
      subHeadings.addElement("Date");
      subHeadings.addElement("Cost");
      if (isHigh) {
        subHeadings.addElement("Actual");
      }
      subHeadings.addElement("Quantity");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String pNo = rs.getString("PartNo");
        String partOrder = rs.getString("PartOrder");
        String dd = rs.getString("DateEntered");
        String cst = rs.getString("CostPrice");
        String act = "";
        if (isHigh) {
          act = rs.getString("ActualPrice");
        }
        String qty = rs.getString("Quantity");
        String desc =
            rs.getString("MakeModelName") + "  " + rs.getString("Year") + "  "
                + rs.getString("PartDescription");

        Hashtable totData = new Hashtable();
        totData.put("Part No", pNo);
        totData.put("Order", partOrder);
        totData.put("Description", desc);
        totData.put("Date", dd);
        totData.put("Cost", cst);
        if (isHigh) {
          totData.put("Actual", act);
        }
        totData.put("Quantity", qty);

        totCnt++;
        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Inovices ");
      }

      totals[0][0] = "Total No Of Parts";
      totals[0][1] = totCnt + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "MissingParts");
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

  public static Hashtable showPendingBouncedChecks() throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowBC" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[2][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "Select a.CheckId, b.CompanyName, a.Balance From BouncedChecks a, Customer b Where a.Balance!=0 and a.customerId=b.customerId ";

      ResultSet rs = null;
      rs = stmt.executeQuery(sql);
      mainHeading = "PENDING BOUNCED CHECKS AS ON " + DateUtils.getNewUSDate();

      int totCnt = 0;

      subHeadings.addElement("Check Id");
      subHeadings.addElement("Customer");
      subHeadings.addElement("Balance");

      double totBalance = 0.0;

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String checkId = rs.getString(1);
        String cust = rs.getString(2);
        double bal = rs.getDouble(3);

        Hashtable totData = new Hashtable();
        totData.put("Check Id", checkId);
        totData.put("Customer", cust);
        totData.put("Balance", bal + "");

        totCnt++;
        totBalance += bal;
        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Checks ");
      }

      String totBalanceStr = totBalance + "";
      if (totBalanceStr.indexOf(".") == totBalanceStr.length() - 2) {
        totBalanceStr += "0";
      }
      totBalanceStr = NumberUtils.cutFractions(totBalanceStr);

      totals[0][0] = "Total No Of Checks";
      totals[0][1] = totCnt + "";
      totals[1][0] = "Total Balance Amount";
      totals[1][1] = totBalanceStr + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "BouncedChecks");
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

  public static Hashtable getTodaysChecks() throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "ShowCHK" + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector data = new Vector();
      String[][] totals = new String[4][2];

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "Select PaymentType, sum(AppliedAmount) From AppliedAmounts Where AppliedDate='"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "' Group By 1 Order by 1 ";
      // String sql =
      // "Select PaymentType, sum(AppliedAmount) From AppliedAmounts Where AppliedDate='"
      // + "2004-08-31" + "' Group By 1 Order by 1 ";

      ResultSet rs = null;
      rs = stmt.executeQuery(sql);
      mainHeading = "TODAYS CHECKS ";

      int totCnt = 0;

      subHeadings.addElement("Check #");
      subHeadings.addElement("Amount");

      double totAmount = 0.0;
      double totCHKAmount = 0.0;
      double totCCAmount = 0.0;

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String checkNo = rs.getString(1);
        double amt = rs.getDouble(2);

        if (checkNo == null) {
          checkNo = "CASH";
        } else if (checkNo.trim().equals("")) {
          checkNo = "CASH";
        } else if (checkNo.trim().indexOf("ap") == -1 && checkNo.trim().indexOf("AP") == -1
            && checkNo.trim().indexOf("apr") == -1 && checkNo.trim().indexOf("APR") == -1
            && checkNo.trim().indexOf("cr") == -1 && checkNo.trim().indexOf("CR") == -1
            && checkNo.trim().indexOf("adj") == -1 && checkNo.trim().indexOf("ADJ") == -1
            && checkNo.trim().indexOf("to") == -1 && checkNo.trim().indexOf("TO") == -1) {
          totCHKAmount += amt;
        } else if (checkNo.trim().startsWith("APR")) {
          totCCAmount += amt;
        }

        Hashtable totData = new Hashtable();
        totData.put("Check #", checkNo);
        totData.put("Amount", amt + "");

        totCnt++;
        totAmount += amt;
        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No More Pending Checks ");
      }

      String totAmountStr = totAmount + "";
      if (totAmountStr.indexOf(".") == totAmountStr.length() - 2) {
        totAmountStr += "0";
      }
      totAmountStr = NumberUtils.cutFractions(totAmountStr);

      String totCHKAmountStr = totCHKAmount + "";
      if (totCHKAmountStr.indexOf(".") == totCHKAmountStr.length() - 2) {
        totCHKAmountStr += "0";
      }
      totCHKAmountStr = NumberUtils.cutFractions(totCHKAmountStr);

      String totCCAmountStr = totCCAmount + "";
      if (totCCAmountStr.indexOf(".") == totCCAmountStr.length() - 2) {
        totCCAmountStr += "0";
      }
      totCCAmountStr = NumberUtils.cutFractions(totCCAmountStr);

      totals[0][0] = "No Of Checks";
      totals[0][1] = totCnt + "";
      totals[1][0] = "Total Check Amount";
      totals[1][1] = totCHKAmountStr + "";
      totals[2][0] = "Total Credit Cards";
      totals[2][1] = totCCAmountStr + "";
      totals[3][0] = "Total Amount";
      totals[3][1] = totAmountStr + "";

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "EnterAmounts");
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

  public static Hashtable getWriteOffInvoices(String writeOffFromDate, String writeOffToDate)
      throws UserException {
    Hashtable toShowInvoices = null;
    try {

      String fileName = "WriteOff" + writeOffFromDate.trim() + writeOffToDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector data = new Vector();
      String[][] totals = new String[2][2];

      boolean getReturns = false;

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql =
          "Select a.InvoiceNo, a.WriteOffDate, a.Notes, b.OrderDate, b.Balance, c.CompanyName ";
      sql += " From WriteOff a, Invoice b, Customer c ";
      if (writeOffFromDate.trim().equals("") && writeOffToDate.trim().equals("")) {
        sql += " Where a.InvoiceNo=b.InvoiceNumber and b.CustomerId=c.CustomerId Order By 1 ";
      } else {
        sql +=
            " Where a.WriteOffDate >='" + DateUtils.convertUSToMySQLFormat(writeOffFromDate)
                + "' And a.WriteOffDate <= '" + DateUtils.convertUSToMySQLFormat(writeOffToDate)
                + "' ";
        sql += " And a.InvoiceNo=b.InvoiceNumber and b.CustomerId=c.CustomerId Order By 1 ";
      }

      ResultSet rs = stmt.executeQuery(sql);

      double writeOffTotal = 0.0;
      int cnt = 0;

      mainHeading =
          "Write Off Invoices From " + writeOffFromDate.trim() + " To " + writeOffToDate.trim();

      subHeadings.addElement("Inv. No.");
      subHeadings.addElement("Order Date");
      subHeadings.addElement("Cust Name");
      subHeadings.addElement("Balance");
      subHeadings.addElement("Write Off On");
      subHeadings.addElement("Notes");

      while (rs.next()) {
        if (toShowInvoices == null) {
          toShowInvoices = new Hashtable();
        }

        String invoiceNo = rs.getString(1);
        String writeOffDate = rs.getString(2);
        String notes = rs.getString(3);
        String orderDate = rs.getString(4);
        double balance = rs.getDouble(5);
        String custName = rs.getString(6);

        cnt++;
        writeOffTotal += balance;

        Hashtable totData = new Hashtable();
        totData.put("Inv. No.", invoiceNo);
        totData.put("Order Date", orderDate);
        totData.put("Cust Name", custName);
        totData.put("Balance", balance + "");
        totData.put("Write Off On", writeOffDate);
        totData.put("Notes", notes);

        data.addElement(totData);

      }

      if (toShowInvoices == null) {
        throw new UserException(" No Sales For This Period ");
      }

      String writeOffTotalStr = writeOffTotal + "";
      if (writeOffTotalStr.indexOf(".") == writeOffTotalStr.length() - 2) {
        writeOffTotalStr += "0";
      }
      writeOffTotalStr = NumberUtils.cutFractions(writeOffTotalStr);

      totals[0][0] = "No. Of Invoices";
      totals[0][1] = cnt + "";
      totals[1][0] = "Total Amount";
      totals[1][1] = writeOffTotalStr;

      toShowInvoices.put("FileName", fileName);
      toShowInvoices.put("BackScreen", "WriteOff");
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

      ft.write("<table");
      if (fileName.startsWith("ShowPartsCat")) {
        ft.write(" border='1'");
      }
      ft.write(">");
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

}
