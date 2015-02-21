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
import com.bvas.utils.UserException;

public class VendorItemBean implements Serializable {
  private static final Logger logger = Logger.getLogger(VendorItemBean.class);

  private int supplierId;

  private String partNo = null;

  private String vendorPartNo = null;

  private String itemDesc1 = null;

  private String itemDesc2 = null;

  private String plNo = null;

  private String oemNo = null;

  private double sellingRate;

  private int noOfPieces;

  private double itemSize;

  private String itemSizeUnits;

  public int getSupplierId() {
    return (this.supplierId);
  }

  public String getPartNo() {
    return (this.partNo);
  }

  public String getVendorPartNo() {
    return (this.vendorPartNo);
  }

  public String getItemDesc1() {
    return (this.itemDesc1);
  }

  public String getItemDesc2() {
    return (this.itemDesc2);
  }

  public String getPlNo() {
    return (this.plNo);
  }

  public String getOemNo() {
    return (this.oemNo);
  }

  public double getSellingRate() {
    return (this.sellingRate);
  }

  public int getNoOfPieces() {
    return (this.noOfPieces);
  }

  public double getItemSize() {
    return (this.itemSize);
  }

  public String getItemSizeUnits() {
    return (this.itemSizeUnits);
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setVendorPartNo(String vendorPartNo) {
    this.vendorPartNo = vendorPartNo;
  }

  public void setItemDesc1(String itemDesc1) {
    this.itemDesc1 = itemDesc1;
  }

  public void setItemDesc2(String itemDesc2) {
    this.itemDesc2 = itemDesc2;
  }

  public void setPlNo(String plNo) {
    this.plNo = plNo;
  }

  public void setOemNo(String oemNo) {
    this.oemNo = oemNo;
  }

  public void setSellingRate(double sellingRate) {
    this.sellingRate = sellingRate;
  }

  public void setNoOfPieces(int noOfPieces) {
    this.noOfPieces = noOfPieces;
  }

  public void setItemSize(double itemSize) {
    this.itemSize = itemSize;
  }

  public void setItemSizeUnits(String itemSizeUnits) {
    this.itemSizeUnits = itemSizeUnits;
  }

  public void addNewItem() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer insertSQL = new StringBuffer("");
      insertSQL.append("INSERT INTO VendorItems (");
      insertSQL.append("SupplierId, PartNo, VendorPartNo, ItemDesc1, ItemDesc2, PLNo, OEMNo, ");
      insertSQL
          .append("SellingRate, NoOfPieces, ItemSize, ItemSizeUnits) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

      PreparedStatement pstmt = con.prepareStatement(insertSQL.toString());
      pstmt.clearParameters();
      pstmt.setInt(1, getSupplierId());
      pstmt.setString(2, getPartNo());
      pstmt.setString(3, getVendorPartNo());
      pstmt.setString(4, getItemDesc1());
      pstmt.setString(5, getItemDesc2());
      pstmt.setString(6, getPlNo());
      pstmt.setString(7, getOemNo());
      pstmt.setDouble(8, getSellingRate());
      pstmt.setInt(9, getNoOfPieces());
      pstmt.setDouble(10, getItemSize());
      pstmt.setString(11, getItemSizeUnits());

      pstmt.execute();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In VendorItemBean - Unable to add the Item<BR/>" + e);
    }

  }

  public void changeItem() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer changeSQL = new StringBuffer("");
      changeSQL.append("UPDATE VendorItems set ");
      changeSQL.append("PartNo=?, ");
      changeSQL.append("ItemDesc1=?,");
      changeSQL.append("ItemDesc2=?, ");
      changeSQL.append("PLNo=?, ");
      changeSQL.append("OemNo=?, ");
      changeSQL.append("SellingRate=?, ");
      changeSQL.append("NoOfPieces=?, ");
      changeSQL.append("ItemSize=?, ");
      changeSQL.append("ItemSizeUnits=?");
      changeSQL.append(" WHERE (SupplierId=? AND VendorPartNo=?)");
      PreparedStatement pstmt = con.prepareStatement(changeSQL.toString());

      pstmt.clearParameters();
      pstmt.setString(1, getPartNo());
      pstmt.setString(2, getItemDesc1());
      pstmt.setString(3, getItemDesc2());
      pstmt.setString(4, getPlNo());
      pstmt.setString(5, getOemNo());
      pstmt.setDouble(6, getSellingRate());
      pstmt.setInt(7, getNoOfPieces());
      pstmt.setDouble(8, getItemSize());
      pstmt.setString(9, getItemSizeUnits());
      pstmt.setInt(10, getSupplierId());
      pstmt.setString(11, getVendorPartNo());

      pstmt.executeUpdate();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In VendorItemBean - Unable to change the Item<BR/>" + e);
    }

  }

  public void deleteItem() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      boolean ourPartNoAvail = false;
      if (getPartNo() != null && !getPartNo().trim().equalsIgnoreCase("null")) {
        ourPartNoAvail = true;
      }
      StringBuffer deleteSQL = new StringBuffer("");
      deleteSQL.append("DELETE FROM VendorItems WHERE (SupplierId=? AND VendorPartNo=? ");
      if (ourPartNoAvail) {
        deleteSQL.append(" AND PartNo=? ");
      }
      deleteSQL.append(")");
      PreparedStatement pstmt = con.prepareStatement(deleteSQL.toString());
      pstmt.clearParameters();
      pstmt.setInt(1, getSupplierId());
      pstmt.setString(2, getVendorPartNo());
      if (ourPartNoAvail) {
        pstmt.setString(3, getPartNo());
      }

      pstmt.execute();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In VendorItemBean - Unable to delete the Item<BR/>" + e);
    }
  }

  public static VendorItemBean getThePart(int supId, String partNum, String VendPartNum,
      Connection con1) throws UserException {
    VendorItemBean viBean = null;
    if (con1 == null) {
      con1 = DBInterfaceLocal.getSQLConnection();
    }
    Connection con2 = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer selectSQL = new StringBuffer("");
      selectSQL
          .append("SELECT * FROM VendorItems WHERE (SupplierId=? AND (PartNo like ? OR VendorPartNo=?))");
      PreparedStatement pstmt = con2.prepareStatement(selectSQL.toString());
      pstmt.clearParameters();
      if (partNum == null || partNum.trim().equals(""))
        partNum = "xyz";
      if (VendPartNum == null || VendPartNum.trim().equals(""))
        VendPartNum = "xyz";
      pstmt.setInt(1, supId);
      pstmt.setString(2, partNum + "%");
      pstmt.setString(3, VendPartNum);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        viBean = new VendorItemBean();
        viBean.setSupplierId(rs.getInt("SupplierId"));
        viBean.setPartNo(rs.getString("PartNo"));
        viBean.setVendorPartNo(rs.getString("VendorPartNo"));
        viBean.setItemDesc1(rs.getString("ItemDesc1"));
        viBean.setItemDesc2(rs.getString("ItemDesc2"));
        viBean.setPlNo(rs.getString("PlNo"));
        viBean.setOemNo(rs.getString("OEMNo"));
        viBean.setSellingRate(rs.getDouble("SellingRate"));
        viBean.setItemSize(rs.getDouble("ItemSize"));
        viBean.setNoOfPieces(rs.getInt("NoOfPieces"));
        viBean.setItemSizeUnits(rs.getString("ItemSizeUnits"));
      } else {
        viBean = null;
      }

      rs.close();
      pstmt.close();
      con2.close();
    } catch (SQLException e) {
      logger.error(e);
      viBean = null;
    }
    return viBean;
  }

  public static VendorItemBean getVendorPart(int supId, String partNum, String VendPartNum)
      throws UserException {
    VendorItemBean viBean = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();

      StringBuffer selectSQL = new StringBuffer("");
      selectSQL.append("SELECT * FROM VendorItems WHERE ");

      // logger.error(partNum);
      // logger.error(VendPartNum);
      selectSQL.append(" SupplierId=" + supId);
      if (partNum != null && !partNum.trim().equals("")) {
        selectSQL.append(" AND PartNo like '" + partNum + "%'");
      }

      if (VendPartNum != null && !VendPartNum.trim().equals("")) {
        selectSQL.append(" AND VendorPartNo='" + VendPartNum + "'");
      }
      // logger.error(selectSQL);
      ResultSet rs = stmt.executeQuery(selectSQL.toString());
      if (rs.next()) {
        viBean = new VendorItemBean();
        viBean.setSupplierId(rs.getInt("SupplierId"));
        viBean.setPartNo(rs.getString("PartNo"));
        viBean.setVendorPartNo(rs.getString("VendorPartNo"));
        viBean.setItemDesc1(rs.getString("ItemDesc1"));
        viBean.setItemDesc2(rs.getString("ItemDesc2"));
        viBean.setPlNo(rs.getString("PlNo"));
        viBean.setOemNo(rs.getString("OEMNo"));
        viBean.setSellingRate(rs.getDouble("SellingRate"));
        viBean.setItemSize(rs.getDouble("ItemSize"));
        viBean.setNoOfPieces(rs.getInt("NoOfPieces"));
        viBean.setItemSizeUnits(rs.getString("ItemSizeUnits"));
      } else {
        viBean = null;
      }

      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      viBean = null;
    }
    return viBean;
  }

  public static Vector<VendorItemBean> getAllParts(int supId) throws UserException {
    Vector<VendorItemBean> v = new Vector<VendorItemBean>();
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer selectSQL = new StringBuffer("");
      selectSQL.append("SELECT * FROM VendorItems WHERE SupplierId=?");
      PreparedStatement pstmt = con.prepareStatement(selectSQL.toString());
      pstmt.clearParameters();
      pstmt.setInt(1, supId);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        VendorItemBean viBean = new VendorItemBean();

        viBean = new VendorItemBean();
        viBean.setSupplierId(rs.getInt("SupplierId"));
        viBean.setPartNo(rs.getString("PartNo"));
        viBean.setVendorPartNo(rs.getString("VendorPartNo"));
        viBean.setItemDesc1(rs.getString("ItemDesc1"));
        viBean.setItemDesc2(rs.getString("ItemDesc2"));
        viBean.setPlNo(rs.getString("PlNo"));
        viBean.setOemNo(rs.getString("OEMNo"));
        viBean.setSellingRate(rs.getDouble("SellingRate"));
        viBean.setItemSize(rs.getDouble("ItemSize"));
        viBean.setNoOfPieces(rs.getInt("NoOfPieces"));
        viBean.setItemSizeUnits(rs.getString("ItemSizeUnits"));

        v.add(viBean);

      }

      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In VendorItemBean - Error when getting the parts<BR/>" + e);
    }
    return v;
  }

  public static Vector<VendorItemBean> getMatchedParts(int supId) throws UserException {
    Vector<VendorItemBean> v = new Vector<VendorItemBean>();
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer selectSQL = new StringBuffer("");
      selectSQL.append("SELECT * FROM VendorItems WHERE SupplierId=? and PartNo!='' ");
      PreparedStatement pstmt = con.prepareStatement(selectSQL.toString());
      pstmt.clearParameters();
      pstmt.setInt(1, supId);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        VendorItemBean viBean = new VendorItemBean();

        viBean = new VendorItemBean();
        viBean.setSupplierId(rs.getInt("SupplierId"));
        viBean.setPartNo(rs.getString("PartNo"));
        viBean.setVendorPartNo(rs.getString("VendorPartNo"));
        viBean.setItemDesc1(rs.getString("ItemDesc1"));
        viBean.setItemDesc2(rs.getString("ItemDesc2"));
        viBean.setPlNo(rs.getString("PlNo"));
        viBean.setOemNo(rs.getString("OEMNo"));
        viBean.setSellingRate(rs.getDouble("SellingRate"));
        viBean.setItemSize(rs.getDouble("ItemSize"));
        viBean.setNoOfPieces(rs.getInt("NoOfPieces"));
        viBean.setItemSizeUnits(rs.getString("ItemSizeUnits"));

        v.add(viBean);

      }

      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In VendorItemBean - Error when getting the parts<BR/>" + e);
    }
    return v;
  }

  public static Vector<VendorItemBean> getUnMatchedParts(int supId) throws UserException {
    Vector<VendorItemBean> v = new Vector<VendorItemBean>();
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer selectSQL = new StringBuffer("");
      selectSQL.append("SELECT * FROM VendorItems WHERE SupplierId=? and partno='' ");
      PreparedStatement pstmt = con.prepareStatement(selectSQL.toString());
      pstmt.clearParameters();
      pstmt.setInt(1, supId);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        VendorItemBean viBean = new VendorItemBean();

        viBean = new VendorItemBean();
        viBean.setSupplierId(rs.getInt("SupplierId"));
        viBean.setPartNo(rs.getString("PartNo"));
        viBean.setVendorPartNo(rs.getString("VendorPartNo"));
        viBean.setItemDesc1(rs.getString("ItemDesc1"));
        viBean.setItemDesc2(rs.getString("ItemDesc2"));
        viBean.setPlNo(rs.getString("PlNo"));
        viBean.setOemNo(rs.getString("OEMNo"));
        viBean.setSellingRate(rs.getDouble("SellingRate"));
        viBean.setItemSize(rs.getDouble("ItemSize"));
        viBean.setNoOfPieces(rs.getInt("NoOfPieces"));
        viBean.setItemSizeUnits(rs.getString("ItemSizeUnits"));

        v.add(viBean);

      }

      rs.close();
      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In VendorItemBean - Error when getting the parts<BR/>" + e);
    }
    return v;
  }
}
