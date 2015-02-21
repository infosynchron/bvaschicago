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

import com.bvas.beans.BouncedChecksBean;
import com.bvas.beans.CustomerBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.EnterAmountsForm;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class EnterAmountsAction extends Action {
  private static final Logger logger = Logger.getLogger(EnterAmountsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    EnterAmountsForm eForm = (EnterAmountsForm) form;
    String buttonClicked = eForm.getButtonClicked();
    ((EnterAmountsForm) eForm).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MakeModelMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    // logger.error(new java.util.Date(System.currentTimeMillis()) +
    // "-----EnterAmounts-----" + user.getUsername());

    if (eForm.getInvoiceNumber() == null) {
      eForm.setInvoiceNumber("");
    }

    int invoiceNo = 0;
    try {
      invoiceNo = Integer.parseInt(eForm.getInvoiceNumber());
    } catch (Exception e) {
      logger.error(e);
      invoiceNo = 0;
    }

    int bCheckId = 0;
    if (eForm.getInvoiceNumber().startsWith("bc") || eForm.getInvoiceNumber().startsWith("BC")) {
      try {
        bCheckId = Integer.parseInt(eForm.getInvoiceNumber().substring(2));
      } catch (Exception e) {
        logger.error(e);
      }
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetInvoice")) {
      session.removeAttribute("EnterAmountsPayments");
      try {
        if (eForm.getInvoiceNumber().startsWith("bc") || eForm.getInvoiceNumber().startsWith("BC")) {
          if (bCheckId == 0) {
            throw new Exception("This Bounced Check No is not Valid .....");
          }
          if (!BouncedChecksBean.isAvailable(bCheckId)) {
            throw new Exception("This Bounced Check Not in the system .....");
          }
          BouncedChecksBean bCheck = BouncedChecksBean.getBouncedCheck(bCheckId, "");
          if (bCheck == null) {
            throw new Exception("Please Check Your Bounced Check ID .....");
          } else {
            session.removeAttribute("BouncedCheckInEnterAmounts");
            session.removeAttribute("InvoiceInEnterAmounts");
            eForm.setInvoiceNumber("BC" + bCheck.getCheckId());
            CustomerBean custBean = CustomerBean.getCustomer(bCheck.getCustomerId());
            eForm.setCompanyName(custBean.getCompanyName());
            eForm.setCreditOrBalance(custBean.getCreditBalance() + "");
            eForm.setAppliedAmount(bCheck.getPaidAmount() + "");
            eForm.setBalance(bCheck.getBalance() + "");
            if (bCheck.getBalance() == 0) {
              eForm.setPayingAmount("");
            } else {
              eForm.setPayingAmount(bCheck.getBalance() + "");
            }
            eForm.setCheckNo("");
            session.setAttribute("BouncedCheckInEnterAmounts", bCheck);
            session.setAttribute("InvoiceAvail", "Yes");

            String[][] payments = bCheck.getPayments(bCheck.getCheckId());
            if (payments != null && payments.length != 0) {
              session.setAttribute("EnterAmountsPayments", payments);
            }

          }
        } else if (invoiceNo == 0) {
          eForm.reset();
          throw new Exception("You Must Enter The Invoice Number");
        } else if (InvoiceBean.isAvailable(invoiceNo)) {
          session.removeAttribute("BouncedCheckInEnterAmounts");
          session.removeAttribute("InvoiceInEnterAmounts");
          InvoiceBean invoice = InvoiceBean.getInvoice(invoiceNo);
          eForm.setInvoiceNumber(invoiceNo + "");

          eForm.setCompanyName(CustomerBean.getCompanyName(invoice.getCustomerId()));
          eForm.setCreditOrBalance(CustomerBean.getCreditBalance(invoice.getCustomerId()) + "");
          eForm.setAppliedAmount(invoice.getAppliedAmount() + "");
          eForm.setBalance(invoice.getBalance() + "");
          if (invoice.getBalance() == 0) {
            eForm.setPayingAmount("");
          } else {
            eForm.setPayingAmount(invoice.getBalance() + "");
          }
          eForm.setCheckNo("");
          session.setAttribute("InvoiceInEnterAmounts", invoice);
          session.setAttribute("InvoiceAvail", "Yes");

          String[][] payments = invoice.getPayments(invoice.getInvoiceNumber());
          if (payments != null && payments.length != 0) {
            session.setAttribute("EnterAmountsPayments", payments);
          }
        } else {
          eForm.reset();
          throw new Exception("*** This Invoice is Not Available ***");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.equals("AddNew")) {
      try {
        if (user.getRole().trim().equalsIgnoreCase("High")
            || user.getRole().trim().equalsIgnoreCase("Acct")) {
          addPayment(eForm, request, user.getUsername());
        } else {
          errorBean
              .setError("You cann't update the add payment field. <BR/>Contact Accounting person instead");
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      session.removeAttribute("EnterAmountsReportCreated");
      session.removeAttribute("EnterAmountsReportCreated1");
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.indexOf("Delete") != -1) {
      try {
        int noDeleted = 0;
        try {
          noDeleted = Integer.parseInt(buttonClicked.substring(6));
        } catch (Exception e) {
          logger.error(e);
        }
        if (noDeleted > 10) {
          throw new UserException("Payment Not Deleted");
        }
        if (user.getRole().trim().equalsIgnoreCase("High")
            || user.getRole().trim().equalsIgnoreCase("Acct")) {
          deletePayment(eForm, request, noDeleted);
        } else {
          errorBean
              .setError("You cann't delete the payment field. <BR/>Contact Accounting person instead");
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      session.removeAttribute("EnterAmountsReportCreated");
      session.removeAttribute("EnterAmountsReportCreated1");
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.equals("Clear")) {
      session.removeAttribute("EnterAmountsPayments");
      session.removeAttribute("EnterAmountsReportCreated1");
      eForm.reset();
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.equals("CreateStatement")) {
      PrintUtils.createFinanceStatement();
      session.setAttribute("EnterAmountsReportCreated", "Yes");
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.equals("AdjStatement")) {
      PrintUtils.createFinanceStatement1();
      session.setAttribute("EnterAmountsReportCreated1", "Yes");
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.equals("PrintThisReport")) {
      forwardPage = "EnterAmounts";
    } else if (buttonClicked.equals("Checks")) {
      try {
        Hashtable checks = ReportUtils2.getTodaysChecks();
        if (checks != null) {
          session.setAttribute("toShowReports", checks);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("System Error...  Try Later   !!!!!!!!");
          forwardPage = "EnterAmounts";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("System Error...  Try Later   !!!!!!!! " + e.getMessage());
        forwardPage = "EnterAmounts";
      }
    } else if (buttonClicked.equals("BackToAcct")) {
      session.removeAttribute("EnterAmountsPayments");
      session.removeAttribute("EnterAmountsReportCreated");
      session.removeAttribute("EnterAmountsReportCreated1");
      eForm.reset();
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("EnterAmountsPayments");
      session.removeAttribute("EnterAmountsReportCreated");
      session.removeAttribute("EnterAmountsReportCreated1");
      eForm.reset();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("EnterAmountsError", errorBean);
    } else {
      session.removeAttribute("EnterAmountsError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void addPayment(EnterAmountsForm eForm, HttpServletRequest request, String userName)
      throws UserException {
    HttpSession session = request.getSession(false);
    Object oops = null;
    if (eForm.getInvoiceNumber() == null) {
      eForm.setInvoiceNumber("");
    }
    if (eForm.getInvoiceNumber().startsWith("bc") || eForm.getInvoiceNumber().startsWith("BC")) {
      oops = session.getAttribute("BouncedCheckInEnterAmounts");
    } else {
      oops = session.getAttribute("InvoiceInEnterAmounts");
    }
    if (oops != null) {
      session.removeAttribute("EnterAmountsPayments");
      if (eForm.getInvoiceNumber().startsWith("bc") || eForm.getInvoiceNumber().startsWith("BC")) {
        BouncedChecksBean bCheck = (BouncedChecksBean) oops;
        double amount = 0;
        String date = "";
        try {
          amount = Double.parseDouble(eForm.getPayingAmount());
        } catch (Exception e) {
          logger.error(e);
          throw new UserException("Amount Entered is Not Valid");
        }
        date = DateUtils.getNewUSDate();
        String checkNo = eForm.getCheckNo();
        if (checkNo == null) {
          checkNo = "";
        } else {
          checkNo = checkNo.trim();
        }
        bCheck.addPayment(amount, date, checkNo, userName);
        eForm.setAppliedAmount(bCheck.getPaidAmount() + "");
        eForm.setBalance(bCheck.getBalance() + "");
        eForm.setCreditOrBalance(CustomerBean.getCustomer(bCheck.getCustomerId())
            .getCreditBalance() + "");
        if (bCheck.getBalance() == 0) {
          eForm.setPayingAmount("");
        } else {
          eForm.setPayingAmount(bCheck.getBalance() + "");
        }

        String[][] payments = bCheck.getPayments(bCheck.getCheckId());
        if (payments != null && payments.length != 0) {
          session.setAttribute("EnterAmountsPayments", payments);
        }

        // PrintUtils.createFinanceStatement();
      } else {
        InvoiceBean invoice = (InvoiceBean) oops;
        double amount = 0;
        String date = "";
        try {
          amount = Double.parseDouble(eForm.getPayingAmount());
        } catch (Exception e) {
          logger.error(e);
          throw new UserException("Amount Entered is Not Valid");
        }
        date = DateUtils.getNewUSDate();
        String checkNo = eForm.getCheckNo();
        if (checkNo == null) {
          checkNo = "";
        } else {
          checkNo = checkNo.trim();
        }
        invoice.addPayment(invoice.getInvoiceNumber(), amount, date, checkNo, userName);
        invoice.setBalance(invoice.getBalance() - amount);
        eForm.setAppliedAmount(invoice.getAppliedAmount() + "");
        eForm.setBalance(invoice.getBalance() + "");
        eForm.setCreditOrBalance(CustomerBean.getCreditBalance(invoice.getCustomerId()) + "");
        if (invoice.getBalance() == 0) {
          eForm.setPayingAmount("");
        } else {
          eForm.setPayingAmount(invoice.getBalance() + "");
        }

        String[][] payments = invoice.getPayments(invoice.getInvoiceNumber());
        if (payments != null && payments.length != 0) {
          session.setAttribute("EnterAmountsPayments", payments);
        }

        // PrintUtils.createFinanceStatement();
      }
    } else {
      logger.error("In Enter Amounts - No InvoiceBean found in session");
    }
  }

  public void deletePayment(EnterAmountsForm eForm, HttpServletRequest request, int noDeleted)
      throws UserException {
    HttpSession session = request.getSession(false);
    Object oops = null;
    if (eForm.getInvoiceNumber() == null) {
      eForm.setInvoiceNumber("");
    }
    if (eForm.getInvoiceNumber().startsWith("bc") || eForm.getInvoiceNumber().startsWith("BC")) {
      oops = session.getAttribute("BouncedCheckInEnterAmounts");
    } else {
      oops = session.getAttribute("InvoiceInEnterAmounts");
    }
    if (oops != null) {
      if (eForm.getInvoiceNumber().startsWith("bc") || eForm.getInvoiceNumber().startsWith("BC")) {
        BouncedChecksBean bCheck = (BouncedChecksBean) oops;
        String[][] payments = null;
        Object oooo = session.getAttribute("EnterAmountsPayments");
        if (oooo != null) {
          payments = (String[][]) oooo;
        } else {
          throw new UserException("Payment Not Found For Delete");
        }
        double amount = 0;
        String date = "";
        try {
          amount = Double.parseDouble(payments[noDeleted][2]);
        } catch (Exception e) {
          logger.error(e);
          throw new UserException("Amount Entered is Not Valid");
        }
        date = payments[noDeleted][1];
        String checkNo = payments[noDeleted][3];
        if (checkNo == null) {
          checkNo = "";
        } else {
          checkNo = checkNo.trim();
        }
        bCheck.deletePayment(amount, date, checkNo);
        eForm.setAppliedAmount(bCheck.getPaidAmount() + "");
        eForm.setBalance(bCheck.getBalance() + "");
        eForm.setCreditOrBalance(CustomerBean.getCustomer(bCheck.getCustomerId())
            .getCreditBalance() + "");
        if (bCheck.getBalance() == 0) {
          eForm.setPayingAmount("");
        } else {
          eForm.setPayingAmount(bCheck.getBalance() + "");
        }

        session.removeAttribute("EnterAmountsPayments");
        payments = null;
        payments = bCheck.getPayments(bCheck.getCheckId());
        if (payments != null && payments.length != 0) {
          session.setAttribute("EnterAmountsPayments", payments);
        }

        // PrintUtils.createFinanceStatement();
      } else {
        InvoiceBean invoice = (InvoiceBean) oops;
        String[][] payments = null;
        Object oooo = session.getAttribute("EnterAmountsPayments");
        if (oooo != null) {
          payments = (String[][]) oooo;
        } else {
          throw new UserException("Payment Not Found For Delete");
        }
        double amount = 0;
        String date = "";
        try {
          amount = Double.parseDouble(payments[noDeleted][2]);
        } catch (Exception e) {
          logger.error(e);
          throw new UserException("Amount Entered is Not Valid");
        }
        date = payments[noDeleted][1];
        String checkNo = payments[noDeleted][3];
        if (checkNo == null) {
          checkNo = "";
        } else {
          checkNo = checkNo.trim();
        }
        invoice.deletePayment(invoice.getInvoiceNumber(), amount, date, checkNo);
        invoice.setBalance(invoice.getBalance() + amount);
        eForm.setAppliedAmount(invoice.getAppliedAmount() + "");
        eForm.setBalance(invoice.getBalance() + "");
        eForm.setCreditOrBalance(CustomerBean.getCustomer(invoice.getCustomerId())
            .getCreditBalance() + "");
        if (invoice.getBalance() == 0) {
          eForm.setPayingAmount("");
        } else {
          eForm.setPayingAmount(invoice.getBalance() + "");
        }

        session.removeAttribute("EnterAmountsPayments");
        payments = null;
        payments = invoice.getPayments(invoice.getInvoiceNumber());
        if (payments != null && payments.length != 0) {
          session.setAttribute("EnterAmountsPayments", payments);
        }

        // PrintUtils.createFinanceStatement();
      }
    } else {
      logger.error("In Enter Amounts - No InvoiceBean found in session");
    }
  }

}
