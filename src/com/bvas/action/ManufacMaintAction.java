package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.formBeans.ManufacMaintForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;

public class ManufacMaintAction extends Action {
  private static final Logger logger = Logger.getLogger(ManufacMaintAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((ManufacMaintForm) form).getButtonClicked();
    ((ManufacMaintForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in ManufacMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("IdGet")) {
      getManufacName(request, form, errorBean);
      forwardPage = "ManufacMaint";
    } else if (buttonClicked.equals("AddNew")) {
      addNew(request, form);
      forwardPage = "ManufacMaint";
      errorBean.setError("MANUFACTURER ADDED SUCCESSFULLY!!!");
    } else if (buttonClicked.equals("Change")) {
      changeOld(request, form);
      forwardPage = "ManufacMaint";
      errorBean.setError("MANUFACTURER CHANGED SUCCESSFULLY!!!");
    } else if (buttonClicked.equals("Remove")) {
      removeOld(request, form);
      forwardPage = "ManufacMaint";
      errorBean.setError("MANUFACTURER REMOVED SUCCESSFULLY!!!");
    } else if (buttonClicked.equals("Previous")) {
      getPrevious(request, form);
      forwardPage = "ManufacMaint";
    } else if (buttonClicked.equals("Next")) {
      getNext(request, form);
      forwardPage = "ManufacMaint";
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("ManufacMaintError", errorBean);
    } else {
      session.removeAttribute("ManufacMaintError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void getManufacName(HttpServletRequest request, ActionForm form, ErrorBean errorBean) {

    ManufacMaintForm mForm = (ManufacMaintForm) form;
    String manufacId = mForm.getManufacturerId();
    String manufacName = mForm.getManufacturerName();
    if (manufacName == null || manufacName.trim().equals("")) {
      manufacName = "XXX";
    }
    ActionError errors = null;
    if (manufacId == null || manufacId.trim().equals("")) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      try {
        String sql =
            "select ManufacturerId, ManufacturerName from Manufacturer where ManufacturerId=? OR ManufacturerName LIKE '"
                + manufacName + "%'";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.clearParameters();
        stmt.setString(1, manufacId.trim());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          mForm.setManufacturerId(rs.getInt("ManufacturerId") + "");
          mForm.setManufacturerName(rs.getString("ManufacturerName"));
        } else {
          errorBean.setError("Manufacturer ID or Name Not Valid. Check Again.");
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (SQLException e) {
        errorBean.setError("Error Occurred when getting Manufacturer Name...Please try later..."
            + e);
        logger.error(e);
      }
    }
    /*
     * if (!errors.empty()) { saveError(request, errors); //return (new
     * ActionForward(mapping.getInput())); }
     */

  }

  public void getPrevious(HttpServletRequest request, ActionForm form) {

    ManufacMaintForm mForm = (ManufacMaintForm) form;
    String manufacId = mForm.getManufacturerId();
    ActionError errors = null;
    if (manufacId == null || manufacId.trim().equals("")) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      try {
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("select ManufacturerId, ManufacturerName from Manufacturer where ManufacturerId=("
                + manufacId.trim() + "-1)");
        if (rs.next()) {
          mForm.setManufacturerId(rs.getInt("ManufacturerId") + "");
          mForm.setManufacturerName(rs.getString("ManufacturerName"));
        } else {
          errors = new ActionError("Manufacturer ID Not Available. Check Again.");
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (SQLException e) {
        errors =
            new ActionError("Error Occurred when getting Manufacturer Name...Please try later...");
        logger.error(e);
      }
    }
  }

  public void getNext(HttpServletRequest request, ActionForm form) {
    ManufacMaintForm mForm = (ManufacMaintForm) form;
    String manufacId = mForm.getManufacturerId();
    ActionError errors = null;
    if (manufacId == null || manufacId.trim().equals("")) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      try {
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("select ManufacturerId, ManufacturerName from Manufacturer where ManufacturerId=("
                + manufacId.trim() + "+1)");
        if (rs.next()) {
          mForm.setManufacturerId(rs.getInt("ManufacturerId") + "");
          mForm.setManufacturerName(rs.getString("ManufacturerName"));
        } else {
          errors = new ActionError("Manufacturer ID Not Available. Check Again.");
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (SQLException e) {
        errors =
            new ActionError("Error Occurred when getting Manufacturer Name...Please try later...");
        logger.error(e);
      }
    }
  }

  public void addNew(HttpServletRequest request, ActionForm form) {
    ManufacMaintForm mForm = (ManufacMaintForm) form;
    String manufacId = mForm.getManufacturerId();
    String manufacName = mForm.getManufacturerName();
    ActionError errors = null;
    if (manufacId == null || manufacId.trim().equals("")) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      try {
        Statement stmt = con.createStatement();
        boolean inserted =
            stmt.execute("Insert into Manufacturer (ManufacturerId, ManufacturerName) values ("
                + manufacId + ", '" + manufacName + "')");
        if (inserted) {
          // mForm.setManufacturerName(rs.getString("ManufacturerName"));
        } else {
          errors = new ActionError("Not Inserted. Check Again.");
        }
        stmt.close();
        con.close();
      } catch (SQLException e) {
        errors = new ActionError("Error When Insert into Manufacturer ...Please try later...");
        logger.error(e);
      }
    }
  }

  public void changeOld(HttpServletRequest request, ActionForm form) {

    ManufacMaintForm mForm = (ManufacMaintForm) form;
    String manufacId = mForm.getManufacturerId();
    String manufacName = mForm.getManufacturerName();
    ActionError errors = null;
    if (manufacId == null || manufacId.trim().equals("")) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      try {
        Statement stmt = con.createStatement();
        int updated =
            stmt.executeUpdate("Update Manufacturer set ManufacturerName='" + manufacName.trim()
                + "' where ManufacturerId=" + manufacId);
        if (updated == 1) {
          // mForm.setManufacturerName(rs.getString("ManufacturerName"));
        } else {
          errors = new ActionError("Manufacturer Not Updated. Check Again.");
        }
        stmt.close();
        con.close();
      } catch (SQLException e) {
        errors = new ActionError("Error Occurred when updating Manufacturer...Please try later...");
        logger.error(e);
      }

    }
  }

  public void removeOld(HttpServletRequest request, ActionForm form) {

    ManufacMaintForm mForm = (ManufacMaintForm) form;
    String manufacId = mForm.getManufacturerId();
    ActionError errors = null;
    if (manufacId == null || manufacId.trim().equals("")) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      try {
        Statement stmt = con.createStatement();
        boolean deleted =
            stmt.execute("delete from Manufacturer where ManufacturerId=" + manufacId.trim());
        if (deleted) {
          mForm.setManufacturerId("");
          mForm.setManufacturerName("");
        } else {
          errors = new ActionError("Manufacturer Not Removed. Check Again.");
        }
        stmt.close();
        con.close();
      } catch (SQLException e) {
        errors = new ActionError("Error Occurred when removing Manufacturer...Please try later...");
        logger.error(e);
      }
    }
  }
}
