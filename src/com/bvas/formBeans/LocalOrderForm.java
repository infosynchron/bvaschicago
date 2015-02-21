package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class LocalOrderForm extends ActionForm {

  private String buttonClicked = null;

  private String invoiceNo = null;

  private String dateEntered = "";

  private String partNo = "";

  private String localVendorNo = "";

  private String quantity = null;

  private String price = null;

  private String vendInvNo = "";

  private String vendInvDate = "";

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getInvoiceNo() {
    return (this.invoiceNo);
  }

  public String getDateEntered() {
    if (this.dateEntered == null || this.dateEntered.trim().equals("")) {
      dateEntered = DateUtils.getNewUSDate();
    }
    return (this.dateEntered);
  }

  public String getPartNo() {
    return (this.partNo);
  }

  public String getLocalVendorNo() {
    return (this.localVendorNo);
  }

  public String getQuantity() {
    return (this.quantity);
  }

  public String getPrice() {
    return (this.price);
  }

  public String getVendInvNo() {
    return (this.vendInvNo);
  }

  public String getVendInvDate() {
    return (this.vendInvDate);
  }

  public void setInvoiceNo(String invoiceNo) {
    this.invoiceNo = invoiceNo;
  }

  public void setDateEntered(String dateEntered) {
    this.dateEntered = dateEntered;
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setLocalVendorNo(String localVendorNo) {
    this.localVendorNo = localVendorNo;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public void setVendInvNo(String vendInvNo) {
    this.vendInvNo = vendInvNo;
  }

  public void setVendInvDate(String vendInvDate) {
    this.vendInvDate = vendInvDate;
  }

  public void reset() {
    invoiceNo = "";
    dateEntered = "";
    partNo = "";
    localVendorNo = "";
    quantity = "";
    price = "";
    vendInvNo = "";
    vendInvDate = "";
  }

}
