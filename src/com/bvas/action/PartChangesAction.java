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

import com.bvas.formBeans.PartChangesForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;

public class PartChangesAction extends Action {
  private static final Logger logger = Logger.getLogger(PartChangesAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((PartChangesForm) form).getButtonClicked();
    ((PartChangesForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in ManufacMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetPart")) {
      String partNo = ((PartChangesForm) form).getPartNo();
      String fromDate = ((PartChangesForm) form).getFromDate();
      String toDate = ((PartChangesForm) form).getToDate();
      if (partNo != null && !partNo.trim().equals("")) {
        if (partNo.trim().equals("")) {
          ((PartChangesForm) form).setPartNo("");
          errorBean.setError("THIS PART NOT FOUND IN THE SYSTEM");
        } else {
          fillPartDetails(partNo, session, "", "");
        }
      } else if (!toDate.trim().equals("")) {
        fillPartDetails("", session, fromDate, toDate);
      } else {
        errorBean.setError("ENTER PART NO OR DATES TO GET DETAILS");
      }
      forwardPage = "PartChanges";
    } else if (buttonClicked.equals("Clear")) {
      session.removeAttribute("PartChangesDetails1");
      ((PartChangesForm) form).setPartNo("");
      ((PartChangesForm) form).setFromDate("");
      ((PartChangesForm) form).setToDate("");
      forwardPage = "PartChanges";
    } else if (buttonClicked.equals("Back")) {
      session.removeAttribute("PartChangesDetails1");
      ((PartChangesForm) form).setPartNo("");
      ((PartChangesForm) form).setFromDate("");
      ((PartChangesForm) form).setToDate("");
      forwardPage = "InvenMaintMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("PartChangesDetails1");
      ((PartChangesForm) form).setPartNo("");
      ((PartChangesForm) form).setFromDate("");
      ((PartChangesForm) form).setToDate("");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("PartChangesError", errorBean);
    } else {
      session.removeAttribute("PartChangesError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillPartDetails(String partNo, HttpSession session, String fromDate, String toDate) {
    Vector<String[]> partDetails1 = new Vector<String[]>();

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql1 = "Select PartNo, ModifiedBy, ModifiedDate, Remarks from PartsChanges where ";
      if (!partNo.trim().equals("")) {
        sql1 += " PartNo='" + partNo + "' Order  By ModifiedDate desc";
      } else {

        if (!fromDate.trim().equals("")) {
          sql1 +=
              " ModifiedDate <= '" + DateUtils.convertUSToMySQLFormat(toDate)
                  + "' and ModifiedDate >= '" + DateUtils.convertUSToMySQLFormat(fromDate) + "'";
        } else {
          sql1 += " ModifiedDate = '" + DateUtils.convertUSToMySQLFormat(toDate) + "' ";
        }
        sql1 += " Order By ModifiedDate desc";
      }
      
      ResultSet rs = stmt.executeQuery(sql1);
      while (rs.next()) {

        String[] retStr = new String[4];

        String partNum = rs.getString(1);
        String pers = rs.getString(2);
        String dt = DateUtils.convertMySQLToUSFormat(rs.getString(3));
        String remarks = rs.getString(4);

        retStr[0] = partNum;
        retStr[1] = pers;
        retStr[2] = dt;
        retStr[3] = remarks;

        partDetails1.addElement(retStr);
        
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    if (partDetails1 != null && partDetails1.size() > 0) {
      session.setAttribute("PartChangesDetails1", partDetails1);
    }

  }

}
