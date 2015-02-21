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
import com.bvas.formBeans.BouncedChecksForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class BouncedChecksAction extends Action {
  private static final Logger logger = Logger.getLogger(BouncedChecksAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    BouncedChecksForm bForm = (BouncedChecksForm) form;
    String buttonClicked = bForm.getButtonClicked();
    ((BouncedChecksForm) bForm).setButtonClicked("");
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
    } else if (buttonClicked.equals("GetTheCheck")) {
      try {

        if ((bForm.getCheckId() == null || bForm.getCheckId().trim().equals(""))
            && (bForm.getCustomerId() == null || bForm.getCustomerId().trim().equals(""))) {
          bForm.reset();
          throw new Exception("You must enter the Check Id OR Customer Id");
        } else {
          int chkId = 0;
          if (bForm.getCheckId() != null && !bForm.getCheckId().trim().equals("")) {
            try {
              chkId = Integer.parseInt(bForm.getCheckId());
            } catch (Exception e) {
              // logger.error(e);
              throw new Exception("Enter Only DIGITS in the Check Id .......");
            }
          }
          CustomerBean customer = null;
          if (bForm.getCustomerId() != null && !bForm.getCustomerId().trim().equals("")) {
            customer = CustomerBean.getCustomer(bForm.getCustomerId());
            if (customer == null) {
              throw new Exception("This Customer Id is Wrong .....");
            }
          }
          BouncedChecksBean bCheck =
              BouncedChecksBean.getBouncedCheck(chkId, bForm.getCustomerId());
          if (bCheck == null) {
            throw new Exception("No Bounced Check with this Id .....");
          } else {
            if (customer == null) {
              customer = CustomerBean.getCustomer(bCheck.getCustomerId());
            }
            bForm.setCheckId(bCheck.getCheckId() + "");
            bForm.setCustomerId(bCheck.getCustomerId());
            bForm.setCompanyName(customer.getCompanyName());
            bForm.setTotalBalance(customer.getCreditBalance() + "");
            bForm.setEnteredDate(bCheck.getEnteredDate());
            bForm.setCheckDate(bCheck.getCheckDate());
            bForm.setCheckNo(bCheck.getCheckNo());
            bForm.setBouncedAmount(bCheck.getBouncedAmount() + "");
            bForm.setPaidAmount(bCheck.getPaidAmount() + "");
            bForm.setBalance(bCheck.getBalance() + "");
            if (bCheck.getIsCleared().trim().equals("Y")) {
              bForm.setIsCleared(true);
            } else {
              bForm.setIsCleared(false);
            }
            session.setAttribute("BouncedCheckID", bForm.getCheckId());
          }
        }

      } catch (Exception e) {
        errorBean.setError(e.getMessage());
      } finally {
        /*
         * ff.delete();
         */
        /*
         * java.io.File ff = new java.io.File(
         * "c:/Tomcat/webapps/bvaschicago/html/reports/SFN.html"); ff.setWritable(true);
         * ff.delete(); org.apache.commons.io.FileUtils.deleteQuietly(ff);
         */

      }
      forwardPage = "BouncedChecks";
    } else if (buttonClicked.equals("AddNew")) {
      try {
        BouncedChecksBean bCheck = fillBouncedCheck(bForm);
        bCheck.addBouncedCheck();
        bForm.setCheckId(bCheck.getCheckId() + "");
        CustomerBean cust = CustomerBean.getCustomer(bForm.getCustomerId());
        bForm.setCompanyName(cust.getCompanyName());
        bForm.setTotalBalance(cust.getCreditBalance() + "");
        bForm.setPaidAmount(bCheck.getPaidAmount() + "");
        bForm.setBalance(bCheck.getBalance() + "");
        errorBean.setError("THIS CHECK ADDED SUCCESSFULLY --- New Check Id is : BC"
            + bCheck.getCheckId());
        // PrintUtils.createFinanceNotice(bForm.getCustomerId(), new
        // Hashtable());
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "BouncedChecks";
    } else if (buttonClicked.equals("Clear")) {
      /*
       * java.io.File ff = new java.io.File( "c:/Tomcat/webapps/bvaschicago/html/reports/SFN.html");
       * ff.setWritable(true); ff.delete(); org.apache.commons.io.FileUtils.deleteQuietly(ff);
       */
      bForm.reset();
      forwardPage = "BouncedChecks";
    } else if (buttonClicked.equals("Change")) {
      try {
        BouncedChecksBean bCheck = fillBouncedCheck(bForm);
        int chk = 0;
        try {
          String str = (String) session.getAttribute("BouncedCheckID");
          if (str != null && !str.trim().equals("")) {
            // logger.error("1" + str);
            chk = Integer.parseInt(str);
          }
        } catch (Exception e) {
          // logger.error(e);
          throw new Exception("CAN NOT CHANGE .. This Check No is Wrong ......");
        }
        // logger.error("2" + bCheck.getCheckId());
        if (bCheck.getCheckId() == 0 || bCheck.getCheckId() != chk) {
          throw new Exception("CAN NOT CHANGE .. This Check No is Wrong ......");
        }
        bCheck.changeBouncedCheck();
        bForm.setTotalBalance(CustomerBean.getCustomer(bForm.getCustomerId()).getCreditBalance()
            + "");
        errorBean.setError("THIS CHECK CHANGED SUCCESSFULLY");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "BouncedChecks";
    } else if (buttonClicked.equals("CreateReport")) {
      Hashtable<String, String> ht = new Hashtable<String, String>();
      String cust = bForm.getCustomerId();
      PrintUtils.createFinanceNotice(cust, ht);
      forwardPage = "BouncedChecks";
    } else if (buttonClicked.equals("PrintThisReport")) {
      forwardPage = "BouncedChecks";
    } else if (buttonClicked.equals("PendingChecks")) {
      try {
        Hashtable toShowSales = ReportUtils2.showPendingBouncedChecks();
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No Checks Are Pending .....");
          forwardPage = "BouncedChecks";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "BouncedChecks";
      }
    } else if (buttonClicked.equals("BackToAcct")) {
      bForm.reset();
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      bForm.reset();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("BouncedChecksError", errorBean);
    } else {
      session.removeAttribute("BouncedChecksError");
    }
    return (mapping.findForward(forwardPage));

  }

  public BouncedChecksBean fillBouncedCheck(BouncedChecksForm bForm) throws UserException {
    BouncedChecksBean bouncedCheck = new BouncedChecksBean();

    if (bForm.getCustomerId() == null || bForm.getCustomerId().trim().equals("")) {
      throw new UserException("You Must Enter Customer Id");
    }
    if (bForm.getCheckNo() == null || bForm.getCheckNo().trim().equals("")) {
      throw new UserException("You Must Enter Check No");
    }
    if (bForm.getCheckDate() == null || bForm.getCheckDate().trim().equals("")) {
      throw new UserException("You Must Enter Check Date");
    }

    if (bForm.getCheckId() != null && !bForm.getCheckId().trim().equals("")) {
      int chkId = 0;
      try {
        chkId = Integer.parseInt(bForm.getCheckId());
        bouncedCheck.setCheckId(chkId);
      } catch (Exception e) {
        // logger.error(e);
        throw new UserException("This Check Id is not Valid .....");
      }
      if (chkId == 0 || !BouncedChecksBean.isAvailable(chkId)) {
        bouncedCheck.setCheckId(BouncedChecksBean.getMaxCheckId());
      }

    } else {
      bouncedCheck.setCheckId(BouncedChecksBean.getMaxCheckId());
    }
    // logger.error("3" + bForm.getCheckId());
    // logger.error("4" + bouncedCheck.getCheckId());
    bouncedCheck.setCustomerId(bForm.getCustomerId());
    bouncedCheck.setEnteredDate(bForm.getEnteredDate());
    bouncedCheck.setCheckNo(bForm.getCheckNo());
    bouncedCheck.setCheckDate(bForm.getCheckDate());
    try {
      bouncedCheck.setBouncedAmount(Double.parseDouble(bForm.getBouncedAmount()));
    } catch (Exception e) {
      // logger.error(e);
      throw new UserException("Please Enter a Valid Amount");
    }
    if (bouncedCheck.getBouncedAmount() == 0) {
      throw new UserException("Please Enter a Valid Amount");
    }
    try {
      bouncedCheck.setPaidAmount(Double.parseDouble(bForm.getPaidAmount()));
    } catch (Exception e) {
      // logger.error(e);
      bouncedCheck.setPaidAmount(0);
    }

    if (bouncedCheck.getPaidAmount() != 0) {
      bouncedCheck
          .setBalance(bouncedCheck.getBouncedAmount() + 25.0 - bouncedCheck.getPaidAmount());
    } else {
      bouncedCheck.setBalance(bouncedCheck.getBouncedAmount() + 25.0);
    }

    // logger.error("Amount:"+bouncedCheck.getBouncedAmount());
    // logger.error("Balance:"+bouncedCheck.getBalance());
    if (bForm.getIsCleared() == true) {
      bouncedCheck.setIsCleared("Y");
    } else {
      bouncedCheck.setIsCleared("N");
    }

    return bouncedCheck;
  }

}
