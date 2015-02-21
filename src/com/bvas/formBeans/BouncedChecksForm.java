package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class BouncedChecksForm extends ActionForm {

  private String buttonClicked = null;

  private String checkId = null;

  private String customerId = null;

  private String companyName = null;

  private String enteredDate = null;

  private String checkNo = null;

  private String checkDate = null;

  private String bouncedAmount = null;

  private String returnsCheckFee = null;

  private String totalBalance = null;

  private boolean isCleared = false;

  private String paidAmount = null;

  private String balance = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getCheckId() {
    return (this.checkId);
  }

  public void setCheckId(String checkId) {
    this.checkId = checkId;
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

  public String getEnteredDate() {
    if (this.enteredDate == null || this.enteredDate.trim().equals("")) {
      enteredDate = DateUtils.getNewUSDate();
    }
    return (this.enteredDate);
  }

  public void setEnteredDate(String enteredDate) {
    this.enteredDate = enteredDate;
  }

  public String getCheckNo() {
    return (this.checkNo);
  }

  public void setCheckNo(String checkNo) {
    this.checkNo = checkNo;
  }

  public String getCheckDate() {
    /*
     * if (this.checkDate == null || this.checkDate.trim().equals("")) { checkDate =
     * DateUtils.getNewUSDate(); }
     */
    return (this.checkDate);
  }

  public void setCheckDate(String checkDate) {
    this.checkDate = checkDate;
  }

  public String getBouncedAmount() {
    return (this.bouncedAmount);
  }

  public void setBouncedAmount(String bouncedAmount) {
    this.bouncedAmount = bouncedAmount;
  }

  public String getReturnsCheckFee() {
    if (returnsCheckFee == null || returnsCheckFee.trim().equals("")) {
      returnsCheckFee = "25.0";
    }
    return (this.returnsCheckFee);
  }

  public void setReturnsCheckFee(String returnsCheckFee) {
    this.returnsCheckFee = returnsCheckFee;
  }

  public String getTotalBalance() {
    return (this.totalBalance);
  }

  public void setTotalBalance(String totalBalance) {
    this.totalBalance = totalBalance;
  }

  public boolean getIsCleared() {
    return (this.isCleared);
  }

  public void setIsCleared(boolean isCleared) {
    this.isCleared = isCleared;
  }

  public String getPaidAmount() {
    return (this.paidAmount);
  }

  public void setPaidAmount(String paidAmount) {
    this.paidAmount = paidAmount;
  }

  public String getBalance() {
    return (this.balance);
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public void reset() {
    checkId = "";
    customerId = "";
    companyName = "";
    enteredDate = "";
    checkNo = "";
    checkDate = "";
    bouncedAmount = "";
    returnsCheckFee = "";
    totalBalance = "";
    isCleared = false;
    paidAmount = "";
    balance = "";
  }
}
