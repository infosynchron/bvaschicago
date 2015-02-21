package com.bvas.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.formBeans.OtherMaintForm;
import com.bvas.utils.ErrorBean;

public class OtherMaintAction extends Action {
  private static final Logger logger = Logger.getLogger(OtherMaintAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((OtherMaintForm) form).getButtonClicked();
    ((OtherMaintForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in OtherMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.trim().equals("MakeModelMaint")) {
      forwardPage = "MakeModelMaint";
    } else if (buttonClicked.trim().equals("ManufacMaint")) {
      forwardPage = "ManufacMaint";
    } else if (buttonClicked.trim().equals("Category")) {
      forwardPage = "Category";
    } else if (buttonClicked.trim().equals("SubCategory")) {
      forwardPage = "SubCategory";
    } else if (buttonClicked.trim().equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("OtherMaintError", errorBean);
    } else {
      session.removeAttribute("OtherMaintError");
    }

    return (mapping.findForward(forwardPage));

  }
}
