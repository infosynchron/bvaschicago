package com.bvas.utils;

import org.apache.log4j.Logger;

public class LabelValueBean {
  private static final Logger logger = Logger.getLogger(LabelValueBean.class);

  public LabelValueBean(String label, String value) {
    this.label = label;
    this.value = value;
  }

  protected String label = null;

  public String getLabel() {
    return (this.label);
  }

  protected String value = null;

  public String getValue() {
    return (this.value);
  }

  public String toString() {
    StringBuffer sb = new StringBuffer("LabelValueBean[");
    sb.append(this.label);
    sb.append(", ");
    sb.append(this.value);
    sb.append("]");
    return (sb.toString());
  }

}
