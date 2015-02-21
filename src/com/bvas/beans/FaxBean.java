package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;

public class FaxBean implements Serializable {
  private static final Logger logger = Logger.getLogger(FaxBean.class);

  private int faxNumber;

  private String faxDate = null;

  private String toWhom = null;

  private String fromWhom = null;

  private String faxTo = null;

  private String phoneTo = null;

  private int pages;

  private String attention = null;

  private String comments = null;

  private int commentsSize;

  private int priority;

  public int getFaxNumber() {
    return (this.faxNumber);
  }

  public String getFaxDate() {
    return (this.faxDate);
  }

  public String getToWhom() {
    return (this.toWhom);
  }

  public String getFromWhom() {
    return (this.fromWhom);
  }

  public String getFaxTo() {
    return (this.faxTo);
  }

  public String getPhoneTo() {
    return (this.phoneTo);
  }

  public int getPages() {
    return (this.pages);
  }

  public String getAttention() {
    return (this.attention);
  }

  public String getComments() {
    return (this.comments);
  }

  public int getPriority() {
    return (this.priority);
  }

  public int getCommentsSize() {
    return (this.commentsSize);
  }

  public void setFaxNumber(int faxNumber) {
    this.faxNumber = faxNumber;
  }

  public void setFaxDate(String faxDate) {
    this.faxDate = faxDate;
  }

  public void setToWhom(String toWhom) {
    this.toWhom = toWhom;
  }

  public void setFromWhom(String fromWhom) {
    this.fromWhom = fromWhom;
  }

  public void setFaxTo(String faxTo) {
    this.faxTo = faxTo;
  }

  public void setPhoneTo(String phoneTo) {
    this.phoneTo = phoneTo;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public void setAttention(String attention) {
    this.attention = attention;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public void setCommentsSize(int commentsSize) {
    this.commentsSize = commentsSize;
  }

  public static int getNewFaxNo() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    int newFaxNo = 0;
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT max(faxNumber) from Fax");
      if (rs.next()) {
        newFaxNo = rs.getInt(1) + 1;
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("In FaxBean - No Max No Found" + e);
    }

    return newFaxNo;
  }

  public static FaxBean getFax(int faxNo) {
    FaxBean fax = null;

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      fax = new FaxBean();
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * from fax WHERE FaxNumber=" + faxNo);
      if (rs.next()) {
        fax.setFaxNumber(rs.getInt("FaxNumber"));
        fax.setFaxDate(DateUtils.convertMySQLToUSFormat(rs.getString("FaxDate")));
        fax.setToWhom(rs.getString("ToWhom"));
        fax.setFromWhom(rs.getString("FromWhom"));
        fax.setFaxTo(rs.getString("FaxTo"));
        fax.setPhoneTo(rs.getString("PhoneTo"));
        fax.setPages(rs.getInt("Pages"));
        fax.setPriority(rs.getInt("Priority"));
        fax.setAttention(rs.getString("Attention"));
        fax.setComments(rs.getString("Comments"));
        fax.setCommentsSize(rs.getInt("CommentsSize"));
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In FaxBean - Fax Not found for fax No: " + faxNo + " - " + e);
      fax = null;
    }
    return fax;
  }

  public void writeFax() {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select * from fax where FaxNumber=" + getFaxNumber());
      if (rs.next()) {
        stmt.execute("DELETE FROM Fax WHERE FaxNumber=" + getFaxNumber());
      }
      String sql = "INSERT INTO Fax ( ";
      sql += "FaxNumber, faxDate, toWhom, FromWhom, FaxTo, PhoneTo, ";
      sql += "Pages, Priority, Attention, Comments, CommentsSize";
      sql += " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement pstmt = con.prepareStatement(sql);
      pstmt.clearParameters();

      pstmt.setInt(1, getFaxNumber());
      pstmt.setString(2, DateUtils.convertUSToMySQLFormat(getFaxDate()));
      pstmt.setString(3, getToWhom());
      pstmt.setString(4, getFromWhom());
      pstmt.setString(5, getFaxTo());
      pstmt.setString(6, getPhoneTo());
      pstmt.setInt(7, getPages());
      pstmt.setInt(8, getPriority());
      pstmt.setString(9, getAttention());
      pstmt.setString(10, getComments());
      pstmt.setInt(11, getCommentsSize());

      if (getComments().indexOf("\n") != -1) {
        logger.error("New lines found in FaxBean");
      } else {
        logger.error("New lines NOT found in FaxBean");
      }

      pstmt.execute();

      rs.close();
      pstmt.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In FaxBean - Unable to write Fax " + e);
    }
  }

  public void removeFax() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      stmt.execute("DELETE FROM Fax WHERE FaxNumber=" + getFaxNumber());

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("In FaxBean - Unable to delete the Fax" + e);
    }

  }

}
