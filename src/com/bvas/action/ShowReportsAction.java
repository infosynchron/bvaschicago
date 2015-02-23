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

import com.bvas.formBeans.ShowReportsForm;
import com.bvas.utils.ErrorBean;

public class ShowReportsAction extends Action {
  private static final Logger logger = Logger.getLogger(ShowReportsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((ShowReportsForm) form).getButtonClicked();
    ((ShowReportsForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in TodaysOrdersAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("PrintThisReport")) {
      forwardPage = "ShowReports";
    } else if (buttonClicked.equals("Back")) {
      String backScreen = (String) session.getAttribute("BackScreen");
      if (backScreen == null || backScreen.trim().equals("")) {
        // forwardPage = "TodaysOrders";
        forwardPage = "Login";
      } else {
        forwardPage = backScreen.trim();
      }

    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("TodaysOrdersError", errorBean);
    } else {
      session.removeAttribute("TodaysOrdersError");
    }

    return (mapping.findForward(forwardPage));

  }
}
