package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.beans.LocalVendorBean;

public class LocalVendorMaintForm extends ActionForm {

  private String buttonClicked = null;

  private String supplierId = null;

  private String companyName = null;

  private String contactName = null;

  private String contactTitle = null;

  private String address1 = null;

  private String address2 = null;

  private String city = null;

  private String region = null;

  private String postalCode = null;

  private String country = null;

  private String phone = null;

  private String fax = null;

  private String homePage = null;

  private String email = null;

  private String comments = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getSupplierId() {
    if (supplierId == null || supplierId.trim().equals("")) {
      this.supplierId = LocalVendorBean.getMaxSupplierId() + "";
    }
    return (this.supplierId);
  }

  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  public String getCompanyName() {
    return (this.companyName);
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
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

  public String getRegion() {
    return (this.region);
  }

  public void setRegion(String region) {
    this.region = region;
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

  public String getHomePage() {
    return (this.homePage);
  }

  public void setHomePage(String homePage) {
    this.homePage = homePage;
  }

  public String getEmail() {
    return (this.email);
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getComments() {
    return (this.comments);
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public void reset() {
    setSupplierId("");
    setCompanyName("");
    setContactName("");
    setContactTitle("");
    setHomePage("");
    setEmail("");
    setComments("");
    setPhone("");
    setFax("");
    setAddress1("");
    setAddress2("");
    setCity("");
    setRegion("");
    setPostalCode("");
    setCountry("");

  }

}
