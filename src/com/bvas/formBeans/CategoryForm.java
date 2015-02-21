package com.bvas.formBeans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ReportUtils;

public class CategoryForm extends ActionForm {
  private static final Logger logger = Logger.getLogger(ReportUtils.class);

  private String buttonClicked = null;

  private String categoryCode = null;

  private String categoryName = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getCategoryCode() {
    if (categoryCode == null || categoryCode.trim().equals("")) {
      categoryCode = getNewCategoryCode();
    }
    return (this.categoryCode);
  }

  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }

  public String getCategoryName() {
    return (this.categoryName);
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getNewCategoryCode() {
    Connection con = DBInterfaceLocal.getSQLConnection();
    int catCd = 0;
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select max(CategoryCode) from Category");
      if (rs.next()) {
        catCd = rs.getInt(1);
        catCd++;
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
      catCd = 1;
    }
    return catCd + "";
  }

  public void reset() {
    categoryCode = "";
    categoryName = "";
    buttonClicked = "";
  }
}
