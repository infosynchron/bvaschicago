package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.UserException;

public class AddressBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = Logger.getLogger(AddressBean.class);

  private String id = null;

  private String type = null;

  private String who = null;

  private String dateCreated = null;

  private int invoiceNumber;

  private String address1 = null;

  private String address2 = null;

  private String city = null;

  private String state = null;

  private String region = null;

  private String postalCode = null;

  private String country = null;

  private String phone = null;

  private String ext = null;

  private String fax = null;

  public String getId() {
    return (this.id);
  }

  public String getType() {
    return (this.type);
  }

  public String getWho() {
    return (this.who);
  }

  public String getDateCreated() {
    return (this.dateCreated);
  }

  public int getInvoiceNumber() {
    return (this.invoiceNumber);
  }

  public String getAddress1() {
    return (this.address1);
  }

  public String getAddress2() {
    return (this.address2);
  }

  public String getCity() {
    return (this.city);
  }

  public String getState() {
    return (this.state);
  }

  public String getRegion() {
    return (this.region);
  }

  public String getPostalCode() {
    return (this.postalCode);
  }

  public String getCountry() {
    return (this.country);
  }

  public String getPhone() {
    return (this.phone);
  }

  public String getExt() {
    return (this.ext);
  }

  public String getFax() {
    return (this.fax);
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setWho(String who) {
    this.who = who;
  }

  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  public void setInvoiceNumber(int invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public static AddressBean getAddress(String id, String type, String who, String dateCreated,
      int invNo) throws UserException {
    AddressBean addBean = new AddressBean();
    Connection con1 = DBInterfaceLocal.getSQLConnection();
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    try {
      StringBuffer addressSQL = new StringBuffer("");
      addressSQL.append("select * from Address where ID = ? and type = ? and who = ? ");
      if (dateCreated != null && !dateCreated.trim().equals("")) {
        addressSQL.append(" and dateCreated = ?");
        if (invNo != 0) {
          addressSQL.append(" and invoiceNumber = ?");
        }
      } else {
        if (invNo != 0) {
          addressSQL.append(" and invoiceNumber = ?");
        }
      }
      pStmt = con1.prepareStatement(addressSQL.toString());
      if (type.trim().equalsIgnoreCase("Bill")) {
        pStmt.clearParameters();
        pStmt.setString(1, id);
        pStmt.setString(2, "Standard");
        pStmt.setString(3, who);
        if (dateCreated != null && !dateCreated.trim().equals("")) {
          pStmt.setString(4, DateUtils.convertUSToMySQLFormat(dateCreated));
          if (invNo != 0) {
            pStmt.setInt(5, 0);
          }
        } else {
          if (invNo != 0) {
            pStmt.setInt(4, 0);
          }
        }
      } else {
        pStmt.clearParameters();
        pStmt.setString(1, id);
        pStmt.setString(2, type);
        pStmt.setString(3, who);
        if (dateCreated != null && !dateCreated.trim().equals("")) {
          pStmt.setString(4, DateUtils.convertUSToMySQLFormat(dateCreated));
          if (invNo != 0) {
            pStmt.setInt(5, invNo);
          }
        } else {
          if (invNo != 0) {
            pStmt.setInt(4, invNo);
          }
        }
      }

      rs = pStmt.executeQuery();
      if (rs.next()) {
        addBean.setId(rs.getString("ID"));
        addBean.setType(rs.getString("type"));
        addBean.setWho(rs.getString("who"));
        addBean.setDateCreated(DateUtils.convertMySQLToUSFormat(rs.getString("dateCreated")));
        addBean.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        addBean.setAddress1(rs.getString("Addr1"));
        addBean.setAddress2(rs.getString("Addr2"));
        addBean.setCity(rs.getString("City"));
        addBean.setState(rs.getString("State"));
        addBean.setRegion(rs.getString("Region"));
        addBean.setPostalCode(rs.getString("PostalCode"));
        addBean.setCountry(rs.getString("Country"));
        addBean.setPhone(rs.getString("Phone"));
        addBean.setExt(rs.getString("Ext"));
        addBean.setFax(rs.getString("Fax"));
      } else {
        addBean.setId(id);
        addBean.setType(type);
        addBean.setWho(who);
        addBean.setDateCreated("");
        addBean.setInvoiceNumber(invNo);
        addBean.setAddress1("");
        addBean.setAddress2("");
        addBean.setCity("");
        addBean.setState("");
        addBean.setRegion("");
        addBean.setPostalCode("");
        addBean.setCountry("");
        addBean.setPhone("");
        addBean.setExt("");
        addBean.setFax("");
      }
      rs.close();
      pStmt.close();
      con1.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Address<BR/>" + e);
    }
    return addBean;
  }

  public static AddressBean getAddress(String id, String type, String who, String dateCreated,
      int invNo, Connection con) throws UserException {
    AddressBean addBean = new AddressBean();
    // Connection con = DBInterfaceLocal.getMySQLConnection();
    try {
      StringBuffer addressSQL = new StringBuffer("");
      addressSQL.append("select * from Address where ID = ? and type = ? and who = ? ");
      if (dateCreated != null && !dateCreated.trim().equals("")) {
        addressSQL.append(" and dateCreated = ?");
        if (invNo != 0) {
          addressSQL.append(" and invoiceNumber = ?");
        }
      } else {
        if (invNo != 0) {
          addressSQL.append(" and invoiceNumber = ?");
        }
      }
      PreparedStatement pStmt = con.prepareStatement(addressSQL.toString());
      if (type.trim().equalsIgnoreCase("Bill")) {
        pStmt.clearParameters();
        pStmt.setString(1, id);
        pStmt.setString(2, "Standard");
        pStmt.setString(3, who);
        if (dateCreated != null && !dateCreated.trim().equals("")) {
          pStmt.setString(4, DateUtils.convertUSToMySQLFormat(dateCreated));
          if (invNo != 0) {
            pStmt.setInt(5, 0);
          }
        } else {
          if (invNo != 0) {
            pStmt.setInt(4, 0);
          }
        }
      } else {
        pStmt.clearParameters();
        pStmt.setString(1, id);
        pStmt.setString(2, type);
        pStmt.setString(3, who);
        if (dateCreated != null && !dateCreated.trim().equals("")) {
          pStmt.setString(4, DateUtils.convertUSToMySQLFormat(dateCreated));
          if (invNo != 0) {
            pStmt.setInt(5, invNo);
          }
        } else {
          if (invNo != 0) {
            pStmt.setInt(4, invNo);
          }
        }
      }

      ResultSet rs = pStmt.executeQuery();
      if (rs.next()) {
        addBean.setId(rs.getString("ID"));
        addBean.setType(rs.getString("type"));
        addBean.setWho(rs.getString("who"));
        addBean.setDateCreated(DateUtils.convertMySQLToUSFormat(rs.getString("dateCreated")));
        addBean.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        addBean.setAddress1(rs.getString("Addr1"));
        addBean.setAddress2(rs.getString("Addr2"));
        addBean.setCity(rs.getString("City"));
        addBean.setState(rs.getString("State"));
        addBean.setRegion(rs.getString("Region"));
        addBean.setPostalCode(rs.getString("PostalCode"));
        addBean.setCountry(rs.getString("Country"));
        addBean.setPhone(rs.getString("Phone"));
        addBean.setExt(rs.getString("Ext"));
        addBean.setFax(rs.getString("Fax"));
      } else {
        addBean.setId(id);
        addBean.setType(type);
        addBean.setWho(who);
        addBean.setDateCreated("");
        addBean.setInvoiceNumber(invNo);
        addBean.setAddress1("");
        addBean.setAddress2("");
        addBean.setCity("");
        addBean.setState("");
        addBean.setRegion("");
        addBean.setPostalCode("");
        addBean.setCountry("");
        addBean.setPhone("");
        addBean.setExt("");
        addBean.setFax("");
      }

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to get the Address<BR/>" + e);
    }
    return addBean;
  }

  public void addNewAddress() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer insertSQL = new StringBuffer("");
      insertSQL.append("INSERT INTO Address (");
      insertSQL.append("Id, Type, Who, DateCreated, Addr1, Addr2, City, State, Region, ");
      insertSQL
          .append("PostalCode, Country, Phone, Ext, Fax, InvoiceNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

      boolean emptyAddress = false;
      if (getType().trim().equalsIgnoreCase("Ship") && getAddress1().trim().equals("")
          && getAddress2().trim().equals("") && getCity().trim().equals("")
          && getState().trim().equals("") && getRegion().trim().equals("")
          && getPostalCode().trim().equals("") && getCountry().trim().equals("")
          && getPhone().trim().equals("") && getExt().trim().equals("")
          && getFax().trim().equals("")) {
        emptyAddress = true;
      }
      if (getType().trim().equalsIgnoreCase("Bill")) {
        emptyAddress = true;
      }
      if (!emptyAddress) {
        PreparedStatement pstmt = con.prepareStatement(insertSQL.toString());
        pstmt.clearParameters();
        pstmt.setString(1, getId());
        pstmt.setString(2, getType());
        pstmt.setString(3, getWho());
        pstmt.setString(4, DateUtils.convertUSToMySQLFormat(getDateCreated()));
        pstmt.setString(5, getAddress1());
        pstmt.setString(6, getAddress2());
        pstmt.setString(7, getCity());
        pstmt.setString(8, getState());
        pstmt.setString(9, getRegion());
        pstmt.setString(10, getPostalCode());
        pstmt.setString(11, getCountry());
        pstmt.setString(12, getPhone());
        pstmt.setString(13, getExt());
        pstmt.setString(14, getFax());
        pstmt.setInt(15, getInvoiceNumber());
        pstmt.execute();
        pstmt.close();
      }
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      logger.error("In AddressBean - Unable to add the Address" + e);
    }

  }

  public void changeAddress() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      if (!getType().trim().equalsIgnoreCase("Bill")) {
        if (!isAvailable()) {
          // logger.error("This " + getType() +
          // " address is not available");
          addNewAddress();
        } else {
          // logger.error("This " + getType() +
          // " address is available");
          StringBuffer changeSQL = new StringBuffer("");
          changeSQL.append("UPDATE Address set ");
          changeSQL.append("DateCreated=?,");
          changeSQL.append("Addr1=?, ");
          changeSQL.append("Addr2=?, ");
          changeSQL.append("City=?, ");
          changeSQL.append("State=?, ");
          changeSQL.append("Region=?, ");
          changeSQL.append("PostalCode=?, ");
          changeSQL.append("Country=?, ");
          changeSQL.append("Phone=?, ");
          changeSQL.append("Ext=?, ");
          changeSQL.append("Fax=?");
          changeSQL.append(" WHERE Id=? AND Type=? AND Who=? ");
          if (getInvoiceNumber() != 0) {
            changeSQL.append(" AND InvoiceNumber=?");
          }
          PreparedStatement pstmt = con.prepareStatement(changeSQL.toString());

          pstmt.clearParameters();
          pstmt.setString(1, DateUtils.convertUSToMySQLFormat(getDateCreated()));
          pstmt.setString(2, getAddress1());
          pstmt.setString(3, getAddress2());
          pstmt.setString(4, getCity());
          pstmt.setString(5, getState());
          pstmt.setString(6, getRegion());
          pstmt.setString(7, getPostalCode());
          pstmt.setString(8, getCountry());
          pstmt.setString(9, getPhone());
          pstmt.setString(10, getExt());
          pstmt.setString(11, getFax());

          pstmt.setString(12, getId());
          pstmt.setString(13, getType());
          pstmt.setString(14, getWho());
          if (getInvoiceNumber() != 0) {
            pstmt.setInt(15, getInvoiceNumber());
          }

          pstmt.executeUpdate();
          pstmt.close();
        }
      }
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In AddressBean - Unable to change the Address<BR/>" + e);
    }

  }

  public void deleteAddress() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer deleteSQL = new StringBuffer("");
      deleteSQL.append("DELETE FROM Address WHERE Id=? AND Who=?");
      PreparedStatement pstmt = con.prepareStatement(deleteSQL.toString());
      pstmt.clearParameters();
      pstmt.setString(1, getId());
      pstmt.setString(2, getWho());

      pstmt.execute();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      logger.error("In AddressBean - Unable to delete the Address" + e);
    }
  }

  public boolean isAvailable() {
    boolean available = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer checkSQL = new StringBuffer("");
      checkSQL.append("SELECT * FROM Address WHERE Id=? AND Who=? AND Type=?");
      if (getInvoiceNumber() != 0) {
        checkSQL.append(" AND InvoiceNumber=?");
      }
      PreparedStatement pstmt = con.prepareStatement(checkSQL.toString());
      pstmt.clearParameters();
      pstmt.setString(1, getId());
      pstmt.setString(2, getWho());
      pstmt.setString(3, getType());
      if (getInvoiceNumber() != 0) {
        pstmt.setString(4, getInvoiceNumber() + "");
      }

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
      logger.error("In AddressBean - Unable to delete the Address" + e);
    }
    return available;
  }
}
