package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.formBeans.SubCategoryForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class SubCategoryAction extends Action {
  private static final Logger logger = Logger.getLogger(SubCategoryAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((SubCategoryForm) form).getButtonClicked();
    SubCategoryForm catForm = (SubCategoryForm) form;
    catForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in CategoryAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      buttonClicked = "IdGet";
    }

    String catCode = (String) request.getParameter("CategorySelected");
    session.setAttribute("CatSelected", catCode);
    int intCatCode = 0;
    try {
      intCatCode = Integer.parseInt(catCode);
    } catch (Exception e) {
      intCatCode = 0;
      logger.error(e);
    }

    String subCategoryCode = catForm.getSubCategoryCode();

    if (buttonClicked.equals("IdGet")) {
      if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int subCatCode = Integer.parseInt(subCategoryCode);
          ResultSet rs =
              stmt.executeQuery("Select * from SubCategory Where SubCategoryCode='"
                  + subCategoryCode + "'");
          if (rs.next()) {
            catForm.setSubCategoryName(rs.getString("SubCategoryName"));
            String xx = rs.getString("CategoryCode");
            session.setAttribute("CatSelected", xx);
          } else {
            errorBean.setError("This Sub Category Not Available .....");
          }
          rs.close();
          stmt.close();
          con.close();
        } catch (Exception e) {
          errorBean.setError("This Sub Category Not Available .....");
          logger.error(e);
        }
      }
      forwardPage = "SubCategory";
    } else if (buttonClicked.equals("AddNew")) {
      if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int subCatCode = Integer.parseInt(subCategoryCode);
          int newSubCatCode = Integer.parseInt(catForm.getNewSubCategoryCode());
          if (subCatCode > newSubCatCode) {
            throw new Exception("This Sub Category Code Is Not The Correct One ");
          }
          if (catForm.getSubCategoryName() == null
              || catForm.getSubCategoryName().trim().equals("")) {
            throw new Exception("Enter a Description .....");
          }

          try {
            Statement stmt1 = con.createStatement();
            ResultSet rs1 =
                stmt1.executeQuery("Select * from Category Where CategoryCode=" + intCatCode);
            if (!rs1.next()) {
              throw new Exception("");
            }
            rs1.close();
            stmt1.close();
          } catch (Exception e) {
            logger.error(e);
            throw new Exception("This Category Code Not Valid ....." + e.getMessage());
          }

          stmt.execute("Insert Into SubCategory (SubCategoryCode, SubCategoryName, CategoryCode) Values ('"
              + subCatCode + "', '" + catForm.getSubCategoryName() + "', '" + catCode + "')");
          errorBean.setError("ADDED !!!!!");

          stmt.close();
          con.close();
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("....." + e.getMessage());
        }
      }
      forwardPage = "SubCategory";
    } else if (buttonClicked.equals("Change")) {
      if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
        errorBean.setError("Enter Sub Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int subCatCode = Integer.parseInt(subCategoryCode);
          int newSubCatCode = Integer.parseInt(catForm.getNewSubCategoryCode());
          if (subCatCode >= newSubCatCode) {
            throw new Exception("This Category Code Is Not Valid .....");
          }

          if (catForm.getSubCategoryName() == null
              || catForm.getSubCategoryName().trim().equals("")) {
            throw new Exception("Enter a Description .....");
          }

          try {
            Statement stmt1 = con.createStatement();
            ResultSet rs1 =
                stmt1.executeQuery("Select * from Category Where CategoryCode=" + intCatCode);
            if (!rs1.next()) {
              throw new Exception("");
            }
            rs1.close();
            stmt1.close();
          } catch (Exception e) {
            logger.error(e);
            throw new Exception("This Category Code Not Valid ....." + e.getMessage());
          }

          stmt.execute("Update SubCategory Set SubCategoryName='" + catForm.getSubCategoryName()
              + "', CategoryCode='" + catCode + "' Where SubCategoryCode='" + subCatCode + "'");
          errorBean.setError("CHANGED !!!!!");

          stmt.close();
          con.close();
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Category Not Available ....." + e.getMessage());
        }
      }
      forwardPage = "SubCategory";
    } else if (buttonClicked.equals("Remove")) {
      if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int subCatCode = Integer.parseInt(subCategoryCode);
          int newSubCatCode = Integer.parseInt(catForm.getNewSubCategoryCode());
          if (subCatCode >= newSubCatCode) {
            throw new Exception("This Category Code Is Not Valid .....");
          }

          stmt.execute("Delete From SubCategory Where SubCategoryCode='" + subCatCode + "'");
          catForm.reset();
          errorBean.setError("DELETED !!!!!");

          stmt.close();
          con.close();

        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Category Not Available ....." + e.getMessage());
        }
      }
      forwardPage = "SubCategory";
    } else if (buttonClicked.equals("ShowAll")) {
      try {
        Hashtable toShowSales = ReportUtils2.showAllCategories(999);
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No Categories Available .....");
          forwardPage = "SubCategory";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "SubCategory";
      }
    } else if (buttonClicked.equals("ShowParts")) {
      try {
        int subCatCode = Integer.parseInt(subCategoryCode);
        int newSubCatCode = Integer.parseInt(catForm.getNewSubCategoryCode());
        // logger.error(subCatCode + " -- " + newSubCatCode);
        if (subCatCode >= newSubCatCode) {
          throw new Exception("This Category Code Is Not Valid .....");
        }
        Hashtable toShowSales = ReportUtils2.showPartsForCat(subCatCode, false);
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No Categories Available .....");
          forwardPage = "SubCategory";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "SubCategory";
      }
    } else if (buttonClicked.equals("ShowInven")) {
      try {
        int subCatCode = Integer.parseInt(subCategoryCode);
        int newSubCatCode = Integer.parseInt(catForm.getNewSubCategoryCode());
        // logger.error(subCatCode + " -- " + newSubCatCode);
        if (subCatCode >= newSubCatCode) {
          throw new Exception("This Category Code Is Not Valid .....");
        }
        Hashtable toShowSales = ReportUtils2.showPartsForCat(subCatCode, true);
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No Categories Available .....");
          forwardPage = "SubCategory";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "SubCategory";
      }
    } else if (buttonClicked.equals("Clear")) {
      session.removeAttribute("CatSelected");
      session.removeAttribute("AllCategories");
      catForm.reset();
      forwardPage = "SubCategory";
    } else if (buttonClicked.equals("Previous")) {
      if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
        subCategoryCode = "1";
      }
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        int subCatCode = Integer.parseInt(subCategoryCode);

        ResultSet rs =
            stmt.executeQuery("Select * from SubCategory Where SubCategoryCode<'" + subCatCode
                + "' Order By SubCategoryCode Desc");
        if (rs.next()) {
          catForm.setSubCategoryCode(rs.getInt("SubCategoryCode") + "");
          catForm.setSubCategoryName(rs.getString("SubCategoryName"));
          session.setAttribute("CatSelected", rs.getString("CategoryCode"));
        } else {
          errorBean.setError("This is the Beginning of the List .....");
          catForm.reset();
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("This Sub Category Not Available .....");
      }

      forwardPage = "SubCategory";

    } else if (buttonClicked.equals("Next")) {
      if (subCategoryCode == null || subCategoryCode.trim().equals("")) {
        subCategoryCode = "1";
      }
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        int subCatCode = Integer.parseInt(subCategoryCode);

        ResultSet rs =
            stmt.executeQuery("Select * from SubCategory Where SubCategoryCode>'" + subCatCode
                + "' Order By SubCategoryCode ");
        if (rs.next()) {
          catForm.setSubCategoryCode(rs.getInt("SubCategoryCode") + "");
          catForm.setSubCategoryName(rs.getString("SubCategoryName"));
          session.setAttribute("CatSelected", rs.getString("CategoryCode"));
        } else {
          errorBean.setError("This is the Beginning of the List .....");
          catForm.reset();
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("This Sub Category Not Available .....");
      }

      forwardPage = "SubCategory";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("CatSelected");
      session.removeAttribute("AllCategories");
      catForm.reset();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("SubCategoryError", errorBean);
    } else {
      session.removeAttribute("SubCategoryError");
    }

    return (mapping.findForward(forwardPage));

  }

}
