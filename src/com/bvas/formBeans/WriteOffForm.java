package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class WriteOffForm extends ActionForm {

  private String buttonClicked = null;

  private String invoiceNumber = null;

  private String companyName = null;

  private String balance = null;

  private String writeOffDate = null;

  private String notes = null;

  private String reportFromDate = null;

  private String reportToDate = null;

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

  public String getBalance() {
    return (this.balance);
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public String getWriteOffDate() {
    if (writeOffDate == null || writeOffDate.trim().equals("")) {
      this.writeOffDate = DateUtils.getNewUSDate();
    }
    return (this.writeOffDate);
  }

  public void setWriteOffDate(String writeOffDate) {
    this.writeOffDate = writeOffDate;
  }

  public String getNotes() {
    return (this.notes);
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getReportFromDate() {
    if (reportFromDate == null || reportFromDate.trim().equals("")) {
      this.reportFromDate = DateUtils.getNewUSDate();
    }
    return (this.reportFromDate);
  }

  public void setReportFromDate(String reportFromDate) {
    this.reportFromDate = reportFromDate;
  }

  public String getReportToDate() {
    if (reportToDate == null || reportToDate.trim().equals("")) {
      this.reportToDate = DateUtils.getNewUSDate();
    }
    return (this.reportToDate);
  }

  public void setReportToDate(String reportToDate) {
    this.reportToDate = reportToDate;
  }

  public void reset() {
    invoiceNumber = "";
    balance = "";
    companyName = "";
    writeOffDate = "";
    notes = "";
  }
}
