package com.bvas.formBeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class LoginForm extends ActionForm {

  private String password = null;

  private String username = null;

  public String getPassword() {
    return (this.password);
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return (this.username);
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {

    this.password = null;
    this.username = null;

  }
}
