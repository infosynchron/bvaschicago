package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.InvoiceBean;
import com.bvas.formBeans.PartsDeliveredForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class PartsDeliveredAction extends Action {
  private static final Logger logger = Logger.getLogger(PartsDeliveredAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    PartsDeliveredForm deliveredForm = (PartsDeliveredForm) form;
    String buttonClicked = deliveredForm.getButtonClicked();
    ((PartsDeliveredForm) deliveredForm).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MakeModelMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("Change")) {
      String whoDelivered = (String) request.getParameter("whoDelivered");
      if (whoDelivered == null || whoDelivered.trim().equals("")
          || whoDelivered.trim().equals("Select")) {
        errorBean.setError("You Must Select An Option");
        session.removeAttribute("DeliveryPendingInvoices");
        deliveredForm.reset();
      } else {
        try {
          changeInvoices(request, whoDelivered);
        } catch (Exception e) {
          errorBean.setError(e.getMessage());
          logger.error(e);
        }
        session.removeAttribute("DeliveryPendingInvoices");
        deliveredForm.reset();
      }
      forwardPage = "PartsDelivered";
    } else if (buttonClicked.equals("BackToRouting")) {
      deliveredForm.reset();
      session.removeAttribute("DeliveryPendingInvoices");
      forwardPage = "RoutingMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      deliveredForm.reset();
      session.removeAttribute("DeliveryPendingInvoices");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("PartsDeliveredError", errorBean);
    } else {
      session.removeAttribute("PartsDeliveredError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void changeInvoices(HttpServletRequest request, String whoDelivered) throws UserException {
    HttpSession session = request.getSession(false);
    Vector pendingInvoices = (Vector) session.getAttribute("DeliveryPendingInvoices");
    if (pendingInvoices == null) {
      throw new UserException("No Invoices Found");
    }

    Hashtable<String, String> checkedInvoices = new Hashtable<String, String>();
    boolean someChecked = false;

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      for (int i = 1; i <= pendingInvoices.size(); i++) {
        String checkedBox = (String) request.getParameter("PD" + i);
        if (checkedBox == null) {
          checkedBox = "";
        } else if (!checkedBox.trim().equals("")) {
          someChecked = true;
        }
        checkedInvoices.put(checkedBox, checkedBox);
        int invNo = 0;

        if (!checkedBox.trim().equals("")) {
          invNo = Integer.parseInt(checkedBox);
        } else {
          invNo = 0;
        }
        if (invNo != 0) {

          Statement stmt = con.createStatement();
          stmt.execute("Insert Into PartsDelivered (InvoiceNumber, Name) Values ('" + invNo
              + "', '" + whoDelivered + "') ");
          if (!InvoiceBean.getInvoice(invNo).getIsDelivered().trim().equalsIgnoreCase("Y")) {
            InvoiceBean.changeIsDelivered(invNo, con);
          }
          stmt.close();
        }
      }// for
      con.close();
    } catch (SQLException e) {
      logger.error(e);
    }

    if (!someChecked) {
      throw new UserException("No Invoices Selected.....");
    }
  }

}
