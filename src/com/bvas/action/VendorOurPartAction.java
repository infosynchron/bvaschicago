package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.bvas.beans.PartsBean;
import com.bvas.beans.VendorItemBean;
import com.bvas.beans.VendorOrderBean;
import com.bvas.formBeans.OrderDetailsForm;
import com.bvas.formBeans.OurPartDetailsForm;
import com.bvas.formBeans.VendorOurPartForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.NumberUtils;

public class VendorOurPartAction extends Action {
  private static final Logger logger = Logger.getLogger(VendorOurPartAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    VendorOurPartForm opForm = (VendorOurPartForm) form;
    String buttonClicked = opForm.getButtonClicked();
    opForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorOurPartAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int tempNo = 0;
    try {
      tempNo = Integer.parseInt(opForm.getTempNo());
    } catch (Exception e) {
      logger.error(e);
      tempNo = 0;
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetOrGetSaved")) {
      Vector<OurPartDetailsForm> v = null;
      if (tempNo == 0) {
        v = PartsBean.getAllParts();
        tempNo = getMaxTempNo();
        opForm.setTempNo(tempNo + "");
        opForm.setTotalItems(v.size() + "");
        opForm.setNoOrdering(0 + "");
        session.setAttribute("AllOurPartDetails", v);
      } else {
        v = getFromSaved(tempNo, opForm);
        session.setAttribute("AllOurPartDetails", v);
      }
      forwardPage = "VendorOurPart";
    } else if (buttonClicked.equals("SaveToDB")) {
      Vector<OurPartDetailsForm> v = null;
      v = saveAndCopy(request, tempNo, opForm);
      session.setAttribute("AllOurPartDetails", v);
      forwardPage = "VendorOurPart";
    } else if (buttonClicked.equals("GoToCreateNewOrder")) {
      int orderNo = VendorOrderBean.getMaxOrderNo();
      session.setAttribute("OrderNumber", orderNo + "");
      logger.error("Order Num in Vendor Our Part Action is: " + orderNo);
      session.setAttribute("SupplierIdForOrder", opForm.getSupplierId());
      session.setAttribute("CompanyNameForOrder", opForm.getSupplierName());
      if (opForm.getSupplierId() != null && !opForm.getSupplierId().trim().equals("")) {
        Vector<OrderDetailsForm> v = getOrderDetails(request, tempNo, opForm, orderNo);

        session.setAttribute("OrderDetails", v);
      } else {
        logger.error("Supplier ID is NULL");
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("ClearAll")) {
      session.removeAttribute("AllOurPartDetails");
      opForm.setTempNo("");
      opForm.setNoOrdering("");
      opForm.setTotalItems("");
      forwardPage = "VendorOurPart";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("AllOurPartDetails");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorOurPartError", errorBean);
    } else {
      session.removeAttribute("VendorOurPartError");
    }

    return (mapping.findForward(forwardPage));

  }

  public int getMaxTempNo() {
    int newTempNo = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select max(TemoNo) from TempParts ");
      if (rs.next()) {
        newTempNo = rs.getInt(1) + 1;
      } else {
        logger.error("In Vendor Our Part Action - No Temp Number");
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      logger.error("In InvoiceBean - Unable to get New Temp Number - " + e);
    }
    return newTempNo;
  }

  public Vector<OurPartDetailsForm> saveAndCopy(HttpServletRequest request, int tempNo,
      VendorOurPartForm opForm) {
    Vector<OurPartDetailsForm> ourParts = new Vector<OurPartDetailsForm>();
    Connection con = null;
    PreparedStatement pstmt = null;
    Statement stmt = null;
    try {
      con = DBInterfaceLocal.getSQLConnection();
      stmt = con.createStatement();
      stmt.execute("DELETE FROM TempParts WHERE TemoNo=" + tempNo);
      String sql =
          "INSERT INTO TempParts (TemoNo, PartNo, ManufacName, MakeModelName, Description, Year, ListPrice, UnitsInStock, NoOrdering) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

      pstmt = DBInterfaceLocal.getSQLConnection().prepareStatement(sql);

    } catch (Exception e) {
      logger.error(e);
      logger.error("Exception in VendorOurPartAction --- " + e);
    }

    int noOrdered = 0;
    int partCount = 0;
    boolean partsAvail = false;
    while (true) {
      partCount++;
      String partNo = (String) request.getParameter("PartNo" + partCount);
      String manufacName = (String) request.getParameter("ManufacName" + partCount);
      String makeModelName = (String) request.getParameter("MakeModelName" + partCount);
      String desc = (String) request.getParameter("Description" + partCount);
      String year = (String) request.getParameter("Year" + partCount);
      String listStr = (String) request.getParameter("ListPrice" + partCount);
      String unitsStr = (String) request.getParameter("UnitsInStock" + partCount);
      String qtyStr = (String) request.getParameter("QtyToOrder" + partCount);

      if (partNo == null || partNo.trim().equals("")) {
        break;
      }

      double list = 0;
      try {
        list = Double.parseDouble(listStr);
      } catch (Exception e) {
        logger.error(e);
        list = 0.0;
      }

      int units = 0;
      try {
        units = Integer.parseInt(unitsStr);
      } catch (Exception e) {
        logger.error(e);
        units = 0;
      }

      int qty = 0;
      try {
        qty = Integer.parseInt(qtyStr);
      } catch (Exception e) {
        logger.error(e);
        qty = 0;
      }

      if (qty != 0) {
        noOrdered++;
      }

      OurPartDetailsForm ourPart = new OurPartDetailsForm();

      ourPart.partNo = partNo;
      ourPart.manufacName = manufacName;
      ourPart.makeModelName = makeModelName;
      ourPart.description = desc;
      ourPart.year = year;
      ourPart.listPrice = list;
      ourPart.unitsInStock = units;
      ourPart.qtyToOrder = qty;

      try {

        pstmt.clearParameters();
        pstmt.setInt(1, tempNo);
        pstmt.setString(2, partNo);
        pstmt.setString(3, manufacName);
        pstmt.setString(4, makeModelName);
        pstmt.setString(5, desc);
        pstmt.setString(6, year);
        pstmt.setDouble(7, list);
        pstmt.setInt(8, units);
        pstmt.setInt(9, qty);

        pstmt.execute();

      } catch (Exception e) {
        logger.error("Exception in VendorOurPartAction --- " + e);
      }

      ourParts.add(ourPart);

    }
    try {
      pstmt.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);

    }
    opForm.setNoOrdering(noOrdered + "");
    opForm.setTotalItems(ourParts.size() + "");
    return ourParts;
  }

  public Vector<OurPartDetailsForm> getFromSaved(int tempNo, VendorOurPartForm opForm) {
    Vector<OurPartDetailsForm> ourParts = new Vector<OurPartDetailsForm>();

    Connection con = DBInterfaceLocal.getSQLConnection();
    int noOrdered = 0;

    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT PartNo, ManufacName, MakeModelName, Description, Year, ListPrice, UnitsInStock, NoOrdering FROM TempParts WHERE TemoNo="
              + tempNo + " ORDER BY 2 ASC, 3 ASC, 5 ASC, 4 ASC");
      while (rs.next()) {

        OurPartDetailsForm ourPart = new OurPartDetailsForm();

        ourPart.partNo = rs.getString("PartNo");
        ourPart.manufacName = rs.getString("ManufacName");
        ourPart.makeModelName = rs.getString("MakeModelName");
        ourPart.description = rs.getString("Description");
        ourPart.year = rs.getString("Year");
        ourPart.listPrice = rs.getDouble("ListPrice");
        ourPart.unitsInStock = rs.getInt("UnitsInStock");
        ourPart.qtyToOrder = rs.getInt("NoOrdering");

        if (ourPart.qtyToOrder != 0) {
          noOrdered++;
        }
        ourParts.add(ourPart);

      }
      rs.close();
      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error("Not able to get the parts in Vendor Our Part Action");
    }

    opForm.setTempNo(tempNo + "");
    opForm.setNoOrdering(noOrdered + "");
    opForm.setTotalItems(ourParts.size() + "");
    return ourParts;
  }

  public Vector<OrderDetailsForm> getOrderDetails(HttpServletRequest request, int tempNo,
      VendorOurPartForm opForm, int orderNo) {
    Vector<OrderDetailsForm> orderDetails = new Vector<OrderDetailsForm>();

    try {
      Statement stmt = DBInterfaceLocal.getSQLConnection().createStatement();
      // stmt.execute("DELETE FROM TempParts WHERE TemoNo=" + tempNo);

    } catch (Exception e) {
      logger.error("Exception in VendorOurPartAction --- " + e);
    }

    int noOrdered = 0;
    int partCount = 0;
    boolean partsAvail = false;
    while (true) {
      partCount++;
      String partNo = (String) request.getParameter("PartNo" + partCount);
      String manufacName = (String) request.getParameter("ManufacName" + partCount);
      String makeModelName = (String) request.getParameter("MakeModelName" + partCount);
      String desc = (String) request.getParameter("Description" + partCount);
      String year = (String) request.getParameter("Year" + partCount);
      String listStr = (String) request.getParameter("ListPrice" + partCount);
      String unitsStr = (String) request.getParameter("UnitsInStock" + partCount);
      String qtyStr = (String) request.getParameter("QtyToOrder" + partCount);

      if (partNo == null || partNo.trim().equals("")) {
        break;
      }

      double list = 0;
      try {
        list = Double.parseDouble(listStr);
      } catch (Exception e) {
        logger.error(e);
        list = 0.0;
      }

      int units = 0;
      try {
        units = Integer.parseInt(unitsStr);
      } catch (Exception e) {
        logger.error(e);
        units = 0;
      }

      int qty = 0;
      try {
        qty = Integer.parseInt(qtyStr);
      } catch (Exception e) {
        logger.error(e);
        qty = 0;
      }

      if (qty != 0) {
        noOrdered++;
      } else {
        continue;
      }

      OrderDetailsForm orderPart = new OrderDetailsForm();
      VendorItemBean viBean = null;
      try {
        int supplId = Integer.parseInt(opForm.getSupplierId());
        viBean = VendorItemBean.getThePart(supplId, partNo, "", null);
      } catch (Exception e) {
        logger.error(e);
      }

      orderPart.partNo = partNo;
      if (viBean == null) {
        orderPart.vendorPartNo = "";
        orderPart.partDescription1 = makeModelName;
        orderPart.partDescription2 = desc;
        // orderPart.oemNumber = "";
        orderPart.sellingRate = 0.0;
        orderPart.noOfPieces = 0;
        orderPart.quantity = qty;
        orderPart.priceForUs = 0.0;
      } else {
        orderPart.vendorPartNo = viBean.getVendorPartNo();
        orderPart.partDescription1 = viBean.getItemDesc1();
        orderPart.partDescription2 = viBean.getItemDesc2();
        // orderPart.oemNumber = viBean.getOemNo();
        orderPart.sellingRate = viBean.getSellingRate();
        orderPart.noOfPieces = viBean.getNoOfPieces();
        if (qty >= orderPart.noOfPieces) {
          orderPart.quantity = qty;
        } else {
          orderPart.quantity = orderPart.noOfPieces;
        }
        orderPart.priceForUs = orderPart.sellingRate * orderPart.quantity;
        if (orderPart.priceForUs != 0) {
          orderPart.priceForUs =
              Double.parseDouble(NumberUtils.cutFractions(orderPart.priceForUs + ""));
        }
      }

      orderDetails.add(orderPart);

    }

    return orderDetails;
  }

}
