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

import com.bvas.formBeans.PartsPulledForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class PartsPulledAction extends Action {
  private static final Logger logger = Logger.getLogger(PartsPulledAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    PartsPulledForm pulledForm = (PartsPulledForm) form;
    String buttonClicked = pulledForm.getButtonClicked();
    ((PartsPulledForm) pulledForm).setButtonClicked("");
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
      String whoPulled = (String) request.getParameter("whoPulled");
      if (whoPulled == null || whoPulled.trim().equals("") || whoPulled.trim().equals("Select")) {
        errorBean.setError("You Must Select An Option");
        session.removeAttribute("RoutingPendingInvoices");
        pulledForm.reset();
      } else {
        try {
          changeInvoices(request, whoPulled);
        } catch (Exception e) {
          errorBean.setError(e.getMessage());
          logger.error(e);
        }
        session.removeAttribute("RoutingPendingInvoices");
        pulledForm.reset();
      }
      forwardPage = "PartsPulled";
    } else if (buttonClicked.equals("BackToRouting")) {
      pulledForm.reset();
      session.removeAttribute("RoutingPendingInvoices");
      forwardPage = "RoutingMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      pulledForm.reset();
      session.removeAttribute("RoutingPendingInvoices");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("PartsPulledError", errorBean);
    } else {
      session.removeAttribute("PartsPulledError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void changeInvoices2(HttpServletRequest request, String whoPulled) throws UserException {
    HttpSession session = request.getSession(false);
    Vector pendingInvoices = (Vector) session.getAttribute("RoutingPendingInvoices");    
    Hashtable<String, String> checkedInvoices = new Hashtable<String, String>();
    boolean someChecked = false;
    if (pendingInvoices == null) {
      throw new UserException("No Invoices Found");
    }
    Connection con = null;
    Statement stmt = null;
    try {
       con = DBInterfaceLocal.getSQLConnection();
      for (int i = 1; i <= pendingInvoices.size(); i++) {
        String checkedBox = (String) request.getParameter("PP" + i);
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
           stmt = con.createStatement();
          stmt.execute("Insert Into PartsPulled (InvoiceNumber, Name) Values ('" + invNo + "', '"
              + whoPulled + "') ");
          stmt.close();
        }
      }// for loop
    } catch (SQLException e) {
      logger.error(e);
    }finally {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

    if (!someChecked) {
      throw new UserException("No Invoices Selected.....");
    }

  }

  public void changeInvoices(HttpServletRequest request, String whoPulled) throws UserException {
    HttpSession session = request.getSession(false);
    Vector pendingInvoices = (Vector) session.getAttribute("RoutingPendingInvoices");
    if (pendingInvoices == null) {
      throw new UserException("No Invoices Found");
    }

    Hashtable<String, String> checkedInvoices = new Hashtable<String, String>();
    boolean someChecked = false;

    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    for (int i = 1; i <= pendingInvoices.size(); i++) {
      String checkedBox = (String) request.getParameter("PP" + i);
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
          try {
             stmt = con.createStatement();
            stmt.execute("Insert Into PartsPulled (InvoiceNumber, Name) Values ('" + invNo + "', '"
                + whoPulled + "') ");
            stmt.close();
          } catch (Exception e) {
            logger.error(e);
          }finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
        }

      } catch (Exception e) {
        logger.error(e);
      }

    }
    try {
      con.close();
    } catch (SQLException e) {
      logger.error(e);
    }
    if (!someChecked) {
      throw new UserException("No Invoices Selected.....");
    }

  }// main

}
