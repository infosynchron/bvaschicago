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

import com.bvas.formBeans.ShowPatternForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;

public class ShowPatternAction extends Action {
  private static final Logger logger = Logger.getLogger(ShowPatternAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((ShowPatternForm) form).getButtonClicked();
    ((ShowPatternForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in ManufacMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      buttonClicked = "GetPattern";
    }

    if (buttonClicked.equals("GetPattern")) {
      String partNo = ((ShowPatternForm) form).getPartNo();
      if (partNo != null && !partNo.trim().equals("")) {
        if (partNo.length() < 2 || partNo.length() > 4) {
          errorBean.setError("CHECK THE LENGTH OF THE STRING... SHOULD BE 2 OR 3 OR 4");
        } else {
          fillPartDetails(partNo, session);
        }
      } else {
        errorBean.setError("ENTER A PATTERN TO GET THE PART NO'S");
      }

      forwardPage = "ShowPattern";
    } else if (buttonClicked.equals("Clear")) {
      session.removeAttribute("PatternDetails");
      ((ShowPatternForm) form).setPartNo("");
      forwardPage = "ShowPattern";
    } else if (buttonClicked.equals("Back")) {
      session.removeAttribute("PatternDetails");
      ((ShowPatternForm) form).setPartNo("");
      forwardPage = "InvenMaintMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("PatternDetails");
      ((ShowPatternForm) form).setPartNo("");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("ShowPatternError", errorBean);
    } else {
      session.removeAttribute("ShowPatternError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillPartDetails(String partNo, HttpSession session) {
    Vector<String[]> patternDetails = new Vector<String[]>();

    patternDetails = addPartDetails(partNo, patternDetails);

    if (patternDetails != null && patternDetails.size() > 0) {
      session.setAttribute("PatternDetails", patternDetails);
    }

  }

  public Vector<String[]> addPartDetails(String partNo, Vector<String[]> partDetails) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select PartNo, MakeModelName, Year, PartDescription From Parts a, MakeModel b Where PartNo like '"
              + partNo + "%' and a.makemodelcode=b.makemodelcode order by 1 ");
      int totQty = 0;
      while (rs.next()) {

        String[] retStr = new String[4];

        String partNum = rs.getString(1);
        String makemodel = rs.getString(2);
        String year = rs.getString(3);
        String desc = rs.getString(4);

        totQty++;
        retStr[0] = partNum;
        retStr[1] = makemodel;
        retStr[2] = year;
        retStr[3] = desc;

        partDetails.addElement(retStr);
      }

      if (totQty != 0) {
        String[] retStr = new String[4];
        retStr[0] = totQty + "";
        retStr[1] = "";
        retStr[2] = "";
        retStr[3] = "";

        partDetails.addElement(retStr);
      }

      rs.close();
      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return partDetails;

  }
}
