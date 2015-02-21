package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
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
import com.bvas.beans.UserBean;
import com.bvas.formBeans.IsDeliveredForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.UserException;

public class IsDeliveredAction extends Action {
  private static final Logger logger = Logger.getLogger(IsDeliveredAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    IsDeliveredForm delForm = (IsDeliveredForm) form;
    String buttonClicked = delForm.getButtonClicked();
    ((IsDeliveredForm) delForm).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MakeModelMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }
    UserBean user = (UserBean) session.getAttribute("User");

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("ChangeIsDelivered")) {
      try {
        deliverInvoices(request, user.getUsername());
      } catch (Exception e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }
      forwardPage = "IsDelivered";
    } else if (buttonClicked.equals("Clear")) {
      delForm.reset();
      session.removeAttribute("ForRouteSheet");
      session.removeAttribute("AllUnDeliveredInvoices");
      session.removeAttribute("AllCheckedInvoicesForDel");
      forwardPage = "IsDelivered";
    } else if (buttonClicked.equals("BackToOrders")) {
      delForm.reset();
      session.removeAttribute("ForRouteSheet");
      session.removeAttribute("AllUnDeliveredInvoices");
      session.removeAttribute("AllCheckedInvoicesForDel");
      forwardPage = "TodaysOrders";
    } else if (buttonClicked.equals("PrintThisReport")) {
      forwardPage = "IsDelivered";
    } else if (buttonClicked.equals("ReturnToMain")) {
      delForm.reset();
      session.removeAttribute("ForRouteSheet");
      session.removeAttribute("AllUnDeliveredInvoices");
      session.removeAttribute("AllCheckedInvoicesForDel");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("IsDeliveredError", errorBean);
    } else {
      session.removeAttribute("IsDeliveredError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void deliverInvoices(HttpServletRequest request, String userName) throws UserException {
    HttpSession session = request.getSession(false);
    session.removeAttribute("ForRouteSheet");
    Vector pendingInvoices = (Vector) session.getAttribute("AllUnDeliveredInvoices");
    Connection con = null;
    Statement stmtX = null;
    Statement stmtY = null;
    ResultSet rsX = null;
    boolean someChecked = false;
    Hashtable<String, String> checkedInvoices = new Hashtable<String, String>();

    if (pendingInvoices == null) {
      throw new UserException("No Invoices Found");
    }
    String whoDelivered = (String) request.getParameter("whoDelivered");

    if (whoDelivered == null || whoDelivered.trim().equals("")) {
      throw new UserException("Select the Driver Name");
    } else if (whoDelivered.trim().equalsIgnoreCase("Select")) {
      throw new UserException("Select the Driver Name");
    }
    try {
      con = DBInterfaceLocal.getSQLConnection();

      for (int i = 1; i <= pendingInvoices.size(); i++) {
        String checkedBox = (String) request.getParameter("CB" + i);
        if (checkedBox == null) {
          checkedBox = "";
        } else if (!checkedBox.trim().equals("")) {
          someChecked = true;
          checkedInvoices.put(checkedBox, checkedBox);
        }
        int invNo = 0;
        if (!checkedBox.trim().equals("")) {
          invNo = Integer.parseInt(checkedBox);
        } else {
          invNo = 0;
        }
        if (invNo != 0) {

          InvoiceBean.changeIsDelivered(invNo, con);

          if (whoDelivered != null && !whoDelivered.trim().equals("")) {
            stmtX = con.createStatement();
            rsX =
                stmtX.executeQuery("Select Name From PartsDelivered Where InvoiceNumber='" + invNo
                    + "'");
            if (rsX.next()) {
              String name = rsX.getString("Name");
              if (!whoDelivered.trim().equals(name.trim())) {
                stmtY = con.createStatement();
                stmtY.execute("Update PartsDelivered Set Name='" + whoDelivered
                    + "' Where InvoiceNumber='" + invNo + "'");

              }
            } else {
              stmtY = con.createStatement();
              stmtY.execute("Insert Into PartsDelivered (InvoiceNumber, Name) Values ('" + invNo
                  + "', '" + whoDelivered + "') ");
            }

          }
        }
      }
      stmtY.close();
      rsX.close();
      stmtX.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
    } finally {
      if (someChecked) {
        PrintUtils.createRouteSheet(userName, whoDelivered, checkedInvoices);
        session.setAttribute("ForRouteSheet", whoDelivered);
        session.setAttribute("AllCheckedInvoicesForDel", checkedInvoices);
      } else {
        throw new UserException("No Invoices Selected.....");
      }
    }
  }

}
