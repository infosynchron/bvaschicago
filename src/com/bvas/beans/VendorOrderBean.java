package com.bvas.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.formBeans.OrderFinalExtrasForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.NumberUtils;
import com.bvas.utils.ReportUtils;
import com.bvas.utils.UserException;

public class VendorOrderBean {
  private static final Logger logger = Logger.getLogger(VendorOrderBean.class);

  private int orderNo;

  private int supplierId;

  private String orderDate = null;

  private String deliveredDate = null;

  private String orderStatus = null;

  private double orderTotal;

  private Vector<VendorOrderedItemsBean> orderedItems = null;

  private String updatedInventory = "";

  private String updatedPrices = "";

  private String containerNo = null;

  private String supplInvNo = null;

  private String arrivedDate = null;

  private boolean isFinal;

  private int totalItems;

  private double discount;

  private double stickerCharges;

  private double overheadAmountsTotal;

  private String unitsOrderDoneDate = null;

  private String pricesDoneDate = null;

  private String inventoryDoneDate = null;

  private String paymentTerms = null;

  private String paymentDate = null;

  private String estimatedArrivalDate = null;

  public int getOrderNo() {
    return (this.orderNo);
  }

  public int getSupplierId() {
    return (this.supplierId);
  }

  public static int getSupplierId(int orderNo) {
    int supplId = 0;
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select SupplierId from VendorOrder Where OrderNo=" + orderNo);
      if (rs.next()) {
        supplId = rs.getInt(1);
      } else {
        supplId = 0;
      }

      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }
    return supplId;
  }

  public String getOrderDate() {
    return (this.orderDate);
  }

  public String getDeliveredDate() {
    return (this.deliveredDate);
  }

  public String getOrderStatus() {
    return (this.orderStatus);
  }

  public double getOrderTotal() {
    return (this.orderTotal);
  }

  public Vector<VendorOrderedItemsBean> getOrderedItems() {
    return (this.orderedItems);
  }

  public String getUpdatedInventory() {
    if (this.updatedInventory == null || this.updatedInventory.trim().equals("")) {
      this.updatedInventory = "N";
    }
    return (this.updatedInventory);
  }

  public String getUpdatedPrices() {
    if (this.updatedPrices == null || this.updatedPrices.trim().equals("")) {
      this.updatedPrices = "N";
    }
    return (this.updatedPrices);
  }

  public String getContainerNo() {
    return (this.containerNo);
  }

  public String getSupplInvNo() {
    return (this.supplInvNo);
  }

  public String getArrivedDate() {
    return (this.arrivedDate);
  }

  public boolean getIsFinal() {
    return (this.isFinal);
  }

  public int getTotalItems() {
    return (this.totalItems);
  }

  public double getDiscount() {
    return (this.discount);
  }

  public double getStickerCharges() {
    return (this.stickerCharges);
  }

  public double getOverheadAmountsTotal() {
    return (this.overheadAmountsTotal);
  }

  public String getUnitsOrderDoneDate() {
    return (this.unitsOrderDoneDate);
  }

  public String getPricesDoneDate() {
    return (this.pricesDoneDate);
  }

  public String getInventoryDoneDate() {
    return (this.inventoryDoneDate);
  }

  public String getPaymentTerms() {
    return (this.paymentTerms);
  }

  public String getPaymentDate() {
    return (this.paymentDate);
  }

  public String getEstimatedArrivalDate() {
    return (this.estimatedArrivalDate);
  }

  public void setOrderNo(int orderNo) {
    this.orderNo = orderNo;
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public void setDeliveredDate(String deliveredDate) {
    this.deliveredDate = deliveredDate;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public void setOrderTotal(double orderTotal) {
    this.orderTotal = orderTotal;
  }

  public void setOrderedItems(Vector<VendorOrderedItemsBean> orderedItems) {
    this.orderedItems = orderedItems;
  }

  public void setUpdatedInventory(String updatedInventory) {
    this.updatedInventory = updatedInventory;
  }

  public void setUpdatedPrices(String updatedPrices) {
    this.updatedPrices = updatedPrices;
  }

  public void setContainerNo(String containerNo) {
    this.containerNo = containerNo;
  }

  public void setSupplInvNo(String supplInvNo) {
    this.supplInvNo = supplInvNo;
  }

  public void setArrivedDate(String arrivedDate) {
    this.arrivedDate = arrivedDate;
  }

  public void setIsFinal(boolean isFinal) {
    this.isFinal = isFinal;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public void setStickerCharges(double stickerCharges) {
    this.stickerCharges = stickerCharges;
  }

  public void setOverheadAmountsTotal(double overheadAmountsTotal) {
    this.overheadAmountsTotal = overheadAmountsTotal;
  }

  public void setUnitsOrderDoneDate(String unitsOrderDoneDate) {
    this.unitsOrderDoneDate = unitsOrderDoneDate;
  }

  public void setPricesDoneDate(String pricesDoneDate) {
    this.pricesDoneDate = pricesDoneDate;
  }

  public void setInventoryDoneDate(String inventoryDoneDate) {
    this.inventoryDoneDate = inventoryDoneDate;
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public void setEstimatedArrivalDate(String estimatedArrivalDate) {
    this.estimatedArrivalDate = estimatedArrivalDate;
  }

  public static VendorOrderBean getOrder(int orderNo) throws UserException {
    VendorOrderBean voBean = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs2 = null;
    try {
      String sql = "SELECT * FROM VendorOrder where OrderNo=?";
      pstmt = con.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setInt(1, orderNo);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        voBean = new VendorOrderBean();
        voBean.setOrderNo(rs.getInt("OrderNo"));
        voBean.setSupplierId(rs.getInt("SupplierId"));
        voBean.setOrderDate(DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate")));
        voBean.setDeliveredDate(DateUtils.convertMySQLToUSFormat(rs.getString("DeliveredDate")));
        voBean.setOrderStatus(rs.getString("OrderStatus"));
        voBean.setOrderTotal(rs.getDouble("OrderTotal"));
        voBean.setUpdatedInventory(rs.getString("UpdatedInventory"));
        voBean.setUpdatedPrices(rs.getString("UpdatedPrices"));

        voBean.setContainerNo(rs.getString("ContainerNo"));
        voBean.setSupplInvNo(rs.getString("SupplInvNo"));
        voBean.setArrivedDate(rs.getString("ArrivedDate"));
        if (voBean.getArrivedDate() != null && !voBean.getArrivedDate().trim().equals("")
            && !voBean.getArrivedDate().trim().equals("0000-00-00")) {
          voBean.setArrivedDate(DateUtils.convertMySQLToUSFormat(voBean.getArrivedDate()));
        } else {
          voBean.setArrivedDate("");
        }
        String fin = rs.getString("IsFinal");
        if (fin != null && fin.trim().equals("Y")) {
          voBean.setIsFinal(true);
        } else {
          voBean.setIsFinal(false);
        }
        voBean.setTotalItems(rs.getInt("TotalItems"));
        voBean.setDiscount(rs.getDouble("Discount"));
        voBean.setStickerCharges(rs.getDouble("StickerCharges"));
        voBean.setOverheadAmountsTotal(rs.getDouble("OverheadAmountsTotal"));
        voBean.setUnitsOrderDoneDate(rs.getString("UnitsOrderDoneDate"));
        if (voBean.getUnitsOrderDoneDate() != null
            && !voBean.getUnitsOrderDoneDate().trim().equals("")
            && !voBean.getUnitsOrderDoneDate().trim().equals("0000-00-00")) {
          voBean.setUnitsOrderDoneDate(DateUtils.convertMySQLToUSFormat(voBean
              .getUnitsOrderDoneDate()));
        } else {
          voBean.setUnitsOrderDoneDate("");
        }
        voBean.setPricesDoneDate(rs.getString("PricesDoneDate"));
        if (voBean.getPricesDoneDate() != null && !voBean.getPricesDoneDate().trim().equals("")
            && !voBean.getPricesDoneDate().trim().equals("0000-00-00")) {
          voBean.setPricesDoneDate(DateUtils.convertMySQLToUSFormat(voBean.getPricesDoneDate()));
        } else {
          voBean.setPricesDoneDate("");
        }
        voBean.setInventoryDoneDate(rs.getString("InventoryDoneDate"));
        if (voBean.getInventoryDoneDate() != null
            && !voBean.getInventoryDoneDate().trim().equals("")
            && !voBean.getInventoryDoneDate().trim().equals("0000-00-00")) {
          voBean.setInventoryDoneDate(DateUtils.convertMySQLToUSFormat(voBean
              .getInventoryDoneDate()));
        } else {
          voBean.setInventoryDoneDate("");
        }
        voBean.setPaymentTerms(rs.getString("PaymentTerms"));
        voBean.setPaymentDate(rs.getString("PaymentDate"));
        if (voBean.getPaymentDate() != null && !voBean.getPaymentDate().trim().equals("")
            && !voBean.getPaymentDate().trim().equals("0000-00-00")) {
          voBean.setPaymentDate(DateUtils.convertMySQLToUSFormat(voBean.getPaymentDate()));
        } else {
          voBean.setPaymentDate("");
        }
        voBean.setEstimatedArrivalDate(rs.getString("EstimatedArrivalDate"));
        if (voBean.getEstimatedArrivalDate() != null
            && !voBean.getEstimatedArrivalDate().trim().equals("")
            && !voBean.getEstimatedArrivalDate().trim().equals("0000-00-00")) {
          voBean.setEstimatedArrivalDate(DateUtils.convertMySQLToUSFormat(voBean
              .getEstimatedArrivalDate()));
        } else {
          voBean.setEstimatedArrivalDate("");
        }

        String sql2 =
            "SELECT OrderNo, PartNo, VendorPartNo, Quantity, Price, NoOrder FROM VendorOrderedItems WHERE OrderNo=? ORDER BY NoOrder ASC";
        pstmt2 = con.prepareStatement(sql2);
        pstmt2.clearParameters();
        pstmt2.setInt(1, voBean.getOrderNo());
        rs2 = pstmt2.executeQuery();
        Vector<VendorOrderedItemsBean> v = new Vector<VendorOrderedItemsBean>();
        while (rs2.next()) {
          VendorOrderedItemsBean voiBean = new VendorOrderedItemsBean();
          voiBean.setOrderNo(rs2.getInt("OrderNo"));
          voiBean.setPartNo(rs2.getString("PartNo"));
          voiBean.setVendorPartNo(rs2.getString("VendorPartNo"));
          voiBean.setQuantity(rs2.getInt("Quantity"));
          voiBean.setPrice(rs2.getDouble("Price"));
          v.add(voiBean);
        }
        voBean.setOrderedItems(v);
      } else {
        throw new UserException("No Order with this number");
      }
      rs2.close();
      pstmt2.close();
      rs.close();
      pstmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("No Order Exists in the Database");
    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Unknown Problem - Try Later");
    }
    return voBean;
  }

  public void createOrder() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      con.setAutoCommit(false);
      String sql1 =
          "INSERT INTO VendorOrder (OrderNo, SupplierId, OrderDate, DeliveredDate, OrderStatus, "
              + "OrderTotal, UpdatedInventory, UpdatedPrices, ContainerNo, "
              + "SupplInvNo, ArrivedDate, TotalItems, Discount, StickerCharges, "
              + "OverheadAmountsTotal, UnitsOrderDoneDate, PricesDoneDate, InventoryDoneDate, "
              + "PaymentTerms, PaymentDate, IsFinal, EstimatedArrivalDate)"
              + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement pstmt1 = con.prepareStatement(sql1);
      pstmt1.clearParameters();
      pstmt1.setInt(1, getOrderNo());
      pstmt1.setInt(2, getSupplierId());
      pstmt1.setString(3, DateUtils.convertUSToMySQLFormat(getOrderDate()));
      pstmt1.setString(4, DateUtils.convertUSToMySQLFormat(getDeliveredDate()));
      pstmt1.setString(5, getOrderStatus());
      pstmt1.setDouble(6, getOrderTotal());
      pstmt1.setString(7, "N");
      pstmt1.setString(8, "N");
      pstmt1.setString(9, "");
      pstmt1.setString(10, "");
      pstmt1.setString(11, "");
      pstmt1.setInt(12, getTotalItems());
      pstmt1.setDouble(13, 0.0);
      pstmt1.setDouble(14, 0.0);
      pstmt1.setDouble(15, 0.0);
      pstmt1.setString(16, "");
      pstmt1.setString(17, "");
      pstmt1.setString(18, "");
      pstmt1.setString(19, "");
      pstmt1.setString(20, "");
      pstmt1.setString(21, "N");
      pstmt1.setString(22, "");

      pstmt1.execute();

      Enumeration<VendorOrderedItemsBean> ennum = getOrderedItems().elements();
      String sql =
          "INSERT INTO VendorOrderedItems (OrderNo, PartNo, VendorPartNo, Quantity, Price, NoOrder) VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement pstmt = con.prepareStatement(sql);
      int cnt = 0;
      while (ennum.hasMoreElements()) {
        VendorOrderedItemsBean voiBean = ennum.nextElement();
        pstmt.clearParameters();
        pstmt.setInt(1, voiBean.getOrderNo());
        pstmt.setString(2, voiBean.getPartNo());
        pstmt.setString(3, voiBean.getVendorPartNo());
        pstmt.setInt(4, voiBean.getQuantity());
        pstmt.setDouble(5, voiBean.getPrice());
        cnt++;
        pstmt.setInt(6, cnt);
        pstmt.execute();
      }
      con.setAutoCommit(true);
      pstmt1.close();
      pstmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      try {
        con.rollback();
        con.close();
      } catch (SQLException f) {
        logger.error(e);
      }
      throw new UserException("Order Not Created");
    } catch (Exception e) {
      try {
        con.rollback();
        con.close();
      } catch (SQLException f) {
        logger.error(f);
      }
      throw new UserException("Unknown Problem - Unable to create the Order" + e);
    }
  }

  public void changeOrder() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      con.setAutoCommit(false);
      // logger.error("In Vendor Order Bean -- " + getOrderNo());
      String sql1 =
          "UPDATE VendorOrder SET OrderDate=?, DeliveredDate=?, "
              + "OrderStatus=?, OrderTotal=?, UpdatedInventory=?, "
              + "UpdatedPrices=?, ContainerNo=?, SupplInvNo=?, ArrivedDate=?, IsFinal=?, "
              + "TotalItems=?, Discount=?, StickerCharges=?, OverheadAmountsTotal=?, "
              + "UnitsOrderDoneDate=?, PricesDoneDate=?, InventoryDoneDate=?, "
              + "PaymentTerms=?, PaymentDate=?, EstimatedArrivalDate=? " + "WHERE OrderNo=?";
      PreparedStatement pstmt1 = con.prepareStatement(sql1);
      pstmt1.clearParameters();
      pstmt1.setString(1, DateUtils.convertUSToMySQLFormat(getOrderDate()));
      pstmt1.setString(2, DateUtils.convertUSToMySQLFormat(getDeliveredDate()));
      pstmt1.setString(3, getOrderStatus());
      pstmt1.setDouble(4, getOrderTotal());
      pstmt1.setString(5, getUpdatedInventory());
      pstmt1.setString(6, getUpdatedPrices());
      pstmt1.setString(7, getContainerNo());
      pstmt1.setString(8, getSupplInvNo());
      if (getArrivedDate() != null && !getArrivedDate().trim().equals("")) {
        pstmt1.setString(9, DateUtils.convertUSToMySQLFormat(getArrivedDate()));
      } else {
        pstmt1.setString(9, "0000-00-00");
      }
      if (isFinal) {
        pstmt1.setString(10, "Y");
      } else {
        pstmt1.setString(10, "N");
      }
      pstmt1.setInt(11, getTotalItems());
      pstmt1.setDouble(12, getDiscount());
      pstmt1.setDouble(13, getStickerCharges());
      pstmt1.setDouble(14, getOverheadAmountsTotal());
      if (getUnitsOrderDoneDate() != null && !getUnitsOrderDoneDate().trim().equals("")) {
        pstmt1.setString(15, DateUtils.convertUSToMySQLFormat(getUnitsOrderDoneDate()));
      } else {
        pstmt1.setString(15, "0000-00-00");
      }
      if (getPricesDoneDate() != null && !getPricesDoneDate().trim().equals("")) {
        pstmt1.setString(16, DateUtils.convertUSToMySQLFormat(getPricesDoneDate()));
      } else {
        pstmt1.setString(16, "0000-00-00");
      }
      if (getInventoryDoneDate() != null && !getInventoryDoneDate().trim().equals("")) {
        pstmt1.setString(17, DateUtils.convertUSToMySQLFormat(getInventoryDoneDate()));
      } else {
        pstmt1.setString(17, "0000-00-00");
      }
      pstmt1.setString(18, getPaymentTerms());
      if (getPaymentDate() != null && !getPaymentDate().trim().equals("")) {
        pstmt1.setString(19, DateUtils.convertUSToMySQLFormat(getPaymentDate()));
      } else {
        pstmt1.setString(19, "0000-00-00");
      }
      if (getEstimatedArrivalDate() != null && !getEstimatedArrivalDate().trim().equals("")) {
        pstmt1.setString(20, DateUtils.convertUSToMySQLFormat(getEstimatedArrivalDate()));
      } else {
        pstmt1.setString(20, "0000-00-00");
      }
      pstmt1.setInt(21, getOrderNo());
      pstmt1.execute();

      String deleteSQL = "DELETE FROM VendorOrderedItems WHERE OrderNo=" + getOrderNo();
      Statement stmt = con.createStatement();
      stmt.execute(deleteSQL);

      Enumeration<VendorOrderedItemsBean> ennum = getOrderedItems().elements();
      String sql =
          "INSERT INTO VendorOrderedItems (OrderNo, PartNo, VendorPartNo, Quantity, Price, NoOrder) VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement pstmt = con.prepareStatement(sql);
      int cnt = 0;
      while (ennum.hasMoreElements()) {
        VendorOrderedItemsBean voiBean = ennum.nextElement();
        pstmt.clearParameters();
        pstmt.setInt(1, voiBean.getOrderNo());
        pstmt.setString(2, voiBean.getPartNo());
        pstmt.setString(3, voiBean.getVendorPartNo());
        pstmt.setInt(4, voiBean.getQuantity());
        pstmt.setDouble(5, voiBean.getPrice());
        cnt++;
        pstmt.setInt(6, cnt);
        pstmt.execute();
      }
      con.setAutoCommit(true);
      pstmt1.close();
      pstmt.close();
      con.close();
    } catch (SQLException e) {
      try {
        con.rollback();
        con.close();
      } catch (SQLException f) {
        logger.error(f);
      }
      logger.error(e);
      throw new UserException("Order Not Changed");
    } catch (Exception e) {
      try {
        con.rollback();
        con.close();
      } catch (SQLException f) {
        logger.error(f);
      }
      throw new UserException("Unknown Problem - Unable to Change the Order" + e);
    }
  }

  public static boolean isAvailable(int orderNo) {
    boolean available = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM VendorOrder WHERE OrderNo=" + orderNo);
      if (rs.next())
        available = true;
      else
        available = false;
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
      available = false;
    }

    return available;
  }

  public static int getMaxOrderNo() {
    int newOrderNo = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select max(OrderNo) from VendorOrder ");
      if (rs.next()) {
        newOrderNo = rs.getInt(1) + 1;
      } else {
        logger.error("In VendorOrderBean - No New Order Number");
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In VendorOrderBean - Unable to get New Order Number - " + e);
    }
    return newOrderNo;
  }

  public static boolean deleteOrder(int orderNo) {
    boolean success = false;
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      Statement stmt = con.createStatement();
      int succ1 = stmt.executeUpdate("Delete From VendorOrder Where OrderNo=" + orderNo);
      if (succ1 != 0) {
        success = true;
      }
      int succ2 = stmt.executeUpdate("Delete From VendorOrderedItems Where OrderNo=" + orderNo);

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return success;
  }

  public static boolean deleteItems(int orderNo) {
    boolean success = false;
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      Statement stmt = con.createStatement();
      int succ = stmt.executeUpdate("Delete From VendorOrderedItems Where OrderNo=" + orderNo);
      success = true;

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return success;
  }

  public static void makeFinal(int orderNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate(" Update VendorOrder Set IsFinal='Y' Where OrderNo=" + orderNo);

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  public static void removeFinal(int orderNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate(" Update VendorOrder Set IsFinal='N' Where OrderNo=" + orderNo);

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  public static boolean createOrderForOthers(int orderNo1, int orderNo2, int suppId) {
    boolean success = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      Statement stmt = con.createStatement();

      Statement stmtX = con.createStatement();
      Statement stmtZ = con.createStatement();

      Statement stmX = con.createStatement();
      Statement stmY = con.createStatement();

      ResultSet rs1X =
          stmX.executeQuery("Select SupplierId from vendorOrder Where OrderNo=" + orderNo1);

      int supp1 = 0;

      if (rs1X.next()) {
        supp1 = rs1X.getInt(1);
      }

      stmtX
          .execute("Insert Into VendorOrder (OrderNo, SupplierId, OrderDate, DeliveredDate, OrderStatus, OrderTotal, UpdatedInventory, UpdatedPrices, IsFinal) values ("
              + orderNo2
              + ", "
              + suppId
              + ", '"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "', '"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "', 'New', 0.0, 'N', 'N', 'N' ) ");

      ResultSet rs =
          stmt.executeQuery("SELECT * FROM VendorOrderedItems WHERE OrderNo=" + orderNo1
              + " Order By NoOrder");
      int orderNo = orderNo1;
      while (rs.next()) {
        String partNo = rs.getString("PartNo");
        String vendPartNo = "";
        int qty = rs.getInt("Quantity");
        double price = 0.0;
        int noOrder = rs.getInt("NoOrder");

        if (supp1 == suppId) {
          vendPartNo = rs.getString("VendorPartNo");
          price = rs.getDouble("Price");
        } else {
          Statement stmtY = con.createStatement();
          ResultSet rsX =
              stmtY.executeQuery("Select * from VendorItems Where SupplierId=" + suppId
                  + " and PartNo = '" + partNo + "'");
          if (rsX.next()) {
            vendPartNo = rsX.getString("VendorPartNo");
            // logger.error(vendPartNo);
          }
          rs1X.close();
          stmtY.close();
        }
        if (vendPartNo == null) {
          vendPartNo = "";
        } else {
          vendPartNo = vendPartNo.trim();
        }

        stmtZ
            .execute("INSERT INTO VendorOrderedItems (OrderNo, PartNo, VendorPartNo, Quantity, Price, NoOrder) VALUES ('"
                + orderNo2
                + "', '"
                + partNo
                + "', '"
                + vendPartNo
                + "', "
                + qty
                + ", "
                + price
                + ", " + noOrder + ")");

        success = true;
      }

      // stmt.execute("");
      rs.close();
      stmtZ.close();
      stmtX.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error("Exception---" + e);
    } catch (Exception e) {
      logger.error("Exception---" + e);
    }

    return success;
  }

  public static boolean deleteItemsFromOrder1(int orderNo1, int orderNo2) {
    boolean success = false;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      Statement stmt1 = con.createStatement();
      Statement stmt2 = con.createStatement();

      ResultSet rs1 =
          stmt1.executeQuery("Select * from VendorOrderedItems Where OrderNo=" + orderNo2);

      while (rs1.next()) {

        String partNo = rs1.getString("PartNo");

        if (partNo == null || partNo.trim().equals("")) {
          continue;
        } else {
          stmt2.executeUpdate("DELETE FROM VendorOrderedItems WHERE OrderNo=" + orderNo1
              + " AND PartNo='" + partNo.trim() + "'");
        }

        PartsBean part = PartsBean.getPart(partNo, con);
        if (part == null) {
          continue;
        }

        Vector<PartsBean> interParts = null;
        if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")) {
          // logger.error("Deleting InterchangeNo  " + partNo +
          // "---" + part.getInterchangeNo());
          stmt2.executeUpdate("DELETE FROM VendorOrderedItems WHERE OrderNo=" + orderNo1
              + " AND PartNo='" + part.getInterchangeNo().trim() + "'");
          interParts = PartsBean.getPart(part.getInterchangeNo(), con).getAllInterChangeParts(con);
        } else {
          interParts = part.getAllInterChangeParts(con);
        }
        if (interParts != null) {
          Enumeration<PartsBean> ennum = interParts.elements();
          while (ennum.hasMoreElements()) {
            PartsBean pat = ennum.nextElement();
            // logger.error("Deleting InterchangeNo2  " +
            // partNo + "---" + pat.getPartNo());
            stmt2.executeUpdate("DELETE FROM VendorOrderedItems WHERE OrderNo=" + orderNo1
                + " AND PartNo='" + pat.getPartNo().trim() + "'");

          }
        }

      }
      success = true;

      rs1.close();
      stmt1.close();
      stmt2.close();
      con.close();

    } catch (Exception e) {
      logger.error("Exception---" + e);
    }

    return success;
  }

  public static void removeAlreadyOrdered(int orderNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      File returns = new File("c:/bvaschicago/Data/ItemsRemoved" + orderNo + ".txt");
      FileWriter wrt = new FileWriter(returns);
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select SupplierId From VendorOrder Where OrderNo=" + orderNo);
      Statement stmtX = null;
      int supId = 0;
      if (rs.next()) {
        supId = rs.getInt(1);
      }
      int totRem = 0;
      int totCh = 0;

      Statement stmt1 = con.createStatement();
      String sql = "Select * From VendorOrderedItems Where OrderNo=" + orderNo;
      ResultSet rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        String partNo = rs1.getString("PartNo");
        String vendorNo = rs1.getString("VendorPartNo");
        int noOrder = rs1.getInt("NoOrder");
        int qty = rs1.getInt("Quantity");
        int noPieces = 0;
        int totalOrdered = 0;
        int stock = 0;
        int reorder = 0;
        int diff = 0;
        int toOrder = 0;

        if (partNo == null || partNo.trim().equals("")) {
          continue;
        }
        PartsBean part = PartsBean.getPart(partNo, con);
        if (part == null) {
          continue;
        } else {
          stock = part.getUnitsInStock();
          reorder = part.getReorderLevel();
        }

        VendorItemBean viBean = null;
        if (vendorNo == null || vendorNo.trim().equals("")) {
          noPieces = 1;
        } else {
          viBean = viBean.getThePart(supId, partNo, vendorNo, con);
          if (viBean != null) {
            noPieces = viBean.getNoOfPieces();
          } else {
            noPieces = 1;
          }
        }

        Statement stmt2 = con.createStatement();
        String sql2 = "Select sum(Quantity) from VendorOrderedItems a, VendorOrder b where ";
        sql2 += " ( ";
        String sql3 = "";
        if (part.getInterchangeNo().trim().equals("")) {
          sql3 += " a.PartNo='" + partNo + "' ";
          Vector<PartsBean> v = part.getAllInterChangeParts(con);
          if (v != null) {
            Enumeration<PartsBean> ennum = v.elements();
            while (ennum.hasMoreElements()) {
              sql3 += " or a.PartNo='" + ennum.nextElement().getPartNo() + "' ";
            }
          }
        } else {
          sql3 += " a.PartNo='" + part.getInterchangeNo() + "' ";
          Vector<PartsBean> v =
              PartsBean.getPart(part.getInterchangeNo(), con).getAllInterChangeParts(con);
          if (v != null) {
            Enumeration<PartsBean> ennum = v.elements();
            while (ennum.hasMoreElements()) {
              sql3 += " or a.PartNo='" + ennum.nextElement().getPartNo() + "' ";
            }
          }
        }
        sql2 += sql3;
        sql2 += " ) and ";
        sql2 += " a.OrderNo=b.OrderNo and b.IsFinal='Y' and b.UpdatedInventory='N'";
        ResultSet rs2 = stmt2.executeQuery(sql2);
        if (rs2.next()) {
          totalOrdered = rs2.getInt(1);
        } else {
          totalOrdered = 0;
        }

        boolean toRemove = false;
        diff = reorder - (stock + totalOrdered);

        if (diff < 0) {
          toRemove = true;
        } else {
          if (supId == 15 || supId == 34) {
            // diff = Math.round(reorder / 2) - stock -
            // totalOrdered;
            diff = Math.round(reorder / 5) - stock;
            toOrder = diff + 1;
            if (toOrder < 1) {
              toRemove = true;
            }
          } else if (supId == 13 || supId == 31) {
            // diff = Math.round(reorder / 2) - stock -
            // totalOrdered;
            diff = Math.round(reorder / 2) - stock;
            toOrder = diff + 1;
            if (toOrder < 1) {
              toRemove = true;
            }
          } else if (supId == 4 || supId == 10) {
            if (stock == 1 && reorder < 4) {
              toRemove = true;
            } else if (stock > 5) {
              toRemove = true;
            } else {
              if (reorder <= 3) {
                toOrder = 1;
              } else if (reorder >= 4 && reorder <= 7) {
                toOrder = 2;
              } else if (reorder >= 8 && reorder <= 12) {
                toOrder = 3;
              } else if (reorder >= 13 && reorder <= 20) {
                toOrder = 4;
              } else {
                toOrder = 5;
              }
              toOrder = toOrder - stock;
              if (toOrder <= 0) {
                toRemove = true;
              }
            }
          } else {
            if (noPieces == 0 || noPieces == 1) {
              toOrder = diff + 1;
              if (toOrder == 1) {
                toOrder = 2;
              }
              if (reorder <= 5) {
                toOrder += 1;
              } else if (reorder >= 6 && reorder <= 12) {
                toOrder += 3;
              } else if (reorder >= 13 && reorder <= 19) {
                toOrder += 4;
              } else if (reorder >= 20 && reorder <= 40) {
                toOrder += 6;
              } else {
                toOrder += 8;
              }
            } else {
              if (reorder <= 5) {
                diff += 1;
              } else if (reorder >= 6 && reorder <= 12) {
                diff += 3;
              } else if (reorder >= 13 && reorder <= 19) {
                diff += 4;
              } else if (reorder >= 20 && reorder <= 40) {
                diff += 6;
              } else {
                diff += 8;
              }
              if (diff <= noPieces) {
                toOrder = noPieces;
              } else {
                int rem = diff % noPieces;
                if (rem == 0) {
                  toOrder = diff;
                } else {
                  toOrder = diff - (rem) + noPieces;
                }
              }
            }

          }
        }

        if (toRemove) {
          stmtX = con.createStatement();
          wrt.write("Remove " + partNo + " Stock=" + stock + " Re=" + reorder + " Or="
              + totalOrdered);
          wrt.write("\n");
          stmtX.execute("Delete From VendorOrderedItems Where OrderNo=" + orderNo + " and NoOrder="
              + noOrder);
          totRem++;

        } else if (toOrder != qty) {
          stmtX = con.createStatement();
          wrt.write("Change " + partNo + " Stock=" + stock + " Re=" + reorder + " Or="
              + totalOrdered + " From" + qty + " To " + toOrder);
          wrt.write("\n");
          stmtX.execute("Update VendorOrderedItems Set Quantity=" + toOrder + " Where OrderNo="
              + orderNo + " and NoOrder=" + noOrder);
          totCh++;
        }

      }
      wrt.write("\n");
      wrt.write("\n");
      wrt.write("\n");
      wrt.write("\n");
      wrt.write("\n");
      wrt.write("Total Removed: " + totRem);
      wrt.write("\n");
      wrt.write("\n");
      wrt.write("Total Changed: " + totCh);

      wrt.close();
      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();
      stmtX.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception thrown on RemoveAlreadyOrdered: " + e.getMessage());
    }
  }

  public static void compareForPrices(int orderNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {

      // logger.error(11111);
      File returns = new File("c:/bvaschicago/Data/OrderCompared" + orderNo + ".txt");
      FileWriter wrt = new FileWriter(returns);
      // File returns1 = new File("c:/bvaschicago/Data/OrderCompared2-" +
      // orderNo
      // + ".txt");
      // FileWriter wrt1 = new FileWriter(returns1);

      Statement stmt = con.createStatement();
      PreparedStatement pstmt1 =
          con.prepareStatement("Select a.PartNo, a.VendorPartNo, a.Price, a.Quantity, b.CostPrice, b.ActualPrice, b.UnitsInStock, b.ReorderLevel, b.InterchangeNo, b.Year, b.PartDescription, c.MakeModelName from VendorOrderedItems a, Parts b, MakeModel c where a.OrderNo="
              + orderNo
              + " and a.partno=b.partno and b.MakeModelCode=c.MakeModelCode Order By NoOrder ");
      // logger.error(22222);
      // PreparedStatement pstmt2 =
      // con.prepareStatement("Select SupplierId, SellingRate from VendorItems Where PartNo=? and SupplierId != ? and SellingRate != 0 Order By SellingRate");
      Statement pstmt2 = con.createStatement();
      Statement stmt3 = con.createStatement();
      ResultSet rs3 = stmt3.executeQuery("Select SupplierId, CompanyName from Vendors ");
      Hashtable<String, String> vendorTable = new Hashtable<String, String>();
      while (rs3.next()) {
        vendorTable.put(rs3.getString(1), rs3.getString(2));
      }
      Statement stmt4 = con.createStatement();
      ResultSet rs4 = stmt4.executeQuery("Select SupplierId, CompanyName from LocalVendors ");
      Hashtable<String, String> localVendorTable = new Hashtable<String, String>();
      // logger.error(33333);
      while (rs4.next()) {
        localVendorTable.put(rs4.getString(1), rs4.getString(2));
      }

      int supId = 0;
      ResultSet rs0 =
          stmt.executeQuery("SELECT SupplierId FROM VendorOrder WHERE OrderNo=" + orderNo);
      if (rs0.next()) {
        supId = rs0.getInt("SupplierId");
      } else {
        logger.error("No SupplierId");
        System.exit(1);
      }

      // logger.error(44444);
      pstmt1.clearParameters();
      ResultSet rs1 = pstmt1.executeQuery();
      wrt.write("PRICE COMPARISON FOR THE ORDER : " + orderNo);
      wrt.write("\n");
      wrt.write("\n");

      // wrt1.write("PRICE COMPARISON FOR THE ORDER : " + orderNo);
      // wrt1.write("\n");
      // wrt1.write("\n");

      while (rs1.next()) {
        String partNo = rs1.getString("PartNo");
        logger.error("Compare::" + partNo);
        String vendPartNo = rs1.getString("VendorPartNo");
        String vendDesc = "";
        // logger.error(partNo);
        double price = rs1.getDouble("Price");
        int qty = rs1.getInt("Quantity");
        double costPrice = rs1.getDouble("CostPrice");
        double actualPrice = rs1.getDouble("ActualPrice");
        int units = rs1.getInt("UnitsInStock");
        int reorder = rs1.getInt("ReorderLevel");
        String descrip =
            rs1.getString("Year") + "  " + rs1.getString("PartDescription") + "  "
                + rs1.getString("MakeModelName");
        String remarkStr = "";
        String interNo = rs1.getString("InterchangeNo");
        if (!interNo.trim().equals("")) {
          Statement stmtXX = con.createStatement();
          ResultSet rsXX =
              stmtXX.executeQuery("Select UnitsInStock from parts where partno='" + interNo + "'");
          if (rsXX.next()) {
            units = rsXX.getInt(1);
          }
        }
        String p1 = "";
        String p2 = "";

        Statement stmtA = con.createStatement();
        String sqlA = "Select PartNo From Parts Where InterchangeNo='";
        if (interNo.trim().equals("")) {
          sqlA += partNo + "'";
        } else {
          sqlA += interNo + "' and partNo!='" + partNo + "'";
        }
        ResultSet rsA = stmtA.executeQuery(sqlA);
        while (rsA.next()) {
          String xxx = rsA.getString(1);
          if (p1.trim().equals("")) {
            p1 = xxx;
          } else if (p2.trim().equals("")) {
            p2 = xxx;
          } else {
            break;
          }
        }

        if (vendPartNo != null && !vendPartNo.trim().equals("")) {
          String it1 = "";
          String it2 = "";
          Statement stmtXXX = con.createStatement();
          ResultSet rsXXX =
              stmtXXX
                  .executeQuery("Select ItemDesc1, ItemDesc2 From VendorItems Where VendorPartNo like '"
                      + vendPartNo + "%' ");
          if (rsXXX.next()) {
            vendDesc = "";
            it1 = rsXXX.getString("ItemDesc1");
            it2 = rsXXX.getString("ItemDesc2");
            if (it1 != null && !it1.trim().equals("")) {
              vendDesc = it1.trim();
            }
            if (it2 != null && !it2.trim().equals("")) {
              vendDesc += it2.trim();
            }
          }
        }

        String printStr1X = "";
        if (costPrice != 0 && price != 0) {
          if ((((costPrice - price) / costPrice) * 100) > 40) {
            printStr1X += "COST :  GOOD";
          } else {
            printStr1X += "COST : CHECK";
          }
          if (printStr1X.trim().equals("")) {
            printStr1X += "            ";
          }
        }
        double lowestPrice = price;
        double price1 = 0;
        double price2 = 0;
        double price3 = 0;
        double price4 = 0;
        double price5 = 0;
        int supId1 = 0;
        int supId2 = 0;
        int supId3 = 0;
        int supId4 = 0;
        int supId5 = 0;
        String supName1 = "";
        String supName2 = "";
        String supName3 = "";
        String supName4 = "";
        String supName5 = "";

        String sqle =
            "Select SupplierId, SellingRate from VendorItems Where (PartNo like '%" + partNo.trim()
                + "%'";
        if (!interNo.trim().equals("")) {
          sqle += " or PartNo like '%" + interNo + "%'";
        }
        if (!p1.trim().equals("")) {
          sqle += " or PartNo like '%" + p1 + "%'";
        }
        if (!p2.trim().equals("")) {
          sqle += " or PartNo like '%" + p2 + "%'";
        }
        sqle += ") and SupplierId != " + supId + " and SellingRate != 0 Order By SellingRate";
        ResultSet rs2 = pstmt2.executeQuery(sqle);

        if (rs2.next()) {
          supId1 = rs2.getInt("SupplierId");
          price1 = rs2.getDouble("SellingRate");
        }
        if (rs2.next()) {
          supId2 = rs2.getInt("SupplierId");
          price2 = rs2.getDouble("SellingRate");
        }
        if (rs2.next()) {
          supId3 = rs2.getInt("SupplierId");
          price3 = rs2.getDouble("SellingRate");
        }
        if (rs2.next()) {
          supId4 = rs2.getInt("SupplierId");
          price4 = rs2.getDouble("SellingRate");
        }
        if (rs2.next()) {
          supId5 = rs2.getInt("SupplierId");
          price5 = rs2.getDouble("SellingRate");
        }

        String remarks = "";

        if (price5 != 0) {
          if (price >= price5) {
            remarks = "Hi";
          }
        } else if (price4 != 0) {
          if (price >= price4) {
            remarks = "Hi";
          }
        } else if (price3 != 0) {
          if (price >= price3) {
            remarks = "Hi";
          }
        } else if (price2 != 0) {
          if (price >= price2) {
            remarks = "Hi";
          }
        } else if (price1 != 0) {
          if (price >= price1) {
            remarks = "Hi";
          }
        }
        if (price1 != 0) {
          if (price <= price1) {
            remarks = "Lo";
          }

          // For Checking The Lowest Price
          if (price1 < lowestPrice) {
            lowestPrice = price1;
          }
        }
        if (remarks.trim().equals("")) {
          remarks = "Mid";
        }

        if (supId1 != 0) {
          supName1 = vendorTable.get(supId1 + "");
        }
        if (supId2 != 0) {
          supName2 = vendorTable.get(supId2 + "");
        }
        if (supId3 != 0) {
          supName3 = vendorTable.get(supId3 + "");
        }
        if (supId4 != 0) {
          supName4 = vendorTable.get(supId4 + "");
        }
        if (supId5 != 0) {
          supName5 = vendorTable.get(supId5 + "");
        }

        if (supId1 == 1 || supId2 == 1 || supId3 == 1 || supId4 == 1 || supId5 == 1) {
          remarkStr += "-EE-";
        }
        if (supId1 == 13 || supId2 == 13 || supId3 == 13 || supId4 == 13 || supId5 == 13) {
          remarkStr += "-AC-";
        }

        String printStr1 = "";
        String printStr2 = "";
        String printStr3 = "";
        String printStr31 = "";
        // String printStr32 = "";
        String printStr32X = "";
        String printStr32Y = "";
        String printStr32Z = "";
        String printStr4 = "";
        String printStrXX1 = "BV :: " + descrip;
        String printStrXX2 = "VD :: " + vendDesc.trim();

        printStr1 =
            partNo + ":" + price + " (" + remarks + ")" + " QTY:" + qty + " CST:" + costPrice
                + " ACT:" + actualPrice + " ST:" + units + " RE:" + reorder;
        // printStr1 = partNo + " : " + price + " (" + remarks + ")";
        if (price1 != 0) {
          printStr2 = "Prices:   " + price1 + " (" + supName1.substring(0, 3) + ") ";
        }
        if (price2 != 0) {
          printStr2 += "\t" + price2 + " (" + supName2.substring(0, 3) + ") ";
        }
        if (price3 != 0) {
          printStr2 += "\t" + price3 + " (" + supName3.substring(0, 3) + ") ";
        }
        if (price4 != 0) {
          printStr2 += "\t" + price4 + " (" + supName4.substring(0, 3) + ") ";
        }
        if (price5 != 0) {
          printStr2 += "\t" + price5 + " (" + supName5.substring(0, 3) + ") ";
        }

        String sqlx =
            "Select b.SupplierId, b.OrderDate, a.OrderNo, a.Price, a.Quantity from VendorOrderedItems a, VendorOrder b Where (a.PartNo like '%"
                + partNo.trim() + "%'";
        if (!interNo.trim().equals("")) {
          sqlx += " or a.PartNo like '%" + interNo + "%'";
        }
        if (!p1.trim().equals("")) {
          sqlx += " or a.PartNo like '%" + p1 + "%'";
        }
        if (!p2.trim().equals("")) {
          sqlx += " or a.PartNo like '%" + p2 + "%'";
        }
        sqlx +=
            ") and a.OrderNo != "
                + orderNo
                + " and a.Price != 0 and a.OrderNo=b.OrderNo and b.IsFinal='Y' and b.UpdatedInventory='N' Order By a.OrderNo ";
        Statement stmt5 = con.createStatement();
        ResultSet rs5 = stmt5.executeQuery(sqlx);
        printStr3 = "Committed :";
        while (rs5.next()) {
          String xx = DateUtils.convertMySQLToUSFormat(rs5.getString("OrderDate"));
          String mm = "";
          String yy = "";
          try {
            mm = xx.substring(0, 2);
            yy = xx.substring(8);
          } catch (Exception e) {
            logger.error(e);
          }
          double prce = rs5.getDouble("Price");
          if (prce < lowestPrice) {
            lowestPrice = prce;
          }
          String ordNo = rs5.getString("OrderNo");
          int suppId = rs5.getInt("SupplierId");
          if (suppId == 1 && remarkStr.indexOf("-EE-") == -1) {
            remarkStr += "-EE-";
          }
          if (suppId == 13 && remarkStr.indexOf("-AC-") == -1) {
            remarkStr += "-AC-";
          }
          String supNm = vendorTable.get(suppId + "").substring(0, 3);
          int qt = rs5.getInt("Quantity");
          printStr3 +=
              prce + " (" + qt + "-" + supNm + "(" + ordNo + ")" + "-" + mm + "/" + yy + ") ";
        }

        // FOR OLD ORDERS
        String sqlx1 =
            "Select b.SupplierId, b.OrderDate, a.OrderNo, a.Price from VendorOrderedItems a, VendorOrder b Where a.OrderNo > 3000 and (a.PartNo like '%"
                + partNo.trim() + "%'";
        if (!interNo.trim().equals("")) {
          sqlx1 += " or a.PartNo like '%" + interNo + "%'";
        }
        if (!p1.trim().equals("")) {
          sqlx1 += " or a.PartNo like '%" + p1 + "%'";
        }
        if (!p2.trim().equals("")) {
          sqlx1 += " or a.PartNo like '%" + p2 + "%'";
        }
        sqlx1 +=
            ") and a.OrderNo != "
                + orderNo
                + " and a.Price != 0 and a.OrderNo=b.OrderNo and b.IsFinal='Y' and b.UpdatedInventory='Y' Order By a.OrderNo ";
        Statement stmt51 = con.createStatement();
        ResultSet rs51 = stmt51.executeQuery(sqlx1);
        printStr31 = "Old :";
        while (rs51.next()) {
          String xx1 = DateUtils.convertMySQLToUSFormat(rs51.getString("OrderDate"));
          String mm1 = "";
          String yy1 = "";
          try {
            mm1 = xx1.substring(0, 2);
            yy1 = xx1.substring(8);
          } catch (Exception e) {
            logger.error(e);
          }
          double prce = rs51.getDouble("Price");
          if (prce < lowestPrice) {
            lowestPrice = prce;
          }
          int suppId = rs51.getInt("SupplierId");
          if (suppId == 1 && remarkStr.indexOf("-EE-") == -1) {
            remarkStr += "-EE-";
          }
          if (suppId == 13 && remarkStr.indexOf("-AC-") == -1) {
            remarkStr += "-AC-";
          }
          printStr31 +=
              prce + " (" + rs51.getString("OrderNo") + "- "
                  + vendorTable.get(suppId + "").substring(0, 3) + "-" + mm1 + "/" + yy1 + ") ";
        }

        String sqlx2 =
            "Select b.SupplierId, b.OrderDate, a.OrderNo, a.Price from VendorOrderedItems a, VendorOrder b Where (a.PartNo like '%"
                + partNo.trim() + "%'";
        if (!interNo.trim().equals("")) {
          sqlx2 += " or a.PartNo like '%" + interNo + "%'";
        }
        if (!p1.trim().equals("")) {
          sqlx2 += " or a.PartNo like '%" + p1 + "%'";
        }
        if (!p2.trim().equals("")) {
          sqlx2 += " or a.PartNo like '%" + p2 + "%'";
        }
        sqlx2 +=
            ") and a.OrderNo != " + orderNo
                + " and a.Price != 0 and a.OrderNo=b.OrderNo and b.IsFinal='N' Order By a.OrderNo ";
        Statement stmt52 = con.createStatement();
        ResultSet rs52 = stmt52.executeQuery(sqlx2);
        // printStr32 = "New Orders :";
        printStr32X = "Back :";
        printStr32Y = "Remo :";
        printStr32Z = "Orde :";
        while (rs52.next()) {
          String xx2 = DateUtils.convertMySQLToUSFormat(rs52.getString("OrderDate"));
          String mm2 = "";
          String yy2 = "";
          try {
            mm2 = xx2.substring(0, 2);
            yy2 = xx2.substring(8);
          } catch (Exception e) {
            logger.error(e);
          }
          double prce = rs52.getDouble("Price");
          if (prce < lowestPrice) {
            lowestPrice = prce;
          }
          int suppId = rs52.getInt("SupplierId");
          if (suppId == 1 && remarkStr.indexOf("-EE-") == -1) {
            remarkStr += "-EE-";
          }
          if (suppId == 13 && remarkStr.indexOf("-AC-") == -1) {
            remarkStr += "-AC-";
          }
          int ordNo = Integer.parseInt(rs52.getString("OrderNo"));

          // FOR REMOVING GR ORDERS & OTHERS
          // if ((ordNo >= 526 && ordNo <= 600) || (ordNo >= 651 &&
          // ordNo <= 750)) {
          // continue;
          // FOR BACK ORDERS
          // } else if (ordNo >= 601 && ordNo <= 650) {
          if ((ordNo >= 4001 && ordNo <= 4400) || (ordNo >= 4401 && ordNo <= 4800)) {
            printStr32X +=
                prce + " (" + ordNo + "-" + vendorTable.get(suppId + "").substring(0, 3) + "-"
                    + mm2 + "/" + yy2 + ") ";
            // REMOVED FOR HIGH PRICES
          } else if ((ordNo >= 3361 && ordNo <= 3800) || (ordNo >= 8251 && ordNo <= 9000)) {
            printStr32Y +=
                prce + " (" + ordNo + "-" + vendorTable.get(suppId + "").substring(0, 3) + "-"
                    + mm2 + "/" + yy2 + ") ";
          } else {
            printStr32Z +=
                prce + " (" + ordNo + "-" + vendorTable.get(suppId + "").substring(0, 3) + "-"
                    + mm2 + "/" + yy2 + ") ";
          }
        }

        String sqly =
            "Select SupplierId, DateEntered, Price from LocalOrders Where DateEntered>'2009-07-01' and supplierid!=38 and supplierid!=39 and (PartNo like '%"
                + partNo.trim() + "%'";
        if (!interNo.trim().equals("")) {
          sqly += " or PartNo like '%" + interNo + "%'";
        }
        if (!p1.trim().equals("")) {
          sqly += " or PartNo like '%" + p1 + "%'";
        }
        if (!p2.trim().equals("")) {
          sqly += " or PartNo like '%" + p2 + "%'";
        }
        sqly += ") and Price != 0 ";
        Statement stmt6 = con.createStatement();
        ResultSet rs6 = stmt6.executeQuery(sqly);
        printStr4 = "Local :";
        while (rs6.next()) {
          String xx = DateUtils.convertMySQLToUSFormat(rs6.getString("DateEntered"));
          String mm = "";
          String yy = "";
          try {
            mm = xx.substring(0, 2);
            yy = xx.substring(8);
          } catch (Exception e) {
            logger.error(e);
          }
          int supIdd = 0;
          supIdd = rs6.getInt("SupplierId");
          if (supIdd == 4) {
            remarkStr += "-MX-";
          }
          if (supIdd == 14) {
            remarkStr += "-AC-";
          }
          String supStr = "";
          if (supIdd != 0) {
            supStr = localVendorTable.get(supIdd + "").substring(0, 3);
          }
          double prce = rs6.getDouble("Price");
          if (prce < lowestPrice) {
            lowestPrice = prce;
          }
          printStr4 += prce + " (" + supStr + "-" + mm + "/" + yy + ") ";
        }

        if (lowestPrice == price) {
          printStr1X += "      PRICE : LOWEST";
        } else if ((price - lowestPrice) < 0.50) {
          printStr1X += "      PRICE : OK";
        } else {
          printStr1X += "      PRICE : " + lowestPrice;
        }

        if (costPrice != 0) {
          double perc = ((costPrice - price) / costPrice) * 100;
          if (perc > 0) {
            perc = NumberUtils.cutFractions(perc);
          }
          if (price < 2 && perc < 70) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 2 && price < 5 && perc < 60) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 5 && price < 8 && perc < 55) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 8 && price < 12 && perc < 50) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 12 && price < 22 && perc < 45) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 22 && price < 42 && perc < 40) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 42 && price < 72 && perc < 35) {
            printStr1X += "          LOW COST : " + perc;
          } else if (price >= 72 && perc < 30) {
            printStr1X += "          LOW COST : " + perc;
          }
        }

        if (!remarkStr.trim().equals("")) {
          printStr1X += remarkStr;
        }

        if (!printStr1.trim().equals("")) {
          wrt.write(printStr1);
          wrt.write("\n");
          wrt.write(printStr1X);
          wrt.write("\n");
          if (!printStr2.trim().equals("")) {
            wrt.write(printStr2);
            wrt.write("\n");
          }
          if (printStr3.trim().length() > 11) {
            wrt.write(printStr3);
            wrt.write("\n");
          }
          if (printStr31.trim().length() > 5) {
            wrt.write(printStr31);
            wrt.write("\n");
          }
          if (printStr32X.trim().length() > 6) {
            wrt.write(printStr32X);
            wrt.write("\n");
          }
          if (printStr32Y.trim().length() > 6) {
            wrt.write(printStr32Y);
            wrt.write("\n");
          }
          if (printStr32Z.trim().length() > 6) {
            wrt.write(printStr32Z);
            wrt.write("\n");
          }
          if (printStr4.trim().length() > 7) {
            wrt.write(printStr4);
            wrt.write("\n");
          }

          wrt.write(printStrXX1);
          wrt.write("\n");

          if (printStrXX2.trim().length() > 6) {
            wrt.write(printStrXX2);
            wrt.write("\n");
          }
          wrt.write("\n");

          printStr1 = "";
          printStr1X = "";
          printStr2 = "";
          printStr3 = "";
          printStr31 = "";
          printStr32X = "";
          printStr32Y = "";
          printStr32Z = "";
          printStr4 = "";
          printStrXX1 = "";
          printStrXX2 = "";

          // wrt1.write(printStr1);
          // wrt1.write("\n");
          // wrt1.write(printStr1X);
          // wrt1.write("\n\n");
        }

      }

      wrt.close();
      stmt.close();
      pstmt1.close();
      pstmt2.close();
      stmt3.close();
      rs3.close();
      rs4.close();
      stmt4.close();

      con.close();

      // wrt1.close();
    } catch (FileNotFoundException e) {
      logger.error("Exception---" + e);
    } catch (IOException e) {
      logger.error("Exception---" + e);
    } catch (Exception e) {
      logger.error("Exception---" + e);
    }

  }

  public static boolean mergeOrders(int orderNo1, int orderNo2) {
    boolean success = false;
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      con.setAutoCommit(false);
      Statement stmt = con.createStatement();
      Statement stmt2 = con.createStatement();
      ResultSet rs1 =
          stmt.executeQuery("SELECT MAX(NoOrder) FROM VendorOrderedItems where orderNo=" + orderNo1);
      int maxNoOrder = 0;
      if (rs1.next()) {
        maxNoOrder = rs1.getInt(1);
        // logger.error(maxNoOrder);
      }

      ResultSet rs =
          stmt.executeQuery("SELECT * FROM VendorOrderedItems WHERE OrderNo=" + orderNo2);
      int orderNo = orderNo1;
      int suppl1 = VendorOrderBean.getSupplierId(orderNo1);
      int suppl2 = VendorOrderBean.getSupplierId(orderNo2);
      while (rs.next()) {
        String partNo = rs.getString("PartNo");
        String vendPartNo = "";
        if (suppl1 == suppl2) {
          vendPartNo = rs.getString("VendorPartNo");
        } else {
        }
        int qty = rs.getInt("Quantity");
        double price = rs.getDouble("Price");
        int noOrder = rs.getInt("NoOrder");

        noOrder = ++maxNoOrder;
        // logger.error(maxNoOrder);

        stmt2
            .execute("INSERT INTO VendorOrderedItems (OrderNo, PartNo, VendorPartNo, Quantity, Price, NoOrder) VALUES ('"
                + orderNo
                + "', '"
                + partNo
                + "', '"
                + vendPartNo
                + "', "
                + qty
                + ", "
                + price
                + ", " + noOrder + ")");
        success = true;
      }
      con.commit();
      con.setAutoCommit(true);
      rs.close();
      rs1.close();
      stmt.close();
      stmt2.close();
      con.close();
      // stmt.execute("");

    } catch (SQLException e) {
      success = false;
      try {
        con.rollback();
        con.close();
      } catch (Exception ex) {
        logger.error(ex);
      }
      logger.error("Exception---" + e);
    } catch (Exception e) {
      success = false;
      try {
        con.rollback();
        con.close();
      } catch (Exception ex) {
        logger.error(ex);
      }
      logger.error("Exception---" + e);
    }

    return success;
  }

  public static Vector<OrderFinalExtrasForm> getExtras(int orderNo) {
    Vector<OrderFinalExtrasForm> v = null;

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM VendorOrderExtras where OrderNo=" + orderNo);
      while (rs.next()) {
        if (v == null) {
          v = new Vector<OrderFinalExtrasForm>();
        }
        com.bvas.formBeans.OrderFinalExtrasForm form =
            new com.bvas.formBeans.OrderFinalExtrasForm();
        form.orderNo = orderNo;
        form.extraReason = rs.getString("ExtraReason");
        form.addedDate = DateUtils.convertMySQLToUSFormat(rs.getString("AddedDate"));
        form.extraAmount = rs.getDouble("ExtraAmount");
        v.add(form);
      }
      rs.close();

      stmt.close();

      con.close();
    } catch (Exception e) {
      logger.error(e);
      v = null;
    }
    return v;
  }

  public static boolean removeExtras(int orderNo) {
    boolean removed = false;

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      removed = stmt.execute("DELETE FROM VendorOrderExtras where OrderNo=" + orderNo);

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception in Remove Order Extras: " + e);
      removed = false;
    }

    return removed;
  }

  public static void addExtras(Vector<OrderFinalExtrasForm> orderExtras) {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      String sql =
          "INSERT INTO VendorOrderExtras (OrderNo, ExtraReason, AddedDate, ExtraAmount) VALUES (?, ?, ?, ?)";
      PreparedStatement pstmt = con.prepareStatement(sql);
      Enumeration<OrderFinalExtrasForm> ennum = orderExtras.elements();
      VendorOrderBean orderBean = null;
      while (ennum.hasMoreElements()) {
        OrderFinalExtrasForm form = ennum.nextElement();
        if (orderBean == null) {
          orderBean = VendorOrderBean.getOrder(form.orderNo);
          orderBean.setOverheadAmountsTotal(0.0);
        }
        pstmt.clearParameters();
        pstmt.setInt(1, form.orderNo);
        pstmt.setString(2, form.extraReason);
        pstmt.setString(3, DateUtils.convertUSToMySQLFormat(form.addedDate));
        pstmt.setDouble(4, form.extraAmount);
        pstmt.execute();

        if (form.extraReason.trim().equalsIgnoreCase("Discount")) {
          orderBean.setDiscount(form.extraAmount);
        } else if (form.extraReason.trim().equalsIgnoreCase("Sticker Charges")) {
          orderBean.setStickerCharges(form.extraAmount);
        } else {
          orderBean.setOverheadAmountsTotal(orderBean.getOverheadAmountsTotal() + form.extraAmount);
        }
      }
      orderBean.changeOrder();

      pstmt.close();

      con.close();
    } catch (Exception e) {
      logger.error("Exception in Add Order Extras: " + e);
    }
  }

  public void doFinalSteps(Vector<OrderFinalExtrasForm> orderExtras, int supplierId,
      boolean findErrors, boolean updateInventory, boolean updatePrices) {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      con.setAutoCommit(false);
      File errorLog = new File("c:/bvas/data/VendorOrderErrorLog" + getOrderNo() + ".txt");
      FileWriter wrt = null;
      try {
        wrt = new FileWriter(errorLog);
      } catch (Exception e) {
        logger.error(e);
      }
      boolean invenPrevUpdated = false;
      if (getUpdatedInventory() != null && !getUpdatedInventory().trim().equals("")
          && getUpdatedInventory().trim().equals("Y")) {
        invenPrevUpdated = true;
      }
      boolean orderChanged = false;
      if (updateInventory) {
        if (!findErrors) {
          setUpdatedInventory("Y");
        }
        // logger.error("UpdatedInventory==="+getUpdatedInventory());
        orderChanged = true;
      }
      if (updatePrices) {
        if (!findErrors) {
          setUpdatedPrices("Y");
        }
        orderChanged = true;
      }

      if (orderChanged) {
        double orderTotal = getOrderTotal();
        double extraTotal = 0.0;
        double totalPriceFromActual = 0;
        int itemCount = 0;
        double tst = 0;

        Enumeration<OrderFinalExtrasForm> en = orderExtras.elements();
        while (en.hasMoreElements()) {
          OrderFinalExtrasForm form = en.nextElement();
          extraTotal += form.extraAmount;
        }
        double multiplyFactor = (1 + (extraTotal / orderTotal));

        Enumeration<VendorOrderedItemsBean> ennum = getOrderedItems().elements();

        while (ennum.hasMoreElements()) {
          VendorOrderedItemsBean orderDetails = ennum.nextElement();
          String partNo = orderDetails.getPartNo();
          String vendorPartNo = orderDetails.getVendorPartNo();

          if (partNo == null)
            partNo = "";
          if (vendorPartNo == null)
            vendorPartNo = "";

          int newQty = 0;
          boolean partChanged = false;
          double newPrice = 0.0;
          double newTotPrice = 0.0;

          if (vendorPartNo != null && !partNo.trim().equals("")) {
            double vendorPrice = 0.0;
            vendorPrice = orderDetails.getPrice();

            newPrice = vendorPrice * multiplyFactor;
            newPrice = NumberUtils.cutFractions(newPrice, 2);
            try {
              wrt.write("\n\n");
              double tmp = 0;
              tmp = orderDetails.getQuantity() * vendorPrice;
              tst += tmp;
              wrt.write(orderDetails.getQuantity() + "-----" + vendorPrice + "-----" + tmp
                  + "-----" + tst + "-----" + partNo + "-----" + vendorPartNo);
            } catch (Exception e) {
              logger.error(e);
            }
          }
          newTotPrice = newPrice * orderDetails.getQuantity();
          totalPriceFromActual += newTotPrice;
          itemCount++;
          try {
            wrt.write("\n");
            wrt.write(orderDetails.getQuantity() + "-----" + newPrice + "-----" + newTotPrice
                + "-----" + totalPriceFromActual);
          } catch (Exception e) {
            logger.error(e);
          }
          PartsBean part = PartsBean.getPart(partNo, con);
          if (part != null && !part.getInterchangeNo().trim().equals("")) {
            String xx = part.getInterchangeNo();
            part = null;
            part = PartsBean.getPart(xx, con);
          }

          if (part == null) {
            try {
              wrt.write("\n\n");
              wrt.write("Please check this : Our Part No could be wrong");
              wrt.write("\n");
              wrt.write("Our PartNo = :" + partNo + ":     Vendor Part No = " + vendorPartNo);
              if (!partNo.trim().equals(part.getPartNo())) {
                wrt.write("\nWORKING ON MAIN PART  :  " + part.getPartNo());
              }
              wrt.write("\n\n");
            } catch (Exception e) {
              logger.error(e);
            }
            continue;
          } else {

            // logger.error("updatePrices Value is   :   " +
            // updatePrices);
            if (updatePrices) {
              setPricesDoneDate(DateUtils.getNewUSDate());
              double partActualPrice = 0;

              // GETTING ACTUAL PRICE
              partActualPrice = part.getActualPrice();

              // GETTING UNITS IN STOCK
              int partQuantity = part.getUnitsInStock();

              if (partActualPrice == 0) {
                part.setActualPrice(newPrice);
                partChanged = true;
              } else {
                double percPrice = newPrice * 0.20;
                if (partActualPrice > (newPrice + percPrice)
                    || partActualPrice < (newPrice - percPrice)) {
                  try {
                    wrt.write("\n");
                    wrt.write("Please check this : Too much Difference in Old Actual Price and New Actual Price");
                    wrt.write("\n");
                    wrt.write("Our PartNo = " + partNo + "     Vendor Part No = " + vendorPartNo
                        + "     System Actual Price = " + partActualPrice
                        + "     New Actual Price = " + newPrice);
                    if (!partNo.trim().equals(part.getPartNo())) {
                      wrt.write("\nWORKING ON MAIN PART  :  " + part.getPartNo());
                    }
                    wrt.write("\n");
                  } catch (Exception e) {
                    logger.error(e);
                  }
                }

                if (updateInventory && partQuantity > 0) {
                  newPrice =
                      (((newPrice * orderDetails.getQuantity()) + (partActualPrice * partQuantity)) / (orderDetails
                          .getQuantity() + partQuantity));
                }
                newPrice = NumberUtils.cutFractions(newPrice, 2);
                part.setActualPrice(newPrice);
                partChanged = true;

              }

              try {
                VendorItemBean vendorBean =
                    VendorItemBean.getThePart(getSupplierId(), partNo, vendorPartNo, con);
                if (vendorBean == null) {
                  vendorBean = VendorItemBean.getVendorPart(getSupplierId(), partNo, vendorPartNo);
                }
                if (vendorBean != null && vendorBean.getOemNo() != null
                    && !vendorBean.getOemNo().trim().equals("")
                    && (part.getOemNumber() == null || part.getOemNumber().trim().equals(""))) {
                  part.setOemNumber(vendorBean.getOemNo().trim());
                }
                if (vendorBean != null
                    && vendorBean.getPlNo() != null
                    && !vendorBean.getPlNo().trim().equals("")
                    && (part.getKeystoneNumber() == null || part.getKeystoneNumber().trim()
                        .equals(""))) {
                  part.setKeystoneNumber(vendorBean.getPlNo().trim());
                }
              } catch (UserException e) {
                logger.error(e);
              }
            }
            double newPriceMore = newPrice / 0.70;
            if (part.getCostPrice() != 0 && part.getCostPrice() < newPriceMore) {
              try {
                wrt.write("\n\n");
                wrt.write("Please check this : COST PRICE IS LOW");
                wrt.write("\n");
                wrt.write("Our PartNo = " + partNo + "     Vendor Part No = " + vendorPartNo
                    + "     SYSTEM COST PRICE = " + part.getCostPrice()
                    + "     Our Actual Price = " + newPrice);
                if (!partNo.trim().equals(part.getPartNo())) {
                  wrt.write("\nWORKING ON MAIN PART  :  " + part.getPartNo());
                }
                wrt.write("\n\n");
              } catch (Exception e) {
                logger.error(e);
              }

            }

            if (updateInventory && !invenPrevUpdated) {
              setInventoryDoneDate(DateUtils.getNewUSDate());
              try {

                // SETTING NEW UNITS IN STOCK
                if (part.getUnitsInStock() < 0) {
                  try {
                    wrt.write("\n\n");
                    wrt.write("Please check this : QUANTITY IS LESS THAN ZERO");
                    wrt.write("\n");
                    wrt.write("Our PartNo = " + partNo + "     Vendor Part No = " + vendorPartNo);
                    if (!partNo.trim().equals(part.getPartNo())) {
                      wrt.write("\nWORKING ON MAIN PART  :  " + part.getPartNo());
                    }
                    wrt.write("\n\n");
                  } catch (Exception e) {
                    logger.error(e);
                  }
                }
                newQty = part.getUnitsInStock() + orderDetails.getQuantity();

                part.setUnitsInStock(newQty);
                part.setUnitsOnOrder(part.getUnitsOnOrder() - orderDetails.getQuantity());
                if (part.getUnitsOnOrder() < 0) {
                  part.setUnitsOnOrder(0);
                }

                part.setSupplierId(supplierId);
                part.setOrderNo(orderNo);
                partChanged = true;
              } catch (Exception e) {
                logger.error(e);
              }
            }
            if (!findErrors && partChanged) {
              part.changePart();
              Vector<PartsBean> v = part.getAllInterChangeParts(con);
              if (v != null) {
                Enumeration<PartsBean> ennumX = v.elements();
                while (ennumX.hasMoreElements()) {
                  PartsBean pp = ennumX.nextElement();
                  pp.setSupplierId(part.getSupplierId());
                  pp.setOrderNo(part.getOrderNo());
                  pp.setActualPrice(part.getActualPrice());
                  if ((pp.getOemNumber() == null || pp.getOemNumber().trim().equals(""))
                      && (part.getOemNumber() != null && !part.getOemNumber().trim().equals(""))) {
                    pp.setOemNumber(part.getOemNumber());
                  }
                  if ((pp.getKeystoneNumber() == null || pp.getKeystoneNumber().trim().equals(""))
                      && (part.getKeystoneNumber() != null && !part.getKeystoneNumber().trim()
                          .equals(""))) {
                    pp.setKeystoneNumber(part.getKeystoneNumber());
                  }
                  if ((pp.getLocation() == null || pp.getLocation().trim().equals(""))
                      && (part.getLocation() != null && !part.getLocation().trim().equals(""))) {
                    pp.setLocation(part.getLocation());
                  }
                  pp.setUnitsOnOrder(part.getUnitsOnOrder());
                  pp.changePart();
                }
              }
            }
          }

        }
        try {
          changeOrder();
        } catch (Exception e) {
          logger.error(e);
        }
        try {
          wrt.write("\n\n\n\n\n");
          wrt.write("Total No. Of Items Calculated  :  " + itemCount);
          wrt.write("\n");
          wrt.write("Total From the Order  :  " + orderTotal);
          wrt.write("\n");
          wrt.write("All Extras Total  :  " + extraTotal);
          wrt.write("\n");
          wrt.write("Multification Factor  :  " + multiplyFactor);
          wrt.write("\n");
          wrt.write("Total Order Price after adding all extras: " + (extraTotal + orderTotal));
          wrt.write("\n");
          wrt.write("Total Order Price after calculating all the actualprices: "
              + totalPriceFromActual);
          wrt.write("\n");
          wrt.close();
        } catch (Exception e) {
          logger.error(e);
        }
      }
      con.commit();
      con.setAutoCommit(true);

      con.close();
    } catch (Exception exx) {
      logger.error(exx);
      try {
        con.rollback();
        con.close();
      } catch (Exception exxx) {
      }
    }
  }

  public void changeUnitsOnOrder() throws UserException {

    Enumeration<VendorOrderedItemsBean> ennum1 = getOrderedItems().elements();

    boolean somePartNosWrong = false;
    int noPartNos = 0;
    String partNosWrongStr = "";
    int strLen = 0;
    while (ennum1.hasMoreElements()) {
      VendorOrderedItemsBean orderDetails = ennum1.nextElement();
      String partNo = orderDetails.getPartNo();
      if (partNo == null || partNo.trim().equals("")) {
        noPartNos++;
      } else {
        PartsBean part = PartsBean.getPart(partNo, null);
        if (part == null) {
          somePartNosWrong = true;
          if (partNosWrongStr.trim().equals("")) {
            partNosWrongStr += partNo.trim();
          } else {
            partNosWrongStr += ", " + partNo.trim();
          }
          if (partNosWrongStr.length() > (strLen + 40)) {
            strLen = partNosWrongStr.length();
            partNosWrongStr += "<BR>";
          }
        }
      }
    }
    if (!somePartNosWrong) {
      Enumeration<VendorOrderedItemsBean> ennum2 = getOrderedItems().elements();
      while (ennum2.hasMoreElements()) {
        VendorOrderedItemsBean orderDetails = ennum2.nextElement();
        String partNo = orderDetails.getPartNo();
        int qty = orderDetails.getQuantity();
        PartsBean part = PartsBean.getPart(partNo, null);
        if (part != null) {
          if (part.getInterchangeNo() == null || part.getInterchangeNo().trim().equals("")) {
            part.setUnitsOnOrder(part.getUnitsOnOrder() + qty);
            part.changePart();
            part.changeUnitsOnOrderForInterchangeables(part.getUnitsOnOrder());
          } else {
            PartsBean mainPart = PartsBean.getPart(part.getInterchangeNo(), null);
            mainPart.setUnitsOnOrder(mainPart.getUnitsOnOrder() + qty);
            mainPart.changePart();
            mainPart.changeUnitsOnOrderForInterchangeables(mainPart.getUnitsOnOrder());
          }
        }
      }
    } else {
      throw new UserException("Units On Order Not Changed because these part Nos Are Wrong <BR>"
          + partNosWrongStr);
    }

  }

  public static Hashtable showPendingOrders() throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "PendingOrders.html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[5][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.OrderNo, b.CompanyName, a.OrderDate, a.DeliveredDate, (a.OrderTotal + a.Discount + a.StickerCharges) as TotalOrder, a.PaymentTerms, a.PaymentDate, a.UpdatedInventory FROM VendorOrder a, Vendors b WHERE a.IsFinal='Y' AND a.SupplierId=b.SupplierId Order By OrderNo ";

      ResultSet rs = stmt.executeQuery(sql);

      double totalPaymentInvDone = 0.0;
      double totalPaymentInvNotDone = 0.0;
      double totalPayment = 0.0;

      mainHeading = " Suppliers Payments Pending As On " + DateUtils.getNewUSDate();
      subHeadings.addElement("Order");
      subHeadings.addElement("Company");
      subHeadings.addElement("Order Date");
      subHeadings.addElement("Amount");
      subHeadings.addElement("Ship Date");
      subHeadings.addElement("Pmt Date");
      subHeadings.addElement("Inven");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        int orderNo = rs.getInt("OrderNo");
        String companyName = rs.getString("CompanyName");
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate"));
        String deliveredDate = DateUtils.convertMySQLToUSFormat(rs.getString("DeliveredDate"));
        double orderTotal = rs.getDouble("TotalOrder");
        String paymentTerms = rs.getString("PaymentTerms");
        String paymentDate = rs.getString("PaymentDate");
        String datePayment = "";
        String updatedInventory = rs.getString("UpdatedInventory");

        if (paymentDate != null && !paymentDate.trim().equals("")
            && !paymentDate.trim().equals("0000-00-00")) {
          continue;
        }

        if (updatedInventory == null) {
          updatedInventory = "";
        }
        if (updatedInventory.trim().equals("Y")) {
          totalPaymentInvDone += orderTotal;
        } else {
          totalPaymentInvNotDone += orderTotal;
        }

        int terms = 0;
        try {
          terms = Integer.parseInt(paymentTerms.trim());
        } catch (Exception e) {
          // logger.error(e.getMessage());
        }

        try {
          java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
          java.util.Date dd = sdf.parse(deliveredDate);
          java.util.Date ddX =
              new java.util.Date(dd.getTime() + (1000 * 60 * 60 * 24 * (long) terms));
          datePayment = sdf.format(ddX);
        } catch (Exception e) {
          logger.error(e.getMessage());
        }

        Hashtable totData = new Hashtable();
        totData.put("Order", orderNo + "");
        totData.put("Company", companyName);
        totData.put("Order Date", orderDate);
        totData.put("Amount", orderTotal + "");
        totData.put("Ship Date", deliveredDate);
        totData.put("Pmt Date", datePayment);
        totData.put("Inven", updatedInventory);

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Pending Payments ");
      }

      String totalPaymentInvDoneStr = totalPaymentInvDone + "";
      String totalPaymentInvNotDoneStr = totalPaymentInvNotDone + "";
      String totalPaymentStr = totalPaymentInvDone + totalPaymentInvNotDone + "";

      if (totalPaymentInvDoneStr.indexOf(".") == totalPaymentInvDoneStr.length() - 2) {
        totalPaymentInvDoneStr += "0";
      }
      if (totalPaymentInvNotDoneStr.indexOf(".") == totalPaymentInvNotDoneStr.length() - 2) {
        totalPaymentInvNotDoneStr += "0";
      }
      if (totalPaymentStr.indexOf(".") == totalPaymentStr.length() - 2) {
        totalPaymentStr += "0";
      }

      totalPaymentInvDoneStr = NumberUtils.cutFractions(totalPaymentInvDoneStr);
      totalPaymentInvNotDoneStr = NumberUtils.cutFractions(totalPaymentInvNotDoneStr);
      totalPaymentStr = NumberUtils.cutFractions(totalPaymentStr);

      totals[0][0] = "Inven Done Orders Total";
      totals[0][1] = totalPaymentInvDoneStr;
      totals[1][0] = "&nbsp;";
      totals[1][1] = "&nbsp;";
      totals[2][0] = "Inven Not Done Orders Total";
      totals[2][1] = totalPaymentInvNotDoneStr;
      totals[3][0] = "&nbsp;";
      totals[3][1] = "&nbsp;";
      totals[4][0] = "Total Pending";
      totals[4][1] = totalPaymentStr;

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "VendorOrderDetails");
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

  public static Hashtable showPaidOrders() throws UserException {
    Hashtable toShowSales = null;
    try {

      String fileName = "PaidOrders.html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[1][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.OrderNo, b.CompanyName, a.OrderDate, a.DeliveredDate, (a.OrderTotal + a.Discount + a.StickerCharges) as TotalOrder, a.PaymentDate FROM VendorOrder a, Vendors b WHERE a.IsFinal='Y' AND a.SupplierId=b.SupplierId Order By OrderNo ";

      ResultSet rs = stmt.executeQuery(sql);

      //double totalPayment = 0.0;
      BigDecimal  totPayments=new BigDecimal("0");
      mainHeading = " Suppliers Payments Paid As On " + DateUtils.getNewUSDate();
      subHeadings.addElement("Order No");
      subHeadings.addElement("Company Name");
      subHeadings.addElement("Order Date");
      subHeadings.addElement("Amount");
      subHeadings.addElement("Shipped Date");
      subHeadings.addElement("Payment Date");

      while (rs.next()) {
        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }

        int orderNo = rs.getInt("OrderNo");
        String companyName = rs.getString("CompanyName");
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate"));
        String deliveredDate = DateUtils.convertMySQLToUSFormat(rs.getString("DeliveredDate"));
        double orderTotal = rs.getDouble("TotalOrder");
        String paymentDate = rs.getString("PaymentDate"); 

        if (paymentDate == null || paymentDate.trim().equals("")
            || paymentDate.trim().equals("0000-00-00") || paymentDate.trim().equals("11-30-0002")) {
          continue;
        }
        paymentDate = DateUtils.convertMySQLToUSFormat(paymentDate);
        totPayments = totPayments.add(new BigDecimal(orderTotal).setScale(2,BigDecimal.ROUND_HALF_EVEN));
        //totalPayment += orderTotal;

        Hashtable totData = new Hashtable();
        totData.put("Order No", orderNo + "");
        totData.put("Company Name", companyName);
        totData.put("Order Date", orderDate);
        totData.put("Amount", orderTotal + "");
        totData.put("Shipped Date", deliveredDate);
        totData.put("Payment Date", paymentDate);

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Pending Payments ");
      }


      totals[0][0] = "Orders Total";
      totals[0][1] = totPayments.toString();

      toShowSales.put("FileName", fileName);
      toShowSales.put("BackScreen", "VendorOrderDetails");
      toShowSales.put("MainHeading", mainHeading);
      toShowSales.put("SubHeadings", subHeadings);
      toShowSales.put("Data", data);
      toShowSales.put("Totals", totals);

      ReportUtils.createReport(toShowSales);

      rs.close();

      stmt.close();

      con.close();
    } catch (UserException e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    } catch (SQLException e) {
      logger.error(e);
    }
    return toShowSales;
  }

  @SuppressWarnings("unused")
  public static Hashtable showVendorPurchases(UserBean user, String fromDate, String toDate)
      throws UserException {
    Hashtable toShowSales = new Hashtable();
    try {

      String fileName = "VendorPurchases" + fromDate.trim() + toDate.trim() + ".html";

      String mainHeading = "";
      Vector subHeadings = new Vector();
      Vector<Hashtable> data = new Vector<Hashtable>();
      String[][] totals = new String[5][2];

      Connection con = DBInterfaceLocal.getSQLConnection();

      Statement stmt = con.createStatement();
      String sql =
          "SELECT a.OrderNo, b.CompanyName, a.OrderDate, InventoryDoneDate, (a.OrderTotal + a.Discount + a.StickerCharges) as TotalOrder, a.OverheadAmountsTotal FROM VendorOrder a, Vendors b WHERE a.SupplierId=b.SupplierId and IsFinal='Y' and ";
      if (fromDate.trim().equals(toDate.trim())) {
        sql += " InventoryDoneDate='" + DateUtils.convertUSToMySQLFormat(toDate.trim()) + "'";
      } else {
        sql +=
            " InventoryDoneDate>='" + DateUtils.convertUSToMySQLFormat(fromDate.trim())
                + "' AND InventoryDoneDate<='" + DateUtils.convertUSToMySQLFormat(toDate.trim())
                + "' ";
      }

      ResultSet rs = stmt.executeQuery(sql);

      double totOrderTotal = 0.0;
      double totOverHeadAmountsTotal = 0.0;

      mainHeading = "VendorPurchases For The Period From " + fromDate + " To " + toDate;
      subHeadings.addElement("Order No");
      subHeadings.addElement("Company Name");
      subHeadings.addElement("Order Date");
      subHeadings.addElement("Inven date");
      subHeadings.addElement("Amount");
      subHeadings.addElement("Overhead Amount");

      while (rs.next()) {

        if (toShowSales == null) {
          toShowSales = new Hashtable();
        }


        int orderNo = 0;
        String companyName = "";
        String orderDate = "";
        String invenDate = "";
        double amount = 0.0;
        double overheadAmount = 0.0;

        orderNo = rs.getInt("OrderNo");
        companyName = rs.getString("CompanyName");
        orderDate = DateUtils.convertMySQLToUSFormat(rs.getString("OrderDate"));
        invenDate = DateUtils.convertMySQLToUSFormat(rs.getString("InventoryDoneDate"));
        amount = rs.getDouble("TotalOrder");
        overheadAmount = rs.getDouble("OverheadAmountsTotal");

        totOrderTotal += amount;
        totOverHeadAmountsTotal += overheadAmount;

        Hashtable totData = new Hashtable();
        totData.put("Order No", orderNo + "");
        totData.put("Company Name", companyName);
        totData.put("Order Date", orderDate);
        totData.put("Inven date", invenDate);
        totData.put("Amount", amount + "");
        totData.put("Overhead Amount", overheadAmount + "");

        System.out
            .println("________________________________________________________________________________________");

        System.out.println("Order No" + orderNo + "");
        System.out.println("Company Name" + companyName);
        System.out.println("Order Date" + orderDate);
        System.out.println("Inven date" + invenDate);
        System.out.println("Amount" + amount + "");
        System.out.println("Overhead Amount" + overheadAmount + "");

        data.addElement(totData);

      }

      if (toShowSales == null) {
        throw new UserException(" No Purchases For This Period ");
      }


      String totOrderTotalStr = totOrderTotal + "";
      String totOverHeadAmountsTotalStr = totOverHeadAmountsTotal + "";
      String totTotalStr = totOrderTotal + totOverHeadAmountsTotal + "";

      if (totOrderTotalStr.indexOf(".") == totOrderTotalStr.length() - 2) {
        totOrderTotalStr += "0";
      }
      if (totOverHeadAmountsTotalStr.indexOf(".") == totOverHeadAmountsTotalStr.length() - 2) {
        totOverHeadAmountsTotalStr += "0";
      }
      if (totTotalStr.indexOf(".") == totTotalStr.length() - 2) {
        totTotalStr += "0";
      }

      totOrderTotalStr = totOrderTotalStr;
      totOverHeadAmountsTotalStr = totOverHeadAmountsTotalStr;
      totTotalStr = totTotalStr;

      totals[0][0] = "Total Purchase Amount";
      totals[0][1] = totOrderTotalStr;
      totals[1][0] = "&nbsp;";
      totals[1][1] = "&nbsp;";
      totals[2][0] = "Overhead Amounts Total";
      totals[2][1] = totOverHeadAmountsTotalStr;
      totals[3][0] = "&nbsp;";
      totals[3][1] = "&nbsp;";
      totals[4][0] = "Totam Amounts";
      totals[4][1] = totTotalStr;

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
    } catch (NullPointerException e2) {
      logger.error(e2);
      System.out.println(e2.getMessage());
    }

    catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
    return toShowSales;
  }

  public static String getETADate(PartsBean part) {
    String etaDate = "";
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String pno = part.getPartNo();
      int gotDates = 0;
      ResultSet rs =
          stmt.executeQuery("Select a.EstimatedArrivalDate from VendorOrder a, VendorOrderedItems b where a.IsFinal='Y' and a.UpdatedInventory='N' AND a.EstimatedArrivalDate!='' AND a.OrderNo=b.OrderNo AND b.PartNo='"
              + pno + "'");
      while (rs.next()) {
        gotDates++;
        String str = rs.getString(1).trim();
        etaDate += " " + str.substring(5, 7) + "/" + str.substring(8);
      }
      if (gotDates == 0 && !part.getInterchangeNo().trim().equals("")) {
        pno = part.getInterchangeNo();
        ResultSet rs1 =
            stmt.executeQuery("Select a.EstimatedArrivalDate from VendorOrder a, VendorOrderedItems b where a.IsFinal='Y' and a.UpdatedInventory='N' AND a.EstimatedArrivalDate!='' AND a.OrderNo=b.OrderNo AND b.PartNo='"
                + pno + "'");
        while (rs1.next()) {
          String str = rs1.getString(1).trim();
          etaDate += " " + str.substring(5, 7) + "/" + str.substring(8);
        }
        rs1.close();
      }
      rs.close();

      stmt.close();

      con.close();
    } catch (Exception e) {
      // logger.error(e.getMessage());
    }

    return etaDate;
  }

}
