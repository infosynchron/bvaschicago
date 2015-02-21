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

import com.bvas.beans.UserBean;
import com.bvas.formBeans.InvenMaintMenuForm;
import com.bvas.utils.ErrorBean;

public class InvenMaintMenuAction extends Action {
  private static final Logger logger = Logger.getLogger(InvenMaintMenuAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((InvenMaintMenuForm) form).getButtonClicked();
    ((InvenMaintMenuForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in InvenMaintMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    logger.error(new java.util.Date(System.currentTimeMillis()) + "-----InvenMaintMenu-----"
        + user.getUsername());

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.trim().equals("MaintainInventory")) {
      forwardPage = "InvenMaint";
    } else if (buttonClicked.trim().equals("ShowPattern")) {
      forwardPage = "ShowPattern";
    } else if (buttonClicked.trim().equals("DamagedParts")) {
      forwardPage = "DamagedParts";
    } else if (buttonClicked.trim().equals("MissingParts")) {
      forwardPage = "MissingParts";
    } else if (buttonClicked.trim().equals("PartHistory")) {
      forwardPage = "PartHistory";
    } else if (buttonClicked.trim().equals("PartChanges")) {
      forwardPage = "PartChanges";
    } else if (buttonClicked.trim().equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("InvenMaintMenuError", errorBean);
    } else {
      session.removeAttribute("InvenMaintMenuError");
    }

    return (mapping.findForward(forwardPage));

  }

}
