package com.bvas.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.UserBean;
import com.bvas.formBeans.MainMenuForm;
import com.bvas.utils.PrintUtils;

public class MainMenuAction extends Action {
  private static final Logger logger = Logger.getLogger(MainMenuAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    // servlet.log("Error message text", exception);
    String buttonClicked = ((MainMenuForm) form).getButtonClicked();
    ((MainMenuForm) form).setButtonClicked("");
    String forwardPage = "";

    HttpSession session = request.getSession(false);

    UserBean user = (UserBean) session.getAttribute("User");
    if (session == null || user == null) {
      logger.error("No session or no User in MainMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    // logger.error(new java.util.Date(System.currentTimeMillis())
    // + "-----MainMenu-----" + user.getUsername());

    // logger.error("ButtonClicked-------" + buttonClicked);
    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("CreateInvoice")) {
      forwardPage = "InvoiceGen";
    } else if (buttonClicked.equals("InvoiceArchives")) {
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("Reports")) {

      forwardPage = "TodaysOrders";
    } else if (buttonClicked.trim().equals("InventoryAvailability")) {
      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("MaintainInventoryMenu")) {
      forwardPage = "InvenMaintMenu";
    } else if (buttonClicked.equals("AcctMenu")) {
      forwardPage = "AcctMenu";
    } else if (buttonClicked.equals("RoutingMenu")) {
      forwardPage = "RoutingMenu";
    } else if (buttonClicked.equals("LookupClient")) {
      File file = new File("c:/Tomcat/webapps/bvaschicago/html/reports/ClientListing.html");
      long lastModified = file.lastModified();
      long currentTime = System.currentTimeMillis();
      long diff1 = currentTime - lastModified;
      logger.error(diff1);
      long diff2 = 86400000 * 5;
      if (lastModified == 0 || diff1 > diff2) {
        PrintUtils.createClientListing();
      }
      forwardPage = "ClientLookup";
    } else if (buttonClicked.equals("MaintainClients")) {
      File file = new File("c:/Tomcat/webapps/bvaschicago/html/reports/ClientListing.html");
      long lastModified = file.lastModified();
      long currentTime = System.currentTimeMillis();
      long diff1 = currentTime - lastModified;
      logger.error(diff1);
      long diff2 = 86400000 * 5;
      if (lastModified == 0 || diff1 > diff2) {
        PrintUtils.createClientListing();
      }
      forwardPage = "ClientMaint";
    } else if (buttonClicked.equals("MaintainLocalVendors")) {
      forwardPage = "LocalVendorMenu";
    } else if (buttonClicked.equals("MaintainVendors")) {
      forwardPage = "VendorMenu";
    } else if (buttonClicked.equals("MaintainOther")) {
      forwardPage = "OtherMaint";
    } else if (buttonClicked.equals("CreateFax")) {
      forwardPage = "FaxCover";
    } else if (buttonClicked.equals("LogOut")) {
      // session.removeAttribute("User");

      session.invalidate();
      forwardPage = "Login";
    } else {
      forwardPage = "Login";
    }

    /*
     * if (session != null && errorBean.getError() != null) { session.setAttribute("MainMenuError",
     * errorBean); } else { session.removeAttribute("MainMenuError"); }
     */

    return (mapping.findForward(forwardPage));
  }
}
