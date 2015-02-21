package com.bvas.beans;

import org.apache.log4j.Logger;

public class VendorOrderedItemsBean {
  private static final Logger logger = Logger.getLogger(VendorOrderedItemsBean.class);

  private int orderNo;

  private String partNo = null;

  private String vendorPartNo = null;

  private int quantity;

  private double price;

  public int getOrderNo() {
    return (this.orderNo);
  }

  public String getPartNo() {
    return (this.partNo);
  }

  public String getVendorPartNo() {
    return (this.vendorPartNo);
  }

  public int getQuantity() {
    return (this.quantity);
  }

  public double getPrice() {
    return (this.price);
  }

  public void setOrderNo(int orderNo) {
    this.orderNo = orderNo;
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setVendorPartNo(String vendorPartNo) {
    this.vendorPartNo = vendorPartNo;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setPrice(double price) {
    this.price = price;
  }

}
