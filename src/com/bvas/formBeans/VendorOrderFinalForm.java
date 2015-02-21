package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class VendorOrderFinalForm extends ActionForm {

  private String buttonClicked = null;

  private String orderNo = null;

  private String orderDate = null;

  private String supplierId = null;

  private String supplierName = null;

  private String orderTotal = null;

  private String orderPrice = null;

  private boolean findErrors = false;

  private boolean doInvenUpdate = false;

  private boolean doPriceCal = false;

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

  public String getOrderPrice() {
    return (this.orderPrice);
  }

  public void setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
  }

  public boolean getFindErrors() {
    return (this.findErrors);
  }

  public void setFindErrors(boolean findErrors) {
    this.findErrors = findErrors;
  }

  public boolean getDoInvenUpdate() {
    return (this.doInvenUpdate);
  }

  public void setDoInvenUpdate(boolean doInvenUpdate) {
    this.doInvenUpdate = doInvenUpdate;
  }

  public boolean getDoPriceCal() {
    return (this.doPriceCal);
  }

  public void setDoPriceCal(boolean doPriceCal) {
    this.doPriceCal = doPriceCal;
  }

}
