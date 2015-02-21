package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class InvoiceArchForm extends ActionForm {

  private String buttonClicked = null;

  private String invoiceNumber = null;

  private String orderDate = null;

  private String customerId = null;

  private String shippedVia = null;

  private String salesPerson = null;

  private String invoiceTime = null;

  private String companyName = null;

  private String invoiceTotal = null;

  private String appliedAmount = null;

  private String discount = null;

  private String clientBalance = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getInvoiceNumber() {
    return (this.invoiceNumber);
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public String getOrderDate() {
    return (this.orderDate);
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getCustomerId() {
    return (this.customerId);
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getShippedVia() {
    return (this.shippedVia);
  }

  public void setShippedVia(String shippedVia) {
    this.shippedVia = shippedVia;
  }

  public String getSalesPerson() {
    return (this.salesPerson);
  }

  public void setSalesPerson(String salesPerson) {
    this.salesPerson = salesPerson;
  }

  public String getInvoiceTime() {
    return (this.invoiceTime);
  }

  public void setInvoiceTime(String invoiceTime) {
    this.invoiceTime = invoiceTime;
  }

  public String getCompanyName() {
    return (this.companyName);
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getInvoiceTotal() {
    return (this.invoiceTotal);
  }

  public void setInvoiceTotal(String invoiceTotal) {
    this.invoiceTotal = invoiceTotal;
  }

  public String getAppliedAmount() {
    return (this.appliedAmount);
  }

  public void setAppliedAmount(String appliedAmount) {
    this.appliedAmount = appliedAmount;
  }

  public String getDiscount() {
    return (this.discount);
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getClientBalance() {
    return (this.clientBalance);
  }

  public void setClientBalance(String clientBalance) {
    this.clientBalance = clientBalance;
  }

}
