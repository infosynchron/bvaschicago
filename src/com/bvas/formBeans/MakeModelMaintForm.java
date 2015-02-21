package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class MakeModelMaintForm extends ActionForm {

  private String buttonClicked = null;

  private String makeModelCode = null;

  private String makeModelName = null;

  private String manufacturerId = null;

  private String interChangeModel = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getMakeModelCode() {
    return (this.makeModelCode);
  }

  public void setMakeModelCode(String makeModelCode) {
    this.makeModelCode = makeModelCode;
  }

  public String getMakeModelName() {
    return (this.makeModelName);
  }

  public void setMakeModelName(String makeModelName) {
    this.makeModelName = makeModelName;
  }

  public String getManufacturerId() {
    return (this.manufacturerId);
  }

  public void setManufacturerId(String manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public String getInterChangeModel() {
    return (this.interChangeModel);
  }

  public void setInterChangeModel(String interChangeModel) {
    this.interChangeModel = interChangeModel;
  }

}
