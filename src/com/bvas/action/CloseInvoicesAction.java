package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
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
import com.bvas.formBeans.CloseInvoicesForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.ReportUtils;
import com.bvas.utils.UserException;

public class CloseInvoicesAction extends Action {
  private static final Logger logger = Logger.getLogger(CloseInvoicesAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    CloseInvoicesForm closingForm = (CloseInvoicesForm) form;
    String buttonClicked = closingForm.getButtonClicked();
    ((CloseInvoicesForm) closingForm).setButtonClicked("");
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
    } else if (buttonClicked.indexOf("History") != -1) {
      try {
        int invNo = 0;
        try {
          invNo = Integer.parseInt(buttonClicked.substring(7));
        } catch (Exception e) {
          logger.error(e);
          invNo = 0;
        }
        if (InvoiceBean.isAvailable(invNo)) {
          Hashtable invoiceHistory = ReportUtils.createHistoryReport(invNo);
          if (invoiceHistory != null) {
            session.setAttribute("toShowReports", invoiceHistory);
          } else {
            throw new UserException("No History Available For This Invoice");
          }
        } else {
          throw new UserException("Please Check the InvoiceNumber");
        }

        forwardPage = "ShowReports";

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
        forwardPage = "CloseInvoices";
      }
    } else if (buttonClicked.equals("CloseInvoices")) {
      try {
        closeInvoices(request);
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "CloseInvoices";
    } else if (buttonClicked.equals("CloseAll")) {
      try {
        closeAllInvoices(request);
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      closingForm.reset();
      session.removeAttribute("AllPendingInvoices");
      session.removeAttribute("AllCheckedInvoices");
      forwardPage = "CloseInvoices";
    } else if (buttonClicked.equals("Clear")) {
      closingForm.reset();
      session.removeAttribute("AllPendingInvoices");
      session.removeAttribute("AllCheckedInvoices");
      forwardPage = "CloseInvoices";
    } else if (buttonClicked.equals("BackToAcct")) {
      closingForm.reset();
      session.removeAttribute("AllPendingInvoices");
      session.removeAttribute("AllCheckedInvoices");
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      closingForm.reset();
      session.removeAttribute("AllPendingInvoices");
      session.removeAttribute("AllCheckedInvoices");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("CloseInvoicesError", errorBean);
    } else {
      session.removeAttribute("CloseInvoicesError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void closeInvoices(HttpServletRequest request) throws UserException {
    HttpSession session = request.getSession(false);
    Vector pendingInvoices = (Vector) session.getAttribute("AllPendingInvoices");
    if (pendingInvoices == null) {
      throw new UserException("No Invoices Found");
    }

    Hashtable<String, String> checkedInvoices = new Hashtable<String, String>();
    boolean someChecked = false;
    for (int i = 1; i <= pendingInvoices.size(); i++) {
      String checkedBox = (String) request.getParameter("CB" + i);
      if (checkedBox == null) {
        checkedBox = "";
      } else if (!checkedBox.trim().equals("")) {
        someChecked = true;
      }
      checkedInvoices.put(checkedBox, checkedBox);
      int invNo = 0;
      try {
        if (!checkedBox.trim().equals("")) {
          invNo = Integer.parseInt(checkedBox);
        } else {
          invNo = 0;
        }
        if (invNo != 0) {
          InvoiceBean.closeInvoice(invNo);
        }
      } catch (Exception e) {
        logger.error(e);
      }

    }
    if (someChecked) {
      session.setAttribute("AllCheckedInvoices", checkedInvoices);
    } else {
      throw new UserException("No Invoices Selected.....");
    }
  }

  public void closeAllInvoices(HttpServletRequest request) throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      stmt.execute("Update Invoice Set Status='C' where Balance=0 and Status!='C' and Status!='W' ");
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }
  }

}
