package com.bvas.beans;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class ErrorLogBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = Logger.getLogger(ErrorLogBean.class);

  private String dateTime = null;

  private String username = null;

  private String errorMessage = null;

  public String getDateTime() {
    return (this.dateTime);
  }

  public String getUsername() {
    return (this.username);
  }

  public String getErrorMessage() {
    return (this.errorMessage);
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
