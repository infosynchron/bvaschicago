package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class VendorOurPartForm extends ActionForm {

  private String buttonClicked = null;

  private String tempNo = null;

  private String supplierId = null;

  private String supplierName = null;

  private String noOrdering = null;

  private String totalItems = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getTempNo() {
    return (this.tempNo);
  }

  public void setTempNo(String tempNo) {
    this.tempNo = tempNo;
  }

  public String getSupplierId() {
    return (this.supplierId);
  }

  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  public String getSupplierName() {
    return (this.supplierName);
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public String getTotalItems() {
    return (this.totalItems);
  }

  public void setTotalItems(String totalItems) {
    this.totalItems = totalItems;
  }

  public String getNoOrdering() {
    return (this.noOrdering);
  }

  public void setNoOrdering(String noOrdering) {
    this.noOrdering = noOrdering;
  }

}
