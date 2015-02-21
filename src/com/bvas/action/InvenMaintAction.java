package com.bvas.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
import com.bvas.formBeans.InvenMaintForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.NumberUtils;
import com.bvas.utils.UserException;

public class InvenMaintAction extends Action {
  private static final Logger logger = Logger.getLogger(InvenMaintAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    InvenMaintForm invenForm = (InvenMaintForm) form;
    String buttonClicked = invenForm.getButtonClicked();
    invenForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    UserBean user = (UserBean) session.getAttribute("User");
    if (session == null || user == null) {
      logger.error("No session or no User in MainMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    // logger.error(new java.util.Date(System.currentTimeMillis()) +
    // "-----InvenMaint-----" + user.getUsername());
    PartsBean part = null;
    String partNo = invenForm.getPartNo();

    String sessionPartName = ((UserBean) session.getAttribute("User")).getUsername();
    sessionPartName += "Part";

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetPart")) {
      if (partNo == null || partNo.trim().equals("")) {
        logger.error("In Inven Maint Action - Enter a Part");
      } else {
        part = new PartsBean();
        part = part.getPart(partNo, null);
        try {
          fillInvenMaintForm(invenForm, part);
          session.setAttribute("InvenMaintExtraMessage", part.getExtraMessage());
          session.removeAttribute(sessionPartName);
          session.setAttribute("SubCatSelected", part.getSubCategory() + "");
          session.setAttribute(sessionPartName, part);
        } catch (UserException e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
          invenForm.reset();
          session.removeAttribute(sessionPartName);
        }
      }
      forwardPage = "InvenMaint";
    } else if (buttonClicked.equals("ChangeInventory")) {

      try {
        Object o = session.getAttribute(sessionPartName);
        if (o == null) {
          logger.error("In Inven Maint Action - This part Can Not Be Changed - No Session");
        } else {
          part = (PartsBean) o;
          boolean ok = changePartBean(invenForm, part, user);
          if (ok) {

            try {
              if (part.getPartNo().trim().equalsIgnoreCase(part.getInterchangeNo().trim())) {
                throw new UserException("INTERCHANGE # IS SAME AS PART # -- CANN'T CHANGE");
              } else {
                if (!part.getInterchangeNo().trim().equals("")) {
                  PartsBean pp = PartsBean.getPart(part.getInterchangeNo(), null);
                  if (pp.getInterchangeNo() != null && !pp.getInterchangeNo().trim().equals("")) {
                    throw new UserException("THIS INTERCHANGE # CANN'T BE USED");
                  }
                }
              }
              String catCode = (String) request.getParameter("CategorySelected");
              session.setAttribute("SubCatSelected", catCode);

              part.setSubCategory(catCode);

              part.changePart();
              fillInvenMaintForm(invenForm, part);
              session.setAttribute("InvenMaintExtraMessage", part.getExtraMessage());
              errorBean.setError("THIS PART CHANGED SUCCESSFULLY!!!");
            } catch (Exception e) {
              logger.error(e);
              errorBean.setError("In Inven Maint Action - " + e);
            }
          } else {
            errorBean.setError("In Inven Maint Action - This part Can Not Be Changed");
          }
        }
      } catch (Exception ex) {
        logger.error(ex);
        errorBean.setError(ex.getMessage());
      }

      forwardPage = "InvenMaint";
    } else if (buttonClicked.equals("EnterNewSKUs")) {

      Object o = session.getAttribute(sessionPartName);
      if (o != null) {
        if (partNo == null || partNo.trim().equals("")) {
          logger.error("In Inven Maint Action - No Part No Available");
        } else {
          part = (PartsBean) o;
          if (partNo.trim().equals(part.getPartNo())) {
            logger.error("In Inven Maint Action - This is not a new part");
          } else {
            try {
              part = createPartBean(invenForm, user);
              part.addNewPart();
              errorBean.setError("THIS PART CHANGED SUCCESSFULLY!!!");
            } catch (Exception e) {
              logger.error(e);
              errorBean.setError("This part not added. Check your values...");
            }
          }
        }
      } else {
        try {
          part = createPartBean(invenForm, user);
          part.addNewPart();
          errorBean.setError("THIS PART CHANGED SUCCESSFULLY!!!");
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This part not added. Check your values...");
        }
      }

      forwardPage = "InvenMaint";
    } else if (buttonClicked.equals("DeletePart")) {
      Object o = session.getAttribute(sessionPartName);
      if (o == null) {
        logger.error("This part Can Not Be Changed - No Session");
      } else {
        try {
          part = (PartsBean) o;
          part.deletePart();
          invenForm.reset();
          session.removeAttribute("SubCatSelected");
        } catch (UserException e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("This Part is NOT deleted");
        }
      }

      forwardPage = "InvenMaint";
    } else if (buttonClicked.equals("ClearPart")) {
      invenForm.reset();
      session.setAttribute("SubCatSelected", "0");
      session.removeAttribute(sessionPartName);

      forwardPage = "InvenMaint";
    } else if (buttonClicked.equals("GoToHistory")) {
      session.setAttribute("PartNoForPartHistory", partNo);
      forwardPage = "PartHistory";
    } else if (buttonClicked.equals("GoToAvail")) {
      invenForm.reset();
      session.removeAttribute("AllSubCategories");
      session.removeAttribute("SubCatSelected");
      session.removeAttribute(sessionPartName);

      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("ReturnToMain")) {
      invenForm.reset();
      session.removeAttribute("AllSubCategories");
      session.removeAttribute("SubCatSelected");
      session.removeAttribute(sessionPartName);
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("InvenMaintError", errorBean);
    } else {
      session.removeAttribute("InvenMaintError");
    }

    return (mapping.findForward(forwardPage));

  }

  public boolean changePartBean(InvenMaintForm invenForm, PartsBean part, UserBean user)
      throws UserException {
    boolean ok = false;
    if (!invenForm.getPartNo().trim().equalsIgnoreCase(part.getPartNo())) {
      logger.error("In Inven Maint Action - This part cannot be changed");
    } else {
      fillIntoPart(invenForm, part, user);
      ok = true;
    }
    return ok;
  }

  public void fillInvenMaintForm(InvenMaintForm form, PartsBean part) throws UserException {

    if (part == null) {
      throw new UserException("This part Not Available");
    }
    form.setPartNo(part.getPartNo());
    form.setInterchangeNo(part.getInterchangeNo());
    form.setPartDescription(part.getPartDescription());
    form.setMakeModelName(MakeModelBean.getMakeModelName(part.getMakeModelCode()));
    form.setYear(part.getYear());
    form.setSupplierId(part.getSupplierId() + "");
    form.setCostPrice(part.getCostPrice() + "");
    form.setListPrice(part.getListPrice() + "");
    form.setActualPrice(part.getActualPrice() + "");
    form.setDiscount("");
    form.setUnitsInStock(part.getUnitsInStock() + "");
    form.setUnitsOnOrder(part.getUnitsOnOrder() + "");
    form.setReorderLevel(part.getReorderLevel() + "");
    //form.setCompPrice1(part.getCompPrice1() + "");
    //form.setCompPrice2(part.getCompPrice2() + "");
    //form.setCompPrice3(part.getCompPrice3() + "");
    form.setKeystoneNumber(part.getKeystoneNumber());
    form.setOemNumber(part.getOemNumber());
    form.setLocation(part.getLocation());

  }

  public PartsBean createPartBean(InvenMaintForm invenForm, UserBean user) {
    PartsBean part = new PartsBean();

    part.setPartNo(invenForm.getPartNo().trim());
    part.setMakeModelCode(MakeModelBean.getMakeModelCode(invenForm.getMakeModelName().trim()));
    fillIntoPart(user, invenForm, part);

    return part;
  }

  public void fillIntoPart(InvenMaintForm invenForm, PartsBean part, UserBean user)
      throws UserException {

    File returns = new File("c:/bvaschicago/Data/PartsChanges.txt");
    FileWriter wrt = null;
    try {
      wrt = new FileWriter(returns, true);
    } catch (Exception e) {
      logger.error(e);
    }

    String histString = "";
    if ((part.getInterchangeNo() == null || part.getInterchangeNo().trim().equals(""))
        && invenForm.getInterchangeNo() != null && !invenForm.getInterchangeNo().trim().equals("")) {
      histString += "Interchange No Added: " + invenForm.getInterchangeNo();
      try {
        wrt.write("Update Parts Set InterChangeNo='" + invenForm.getInterchangeNo()
            + "' where PartNo='" + part.getPartNo() + "';\n");
      } catch (Exception e) {
        logger.error(e);
      }
    } else if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")
        && (invenForm.getInterchangeNo() == null || invenForm.getInterchangeNo().trim().equals(""))) {
      histString += "Interchange No " + part.getInterchangeNo() + " Removed ";
      try {
        wrt.write("Update Parts Set InterChangeNo='' where PartNo='" + part.getPartNo() + "';\n");
      } catch (Exception e) {
        logger.error(e);
      }
    } else if (part.getInterchangeNo() != null && invenForm.getInterchangeNo() != null
        && !part.getInterchangeNo().trim().equalsIgnoreCase(invenForm.getInterchangeNo().trim())) {
      histString +=
          "Interchange No:: " + part.getInterchangeNo() + " -->> " + invenForm.getInterchangeNo();
      try {
        wrt.write("Update Parts Set InterChangeNo='" + invenForm.getInterchangeNo()
            + "' where PartNo='" + part.getPartNo() + "';\n");
      } catch (Exception e) {
        logger.error(e);
      }
    }
    part.setInterchangeNo(invenForm.getInterchangeNo());
    if (!part.getPartDescription().trim().equalsIgnoreCase(invenForm.getPartDescription().trim())) {
      if (histString.trim().equals("")) {
        histString +=
            "Desc:: " + part.getPartDescription() + " -->> " + invenForm.getPartDescription();
        try {
          wrt.write("Update Parts Set PartDescription='" + invenForm.getPartDescription()
              + "' where PartNo='" + part.getPartNo() + "';\n");
        } catch (Exception e) {
          logger.error(e);
        }
      } else {
        histString +=
            "<br/>Desc:: " + part.getPartDescription() + " -->> " + invenForm.getPartDescription();
        try {
          wrt.write("Update Parts Set PartDescription='" + invenForm.getPartDescription()
              + "' where PartNo='" + part.getPartNo() + "';\n");
        } catch (Exception e) {
          logger.error(e);
        }
      }
    }
    part.setPartDescription(invenForm.getPartDescription());
    if (part.getYear() != null && invenForm.getYear() != null
        && !part.getYear().trim().equalsIgnoreCase(invenForm.getYear().trim())) {
      if (histString.trim().equals("")) {
        histString += "Year:: " + part.getYear() + " -->> " + invenForm.getYear();
        try {
          wrt.write("Update Parts Set Year='" + invenForm.getYear() + "' where PartNo='"
              + part.getPartNo() + "';\n");
        } catch (Exception e) {
          logger.error(e);
        }
      } else {
        histString += "<br/>Year:: " + part.getYear() + " -->> " + invenForm.getYear();
        try {
          wrt.write("Update Parts Set Year='" + invenForm.getYear() + "' where PartNo='"
              + part.getPartNo() + "';\n");
        } catch (Exception e) {
          logger.error(e);
        }
      }
    }
    try {
      wrt.close();
    } catch (Exception e) {
    }
    part.setYear(invenForm.getYear());
    try {
      part.setSupplierId(Integer.parseInt(invenForm.getSupplierId()));
    } catch (Exception e) {
      logger.error(e);
    }
    double origCost = part.getCostPrice();
    double newCost = 0.0;
    double origList = part.getListPrice();
    double newList = 0.0;
    double actPrice = part.getActualPrice();
    try {
      newCost = Double.parseDouble(invenForm.getCostPrice());
      newList = Double.parseDouble(invenForm.getListPrice());
    } catch (Exception e) {
      logger.error(e);
    }

    if (actPrice != 0) {
      if (newCost < origCost && !user.getRole().equalsIgnoreCase("High")) {
        double calCost = 0.0;
        if (actPrice < 5) {
          calCost = actPrice / 0.40;
        } else if (actPrice < 10) {
          calCost = actPrice / 0.45;
        } else if (actPrice < 15) {
          calCost = actPrice / 0.50;
        } else if (actPrice < 20) {
          calCost = actPrice / 0.55;
        } else if (actPrice < 50) {
          calCost = actPrice / 0.60;
        } else if (actPrice < 75) {
          calCost = actPrice / 0.65;
        } else if (actPrice < 125) {
          calCost = actPrice / 0.70;
        } else if (actPrice < 200) {
          calCost = actPrice / 0.75;
        } else {
          calCost = actPrice / 0.80;
        }
        if (newCost < calCost) {
          throw new UserException("*****     THE COST PRICE IS TOO LOW     *****");
        } else {
          part.setCostPrice(newCost);
        }
      } else {
        part.setCostPrice(newCost);
      }
      if (newList < origList && !user.getRole().equalsIgnoreCase("High")) {
        double calList = 0.0;
        if (actPrice < 5) {
          calList = (actPrice / 0.40) / 0.70;
        } else if (actPrice < 10) {
          calList = (actPrice / 0.45) / 0.70;
        } else if (actPrice < 15) {
          calList = (actPrice / 0.50) / 0.70;
        } else if (actPrice < 20) {
          calList = (actPrice / 0.55) / 0.70;
        } else if (actPrice < 50) {
          calList = (actPrice / 0.60) / 0.70;
        } else if (actPrice < 75) {
          calList = (actPrice / 0.65) / 0.70;
        } else if (actPrice < 125) {
          calList = (actPrice / 0.70) / 0.70;
        } else if (actPrice < 200) {
          calList = (actPrice / 0.75) / 0.70;
        } else {
          calList = (actPrice / 0.80) / 0.70;
        }
        if (newList < calList) {
          throw new UserException("*****     THE LIST PRICE IS TOO LOW     *****");
        } else {
          part.setListPrice(newList);
        }
      } else {
        part.setListPrice(newList);
      }
    } else {
      part.setCostPrice(newCost);
      part.setListPrice(newList);
    }

    try {
      part.setActualPrice(Double.parseDouble(invenForm.getActualPrice()));
    } catch (Exception e) {
      logger.error(e);
    }

    double discount = 0.0;
    try {
      discount = Double.parseDouble(invenForm.getDiscount());
    } catch (Exception e) {
      logger.error(e);
    }
    if (discount > 0) {
      // double costPr = 0.0;
      // costPr = part.getListPrice() * (1 - discount / 100);
      // costPr = NumberUtils.cutFractions(costPr);
      // part.setCostPrice(costPr);
      // invenForm.setCostPrice(costPr + "");
      // invenForm.setDiscount("");
      double listPr = 0.0;
      listPr = part.getCostPrice() / (1 - discount / 100);
      listPr = NumberUtils.cutFractions(listPr);
      part.setListPrice(listPr);
      invenForm.setListPrice(listPr + "");
      invenForm.setDiscount("");
    }

    if (origCost != part.getCostPrice()) {
      if (histString.trim().equals("")) {
        histString += "Cost:: " + origCost + " -->> " + part.getCostPrice();
      } else {
        histString += "<br/>Cost:: " + origCost + " -->> " + part.getCostPrice();
      }
    }
    if (origList != part.getListPrice()) {
      if (histString.trim().equals("")) {
        histString += "List:: " + origList + " -->> " + part.getListPrice();
      } else {
        histString += "<br/>List:: " + origList + " -->> " + part.getListPrice();
      }
    }

    try {
      part.setUnitsOnOrder(Integer.parseInt(invenForm.getUnitsOnOrder()));
    } catch (Exception e) {
      logger.error(e);
    }
    try {
      int origUnits = 0;
      int origUnits2 = 0;
      Connection con = DBInterfaceLocal.getSQLConnection();
      origUnits2 = PartsBean.getQuantity(part.getPartNo(), con);
      if (part.getUnitsInStock() != origUnits2) {
        origUnits = origUnits2;
      } else {
        origUnits = part.getUnitsInStock();
      }
      int newUnits = Integer.parseInt(invenForm.getUnitsInStock());
      if (origUnits != newUnits) {
        if (histString.trim().equals("")) {
          histString += "Stock:: " + origUnits + " -->> " + newUnits;
        } else {
          histString += "<br/>Stock:: " + origUnits + " -->> " + newUnits;
        }
      }
      con.close();
      part.setUnitsInStock(newUnits);
    } catch (Exception e) {
      logger.error(e);
    }
    try {
      part.setReorderLevel(Integer.parseInt(invenForm.getReorderLevel()));
    } catch (Exception e) {
      logger.error(e);
    }
//    try {
//      part.setCompPrice1(Double.parseDouble(invenForm.getCompPrice1()));
//    } catch (Exception e) {
//      logger.error(e);
//    }
//    try {
//      part.setCompPrice2(Double.parseDouble(invenForm.getCompPrice2()));
//    } catch (Exception e) {
//      logger.error(e);
//    }
//    try {
//      part.setCompPrice3(Double.parseDouble(invenForm.getCompPrice3()));
//    } catch (Exception e) {
//      logger.error(e);
//    }
    part.setKeystoneNumber(invenForm.getKeystoneNumber());
    part.setOemNumber(invenForm.getOemNumber());
    part.setLocation(invenForm.getLocation());
    if (!histString.trim().equals("")) {
      String xx = histString;
      while (xx.length() > 0) {
        if (xx.length() > 240) {
          writeHistory(part.getPartNo(), xx.substring(0, 240), user.getUsername());
          xx = xx.substring(240);
        } else {
          writeHistory(part.getPartNo(), xx, user.getUsername());
          xx = "";
        }
      }
    }
  }

  public void writeHistory(String partNo, String histString, String username) {
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select Max(ModifiedOrder) From PartsChanges Where PartNo='" + partNo
              + "'");
      int orderNo = 0;
      if (rs1.next()) {
        orderNo = rs1.getInt(1) + 1;
      } else {
        orderNo++;
      }
      String dt = DateUtils.getNewUSDate();
      Statement stmt2 = con.createStatement();
      stmt2
          .execute("Insert Into PartsChanges (PartNo, ModifiedBy, ModifiedDate, ModifiedOrder, Remarks) Values ('"
              + partNo
              + "', '"
              + username
              + "', '"
              + DateUtils.convertUSToMySQLFormat(dt)
              + "', "
              + orderNo + ", '" + histString + "') ");
      rs1.close();
      stmt1.close();
      stmt2.close();
      con.close();
    } catch (Exception e) {
      logger.error("Exception When Writing Part History : " + e.getMessage());
    }
  }

  public void fillIntoPart(UserBean user, InvenMaintForm invenForm, PartsBean part) {

    File returns = new File("c:/bvaschicago/Data/PartsChanges.txt");
    FileWriter wrt = null;
    try {
      wrt = new FileWriter(returns, true);
    } catch (Exception e) {
      logger.error(e);
    }

    String histString = "";

    part.setInterchangeNo(invenForm.getInterchangeNo());
    part.setPartDescription(invenForm.getPartDescription());
    part.setYear(invenForm.getYear());
    try {
      part.setSupplierId(Integer.parseInt(invenForm.getSupplierId()));
    } catch (Exception e) {
      logger.error(e);
    }
    try {
      part.setCostPrice(Double.parseDouble(invenForm.getCostPrice()));
    } catch (Exception e) {
      logger.error(e);
    }

    try {
      part.setListPrice(Double.parseDouble(invenForm.getListPrice()));
    } catch (Exception e) {
      logger.error(e);
    }

    double discount = 0.0;
    try {
      discount = Double.parseDouble(invenForm.getDiscount());
    } catch (Exception e) {
      logger.error(e);
    }
    if (discount > 0) {
      // double costPr = 0.0;
      // costPr = part.getListPrice() * (1 - discount / 100);
      // costPr = NumberUtils.cutFractions(costPr);
      // part.setCostPrice(costPr);
      // invenForm.setCostPrice(costPr + "");
      // invenForm.setDiscount("");
      double listPr = 0.0;
      listPr = part.getCostPrice() / (1 - discount / 100);
      listPr = NumberUtils.cutFractions(listPr);
      part.setListPrice(listPr);
      invenForm.setListPrice(listPr + "");
      invenForm.setDiscount("");
    }

    try {
      part.setUnitsOnOrder(Integer.parseInt(invenForm.getUnitsOnOrder()));
    } catch (Exception e) {
      logger.error(e);
    }
    try {
      part.setUnitsInStock(0);
    } catch (Exception e) {
      logger.error(e);
    }
    try {
      part.setReorderLevel(Integer.parseInt(invenForm.getReorderLevel()));
    } catch (Exception e) {
      logger.error(e);
    }
//    try {
//      part.setCompPrice1(Double.parseDouble(invenForm.getCompPrice1()));
//    } catch (Exception e) {
//      logger.error(e);
//    }
//    try {
//      part.setCompPrice2(Double.parseDouble(invenForm.getCompPrice2()));
//    } catch (Exception e) {
//      logger.error(e);
//    }
//    try {
//      part.setCompPrice3(Double.parseDouble(invenForm.getCompPrice3()));
//    } catch (Exception e) {
//      logger.error(e);
//    }
    part.setKeystoneNumber(invenForm.getKeystoneNumber());
    part.setOemNumber(invenForm.getOemNumber());
    part.setLocation(invenForm.getLocation());
    histString +=
        "New Part: " + part.getPartNo() + "<br/>Year: " + part.getYear() + "<br/>Desc: "
            + part.getPartDescription() + "<br/>Model: " + invenForm.getMakeModelName();
    try {
      wrt.write("Insert into Parts (PartNo, Year, PartDescription, MakeModelCode) Values ('"
          + part.getPartNo() + "', '" + part.getYear() + "', '" + part.getPartDescription()
          + "', '" + part.getMakeModelCode() + "');\n");
      wrt.close();
    } catch (Exception e) {
      logger.error(e);
    }
    if (!histString.trim().equals("")) {
      String xx = histString;
      while (xx.length() > 0) {
        if (xx.length() > 240) {
          writeHistory(part.getPartNo(), xx.substring(0, 240), user.getUsername());
          xx = xx.substring(240);
        } else {
          writeHistory(part.getPartNo(), xx, user.getUsername());
          xx = "";
        }
      }
    }
  }

}
