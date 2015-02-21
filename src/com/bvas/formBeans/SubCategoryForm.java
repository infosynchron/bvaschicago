package com.bvas.formBeans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ReportUtils;

public class SubCategoryForm extends ActionForm {
  private static final Logger logger = Logger.getLogger(ReportUtils.class);

  private String buttonClicked = null;

  private String subCategoryCode = null;

  private String subCategoryName = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getSubCategoryCode() {
    if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
      subCategoryCode = getNewSubCategoryCode();
    }
    return (this.subCategoryCode);
  }

  public void setSubCategoryCode(String subCategoryCode) {
    this.subCategoryCode = subCategoryCode;
  }

  public String getSubCategoryName() {
    return (this.subCategoryName);
  }

  public void setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
  }

  public String getNewSubCategoryCode() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    int catCd = 0;
    try {
      Statement stmt = con.createStatement();
      int maxCd = 0;
      ResultSet rs = stmt.executeQuery("select SubCategoryCode from SubCategory");
      while (rs.next()) {
        catCd = rs.getInt(1);
        if (catCd > maxCd) {
          maxCd = catCd;
        }
      }
      maxCd++;
      catCd = maxCd;
      rs.close();
      stmt.close();

      con.close();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      catCd = 1;
    }
    return catCd + "";
  }

  public void reset() {
    subCategoryCode = "";
    subCategoryName = "";
    buttonClicked = "";
  }
}
