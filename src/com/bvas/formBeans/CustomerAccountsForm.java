package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class CustomerAccountsForm extends ActionForm {

  private String buttonClicked = null;

  private String customerId = null;

  private String companyName = null;

  private String routeNo = null;

  private String pendingInvoices = null;

  private String amountPending = null;

  private String bouncedChecksAmount = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getCustomerId() {
    return (this.customerId);
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getCompanyName() {
    return (this.companyName);
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getRouteNo() {
    return (this.routeNo);
  }

  public void setRouteNo(String routeNo) {
    this.routeNo = routeNo;
  }

  public String getPendingInvoices() {
    return (this.pendingInvoices);
  }

  public void setPendingInvoices(String pendingInvoices) {
    this.pendingInvoices = pendingInvoices;
  }

  public String getAmountPending() {
    return (this.amountPending);
  }

  public void setAmountPending(String amountPending) {
    this.amountPending = amountPending;
  }

  public String getBouncedChecksAmount() {
    return (this.bouncedChecksAmount);
  }

  public void setBouncedChecksAmount(String bouncedChecksAmount) {
    this.bouncedChecksAmount = bouncedChecksAmount;
  }

  public void reset() {
    customerId = "";
    companyName = "";
    routeNo = "";
    pendingInvoices = "";
    amountPending = "";
    bouncedChecksAmount = "";
  }
}
