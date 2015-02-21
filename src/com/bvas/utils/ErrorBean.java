package com.bvas.utils;

import org.apache.log4j.Logger;

public class ErrorBean {
  private static final Logger logger = Logger.getLogger(ErrorBean.class);

  private String error = null;

  public String getError() {
    return this.error;
  }

  public void setError(String error) {
    if (this.error == null) {
      this.error = error;
    } else {
      this.error += "<BR/>" + error;
    }
  }

}
