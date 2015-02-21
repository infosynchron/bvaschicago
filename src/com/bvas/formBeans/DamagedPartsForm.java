package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class DamagedPartsForm extends ActionForm {

  private String buttonClicked = null;

  private String partNo = null;

  private String partOrder = null;

  private String partDescription = null;

  private String costPrice = null;

  private String suggestedDiscount = null;

  private String actualPrice = null;

  private String damagedDesc = null;

  private String dateEntered = null;

  private String whoEntered = null;

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

  public String getPartOrder() {
    return (this.partOrder);
  }

  public void setPartOrder(String partOrder) {
    this.partOrder = partOrder;
  }

  public String getPartDescription() {
    return (this.partDescription);
  }

  public void setPartDescription(String partDescription) {
    this.partDescription = partDescription;
  }

  public String getCostPrice() {
    return (this.costPrice);
  }

  public void setCostPrice(String costPrice) {
    this.costPrice = costPrice;
  }

  public String getSuggestedDiscount() {
    return (this.suggestedDiscount);
  }

  public void setSuggestedDiscount(String suggestedDiscount) {
    this.suggestedDiscount = suggestedDiscount;
  }

  public String getActualPrice() {
    return (this.actualPrice);
  }

  public void setActualPrice(String actualPrice) {
    this.actualPrice = actualPrice;
  }

  public String getDamagedDesc() {
    return (this.damagedDesc);
  }

  public void setDamagedDesc(String damagedDesc) {
    this.damagedDesc = damagedDesc;
  }

  public String getDateEntered() {
    return (this.dateEntered);
  }

  public void setDateEntered(String dateEntered) {
    this.dateEntered = dateEntered;
  }

  public String getWhoEntered() {
    return (this.whoEntered);
  }

  public void setWhoEntered(String whoEntered) {
    this.whoEntered = whoEntered;
  }

  public void reset() {
    setCostPrice("");
    setSuggestedDiscount("");
    setActualPrice("");
    setPartDescription("");
    setPartNo("");
    setPartOrder("");
    setDamagedDesc("");
    setDateEntered("");
    setWhoEntered("");
  }

}
