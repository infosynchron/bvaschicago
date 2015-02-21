package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class PartsDeliveredForm extends ActionForm {

  private String buttonClicked = null;

  private String pendingInvoices = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getPendingInvoices() {
    return (this.pendingInvoices);
  }

  public void setPendingInvoices(String pendingInvoices) {
    this.pendingInvoices = pendingInvoices;
  }

  public void reset() {
    pendingInvoices = "";
  }
}
