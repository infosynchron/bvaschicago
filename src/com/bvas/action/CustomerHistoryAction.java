package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

import com.bvas.beans.CustomerBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.CustomerHistoryForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;

public class CustomerHistoryAction extends Action {
  private static final Logger logger = Logger.getLogger(CustomerHistoryAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((CustomerHistoryForm) form).getButtonClicked();
    ((CustomerHistoryForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in ManufacMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    logger.error(new java.util.Date(System.currentTimeMillis()) + "-----CustomerHistory-----"
        + user.getUsername());

    if (buttonClicked == null || buttonClicked.equals("")) {
      buttonClicked = "GetPart";
    }

    if (buttonClicked.equals("GetCust")) {
      try {
        String customerId = ((CustomerHistoryForm) form).getCustomerId();
        String fromDate = ((CustomerHistoryForm) form).getFromDate();
        String toDate = ((CustomerHistoryForm) form).getToDate();
        if (fromDate == null || fromDate.trim().equals("")) {
          fromDate = toDate;
        }
        if (customerId != null && !customerId.trim().equals("")) {
          CustomerBean cust = CustomerBean.getCustomer(customerId);
          if (cust == null) {
            ((CustomerHistoryForm) form).setCustomerId("");
            errorBean.setError("THIS CUSTOMER NOT FOUND IN THE SYSTEM");
          } else {
            session.removeAttribute("CustHistoryDetails1");
            session.removeAttribute("CustHistoryDetails2");
            session.removeAttribute("CustHistoryDetails3");

            fillCustDetails(cust, session, fromDate, toDate);
          }
        } else {
          errorBean.setError("ENTER CUSTOMER ID TO GET HISTORY");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("CUSTOMER ID MAY BE WRONG");
      }
      forwardPage = "CustomerHistory";
    } else if (buttonClicked.equals("Clear")) {
      session.removeAttribute("CustHistoryDetails1");
      session.removeAttribute("CustHistoryDetails2");
      session.removeAttribute("CustHistoryDetails3");
      ((CustomerHistoryForm) form).setCustomerId("");
      ((CustomerHistoryForm) form).setFromDate("");
      ((CustomerHistoryForm) form).setToDate("");
      forwardPage = "CustomerHistory";
    } else if (buttonClicked.equals("Back")) {
      session.removeAttribute("CustHistoryDetails1");
      session.removeAttribute("CustHistoryDetails2");
      session.removeAttribute("CustHistoryDetails3");
      ((CustomerHistoryForm) form).setCustomerId("");
      ((CustomerHistoryForm) form).setFromDate("");
      ((CustomerHistoryForm) form).setToDate("");
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("CustHistoryDetails1");
      session.removeAttribute("CustHistoryDetails2");
      session.removeAttribute("CustHistoryDetails3");
      ((CustomerHistoryForm) form).setCustomerId("");
      ((CustomerHistoryForm) form).setFromDate("");
      ((CustomerHistoryForm) form).setToDate("");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("CustHistoryError", errorBean);
    } else {
      session.removeAttribute("CustHistoryError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillCustDetails(CustomerBean cust, HttpSession session, String fromDate, String toDate) {
    Vector<String[]> custDetails1 = new Vector<String[]>();
    Vector<String[]> custDetails2 = new Vector<String[]>();
    Vector<String[]> custDetails3 = new Vector<String[]>();

    int terms = 0;
    if (cust.getPaymentTerms().trim().equalsIgnoreCase("O")) {
      terms = 2;
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("C")) {
      terms = 2;
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("W")) {
      terms = 9;
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("B")) {
      terms = 16;
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("M")) {
      terms = 33;
    }

    custDetails1 = addCustDetails(cust, custDetails1, fromDate, toDate, terms);
    custDetails2 = addBCDetails(cust.getCustomerId(), custDetails2);
    custDetails3 = addInvoiceDetails(cust.getCustomerId(), custDetails3, fromDate, toDate, terms);

    if (custDetails1 != null && custDetails1.size() > 0) {
      session.setAttribute("CustHistoryDetails1", custDetails1);
    }
    if (custDetails2 != null && custDetails2.size() > 0) {
      session.setAttribute("CustHistoryDetails2", custDetails2);
    }
    if (custDetails3 != null && custDetails3.size() > 0) {
      session.setAttribute("CustHistoryDetails3", custDetails3);
    }

  }

  public Vector<String[]> addCustDetails(CustomerBean cust, Vector<String[]> custDetails,
      String fromDate, String toDate, int terms) {

    String[] retStr = new String[6];

    retStr[0] = cust.getCustomerId();
    retStr[1] = cust.getCompanyName();
    if (cust.getPaymentTerms().trim().equalsIgnoreCase("O")) {
      retStr[2] = "CASH ONLY";
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("C")) {
      retStr[2] = "C.O.D.";
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("W")) {
      retStr[2] = "WEEKLY";
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("B")) {
      retStr[2] = "BI-WEEKLY";
    } else if (cust.getPaymentTerms().trim().equalsIgnoreCase("M")) {
      retStr[2] = "MONTHLY";
    } else {
      retStr[2] = "&nbsp;";
    }
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select sum(invoicetotal) From Invoice Where Customerid='"
              + cust.getCustomerId() + "' And Orderdate>='"
              + DateUtils.convertUSToMySQLFormat(fromDate) + "' and OrderDate<='"
              + DateUtils.convertUSToMySQLFormat(toDate) + "' ");
      if (rs.next()) {
        String str = rs.getString(1);
        if (str == null || str.trim().equals("")) {
          retStr[3] = "0.0";
        } else {
          retStr[3] = str;
        }
      }
      ResultSet rs1 =
          stmt.executeQuery("Select sum(Balance) From Invoice Where Customerid='"
              + cust.getCustomerId() + "' and Status!='C' and Status!='W' and Balance>0 ");
      if (rs1.next()) {
        String str = rs1.getString(1);
        if (str == null || str.trim().equals("")) {
          retStr[4] = "0.0";
        } else {
          retStr[4] = str;
        }
      }
      ResultSet rs2 =
          stmt.executeQuery("Select sum(Balance) From Invoice Where Customerid='"
              + cust.getCustomerId() + "' and Status='W' and Balance>0 ");
      if (rs2.next()) {
        String str = rs2.getString(1);
        if (str == null || str.trim().equals("")) {
          retStr[5] = "0.0";
        } else {
          retStr[5] = str;
        }
      }
      rs.close();
      rs1.close();
      rs2.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    custDetails.addElement(retStr);
    return custDetails;
  }

  public Vector<String[]> addBCDetails(String customerId, Vector<String[]> custDetails) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select CheckId, EnteredDate, CheckNo, CheckDate, BouncedAmount, Balance from BouncedChecks Where CustomerId='"
              + customerId + "' order by 2 ");
      while (rs.next()) {

        String[] retStr = new String[6];

        String checkId = rs.getString("CheckId");
        String enteredDate = DateUtils.convertMySQLToUSFormat(rs.getString("EnteredDate"));
        String checkNo = rs.getString("CheckNo");
        String checkDate = DateUtils.convertMySQLToUSFormat(rs.getString("CheckDate"));
        String bouncedAmt = rs.getString("BouncedAmount");
        String bal = rs.getString("Balance");

        retStr[0] = checkId;
        retStr[1] = enteredDate;
        retStr[2] = checkNo + "/" + checkDate;
        retStr[3] = bouncedAmt;
        retStr[4] = bal;

        retStr[5] = "**NOT PAID**";

        Statement stmt1 = con.createStatement();
        ResultSet rs1 =
            stmt1
                .executeQuery("Select AppliedDate, AppliedAmount from AppliedAmounts Where InvoiceNumber='BC"
                    + checkId + "'");
        if (rs1.next()) {
          retStr[5] =
              DateUtils.convertMySQLToUSFormat(rs1.getString("AppliedDate")) + "/"
                  + rs1.getString("AppliedAmount");
          while (rs1.next()) {
            retStr[5] +=
                "---" + DateUtils.convertMySQLToUSFormat(rs1.getString("AppliedDate")) + "/"
                    + rs1.getString("AppliedAmount");
          }
        }
        custDetails.addElement(retStr);
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return custDetails;

  }

  public Vector<String[]> addInvoiceDetails(String customerId, Vector<String[]> custDetails,
      String fromDate, String toDate, int terms) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      Statement stmt1 = con.createStatement();

      logger.error(fromDate);
      logger.error(toDate);
      ResultSet rs =
          stmt.executeQuery("Select InvoiceNumber, InvoiceTotal+Tax-Discount, Balance, OrderDate, Status from Invoice Where CustomerId='"
              + customerId
              + "' and orderdate>='"
              + DateUtils.convertUSToMySQLFormat(fromDate)
              + "' and orderdate<='" + DateUtils.convertUSToMySQLFormat(toDate) + "' Order By 1");
      ResultSet rs1 =
          stmt1
              .executeQuery("Select a.InvoiceNumber, AppliedDate, b.AppliedAmount, PaymentType, AppliedDate-a.OrderDate from Invoice a, AppliedAmounts b Where CustomerId='"
                  + customerId
                  + "' and orderdate>='"
                  + DateUtils.convertUSToMySQLFormat(fromDate)
                  + "' and orderdate<='"
                  + DateUtils.convertUSToMySQLFormat(toDate)
                  + "' and a.InvoiceNumber=b.InvoiceNumber Order By 1 ");

      int pmtInv = 0;
      String pmtDate = "";
      double pmtAmt = 0.0;
      String pmtType = "";
      int paidDays = 0;

      if (rs1.next()) {
        pmtInv = rs1.getInt(1);
        pmtDate = DateUtils.convertMySQLToUSFormat(rs1.getString(2));
        pmtAmt = rs1.getDouble(3);
        pmtType = rs1.getString(4);
        paidDays = rs1.getInt(5);
      }

      while (rs.next()) {

        String[] retStr = new String[5];

        int invNo = rs.getInt(1);
        double invTotal = rs.getDouble(2);
        double bal = rs.getDouble(3);
        String orderDate = DateUtils.convertMySQLToUSFormat(rs.getString(4));
        String status = rs.getString(5);

        // logger.error(invNo);
        retStr[0] = invNo + "";
        retStr[1] = orderDate;
        retStr[2] = invTotal + "";
        retStr[3] = "";
        retStr[4] = "";

        if (status.trim().equalsIgnoreCase("w")) {
          retStr[3] += "*** WRITE OFF ***";
        }

        if (pmtInv == invNo) {

          if (bal != 0) {
            retStr[3] += "**PENDING AMOUNT - " + bal + "**";
          }

          if (paidDays - terms > 0) {
            retStr[3] += "PAID LATE BY " + (paidDays - terms) + " DAYS";
          }

          if (pmtType.trim().equals("")) {
            retStr[4] += "Cash Amt - " + pmtAmt + " On " + pmtDate;
          } else {
            try {
              int chkType = Integer.parseInt(pmtType);
              retStr[4] += "Check # " + pmtType + " -- Amt - " + pmtAmt + " On " + pmtDate;
            } catch (Exception e) {
              logger.error(e);
              retStr[4] += "Adjustment - " + pmtType + " -- Amt - " + pmtAmt + " On " + pmtDate;
            }
          }

          pmtInv = 0;
          pmtDate = "";
          pmtAmt = 0.0;
          pmtType = "";

          while (rs1.next()) {
            pmtInv = rs1.getInt(1);
            pmtDate = DateUtils.convertMySQLToUSFormat(rs1.getString(2));
            pmtAmt = rs1.getDouble(3);
            pmtType = rs1.getString(4);
            paidDays = rs1.getInt(5);

            if (pmtInv == invNo) {
              if (paidDays - terms > 0) {
                retStr[3] += "PAID LATE BY " + (paidDays - terms) + " DAYS";
              }

              if (pmtType.trim().equals("")) {
                retStr[4] += "Cash Amt - " + pmtAmt + " On " + pmtDate;
              } else {
                try {
                  int chkType = Integer.parseInt(pmtType);
                  retStr[4] += "Check # " + pmtType + " -- Amt - " + pmtAmt + " On " + pmtDate;
                } catch (Exception e) {
                  logger.error(e);
                  retStr[4] += "Adjustment - " + pmtType + " -- Amt - " + pmtAmt + " On " + pmtDate;
                }
              }

              pmtInv = 0;
              pmtDate = "";
              pmtAmt = 0.0;
              pmtType = "";
            } else {
              break;
            }

          }
        } else {
          if (bal == 0 && invTotal == 0) {
            retStr[3] = "**VOID**";
          } else {
            retStr[3] = "**NOT PAID**";
          }
        }

        if (retStr[3].trim().equals("")) {
          retStr[3] = "&nbsp;";
        }
        if (retStr[4].trim().equals("")) {
          retStr[4] = "&nbsp;";
        }
        custDetails.addElement(retStr);
      }
      rs.close();
      rs1.close();
      stmt.close();
      stmt1.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return custDetails;

  }

}
