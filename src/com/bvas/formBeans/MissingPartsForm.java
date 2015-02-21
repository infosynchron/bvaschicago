package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class MissingPartsForm extends ActionForm {

  private String buttonClicked = null;

  private String partNo = null;

  private String partOrder = null;

  private String unitsInStock = null;

  private String quantity = null;

  private String dateEntered = null;

  private String whoEntered = null;

  private String partDescription = null;

  private String costPrice = null;

  private String actualPrice = null;

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

  public String getQuantity() {
    return (this.quantity);
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getUnitsInStock() {
    return (this.unitsInStock);
  }

  public void setUnitsInStock(String unitsInStock) {
    this.unitsInStock = unitsInStock;
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

  public String getActualPrice() {
    return (this.actualPrice);
  }

  public void setActualPrice(String actualPrice) {
    this.actualPrice = actualPrice;
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
    setPartNo("");
    setPartOrder("");
    setQuantity("");
    setUnitsInStock("");
    setDateEntered("");
    setWhoEntered("");
    setCostPrice("");
    setActualPrice("");
    setPartDescription("");
  }

}
