package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.UserException;

public class CustomerBean implements Serializable {
  private static final Logger logger = Logger.getLogger(CustomerBean.class);

  private static final long serialVersionUID = 1L;

  private String customerId = null;

  private String companyName = null;

  private String contactName = null;

  private String contactTitle = null;

  private String terms = null;

  private String taxId = null;

  private String taxIdNumber = null;

  private String notes = null;

  private String paymentTerms = null;

  private double creditBalance;

  private double creditLimit;

  private int customerLevel;

  private AddressBean address = null;

  public String getCustomerId() {
    return (this.customerId);
  }

  public String getCompanyName() {
    return (this.companyName);
  }

  public String getContactName() {
    return (this.contactName);
  }

  public String getContactTitle() {
    return (this.contactTitle);
  }

  public String getTerms() {
    return (this.terms);
  }

  public String getTaxId() {
    return (this.taxId);
  }

  public String getTaxIdNumber() {
    return (this.taxIdNumber);
  }

  public String getNotes() {
    return (this.notes);
  }

  public String getPaymentTerms() {
    return (this.paymentTerms);
  }

  public double getCreditBalance() {
    // if (creditBalance == 0) {
    // getCreditOrBalance();
    // }
    return (this.creditBalance);
  }

  public double getCreditLimit() {
    return (this.creditLimit);
  }

  public int getCustomerLevel() {
    return (this.customerLevel);
  }

  public AddressBean getAddress() {
    return (this.address);
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public void setContactTitle(String contactTitle) {
    this.contactTitle = contactTitle;
  }

  public void setTerms(String terms) {
    this.terms = terms;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public void setTaxIdNumber(String taxIdNumber) {
    this.taxIdNumber = taxIdNumber;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public void setCreditBalance(double creditBalance) {
    this.creditBalance = creditBalance;
  }

  public void setCreditLimit(double creditLimit) {
    this.creditLimit = creditLimit;
  }

  public void setCustomerLevel(int customerLevel) {
    this.customerLevel = customerLevel;
  }

  public void setAddress(AddressBean address) {
    this.address = address;
  }

  public static Hashtable<String, String> getAllCustomers() throws SQLException {
    Hashtable<String, String> allCustomers = new Hashtable<String, String>();

    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      Statement stmt = con.createStatement();
      StringBuffer sql = new StringBuffer("SELECT ");
      sql.append(" CustomerId, CompanyName ");
      sql.append(" from Customer ORDER BY CompanyName");
      ResultSet rs = stmt.executeQuery(sql.toString());
      while (rs.next()) {
        String custId = rs.getString("CustomerId");
        String custName = rs.getString("CompanyName");
        allCustomers.put(custId, custName);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      throw e;
    }
    return allCustomers;
  }

  public static CustomerBean getCustomer(String idOrName) throws UserException {
    Connection con1 = DBInterfaceLocal.getSQLConnection();
    CustomerBean custBean = null;
    try {

      PreparedStatement pstmt =
          con1.prepareStatement("SELECT * FROM Customer WHERE CustomerId=? OR CompanyName LIKE '"
              + idOrName + "%'");
      pstmt.clearParameters();
      pstmt.setString(1, idOrName);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        custBean = new CustomerBean();
        String custId = rs.getString("CustomerId");
        custBean.setCustomerId(custId);
        custBean.setCompanyName(rs.getString("CompanyName"));
        custBean.setContactName(rs.getString("ContactName"));
        custBean.setContactTitle(rs.getString("ContactTitle"));
        custBean.setTerms(rs.getString("Terms"));
        custBean.setTaxId(rs.getString("TaxId"));
        custBean.setTaxIdNumber(rs.getString("TaxIdNumber"));
        custBean.setNotes(rs.getString("Notes"));
        custBean.setPaymentTerms(rs.getString("PaymentTerms"));
        custBean.setCreditBalance(rs.getDouble("CreditBalance"));
        custBean.setCreditLimit(rs.getDouble("CreditLimit"));
        custBean.setCustomerLevel(rs.getInt("CustomerLevel"));

        AddressBean address = null;
        address = AddressBean.getAddress(custId, "Standard", "Cust", "", 0);
        custBean.setAddress(address);

      }
      rs.close();
      pstmt.close();
      con1.close();
    } catch (SQLException e) {
      logger.error("In CustomerBean - Unable to getCustomer" + e);
      throw new UserException("Customer Not Available :  Please check your Customer Id");
    }

    return custBean;
  }

  public void addNewCustomer() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer insertSQL = new StringBuffer("");
      insertSQL.append("INSERT INTO Customer (");
      insertSQL.append("CustomerId, CompanyName, ContactName, ContactTitle, ");
      insertSQL
          .append("Terms, TaxId, TaxIdNumber, Notes, PaymentTerms, CreditBalance, CreditLimit, CustomerLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)");

      PreparedStatement pstmt = con.prepareStatement(insertSQL.toString());
      pstmt.clearParameters();
      pstmt.setString(1, getCustomerId());
      pstmt.setString(2, getCompanyName());
      pstmt.setString(3, getContactName());
      pstmt.setString(4, getContactTitle());
      pstmt.setString(5, getTerms());
      pstmt.setString(6, getTaxId());
      pstmt.setString(7, getTaxIdNumber());
      pstmt.setString(8, getNotes());
      pstmt.setString(9, "C");
      pstmt.setDouble(10, 0);
      pstmt.setDouble(11, 1000.00);

      pstmt.execute();

      getAddress().addNewAddress();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In CustomerBean - Unable to add the Customer<BR/>" + e);
    }

  }

  public void changeCustomer() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer changeSQL = new StringBuffer("");
      changeSQL.append("UPDATE Customer set ");
      changeSQL.append("CompanyName=?, ");
      changeSQL.append("ContactName=?,");
      changeSQL.append("ContactTitle=?,");
      changeSQL.append("Terms=?, ");
      changeSQL.append("TaxId=?, ");
      changeSQL.append("TaxIdNumber=?, ");
      changeSQL.append("Notes=?, ");
      changeSQL.append("PaymentTerms=?, ");
      changeSQL.append("CreditBalance=?, ");
      changeSQL.append("CreditLimit=?, ");
      changeSQL.append("CustomerLevel=? ");
      changeSQL.append(" WHERE CustomerId=?");
      PreparedStatement pstmt = con.prepareStatement(changeSQL.toString());

      pstmt.clearParameters();
      pstmt.setString(1, getCompanyName());
      pstmt.setString(2, getContactName());
      pstmt.setString(3, getContactTitle());
      pstmt.setString(4, getTerms());
      pstmt.setString(5, getTaxId());
      pstmt.setString(6, getTaxIdNumber());
      pstmt.setString(7, getNotes());
      pstmt.setString(8, getPaymentTerms());
      pstmt.setDouble(9, creditBalance);
      pstmt.setDouble(10, getCreditLimit());
      pstmt.setInt(11, getCustomerLevel());
      pstmt.setString(12, getCustomerId());

      pstmt.executeUpdate();

      if (getAddress().isAvailable()) {
        getAddress().changeAddress();
      } else {
        getAddress().addNewAddress();
      }

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In CustomerBean - Unable to change the Customer<BR/>" + e);
    }

  }

  public void deleteCustomer() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer deleteSQL = new StringBuffer("");
      deleteSQL.append("DELETE FROM Customer WHERE CustomerId=? ");
      PreparedStatement pstmt = con.prepareStatement(deleteSQL.toString());
      pstmt.clearParameters();
      pstmt.setString(1, getCustomerId());

      pstmt.execute();

      getAddress().deleteAddress();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In CustomerBean - Unable to delete the Customer<BR/>" + e);
    }
  }

  public static String getTaxId(String custId) {
    String tax = "";
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      String sql = "SELECT TaxId FROM Customer WHERE CustomerId='" + custId + "'";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        tax = rs.getString("TaxId");
      } else {
        tax = "N";
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
      tax = "N";
    }
    return tax;

  }

  public static String getCompanyName(String custId) {
    String custName = "";
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      String sql = "SELECT CompanyName FROM Customer WHERE CustomerId='" + custId + "'";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        custName = rs.getString("CompanyName");
      }
      rs.close();

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When Getting the Customer Name : " + custId);
    }
    return custName;

  }

  public static String getCreditBalance(String custId) {
    String creditBalance = "";
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      String sql = "SELECT CreditBalance FROM Customer WHERE CustomerId='" + custId + "'";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        creditBalance = rs.getString("CreditBalance");
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When Getting the Credit Balance : " + custId);
    }
    return creditBalance;

  }

  public static String getRegion(String custId) {
    String region = "";

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      String sql =
          "SELECT Region FROM Address WHERE Id='" + custId
              + "' And Who='Cust' and Type='Standard' ";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        region = rs.getString("Region");
      }
      if (region == null) {
        region = "";
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e1) {
      logger.error(e1);
    }

    return region;

  }

  public static int getCustomerLevel(String custId) {
    int cLevel = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      String sql = "SELECT CustomerLevel FROM Customer WHERE CustomerId='" + custId + "'";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        cLevel = rs.getInt("CustomerLevel");
      } else {
        cLevel = 0;
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When Getting the Customer Level : " + custId);
      cLevel = 0;
    }
    return cLevel;

  }

  public boolean hasGoodCredit() {
    if (getCreditLimit() > 0 && getCreditBalance() > getCreditLimit()) {
      double amt = getCreditOrBalance();
      if (getCreditLimit() > 0 && getCreditBalance() > getCreditLimit()) {
        return false;
      } else if (getCreditLimit() == 1) {
        return false;
      } else {
        return true;
      }
    } else if (getCreditLimit() == 1) {
      return false;
    } else {
      return true;
    }
  }

  public static boolean isAvailable(String custId) {
    boolean available = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer checkSQL = new StringBuffer("");
      checkSQL.append("SELECT * FROM Customer WHERE CustomerId=?");
      PreparedStatement pstmt = con.prepareStatement(checkSQL.toString());
      pstmt.clearParameters();
      pstmt.setString(1, custId);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        available = true;
      } else {
        available = false;
      }

      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
    }
    return available;
  }

  public String getMostRecentInvoice() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    String recentInvoice = "";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select Max(OrderDate) From Invoice Where CustomerId='"
              + getCustomerId().trim() + "'");
      if (rs.next()) {
        recentInvoice = rs.getString(1);
        if (recentInvoice != null && !recentInvoice.trim().equals("")) {
          recentInvoice = DateUtils.convertMySQLToUSFormat(recentInvoice);
        } else {
          recentInvoice = "";
        }
      } else {
        recentInvoice = "";
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return recentInvoice;
  }

  public double getCreditOrBalance() {
    double amountPending = 0.0;
    try {
      Vector<InvoiceBean> pendingInvoices = InvoiceBean.getPendingInvoices(getCustomerId());

      if (pendingInvoices != null) {
        Enumeration<InvoiceBean> ennum = pendingInvoices.elements();
        while (ennum.hasMoreElements()) {
          InvoiceBean invoice = ennum.nextElement();
          amountPending += invoice.getBalance();
        }
      }
      Vector<BouncedChecksBean> bouncedChecks =
          BouncedChecksBean.getAllBouncedChecks(getCustomerId());
      if (bouncedChecks != null) {
        Enumeration<BouncedChecksBean> ennum = bouncedChecks.elements();
        while (ennum.hasMoreElements()) {
          BouncedChecksBean bCheck = ennum.nextElement();
          amountPending += bCheck.getBalance();
        }
      }
      if (creditBalance != amountPending) {
        setCreditBalance(amountPending);
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        stmt.execute("Update Customer Set CreditBalance=" + amountPending + " Where CustomerId='"
            + getCustomerId() + "'");
        // changeCustomer();

        stmt.close();
        con.close();
      }

    } catch (Exception e) {
      logger.error(e);
    }

    return amountPending;
  }

  public static int getMonthEndDiscount(String custId) {
    int disc = 5;

    try {

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select sum(InvoiceTotal) from invoice where customerid='"
              + custId.trim() + "' and OrderDate>='2007-06-01' and OrderDate<='"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDateForInvoice()) + "'");
      if (rs.next()) {
        double amt = rs.getDouble(1);
        logger.error(custId + " --- Totally Bought: " + amt);
        /*
         * if (amt < 500) { disc = 5; } else if (amt < 1000) { disc = 7; } else if (amt < 3000) {
         * disc = 9; } else if (amt < 5000) { disc = 11; } else if (amt < 8000) { disc = 13; } else
         * { disc = 15; }
         */
        if (amt < 500) {
          disc = 5;
        } else if (amt < 1000) {
          disc = 6;
        } else if (amt < 2000) {
          disc = 7;
        } else if (amt < 3000) {
          disc = 8;
        } else if (amt < 4000) {
          disc = 9;
        } else if (amt < 6000) {
          disc = 10;
        } else if (amt < 8000) {
          disc = 11;
        } else if (amt < 10000) {
          disc = 12;
        } else if (amt < 13000) {
          disc = 13;
        } else if (amt < 17000) {
          disc = 14;
        } else {
          disc = 15;
        }
        logger.error("   Giving Discount: " + disc);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return disc;
  }

  public static boolean isFirstTimeBuying(String custId) {
    boolean isFirstTime = false;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select count(*) from invoice where customerid='" + custId.trim() + "'");
      if (rs.next()) {
        int xx = rs.getInt(1);
        if (xx == 0) {
          isFirstTime = true;
          logger.error(custId + " --- Is First Time");
        }
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return isFirstTime;
  }

  public static int getFirstInvoice(String custId) {
    int invNo = 0;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select min(InvoiceNumber) from invoice where customerid='"
              + custId.trim() + "'");
      if (rs.next()) {
        int xx = rs.getInt(1);
        if (xx == 0) {
          invNo = 0;
        } else {
          invNo = xx;
        }
      }
      rs.close();

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return invNo;
  }

  public static double checkWriteOff(String custId) {
    double amt = 0.0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select sum(Balance) from invoice Where CustomerId='" + custId
              + "' and (Status='W' or Status='C') and Balance > 0 ");
      if (rs.next()) {
        amt = rs.getDouble(1);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When getting Write Off Amounts" + e.getMessage());
    }
    return amt;
  }

}
