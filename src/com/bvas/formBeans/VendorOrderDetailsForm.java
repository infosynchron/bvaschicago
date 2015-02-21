package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class VendorOrderDetailsForm extends ActionForm {

  private String buttonClicked = null;

  private String orderNo = null;

  private String companyName = null;

  private String containerNo = null;

  private String supplInvNo = null;

  private String orderDate = null;

  private String shipDate = null;

  private String arrivedDate = null;

  private boolean isFinal = false;

  private String totalItems = null;

  private String orderTotal = null;

  private String discount = null;

  private String stickerCharges = null;

  private String overheadAmountsTotal = null;

  private String unitsOrderDoneDate = null;

  private String pricesDoneDate = null;

  private String inventoryDoneDate = null;

  private String paymentTerms = null;

  private String paymentDate = null;

  private String estimatedArrivalDate = null;

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

  public String getCompanyName() {
    return (this.companyName);
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getContainerNo() {
    return (this.containerNo);
  }

  public void setContainerNo(String containerNo) {
    this.containerNo = containerNo;
  }

  public String getSupplInvNo() {
    return (this.supplInvNo);
  }

  public void setSupplInvNo(String supplInvNo) {
    this.supplInvNo = supplInvNo;
  }

  public String getOrderDate() {
    return (this.orderDate);
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getShipDate() {
    return (this.shipDate);
  }

  public void setShipDate(String shipDate) {
    this.shipDate = shipDate;
  }

  public String getArrivedDate() {
    return (this.arrivedDate);
  }

  public void setArrivedDate(String arrivedDate) {
    this.arrivedDate = arrivedDate;
  }

  public String getTotalItems() {
    return (this.totalItems);
  }

  public void setTotalItems(String totalItems) {
    this.totalItems = totalItems;
  }

  public String getOrderTotal() {
    return (this.orderTotal);
  }

  public void setOrderTotal(String orderTotal) {
    this.orderTotal = orderTotal;
  }

  public String getDiscount() {
    return (this.discount);
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getStickerCharges() {
    return (this.stickerCharges);
  }

  public void setStickerCharges(String stickerCharges) {
    this.stickerCharges = stickerCharges;
  }

  public String getOverheadAmountsTotal() {
    return (this.overheadAmountsTotal);
  }

  public void setOverheadAmountsTotal(String overheadAmountsTotal) {
    this.overheadAmountsTotal = overheadAmountsTotal;
  }

  public String getUnitsOrderDoneDate() {
    return (this.unitsOrderDoneDate);
  }

  public void setUnitsOrderDoneDate(String unitsOrderDoneDate) {
    this.unitsOrderDoneDate = unitsOrderDoneDate;
  }

  public String getPricesDoneDate() {
    return (this.pricesDoneDate);
  }

  public void setPricesDoneDate(String pricesDoneDate) {
    this.pricesDoneDate = pricesDoneDate;
  }

  public String getInventoryDoneDate() {
    return (this.inventoryDoneDate);
  }

  public void setInventoryDoneDate(String inventoryDoneDate) {
    this.inventoryDoneDate = inventoryDoneDate;
  }

  public String getPaymentTerms() {
    return (this.paymentTerms);
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public String getPaymentDate() {
    return (this.paymentDate);
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public boolean getIsFinal() {
    return (this.isFinal);
  }

  public void setIsFinal(boolean isFinal) {
    this.isFinal = isFinal;
  }

  public String getEstimatedArrivalDate() {
    return (this.estimatedArrivalDate);
  }

  public void setEstimatedArrivalDate(String estimatedArrivalDate) {
    this.estimatedArrivalDate = estimatedArrivalDate;
  }

  public void reset() {
    orderNo = "";
    companyName = "";
    containerNo = "";
    supplInvNo = "";
    orderDate = "";
    shipDate = "";
    arrivedDate = "";
    isFinal = false;
    totalItems = "";
    orderTotal = "";
    discount = "";
    stickerCharges = "";
    overheadAmountsTotal = "";
    unitsOrderDoneDate = "";
    pricesDoneDate = "";
    inventoryDoneDate = "";
    paymentTerms = "";
    paymentDate = "";
    estimatedArrivalDate = "";
  }
}
