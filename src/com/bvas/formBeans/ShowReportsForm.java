package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class ShowReportsForm extends ActionForm {

  private String buttonClicked = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

}
