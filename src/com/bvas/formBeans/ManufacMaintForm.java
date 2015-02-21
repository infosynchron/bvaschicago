package com.bvas.formBeans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DBInterfaceLocal;

public class ManufacMaintForm extends ActionForm {

  private String buttonClicked = null;

  private String manufacturerId = null;

  private String manufacturerName = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getManufacturerId() {
    if (manufacturerId == null || manufacturerId.trim().equals("")) {
      manufacturerId = getNewManufacId();
    }
    return (this.manufacturerId);
  }

  public void setManufacturerId(String manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public String getManufacturerName() {
    return (this.manufacturerName);
  }

  public void setManufacturerName(String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }

  public String getNewManufacId() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    int manufacId = 0;
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select max(ManufacturerId) from Manufacturer");
      if (rs.next())
        manufacId = Integer.parseInt(rs.getString(1));
      manufacId++;
      rs.close();
      stmt.close();

      con.close();
    } catch (SQLException e) {
    }
    return manufacId + "";
  }
}
