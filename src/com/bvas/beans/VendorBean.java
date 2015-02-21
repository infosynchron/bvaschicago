package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.LabelValueBean;
import com.bvas.utils.UserException;

public class VendorBean implements Serializable {
  private static final Logger logger = Logger.getLogger(VendorBean.class);

  private int supplierId;

  private String companyName = null;

  private String contactName = null;

  private String contactTitle = null;

  private String homePage = null;

  private String eMail = null;

  private String comments = null;

  private AddressBean address = null;

  public int getSupplierId() {
    return (this.supplierId);
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

  public String getHomePage() {
    return (this.homePage);
  }

  public String getEMail() {
    return (this.eMail);
  }

  public String getComments() {
    return (this.comments);
  }

  public AddressBean getAddress() {
    return (this.address);
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
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

  public void setHomePage(String homePage) {
    this.homePage = homePage;
  }

  public void setEMail(String eMail) {
    this.eMail = eMail;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public void setAddress(AddressBean address) {
    this.address = address;
  }

  public void addNewVendor() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer insertSQL = new StringBuffer("INSERT INTO Vendors (");
      insertSQL.append("SupplierId, CompanyName, ContactName, ");
      insertSQL.append("ContactTitle, Homepage, ");
      insertSQL.append("eMail, Comments ) VALUES (?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement pstmt = con.prepareStatement(insertSQL.toString());

      pstmt.clearParameters();
      pstmt.setInt(1, getSupplierId());
      pstmt.setString(2, getCompanyName());
      pstmt.setString(3, getContactName());
      pstmt.setString(4, getContactTitle());
      pstmt.setString(5, getHomePage());
      pstmt.setString(6, getEMail());
      pstmt.setString(7, getComments());
      logger.error("1");
      pstmt.execute();
      logger.error("2");

      getAddress().addNewAddress();
      logger.error("3");

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to Add New Vendor - " + e);
    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Unable to Add New Vendor - " + e);
    }

  }

  public void changeVendor() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer changeSQL = new StringBuffer("UPDATE Vendors set ");
      changeSQL.append("CompanyName=?, ");
      changeSQL.append("ContactName=?, ");
      changeSQL.append("ContactTitle=?, ");
      changeSQL.append("HomePage=?, ");
      changeSQL.append("eMail=?, ");
      changeSQL.append("Comments=? WHERE SupplierId=? ");
      PreparedStatement pstmt = con.prepareStatement(changeSQL.toString());

      pstmt.clearParameters();
      pstmt.setString(1, getCompanyName());
      pstmt.setString(2, getContactName());
      pstmt.setString(3, getContactTitle());
      pstmt.setString(4, getHomePage());
      pstmt.setString(5, getEMail());
      pstmt.setString(6, getComments());
      pstmt.setInt(7, getSupplierId());

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
      throw new UserException("Unable to Change the Vendor - " + e);
    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Unable to Change the Vendor - " + e);
    }

  }

  public void deleteVendor() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer deleteSQL =
          new StringBuffer("DELETE FROM Vendors WHERE SupplierId=" + getSupplierId());
      PreparedStatement pstmt = con.prepareStatement(deleteSQL.toString());

      pstmt.execute();

      getAddress().deleteAddress();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to Delete the Vendor - " + e);
    }

  }

  public static VendorBean getVendor(int supplierId) throws UserException {

    VendorBean vendor = new VendorBean();

    Connection con = DBInterfaceLocal.getSQLConnection();

    try {

      PreparedStatement pstmt =
          con.prepareStatement("select * from Vendors where SupplierId=" + supplierId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        vendor.setSupplierId(rs.getInt("SupplierId"));
        vendor.setCompanyName(rs.getString("CompanyName"));
        vendor.setContactName(rs.getString("ContactName"));
        vendor.setContactTitle(rs.getString("ContactTitle"));
        vendor.setHomePage(rs.getString("HomePage"));
        vendor.setEMail(rs.getString("eMail"));
        vendor.setComments(rs.getString("Comments"));

        AddressBean addr = AddressBean.getAddress(supplierId + "", "", "Vend", "", 0);
        vendor.setAddress(addr);

      } else {
        throw new UserException("Unable to get the vendor - Check Id");
      }

      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      vendor = null;
      throw new UserException("In VendorBean - Unable to get the Vendor <BR/>" + e);
    }

    return vendor;

  }

  public static ArrayList<LabelValueBean> getAllVendors() {

    ArrayList<LabelValueBean> list = new ArrayList<LabelValueBean>();

    Connection con = DBInterfaceLocal.getSQLConnection();

    try {

      PreparedStatement pstmt =
          con.prepareStatement("select SupplierId, CompanyName from Vendors ORDER BY 2");
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String id = rs.getInt("SupplierId") + "";
        String name = rs.getString("CompanyName");
        LabelValueBean label = new LabelValueBean(name, id);

        list.add(label);
      }

      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
    }

    return list;

  }

  public static int getMaxSupplierId() {
    int newSupNo = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select max(SupplierId) from Vendors ");
      if (rs.next()) {
        newSupNo = rs.getInt(1) + 1;
      } else {
        logger.error("In VendorBean - No New Supplier Id");
      }

      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In VendorBean - Unable to get New Supplier Id - " + e);
    }
    return newSupNo;
  }

}
