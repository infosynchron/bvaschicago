package com.bvas.beans;

import java.io.Serializable;

public class InvoiceDeliveryBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private int invoicenumber;

  private String salesperson;

  private String customername;

  private Long invoicetime;

  private String region;

  private String isdelivered;
  
  private String notes;

  public int getInvoicenumber() {
    return invoicenumber;
  }

  public void setInvoicenumber(int invoicenumber) {
    this.invoicenumber = invoicenumber;
  }

  public String getSalesperson() {
    return salesperson;
  }

  public void setSalesperson(String salesperson) {
    this.salesperson = salesperson;
  }

  public String getCustomername() {
    return customername;
  }

  public void setCustomername(String customername) {
    this.customername = customername;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getIsdelivered() {
    return isdelivered;
  }

  public void setIsdelivered(String isdelivered) {
    this.isdelivered = isdelivered;
  }

  public Long getInvoicetime() {
    return invoicetime;
  }

  public void setInvoicetime(Long invoicetime) {
    this.invoicetime = invoicetime;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

}
