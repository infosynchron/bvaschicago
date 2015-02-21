package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.beans.FaxBean;
import com.bvas.utils.DateUtils;

public class FaxCoverForm extends ActionForm {

  private String buttonClicked = null;

  private String faxNo = null;

  private String toWhom = null;

  private String faxTo = null;

  private String pages = null;

  private String faxPriority = null;

  private String fromWhom = null;

  private String phoneTo = null;

  private String faxDate = null;

  private String attention = null;

  private String comments = null;

  private String commentsSize = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getFaxNo() {
    if (faxNo == null || faxNo.trim().equals("") || (Integer.parseInt(faxNo) == 0)) {
      this.faxNo = FaxBean.getNewFaxNo() + "";
    }
    return (this.faxNo);
  }

  public void setFaxNo(String faxNo) {
    this.faxNo = faxNo;
  }

  public String getToWhom() {
    return (this.toWhom);
  }

  public void setToWhom(String toWhom) {
    this.toWhom = toWhom;
  }

  public String getFaxTo() {
    return (this.faxTo);
  }

  public void setFaxTo(String faxTo) {
    this.faxTo = faxTo;
  }

  public String getPages() {
    return (this.pages);
  }

  public void setPages(String pages) {
    this.pages = pages;
  }

  public String getFaxPriority() {
    return (this.faxPriority);
  }

  public void setFaxPriority(String faxPriority) {
    this.faxPriority = faxPriority;
  }

  public String getFromWhom() {
    return (this.fromWhom);
  }

  public void setFromWhom(String fromWhom) {
    this.fromWhom = fromWhom;
  }

  public String getPhoneTo() {
    return (this.phoneTo);
  }

  public void setPhoneTo(String phoneTo) {
    this.phoneTo = phoneTo;
  }

  public String getFaxDate() {
    if (faxDate == null || faxDate.equals("")) {
      faxDate = DateUtils.getNewUSDate();
    }
    return (this.faxDate);
  }

  public void setFaxDate(String faxDate) {
    this.faxDate = faxDate;
  }

  public String getAttention() {
    return (this.attention);
  }

  public void setAttention(String attention) {
    this.attention = attention;
  }

  public String getComments() {
    return (this.comments);
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getCommentsSize() {
    return (this.commentsSize);
  }

  public void setCommentsSize(String commentsSize) {
    this.commentsSize = commentsSize;
  }

}
