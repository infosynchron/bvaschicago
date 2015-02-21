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
import com.bvas.formBeans.MissingPartsForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.ReportUtils2;
import com.bvas.utils.UserException;

public class MissingPartsAction extends Action {
  private static final Logger logger = Logger.getLogger(MissingPartsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    MissingPartsForm missingForm = (MissingPartsForm) form;
    String buttonClicked = missingForm.getButtonClicked();
    missingForm.setButtonClicked("");
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
    String partNo = missingForm.getPartNo();

    if ((buttonClicked == null || buttonClicked.equals("")) && !forwardPage.trim().equals("Login")) {
      forwardPage = "GetPart";
    }

    if (buttonClicked.equals("GetPart")) {
      if (partNo == null || partNo.trim().equals("")) {
        errorBean.setError("Enter a Part #");
      } else {
        int partOrder = 0;
        if (missingForm.getPartOrder() != null && !missingForm.getPartOrder().trim().equals("")) {
          try {
            partOrder = Integer.parseInt(missingForm.getPartOrder());
          } catch (Exception e) {
            partOrder = 0;
            logger.error(e);
          }
        }
        try {
          part = PartsBean.getPart(partNo, null);
          fillMissingForm(missingForm, part, partOrder, user.getUsername());
          session.setAttribute("MissingPart", part);
        } catch (UserException e) {
          errorBean.setError(e.getMessage());
          missingForm.reset();
          logger.error(e);
        }
      }
      forwardPage = "MissingParts";
    } else if (buttonClicked.equals("Change")) {

      try {
        String pNo = missingForm.getPartNo();
        int pOrder = 0;
        try {
          pOrder = Integer.parseInt(missingForm.getPartOrder());
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Part Order is Wrong ...");
        }
        Object obj = session.getAttribute("MissingPart");
        if (obj == null) {
          throw new Exception("Do Not Change Part # Or Part Order #");
        } else {
          part = (PartsBean) obj;
          if (!pNo.trim().equalsIgnoreCase(part.getPartNo().trim())) {
            throw new Exception("Do Not Change Part # Or Part Order #");
          }
        }

        int qty = 0;
        try {
          qty = Integer.parseInt(missingForm.getQuantity());
          if (qty < 0) {
            throw new Exception("Quantity Must Be Positive ..... ");
          }
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Quantity Must Be Positive ..... ");
        }

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("Select Quantity, ActualPrice, CostPrice From MissingParts Where PartNo='"
                + pNo + "' and PartOrder=" + pOrder);
        int oldQty = 0;
        double oldAct = 0.0;
        double oldCost = 0.0;
        double newAct = 0.0;
        double newCost = 0.0;
        double newPartAct = 0.0;
        if (rs.next()) {
          oldQty = rs.getInt(1);
          oldAct = rs.getDouble(2);
          oldCost = rs.getDouble(3);
        } else {
          throw new Exception("Something is Wrong, Try Later .....");
        }
        if (qty != oldQty) {
          if (qty > oldQty) {
            newAct =
                (((oldQty * oldAct) + ((qty - oldQty) * part.getActualPrice())) / (oldQty + (qty - oldQty)));
            newCost =
                (((oldQty * oldCost) + ((qty - oldQty) * part.getCostPrice())) / (oldQty + (qty - oldQty)));

            Statement stmt1 = con.createStatement();
            stmt1
                .execute("Update MissingParts Set Quantity=" + qty + ", CostPrice=" + newCost
                    + ", ActualPrice=" + newAct + " Where PartNo='" + pNo + "' and PartOrder="
                    + pOrder);
            errorBean.setError("MISSING PART CHANGED SUCCESSFULLY ! ! !");
            stmt1.close();
          } else {
            newPartAct =
                (((part.getUnitsInStock() * part.getActualPrice()) + ((oldQty - qty) * oldAct)) / (part
                    .getUnitsInStock() + (oldQty - qty)));

            Statement stmt1 = con.createStatement();
            stmt1.execute("Update Parts Set ActualPrice=" + newPartAct + " Where PartNo='" + pNo
                + "'");

            Statement stmt2 = con.createStatement();
            stmt2.execute("Update MissingParts Set Quantity=" + qty + " Where PartNo='" + pNo
                + "' and PartOrder=" + pOrder);
            errorBean.setError("MISSING PART CHANGED SUCCESSFULLY ! ! !");
            stmt1.close();
            stmt2.close();
          }
          part.setUnitsInStock(part.getUnitsInStock() - (qty - oldQty));
          PartsBean.changeQuantity(part.getPartNo(), part.getUnitsInStock());
          missingForm.setUnitsInStock(part.getUnitsInStock() + "");

        }
        session.removeAttribute("MissingPart");
        rs.close();
        stmt.close();
        con.close();

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MissingParts";
    } else if (buttonClicked.equals("New")) {

      try {
        String pNo = missingForm.getPartNo();
        int pOrder = 0;
        try {
          pOrder = Integer.parseInt(missingForm.getPartOrder());
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Part Order is Wrong ...");
        }
        Object obj = session.getAttribute("MissingPart");
        session.removeAttribute("MissingPart");
        if (obj == null) {
          throw new Exception("Do Not Change Part # Or Part Order #");
        } else {
          part = (PartsBean) obj;
          if (!pNo.trim().equalsIgnoreCase(part.getPartNo().trim())) {
            throw new Exception("Do Not Change Part # Or Part Order #");
          }
        }
        String dateEntered = DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate());
        missingForm.setDateEntered(DateUtils.getNewUSDate());
        String whoEntered = user.getUsername();
        missingForm.setWhoEntered(whoEntered);
        double act = part.getActualPrice();
        missingForm.setActualPrice(act + "");
        double cost = part.getCostPrice();
        missingForm.setCostPrice(cost + "");
        int qty = 0;
        try {
          qty = Integer.parseInt(missingForm.getQuantity());
          if (qty == 0) {
            throw new Exception("Must Enter How Many Pieces Missing ...");
          }
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Must Enter How Many Pieces Missing ...");
        }

        part.setUnitsInStock(part.getUnitsInStock() - qty);
        PartsBean.changeQuantity(part.getPartNo(), part.getUnitsInStock());
        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        stmt.execute("Insert Into MissingParts (PartNo, PartOrder, DateEntered, WhoEntered, CostPrice, Quantity, ActualPrice) Values ('"
            + pNo
            + "', "
            + pOrder
            + ", '"
            + dateEntered
            + "', '"
            + whoEntered
            + "', "
            + cost
            + ", " + qty + ", " + act + ") ");
        errorBean.setError("MISSING PART ADDED SUCCESSFULLY ! ! !");

        stmt.close();
        con.close();

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MissingParts";
    } else if (buttonClicked.equals("Delete")) {
      try {
        String pNo = missingForm.getPartNo();
        int pOrder = 0;
        try {
          pOrder = Integer.parseInt(missingForm.getPartOrder());
        } catch (Exception e) {
          logger.error(e);
          throw new Exception("Part Order is Wrong ...");
        }
        Object obj = session.getAttribute("MissingPart");
        if (obj == null) {
          throw new Exception("Do Not Change Part # Or Part Order #");
        } else {
          part = (PartsBean) obj;
          if (!pNo.trim().equalsIgnoreCase(part.getPartNo().trim())) {
            throw new Exception("Do Not Change Part # Or Part Order #");
          }
        }

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("Select Quantity, ActualPrice, CostPrice From MissingParts Where PartNo='"
                + pNo + "' and PartOrder=" + pOrder);
        int oldQty = 0;
        double oldAct = 0.0;
        double oldCost = 0.0;
        double newPartAct = 0.0;
        if (rs.next()) {
          oldQty = rs.getInt(1);
          oldAct = rs.getDouble(2);
          oldCost = rs.getDouble(3);
        } else {
          throw new Exception("Something is Wrong, Try Later .....");
        }
        if (part.getUnitsInStock() + oldQty == 0) {
          newPartAct = oldAct;
        } else {
          newPartAct =
              (((part.getUnitsInStock() * part.getActualPrice()) + (oldQty * oldAct)) / (part
                  .getUnitsInStock() + oldQty));
        }
        Statement stmt1 = con.createStatement();
        stmt1.execute("Update Parts Set ActualPrice=" + newPartAct + " Where PartNo='" + pNo + "'");

        Statement stmt2 = con.createStatement();
        stmt2
            .execute("Delete From MissingParts Where PartNo='" + pNo + "' and PartOrder=" + pOrder);

        part.setUnitsInStock(part.getUnitsInStock() + oldQty);
        PartsBean.changeQuantity(part.getPartNo(), part.getUnitsInStock());
        missingForm.setUnitsInStock(part.getUnitsInStock() + "");

        session.removeAttribute("MissingPart");
        missingForm.reset();
        errorBean.setError("MISSING PART DELETED SUCCESSFULLY ! ! !");

        rs.close();
        stmt.close();
        stmt1.close();
        con.close();

      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "MissingParts";
    } else if (buttonClicked.equals("List")) {
      try {
        logger.error(1);
        String pNo = missingForm.getPartNo();
        logger.error(1);
        boolean isHigh = false;
        if (user.getRole().trim().equalsIgnoreCase("High")) {
          isHigh = true;
        }
        Hashtable toShowSales = ReportUtils2.showMissingParts(pNo, isHigh);
        logger.error(1);
        if (toShowSales != null) {
          logger.error(1);
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          logger.error(1);
          errorBean.setError("No Missing Parts Available .....");
          forwardPage = "MissingParts";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("Please Check The Errors:" + e.getMessage());
        forwardPage = "MissingParts";
      }
    } else if (buttonClicked.equals("Clear")) {
      missingForm.reset();
      session.removeAttribute("MissingPart");
      forwardPage = "MissingParts";
    } else if (buttonClicked.equals("Back")) {
      missingForm.reset();
      session.removeAttribute("MissingPart");
      forwardPage = "InvenMaintMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      missingForm.reset();
      session.removeAttribute("MissingPart");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("MissingPartsError", errorBean);
    } else {
      session.removeAttribute("MissingPartsError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillMissingForm(MissingPartsForm form, PartsBean part, int partOrder, String username)
      throws UserException {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      if (part == null) {
        throw new UserException("This part # Not Available");
      }
      form.setPartNo(part.getPartNo());
      form.setPartDescription(MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  "
          + part.getYear() + "  " + part.getPartDescription());
      form.setUnitsInStock(part.getUnitsInStock() + "");
      if (partOrder == 0) {
        form.setCostPrice(part.getCostPrice() + "");
        form.setActualPrice(part.getActualPrice() + "");
        form.setQuantity("");
        form.setDateEntered(DateUtils.getNewUSDate());
        form.setWhoEntered(username);
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("Select max(partorder) from MissingParts Where PartNo='"
                + part.getPartNo() + "'");
        if (rs.next()) {
          form.setPartOrder((rs.getInt(1) + 1) + "");
        } else {
          form.setPartOrder(1 + "");
        }
      } else {
        Statement stmt = con.createStatement();
        ResultSet rs =
            stmt.executeQuery("Select * from MissingParts Where PartNo='" + part.getPartNo()
                + "' and PartOrder=" + partOrder);
        if (rs.next()) {
          form.setQuantity(rs.getString("Quantity"));
          form.setCostPrice(rs.getString("CostPrice"));
          form.setActualPrice(rs.getString("ActualPrice"));
          form.setPartOrder(rs.getString("PartOrder"));
          form.setDateEntered(DateUtils.convertMySQLToUSFormat(rs.getString("DateEntered")));
          form.setWhoEntered(rs.getString("WhoEntered"));
        } else {
          fillMissingForm(form, part, 0, username);
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
