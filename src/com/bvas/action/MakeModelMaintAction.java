package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.MakeModelBean;
import com.bvas.formBeans.MakeModelMaintForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class MakeModelMaintAction extends Action {
  private static final Logger logger = Logger.getLogger(MakeModelMaintAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    MakeModelMaintForm mForm = (MakeModelMaintForm) form;
    String buttonClicked = mForm.getButtonClicked();
    ((MakeModelMaintForm) mForm).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MakeModelMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    String makeModelCode = mForm.getMakeModelCode();
    String makeModelName = mForm.getMakeModelName();
    String manufacId = mForm.getManufacturerId();
    String interChangeModel = mForm.getInterChangeModel();
    MakeModelBean makeModelBean = new MakeModelBean();
    makeModelBean.setMakeModelCode(makeModelCode);
    makeModelBean.setMakeModelName(makeModelName);
    makeModelBean.setInterChangeModel(interChangeModel);
    try {
      makeModelBean.setManufacturerId(Integer.parseInt(manufacId));
    } catch (Exception e) {
      logger.error(e);
      makeModelBean.setManufacturerId(0);
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("Get")) {
      try {
        makeModelBean.getMakeModel();
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      mForm.setMakeModelCode(makeModelBean.getMakeModelCode());
      mForm.setMakeModelName(makeModelBean.getMakeModelName());
      mForm.setManufacturerId(makeModelBean.getManufacturerId() + "");
      mForm.setInterChangeModel(makeModelBean.getInterChangeModel());
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("AddNew")) {
      try {
        makeModelBean.addNewMakeModel();
        errorBean.setError("THIS MODEL ADDED SUCCESSFULLY");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("Clear")) {
      mForm.setMakeModelName("");
      mForm.setMakeModelCode("");
      mForm.setManufacturerId("");
      mForm.setInterChangeModel("");
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("Change")) {
      try {
        makeModelBean.changeMakeModel();
        errorBean.setError("THIS MODEL CHANGED SUCCESSFULLY");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("Remove")) {
      try {
        makeModelBean.removeMakeModel();
        mForm.setMakeModelName("");
        mForm.setMakeModelCode("");
        mForm.setManufacturerId("");
        mForm.setInterChangeModel("");
        errorBean.setError("THIS MODEL REMOVED SUCCESSFULLY");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("Previous")) {
      try {
        getPrevious(makeModelBean, request, mForm);
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("Next")) {
      try {
        getNext(makeModelBean, request, mForm);
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("MakeModelMaintError", errorBean);
    } else {
      session.removeAttribute("MakeModelMaintError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void getPrevious(MakeModelBean makeModelBean, HttpServletRequest request,
      MakeModelMaintForm form) throws UserException {

    String makeModelCode = makeModelBean.getMakeModelCode();
    int manufacId = makeModelBean.getManufacturerId();
    if (manufacId == 0) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = null;
      ResultSet rs = null;
      try {
        stmt = con.createStatement();
        rs =
            stmt.executeQuery("select MakeModelCode, MakeModelName, ManufacturerId, InterChangeModel from MakeModel where MakeModelCode < '"
                + makeModelCode.trim()
                + "' AND ManufacturerId="
                + manufacId
                + " Order by MakeModelCode desc");
        if (rs.next()) {
          String newCode = rs.getString("MakeModelCode");
          String newName = rs.getString("MakeModelName");
          int id = rs.getInt("ManufacturerId");
          String interChange = rs.getString("InterChangeModel");
          form.setMakeModelCode(newCode);
          form.setMakeModelName(newName);
          form.setManufacturerId(id + "");
          form.setInterChangeModel(interChange);
        } else {
          throw new UserException("This is the First Item for this Make");
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (SQLException e) {
        logger.error(e);
      } finally {
        try {
          if (rs != null) {
            rs.close();
          }
          if (stmt != null) {
            stmt.close();
          }
        } catch (Exception e) {
          logger.error(e);
        }
      }

    }
  }

  public void getNext(MakeModelBean makeModelBean, HttpServletRequest request,
      MakeModelMaintForm form) throws UserException {

    String makeModelCode = makeModelBean.getMakeModelCode();
    int manufacId = makeModelBean.getManufacturerId();
    if (manufacId == 0) {
    } else {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = null;
      ResultSet rs = null;
      try {
        stmt = con.createStatement();
        rs =
            stmt.executeQuery("select MakeModelCode, MakeModelName, ManufacturerId, InterChangeModel from MakeModel where MakeModelCode > '"
                + makeModelCode.trim() + "' AND ManufacturerId=" + manufacId + "");
        if (rs.next()) {
          String newCode = rs.getString("MakeModelCode");
          String newName = rs.getString("MakeModelName");
          int id = rs.getInt("ManufacturerId");
          String interChange = rs.getString("InterChangeModel");
          form.setMakeModelCode(newCode);
          form.setMakeModelName(newName);
          form.setManufacturerId(id + "");
          form.setInterChangeModel(interChange);
        } else {
          throw new UserException("This is the Last Item for this Make");
        }
        rs.close();
        stmt.close();
        con.close();
      } catch (SQLException e) {
        logger.error(e);
      } finally {
        try {
          if (rs != null) {
            rs.close();
          }
          if (stmt != null) {
            stmt.close();
          }
        } catch (Exception e) {
          logger.error(e);
        }
      }
    }
  }

}
