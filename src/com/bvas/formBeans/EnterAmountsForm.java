package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class EnterAmountsForm extends ActionForm {

  private String buttonClicked = null;

  private String invoiceNumber = null;

  private String companyName = null;

  private String creditOrBalance = null;

  private String appliedAmount = null;

  private String balance = null;

  private String payingAmount = null;

  private String checkNo = null;

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

  public String getCompanyName() {
    return (this.companyName);
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCreditOrBalance() {
    return (this.creditOrBalance);
  }

  public void setCreditOrBalance(String creditOrBalance) {
    this.creditOrBalance = creditOrBalance;
  }

  public String getAppliedAmount() {
    return (this.appliedAmount);
  }

  public void setAppliedAmount(String appliedAmount) {
    this.appliedAmount = appliedAmount;
  }

  public String getBalance() {
    return (this.balance);
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public String getPayingAmount() {
    return (this.payingAmount);
  }

  public void setPayingAmount(String payingAmount) {
    this.payingAmount = payingAmount;
  }

  public String getCheckNo() {
    return (this.checkNo);
  }

  public void setCheckNo(String checkNo) {
    this.checkNo = checkNo;
  }

  public void reset() {
    invoiceNumber = "";
    companyName = "";
    creditOrBalance = "";
    appliedAmount = "";
    balance = "";
    payingAmount = "";
    checkNo = "";
  }
}
