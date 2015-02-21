package com.bvas.beans;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class UserBean implements Serializable {
  private static final Logger logger = Logger.getLogger(UserBean.class);

  private String username = null;

  private String password = null;

  private String role = null;

  public String getUsername() {
    return (this.username);
  }

  public String getPassword() {
    return (this.password);
  }

  public String getRole() {
    return (this.role);
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
