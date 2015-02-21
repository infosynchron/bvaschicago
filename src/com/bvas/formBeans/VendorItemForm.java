package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class VendorItemForm extends ActionForm {

  private String buttonClicked = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  private String supplierId;

  private String partNo = null;

  private String vendorPartNo = null;

  private String itemDesc1 = null;

  private String itemDesc2 = null;

  private String plNo = null;

  private String oemNo = null;

  private String sellingRate;

  private String noOfPieces;

  private String itemSize;

  private String itemSizeUnits;

  public String getSupplierId() {
    return (this.supplierId);
  }

  public String getPartNo() {
    return (this.partNo);
  }

  public String getVendorPartNo() {
    return (this.vendorPartNo);
  }

  public String getItemDesc1() {
    return (this.itemDesc1);
  }

  public String getItemDesc2() {
    return (this.itemDesc2);
  }

  public String getPlNo() {
    return (this.plNo);
  }

  public String getOemNo() {
    return (this.oemNo);
  }

  public String getSellingRate() {
    return (this.sellingRate);
  }

  public String getNoOfPieces() {
    return (this.noOfPieces);
  }

  public String getItemSize() {
    return (this.itemSize);
  }

  public String getItemSizeUnits() {
    return (this.itemSizeUnits);
  }

  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  public void setPartNo(String partNo) {
    this.partNo = partNo;
  }

  public void setVendorPartNo(String vendorPartNo) {
    this.vendorPartNo = vendorPartNo;
  }

  public void setItemDesc1(String itemDesc1) {
    this.itemDesc1 = itemDesc1;
  }

  public void setItemDesc2(String itemDesc2) {
    this.itemDesc2 = itemDesc2;
  }

  public void setPlNo(String plNo) {
    this.plNo = plNo;
  }

  public void setOemNo(String oemNo) {
    this.oemNo = oemNo;
  }

  public void setSellingRate(String sellingRate) {
    this.sellingRate = sellingRate;
  }

  public void setNoOfPieces(String noOfPieces) {
    this.noOfPieces = noOfPieces;
  }

  public void setItemSize(String itemSize) {
    this.itemSize = itemSize;
  }

  public void setItemSizeUnits(String itemSizeUnits) {
    this.itemSizeUnits = itemSizeUnits;
  }

  public void resetForm() {
    setSupplierId("");
    setPartNo("");
    setVendorPartNo("");
    setItemDesc1("");
    setItemDesc2("");
    setPlNo("");
    setOemNo("");
    setSellingRate("");
    setItemSize("");
    setItemSizeUnits("");
    setNoOfPieces("");

  }

}
