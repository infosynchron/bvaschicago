package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class VendorOrderForm extends ActionForm {

  private String buttonClicked = null;

  private String orderNo = null;

  private String orderDate = null;

  private String supplierId = null;

  private String supplierName = null;

  private String orderTotal = null;

  private String totalItems = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getOrderNo() {
    return (this.orderNo);
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getOrderDate() {
    if (this.orderDate == null || this.orderDate.trim().equals("")) {
      orderDate = DateUtils.getNewUSDate();
    }
    return (this.orderDate);
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getSupplierId() {
    return (this.supplierId);
  }

  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  public String getSupplierName() {
    return (this.supplierName);
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public String getOrderTotal() {
    return (this.orderTotal);
  }

  public void setOrderTotal(String orderTotal) {
    this.orderTotal = orderTotal;
  }

  public String getTotalItems() {
    return (this.totalItems);
  }

  public void setTotalItems(String totalItems) {
    this.totalItems = totalItems;
  }

}
