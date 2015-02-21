package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

import com.bvas.beans.CustomerBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.formBeans.WriteOffForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class WriteOffAction extends Action {
  private static final Logger logger = Logger.getLogger(WriteOffAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    WriteOffForm eForm = (WriteOffForm) form;
    String buttonClicked = eForm.getButtonClicked();
    ((WriteOffForm) eForm).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MakeModelMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

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

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetInvoice")) {
      session.removeAttribute("EnterAmountsPayments");
      try {
        if (invoiceNo == 0) {
          eForm.reset();
          throw new Exception("Please Enter The Invoice Number");
        } else if (InvoiceBean.isAvailable(invoiceNo)) {
          InvoiceBean invoice = InvoiceBean.getInvoice(invoiceNo);
          eForm.setInvoiceNumber(invoiceNo + "");
          eForm.setCompanyName(CustomerBean.getCustomer(invoice.getCustomerId()).getCompanyName());
          eForm.setBalance(invoice.getBalance() + "");
          Connection con = DBInterfaceLocal.getSQLConnection();
          Statement stmt = con.createStatement();
          ResultSet rs =
              stmt.executeQuery("Select WriteOffDate, Notes From WriteOff Where InvoiceNo ="
                  + invoiceNo);
          if (rs.next()) {
            eForm.setWriteOffDate(DateUtils.convertMySQLToUSFormat(rs.getString("WriteOffDate")));
            eForm.setNotes(rs.getString("Notes"));
          } else {
            eForm.setWriteOffDate("");
            eForm.setNotes("");
          }
          rs.close();
          stmt.close();
          con.close();
        } else {
          eForm.reset();
          throw new Exception("*** This Invoice is Not Available ***");
        }

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "WriteOff";
    } else if (buttonClicked.equals("WriteOff")) {
      try {
        addWriteOff(eForm);
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "WriteOff";
    } else if (buttonClicked.equals("Delete")) {
      try {
        deleteWriteOff(eForm);
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "WriteOff";
    } else if (buttonClicked.equals("Clear")) {
      eForm.reset();
      forwardPage = "WriteOff";
    } else if (buttonClicked.equals("BackToAcct")) {
      eForm.reset();
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      eForm.reset();
      forwardPage = "MainMenu";
    } else if (buttonClicked.equals("ShowReport")) {
      try {
        String writeOffFromDate = "";
        String writeOffToDate = "";
        writeOffFromDate = eForm.getReportFromDate();
        writeOffToDate = eForm.getReportToDate();

        if (!writeOffToDate.trim().equals("")) {
          Hashtable toShowInvoices =
              ReportUtils2.getWriteOffInvoices(writeOffFromDate, writeOffToDate);
          if (toShowInvoices != null) {
            session.setAttribute("toShowReports", toShowInvoices);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Dates .....");
            forwardPage = "WriteOff";
          }
        } else {
          errorBean.setError("Please Enter the date for the Invoices to Show .....");
          forwardPage = "WriteOff";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "WriteOff";
      }
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("WriteOffError", errorBean);
    } else {
      session.removeAttribute("WriteOffError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void addWriteOff(WriteOffForm eForm) throws UserException {
    try {
      int invNo = 0;
      String compName = "";
      String wfDate = "";
      String notes = "";
      try {
        invNo = Integer.parseInt(eForm.getInvoiceNumber());
      } catch (Exception e) {
        logger.error(e);
        invNo = 0;
      }
      if (invNo == 0 || !InvoiceBean.isAvailable(invNo)) {
        throw new UserException("***** This Invoice # is Not Valid *****");
      }
      if (InvoiceBean.getInvoice(invNo).getBalance() == 0) {
        throw new UserException("***** ALREADY PAID INVOICE *****");
      }
      compName = eForm.getCompanyName();
      wfDate = eForm.getWriteOffDate();
      if (wfDate.trim().equals("")) {
        wfDate = DateUtils.getNewUSDate();
      }
      notes = eForm.getNotes();
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 = stmt1.executeQuery("Select * from WriteOff Where InvoiceNo=" + invNo);
      if (rs1.next()) {
        throw new UserException("***** ALREADY WRITTEN OFF *****");
      }
      Statement stmt2 = con.createStatement();
      try {
        stmt2.execute("Insert Into WriteOff (InvoiceNo, WriteOffDate, Notes) Values (" + invNo
            + ", '" + DateUtils.convertUSToMySQLFormat(wfDate) + "', '" + notes + "')");
      } catch (Exception e) {
        logger.error(e);
        throw new UserException("Error Writing Off : " + e.getMessage());
      }

      Statement stmt3 = con.createStatement();
      try {
        stmt3.execute("Update Invoice Set Status='W' Where InvoiceNumber=" + invNo);
      } catch (Exception e) {
        logger.error(e);
        throw new UserException("Error When Writing Off In Invoice : " + e.getMessage());
      }

      rs1.close();
      stmt1.close();
      stmt2.close();
      stmt3.close();
      con.close();

      throw new UserException("SUCCESSFUL .....");
    } catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
  }

  public void deleteWriteOff(WriteOffForm eForm) throws UserException {
    try {
      int invNo = 0;
      String compName = "";
      String wfDate = "";
      String notes = "";
      try {
        invNo = Integer.parseInt(eForm.getInvoiceNumber());
      } catch (Exception e) {
        logger.error(e);
        invNo = 0;
      }
      if (invNo == 0 || !InvoiceBean.isAvailable(invNo)) {
        throw new UserException("***** This Invoice # is Not Valid *****");
      }
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 = stmt1.executeQuery("Select * from WriteOff Where InvoiceNo=" + invNo);
      if (!rs1.next()) {
        throw new UserException("***** NOT WRITTEN OFF *****");
      }

      Statement stmt2 = con.createStatement();
      Statement stmt3 = con.createStatement();
      try {
        stmt2.execute("Delete from WriteOff Where InvoiceNo=" + invNo);
        stmt3.execute("Update Invoice Set Status='M' Where InvoiceNumber=" + invNo);
      } catch (Exception e) {
        logger.error(e);
        throw new UserException("Cannot delete: " + e.getMessage());
      }
      rs1.close();
      stmt1.close();
      stmt2.close();
      stmt3.close();
      con.close();
      throw new UserException("DELETE SUCCESSFUL .....");
    } catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
  }

}
