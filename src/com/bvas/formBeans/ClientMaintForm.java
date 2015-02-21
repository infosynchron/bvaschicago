package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

public class ClientMaintForm extends ActionForm {

  private String buttonClicked = null;

  private String clientId = null;

  private String companyName = null;

  private String address1 = null;

  private String address2 = null;

  private String city = null;

  private String state = null;

  private String postalCode = null;

  private String region = null;

  private String country = null;

  private String contactName = null;

  private String contactTitle = null;

  private String phone = null;

  private String fax = null;

  private String ext = null;

  private String terms = null;

  private boolean taxId = false;

  private String taxIdNumber = null;

  private String notes = null;

  private String amountVowed = null;

  private String paymentTerms = null;

  private String creditBalance = null;

  private String creditLimit = null;

  private String customerLevel = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getClientId() {
    return (this.clientId);
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getCompanyName() {
    return (this.companyName);
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getAddress1() {
    return (this.address1);
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return (this.address2);
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return (this.city);
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return (this.state);
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostalCode() {
    return (this.postalCode);
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return (this.country);
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getRegion() {
    return (this.region);
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getContactName() {
    return (this.contactName);
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactTitle() {
    return (this.contactTitle);
  }

  public void setContactTitle(String contactTitle) {
    this.contactTitle = contactTitle;
  }

  public String getPhone() {
    return (this.phone);
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFax() {
    return (this.fax);
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getExt() {
    return (this.ext);
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public String getTerms() {
    return (this.terms);
  }

  public void setTerms(String terms) {
    this.terms = terms;
  }

  public boolean getTaxId() {
    return (this.taxId);
  }

  public void setTaxId(boolean taxId) {
    this.taxId = taxId;
  }

  public String getTaxIdNumber() {
    return (this.taxIdNumber);
  }

  public void setTaxIdNumber(String taxIdNumber) {
    this.taxIdNumber = taxIdNumber;
  }

  public String getNotes() {
    return (this.notes);
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getAmountVowed() {
    return (this.amountVowed);
  }

  public void setAmountVowed(String amountVowed) {
    this.amountVowed = amountVowed;
  }

  public String getCreditBalance() {
    return (this.creditBalance);
  }

  public void setCreditBalance(String creditBalance) {
    this.creditBalance = creditBalance;
  }

  public String getCreditLimit() {
    return (this.creditLimit);
  }

  public void setCreditLimit(String creditLimit) {
    this.creditLimit = creditLimit;
  }

  public String getCustomerLevel() {
    return (this.customerLevel);
  }

  public void setCustomerLevel(String customerLevel) {
    this.customerLevel = customerLevel;
  }

  public String getPaymentTerms() {
    return (this.paymentTerms);
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public void resetForm() {
    setClientId("");
    setCompanyName("");
    setContactName("");
    setContactTitle("");
    setTerms("");
    setNotes("");
    setTaxId(false);
    setTaxIdNumber("");
    setPaymentTerms("");
    setAddress1("");
    setAddress2("");
    setCity("");
    setState("");
    setRegion("");
    setPostalCode("");
    setCountry("");
    setPhone("");
    setExt("");
    setFax("");
    setCreditBalance("");
    setCreditLimit("");
    setCustomerLevel("");
  }

}
