package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class InvenMaintForm_1 extends ActionForm {

  private String buttonClicked = null;

  private String partNo = null;

  private String interchangeNo = null;

  private String partDescription = null;

  private String makeModelName = null;

  private String year = null;

  private String supplierId = null;

  private String costPrice = null;

  private String listPrice = null;

  private String actualPrice = null;

  private String discount = null;

  private String unitsInStock = null;

  private String unitsOnOrder = null;

  private String reorderLevel = null;

//  private String compPrice1 = null;
//
//  private String compPrice2 = null;
//
//  private String compPrice3 = null;

  private String keystoneNumber = null;

  private String oemNumber = null;

  private String location = null;

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

  public String getInterchangeNo() {
    return (this.interchangeNo);
  }

  public void setInterchangeNo(String interchangeNo) {
    this.interchangeNo = interchangeNo;
  }

  public String getPartDescription() {
    return (this.partDescription);
  }

  public void setPartDescription(String partDescription) {
    this.partDescription = partDescription;
  }

  public String getMakeModelName() {
    return (this.makeModelName);
  }

  public void setMakeModelName(String makeModelName) {
    this.makeModelName = makeModelName;
  }

  public String getYear() {
    return (this.year);
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getSupplierId() {
    return (this.supplierId);
  }

  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  public String getCostPrice() {
    return (this.costPrice);
  }

  public void setCostPrice(String costPrice) {
    this.costPrice = costPrice;
  }

  public String getListPrice() {
    return (this.listPrice);
  }

  public void setListPrice(String listPrice) {
    this.listPrice = listPrice;
  }

  public String getActualPrice() {
    return (this.actualPrice);
  }

  public void setActualPrice(String actualPrice) {
    this.actualPrice = actualPrice;
  }

  public String getDiscount() {
    return (this.discount);
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getUnitsInStock() {
    return (this.unitsInStock);
  }

  public void setUnitsInStock(String unitsInStock) {
    this.unitsInStock = unitsInStock;
  }

  public String getUnitsOnOrder() {
    return (this.unitsOnOrder);
  }

  public void setUnitsOnOrder(String unitsOnOrder) {
    this.unitsOnOrder = unitsOnOrder;
  }

  public String getReorderLevel() {
    return (this.reorderLevel);
  }

  public void setReorderLevel(String reorderLevel) {
    this.reorderLevel = reorderLevel;
  }

//  public String getCompPrice1() {
//    return (this.compPrice1);
//  }
//
//  public void setCompPrice1(String compPrice1) {
//    this.compPrice1 = compPrice1;
//  }
//
//  public String getCompPrice2() {
//    return (this.compPrice2);
//  }

//  public void setCompPrice2(String compPrice2) {
//    this.compPrice2 = compPrice2;
//  }
//
//  public String getCompPrice3() {
//    return (this.compPrice3);
//  }
//
//  public void setCompPrice3(String compPrice3) {
//    this.compPrice3 = compPrice3;
//  }

  public String getKeystoneNumber() {
    return (this.keystoneNumber);
  }

  public void setKeystoneNumber(String keystoneNumber) {
    this.keystoneNumber = keystoneNumber;
  }

  public String getOemNumber() {
    return (this.oemNumber);
  }

  public void setOemNumber(String oemNumber) {
    this.oemNumber = oemNumber;
  }

  public String getLocation() {
    return (this.location);
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void reset() {
//    setCompPrice1("");
//    setCompPrice2("");
//    setCompPrice3("");
    setCostPrice("");
    setInterchangeNo("");
    setKeystoneNumber("");
    setListPrice("");
    setActualPrice("");
    setDiscount("");
    setLocation("");
    setMakeModelName("");
    setOemNumber("");
    setPartDescription("");
    setPartNo("");
    setReorderLevel("");
    setSupplierId("");
    setUnitsInStock("");
    setUnitsOnOrder("");
    setYear("");
  }

}
