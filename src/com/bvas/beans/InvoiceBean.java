package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.NumberUtils;
import com.bvas.utils.UserException;

public class InvoiceBean implements Serializable {
  private static final Logger logger = Logger.getLogger(InvoiceBean.class);

  private int invoiceNumber;

  private String orderDate = null;

  private String customerId = null;

  private String paymentTerms = null;

  private int returnedInvoice;

  private String shipTo = null;

  private String shipAttention = null;

  private String shipVia = null;

  private String billAttention = null;

  private double invoiceTotal;

  private double discount;

  private String discountType = null;

  private double appliedAmount;

  private double newApplied;

  private String dateNewApplied = null;

  private double tax;

  private double balance;

  private String salesPerson = null;

  private String notes = null;

  private String status = null;

  private String history = null;

  private String isPrinted = null;

  private String isDelivered = null;

  private Vector<InvoiceDetailsBean> invoiceDetails = null;

  private AddressBean shipToAddress = null;

  private AddressBean billToAddress = null;

  public int getInvoiceNumber() {
    return (this.invoiceNumber);
  }

  public String getOrderDate() {
    return (this.orderDate);
  }

  public String getCustomerId() {
    return (this.customerId);
  }

  public String getPaymentTerms() {
    return (this.paymentTerms);
  }

  public int getReturnedInvoice() {
    return (this.returnedInvoice);
  }

  public String getShipTo() {
    return (this.shipTo);
  }

  public String getShipAttention() {
    return (this.shipAttention);
  }

  public String getBillAttention() {
    return (this.billAttention);
  }

  public String getShipVia() {
    return (this.shipVia);
  }

  public double getInvoiceTotal() {
    return (this.invoiceTotal);
  }

  public double getDiscount() {
    return (this.discount);
  }

  public String getDiscountType() {
    return (this.discountType);
  }

  public double getAppliedAmount() {
    return (this.appliedAmount);
  }

  public double getNewApplied() {
    return (this.newApplied);
  }

  public String getDateNewApplied() {
    return (this.dateNewApplied);
  }

  public double getTax() {
    return (this.tax);
  }

  public double getBalance() {
    return (this.balance);
  }

  public String getSalesPerson() {
    return (this.salesPerson);
  }

  public String getNotes() {
    return (this.notes);
  }

  public String getStatus() {
    return (this.status);
  }

  public String getHistory() {
    return (this.history);
  }

  public String getIsPrinted() {
    return (this.isPrinted);
  }

  public String getIsDelivered() {
    return (this.isDelivered);
  }

  public Vector<InvoiceDetailsBean> getInvoiceDetails() {
    return (this.invoiceDetails);
  }

  public AddressBean getShipToAddress() {
    return (this.shipToAddress);
  }

  public AddressBean getBillToAddress() {
    return (this.billToAddress);
  }

  public long getInvoiceTime() {
    long invTime = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select InvoiceTime From Invoice Where InvoiceNumber=" + invoiceNumber);
      if (rs.next()) {
        invTime = rs.getLong("InvoiceTime");
      }

      rs.close();
      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e);
    }/*
      * finally { try { if (con != null) { con.close(); } } catch (Exception exxx) { } }
      */
    return invTime;
  }

  public void setInvoiceNumber(int invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public void setReturnedInvoice(int returnedInvoice) {
    this.returnedInvoice = returnedInvoice;
  }

  public void setShipTo(String shipTo) {
    this.shipTo = shipTo;
  }

  public void setShipAttention(String shipAttention) {
    this.shipAttention = shipAttention;
  }

  public void setShipVia(String shipVia) {
    this.shipVia = shipVia;
  }

  public void setBillAttention(String billAttention) {
    this.billAttention = billAttention;
  }

  public void setInvoiceTotal(double invoiceTotal) {
    this.invoiceTotal = invoiceTotal;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public void setAppliedAmount(double appliedAmount) {
    this.appliedAmount = appliedAmount;
  }

  public void setNewApplied(double newApplied) {
    this.newApplied = newApplied;
  }

  public void setDateNewApplied(String dateNewApplied) {
    this.dateNewApplied = dateNewApplied;
  }

  public void setTax(double tax) {
    this.tax = tax;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void setSalesPerson(String salesPerson) {
    this.salesPerson = salesPerson;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setHistory(String history) {
    this.history = history;
  }

  public void setIsPrinted(String isPrinted) {
    this.isPrinted = isPrinted;
  }

  public void setIsDelivered(String isDelivered) {
    this.isDelivered = isDelivered;
  }

  public void setInvoiceDetails(Vector<InvoiceDetailsBean> invoiceDetails) {
    this.invoiceDetails = invoiceDetails;
  }

  public void setShipToAddress(AddressBean shipToAddress) {
    this.shipToAddress = shipToAddress;
  }

  public void setBillToAddress(AddressBean billToAddress) {
    this.billToAddress = billToAddress;
  }

  public void addInvoice() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      con.setAutoCommit(false);
      StringBuffer sql = new StringBuffer("");
      sql.append("INSERT INTO Invoice (InvoiceNumber, OrderDate, CustomerId, ");
      sql.append("ShipTo, ShipAttention, ShipVia, BillAttention, ");
      sql.append("InvoiceTotal, AppliedAmount, NewApplied, DateNewApplied, ");
      sql.append("Balance, Tax, SalesPerson, Discount, ReturnedInvoice, Notes, Status, History, InvoiceTime, IsPrinted, IsDelivered, PaymentTerms, DiscountType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N', 'N', ?, 'N', 'N', ?, ?)");
      PreparedStatement pstmt = con.prepareStatement(sql.toString());

      if (!getCustomerId().trim().equals("1111111111")) {
        checkOtherDiscounts();
      }

      if (getDiscount() > 0 && getTax() > 0) {
        // if (DateUtils.getNewUSDateForInvoice().equals("07-01-2005"))
        // {
        setTax((getInvoiceTotal() - getDiscount()) * .0925);
        // Until 12/31/12 - 9.50%
        // setTax((getInvoiceTotal() - getDiscount()) *.0950);
        // Changed on 01/03/12
        // setTax((getInvoiceTotal() - getDiscount()) *.0975);
        // Changed on 06/30/10
        // setTax((getInvoiceTotal() - getDiscount()) *.1025);
        // Changed on 07/03/08
        // setTax((getInvoiceTotal() - getDiscount()) *.0925);
        // Changed on 04/21/08
        // setTax((getInvoiceTotal() - getDiscount()) *.0900);
        // } else {
        // setTax((getInvoiceTotal() - getDiscount()) *.0875);
        // }
        setTax(NumberUtils.cutFractions(getTax()));
        setBalance(getInvoiceTotal() + getTax() - getDiscount());
      }

      pstmt.clearParameters();
      pstmt.setInt(1, getInvoiceNumber());
      pstmt.setString(2, DateUtils.convertUSToMySQLFormat(getOrderDate()));
      pstmt.setString(3, getCustomerId());
      pstmt.setString(4, getShipTo());
      pstmt.setString(5, getShipAttention());
      pstmt.setString(6, getShipVia());
      pstmt.setString(7, getBillAttention());
      pstmt.setDouble(8, getInvoiceTotal());
      pstmt.setDouble(9, getAppliedAmount());
      pstmt.setDouble(10, getNewApplied());
      pstmt.setString(11, getDateNewApplied());
      pstmt.setDouble(12, getBalance());
      pstmt.setDouble(13, getTax());
      pstmt.setString(14, getSalesPerson());
      pstmt.setDouble(15, getDiscount());
      pstmt.setInt(16, getReturnedInvoice());
      pstmt.setString(17, getNotes());
      pstmt.setLong(18, System.currentTimeMillis());
      pstmt.setString(19, getPaymentTerms());
      pstmt.setString(20, getDiscountType());

      pstmt.execute();

      getShipToAddress().addNewAddress();
      getBillToAddress().addNewAddress();

      InvoiceDetailsBean.addInvoiceDetails(getInvoiceDetails());
      /*
       * if (getAppliedAmount() != 0) { PreparedStatement pstmt1 = con.prepareStatement(
       * "INSERT INTO AppliedAmounts (InvoiceNumber, AppliedAmount, AppliedDate) VALUES (?, ?, ?) "
       * ); pstmt1.clearParameters(); pstmt1.setInt(1, getInvoiceNumber()); pstmt1.setDouble(2,
       * getAppliedAmount()); pstmt1.setString(3, DateUtils.convertUSToMySQLFormat(getOrderDate()));
       * pstmt1.execute(); }
       */
      try {
        con.createStatement().execute("DELETE FROM InvNo where InvNum=" + getInvoiceNumber());
        con.commit();
      } catch (Exception e) {
        logger.info("Exception When Creating Invoice And When Deleting the Inv No : "
            + getInvoiceNumber() + " -- " + e.getMessage());
      }

      try {
        CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
        cust.setCreditBalance(cust.getCreditBalance() + getBalance());
        cust.changeCustomer();
      } catch (Exception e) {
        logger.info("Exception In Create Invoice And When Setting Credit Balance : "
            + getInvoiceNumber() + " -- " + e.getMessage());
      }

      // Statement stmtPP = con.createStatement();
      // stmtPP.executeQuery("Insert Into PartsPending (InvoiceNo, Printed) Values ("
      // + getInvoiceNumber() + ", 'N') ");

      // con.commit();

      pstmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("Exception In Add Invoice(): " + e.getMessage());
      /*
       * try { con.rollback(); } catch (Exception ex) {
       * logger.error("Exception when doing rollback(): " + ex); }
       */
      throw new UserException(
          "Unable to add the invoice<BR/>Looks like other program added an invoice with this number<BR/>Remedy: Clear This Invoice And Start Again <BR/> "
              + e);
    }
  }

  public void changeInvoice() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
      cust.setCreditBalance(cust.getCreditBalance()
          - InvoiceBean.getInvoice(getInvoiceNumber()).getBalance());

      String ordDate = InvoiceBean.getInvoice(getInvoiceNumber()).getOrderDate();
      con.setAutoCommit(false);
      StringBuffer sql = new StringBuffer("");
      sql.append("UPDATE Invoice set OrderDate=?, ");
      sql.append("ShipTo=?, ShipAttention=?, ShipVia=?, BillAttention=?, ");
      sql.append("InvoiceTotal=?, AppliedAmount=?, NewApplied=?, DateNewApplied=?, ");
      sql.append("Balance=?, Tax=?, Discount=?, ReturnedInvoice=?, Notes=?, Status=?, History='Y', IsPrinted='N' ");
      sql.append("WHERE InvoiceNumber=? AND CustomerId=?");

      Statement stmt = con.createStatement();
      ResultSet rsx =
          stmt.executeQuery("SELECT SUM(AppliedAmount) FROM AppliedAmounts where InvoiceNumber='"
              + getInvoiceNumber() + "'");
      double applAmounts = 0.0;
      if (rsx.next()) {
        applAmounts = rsx.getDouble(1);
      }
      if (applAmounts != 0) {
        setAppliedAmount(applAmounts);
        setBalance(getBalance() - getAppliedAmount());
      }

      PreparedStatement pstmt = con.prepareStatement(sql.toString());

      pstmt.clearParameters();
      pstmt.setString(1, DateUtils.convertUSToMySQLFormat(getOrderDate()));
      pstmt.setString(2, getShipTo());
      pstmt.setString(3, getShipAttention());
      pstmt.setString(4, getShipVia());
      pstmt.setString(5, getBillAttention());
      pstmt.setDouble(6, getInvoiceTotal());
      pstmt.setDouble(7, getAppliedAmount());
      pstmt.setDouble(8, getNewApplied());
      pstmt.setString(9, getDateNewApplied());
      pstmt.setDouble(10, getBalance());
      pstmt.setDouble(11, getTax());
      pstmt.setDouble(12, getDiscount());
      pstmt.setInt(13, getReturnedInvoice());
      pstmt.setString(14, getNotes());
      pstmt.setString(15, getStatus());
      pstmt.setInt(16, getInvoiceNumber());
      pstmt.setString(17, getCustomerId());

      pstmt.execute();

      getShipToAddress().changeAddress();
      getBillToAddress().changeAddress();

      Vector<InvoiceDetailsBean> v = InvoiceDetailsBean.getInvoiceDetails(getInvoiceNumber());
      Enumeration<InvoiceDetailsBean> ennum = v.elements();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean iBean = ennum.nextElement();
        if (!iBean.getPartNumber().startsWith("XX")) {
          int qty = PartsBean.getQuantity(iBean.getPartNumber(), con) + iBean.getQuantity();
          PartsBean.changeQuantity(iBean.getPartNumber(), qty);
        }
      }

      InvoiceDetailsBean.deleteInvoiceDetails(getInvoiceNumber());
      InvoiceDetailsBean.addInvoiceDetails(getInvoiceDetails());
      /*
       * if (getAppliedAmount() != 0) { Statement stmtAppl = con.createStatement();
       * stmtAppl.execute("DELETE FROM AppliedAmounts WHERE InvoiceNumber="
       * +getInvoiceNumber()+" AND AppliedDate='"+DateUtils. convertUSToMySQLFormat(ordDate)+"'");
       * PreparedStatement pstmt1 = con.prepareStatement(
       * "INSERT INTO AppliedAmounts (InvoiceNumber, AppliedAmount, AppliedDate) VALUES (?, ?, ?) "
       * ); pstmt1.clearParameters(); pstmt1.setInt(1, getInvoiceNumber()); pstmt1.setDouble(2,
       * getAppliedAmount()); pstmt1.setString(3, DateUtils.convertUSToMySQLFormat(getOrderDate()));
       * pstmt1.execute(); }
       */

      cust.setCreditBalance(cust.getCreditBalance() + getBalance());
      cust.changeCustomer();
      con.commit();
      con.setAutoCommit(true);
    } catch (Exception e) {
      try {
        con.rollback();
      } catch (SQLException ef) {
        logger.error(e);
      }
      throw new UserException(
          "Unable to Change the invoice<BR/>Possible cause include Changing Invoice Number, Customer Id, SalesPerson Name"
              + e);
    }finally {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
  }

  public static InvoiceBean getInvoice(int invNo) throws UserException {
    InvoiceBean invoice = null;
    Connection con = null;
    try {
       con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String getSQL = "";
      getSQL += "select * from Invoice where InvoiceNumber = " + invNo;
      ResultSet rs = stmt.executeQuery(getSQL);
      if (rs.next()) {
        invoice = new InvoiceBean();
        invoice.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        invoice.setOrderDate(DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate")));
        invoice.setCustomerId(rs.getString("CustomerId"));
        invoice.setPaymentTerms(rs.getString("PaymentTerms"));
        invoice.setReturnedInvoice(rs.getInt("ReturnedInvoice"));
        invoice.setShipTo(rs.getString("ShipTo"));
        invoice.setShipAttention(rs.getString("ShipAttention"));
        invoice.setShipVia(rs.getString("ShipVia"));
        invoice.setBillAttention(rs.getString("BillAttention"));
        invoice.setInvoiceTotal(rs.getDouble("InvoiceTotal"));
        invoice.setDiscount(rs.getDouble("Discount"));
        invoice.setDiscountType(rs.getString("DiscountType"));
        invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        invoice.setNewApplied(rs.getDouble("NewApplied"));
        invoice.setDateNewApplied(rs.getString("DateNewApplied"));
        invoice.setTax(rs.getDouble("Tax"));
        invoice.setBalance(rs.getDouble("Balance"));
        invoice.setSalesPerson(rs.getString("SalesPerson"));
        invoice.setNotes(rs.getString("Notes"));
        invoice.setStatus(rs.getString("Status"));
        invoice.setHistory(rs.getString("History"));
        invoice.setIsPrinted(rs.getString("IsPrinted"));
        invoice.setIsDelivered(rs.getString("IsDelivered"));

        // invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invoice.getInvoiceNumber()));
        // invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Ship", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));
        // invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Bill", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));
        invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(), "Ship", "Cust",
            "", invoice.getInvoiceNumber()));
        invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(), "Bill", "Cust",
            "", invoice.getInvoiceNumber()));

        invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invNo, con));

      }

      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoice<BR/>" + e);
    }

    return invoice;
  }

  public static InvoiceBean getInvoice(int invNo, Connection con1) throws UserException {
    InvoiceBean invoice = null;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String getSQL = "";
      getSQL += "select * from Invoice where InvoiceNumber = " + invNo;
      ResultSet rs = stmt.executeQuery(getSQL);
      if (rs.next()) {
        invoice = new InvoiceBean();
        invoice.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        invoice.setOrderDate(DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate")));
        invoice.setCustomerId(rs.getString("CustomerId"));
        invoice.setPaymentTerms(rs.getString("PaymentTerms"));
        invoice.setReturnedInvoice(rs.getInt("ReturnedInvoice"));
        invoice.setShipTo(rs.getString("ShipTo"));
        invoice.setShipAttention(rs.getString("ShipAttention"));
        invoice.setShipVia(rs.getString("ShipVia"));
        invoice.setBillAttention(rs.getString("BillAttention"));
        invoice.setInvoiceTotal(rs.getDouble("InvoiceTotal"));
        invoice.setDiscount(rs.getDouble("Discount"));
        invoice.setDiscountType(rs.getString("DiscountType"));
        invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        invoice.setNewApplied(rs.getDouble("NewApplied"));
        invoice.setDateNewApplied(rs.getString("DateNewApplied"));
        invoice.setTax(rs.getDouble("Tax"));
        invoice.setBalance(rs.getDouble("Balance"));
        invoice.setSalesPerson(rs.getString("SalesPerson"));
        invoice.setNotes(rs.getString("Notes"));
        invoice.setStatus(rs.getString("Status"));
        invoice.setHistory(rs.getString("History"));
        invoice.setIsPrinted(rs.getString("IsPrinted"));
        invoice.setIsDelivered(rs.getString("IsDelivered"));

        // invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invoice.getInvoiceNumber()));
        // invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Ship", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));
        // invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Bill", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));
        invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(), "Ship", "Cust",
            "", invoice.getInvoiceNumber(), con));
        invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(), "Bill", "Cust",
            "", invoice.getInvoiceNumber(), con));

        invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invNo, con));

      }

      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoice<BR/>" + e);
    }

    return invoice;
  }

  public static synchronized int getNewInvoiceNo(String username) throws UserException {
    int newInvNo = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      con.setAutoCommit(false);
      Statement stmt = con.createStatement();
      ResultSet rs1 = stmt.executeQuery("select * from InvNo where Username='" + username + "'");

      // This invoice number is from the invoice table and it is based on
      // that user......
      if (rs1.next()) {
        newInvNo = rs1.getInt("InvNum");
        // Invoice number for that user is not available in the invno
        // table
      } else {
        long newTimeLocked = System.currentTimeMillis();
        ResultSet rs2 =
            stmt.executeQuery("select InvNum, Username, TimeLocked from InvNo where ((TimeLocked + 1800000) < "
                + newTimeLocked + ") order by InvNum ASC ");
        // ResultSet rs2 =
        // stmt.executeQuery("select min(InvNum) from InvNo where " +
        // newTimeLocked + " - TimeLocked > 1800000");
        // Getting the lowest invoice number which is pending for half
        // an hour
        if (rs2.next()) {
          newInvNo = rs2.getInt(1);
        }
        rs2.close();

        // minimum invoice number is available
        if (newInvNo != 0) {
          stmt.execute("update InvNo set Username='" + username + "', TimeLocked=" + newTimeLocked
              + " where InvNum=" + newInvNo);
          // Pending Minimum invoice number is not available and
          // going to get the new invoice number from either invoice
          // table
          // or the InvNo table based on which one is greater
        } else {

          int numFromInvoiceTable = 0;
          int numFromInvNoTable = 0;

          ResultSet rs3 = stmt.executeQuery("select max(invoiceNumber) from Invoice");
          if (rs3.next()) {
            numFromInvoiceTable = rs3.getInt(1);
          }
          rs3.close();

          ResultSet rs4 = stmt.executeQuery("select max(invNum) from InvNo");
          if (rs4.next()) {
            numFromInvNoTable = rs4.getInt(1);
          }
          rs4.close();

          if (numFromInvNoTable > numFromInvoiceTable) {
            newInvNo = numFromInvNoTable;
          } else {
            newInvNo = numFromInvoiceTable;
          }
          newInvNo++;
          logger.error("NEW INVOICE NUMBER FOR THE USER: " + username + " IS " + newInvNo);
          stmt.execute("INSERT INTO InvNo (Username, InvNum, TimeLocked) VALUES ('" + username
              + "', " + newInvNo + ", " + newTimeLocked + ")");

        }
      }
      con.commit();
      con.setAutoCommit(true);
      rs1.close();

      stmt.close();
      con.close();
    } catch (SQLException e) {
      try {
        con.rollback();
      } catch (Exception re) {
        logger.error(re);
      }
      logger.info("In InvoiceBean - Unable to get New Invoice Number - " + e);
    }
    return newInvNo;
  }

  public void checkOtherDiscounts() {
    try {
      if (getDiscount() == 0) {
        boolean isSalePeriod = false;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
        java.util.Date dd1 = sdf.parse("06-28-2007");
        java.util.Date dd2 = sdf.parse("07-01-2007");
        long fromDate = dd1.getTime();
        long toDate = dd2.getTime();
        long currTime = System.currentTimeMillis();
        logger.error(fromDate);
        logger.error(toDate);
        logger.error(currTime);
        double detailedDisc = 0;
        double discOnTotal = 0;

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        ResultSet rst =
            stmt.executeQuery("Select max(OrderDate) from invoice where customerid='"
                + getCustomerId() + "' ");
        String maxDate = "";
        long compareTime = 0L;
        long maxTime = 0L;
        if (rst.next()) {
          String xx = rst.getString(1);
          if (xx == null || xx.trim().equals("")) {
            maxTime = 0;
            compareTime = 0;
          } else {
            maxDate = com.bvas.utils.DateUtils.convertMySQLToUSFormat(xx);
            logger.error("Last Time Ordered On: " + maxDate);
            java.util.Date ddX = null;
            ddX = sdf.parse(maxDate);
            maxTime = ddX.getTime();
            long lt = 7689600000L;
            compareTime = System.currentTimeMillis() - lt;
          }
        }

        if (CustomerBean.getCustomerLevel(getCustomerId()) == 0) {
          if (currTime > fromDate && currTime < toDate) {
            // Gives Month End Sale Discount
            isSalePeriod = true;

            int monthEndDiscount = CustomerBean.getMonthEndDiscount(getCustomerId());

            // ***** THIS DISCOUNT IS ONLY FOR THANKS GIVING SALE
            // int monthEndDiscount = 10;

            double xy = getInvoiceTotal() * monthEndDiscount / 100;
            double xx = checkDetailedDiscount(monthEndDiscount);
            if (xx > 0) {
              discOnTotal = xy;
              detailedDisc = xx;
              setDiscountType("M");
            }
          }
          if (getInvoiceTotal() > 499 && CustomerBean.isFirstTimeBuying(getCustomerId())) {
            // Gives $ 500 Discount And New Customer Discount
            double xy = getInvoiceTotal() * .15;
            double xx = checkDetailedDiscount(15);
            if (xx > 0 && xx > detailedDisc) {
              discOnTotal = xy;
              detailedDisc = xx;
              setDiscountType("X");
            }
          }
          if (CustomerBean.isFirstTimeBuying(getCustomerId())
              && !getDiscountType().trim().equals("X")) {
            // Gives New Customer Discount
            double xy = getInvoiceTotal() * .10;
            double xx = checkDetailedDiscount(10);
            if (xx > 0 && xx > detailedDisc) {
              discOnTotal = xy;
              detailedDisc = xx;
              setDiscountType("N");
            }
          }
          if (maxTime < compareTime && !getDiscountType().trim().equals("X")
              && !getDiscountType().trim().equals("N")) {
            // Gives Discount For Returning Customers
            double xy = getInvoiceTotal() * .10;
            double xx = checkDetailedDiscount(10);
            if (xx > 0 && xx > detailedDisc) {
              discOnTotal = xy;
              detailedDisc = xx;
              setDiscountType("R");
            }
          }
          if (getInvoiceTotal() > 499 && !getDiscountType().trim().equals("R")
              && !getDiscountType().trim().equals("X") && !getDiscountType().trim().equals("N")) {
            // Gives $ 500 Discount
            double xy = getInvoiceTotal() * .05;
            double xx = checkDetailedDiscount(5);
            if (xx > 0 && xx > detailedDisc) {
              discOnTotal = xy;
              detailedDisc = xx;
              setDiscountType("O");
            }
          }

          if (detailedDisc != 0 && discOnTotal != 0) {
            double newDisc = 0;
            if (detailedDisc < discOnTotal) {
              newDisc = detailedDisc;
            } else {
              newDisc = discOnTotal;
            }
            if (newDisc != 0 && getReturnedInvoice() == 0) {
              newDisc = NumberUtils.cutFractions(newDisc);
              setDiscount(newDisc);
              setBalance(getBalance() - newDisc);
            }
          }
        }

        if (CustomerBean.getCustomerLevel(getCustomerId()) <= 8) {
          double xyz = checkPromoDiscount();
          if (xyz > getDiscount()) {
            setDiscount(xyz);
            setBalance(getInvoiceTotal() + getTax() - xyz);
            setDiscountType("P");
          }
        }
        rst.close();
        stmt.close();
        con.close();

      }

    } catch (SQLException e) {
      e.printStackTrace();
      logger.error("SQLException:" + e.toString());
    } catch (ParseException e) {
      e.printStackTrace();
      logger.error("ParseException:" + e.toString());
    }
  }

  public double checkDetailedDiscount(int discPerc) {
    double newDisc = 0.0;

    Vector<InvoiceDetailsBean> v = getInvoiceDetails();
    Enumeration<InvoiceDetailsBean> ennum = v.elements();
    double costPrice = 0.0;
    double actPrice = 0.0;

    while (ennum.hasMoreElements()) {
      InvoiceDetailsBean bean = ennum.nextElement();
      PartsBean part = null;
      if (!bean.getPartNumber().startsWith("XX")) {
        part = PartsBean.getPart(bean.getPartNumber(), null);
        if (part.getUnitsInStock() > 0 && bean.getQuantity() > 0) {
          int inStock = part.getUnitsInStock() - bean.getQuantity();
          if (inStock >= 0) {
            costPrice += bean.getSoldPrice() * bean.getQuantity();
            actPrice += part.getActualPrice() * bean.getQuantity();
          } else {
            costPrice += bean.getSoldPrice() * inStock * -1;
            actPrice += part.getActualPrice() * inStock * -1;
          }
        }
      } else {
        continue;
      }

    }
    double perc = ((costPrice - actPrice) / costPrice) * 100;
    // logger.error("CostPrice:" + costPrice);
    // logger.error("actPrice:" + actPrice);
    if (perc > (discPerc + 10)) {
      newDisc = costPrice * discPerc / 100;
    }

    if (newDisc != 0) {
      newDisc = NumberUtils.cutFractions(newDisc);
    }
    return newDisc;
  }

  public double checkPromoDiscount() {
    double newDisc = 0.0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    Vector<InvoiceDetailsBean> v = getInvoiceDetails();
    Enumeration<InvoiceDetailsBean> ennum = v.elements();
    double totDiscount = 0.0;

    while (ennum.hasMoreElements()) {
      InvoiceDetailsBean bean = ennum.nextElement();
      PartsBean part = null;
      if (!bean.getPartNumber().startsWith("XX")) {
        part = PartsBean.getPart(bean.getPartNumber(), con);
        if (part.getUnitsInStock() > 0 && bean.getQuantity() > 0) {
          int inStock = part.getUnitsInStock() - bean.getQuantity();
          if (inStock >= 0) {
            double costPrice = bean.getSoldPrice() * bean.getQuantity();
            try {
               stmt = con.createStatement();
              ResultSet rs =
                  stmt.executeQuery("Select * from SaleParts Where PartNo='" + bean.getPartNumber()
                      + "'");
              if (rs.next()) {
                double prce = rs.getDouble("SalePrice");
                String frDate = DateUtils.convertMySQLToUSFormat(rs.getString("FromDate"));
                String trDate = DateUtils.convertMySQLToUSFormat(rs.getString("ToDate"));
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
                java.util.Date dd1 = sdf.parse(frDate);
                java.util.Date dd2 = sdf.parse(trDate);
                long fromDate = dd1.getTime();
                long toDate = dd2.getTime() + 86400000;
                long currTime = System.currentTimeMillis();
                if (currTime > fromDate && currTime < toDate) {
                  totDiscount += (costPrice - (prce * bean.getQuantity()));
                }

              } else {
                continue;
              }
            } catch (Exception e) {
              logger.error(e);
              continue;
            } finally {
            	if (stmt != null) {
    				try {
    					stmt.close();
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

          } else {
            continue;
          }
        }
      } else {
        continue;
      }

    }
    return totDiscount;
  }

  public void addPayment(int invNo, double amount, String date, String paymentType, String userName)
      throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    try {

      String status = InvoiceBean.getInvoice(invNo).getStatus();
      if (!status.trim().equals("C") && !status.trim().equals("W")) {
        con.setAutoCommit(false);
         stmt = con.createStatement();
        String sql =
            "INSERT INTO AppliedAmounts (InvoiceNumber, AppliedAmount, AppliedDate, PaymentType, UserName, PaymentTime) VALUES ('"
                + invNo
                + "', "
                + amount
                + ", '"
                + DateUtils.convertUSToMySQLFormat(date)
                + "', '"
                + paymentType
                + "', '"
                + userName
                + "', '"
                + (new java.util.Date(System.currentTimeMillis()).toString()).substring(11, 16)
                + "') ";
        stmt.execute(sql);
        String sql2 =
            "UPDATE Invoice SET AppliedAmount=AppliedAmount+" + amount + ", Balance=Balance-"
                + amount + ", Status='P' WHERE InvoiceNumber=" + invNo;
        Statement stmt2 = con.createStatement();
        stmt2.execute(sql2);
        setAppliedAmount(getAppliedAmount() + amount);

        CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
        cust.setCreditBalance(cust.getCreditBalance() - amount);
        cust.changeCustomer();

        con.commit();
      } else {
        throw new UserException("This Invoice cannot be changed - Status is Closed");
      }

    } catch (SQLException e) {
      try {
        con.rollback();
      } catch (SQLException se) {
        logger.error(e);
      }
      throw new UserException("Payment not added - " + e);
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
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

  public void deletePayment(int invNo, double amount, String date, String paymentType)
      throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt2 = null;
    try {

      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select count(*) from AppliedAmounts Where InvoiceNumber='" + invNo
              + "'");
      int noOfPayments = 0;
      if (rs1.next()) {
        noOfPayments = rs1.getInt(1);
      }
      if (noOfPayments == 0) {
        throw new UserException("No Payments To Delete");
      }

      Statement stmtX = con.createStatement();
      ResultSet rsX =
          stmtX.executeQuery("Select count(*) from AppliedAmounts Where InvoiceNumber='" + invNo
              + "' and AppliedAmount=" + amount + " and AppliedDate='"
              + DateUtils.convertUSToMySQLFormat(date) + "' and PaymentType='" + paymentType + "'");
      int noPayments = 0;
      if (rsX.next()) {
        noPayments = rsX.getInt(1);
      }
      if (noPayments == 2) {
        throw new UserException("Duplicate Payments - Not Delete");
      }

      String status = InvoiceBean.getInvoice(invNo).getStatus();
      if (!status.trim().equals("C") && !status.trim().equals("W")) {
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        String sql = "DELETE FROM AppliedAmounts WHERE ";
        sql += "InvoiceNumber='" + invNo;
        sql += "' and AppliedAmount=" + amount;
        if (date != null && !date.trim().equals("")) {
          sql += " and AppliedDate='" + DateUtils.convertUSToMySQLFormat(date) + "'";
        }
        if (paymentType != null && !paymentType.trim().equals("")) {
          sql += " and PaymentType='" + paymentType + "'";
        }
        stmt.execute(sql);
        String sql2 =
            "UPDATE Invoice SET AppliedAmount=AppliedAmount-" + amount + ", Balance=Balance+"
                + amount + ", Status='P' WHERE InvoiceNumber=" + invNo;
         stmt2 = con.createStatement();
        stmt2.execute(sql2);
        setAppliedAmount(getAppliedAmount() - amount);

        CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
        cust.setCreditBalance(cust.getCreditBalance() + amount);
        cust.changeCustomer();

        con.commit();
      } else {
        throw new UserException("This Invoice cannot be changed - Status is Closed");
      }

    } catch (SQLException e) {
      try {
        con.rollback();
      } catch (SQLException se) {
        logger.error(e);
      }
      throw new UserException("Payment not deleted - " + e);
    }finally {
		if (stmt2 != null) {
			try {
				stmt2.close();
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

  public String[][] getPayments(int invNo) throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt1 = null;
    String[][] payments = null;
    try {

       stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select count(*) from AppliedAmounts Where InvoiceNumber='" + invNo
              + "'");
      int noOfPayments = 0;
      if (rs1.next()) {
        noOfPayments = rs1.getInt(1);
      }
      if (noOfPayments != 0) {
        payments = new String[noOfPayments][6];
        Statement stmt2 = con.createStatement();
        ResultSet rs2 =
            stmt2.executeQuery("Select * from AppliedAmounts Where InvoiceNumber='" + invNo + "'");
        int cnt = 0;
        while (rs2.next()) {
          payments[cnt][0] = rs2.getInt("InvoiceNumber") + "";
          payments[cnt][1] = DateUtils.convertMySQLToUSFormat(rs2.getString("AppliedDate"));
          payments[cnt][2] = rs2.getDouble("AppliedAmount") + "";
          payments[cnt][3] = rs2.getString("PaymentType");
          payments[cnt][4] = rs2.getString("UserName");
          payments[cnt][5] = rs2.getString("PaymentTime");
          cnt++;
        }
      }

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Exception When Finding Payments - " + e);
    }finally {
		if (stmt1 != null) {
			try {
				stmt1.close();
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

    return payments;
  }

  public static boolean isAvailable(int invNo) {
    boolean avail = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("Select * from Invoice where InvoiceNumber=" + invNo);
      if (rs.next()) {
        avail = true;
      }
    } catch (SQLException e) {

      logger.error(e);
    }
    return avail;
  }

  public static boolean isAvailableInInvoice(int invNo, String partNo, int qty)
      throws UserException {
    boolean avail = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select Quantity from InvoiceDetails where InvoiceNumber=" + invNo
              + " and PartNumber='" + partNo.trim() + "'");
      if (rs.next()) {
        avail = true;
        int invQty = rs.getInt(1);
        if (invQty < (qty * -1)) {
          throw new UserException("Quantity Not Matching with the original Invoice");
        }
      } else {
        avail = false;
      }
    } catch (SQLException e) {
      logger.error(e);
    }
    return avail;
  }

  public static boolean isDoubleReturn(int invNo, int retInv, String partNo, int qty)
      throws UserException {
    boolean doubleReturn = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    ResultSet rs1 = null;
    try {

       stmt = con.createStatement();
       rs1 =
          stmt.executeQuery("Select InvoiceNumber From Invoice Where ReturnedInvoice=" + retInv);
      String sql = "Select Sum(Quantity) from InvoiceDetails where (InvoiceNumber=" + retInv;
      while (rs1.next()) {
        sql += " or InvoiceNumber =" + rs1.getInt(1);
      }
      sql += ") and PartNumber='" + partNo.trim() + "'";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        int totQty = rs.getInt(1);
        if ((InvoiceBean.isAvailable(invNo) && totQty < 0)
            || (!InvoiceBean.isAvailable(invNo) && totQty + qty < 0)) {
          doubleReturn = true;
        }
      } else {
        doubleReturn = false;
      }
    } catch (SQLException e) {
      logger.error(e);
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
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
    return doubleReturn;
  }

  public static void changeIsPrinted(int invNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    try {
       stmt = con.createStatement();
      stmt.executeUpdate("Update Invoice Set IsPrinted='Y' where InvoiceNumber=" + invNo);
    } catch (SQLException e) {

      logger.info("In InvoiceBean-When changing the print Flag::::" + invNo + "::::"
          + e.getMessage());
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
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

  public static void changeIsDelivered(int invNo, Connection con) {

    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("Update Invoice Set IsDelivered='Y' where InvoiceNumber=" + invNo);
      stmt.close();

    } catch (SQLException e) {
      logger.error(e);
    }
  }

  public static Vector<InvoiceBean> getPendingInvoices(String customerId) throws UserException {
    Vector<InvoiceBean> pendingInvoices = null;
    Connection con = null;
    Statement stmt = null;
    try {
       con = DBInterfaceLocal.getSQLConnection();
       stmt = con.createStatement();
      String getSQL = "";
      getSQL +=
          "select * from Invoice where Balance != 0 and Status != 'C' and Status != 'W' and CustomerId='"
              + customerId.trim() + "' order by invoicenumber ";
      ResultSet rs = stmt.executeQuery(getSQL);
      while (rs.next()) {
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<InvoiceBean>();
        }
        InvoiceBean invoice = new InvoiceBean();
        invoice.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        invoice.setOrderDate(DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate")));
        invoice.setCustomerId(rs.getString("CustomerId"));
        invoice.setReturnedInvoice(rs.getInt("ReturnedInvoice"));
        invoice.setShipTo(rs.getString("ShipTo"));
        invoice.setShipAttention(rs.getString("ShipAttention"));
        invoice.setShipVia(rs.getString("ShipVia"));
        invoice.setBillAttention(rs.getString("BillAttention"));
        invoice.setInvoiceTotal(rs.getDouble("InvoiceTotal"));
        invoice.setDiscount(rs.getDouble("Discount"));
        invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        invoice.setNewApplied(rs.getDouble("NewApplied"));
        invoice.setDateNewApplied(rs.getString("DateNewApplied"));
        invoice.setTax(rs.getDouble("Tax"));
        invoice.setBalance(rs.getDouble("Balance"));
        invoice.setSalesPerson(rs.getString("SalesPerson"));
        invoice.setNotes(rs.getString("Notes"));
        invoice.setStatus(rs.getString("Status"));
        invoice.setHistory(rs.getString("History"));

        // invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invoice.getInvoiceNumber()));
        invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(), "Ship", "Cust",
            invoice.getOrderDate(), invoice.getInvoiceNumber()));
        invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(), "Bill", "Cust",
            invoice.getOrderDate(), invoice.getInvoiceNumber()));

        invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invoice.getInvoiceNumber()));

        pendingInvoices.add(invoice);

      }
      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoices<BR/>" + e);
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
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

    return pendingInvoices;
  }

  public static Vector<InvoiceBean> getPendingInvoices() throws UserException {
    Vector<InvoiceBean> pendingInvoices = null;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String getSQL = "";
      // getSQL +=
      // "select * from Invoice where Status!='C' and Status!='W' and Status!='N' order by InvoiceNumber ";
      getSQL += "select * from Invoice where Status!='C' and Status!='W' order by InvoiceNumber ";
      ResultSet rs = stmt.executeQuery(getSQL);
      while (rs.next()) {
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<InvoiceBean>();
        }
        InvoiceBean invoice = new InvoiceBean();
        invoice.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        invoice.setOrderDate(DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate")));
        // invoice.setCustomerId(rs.getString("CustomerId"));
        // invoice.setReturnedInvoice(rs.getInt("ReturnedInvoice"));
        // invoice.setShipTo(rs.getString("ShipTo"));
        // invoice.setShipAttention(rs.getString("ShipAttention"));
        // invoice.setShipVia(rs.getString("ShipVia"));
        // invoice.setBillAttention(rs.getString("BillAttention"));
        invoice.setInvoiceTotal(rs.getDouble("InvoiceTotal"));
        invoice.setDiscount(rs.getDouble("Discount"));
        invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        // invoice.setNewApplied(rs.getDouble("NewApplied"));
        // invoice.setDateNewApplied(rs.getString("DateNewApplied"));
        invoice.setTax(rs.getDouble("Tax"));
        invoice.setBalance(rs.getDouble("Balance"));
        invoice.setSalesPerson(rs.getString("SalesPerson"));
        // invoice.setNotes(rs.getString("Notes"));
        invoice.setStatus(rs.getString("Status"));
        invoice.setHistory(rs.getString("History"));

        // invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Ship", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));
        // invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Bill", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));

        // invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invoice.getInvoiceNumber()));

        pendingInvoices.add(invoice);

      }

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoices<BR/>" + e);
    }

    return pendingInvoices;
  }

  public static Vector<InvoiceBean> getRoutingPendingInvoices() throws UserException {
    Vector<InvoiceBean> pendingInvoices = null;
    Connection con = null;
    Statement stmt = null;
    try {
       con = DBInterfaceLocal.getSQLConnection();
       stmt = con.createStatement();

      long currTime = System.currentTimeMillis();
      long diffPeriod = 1296000000L;
      long oldTime = currTime - diffPeriod;
      java.util.Date dd = new java.util.Date(oldTime);
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
      String ddDate = sdf.format(dd);

      String getSQL = "";
      getSQL +=
          "select InvoiceNumber, OrderDate, Balance, InvoiceTotal from Invoice where InvoiceNumber > 600000 and ReturnedInvoice = 0  and OrderDate>'"
              + DateUtils.convertUSToMySQLFormat(ddDate) + "' order by InvoiceNumber ";
      System.out.println("getSQL--"+getSQL);
      ResultSet rs = stmt.executeQuery(getSQL);
      Statement stmtX = null;
      ResultSet rsX = null;
      while (rs.next()) {
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<InvoiceBean>();
        }
        int invNo = rs.getInt("InvoiceNumber");
        double bal = rs.getDouble("Balance");
        double invTot = rs.getDouble("InvoiceTotal");
        String ordDate = DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate"));
        long ordTime = DateUtils.getLongFromUSDate(ordDate);
        if (invTot == 0 && ((System.currentTimeMillis() - 432000000L) > ordTime)) {
          continue;
        }
        if (bal == 0 && ((System.currentTimeMillis() - 1296000000L) > ordTime)) {
          continue;
        }

         stmtX = con.createStatement();
         rsX = stmtX.executeQuery("Select Name From PartsPulled Where InvoiceNumber='" + invNo + "'");
        if (rsX.next()) {
          continue;
        }

        InvoiceBean invoice = new InvoiceBean();
        invoice.setInvoiceNumber(invNo);
        invoice.setOrderDate(ordDate);
        invoice.setInvoiceTotal(invTot);
        // invoice.setDiscount(rs.getDouble("Discount"));
        // invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        // invoice.setTax(rs.getDouble("Tax"));
        invoice.setBalance(bal);
        // invoice.setSalesPerson(rs.getString("SalesPerson"));
        // invoice.setStatus(rs.getString("Status"));
        // invoice.setHistory(rs.getString("History"));

        pendingInvoices.add(invoice);

      }
      stmtX.close();
      rsX.close();
      rs.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoices<BR/>" + e);
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
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

    return pendingInvoices;
  }

  public static Vector<String[]> getRoutingPendingInvoices2() throws UserException {
    Vector<String[]> pendingInvoices = null;
    Connection con = null;
    Statement stmt = null;
    try {
       con = DBInterfaceLocal.getSQLConnection();
       stmt = con.createStatement();
       Statement stmtX = null;
       ResultSet rsX = null;
      String getSQL = "";
      getSQL +=
          "select InvoiceNumber, OrderDate, Balance, InvoiceTotal from Invoice where InvoiceNumber > 400000 and ReturnedInvoice = 0 order by InvoiceNumber ";
      ResultSet rs = stmt.executeQuery(getSQL);
      while (rs.next()) {
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<String[]>();
        }
        int invNo = rs.getInt("InvoiceNumber");
        double bal = rs.getDouble("Balance");
        double invTot = rs.getDouble("InvoiceTotal");
        String ordDate = DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate"));
        long ordTime = DateUtils.getLongFromUSDate(ordDate);
        if (invTot == 0 && ((System.currentTimeMillis() - 432000000L) > ordTime)) {
          continue;
        }
        if (bal == 0 && ((System.currentTimeMillis() - 1296000000L) > ordTime)) {
          continue;
        }

         stmtX = con.createStatement();
         rsX = stmtX.executeQuery("Select Name From PartsPulled Where InvoiceNumber='" + invNo + "'");
        if (rsX.next()) {
          continue;
        }

        String[] retStr = new String[4];
        retStr[0] = invNo + "";
        retStr[1] = ordDate;
        retStr[2] = invTot + "";
        retStr[3] = bal + "";

        // invoice.setSalesPerson(rs.getString("SalesPerson"));
        // invoice.setStatus(rs.getString("Status"));
        // invoice.setHistory(rs.getString("History"));

        pendingInvoices.add(retStr);

      }
      stmtX.close();
      rsX.close();
      rs.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoices<BR/>" + e);
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
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

    return pendingInvoices;
  }

  public static Vector<InvoiceBean> getDeliveryPendingInvoices() throws UserException {
    Vector<InvoiceBean> pendingInvoices = null;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      Statement stmtX = null;
      ResultSet rsX = null;
      String getSQL = "";
      getSQL +=
          "select InvoiceNumber, OrderDate, Balance, InvoiceTotal from Invoice where InvoiceNumber > 74586 and ShipVia='Deliver' and ReturnedInvoice = 0 and orderdate > '2013-01-01' order by InvoiceNumber";
      ResultSet rs = stmt.executeQuery(getSQL);
      while (rs.next()) {
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<InvoiceBean>();
        }
        int invNo = rs.getInt("InvoiceNumber");
        double bal = rs.getDouble("Balance");
        double invTot = rs.getDouble("InvoiceTotal");
        String ordDate = DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate"));
        long ordTime = DateUtils.getLongFromUSDate(ordDate);
        if (invTot == 0 && ((System.currentTimeMillis() - 864000000L) > ordTime)) {
          continue;
        }
        if (bal == 0 && ((System.currentTimeMillis() - 7776000000L) > ordTime)) {
          continue;
        }

        stmtX = con.createStatement();
        rsX =
            stmtX.executeQuery("Select Name From PartsDelivered Where InvoiceNumber='" + invNo
                + "'");
        if (rsX.next()) {
          continue;
        }

        InvoiceBean invoice = new InvoiceBean();
        invoice.setInvoiceNumber(invNo);
        invoice.setOrderDate(ordDate);
        invoice.setInvoiceTotal(invTot);
        // invoice.setDiscount(rs.getDouble("Discount"));
        // invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        // invoice.setTax(rs.getDouble("Tax"));
        invoice.setBalance(bal);
        // invoice.setSalesPerson(rs.getString("SalesPerson"));
        // invoice.setStatus(rs.getString("Status"));
        // invoice.setHistory(rs.getString("History"));

        pendingInvoices.add(invoice);

      }
      rsX.close();
      rs.close();
      stmtX.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoices<BR/>" + e);
    }

    return pendingInvoices;
  }

  public static Vector<InvoiceDeliveryBean> getInvoicesNotDelivered2() throws UserException {
    Vector<InvoiceDeliveryBean> pendingInvoices = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
      con = DBInterfaceLocal.getSQLConnection();
      stmt = con.createStatement();
      String getSQL = "";
      getSQL +=
          "SELECT i.InvoiceNumber, i.InvoiceTime, c.CustomerId, i.SalesPerson, i.IsDelivered, c.companyname,i.notes"
              + " FROM Invoice i, customer c  " + " WHERE i.CustomerID = c.CustomerId "
              + " AND i.OrderDate >  DATE_SUB(NOW(), INTERVAL 3 MONTH) " + " AND i.Status!='C' "
              + " AND i.Status!='W' " + " AND i.ShipVia='Deliver' " + " AND i.IsDelivered='N' "
              + " AND i.Balance!=0 " + " AND i.ReturnedInvoice=0 " + " ORDER BY i.InvoiceNumber";
      rs = stmt.executeQuery(getSQL);
      while (rs.next()) {

        String customerid = rs.getString("CustomerId");
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<InvoiceDeliveryBean>();
        }
        InvoiceDeliveryBean invoiceDeliveryBean = new InvoiceDeliveryBean();
        invoiceDeliveryBean.setInvoicenumber(rs.getInt("InvoiceNumber"));
        invoiceDeliveryBean.setInvoicetime(rs.getLong("InvoiceTime"));
        invoiceDeliveryBean.setSalesperson(rs.getString("SalesPerson"));
        invoiceDeliveryBean.setIsdelivered(rs.getString("IsDelivered"));
        invoiceDeliveryBean.setCustomername(rs.getString("companyname"));
        invoiceDeliveryBean.setNotes(rs.getString("notes"));

        Statement stmt1 = con.createStatement();
        String sql =
            "SELECT Region FROM Address WHERE Id='" + customerid
                + "' And Who='Cust' and Type='Standard' ";
        ResultSet rs1 = stmt1.executeQuery(sql);
        if (rs1.next()) {
          invoiceDeliveryBean.setRegion(rs1.getString("Region"));
        } else {
          invoiceDeliveryBean.setRegion("");
        }
        rs1.close();
        stmt1.close();

        pendingInvoices.add(invoiceDeliveryBean);

      }

      rs.close();

      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e.toString());
    }

    return pendingInvoices;
  }

  public static Vector<InvoiceBean> getInvoicesNotDelivered() throws UserException {
    Vector<InvoiceBean> pendingInvoices = null;

    try {
      Connection con1 = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con1.createStatement();
      String getSQL = "";
      getSQL +=
          "SELECT * FROM Invoice WHERE OrderDate >  DATE_SUB(NOW(), INTERVAL 3 MONTH) AND STATUS!='C' AND STATUS!='W' AND ShipVia='Deliver' AND IsDelivered='N' AND Balance!=0 AND ReturnedInvoice=0 ORDER BY InvoiceNumber DESC ";
      ResultSet rs = stmt.executeQuery(getSQL);
      while (rs.next()) {
        if (pendingInvoices == null) {
          pendingInvoices = new Vector<InvoiceBean>();
        }
        InvoiceBean invoice = new InvoiceBean();
        invoice.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        // invoice.setInvoiceTime((rs.getLong("InvoiceTime")));
        invoice.setCustomerId(rs.getString("CustomerId"));
        // invoice.setReturnedInvoice(rs.getInt("ReturnedInvoice"));
        // invoice.setShipTo(rs.getString("ShipTo"));
        // invoice.setShipAttention(rs.getString("ShipAttention"));
        // invoice.setShipVia(rs.getString("ShipVia"));
        // invoice.setBillAttention(rs.getString("BillAttention"));
        // invoice.setInvoiceTotal(rs.getDouble("InvoiceTotal"));
        // invoice.setDiscount(rs.getDouble("Discount"));
        // invoice.setAppliedAmount(rs.getDouble("AppliedAmount"));
        // invoice.setNewApplied(rs.getDouble("NewApplied"));
        // invoice.setDateNewApplied(rs.getString("DateNewApplied"));
        // invoice.setTax(rs.getDouble("Tax"));
        // invoice.setBalance(rs.getDouble("Balance"));
        invoice.setSalesPerson(rs.getString("SalesPerson"));
        // invoice.setNotes(rs.getString("Notes"));
        // invoice.setStatus(rs.getString("Status"));
        // invoice.setHistory(rs.getString("History"));
        invoice.setIsDelivered(rs.getString("IsDelivered"));

        // invoice.setShipToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Ship", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));
        // invoice.setBillToAddress(AddressBean.getAddress(invoice.getCustomerId(),
        // "Bill", "Cust",
        // invoice.getOrderDate(), invoice.getInvoiceNumber()));

        // invoice.setInvoiceDetails(InvoiceDetailsBean.getInvoiceDetails(invoice.getInvoiceNumber()));

        pendingInvoices.add(invoice);

      }

      rs.close();
      stmt.close();
      con1.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Invoices<BR/>" + e);
    }

    return pendingInvoices;
  }

  public static void closeInvoice(int invNo) {
    // logger.error("2---" + invNo);
    if (invNo != 0 && InvoiceBean.isAvailable(invNo)) {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = null;
      try {
         stmt = con.createStatement();
        stmt.execute("Update Invoice set Status='C' where InvoiceNumber=" + invNo);
      } catch (SQLException e) {
        logger.error("In Close Invoice: " + e.getMessage());
      }finally {
			if (stmt != null) {
				try {
					stmt.close();
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
  }

  public static void createHistory(UserBean user, InvoiceBean oldInvoice, InvoiceBean newInvoice) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    PreparedStatement pstmt = null;
    try {
      int invoiceNo = 0;
      String modifiedBy = user.getUsername();
      String modifiedDate = DateUtils.getNewUSDate();
      int modifiedOrder = 0;
      String remarks = "";

      invoiceNo = oldInvoice.getInvoiceNumber();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select max(ModifiedOrder) From InvoiceHistory Where InvoiceNumber="
              + invoiceNo);
      if (rs.next()) {
        modifiedOrder = rs.getInt(1) + 1;
      } else {
        modifiedOrder = 1;
      }

      if (!oldInvoice.getOrderDate().trim().equals(newInvoice.getOrderDate().trim())) {
        remarks +=
            "Invoice Date Changed From " + oldInvoice.getOrderDate().trim() + " To "
                + newInvoice.getOrderDate().trim() + "<br>";
      }

      if (oldInvoice.getReturnedInvoice() == 0 && newInvoice.getReturnedInvoice() != 0) {
        remarks += "Returned Invoice No Added: " + newInvoice.getReturnedInvoice() + "<BR>";
      } else if (oldInvoice.getReturnedInvoice() != 0 && newInvoice.getReturnedInvoice() == 0) {
        remarks += "Returned Invoice No " + oldInvoice.getReturnedInvoice() + " Removed " + "<BR>";
      } else if (oldInvoice.getReturnedInvoice() != newInvoice.getReturnedInvoice()) {
        remarks +=
            "Returned Invoice No Changed From " + oldInvoice.getReturnedInvoice() + " To "
                + newInvoice.getReturnedInvoice() + "<BR>";
      }

      if (oldInvoice.getInvoiceTotal() != newInvoice.getInvoiceTotal()) {
        remarks +=
            "Invoice Total Changed From " + oldInvoice.getInvoiceTotal() + " To "
                + newInvoice.getInvoiceTotal() + "<BR>";
      }
      if (oldInvoice.getTax() != newInvoice.getTax()) {
        remarks +=
            "Tax Changed From " + oldInvoice.getTax() + " To " + newInvoice.getTax() + "<BR>";
      }
      if (oldInvoice.getDiscount() == 0 && newInvoice.getDiscount() != 0) {
        remarks += "Discount Of " + newInvoice.getDiscount() + " is Added " + "<BR>";
      } else if (oldInvoice.getDiscount() != newInvoice.getDiscount()) {
        remarks +=
            "Discount Changed From " + oldInvoice.getDiscount() + " To " + newInvoice.getDiscount()
                + "<BR>";
      }

      if (oldInvoice.getInvoiceDetails().size() != 0 && newInvoice.getInvoiceDetails().size() == 0) {
        remarks += " *** Invoice Voided *** " + "<BR>";
      }

      Enumeration<InvoiceDetailsBean> ennum1 = oldInvoice.getInvoiceDetails().elements();
      while (ennum1.hasMoreElements()) {
        InvoiceDetailsBean invDet1 = ennum1.nextElement();
        Enumeration<InvoiceDetailsBean> ennum2 = newInvoice.getInvoiceDetails().elements();
        boolean partFound = false;
        while (ennum2.hasMoreElements()) {
          InvoiceDetailsBean invDet2 = ennum2.nextElement();
          if (invDet1.getPartNumber().trim().equals(invDet2.getPartNumber().trim())) {
            partFound = true;
            remarks += " Part Number " + invDet1.getPartNumber() + " ----- Exists " + "<BR>";
            if (invDet1.getQuantity() != invDet2.getQuantity()) {
              remarks +=
                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quantity Changed From "
                      + invDet1.getQuantity() + " To " + invDet2.getQuantity() + "<BR>";
            }
            if (invDet1.getSoldPrice() != invDet2.getSoldPrice()) {
              remarks +=
                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost Price Changed From "
                      + invDet1.getSoldPrice() + " To " + invDet2.getSoldPrice() + "<BR>";
            }
            break;
          }
        }
        if (!partFound) {
          remarks += " Part Number " + invDet1.getPartNumber() + " ----- Removed " + "<BR>";
        }
      }

      Enumeration<InvoiceDetailsBean> ennum3 = newInvoice.getInvoiceDetails().elements();
      while (ennum3.hasMoreElements()) {
        InvoiceDetailsBean invDet3 = ennum3.nextElement();
        Enumeration<InvoiceDetailsBean> ennum4 = oldInvoice.getInvoiceDetails().elements();
        boolean partFound = false;
        while (ennum4.hasMoreElements()) {
          InvoiceDetailsBean invDet4 = ennum4.nextElement();
          if (invDet3.getPartNumber().trim().equals(invDet4.getPartNumber().trim())) {
            partFound = true;
          }
        }
        if (!partFound) {
          remarks += " Part Number " + invDet3.getPartNumber() + " ----- Added " + "<BR>";
          remarks +=
              "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quantity : "
                  + invDet3.getQuantity() + "<BR>";
          remarks +=
              "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost Price : "
                  + invDet3.getSoldPrice() + "<BR>";
        }
      }

       pstmt = con.prepareStatement("INSERT INTO InvoiceHistory (InvoiceNumber, ModifiedBy, ModifiedDate, ModifiedOrder, Remarks1, Remarks2, Remarks3, Remarks4, Remarks5) values (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

      pstmt.clearParameters();

      pstmt.setInt(1, invoiceNo);
      pstmt.setString(2, modifiedBy);
      pstmt.setString(3, DateUtils.convertUSToMySQLFormat(modifiedDate));
      pstmt.setInt(4, modifiedOrder);

      if (remarks.equals("")) {
        remarks += "<br>";
      }
      if (remarks.length() <= 200) {
        pstmt.setString(5, remarks);
      } else {
        pstmt.setString(5, remarks.substring(0, 200));
      }
      if (remarks.length() <= 200) {
        pstmt.setString(6, "");
      } else if (remarks.length() > 200 && remarks.length() <= 400) {
        pstmt.setString(6, remarks.substring(200));
      } else if (remarks.length() > 400) {
        pstmt.setString(6, remarks.substring(200, 400));
      }
      if (remarks.length() <= 400) {
        pstmt.setString(7, "");
      } else if (remarks.length() > 400 && remarks.length() <= 600) {
        pstmt.setString(7, remarks.substring(400));
      } else if (remarks.length() > 600) {
        pstmt.setString(7, remarks.substring(400, 600));
      }
      if (remarks.length() <= 600) {
        pstmt.setString(8, "");
      } else if (remarks.length() > 600 && remarks.length() <= 800) {
        pstmt.setString(8, remarks.substring(600));
      } else if (remarks.length() > 800) {
        pstmt.setString(8, remarks.substring(600, 800));
      }
      if (remarks.length() <= 800) {
        pstmt.setString(9, "");
      } else if (remarks.length() > 800 && remarks.length() <= 1000) {
        pstmt.setString(9, remarks.substring(800));
      } else if (remarks.length() > 1000) {
        pstmt.setString(9, remarks.substring(800, 1000));
      }

      pstmt.execute();

    } catch (Exception e) {
      logger.error(e);
    }finally {
		if (pstmt != null) {
			try {
				pstmt.close();
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

}
