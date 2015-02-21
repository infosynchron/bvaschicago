package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
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

import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.PartHistoryForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;

public class PartHistoryAction extends Action {
  private static final Logger logger = Logger.getLogger(PartHistoryAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((PartHistoryForm) form).getButtonClicked();
    ((PartHistoryForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in ManufacMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    logger.error(new java.util.Date(System.currentTimeMillis()) + "-----PartHistory-----"
        + user.getUsername());

    if (buttonClicked == null || buttonClicked.equals("")) {
      buttonClicked = "GetPart";
    }

    if (buttonClicked.equals("GetPart")) {
      String partNo = ((PartHistoryForm) form).getPartNo();
      if (partNo != null && !partNo.trim().equals("")) {
        PartsBean part = PartsBean.getPart(partNo, null);
        if (part == null) {
          ((PartHistoryForm) form).setPartNo("");
          errorBean.setError("THIS PART NOT FOUND IN THE SYSTEM");
        } else {
          session.removeAttribute("PartHistoryDetails1");
          session.removeAttribute("PartHistoryDetails2");
          session.removeAttribute("PartHistoryDetails3");
          session.removeAttribute("PartHistoryDetails4");
          session.removeAttribute("PartHistoryDetails5");

          fillPartDetails(part, session);
        }
      } else {
        errorBean.setError("ENTER PART NO TO GET DETAILS");
      }
      forwardPage = "PartHistory";
    } else if (buttonClicked.equals("Clear")) {
      session.removeAttribute("PartHistoryDetails1");
      session.removeAttribute("PartHistoryDetails2");
      session.removeAttribute("PartHistoryDetails3");
      session.removeAttribute("PartHistoryDetails4");
      session.removeAttribute("PartHistoryDetails5");
      ((PartHistoryForm) form).setPartNo("");
      forwardPage = "PartHistory";
    } else if (buttonClicked.equals("Back")) {
      session.removeAttribute("PartHistoryDetails1");
      session.removeAttribute("PartHistoryDetails2");
      session.removeAttribute("PartHistoryDetails3");
      session.removeAttribute("PartHistoryDetails4");
      session.removeAttribute("PartHistoryDetails5");
      ((PartHistoryForm) form).setPartNo("");
      forwardPage = "InvenMaintMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("PartHistoryDetails1");
      session.removeAttribute("PartHistoryDetails2");
      session.removeAttribute("PartHistoryDetails3");
      session.removeAttribute("PartHistoryDetails4");
      session.removeAttribute("PartHistoryDetails5");
      ((PartHistoryForm) form).setPartNo("");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("PartHistoryError", errorBean);
    } else {
      session.removeAttribute("PartHistoryError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillPartDetails(PartsBean part, HttpSession session) {
    Vector<String[]> partDetails1 = new Vector<String[]>();
    Vector<String[]> partDetails2 = new Vector<String[]>();
    Vector<String[]> partDetails3 = new Vector<String[]>();
    Vector<String[]> partDetails4 = new Vector<String[]>();
    Vector<String[]> partDetails5 = new Vector<String[]>();

    partDetails1 = addPartDetails(part, partDetails1);
    partDetails2 = addVendorDetails(part.getPartNo(), partDetails2);
    partDetails3 = addLocalDetails(part.getPartNo(), partDetails3);
    partDetails4 = addInvoiceDetails(part.getPartNo(), partDetails4);
    partDetails5 = addOrderedDetails(part.getPartNo(), partDetails5);

    if (part.getInterchangeNo() == null || part.getInterchangeNo().trim().equals("")) {
      // No Interchange No For This Part --- May Be Main Part
      Vector<PartsBean> interParts = part.getAllInterChangeParts(null);
      if (interParts != null) {
        Enumeration<PartsBean> ennum = interParts.elements();
        while (ennum.hasMoreElements()) {
          PartsBean interPart = ennum.nextElement();

          partDetails1 = addPartDetails(interPart, partDetails1);
          partDetails2 = addVendorDetails(interPart.getPartNo(), partDetails2);
          partDetails3 = addLocalDetails(interPart.getPartNo(), partDetails3);
          partDetails4 = addInvoiceDetails(interPart.getPartNo(), partDetails4);
          partDetails5 = addOrderedDetails(interPart.getPartNo(), partDetails5);
        }
      }
    } else {
      // Interchange No's Available -- Should get remaining inter parts
      partDetails1 = addPartDetails(PartsBean.getPart(part.getInterchangeNo(), null), partDetails1);
      partDetails2 = addVendorDetails(part.getInterchangeNo(), partDetails2);
      partDetails3 = addLocalDetails(part.getInterchangeNo(), partDetails3);
      partDetails4 = addInvoiceDetails(part.getInterchangeNo(), partDetails4);
      partDetails5 = addOrderedDetails(part.getInterchangeNo(), partDetails5);

      Vector<PartsBean> interParts = part.getAllInterChangeParts(null);
      if (interParts != null) {
        Enumeration<PartsBean> ennum = interParts.elements();
        while (ennum.hasMoreElements()) {
          PartsBean interPart = ennum.nextElement();

          partDetails1 = addPartDetails(interPart, partDetails1);
          partDetails2 = addVendorDetails(interPart.getPartNo(), partDetails2);
          partDetails3 = addLocalDetails(interPart.getPartNo(), partDetails3);
          partDetails4 = addInvoiceDetails(interPart.getPartNo(), partDetails4);
          partDetails5 = addOrderedDetails(interPart.getPartNo(), partDetails5);
        }
      }
    }

    if (partDetails1 != null && partDetails1.size() > 0) {
      session.setAttribute("PartHistoryDetails1", partDetails1);
    }
    if (partDetails2 != null && partDetails2.size() > 0) {
      session.setAttribute("PartHistoryDetails2", partDetails2);
    }
    if (partDetails3 != null && partDetails3.size() > 0) {
      session.setAttribute("PartHistoryDetails3", partDetails3);
    }
    if (partDetails4 != null && partDetails4.size() > 0) {
      session.setAttribute("PartHistoryDetails4", partDetails4);
    }
    if (partDetails5 != null && partDetails5.size() > 0) {
      session.setAttribute("PartHistoryDetails5", partDetails5);
    }

  }

  public Vector<String[]> addPartDetails(PartsBean part, Vector<String[]> partDetails) {

    String[] retStr = new String[7];

    retStr[0] = part.getPartNo();
    retStr[1] = part.getYear() + "  " + MakeModelBean.getMakeModelName(part.getMakeModelCode());
    retStr[2] = part.getPartDescription();
    retStr[3] = part.getUnitsInStock() + "";
    retStr[4] = part.getCostPrice() + "";
    retStr[5] = part.getActualPrice() + "";
    retStr[6] = part.getLocation() + "";

    if (retStr[6].trim().equals("")) {
      retStr[6] = "&nbsp;";
    }
    partDetails.addElement(retStr);

    return partDetails;
  }

  public Vector<String[]> addVendorDetails(String partNo, Vector<String[]> partDetails) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select a.OrderNo, a.PartNo, a.Quantity, a.Price, b.ItemDesc1, b.ItemDesc2, d.CompanyName from VendorOrderedItems a, VendorItems b, VendorOrder c, Vendors d where a.PartNo='"
              + partNo
              + "' and a.OrderNo=c.OrderNo and c.UpdatedInventory='Y' and c.SupplierId=b.SupplierId and b.partNo=a.partNo and c.supplierid=d.supplierid ORDER BY c.ORDERDATE desc");
      while (rs.next()) {

        String[] retStr = new String[6];

        int orderNo = rs.getInt(1);
        String partNum = rs.getString(2);
        int qty = rs.getInt(3);
        double price = rs.getDouble(4);
        String desc1 = rs.getString(5);
        String desc2 = rs.getString(6);
        if (desc1 == null)
          desc1 = "";
        if (desc2 == null)
          desc2 = "";
        String compName = rs.getString(7);

        retStr[0] = partNum;
        retStr[1] = desc1 + " " + desc2;
        retStr[2] = orderNo + "";
        retStr[3] = compName;
        retStr[4] = qty + "";
        retStr[5] = price + "";

        partDetails.addElement(retStr);
      }
      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt.executeQuery("Select a.OrderNo, a.PartNo, a.Quantity, a.Price, c.CompanyName from VendorOrderedItems a, VendorOrder b, Vendors c where a.PartNo='"
              + partNo
              + "' and a.vendorpartno='' and a.OrderNo=b.OrderNo and b.UpdatedInventory='Y' and b.SupplierId=c.SupplierId ");
      while (rs1.next()) {

        String[] retStr = new String[6];

        int orderNo = rs1.getInt(1);
        String partNum = rs1.getString(2);
        int qty = rs1.getInt(3);
        double price = rs1.getDouble(4);
        String compName = rs1.getString(5);

        retStr[0] = partNum;
        retStr[1] = " &nbsp; ";
        retStr[2] = orderNo + "";
        retStr[3] = compName;
        retStr[4] = qty + "";
        retStr[5] = price + "";

        partDetails.addElement(retStr);
      }
      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();
      con.close();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return partDetails;

  }

  public Vector<String[]> addLocalDetails(String partNo, Vector<String[]> partDetails) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select a.InvoiceNo, a.DateEntered, a.PartNo, a.Quantity, a.Price, b.CompanyName from LocalOrders a, LocalVendors b where a.PartNo='"
              + partNo + "' and a.SupplierId=b.SupplierId ORDER BY a.DateEntered desc");
      while (rs.next()) {

        String[] retStr = new String[6];

        int invNo = rs.getInt(1);
        String dateEntered = DateUtils.convertMySQLToUSFormat(rs.getString(2));
        String partNum = rs.getString(3);
        int qty = rs.getInt(4);
        double price = rs.getDouble(5);
        String compName = rs.getString(6);

        retStr[0] = partNum;
        retStr[1] = invNo + "";
        retStr[2] = dateEntered;
        retStr[3] = compName;
        retStr[4] = qty + "";
        retStr[5] = price + "";

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

  public Vector<String[]> addInvoiceDetails(String partNo, Vector<String[]> partDetails) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select id.PartNumber, id.InvoiceNumber, id.Quantity, id.SoldPrice, id.ActualPrice From InvoiceDetails id, Invoice i Where id.PartNumber='"
              + partNo + "' AND id.InvoiceNumber = i.InvoiceNumber order by i.orderdate desc");
      int totQty = 0;
      while (rs.next()) {

        String[] retStr = new String[5];

        String partNum = rs.getString(1);
        int invNo = rs.getInt(2);
        int qty = rs.getInt(3);
        double soldPrice = rs.getDouble(4);
        double actPrice = rs.getDouble(5);

        totQty += qty;
        retStr[0] = partNum;
        retStr[1] = invNo + "";
        retStr[2] = qty + "";
        retStr[3] = soldPrice + "";
        retStr[4] = actPrice + "";

        partDetails.addElement(retStr);
      }

      if (totQty != 0) {
        String[] retStr = new String[5];
        retStr[0] = "";
        retStr[1] = "";
        retStr[2] = totQty + "";
        retStr[3] = "";
        retStr[4] = "";

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

  public Vector<String[]> addOrderedDetails(String partNo, Vector<String[]> partDetails) {

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("Select a.OrderNo, a.PartNo, a.Quantity, a.Price, c.CompanyName from VendorOrderedItems a, VendorOrder b, Vendors c where a.PartNo='"
              + partNo
              + "' and a.OrderNo=b.OrderNo and b.UpdatedInventory='N' and b.IsFinal='Y' and b.SupplierId=c.SupplierId ORDER BY b.ORDERDATE desc");
      while (rs.next()) {

        String[] retStr = new String[6];

        int orderNo = rs.getInt(1);
        String partNum = rs.getString(2);
        int qty = rs.getInt(3);
        double price = rs.getDouble(4);
        String compName = rs.getString(5);

        retStr[0] = partNum;
        retStr[1] = orderNo + "";
        retStr[2] = compName;
        retStr[3] = qty + "";
        retStr[4] = price + "";

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
