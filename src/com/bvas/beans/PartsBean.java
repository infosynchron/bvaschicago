package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.formBeans.OurPartDetailsForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.UserException;

public class PartsBean implements Serializable {
  private static final Logger logger = Logger.getLogger(PartsBean.class);

  private String partNo = null;

  private String interchangeNo = null;

  private String partDescription = null;

  private String makeModelCode = null;

  private String year = null;

  private int supplierId;

  private int orderNo;

  private double costPrice;

  private double listPrice;

  private double actualPrice;

  private int unitsInStock;

  private int unitsOnOrder;

  private int reorderLevel;

//  private long reorderLevelDate;

//  private double compPrice1;
//
//  private double compPrice2;
//
//  private double compPrice3;

  private String keystoneNumber = null;

  private String oemNumber = null;

  private String location = null;

  private String subCategory = null;

  public String getPartNo() {
    return (this.partNo);
  }

  public String getInterchangeNo() {
    return (this.interchangeNo);
  }

  public String getPartDescription() {
    return (this.partDescription);
  }

  public String getMakeModelCode() {
    return (this.makeModelCode);
  }

  public String getYear() {
    return (this.year);
  }

  public int getSupplierId() {
    return (this.supplierId);
  }

  public int getOrderNo() {
    return (this.orderNo);
  }

  public double getCostPrice() {
    if (this.costPrice <= this.actualPrice) {
      this.costPrice = 0;
      this.listPrice = 0;
    }
    return (this.costPrice);
  }

  public double getCostPrice(int custLvl) {
    double perc = 0;
    int givingPerc = 0;
    double newCost = 0.0;
    if (custLvl != 0 && getCostPrice() != 0 && getActualPrice() != 0) {
      perc = ((getCostPrice() - getActualPrice()) / getCostPrice()) * 100;
      if (perc > 75) {
        switch (custLvl) {
          case 1:
            givingPerc = 4;
            break;
          case 2:
            givingPerc = 8;
            break;
          case 3:
            givingPerc = 12;
            break;
          case 4:
            givingPerc = 17;
            break;
          case 5:
            givingPerc = 28;
            break;
          case 6:
            givingPerc = 32;
            break;
          case 7:
            givingPerc = 36;
            break;
          case 8:
            givingPerc = 40;
            break;
          case 9:
            givingPerc = 44;
            break;
          // case 10: givingPerc = 50; break;
          default:
            givingPerc = 0;
            break;
        }
      } else if (perc > 60) {
        switch (custLvl) {
          case 1:
            givingPerc = 3;
            break;
          case 2:
            givingPerc = 6;
            break;
          case 3:
            givingPerc = 10;
            break;
          case 4:
            givingPerc = 13;
            break;
          case 5:
            givingPerc = 20;
            break;
          case 6:
            givingPerc = 24;
            break;
          case 7:
            givingPerc = 28;
            break;
          case 8:
            givingPerc = 32;
            break;
          case 9:
            givingPerc = 36;
            break;
          // case 10: givingPerc = 40; break;
          default:
            givingPerc = 0;
            break;
        }
      } else if (perc > 45) {
        switch (custLvl) {
          case 1:
            givingPerc = 2;
            break;
          case 2:
            givingPerc = 4;
            break;
          case 3:
            givingPerc = 7;
            break;
          case 4:
            givingPerc = 9;
            break;
          case 5:
            givingPerc = 13;
            break;
          case 6:
            givingPerc = 16;
            break;
          case 7:
            givingPerc = 20;
            break;
          case 8:
            givingPerc = 24;
            break;
          case 9:
            givingPerc = 28;
            break;
          // case 10: givingPerc = 30; break;
          default:
            givingPerc = 0;
            break;
        }
      } else if (perc > 30) {
        switch (custLvl) {
          case 1:
            givingPerc = 2;
            break;
          case 2:
            givingPerc = 4;
            break;
          case 3:
            givingPerc = 4;
            break;
          case 4:
            givingPerc = 5;
            break;
          case 5:
            givingPerc = 11;
            break;
          case 6:
            givingPerc = 13;
            break;
          case 7:
            givingPerc = 15;
            break;
          case 8:
            givingPerc = 17;
            break;
          case 9:
            givingPerc = 19;
            break;
          // case 10: givingPerc = 20; break;
          default:
            givingPerc = 0;
            break;
        }
      } else if (perc > 15) {
        switch (custLvl) {
          case 1:
            givingPerc = 1;
            break;
          case 2:
            givingPerc = 2;
            break;
          case 3:
            givingPerc = 3;
            break;
          case 4:
            givingPerc = 4;
            break;
          case 5:
            givingPerc = 6;
            break;
          case 6:
            givingPerc = 7;
            break;
          case 7:
            givingPerc = 8;
            break;
          case 8:
            givingPerc = 10;
            break;
          case 9:
            givingPerc = 12;
            break;
          // case 10: givingPerc = 10; break;
          default:
            givingPerc = 0;
            break;
        }

      } else if (perc > 10) {
        switch (custLvl) {
          case 5:
            givingPerc = 3;
            break;
          case 6:
            givingPerc = 4;
            break;
          case 7:
            givingPerc = 4;
            break;
          case 8:
            givingPerc = 4;
            break;
          case 9:
            givingPerc = 5;
            break;
          // case 10: givingPerc = 6; break;
          default:
            givingPerc = 0;
            break;
        }
      } else {
        givingPerc = 0;
      }

      if (givingPerc != 0) {
        newCost = getCostPrice() * (100 - givingPerc) / 100;
        newCost = Math.rint(newCost);
      } else {
        newCost = getCostPrice();
      }
      if (custLvl == 10) {
        newCost = getCostPrice() / 0.95;
        newCost = Math.rint(newCost);
      }
      if (custLvl == 11) {
        newCost = getListPrice() * 0.75;
        newCost = Math.rint(newCost);
      }
      if (custLvl == 12) {
        newCost = getListPrice() * 0.80;
        newCost = Math.rint(newCost);
      }
    } else {
      newCost = getCostPrice();
    }

    if (this.costPrice <= this.actualPrice) {
      this.costPrice = 0;
      this.listPrice = 0;
    }
    return (newCost);
  }

  public double getListPrice() {
    return (this.listPrice);
  }

  public double getActualPrice() {
    if (actualPrice == 0) {
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        if (interchangeNo == null || interchangeNo.trim().equals("")) {
          ResultSet rs =
              stmt.executeQuery("Select ActualPrice From Parts Where InterchangeNo='"
                  + partNo.trim() + "' And ActualPrice!=0");
          if (rs.next()) {
            actualPrice = rs.getDouble(1);
          }
          rs.close();
        } else {
          actualPrice = PartsBean.getPart(interchangeNo.trim(), con).getActualPrice();
        }

        stmt.close();
        con.close();
      } catch (Exception e) {
        logger.error(e);
      }
    }
    return (this.actualPrice);
  }

  public int getUnitsInStock() {
    return (this.unitsInStock);
  }

  public int getUnitsOnOrder() {
    return (this.unitsOnOrder);
  }

  public int getReorderLevel() {
    /*
     * Connection con = DBInterfaceLocal.getMySQLConnection(); //long twoMonthsBack =
     * System.currentTimeMillis() - 3888000000L; long twoMonthsBack = System.currentTimeMillis() -
     * 172800000L; long timeNow = System.currentTimeMillis(); if (reorderLevelDate < twoMonthsBack
     * || reorderLevel == 0) { if (interchangeNo == null || interchangeNo.trim().equals("")) {
     * Statement stmt = null; Statement stmt1 = null; Statement stmtX = null; ResultSet rs = null;
     * ResultSet rs1 = null; ResultSet rsX = null; // 1000 * 60 * 60 * 24 * 120 //long period =
     * 7776000000L; //long period = 13824000000L; long period = 10368000000L; //Six Months back Date
     * long oldDate = timeNow - period; java.util.Date dd = new java.util.Date(oldDate);
     * java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy"); String ddDate
     * = sdf.format(dd); //logger.error(ddDate);
     * //logger.error(DateUtils.convertUSToMySQLFormat(ddDate)); try { stmt = con.createStatement();
     * rs = stmt.executeQuery(
     * "Select sum(a.Quantity) from InvoiceDetails a, Invoice b where PartNumber='" + partNo +
     * "' and a.InvoiceNumber=b.InvoiceNumber and OrderDate >='" +
     * DateUtils.convertUSToMySQLFormat(ddDate) + "'"); if (rs.next()) { reorderLevel =
     * rs.getInt(1); } stmt1 = con.createStatement(); rs1 =
     * stmt1.executeQuery("select PartNo From Parts where InterchangeNo='" + partNo + "'"); stmtX =
     * con.createStatement(); while (rs1.next()) { rsX = null; String interPartNo =
     * rs1.getString("PartNo"); rsX = stmtX.executeQuery(
     * "Select sum(a.Quantity) from InvoiceDetails a, Invoice b where PartNumber='" + interPartNo +
     * "' and a.InvoiceNumber=b.InvoiceNumber and OrderDate >='" +
     * DateUtils.convertUSToMySQLFormat(ddDate) + "'"); if (rsX.next()) { reorderLevel +=
     * rsX.getInt(1); }
     * 
     * } } catch (SQLException e) { logger.error("Exception in getReorderLevel"+partNo+"---"+e); }
     * finally { try { if (rs == null) { logger.error("ResultSet is null in MakeModelBean"); } else
     * { rs.close(); } if (stmt == null) { logger.error("Statement is null in PartsBean"); } else {
     * stmt.close(); } } catch (Exception e) { logger.error(
     * "Exception when closing the connection in PartsBean : " + e); } } } else { try { Statement
     * stmt = con.createStatement(); ResultSet rs =
     * stmt.executeQuery("Select ReorderLevel from parts Where PartNo='" + interchangeNo + "'"); if
     * (rs.next()) { reorderLevel = rs.getInt(1); } else { reorderLevel = 0; } } catch (Exception e)
     * { reorderLevel = 0; } //reorderLevel = PartsBean.getPart(interchangeNo).getReorderLevel(); }
     * reorderLevelDate = timeNow; changePart(); }
     */
    return (this.reorderLevel);
  }

//  public long getReorderLevelDate() {
//    return (this.reorderLevelDate);
//  }

//  public double getCompPrice1() {
//    return (this.compPrice1);
//  }
//
//  public double getCompPrice2() {
//    return (this.compPrice2);
//  }
//
//  public double getCompPrice3() {
//    return (this.compPrice3);
//  }

  public String getKeystoneNumber() {
    return (this.keystoneNumber);
  }

  public String getOemNumber() {
    return (this.oemNumber);
  }

  public String getLocation() {
    if (this.location == null || this.location.trim().equalsIgnoreCase("null")) {
      this.location = "";
    }
    return (this.location);
  }

  public String getSubCategory() {
    return (this.subCategory);
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setInterchangeNo(String interchangeNo) {
    this.interchangeNo = interchangeNo;
  }

  public void setPartDescription(String partDescription) {
    this.partDescription = partDescription;
  }

  public void setMakeModelCode(String makeModelCode) {
    this.makeModelCode = makeModelCode;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  public void setOrderNo(int orderNo) {
    this.orderNo = orderNo;
  }

  public void setCostPrice(double costPrice) {
    this.costPrice = costPrice;
  }

  public void setListPrice(double listPrice) {
    this.listPrice = listPrice;
  }

  public void setActualPrice(double actualPrice) {
    this.actualPrice = actualPrice;
  }

  public void setUnitsInStock(int unitsInStock) {
    this.unitsInStock = unitsInStock;
  }

  public void setUnitsOnOrder(int unitsOnOrder) {
    this.unitsOnOrder = unitsOnOrder;
  }

  public void setReorderLevel(int reorderLevel) {
    this.reorderLevel = reorderLevel;
  }

//  public void setReorderLevelDate(long reorderLevelDate) {
//    this.reorderLevelDate = reorderLevelDate;
//  }

//  public void setCompPrice1(double compPrice1) {
//    this.compPrice1 = compPrice1;
//  }
//
//  public void setCompPrice2(double compPrice2) {
//    this.compPrice2 = compPrice2;
//  }
//
//  public void setCompPrice3(double compPrice3) {
//    this.compPrice3 = compPrice3;
//  }

  public void setKeystoneNumber(String keystoneNumber) {
    this.keystoneNumber = keystoneNumber;
  }

  public void setOemNumber(String oemNumber) {
    this.oemNumber = oemNumber;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setSubCategory(String subCategory) {
    this.subCategory = subCategory;
  }

  public static PartsBean getAvailPart(String partNo) {
    PartsBean part = new PartsBean();
    /*
     * if (con == null) { con = DBInterfaceLocal.getSQLConnection(); }
     */
    Connection con1 = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con1 = DBInterfaceLocal.getSQLConnection();
      stmt = con1.createStatement();
      rs =
          stmt.executeQuery("Select * from Parts where PartNo='" + partNo
              + "' AND partdescription NOT LIKE 'Z%' AND partno NOT LIKE 'zz%' ");
      if (rs.next()) {
        part.setPartNo(rs.getString("PartNo"));
        String interChangeNo = rs.getString("InterchangeNo");
        part.setInterchangeNo(interChangeNo);
        part.setPartDescription(rs.getString("PartDescription"));
        part.setMakeModelCode(rs.getString("MakeModelCode"));
        part.setYear(rs.getString("Year"));
        part.setSupplierId(rs.getInt("SupplierId"));
        part.setOrderNo(rs.getInt("OrderNo"));
        part.setCostPrice(rs.getDouble("CostPrice"));
        part.setListPrice(rs.getDouble("ListPrice"));
        part.setActualPrice(rs.getDouble("ActualPrice"));
        if (interChangeNo == null || interChangeNo.trim().equals("")
            || interChangeNo.trim().equalsIgnoreCase("null")) {
          part.setUnitsInStock(rs.getInt("UnitsInStock"));
        } else {
          int units = 0;
          // logger.error(interChangeNo);
          units = PartsBean.getQuantity(interChangeNo, con1);
          part.setUnitsInStock(units);
        }
        part.setUnitsOnOrder(rs.getInt("UnitsOnOrder"));
        part.setReorderLevel(rs.getInt("ReorderLevel"));
//        part.setReorderLevelDate(rs.getLong("ReorderLevelDate"));
//        part.setCompPrice1(rs.getDouble("CompPrice1"));
//        part.setCompPrice2(rs.getDouble("CompPrice2"));
//        part.setCompPrice3(rs.getDouble("CompPrice3"));
        part.setKeystoneNumber(rs.getString("KeystoneNumber"));
        part.setOemNumber(rs.getString("OemNumber"));
        part.setLocation(rs.getString("Location"));
        part.setSubCategory(rs.getString("SubCategory"));
      } else {
        part = null;
      }
      rs.close();
      stmt.close();
      con1.close();
    } catch (SQLException e) {
      logger.error("Unable to get Part with PartNumber===" + partNo + "---" + e);
    }
    return part;
  }

  public static PartsBean getPart(String partNo, Connection con2) {
    PartsBean part = new PartsBean();
    /*
     * if (con == null) { con = DBInterfaceLocal.getSQLConnection(); }
     */
    Connection con1 = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con1 = DBInterfaceLocal.getSQLConnection();
      stmt = con1.createStatement();
      rs = stmt.executeQuery("Select * from Parts where PartNo='" + partNo + "'");
      if (rs.next()) {
        part.setPartNo(rs.getString("PartNo"));
        String interChangeNo = rs.getString("InterchangeNo");
        part.setInterchangeNo(interChangeNo);
        part.setPartDescription(rs.getString("PartDescription"));
        part.setMakeModelCode(rs.getString("MakeModelCode"));
        part.setYear(rs.getString("Year"));
        part.setSupplierId(rs.getInt("SupplierId"));
        part.setOrderNo(rs.getInt("OrderNo"));
        part.setCostPrice(rs.getDouble("CostPrice"));
        part.setListPrice(rs.getDouble("ListPrice"));
        part.setActualPrice(rs.getDouble("ActualPrice"));
        if (interChangeNo == null || interChangeNo.trim().equals("")
            || interChangeNo.trim().equalsIgnoreCase("null")) {
          part.setUnitsInStock(rs.getInt("UnitsInStock"));
        } else {
          int units = 0;
          // logger.error(interChangeNo);
          units = PartsBean.getQuantity(interChangeNo, con1);
          part.setUnitsInStock(units);
        }
        part.setUnitsOnOrder(rs.getInt("UnitsOnOrder"));
        part.setReorderLevel(rs.getInt("ReorderLevel"));
//        part.setReorderLevelDate(rs.getLong("ReorderLevelDate"));
//        part.setCompPrice1(rs.getDouble("CompPrice1"));
//        part.setCompPrice2(rs.getDouble("CompPrice2"));
//        part.setCompPrice3(rs.getDouble("CompPrice3"));
        part.setKeystoneNumber(rs.getString("KeystoneNumber"));
        part.setOemNumber(rs.getString("OemNumber"));
        part.setLocation(rs.getString("Location"));
        part.setSubCategory(rs.getString("SubCategory"));
      } else {
        part = null;
      }
      rs.close();
      stmt.close();
      con1.close();
    } catch (SQLException e) {
      logger.error("Unable to get Part with PartNumber===" + partNo + "---" + e);
    }
    return part;
  }

  public static PartsBean getInterPartByMain(String interPartNo) {
    PartsBean part = new PartsBean();
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery("Select * from Parts where InterChangeNo='" + interPartNo + "'");
      if (rs.next()) {
        part.setPartNo(rs.getString("PartNo"));
        String interChangeNo = rs.getString("InterchangeNo");
        part.setInterchangeNo(interChangeNo);
        part.setPartDescription(rs.getString("PartDescription"));
        part.setMakeModelCode(rs.getString("MakeModelCode"));
        part.setYear(rs.getString("Year"));
        part.setSupplierId(rs.getInt("SupplierId"));
        part.setOrderNo(rs.getInt("OrderNo"));
        part.setCostPrice(rs.getDouble("CostPrice"));
        part.setListPrice(rs.getDouble("ListPrice"));
        part.setActualPrice(rs.getDouble("ActualPrice"));
        if (interChangeNo == null || interChangeNo.trim().equals("")
            || interChangeNo.trim().equalsIgnoreCase("null")) {
          part.setUnitsInStock(rs.getInt("UnitsInStock"));
        } else {
          int units = 0;
          Statement stmt1 = con.createStatement();
          ResultSet rs1 =
              stmt1.executeQuery("Select UnitsInStock From Parts Where PartNo='" + interChangeNo
                  + "'");
          if (rs1.next()) {
            units = rs1.getInt(1);
          } else {
            units = 0;
          }
          // units = PartsBean.getQuantity(interChangeNo, con);
          part.setUnitsInStock(units);
        }
        part.setUnitsOnOrder(rs.getInt("UnitsOnOrder"));
        part.setReorderLevel(rs.getInt("ReorderLevel"));
//        part.setReorderLevelDate(rs.getLong("ReorderLevelDate"));
//        part.setCompPrice1(rs.getDouble("CompPrice1"));
//        part.setCompPrice2(rs.getDouble("CompPrice2"));
//        part.setCompPrice3(rs.getDouble("CompPrice3"));
        part.setKeystoneNumber(rs.getString("KeystoneNumber"));
        part.setOemNumber(rs.getString("OemNumber"));
        part.setLocation(rs.getString("Location"));
        part.setSubCategory(rs.getString("SubCategory"));
      } else {
        part = null;
      }
      rs.close();
      stmt.close();

      con.close();
    } catch (SQLException e) {
      logger.error("Unable to get Part with PartNumber===" + interPartNo + "---" + e);
    }
    return part;
  }

  public void addNewPart() {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer insertSQL = new StringBuffer("");
      insertSQL.append("INSERT INTO Parts (");
      insertSQL.append("PartNo, InterchangeNo, PartDescription, ");
      insertSQL.append("MakeModelCode, Year, SupplierId, ");
      insertSQL.append("CostPrice, ListPrice, ");
      insertSQL.append("UnitsInStock, UnitsOnOrder, ReorderLevel, ");
//      insertSQL.append("CompPrice1, CompPrice2, CompPrice3, ");
      insertSQL.append("KeystoneNumber, OEMNumber, Location, ActualPrice ");
      insertSQL.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement pstmt = con.prepareStatement(insertSQL.toString());

      pstmt.clearParameters();
      if (getPartNo() == null || getPartNo().trim().equals("")) {
        throw new Exception("Part No Wrong");
      }
      pstmt.setString(1, getPartNo().toUpperCase());
      pstmt.setString(2, getInterchangeNo());
      pstmt.setString(3, getPartDescription());
      pstmt.setString(4, getMakeModelCode());
      pstmt.setString(5, getYear());
      pstmt.setInt(6, getSupplierId());
      pstmt.setDouble(7, getCostPrice());
      pstmt.setDouble(8, getListPrice());
      if (getInterchangeNo() == null || getInterchangeNo().trim().equals("")) {
        pstmt.setInt(9, getUnitsInStock());
      } else {
        if (PartsBean.getPart(getInterchangeNo(), con) == null) {
          throw new UserException("This Interchangeable No is NOT Valid");
        }
        pstmt.setInt(9, 0);
      }

      pstmt.setInt(10, getUnitsOnOrder());
      pstmt.setInt(11, getReorderLevel());
      pstmt.setString(12, getKeystoneNumber());
      pstmt.setString(13, getOemNumber());
      pstmt.setString(14, getLocation());
      pstmt.setDouble(15, getActualPrice());
//      pstmt.setDouble(12, getCompPrice1());
//      pstmt.setDouble(13, getCompPrice2());
//      pstmt.setDouble(14, getCompPrice3());

      pstmt.execute();

      addToOrder();

      // handleInterchangeNo();

      pstmt.close();
      con.close();

    } catch (Exception e) {
      logger.error("In PartsBean - Unable to add New Part" + e);
    }

  }

  public void changePart() {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer changeSQL = new StringBuffer("");
      changeSQL.append("UPDATE Parts set ");
      changeSQL.append("InterchangeNo='" + getInterchangeNo() + "', ");
      changeSQL.append("PartDescription='" + getPartDescription() + "', ");
      changeSQL.append("MakeModelCode='" + getMakeModelCode() + "', ");
      changeSQL.append("Year='" + getYear() + "', ");
      changeSQL.append("SupplierID=" + getSupplierId() + ", ");
      changeSQL.append("OrderNo=" + getOrderNo() + ", ");
      changeSQL.append("CostPrice=" + getCostPrice() + ", ");
      changeSQL.append("ListPrice=" + getListPrice() + ", ");
      changeSQL.append("ActualPrice=" + getActualPrice() + ", ");

      // This part is for checking whether interchange no is available or
      // not
      PartsBean p = PartsBean.getPart(getPartNo(), con);
      if (getInterchangeNo() == null || getInterchangeNo().trim().equals("")) {
        changeSQL.append("UnitsInStock=" + getUnitsInStock() + ",");
      } else {
        if (PartsBean.getPart(getInterchangeNo(), con) == null) {
          throw new UserException("This Interchangeable No is NOT Valid");
        }
        changeSQL.append("UnitsInStock=0,");
        if (p.getInterchangeNo() != null
            && p.getInterchangeNo().trim().equals(getInterchangeNo().trim())) {
          PartsBean.changeQuantity(getInterchangeNo(), getUnitsInStock());
        }
      }
      changeSQL.append("UnitsOnOrder=" + getUnitsOnOrder() + ", ");
      changeSQL.append("ReorderLevel=" + reorderLevel + ", ");
//      changeSQL.append("ReorderLevelDate=" + getReorderLevelDate() + ", ");
//      changeSQL.append("CompPrice1=" + getCompPrice1() + ", ");
//      changeSQL.append("CompPrice2=" + getCompPrice2() + ", ");
//      changeSQL.append("CompPrice3=" + getCompPrice3() + ", ");
      changeSQL.append("KeystoneNumber='" + getKeystoneNumber() + "', ");
      changeSQL.append("OEMNumber='" + getOemNumber() + "', ");
      changeSQL.append("Location='" + getLocation() + "', ");
      changeSQL.append("SubCategory='" + getSubCategory() + "' ");
      changeSQL.append("WHERE PartNo='" + getPartNo() + "'");
      if (getPartNo() == null || getPartNo().trim().equals("")) {
        throw new Exception("Part Not Changed");
      }
      PreparedStatement pstmt = con.prepareStatement(changeSQL.toString());

      pstmt.executeUpdate();

      // handleInterchangeNo();

      pstmt.close();
      con.close();

    } catch (Exception e) {
      logger.error("In PartsBean - Unable to Change the Part" + e);
    }

  }

  public void deletePart() throws UserException {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      StringBuffer deleteSQL = new StringBuffer("DELETE FROM Parts WHERE PartNo=?");
      PreparedStatement pstmt = con.prepareStatement(deleteSQL.toString());
      pstmt.clearParameters();
      pstmt.setString(1, getPartNo());

      pstmt.executeUpdate();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("In PartsBean - Unable to Change the Part" + e);
    }

  }

  public void handleInterchangeNo() {

    logger.error("In PartsBean ----");
    if (getInterchangeNo() != null && (!getInterchangeNo().trim().equals(""))) {
      StringTokenizer st = new StringTokenizer(getInterchangeNo(), ",");
      while (st.hasMoreTokens()) {
        String interNo = st.nextToken();
        Connection con = DBInterfaceLocal.getSQLConnection();
        try {

          logger.error("In Parts Bean - Token is:" + interNo);
          PreparedStatement stmt =
              con.prepareStatement("UPDATE Parts set UnitsInStock=? WHERE PartNo=?");

          stmt.clearParameters();
          stmt.setInt(1, getUnitsInStock());
          stmt.setString(2, interNo);

          stmt.executeUpdate();

          stmt.close();
          con.close();

        } catch (SQLException e) {
          logger.error("In PartsBean - Some problem when handling Interchange No" + e);
        }
      }
    }
  }

  public static Hashtable<String, String> getPartDescByCriteria(int manufacId, String makeModelCode) {
    Hashtable<String, String> htable = new Hashtable<String, String>();

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      String sql = "SELECT PartNo, PartDescription FROM Parts a, MakeModel b where ";
      sql += "b.ManufacturerId=" + manufacId;
      sql += " AND b.MakeModelCode='" + makeModelCode + "'";
      sql += " AND b.makeModelCode=a.MakeModelCode";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String pKey = rs.getString("PartNo");
        String pVal = rs.getString("PartDescription");

        htable.put(pKey, pVal);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In PartsBean - Unable to get part Descs by Criteria" + e);
      htable = null;
    }
    return htable;

  }

  // Should change the quantity for interchangeable no, if one available
  public static void changeQuantity(String partNo, int qty) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      PartsBean p = PartsBean.getPart(partNo, con);
      if (p.getInterchangeNo() == null || p.getInterchangeNo().trim().equals("")) {
        PreparedStatement stmt =
            con.prepareStatement("update Parts set UnitsInStock=? where partNo=?");
        stmt.clearParameters();
        stmt.setInt(1, qty);
        stmt.setString(2, partNo);
        stmt.execute();
        stmt.close();
      } else {
        PartsBean.changeQuantity(p.getInterchangeNo(), qty);
      }

      con.close();

    } catch (SQLException e) {
      logger.error("In PartsBean - Unable to change qty for partNo:" + partNo);
    }
  }

  // Should get the quantity from interchangeable no, if one available
  public static int getQuantity(String partNo, Connection con4) {
    Connection con2 = null;
    int units = 0;
    try {
      con2 = DBInterfaceLocal.getSQLConnection();
      PartsBean p = PartsBean.getPart(partNo, con2);

      if (p == null) {
        units = 0;
        return units;
      }
      if (p.getInterchangeNo() == null || p.getInterchangeNo().trim().equals("")) {
        PreparedStatement stmt =
            con2.prepareStatement("SELECT PartNo, UnitsInStock FROM Parts WHERE PartNo=?");
        stmt.clearParameters();
        stmt.setString(1, partNo);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          units = rs.getInt("UnitsInStock");
        } else {
          units = 0;
        }
        // logger.error("Quantity got in getQuantity is : " +
        // units);
        stmt.close();

      } else {
        // logger.error("Going to call the method for Interchange No");
        units = PartsBean.getQuantity(p.getInterchangeNo(), con2);
      }
      con2.close();
    } catch (SQLException e) {
      logger.error("In PartsBean - Unable to get qty for partNo:" + partNo);
    }
    return units;
  }

  public static Vector<OurPartDetailsForm> getAllParts() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    Vector<OurPartDetailsForm> allParts = new Vector<OurPartDetailsForm>();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT a.PartNo, a.InterChangeNo, c.ManufacturerName, b.MakeModelName, a.PartDescription, a.Year, a.ListPrice, a.UnitsInStock FROM Parts a, MakeModel b, Manufacturer c WHERE a.MakeModelCode=b.MakeModelCode AND b.ManufacturerId=c.ManufacturerId ORDER BY 3 ASC, 4 ASC, 6 ASC, 5 ASC");

      while (rs.next()) {
        OurPartDetailsForm ourPart = new OurPartDetailsForm();
        ourPart.partNo = rs.getString(1);
        String interChangeNo = rs.getString(2);
        ourPart.manufacName = rs.getString(3);
        ourPart.makeModelName = rs.getString(4);
        ourPart.description = rs.getString(5);
        ourPart.year = rs.getString(6);
        ourPart.listPrice = rs.getDouble(7);
        if (interChangeNo == null || interChangeNo.trim().equals("")
            || interChangeNo.trim().equalsIgnoreCase("null")) {
          ourPart.unitsInStock = rs.getInt(8);
        } else {
          int units = 0;
          units = PartsBean.getQuantity(interChangeNo, con);
          ourPart.unitsInStock = units;
        }
        ourPart.qtyToOrder = 0;

        allParts.add(ourPart);
      }
      rs.close();
      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e);
    }
    return allParts;
  }

  public static Vector<OurPartDetailsForm> getAllParts(int make) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    Vector<OurPartDetailsForm> allParts = new Vector<OurPartDetailsForm>();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT a.PartNo, a.InterChangeNo, c.ManufacturerName, b.MakeModelName, a.PartDescription, a.Year, a.ListPrice, a.UnitsInStock FROM Parts a, MakeModel b, Manufacturer c WHERE a.MakeModelCode=b.MakeModelCode AND b.ManufacturerId=c.ManufacturerId AND c.ManufacturerId="
              + make + " ORDER BY 3 ASC, 4 ASC, 6 ASC, 5 ASC");

      while (rs.next()) {
        OurPartDetailsForm ourPart = new OurPartDetailsForm();
        ourPart.partNo = rs.getString(1);
        String interChangeNo = rs.getString(2);
        ourPart.manufacName = rs.getString(3);
        ourPart.makeModelName = rs.getString(4);
        ourPart.description = rs.getString(5);
        ourPart.year = rs.getString(6);
        ourPart.listPrice = rs.getDouble(7);
        if (interChangeNo == null || interChangeNo.trim().equals("")
            || interChangeNo.trim().equalsIgnoreCase("null")) {
          ourPart.unitsInStock = rs.getInt(8);
        } else {
          int units = 0;
          units = PartsBean.getQuantity(interChangeNo, con);
          ourPart.unitsInStock = units;
        }
        ourPart.qtyToOrder = 0;

        allParts.add(ourPart);
      }
      rs.close();
      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e);
    }
    return allParts;
  }

  public Vector<PartsBean> getAllInterChangeParts(Connection con1) {
    Vector<PartsBean> allInterChangeParts = null;
    try {

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select PartNo From Parts Where InterChangeNo='" + getPartNo() + "'");
      while (rs.next()) {
        if (allInterChangeParts == null) {
          allInterChangeParts = new Vector<PartsBean>();
        }
        String interPartNo = rs.getString("PartNo");
        PartsBean interPart = PartsBean.getPart(interPartNo, con);
        allInterChangeParts.addElement(interPart);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }
    return allInterChangeParts;
  }

  public void changeUnitsOnOrderForInterchangeables(int unitsOnOrder) {
    this.unitsOnOrder = unitsOnOrder;
    if (getAllInterChangeParts(null) != null) {
      Enumeration<PartsBean> ennum = getAllInterChangeParts(null).elements();
      while (ennum.hasMoreElements()) {
        PartsBean interPart = ennum.nextElement();
        interPart.setUnitsOnOrder(unitsOnOrder);
        interPart.changePart();
      }
    }
  }

  public void addToOrder() {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select * from VendorOrderedItems Where OrderNo=-1 and PartNo='"
              + getPartNo() + "'");
      if (!rs1.next()) {
        Statement stmt2 = con.createStatement();
        ResultSet rs2 =
            stmt2.executeQuery("Select max(NoOrder) from VendorOrderedItems Where OrderNo=-1 ");
        int noOrder = 0;
        if (rs2.next()) {
          noOrder = rs2.getInt(1);
        }
        noOrder++;
        Statement stmt3 = con.createStatement();
        stmt3
            .execute("Insert into VendorOrderedItems (OrderNo, PartNo, VendorPartNo, Quantity, NoOrder, Price) Values (-1, '"
                + getPartNo().toUpperCase() + "', '', 1, " + noOrder + ", 0) ");

        rs2.close();
        stmt2.close();
        stmt3.close();

      }

      rs1.close();
      stmt1.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception in AddToOrder  " + e);
    }
  }

  public void addToEnquiry() {

    try {
      Statement stmt3 = null;
      Statement stmt2 = null;
      ResultSet rs2 = null;
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select * from VendorOrderedItems Where OrderNo=-2 and PartNo='"
              + getPartNo() + "'");
      if (!rs1.next()) {
        stmt2 = con.createStatement();
        rs2 = stmt2.executeQuery("Select max(NoOrder) from VendorOrderedItems Where OrderNo=-2 ");
        int noOrder = 0;
        if (rs2.next()) {
          noOrder = rs2.getInt(1);
        }
        noOrder++;
        stmt3 = con.createStatement();
        stmt3
            .execute("Insert into VendorOrderedItems (OrderNo, PartNo, VendorPartNo, Quantity, NoOrder, Price) Values (-2, '"
                + partNo.trim() + "', '', 1, " + noOrder + ", 0) ");
      } else {
        stmt3 = con.createStatement();
        stmt3
            .execute("Update VendorOrderedItems set Quantity=Quantity+1 Where OrderNo=-2 and PartNo='"
                + getPartNo() + "'");
      }

      rs1.close();
      rs2.close();
      stmt1.close();
      stmt2.close();
      stmt3.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception in AddToEnquiry  " + e);
    }
  }

  public String getExtraMessage() {
    String extraMsg = "";

    Vector<PartsBean> vv = getAllInterChangeParts(null);
    if (vv != null) {
      extraMsg = "Also Fits: ";
      Enumeration<PartsBean> ennum = vv.elements();
      while (ennum.hasMoreElements()) {
        PartsBean p1 = ennum.nextElement();
        extraMsg +=
            " " + MakeModelBean.getMakeModelName(p1.getMakeModelCode()) + " - " + p1.getPartNo();
      }
    }

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String dt = DateUtils.getNewUSDate();
      ResultSet rs =
          stmt.executeQuery("Select SalePrice, FromDate, ToDate from SaleParts Where partNo ='"
              + getPartNo() + "' and FromDate<='" + DateUtils.convertUSToMySQLFormat(dt)
              + "' And ToDate>='" + DateUtils.convertUSToMySQLFormat(dt) + "' ");
      while (rs.next()) {
        double salePrice = rs.getDouble("SalePrice");
        String fromDate = DateUtils.convertMySQLToUSFormat(rs.getString("FromDate"));
        String toDate = DateUtils.convertMySQLToUSFormat(rs.getString("ToDate"));
        extraMsg += "<BR>ON SALE::::    For  " + salePrice + "  Till  " + toDate;
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When Getting On Sale Price:::" + e.getMessage());
    }
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select * from DamagedParts Where partNo ='" + getPartNo()
              + "' Order By PartOrder ");
      while (rs.next()) {
        String disc = rs.getString("SuggestedDiscount");
        String damDesc = rs.getString("DamagedDesc");
        extraMsg += "<BR>Damaged:::: " + damDesc + "  --  For Discount: " + disc;
      }
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select * from MissingParts Where partNo ='" + getPartNo()
              + "' Order By PartOrder ");
      while (rs1.next()) {
        String qty = rs1.getString("Quantity");
        extraMsg += "<BR>" + qty + " Pieces MISSING ";
      }
      rs1.close();
      stmt1.close();
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When Getting Damaged & Missing:::" + e.getMessage());
    }
    return extraMsg;
  }

  public static Hashtable<String, String> getCategories() {

    Hashtable<String, String> ht = null;
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select CategoryCode, CategoryName from Category Order By 1 desc ");
      while (rs1.next()) {
        if (ht == null) {
          ht = new Hashtable<String, String>();
        }
        String cd = rs1.getString("CategoryCode");
        String nm = rs1.getString("CategoryName");

        ht.put(cd, nm);
      }
      rs1.close();
      stmt1.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception in getCategories  " + e);
    }
    return ht;
  }

  public static Hashtable<String, String> getSubCategories() {

    Hashtable<String, String> ht = null;
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1
              .executeQuery("Select SubCategoryCode, SubCategoryName from SubCategory Order By 1 desc ");
      while (rs1.next()) {
        if (ht == null) {
          ht = new Hashtable<String, String>();
        }
        String cd = rs1.getString("SubCategoryCode");
        String nm = rs1.getString("SubCategoryName");

        ht.put(cd, nm);
      }
      rs1.close();
      stmt1.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception in getSubCategories  " + e);
    }
    return ht;
  }

  public static Vector<String[]> checkOtherInven(String partNo) {
    Vector<String[]> vect = null;
    int thisLocation = 0;
    if (partNo != null && !partNo.trim().equals("")) {
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Connection conX = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("Select * from Locations ");
        ResultSet rsX = null;
        Statement stmtX = null;
        Statement stmt1 = con.createStatement();
        Statement stmtY = conX.createStatement();
        ResultSet rs1 =
            stmt1.executeQuery("Select Value From Properties Where Name = 'ThisLocation' ");
        if (rs1.next()) {
          thisLocation = rs1.getInt(1);
        }
        while (rs.next()) {
          int locCode = rs.getInt("Code");
          String locName = rs.getString("Name");
          String serverName = rs.getString("ServerName");
          if (locCode == thisLocation) {
            continue;
          }

          String make = "";
          String desc = "";
          String year = "";
          String interNo = "";
          int units = 0;

          Class.forName("com.mysql.jdbc.Driver").newInstance();

          stmtY = conX.createStatement();
          String sql =
              "Select InterChangeNo, MakeModelName, PartDescription, Year, UnitsInStock From Parts a, MakeModel b Where PartNo ='"
                  + partNo + "' and a.MakeModelCode=b.MakeModelCode ";
          ResultSet rsY = stmtY.executeQuery(sql);
          if (rsY.next()) {
            interNo = rsY.getString("InterChangeNo");
            desc = rsY.getString("PartDescription");
            year = rsY.getString("Year");
            make = rsY.getString("MakeModelName");
            units = rsY.getInt("UnitsInStock");
          }
          if (interNo != null && !interNo.trim().equals("")) {
            stmtX = conX.createStatement();
            rsX =
                stmtX.executeQuery("Select UnitsInStock From Parts Where PartNo='" + interNo
                    + "' and UnitsInStock>0 ");
            if (rsX.next()) {
              units = rsX.getInt(1);
            }
          }
          if (units > 0) {
            String[] retStr = new String[6];

            retStr[0] = locName;
            retStr[1] = partNo;
            retStr[2] = year;
            retStr[3] = make;
            retStr[4] = desc;
            retStr[5] = units + "";

            if (vect == null) {
              vect = new Vector<String[]>();
            }
            vect.addElement(retStr);
          }

        }
        rsX.close();
        rs1.close();
        stmt1.close();
        stmtX.close();
        stmtY.close();
        rs.close();
        stmt.close();
        conX.close();
        con.close();
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
    return vect;
  }

}
