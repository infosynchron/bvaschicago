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

import com.bvas.formBeans.CheckOtherInvenForm;
import com.bvas.utils.ErrorBean;

public class CheckOtherInvenAction extends Action {
  private static final Logger logger = Logger.getLogger(CheckOtherInvenAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((CheckOtherInvenForm) form).getButtonClicked();
    ((CheckOtherInvenForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in ManufacMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked.equals("Back")) {
      session.removeAttribute("OthersInventory");
      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("OthersInventory");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("CheckOtherInvenError", errorBean);
    } else {
      session.removeAttribute("CheckOtherInvenError");
    }

    return (mapping.findForward(forwardPage));

  }

}
