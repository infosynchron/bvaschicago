package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class MaintainFinanceForm extends ActionForm {

  private String buttonClicked = null;

  private String clientBalance = null;

  private String appliedAmount = null;

  private String addPayment = null;

  private String datePaymentMade = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getClientBalance() {
    return (this.clientBalance);
  }

  public void setClientBalance(String clientBalance) {
    this.clientBalance = clientBalance;
  }

  public String getAppliedAmount() {
    return (this.appliedAmount);
  }

  public void setAppliedAmount(String appliedAmount) {
    this.appliedAmount = appliedAmount;
  }

  public String getAddPayment() {
    return (this.addPayment);
  }

  public void setAddPayment(String addPayment) {
    this.addPayment = addPayment;
  }

  public String getDatePaymentMade() {
    if (this.datePaymentMade == null || this.datePaymentMade.trim().equals("")) {
      this.datePaymentMade = DateUtils.getNewUSDate();
    }
    return (this.datePaymentMade);
  }

  public void setDatePaymentMade(String datePaymentMade) {
    this.datePaymentMade = datePaymentMade;
  }

  public void reset() {
    setClientBalance("");
    setAppliedAmount("");
    setAddPayment("");
    setDatePaymentMade("");
  }

}
