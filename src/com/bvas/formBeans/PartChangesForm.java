package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class PartChangesForm extends ActionForm {

  private String buttonClicked = null;

  private String partNo = null;

  private String fromDate = null;

  private String toDate = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getPartNo() {
    return (this.partNo);
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public String getFromDate() {
    return (this.fromDate);
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    if (toDate == null || toDate.trim().equals("")) {
      toDate = DateUtils.getNewUSDate();
    }
    return (this.toDate);
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }
}
