package com.bvas.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;

public class AppliedAmountBean {
  private static final Logger logger = Logger.getLogger(AppliedAmountBean.class);

  private int invoiceNumber;

  private double appliedAmount;

  private String appliedDate;

  public int getInvoiceNumber() {
    return (this.invoiceNumber);
  }

  public double getAppliedAmount() {
    return (this.appliedAmount);
  }

  public String getAppliedDate() {
    return (this.appliedDate);
  }

  public void setInvoiceNumber(int invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public void setAppliedAmount(double appliedAmount) {
    this.appliedAmount = appliedAmount;
  }

  public void setAppliedDate(String appliedDate) {
    this.appliedDate = appliedDate;
  }

  public static Vector<AppliedAmountBean> getAppliedAmounts(int invNo) {
    Vector<AppliedAmountBean> all = new Vector<AppliedAmountBean>();
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM AppliedAmounts WHERE InvoiceNumber='" + invNo + "'");
      while (rs.next()) {
        AppliedAmountBean bean = new AppliedAmountBean();
        bean.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        bean.setAppliedAmount(rs.getDouble("AppliedAmount"));
        bean.setAppliedDate(rs.getString("AppliedDate"));
        all.add(bean);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("In AppliedAmountBean - Not able to get applied amounts" + e);
    }
    return all;
  }

}
