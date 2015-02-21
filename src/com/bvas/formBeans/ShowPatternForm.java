package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class ShowPatternForm extends ActionForm {

  private String buttonClicked = null;

  private String partNo = null;

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

}
