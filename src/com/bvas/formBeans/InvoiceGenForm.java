package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class InvoiceGenForm extends ActionForm {

  private String buttonClicked = null;

  private String orderDate = null;

  private String customerId = null;

  private String salesPerson = null;

  private String returnedInvoice = null;

  private String paymentTerms = null;

  private String creditBalance = null;

  private String shipCustomerName = null;

  private String shipAttention = null;

  private String shipAddress1 = null;

  private String shipAddress2 = null;

  private String shipCity = null;

  private String shipState = null;

  private String shipZip = null;

  private String shipRegion = null;

  private String shipCountry = null;

  private String billCustomerName = null;

  private String billAttention = null;

  private String billAddress1 = null;

  private String billAddress2 = null;

  private String billCity = null;

  private String billState = null;

  private String billZip = null;

  private String billRegion = null;

  private String billCountry = null;

  private boolean addressSame;

  private String invoiceTotal = null;

  private String invoiceTax = null;

  private String discount = null;

  private String amountVowed = null;

  private String notes = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getOrderDate() {
    if (this.orderDate == null || this.orderDate.trim().equals("")) {
      // String ordDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // ordDate = sdf.format(dd);
      // this.orderDate = ordDate;
      this.orderDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.orderDate);
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getCustomerId() {
    return (this.customerId);
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getSalesPerson() {
    return (this.salesPerson);
  }

  public void setSalesPerson(String salesPerson) {
    this.salesPerson = salesPerson;
  }

  public String getReturnedInvoice() {
    return (this.returnedInvoice);
  }

  public void setReturnedInvoice(String returnedInvoice) {
    this.returnedInvoice = returnedInvoice;
  }

  public String getPaymentTerms() {
    return (this.paymentTerms);
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public String getCreditBalance() {
    return (this.creditBalance);
  }

  public void setCreditBalance(String creditBalance) {
    this.creditBalance = creditBalance;
  }

  public String getShipCustomerName() {
    return (this.shipCustomerName);
  }

  public void setShipCustomerName(String shipCustomerName) {
    this.shipCustomerName = shipCustomerName;
  }

  public String getShipAttention() {
    return (this.shipAttention);
  }

  public void setShipAttention(String shipAttention) {
    this.shipAttention = shipAttention;
  }

  public String getShipAddress1() {
    return (this.shipAddress1);
  }

  public void setShipAddress1(String shipAddress1) {
    this.shipAddress1 = shipAddress1;
  }

  public String getShipAddress2() {
    return (this.shipAddress2);
  }

  public void setShipAddress2(String shipAddress2) {
    this.shipAddress2 = shipAddress2;
  }

  public String getShipCity() {
    return (this.shipCity);
  }

  public void setShipCity(String shipCity) {
    this.shipCity = shipCity;
  }

  public String getShipState() {
    return (this.shipState);
  }

  public void setShipState(String shipState) {
    this.shipState = shipState;
  }

  public String getShipZip() {
    return (this.shipZip);
  }

  public void setShipZip(String shipZip) {
    this.shipZip = shipZip;
  }

  public String getShipRegion() {
    return (this.shipRegion);
  }

  public void setShipRegion(String shipRegion) {
    this.shipRegion = shipRegion;
  }

  public String getShipCountry() {
    return (this.shipCountry);
  }

  public void setShipCountry(String shipCountry) {
    this.shipCountry = shipCountry;
  }

  public String getBillCustomerName() {
    return (this.billCustomerName);
  }

  public void setBillCustomerName(String billCustomerName) {
    this.billCustomerName = billCustomerName;
  }

  public String getBillAttention() {
    return (this.billAttention);
  }

  public void setBillAttention(String billAttention) {
    this.billAttention = billAttention;
  }

  public String getBillAddress1() {
    return (this.billAddress1);
  }

  public void setBillAddress1(String billAddress1) {
    this.billAddress1 = billAddress1;
  }

  public String getBillAddress2() {
    return (this.billAddress2);
  }

  public void setBillAddress2(String billAddress2) {
    this.billAddress2 = billAddress2;
  }

  public String getBillCity() {
    return (this.billCity);
  }

  public void setBillCity(String billCity) {
    this.billCity = billCity;
  }

  public String getBillState() {
    return (this.billState);
  }

  public void setBillState(String billState) {
    this.billState = billState;
  }

  public String getBillZip() {
    return (this.billZip);
  }

  public void setBillZip(String billZip) {
    this.billZip = billZip;
  }

  public String getBillRegion() {
    return (this.billRegion);
  }

  public void setBillRegion(String billRegion) {
    this.billRegion = billRegion;
  }

  public String getBillCountry() {
    return (this.billCountry);
  }

  public void setBillCountry(String billCountry) {
    this.billCountry = billCountry;
  }

  public boolean getAddressSame() {
    return (this.addressSame);
  }

  public void setAddressSame(boolean addressSame) {
    this.addressSame = addressSame;
  }

  public void reset() {
    setAddressSame(false);
    setAmountVowed("");
    setDiscount("");
    setReturnedInvoice("");
    setBillAddress1("");
    setBillAddress2("");
    setBillAttention("");
    setBillCity("");
    setBillCountry("");
    setBillCustomerName("");
    setBillRegion("");
    setBillState("");
    setBillZip("");
    setCustomerId("");
    setPaymentTerms("");
    setCreditBalance("");
    setInvoiceTax("");
    setInvoiceTotal("");
    setOrderDate("");
    setSalesPerson("");
    setNotes("");
    setShipAddress1("");
    setShipAddress2("");
    setShipAttention("");
    setShipCity("");
    setShipCountry("");
    setShipCustomerName("");
    setShipRegion("");
    setShipState("");
    setShipZip("");
  }

  public String getInvoiceTotal() {
    return (this.invoiceTotal);
  }

  public void setInvoiceTotal(String invoiceTotal) {
    this.invoiceTotal = invoiceTotal;
  }

  public String getInvoiceTax() {
    return (this.invoiceTax);
  }

  public void setInvoiceTax(String invoiceTax) {
    this.invoiceTax = invoiceTax;
  }

  public String getDiscount() {
    return (this.discount);
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getAmountVowed() {
    return (this.amountVowed);
  }

  public void setAmountVowed(String amountVowed) {
    this.amountVowed = amountVowed;
  }

  public String getNotes() {
    return (this.notes);
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

}
