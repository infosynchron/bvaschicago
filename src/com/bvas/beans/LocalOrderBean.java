package com.bvas.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.NumberUtils;
import com.bvas.utils.ReportUtils;
import com.bvas.utils.UserException;

public class LocalOrderBean {
  private static final Logger logger = Logger.getLogger(LocalOrderBean.class);

  private int invoiceNo;

  private String dateEntered = "";

  private int supplierId;

  private String partNo = "";

  private String localVendorNo = "";

  private int quantity;

  private double price;

  private String vendInvNo = "";

  private String vendInvDate = "";

  public int getInvoiceNo() {
    return (this.invoiceNo);
  }

  public String getDateEntered() {
    return (this.dateEntered);
  }

  public int getSupplierId() {
    return (this.supplierId);
  }

  public String getPartNo() {
    return (this.partNo);
  }

  public String getLocalVendorNo() {
    return (this.localVendorNo);
  }

  public int getQuantity() {
    return (this.quantity);
  }

  public double getPrice() {
    return (this.price);
  }

  public String getVendInvNo() {
    return (this.vendInvNo);
  }

  public String getVendInvDate() {
    return (this.vendInvDate);
  }

  public void setInvoiceNo(int invoiceNo) {
    this.invoiceNo = invoiceNo;
  }

  public void setDateEntered(String dateEntered) {
    this.dateEntered = dateEntered;
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setLocalVendorNo(String localVendorNo) {
    this.localVendorNo = localVendorNo;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setVendInvNo(String vendInvNo) {
    this.vendInvNo = vendInvNo;
  }

  public void setVendInvDate(String vendInvDate) {
    this.vendInvDate = vendInvDate;
  }

  public static LocalOrderBean getLocalOrder(int supplierId, int invoiceNo, String partNo,
      String vendorInvNo) throws UserException {
    LocalOrderBean loBean = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement pstmt = null;
    ResultSet rs = null;
    try {
      if (partNo == null)
        partNo = "";
      if (vendorInvNo == null)
        vendorInvNo = "";

      String sql = "SELECT * FROM LocalOrders where ";
      // sql += " SupplierId=" + supplierId;
      // sql += " and (";
      sql += " (";
      if (invoiceNo != 0 && !partNo.trim().equals("") && !vendorInvNo.trim().equals("")) {
        sql +=
            " VendorInvNo='" + vendorInvNo.trim() + "' AND PartNo='" + partNo.trim()
                + "' AND InvoiceNo=" + invoiceNo;
      } else if (invoiceNo != 0 && !partNo.trim().equals("")) {
        sql += " PartNo='" + partNo.trim() + "' AND InvoiceNo=" + invoiceNo;
      } else if (invoiceNo != 0 && !vendorInvNo.trim().equals("")) {
        sql += " VendorInvNo='" + vendorInvNo.trim() + "' AND InvoiceNo=" + invoiceNo;
      } else if (!partNo.trim().equals("") && !vendorInvNo.trim().equals("")) {
        sql += " VendorInvNo='" + vendorInvNo.trim() + "' AND PartNo='" + partNo.trim() + "' ";
      } else if (invoiceNo != 0) {
        sql += " InvoiceNo=" + invoiceNo;
      } else if (!partNo.trim().equals("")) {
        sql += " PartNo='" + partNo.trim() + "' ";
      } else if (!vendorInvNo.trim().equals("")) {
        sql += " VendorInvNo='" + vendorInvNo.trim() + "'";
      }

      sql += ")";
      pstmt = con.createStatement();
      System.err.println("Order--"+sql);
      rs = pstmt.executeQuery(sql);
      if (rs.next()) {
        loBean = new LocalOrderBean();
        loBean.setInvoiceNo(rs.getInt("InvoiceNo"));
        loBean.setDateEntered(DateUtils.convertMySQLToUSFormat(rs.getString("DateEntered")));
        loBean.setSupplierId(rs.getInt("SupplierId"));
        loBean.setPartNo(rs.getString("PartNo"));
        loBean.setLocalVendorNo(rs.getString("VendorPartNo"));
        loBean.setQuantity(rs.getInt("Quantity"));
        loBean.setPrice(rs.getDouble("Price"));
        loBean.setVendInvNo(rs.getString("VendorInvNo"));
        loBean.setVendInvDate(DateUtils.convertMySQLToUSFormat(rs.getString("VendorInvDate")));

      } else {
        loBean = null;
      }
      rs.close();
      pstmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("Exception In LocalOrderBean: " + e);
      loBean = null;
    } catch (Exception e) {
      logger.error("Exception In LocalOrderBean: " + e);
      loBean = null;
    }finally {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
    return loBean;
  }

  public static LocalOrderBean getLocalOrder(int supplierId, int invoiceNo, String partNo,
      String vendorInvNo, Connection con1) throws UserException {
    LocalOrderBean loBean = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement pstmt = null;
    ResultSet rs = null;
    try {
      if (partNo == null)
        partNo = "";
      if (vendorInvNo == null)
        vendorInvNo = "";

      String sql = "SELECT * FROM LocalOrders where ";
      // sql += " SupplierId=" + supplierId;
      // sql += " and (";
      sql += " (";
      if (invoiceNo != 0 && !partNo.trim().equals("") && !vendorInvNo.trim().equals("")) {
        sql +=
            " VendorInvNo='" + vendorInvNo.trim() + "' AND PartNo='" + partNo.trim()
                + "' AND InvoiceNo=" + invoiceNo;
      } else if (invoiceNo != 0 && !partNo.trim().equals("")) {
        sql += " PartNo='" + partNo.trim() + "' AND InvoiceNo=" + invoiceNo;
      } else if (invoiceNo != 0 && !vendorInvNo.trim().equals("")) {
        sql += " VendorInvNo='" + vendorInvNo.trim() + "' AND InvoiceNo=" + invoiceNo;
      } else if (!partNo.trim().equals("") && !vendorInvNo.trim().equals("")) {
        sql += " VendorInvNo='" + vendorInvNo.trim() + "' AND PartNo='" + partNo.trim() + "' ";
      } else if (invoiceNo != 0) {
        sql += " InvoiceNo=" + invoiceNo;
      } else if (!partNo.trim().equals("")) {
        sql += " PartNo='" + partNo.trim() + "' ";
      } else if (!vendorInvNo.trim().equals("")) {
        sql += " VendorInvNo='" + vendorInvNo.trim() + "'";
      }

      sql += ")";
      pstmt = con.createStatement();
      rs = pstmt.executeQuery(sql);
      if (rs.next()) {
        loBean = new LocalOrderBean();
        loBean.setInvoiceNo(rs.getInt("InvoiceNo"));
        loBean.setDateEntered(DateUtils.convertMySQLToUSFormat(rs.getString("DateEntered")));
        loBean.setSupplierId(rs.getInt("SupplierId"));
        loBean.setPartNo(rs.getString("PartNo"));
        loBean.setLocalVendorNo(rs.getString("VendorPartNo"));
        loBean.setQuantity(rs.getInt("Quantity"));
        loBean.setPrice(rs.getDouble("Price"));
        loBean.setVendInvNo(rs.getString("VendorInvNo"));
        loBean.setVendInvDate(DateUtils.convertMySQLToUSFormat(rs.getString("VendorInvDate")));
        rs.close();
        pstmt.close();
        // 10/12/2013 : Changes done as a part of review : Begin
        con.close();
        // 10/12/2013 : Changes done as a part of review : End
      } else {
        loBean = null;
      }
      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error("Exception In LocalOrderBean: " + e);
      loBean = null;
    } catch (Exception e) {
      logger.error("Exception In LocalOrderBean: " + e);
      loBean = null;
    }
    return loBean;
  }

  public void createLocalOrder() throws UserException {
    Connection con1 = DBInterfaceLocal.getSQLConnection();
    try {
      con1.setAutoCommit(false);

      // if (!InvoiceBean.isAvailable(getInvoiceNo())) {
      // throw new UserException ("This Invoice No is Not Valid");
      // }

      PartsBean part = PartsBean.getPart(getPartNo().trim(), con1);
      if (part == null) {
        throw new UserException("This Part Not Available in the Inventory");
      }

      String sql =
          "SELECT * FROM InvoiceDetails WHERE InvoiceNumber=" + getInvoiceNo()
              + " AND PartNumber='" + getPartNo().trim() + "'";
      Statement stmt = con1.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      int invQty = 0;
      boolean partAvail = false;
      if (rs.next()) {
        partAvail = true;
        invQty = rs.getInt("Quantity");
        invQty = getQuantity() - invQty;
        if (invQty > 0) {

          double actP = 0;
          if (part.getUnitsInStock() > 0 && part.getActualPrice() > 0
              && part.getUnitsInStock() + invQty > 0) {
            actP =
                ((part.getUnitsInStock() * part.getActualPrice()) + (invQty * getPrice()))
                    / (part.getUnitsInStock() + invQty);
          } else {
            actP = getPrice();
          }
          part.setActualPrice(actP);
        }
      } else {
        double actP = 0;
        invQty = getQuantity();
        if (part.getUnitsInStock() > 0 && part.getActualPrice() > 0
            && part.getUnitsInStock() + invQty > 0) {
          actP =
              ((part.getUnitsInStock() * part.getActualPrice()) + (invQty * getPrice()))
                  / (part.getUnitsInStock() + invQty);
        } else {
          actP = getPrice();
        }
        part.setActualPrice(actP);
      }

      part.setUnitsInStock(part.getUnitsInStock() + getQuantity());

      if (getSupplierId() == 1
          && (part.getKeystoneNumber() == null || part.getKeystoneNumber().trim().equals(""))) {
        part.setKeystoneNumber(getLocalVendorNo().trim());
      }

      String sql1 =
          "INSERT INTO LocalOrders (InvoiceNo, DateEntered, SupplierId, PartNo, VendorPartNo, Quantity, Price, VendorInvNo, VendorInvDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement pstmt1 = con1.prepareStatement(sql1);
      pstmt1.clearParameters();
      pstmt1.setInt(1, getInvoiceNo());
      pstmt1.setString(2, DateUtils.convertUSToMySQLFormat(getDateEntered().trim()));
      pstmt1.setInt(3, getSupplierId());
      pstmt1.setString(4, getPartNo().trim());
      pstmt1.setString(5, getLocalVendorNo().trim());
      pstmt1.setInt(6, getQuantity());
      pstmt1.setDouble(7, getPrice());
      pstmt1.setString(8, getVendInvNo().trim());
      pstmt1.setString(9, DateUtils.convertUSToMySQLFormat(getVendInvDate().trim()));
      pstmt1.execute();

      part.changePart();

      if (partAvail) {
        PreparedStatement pstmt2 =
            con1.prepareStatement("Update InvoiceDetails Set ActualPrice=" + getPrice()
                + " Where InvoiceNumber=" + getInvoiceNo() + " and PartNumber='"
                + getPartNo().trim() + "'");
        pstmt2.execute();
      }

      con1.setAutoCommit(true);
      rs.close();
      stmt.close();
      con1.close();
    } catch (SQLException e) {
      logger.error(e);
      try {
        con1.rollback();
        con1.close();
      } catch (SQLException f) {
        logger.error(e);
      }
      throw new UserException("Order Not Created" + e.getMessage());
    }

  }

  public void deleteLocalOrder() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    try {
      con.setAutoCommit(false);

      PartsBean part = PartsBean.getPart(getPartNo().trim(), con);
      if (part == null) {
        throw new Exception("This Part Not Available in the Inventory");
      }

      String sql =
          "SELECT * FROM InvoiceDetails WHERE InvoiceNumber=" + getInvoiceNo()
              + " AND PartNumber='" + getPartNo().trim() + "'";
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      int invQty = 0;
      boolean partAvail = false;
      double actP = 0;
      if (rs.next()) {
        partAvail = true;
        invQty = rs.getInt("Quantity");
        invQty = getQuantity() - invQty;
      } else {
        invQty = getQuantity();
      }

      if (part.getUnitsInStock() - invQty != 0) {
        actP =
            (part.getActualPrice() * part.getUnitsInStock() - invQty * getPrice())
                / (part.getUnitsInStock() - invQty);
        part.setActualPrice(actP);
      }
      part.setUnitsInStock(part.getUnitsInStock() - getQuantity());

      if (getSupplierId() == 1
          && (part.getKeystoneNumber() == null || part.getKeystoneNumber().trim().equals(""))) {
        part.setKeystoneNumber(getLocalVendorNo().trim());
      }

      String sql1 =
          "DELETE FROM LocalOrders WHERE SupplierId=? and InvoiceNo=? and VendorInvNo=? and PartNo=? ";
       pstmt1 = con.prepareStatement(sql1);
      pstmt1.clearParameters();
      pstmt1.setInt(1, getSupplierId());
      pstmt1.setInt(2, getInvoiceNo());
      pstmt1.setString(3, getVendInvNo());
      pstmt1.setString(4, getPartNo().trim());
      pstmt1.execute();

      part.changePart();

       pstmt2 =
          con.prepareStatement("Update InvoiceDetails Set ActualPrice=0 Where ActualPrice="
              + getPrice() + " and InvoiceNumber=" + getInvoiceNo() + " and PartNumber='"
              + getPartNo().trim() + "'");
      pstmt2.execute();

      con.setAutoCommit(true);

    } catch (SQLException e) {
      logger.error(e);
      try {
        con.rollback();
      } catch (SQLException f) {
        logger.error(e);
      }
      throw new UserException("Order Not Deleted" + e.getMessage());
    } catch (Exception e) {
      try {
        con.rollback();
      } catch (SQLException f) {
        logger.error(e);
      }
      throw new UserException(e.getMessage());
    }finally {
		if (pstmt1 != null) {
			try {
				pstmt1.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (pstmt2 != null) {
			try {
				pstmt2.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
  }

  public static Hashtable showLocalPurchases(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "LocalPurchases" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[3][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      String sql =
          "SELECT DateEntered, PartNo, InvoiceNo, Quantity, Price, (Quantity * Price) as Amount From LocalOrders WHERE ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " DateEntered='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " DateEntered>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND DateEntered<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sql += " Order By 1, 2 ";

      ResultSet rs = stmt.executeQuery(sql);

      int totQuantity = 0;
      int totItems = 0;
      double totAmount = 0.0;

      mainHeading = "Local Purchases Report For The Period From " + fromDate + " To " + toDate;
      subHeadings.addElement("Date");
      subHeadings.addElement("Part No");
      subHeadings.addElement("Reason");
      subHeadings.addElement("Quantity");
      subHeadings.addElement("Price");
      subHeadings.addElement("Amount");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        String dateEntered = "";
        String partNo = "";
        String reason = "For Stock";
        int invNo = 0;
        int qty = 0;
        double price = 0.0;
        double amount = 0.0;

        dateEntered = DateUtils.convertMySQLToUSFormat(rs.getString("DateEntered"));
        partNo = rs.getString("PartNo");
        invNo = rs.getInt("InvoiceNo");
        qty = rs.getInt("Quantity");
        price = rs.getDouble("Price");
        amount = rs.getDouble("Amount");

        if (invNo != 0) {
          reason = "For " + invNo;
        }
        totItems++;
        totQuantity += qty;
        totAmount += amount;

        Hashtable totData = new Hashtable();
        totData.put("Date", dateEntered);
        totData.put("Part No", partNo);
        totData.put("Reason", reason);
        totData.put("Quantity", qty + "");
        totData.put("Price", price + "");
        totData.put("Amount", amount + "");

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Purchases For This Period ");
      }

      String totAmountStr = totAmount + "";

      if (totAmountStr.indexOf(".") == totAmountStr.length() - 2) {
        totAmountStr += "0";
      }

      totAmountStr = NumberUtils.cutFractions(totAmountStr);

      totals[0][0] = "Total No. Of Items Purchased";
      totals[0][1] = totItems + "";
      totals[1][0] = "Total Quantity Purchased";
      totals[1][1] = totQuantity + "";
      totals[2][0] = "Total Purchase Amount";
      totals[2][1] = totAmountStr;

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      ReportUtils.createReport(toShowSales);
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static Hashtable showLocalPurchasesForVendor(UserBean user, String fromDate,
      String toDate, int vendId) throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "LocalPurchasesFor" + vendId + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[3][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      String sql =
          "SELECT DateEntered, PartNo, InvoiceNo, VendorPartNo, VendorInvNo, VendorInvDate, Quantity, Price, (Quantity * Price) as Amount From LocalOrders WHERE SupplierId="
              + vendId + " and ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " VendorInvDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " VendorInvDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND DateEntered<='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      }
      sql += " Order By 5, 2 ";

      ResultSet rs = stmt.executeQuery(sql);

      int totQuantity = 0;
      int totItems = 0;
      double totAmount = 0.0;

      mainHeading =
          "Purchase Report For " + LocalVendorBean.getLocalVendor(vendId).getCompanyName()
              + " From " + fromDate + " To " + toDate;
      subHeadings.addElement("Date");
      subHeadings.addElement("Part No");
      subHeadings.addElement("Reason");
      subHeadings.addElement("Suppl Inv");
      subHeadings.addElement("Suppl Date");
      subHeadings.addElement("Suppl No");
      subHeadings.addElement("Quantity");
      subHeadings.addElement("Price");
      subHeadings.addElement("Amount");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        String dateEntered = "";
        String partNo = "";
        String reason = "&nbsp;";
        int invNo = 0;
        String vendInvNo = "";
        String vendPartNo = "";
        String vendDate = "";
        int qty = 0;
        double price = 0.0;
        double amount = 0.0;

        dateEntered = DateUtils.convertMySQLToUSFormat(rs.getString("DateEntered"));
        partNo = rs.getString("PartNo");
        invNo = rs.getInt("InvoiceNo");
        vendInvNo = rs.getString("VendorInvNo");
        vendPartNo = rs.getString("VendorPartNo");
        vendDate = DateUtils.convertMySQLToUSFormat(rs.getString("VendorInvDate"));
        qty = rs.getInt("Quantity");
        price = rs.getDouble("Price");
        amount = rs.getDouble("Amount");

        if (invNo != 0) {
          reason = "For " + invNo;
        }
        totItems++;
        totQuantity += qty;
        totAmount += amount;

        Hashtable totData = new Hashtable();
        totData.put("Date", dateEntered);
        totData.put("Part No", partNo);
        totData.put("Reason", reason);
        totData.put("Suppl Inv", vendInvNo);
        totData.put("Suppl Date", vendDate);
        totData.put("Suppl No", vendPartNo);
        totData.put("Quantity", qty + "");
        totData.put("Price", price + "");
        totData.put("Amount", amount + "");

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Purchases For This Period ");
      }

      String totAmountStr = totAmount + "";

      if (totAmountStr.indexOf(".") == totAmountStr.length() - 2) {
        totAmountStr += "0";
      }

      totAmountStr = NumberUtils.cutFractions(totAmountStr);

      totals[0][0] = "Total No. Of Items Purchased";
      totals[0][1] = totItems + "";
      totals[1][0] = "Total Quantity Purchased";
      totals[1][1] = totQuantity + "";
      totals[2][0] = "Total Purchase Amount";
      totals[2][1] = totAmountStr;

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "TodaysOrders");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      ReportUtils.createReport(toShowSales);
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

}
