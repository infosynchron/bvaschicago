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

import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.DamagedPartsForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class DamagedPartsAction extends Action {
  private static final Logger logger = Logger.getLogger(DamagedPartsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    DamagedPartsForm damagedForm = (DamagedPartsForm) form;
    String buttonClicked = damagedForm.getButtonClicked();
    damagedForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    UserBean user = (UserBean) session.getAttribute("User");
    if (session == null || user == null) {
      logger.error("No session or no User in MainMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    PartsBean part = null;
    String partNo = damagedForm.getPartNo();

    String sessionPartName = ((UserBean) session.getAttribute("User")).getUsername();
    sessionPartName += "Part";

    if ((buttonClicked == null || buttonClicked.equals("")) && !forwardPage.trim().equals("Login")) {
      forwardPage = "GetPart";
    }

    if (buttonClicked.equals("GetPart")) {
      if (partNo == null || partNo.trim().equals("")) {
        errorBean.setError("Enter a Part #");
      } else {
        int partOrder = 0;
        if (damagedForm.getPartOrder() != null && !damagedForm.getPartOrder().trim().equals("")) {
          try {
            partOrder = Integer.parseInt(damagedForm.getPartOrder());
          } catch (Exception e) {
            logger.error(e);
            partOrder = 0;
          }
        }
        try {
          part = PartsBean.getPart(partNo, null);
          fillDamagedForm(damagedForm, part, partOrder, user.getUsername());
          session.setAttribute("DamagedPart", part);
        } catch (UserException e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
          damagedForm.reset();
        }
      }
      forwardPage = "DamagedParts";
    } else if (buttonClicked.equals("Change")) {

      try {
        String pNo = damagedForm.getPartNo();
        int pOrder = 0;
        try {
          pOrder = Integer.parseInt(damagedForm.getPartOrder());
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Part Order is Wrong ...");
        }
        Object obj = session.getAttribute("DamagedPart");
        session.removeAttribute("DamagedPart");
        if (obj == null) {
          throw new Exception("Do Not Change Part # Or Part Order #");
        } else {
          part = (PartsBean) obj;
          if (!pNo.trim().equalsIgnoreCase(part.getPartNo().trim())) {
            throw new Exception("Do Not Change Part # Or Part Order #");
          }
        }
        String dateEntered = DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate());
        damagedForm.setDateEntered(DateUtils.getNewUSDate());
        String whoEntered = user.getUsername();
        damagedForm.setWhoEntered(whoEntered);
        double act = part.getActualPrice();
        damagedForm.setActualPrice(act + "");
        double cost = part.getCostPrice();
        damagedForm.setCostPrice(cost + "");
        double sugg = Double.parseDouble(damagedForm.getSuggestedDiscount());
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          if (sugg > (cost - act)) {
            throw new Exception("Discount cann't be that much");
          } else if (sugg > (cost - (cost * 0.70))) {
            throw new Exception("Discount cann't be that much");
          }
        }
        String desc = damagedForm.getDamagedDesc();
        if (desc == null || desc.trim().equals("")) {
          throw new Exception("Enter Damaged Description");
        }

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        stmt.execute("Update DamagedParts Set SuggestedDiscount=" + sugg + ", DamagedDesc='" + desc
            + "' Where PartNo='" + pNo + "' and PartOrder=" + pOrder);
        errorBean.setError("DAMAGED PART CHANGED SUCCESSFULLY ! ! !");

        stmt.close();
        con.close();

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "DamagedParts";
    } else if (buttonClicked.equals("New")) {

      try {
        String pNo = damagedForm.getPartNo();
        int pOrder = 0;
        try {
          pOrder = Integer.parseInt(damagedForm.getPartOrder());
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Part Order is Wrong ...");
        }
        Object obj = session.getAttribute("DamagedPart");
        session.removeAttribute("DamagedPart");
        if (obj == null) {
          throw new Exception("Do Not Change Part # Or Part Order #");
        } else {
          part = (PartsBean) obj;
          if (!pNo.trim().equalsIgnoreCase(part.getPartNo().trim())) {
            throw new Exception("Do Not Change Part # Or Part Order #");
          }
        }
        String dateEntered = DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate());
        damagedForm.setDateEntered(DateUtils.getNewUSDate());
        String whoEntered = user.getUsername();
        damagedForm.setWhoEntered(whoEntered);
        double act = part.getActualPrice();
        damagedForm.setActualPrice(act + "");
        double cost = part.getCostPrice();
        damagedForm.setCostPrice(cost + "");
        double sugg = Double.parseDouble(damagedForm.getSuggestedDiscount());
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          if (sugg > (cost - act)) {
            throw new Exception("Discount cann't be that much");
          } else if (sugg > (cost - (cost * 0.70))) {
            throw new Exception("Discount cann't be that much");
          }
        }
        String desc = damagedForm.getDamagedDesc();
        if (desc == null || desc.trim().equals("")) {
          throw new Exception("Enter Damaged Description");
        }

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        stmt.execute("Insert Into DamagedParts (PartNo, PartOrder, DateEntered, WhoEntered, CostPrice, SuggestedDiscount, ActualPrice, DamagedDesc) Values ('"
            + pNo
            + "', "
            + pOrder
            + ", '"
            + dateEntered
            + "', '"
            + whoEntered
            + "', "
            + cost
            + ", " + sugg + ", " + act + ", '" + desc + "') ");
        errorBean.setError("DAMAGED PART ADDED SUCCESSFULLY ! ! !");

        stmt.close();
        con.close();

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "DamagedParts";
    } else if (buttonClicked.equals("Delete")) {
      try {
        String pNo = damagedForm.getPartNo();
        int pOrder = 0;
        try {
          pOrder = Integer.parseInt(damagedForm.getPartOrder());
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Part Order is Wrong ...");
        }
        Object obj = session.getAttribute("DamagedPart");
        if (obj == null) {
          throw new Exception("Do Not Change Part # Or Part Order #");
        } else {
          part = (PartsBean) obj;
          if (!pNo.trim().equalsIgnoreCase(part.getPartNo().trim())) {
            throw new Exception("Do Not Change Part # Or Part Order #");
          }
        }
        session.removeAttribute("DamagedPart");
        damagedForm.reset();

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        stmt.execute("Delete From DamagedParts Where PartNo='" + pNo + "' and  PartOrder=" + pOrder);
        errorBean.setError("DAMAGED PART DELETED SUCCESSFULLY ! ! !");

        stmt.close();
        con.close();

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "DamagedParts";
    } else if (buttonClicked.equals("List")) {
      try {
        logger.error(1);
        String pNo = damagedForm.getPartNo();
        logger.error(1);
        boolean isHigh = false;
        if (user.getRole().trim().equalsIgnoreCase("High")) {
          isHigh = true;
        }
        Hashtable toShowSales = ReportUtils2.showDamagedParts(pNo, isHigh);
        logger.error(1);
        if (toShowSales != null) {
          logger.error(1);
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          logger.error(1);
          errorBean.setError("No Damaged Parts Available .....");
          forwardPage = "DamagedParts";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "DamagedParts";
      }
    } else if (buttonClicked.equals("Clear")) {
      damagedForm.reset();
      session.removeAttribute("DamagedPart");
      forwardPage = "DamagedParts";
    } else if (buttonClicked.equals("Back")) {
      damagedForm.reset();
      session.removeAttribute("DamagedPart");
      forwardPage = "InvenMaintMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      damagedForm.reset();
      session.removeAttribute("DamagedPart");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("DamagedPartsError", errorBean);
    } else {
      session.removeAttribute("DamagedPartsError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillDamagedForm(DamagedPartsForm form, PartsBean part, int partOrder, String username)
      throws UserException {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      if (part == null) {
        throw new UserException("This part # Not Available");
      }
      form.setPartNo(part.getPartNo());
      form.setPartDescription(MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  "
          + part.getYear() + "  " + part.getPartDescription());
      if (partOrder == 0) {
        form.setCostPrice(part.getCostPrice() + "");
        form.setActualPrice(part.getActualPrice() + "");
        form.setSuggestedDiscount("");
        form.setDamagedDesc("");
        form.setDateEntered(DateUtils.getNewUSDate());
        form.setWhoEntered(username);
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("Select max(partorder) from DamagedParts Where PartNo='"
                + part.getPartNo() + "'");
        if (rs.next()) {
          form.setPartOrder((rs.getInt(1) + 1) + "");
        } else {
          form.setPartOrder(1 + "");
        }
        rs.close();
        stmt.close();
      } else {
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("Select * from DamagedParts Where PartNo='" + part.getPartNo()
                + "' and PartOrder=" + partOrder);
        if (rs.next()) {
          form.setCostPrice(rs.getString("CostPrice"));
          form.setActualPrice(rs.getString("ActualPrice"));
          form.setSuggestedDiscount(rs.getString("SuggestedDiscount"));
          form.setPartOrder(rs.getString("PartOrder"));
          form.setDamagedDesc(rs.getString("DamagedDesc"));
          form.setDateEntered(DateUtils.convertMySQLToUSFormat(rs.getString("DateEntered")));
          form.setWhoEntered(rs.getString("WhoEntered"));
        }
        rs.close();
        stmt.close();
      }

      con.close();
    } catch (Exception e) {
      logger.error(e);
      throw new UserException(e.getMessage());
    }
  }
}
