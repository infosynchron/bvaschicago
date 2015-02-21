package com.bvas.action;

import java.io.IOException;
import java.util.Enumeration;
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

import com.bvas.beans.BouncedChecksBean;
import com.bvas.beans.CustomerBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.formBeans.CustomerAccountsForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.UserException;

public class CustomerAccountsAction extends Action {
  private static final Logger logger = Logger.getLogger(CustomerAccountsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    CustomerAccountsForm custAcctForm = (CustomerAccountsForm) form;
    String buttonClicked = custAcctForm.getButtonClicked();
    ((CustomerAccountsForm) custAcctForm).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MakeModelMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    // UserBean user = (UserBean) session.getAttribute("User");
    // logger.error(new java.util.Date(System.currentTimeMillis())
    // + "-----CustomerAccounts-----" + user.getUsername());

    String customerId = custAcctForm.getCustomerId();

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetCustomer")) {
      try {
        if (customerId == null || customerId.trim().equals("")) {
          custAcctForm.reset();
          throw new Exception("You Must Enter The Customer Account Number");
        } else if (CustomerBean.isAvailable(customerId)) {
          CustomerBean custBean = CustomerBean.getCustomer(customerId);
          String companyName = custBean.getCompanyName();
          String routeNo = custBean.getAddress().getRegion();
          custAcctForm.setRouteNo(routeNo);
          custAcctForm.setCompanyName(companyName);

          Vector<InvoiceBean> pendingInvoices = InvoiceBean.getPendingInvoices(customerId);
          Vector<BouncedChecksBean> bouncedChecks =
              BouncedChecksBean.getAllBouncedChecks(customerId);

          if (pendingInvoices == null && bouncedChecks == null) {
            throw new UserException("*** No Pending Invoices For This Customer ***");
          }

          double totPend = 0.0;
          if (pendingInvoices != null) {
            custAcctForm.setPendingInvoices(pendingInvoices.size() + "");
            Enumeration<InvoiceBean> ennum = pendingInvoices.elements();
            double amountPending = 0.0;
            while (ennum.hasMoreElements()) {
              InvoiceBean invoice = ennum.nextElement();
              amountPending += invoice.getBalance();
            }

            if (amountPending != 0) {
              totPend = amountPending;
            }
            custAcctForm.setAmountPending(amountPending + "");
            session.setAttribute("CustomerAccountsInvoices", pendingInvoices);
          }

          double bouncedCheckAmount = 0;
          if (bouncedChecks != null) {
            Enumeration<BouncedChecksBean> enum1 = bouncedChecks.elements();
            while (enum1.hasMoreElements()) {
              BouncedChecksBean bouncedCheck = enum1.nextElement();
              bouncedCheckAmount += bouncedCheck.getBalance();
              totPend += bouncedCheckAmount;
              custAcctForm.setBouncedChecksAmount(bouncedCheckAmount + "");
            }
            session.setAttribute("CustomerAccountsBouncedChecks", bouncedChecks);
          }

          if (totPend != 0 && totPend != custBean.getCreditBalance()) {
            totPend = custBean.getCreditOrBalance();
          }

        } else {
          custAcctForm.reset();
          session.removeAttribute("CustomerAccountsInvoices");
          throw new UserException("This Customer Not Available.....");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "CustomerAccounts";
    } else if (buttonClicked.equals("CreateStatement")) {
      try {
        createStatement(custAcctForm, request);
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "CustomerAccounts";
    } else if (buttonClicked.equals("Clear")) {
      custAcctForm.reset();
      session.removeAttribute("CustomerAccountsInvoices");
      session.removeAttribute("CheckedInvoices");
      session.removeAttribute("CustomerAccountsBouncedChecks");
      forwardPage = "CustomerAccounts";
    } else if (buttonClicked.equals("PrintThisReport")) {
      /*
       * try { createStatement(custAcctForm, request); } catch (Exception e) {
       * errorBean.setError(e.getMessage()); }
       */
      forwardPage = "CustomerAccounts";
    } else if (buttonClicked.equals("BackToAcct")) {
      custAcctForm.reset();
      session.removeAttribute("CustomerAccountsInvoices");
      session.removeAttribute("CheckedInvoices");
      session.removeAttribute("CustomerAccountsBouncedChecks");
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      custAcctForm.reset();
      session.removeAttribute("CustomerAccountsInvoices");
      session.removeAttribute("CheckedInvoices");
      session.removeAttribute("CustomerAccountsBouncedChecks");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("CustomerAccountsError", errorBean);
    } else {
      session.removeAttribute("CustomerAccountsError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void createStatement(CustomerAccountsForm custAcctForm, HttpServletRequest request)
      throws UserException {
    HttpSession session = request.getSession(false);
    Vector pendingInvoices = (Vector) session.getAttribute("CustomerAccountsInvoices");
    Object oops = session.getAttribute("CustomerAccountsBouncedChecks");

    if (pendingInvoices == null && oops == null) {
      custAcctForm.reset();
      throw new UserException("No Invoices Found");
    } else if (pendingInvoices != null) {

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
      }
      if (someChecked) {
        session.setAttribute("CheckedInvoices", checkedInvoices);
        PrintUtils.createFinanceNotice(custAcctForm.getCustomerId(), checkedInvoices);
      } else if (!someChecked && oops != null) {
        PrintUtils.createFinanceNotice(custAcctForm.getCustomerId(), checkedInvoices);
      } else {
        throw new UserException("No Invoices Selected.....");
      }
    }
  }

}
