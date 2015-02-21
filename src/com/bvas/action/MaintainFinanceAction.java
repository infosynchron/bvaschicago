package com.bvas.action;

import java.io.IOException;
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

import com.bvas.beans.FinMaintInv;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.MaintainFinanceForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.UserException;

public class MaintainFinanceAction extends Action {
  private static final Logger logger = Logger.getLogger(MaintainFinanceAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    MaintainFinanceForm finForm = (MaintainFinanceForm) form;
    String buttonClicked = finForm.getButtonClicked();
    finForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MaintainFinanceAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    InvoiceBean invoice = null;
    /*
     * if (session.getAttribute("InvoiceBean") != null) { invoice = (InvoiceBean)
     * session.getAttribute("InvoiceBean"); }
     */

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetInvoice")) {

      String custId = request.getParameter("customer");
      if (custId != null && (!custId.trim().equals(""))) {
        session.setAttribute("FinanceCustomer", custId.trim());
        PrintUtils.createFinanceNotice(custId.trim());
        FinMaintInv fin = new FinMaintInv();
        Hashtable ht = fin.getInvoices(custId);
        session.setAttribute("InvoiceForFin", ht);
      } else {
        logger.error("No customerId available");
      }

      String invNoStr = request.getParameter("invoice");
      if (invNoStr != null && (!invNoStr.trim().equals(""))) {
        session.setAttribute("FinanceInvoice", invNoStr);
        try {

          invoice = InvoiceBean.getInvoice(Integer.parseInt(invNoStr.trim()));
          finForm.setAppliedAmount(invoice.getAppliedAmount() + "");
          finForm.setClientBalance(invoice.getBalance() + "");
          session.setAttribute("InvoiceBean", invoice);

        } catch (UserException e) {
          errorBean.setError("In MaintainFinanceForm - Error" + e);
          logger.error(e);
        }

      } else {
        session.removeAttribute("InvoiceBean");
      }
      forwardPage = "MaintainFinance";
    } else if (buttonClicked.equals("NewSearch")) {
      session.removeAttribute("FinanceCustomer");
      session.removeAttribute("FinanceInvoice");
      session.removeAttribute("InvoiceForFin");
      session.removeAttribute("InvoiceBean");
      finForm.reset();
      forwardPage = "MaintainFinance";
    } else if (buttonClicked.equals("AddPayment")) {
      try {
        UserBean user = (UserBean) session.getAttribute("User");
        if (user.getRole().trim().equalsIgnoreCase("High")
            || user.getRole().trim().equalsIgnoreCase("Acct")) {
          addPayment(finForm, request);
        } else {
          errorBean
              .setError("You cann't update the add payment field. <BR/>Contact Accounting person instead");
        }
      } catch (UserException e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }
      forwardPage = "MaintainFinance";
    } else if (buttonClicked.equals("PrintFinanceNotice")) {
      String custId = (String) request.getParameter("customer");
      logger.error("Going to print Invoice Notice for the customer" + custId);
      forwardPage = "MaintainFinance";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("FinanceCustomer");
      session.removeAttribute("FinanceInvoice");
      session.removeAttribute("InvoiceForFin");
      session.removeAttribute("InvoiceBean");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("MaintainFinanceError", errorBean);
    } else {
      session.removeAttribute("MaintainFinanceError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void addPayment(MaintainFinanceForm finForm, HttpServletRequest request)
      throws UserException {
    HttpSession session = request.getSession(false);
    Object oops = session.getAttribute("InvoiceBean");
    if (oops != null) {
      InvoiceBean invoice = (InvoiceBean) oops;
      double amount = 0;
      String date = "";
      try {
        amount = Double.parseDouble(finForm.getAddPayment());
      } catch (Exception e) {
        logger.error(e);
        throw new UserException("Payment Not Added");
      }
      date = finForm.getDatePaymentMade();
      invoice.addPayment(invoice.getInvoiceNumber(), amount, date, "", "");
      invoice.setBalance(invoice.getBalance() - amount);
      finForm.setAppliedAmount(invoice.getAppliedAmount() + "");
      finForm.setClientBalance(invoice.getBalance() + "");
      PrintUtils.createFinanceNotice(invoice.getCustomerId());
      PrintUtils.createFinanceStatement();
    } else {
      logger.error("In MaintainFinanceAction - No InvoiceBean found in session");
    }
  }
}
