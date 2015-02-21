package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.UserException;

public class BouncedChecksBean implements Serializable {
  private static final Logger logger = Logger.getLogger(BouncedChecksBean.class);

  private static final long serialVersionUID = 1L;

  private int checkId;

  private String customerId = null;

  private String enteredDate = null;

  private String checkNo = null;

  private String checkDate = null;

  private double bouncedAmount;

  private double paidAmount;

  private double balance;

  private String isCleared = null;

  public int getCheckId() {
    return (this.checkId);
  }

  public String getCustomerId() {
    return (this.customerId);
  }

  public String getEnteredDate() {
    return (this.enteredDate);
  }

  public String getCheckNo() {
    return (this.checkNo);
  }

  public String getCheckDate() {
    return (this.checkDate);
  }

  public double getBouncedAmount() {
    return (this.bouncedAmount);
  }

  public double getPaidAmount() {
    return (this.paidAmount);
  }

  public double getBalance() {
    return (this.balance);
  }

  public String getIsCleared() {
    return (this.isCleared);
  }

  public void setCheckId(int checkId) {
    this.checkId = checkId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setEnteredDate(String enteredDate) {
    this.enteredDate = enteredDate;
  }

  public void setCheckNo(String checkNo) {
    this.checkNo = checkNo;
  }

  public void setCheckDate(String checkDate) {
    this.checkDate = checkDate;
  }

  public void setBouncedAmount(double bouncedAmount) {
    this.bouncedAmount = bouncedAmount;
  }

  public void setPaidAmount(double paidAmount) {
    this.paidAmount = paidAmount;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void setIsCleared(String isCleared) {
    this.isCleared = isCleared;
  }

  public static BouncedChecksBean getBouncedCheck(int id, String custId) {

    BouncedChecksBean bouncedCheck = null;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      if (custId == null) {
        custId = "";
      }
      if (id == 0 && !custId.trim().equals("")) {
        Statement stmt1 = con.createStatement();
        ResultSet rs =
            stmt1.executeQuery("Select Max(CheckId) from BouncedChecks where CustomerId='" + custId
                + "'");
        if (rs.next()) {
          id = rs.getInt(1);
        }
      }

      ResultSet rs = stmt.executeQuery("Select * from BouncedChecks Where CheckId=" + id);
      if (rs.next()) {
        bouncedCheck = new BouncedChecksBean();
        bouncedCheck.setCheckId(rs.getInt("CheckId"));
        bouncedCheck.setCustomerId(rs.getString("CustomerId"));
        bouncedCheck.setEnteredDate(DateUtils.convertMySQLToUSFormat(rs.getString("EnteredDate")));
        bouncedCheck.setCheckNo(rs.getString("CheckNo"));
        bouncedCheck.setCheckDate(DateUtils.convertMySQLToUSFormat(rs.getString("CheckDate")));
        bouncedCheck.setBouncedAmount(rs.getDouble("BouncedAmount"));
        bouncedCheck.setPaidAmount(rs.getDouble("PaidAmount"));
        bouncedCheck.setBalance(rs.getDouble("Balance"));
        bouncedCheck.setIsCleared(rs.getString("IsCleared"));
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return bouncedCheck;

  }

  public static Vector<BouncedChecksBean> getAllBouncedChecks(String custId) {

    Vector<BouncedChecksBean> bouncedChecks = null;

    try {

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select * from BouncedChecks Where CustomerId='" + custId.trim()
              + "' and IsCleared='N'");
      while (rs.next()) {
        if (bouncedChecks == null) {
          bouncedChecks = new Vector<BouncedChecksBean>();
        }
        BouncedChecksBean bouncedCheck = new BouncedChecksBean();
        bouncedCheck.setCheckId(rs.getInt("CheckId"));
        bouncedCheck.setCustomerId(rs.getString("CustomerId"));
        bouncedCheck.setEnteredDate(DateUtils.convertMySQLToUSFormat(rs.getString("EnteredDate")));
        bouncedCheck.setCheckNo(rs.getString("CheckNo"));
        bouncedCheck.setCheckDate(DateUtils.convertMySQLToUSFormat(rs.getString("CheckDate")));
        bouncedCheck.setBouncedAmount(rs.getDouble("BouncedAmount"));
        bouncedCheck.setPaidAmount(rs.getDouble("PaidAmount"));
        bouncedCheck.setBalance(rs.getDouble("Balance"));
        bouncedCheck.setIsCleared(rs.getString("IsCleared"));

        if (bouncedCheck.getBalance() != 0) {
          bouncedChecks.add(bouncedCheck);
        }
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return bouncedChecks;
  }

  public void addBouncedCheck() throws UserException {
    try {
      CustomerBean cust = CustomerBean.getCustomer(getCustomerId());

      if (cust == null) {
        throw new UserException("Customer Id Is Wrong");
      }

      Connection con = DBInterfaceLocal.getSQLConnection();
      PreparedStatement stmt =
          con.prepareStatement("INSERT INTO BouncedChecks (CheckId, CustomerId, EnteredDate, CheckNo, CheckDate, BouncedAmount, IsCleared, PaidAmount, Balance) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?) ");
      stmt.clearParameters();
      stmt.setInt(1, getCheckId());
      stmt.setString(2, getCustomerId());
      stmt.setString(3, DateUtils.convertUSToMySQLFormat(getEnteredDate()));
      stmt.setString(4, getCheckNo());
      stmt.setString(5, DateUtils.convertUSToMySQLFormat(getCheckDate()));
      stmt.setDouble(6, getBouncedAmount());
      stmt.setString(7, getIsCleared());
      stmt.setDouble(8, getBalance());

      stmt.execute();

      cust.setCreditBalance(cust.getCreditBalance() + getBalance());
      cust.changeCustomer();

      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Check Not Added" + e.getMessage());
    }
  }

  public void changeBouncedCheck() throws UserException {
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      PreparedStatement stmt =
          con.prepareStatement("UPDATE BouncedChecks set EnteredDate=?, CheckDate=?, BouncedAmount=?, IsCleared=?, PaidAmount=?, Balance=? Where CheckId=? ");
      stmt.clearParameters();
      stmt.setString(1, DateUtils.convertUSToMySQLFormat(getEnteredDate()));
      stmt.setString(2, DateUtils.convertUSToMySQLFormat(getCheckDate()));
      stmt.setDouble(3, getBouncedAmount());
      stmt.setString(4, getIsCleared());
      stmt.setDouble(5, getPaidAmount());
      stmt.setDouble(6, getBalance());
      stmt.setInt(7, getCheckId());

      stmt.execute();

      CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
      cust.setCreditBalance(cust.getCreditOrBalance());
      cust.changeCustomer();

      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Check Not Changed");
    }
  }

  public static boolean isAvailable(int chkId) {
    boolean avail = false;
    if (chkId == 0) {
      return avail;
    } else {
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("Select * from BouncedChecks Where CheckId =" + chkId);
        if (rs.next()) {
          avail = true;
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (Exception e) {
        logger.error(e);
      }
      return avail;
    }
  }

  public static int getMaxCheckId() {
    int chkId = 0;

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("Select max(CheckId) from BouncedChecks ");
      if (rs.next()) {
        chkId = rs.getInt(1) + 1;
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return chkId;
  }

  public String[][] getPayments(int checkId) throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    String[][] payments = null;
    try {

      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select count(*) from AppliedAmounts Where InvoiceNumber='BC"
              + checkId + "'");
      int noOfPayments = 0;
      if (rs1.next()) {
        noOfPayments = rs1.getInt(1);
      }
      if (noOfPayments != 0) {
        payments = new String[noOfPayments][6];
        Statement stmt2 = con.createStatement();
        ResultSet rs2 =
            stmt2.executeQuery("Select * from AppliedAmounts Where InvoiceNumber='BC" + checkId
                + "'");
        int cnt = 0;
        while (rs2.next()) {
          payments[cnt][0] = rs2.getString("InvoiceNumber") + "";
          payments[cnt][1] = DateUtils.convertMySQLToUSFormat(rs2.getString("AppliedDate"));
          payments[cnt][2] = rs2.getDouble("AppliedAmount") + "";
          payments[cnt][3] = rs2.getString("PaymentType");
          payments[cnt][4] = rs2.getString("UserName");
          payments[cnt][5] = rs2.getString("PaymentTime");
          cnt++;
        }
        rs2.close();
        stmt2.close();
      }

      rs1.close();
      stmt1.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Exception When Finding Payments - " + e);
    }

    return payments;
  }

  public void addPayment(double amount, String date, String paymentType, String userName)
      throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      Statement stmt = con.createStatement();
      String sql =
          "INSERT INTO AppliedAmounts (InvoiceNumber, AppliedAmount, AppliedDate, PaymentType, UserName, PaymentTime) VALUES ('BC"
              + getCheckId()
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

      setPaidAmount(getPaidAmount() + amount);
      setBalance(getBalance() - amount);
      changeBouncedCheck();

      // CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
      // cust.setCreditBalance(cust.getCreditBalance());
      // cust.changeCustomer();

      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Payment not added - " + e);
    }
  }

  public void deletePayment(double amount, String date, String paymentType) throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select count(*) from AppliedAmounts Where InvoiceNumber='BC"
              + getCheckId() + "'");
      int noOfPayments = 0;
      if (rs1.next()) {
        noOfPayments = rs1.getInt(1);
      }
      if (noOfPayments == 0) {
        throw new UserException("No Payments To Delete");
      }

      Statement stmt = con.createStatement();
      String sql = "DELETE FROM AppliedAmounts WHERE ";
      sql += "InvoiceNumber='BC" + getCheckId() + "'";
      sql += " and AppliedAmount=" + amount;
      if (date != null && !date.trim().equals("")) {
        sql += " and AppliedDate='" + DateUtils.convertUSToMySQLFormat(date) + "'";
      }
      if (paymentType != null && !paymentType.trim().equals("")) {
        sql += " and PaymentType='" + paymentType + "'";
      }
      stmt.execute(sql);

      setPaidAmount(getPaidAmount() - amount);
      setBalance(getBalance() + amount);
      changeBouncedCheck();

      // CustomerBean cust = CustomerBean.getCustomer(getCustomerId());
      // cust.setCreditBalance(cust.getCreditBalance() + amount);
      // cust.changeCustomer();

      stmt.close();
      rs1.close();
      stmt1.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Payment not deleted - " + e);
    }
  }

}
