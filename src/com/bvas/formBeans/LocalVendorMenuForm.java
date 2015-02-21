package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class LocalVendorMenuForm extends ActionForm {

  private String buttonClicked = null;

  private String vendorSelect = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getVendorSelect() {
    return (this.vendorSelect);
  }

  public void setVendorSelect(String vendorSelect) {
    this.vendorSelect = vendorSelect;
  }

}
