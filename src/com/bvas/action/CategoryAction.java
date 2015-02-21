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

import com.bvas.formBeans.CategoryForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class CategoryAction extends Action {
  private static final Logger logger = Logger.getLogger(CategoryAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((CategoryForm) form).getButtonClicked();
    CategoryForm catFrom = (CategoryForm) form;
    catFrom.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.info("No session or no User in CategoryAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      buttonClicked = "IdGet";
    }

    String categoryCode = catFrom.getCategoryCode();

    if (buttonClicked.equals("IdGet")) {
      if (categoryCode == null || categoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int catCode = Integer.parseInt(categoryCode);
          ResultSet rs =
              stmt.executeQuery("Select * from Category Where CategoryCode='" + categoryCode + "'");
          if (rs.next()) {
            catFrom.setCategoryName(rs.getString("CategoryName"));
          } else {
            errorBean.setError("This Category Not Available .....");
          }
          rs.close();
          stmt.close();
          con.close();
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Category Not Available .....");
        }
      }
      forwardPage = "Category";
    } else if (buttonClicked.equals("AddNew")) {
      if (categoryCode == null || categoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int catCode = Integer.parseInt(categoryCode);
          int newCatCode = Integer.parseInt(catFrom.getNewCategoryCode());
          if (catCode > newCatCode) {
            throw new Exception("This Category Code Is Not The Correct One ");
          }
          if (catFrom.getCategoryName() == null || catFrom.getCategoryName().trim().equals("")) {
            throw new Exception("Enter a Description .....");
          }

          stmt.execute("Insert Into Category (CategoryCode, CategoryName) Values ('" + categoryCode
              + "', '" + catFrom.getCategoryName() + "')");
          errorBean.setError("ADDED !!!!!");

          stmt.close();
          con.close();

        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Category Not Available ....." + e.getMessage());
        }
      }
      forwardPage = "Category";
    } else if (buttonClicked.equals("Change")) {
      if (categoryCode == null || categoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int catCode = Integer.parseInt(categoryCode);
          int newCatCode = Integer.parseInt(catFrom.getNewCategoryCode());
          if (catCode >= newCatCode) {
            throw new Exception("This Category Code Is Not Valid .....");
          }

          if (catFrom.getCategoryName() == null || catFrom.getCategoryName().trim().equals("")) {
            throw new Exception("Enter a Description .....");
          }

          stmt.execute("Update Category Set CategoryName='" + catFrom.getCategoryName()
              + "' Where CategoryCode='" + categoryCode + "'");
          errorBean.setError("CHANGED !!!!!");

          stmt.close();
          con.close();

        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Category Not Available ....." + e.getMessage());
        }
      }
      forwardPage = "Category";
    } else if (buttonClicked.equals("Remove")) {
      if (categoryCode == null || categoryCode.trim().equals("")) {
        errorBean.setError("Enter a Category Code .........");
      } else {
        try {
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          int catCode = Integer.parseInt(categoryCode);
          int newCatCode = Integer.parseInt(catFrom.getNewCategoryCode());
          if (catCode >= newCatCode) {
            throw new Exception("This Category Code Is Not Valid .....");
          }

          stmt.execute("Delete From Category Where CategoryCode='" + categoryCode + "'");
          catFrom.reset();
          errorBean.setError("DELETED !!!!!");

          stmt.close();
          con.close();

        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Category Not Available ....." + e.getMessage());
        }
      }
      forwardPage = "Category";
    } else if (buttonClicked.equals("Previous")) {
      if (categoryCode == null || categoryCode.trim().equals("")) {
        categoryCode = "1";
      }
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        int catCode = Integer.parseInt(categoryCode);

        ResultSet rs =
            stmt.executeQuery("Select * from Category Where CategoryCode<" + catCode
                + " Order By CategoryCode Desc");
        if (rs.next()) {
          catFrom.setCategoryCode(rs.getString("CategoryCode"));
          catFrom.setCategoryName(rs.getString("CategoryName"));
        } else {
          errorBean.setError("This is the Beginning of the List .....");
          catFrom.reset();
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("This Category Not Available .....");
      }

      forwardPage = "Category";

    } else if (buttonClicked.equals("Next")) {
      if (categoryCode == null || categoryCode.trim().equals("")) {
        categoryCode = "1";
      }
      try {
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        int catCode = Integer.parseInt(categoryCode);

        ResultSet rs =
            stmt.executeQuery("Select * from Category Where CategoryCode>" + catCode
                + " Order By CategoryCode ");
        if (rs.next()) {
          catFrom.setCategoryCode(rs.getString("CategoryCode"));
          catFrom.setCategoryName(rs.getString("CategoryName"));
        } else {
          errorBean.setError("This is the Last .....");
          catFrom.reset();
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("This Category Not Available .....");
      }

      forwardPage = "Category";
    } else if (buttonClicked.equals("ShowAll")) {
      try {
        Hashtable toShowSales = ReportUtils2.showAllCategories(0);
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No Categories Available .....");
          forwardPage = "Category";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "Category";
      }
    } else if (buttonClicked.equals("ShowSub")) {
      try {
        int catCode = Integer.parseInt(categoryCode);
        int newCatCode = Integer.parseInt(catFrom.getNewCategoryCode());
        if (catCode >= newCatCode) {
          throw new Exception("This Category Code Is Not Valid .....");
        }
        Hashtable toShowSales = ReportUtils2.showAllCategories(catCode);
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No Categories Available .....");
          forwardPage = "Category";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "Category";
      }
    } else if (buttonClicked.equals("Clear")) {
      catFrom.reset();
      forwardPage = "Category";

    } else if (buttonClicked.equals("ReturnToMain")) {
      catFrom.reset();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("CategoryError", errorBean);
    } else {
      session.removeAttribute("CategoryError");
    }

    return (mapping.findForward(forwardPage));

  }

}
