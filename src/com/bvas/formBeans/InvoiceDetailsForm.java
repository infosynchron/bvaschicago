package com.bvas.formBeans;

import java.io.Serializable;

import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;

public class InvoiceDetailsForm implements Serializable {

  private String partNo = "";

  private String makeModelName = "";

  private String partDescription = "";

  private String year = "";

  private String unitsInStock = "";

  private String costPrice = "";

  private String listPrice = "";

  private int quantity = 1;

  public String getPartNo() {
    return (this.partNo);
  }

  public String getPartDescription() {
    return (this.partDescription);
  }

  public String getMakeModelName() {
    return (this.makeModelName);
  }

  public String getCostPrice() {
    return (this.costPrice);
  }

  public String getListPrice() {
    return (this.listPrice);
  }

  public String getUnitsInStock() {
    return (this.unitsInStock);
  }

  public String getYear() {
    return (this.year);
  }

  public int getQuantity() {
    return (this.quantity);
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setPartDescription(String partDescription) {
    this.partDescription = partDescription;
  }

  public void setMakeModelName(String makeModelName) {
    this.makeModelName = makeModelName;
  }

  public void setCostPrice(String costPrice) {
    this.costPrice = costPrice;
  }

  public void setListPrice(String listPrice) {
    this.listPrice = listPrice;
  }

  public void setUnitsInStock(String unitsInStock) {
    this.unitsInStock = unitsInStock;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setParts(PartsBean part, int customerLevel) {
    setPartNo(part.getPartNo());
    setMakeModelName(MakeModelBean.getMakeModelName(part.getMakeModelCode()));
    setPartDescription(part.getPartDescription());
    setYear(part.getYear());
    setCostPrice(part.getCostPrice(customerLevel) + "");
    setListPrice(part.getListPrice() + "");
    setUnitsInStock(part.getUnitsInStock() + "");
    setQuantity(1);
  }

}
