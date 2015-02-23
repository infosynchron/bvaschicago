package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.bvas.beans.VendorBean;
import com.bvas.beans.VendorItemBean;
import com.bvas.beans.VendorOrderBean;
import com.bvas.beans.VendorOrderedItemsBean;
import com.bvas.formBeans.OrderDetailsForm;
import com.bvas.formBeans.VendorOrderForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.NumberUtils;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.UserException;

public class VendorOrderAction extends Action {
  private static final Logger logger = Logger.getLogger(VendorOrderAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    VendorOrderForm ordForm = (VendorOrderForm) form;
    String buttonClicked = ordForm.getButtonClicked();
    ordForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorOrderAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int orderNo = 0;
    int orderNoFromSession = 0;
    try {
      orderNo = Integer.parseInt(ordForm.getOrderNo());
    } catch (Exception e) {
      logger.error(e);
      orderNo = 0;
    }
    try {
      orderNoFromSession = Integer.parseInt((String) session.getAttribute("OrderNumber"));
    } catch (Exception e) {
      logger.error(e);
      orderNoFromSession = 0;
    }

    String forAllCust = (String) session.getAttribute("ForAllCustomers");
    if (forAllCust != null && forAllCust.trim().equals("YES")) {
      session.setAttribute("ForAllCustomers", "NO");
    } else if (orderNoFromSession != 0 && buttonClicked != null && buttonClicked.equals("Get")
        && forAllCust != null && forAllCust.trim().equals("NO")) {
      buttonClicked = "GetOrUpdate";
    }

    int supplierId = 0;
    try {
      supplierId = Integer.parseInt((String) session.getAttribute("SupplierIdForOrder"));
    } catch (Exception e) {
      logger.error(e);
    }
    String companyName = (String) session.getAttribute("CompanyNameForOrder");

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("Get")) {
      long st = System.currentTimeMillis();
      if (VendorOrderBean.isAvailable(orderNo)) {
        try {
          VendorOrderBean orderBean = VendorOrderBean.getOrder(orderNo);
          session.setAttribute("OrderNumber", orderNo + "");
          ordForm.setOrderNo(orderNo + "");
          ordForm.setSupplierId(orderBean.getSupplierId() + "");
          ordForm.setSupplierName(VendorBean.getVendor(orderBean.getSupplierId()).getCompanyName());
          session.setAttribute("SupplierIdForOrder", ordForm.getSupplierId());
          session.setAttribute("CompanyNameForOrder", ordForm.getSupplierName());
          ordForm.setOrderDate(orderBean.getOrderDate());

          Vector<OrderDetailsForm> orderDetails = createOrderDetails(supplierId, orderBean);

          ordForm.setTotalItems(orderBean.getOrderedItems().size() + "");
          ordForm.setOrderTotal(orderBean.getOrderTotal() + "");
          session.setAttribute("OrderDetails", orderDetails);
          session.setAttribute("ForAllCustomers", "NO");
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
        }
      } else if (orderNoFromSession == 0) {
        int maxOrdNo = VendorOrderBean.getMaxOrderNo();
        session.setAttribute("OrderNumber", maxOrdNo + "");
        ordForm.setOrderNo(maxOrdNo + "");
        session.setAttribute("ForAllCustomers", "YES");
        errorBean
            .setError("BE CAREFUL: DO NOT PRESS THE TOP 'GET' BUTTON,<BR/> IF YOU WANT TO ENTER THE ORDER USING ONLY OUR PART NOS.");
      } else {
        try {
          Vector<VendorItemBean> allParts = VendorItemBean.getAllParts(supplierId);
          ordForm.setSupplierId(supplierId + "");
          ordForm.setSupplierName(companyName);
          ordForm.setOrderDate(DateUtils.getNewUSDate());

          Vector<OrderDetailsForm> orderDetails = createOrderDetails(allParts);

          ordForm.setTotalItems(orderDetails.size() + "");
          ordForm.setOrderTotal(findTotalPrice(orderDetails) + "");
          session.setAttribute("OrderDetails", orderDetails);
          session.setAttribute("ForAllCustomers", "NO");
        } catch (UserException e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
        }
      }
      logger.error("Order" + (System.currentTimeMillis() - st));
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("GetNeeded")) {
      long st = System.currentTimeMillis();
      try {
        Vector<VendorItemBean> allParts = VendorItemBean.getMatchedParts(supplierId);
        ordForm.setSupplierId(supplierId + "");
        ordForm.setSupplierName(companyName);
        ordForm.setOrderDate(DateUtils.getNewUSDate());

        Vector<OrderDetailsForm> orderDetails = filterOrderDetails(allParts);
        ordForm.setTotalItems(orderDetails.size() + "");
        ordForm.setOrderTotal(findTotalPrice(orderDetails) + "");
        session.setAttribute("OrderDetails", orderDetails);
        session.setAttribute("ForAllCustomers", "NO");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      logger.error("Order Needed Parts : " + (System.currentTimeMillis() - st));
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("GetMatched")) {
      try {
        Vector<VendorItemBean> allParts = VendorItemBean.getMatchedParts(supplierId);
        ordForm.setSupplierId(supplierId + "");
        ordForm.setSupplierName(companyName);
        ordForm.setOrderDate(DateUtils.getNewUSDate());

        Vector<OrderDetailsForm> orderDetails = createOrderDetails(allParts);
        ordForm.setTotalItems(orderDetails.size() + "");
        ordForm.setOrderTotal(findTotalPrice(orderDetails) + "");
        session.setAttribute("OrderDetails", orderDetails);
        session.setAttribute("ForAllCustomers", "NO");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("GetUnMatched")) {
      try {
        Vector<VendorItemBean> allParts = VendorItemBean.getUnMatchedParts(supplierId);
        ordForm.setSupplierId(supplierId + "");
        ordForm.setSupplierName(companyName);
        ordForm.setOrderDate(DateUtils.getNewUSDate());

        Vector<OrderDetailsForm> orderDetails = createOrderDetails(allParts);
        ordForm.setTotalItems(orderDetails.size() + "");
        ordForm.setOrderTotal(findTotalPrice(orderDetails) + "");
        session.setAttribute("OrderDetails", orderDetails);
        session.setAttribute("ForAllCustomers", "NO");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("GetOrUpdate")) {
      session.removeAttribute("OrderDetails");
      getOrUpdate(supplierId, ordForm, request, errorBean);
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("UpdatePrices")) {
      getOrUpdate(supplierId, ordForm, request, errorBean);
      Object oo = null;
      oo = session.getAttribute("OrderDetails");
      if (oo != null) {
        Vector v = (Vector) oo;
        updatePrices(v, supplierId);
      } else {
        errorBean.setError("Prices not updated... Please check your order... ");
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("CreateNewOrder")) {
      try {
        VendorOrderBean orderBean =
            createOrderBean("Create", orderNo, supplierId, ordForm, request);
        orderBean.createOrder();
        session.setAttribute("OrderNumber", orderBean.getOrderNo() + "");
        PrintUtils.printVendorOrder(orderBean);
        errorBean.setError("NEW ORDER CREATED SUCCESSFULLY");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("ChangeOrder")) {
      try {
        VendorOrderBean orderBean =
            createOrderBean("Change", orderNo, supplierId, ordForm, request);
        orderBean.changeOrder();
        session.setAttribute("OrderNumber", orderBean.getOrderNo() + "");
        PrintUtils.printVendorOrder(orderBean);
        errorBean.setError("ORDER CHANGED SUCCESSFULLY");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("ChangeOrderWO")) {
      try {
        VendorOrderBean orderBean =
            createOrderBean("Change", orderNo, supplierId, ordForm, request);
        orderBean.changeOrder();
        session.setAttribute("OrderNumber", orderBean.getOrderNo() + "");
        // PrintUtils.printVendorOrder(orderBean);
        errorBean.setError("ORDER CHANGED SUCCESSFULLY");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("ClearOrder")) {
      session.removeAttribute("OrderNumber");
      session.removeAttribute("OrderDetails");
      session.removeAttribute("ForAllCustomers");
      ordForm.setOrderNo("");
      ordForm.setSupplierId(supplierId + "");
      ordForm.setSupplierName(companyName);
      ordForm.setOrderDate("");
      ordForm.setOrderTotal("");
      ordForm.setTotalItems("");
      System.gc();
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("PrintOrder")) {
      forwardPage = "VendorOrder";
    } else if (buttonClicked.equals("DoFinalSteps")) {
      try {
        VendorOrderBean order = VendorOrderBean.getOrder(orderNo);
        session.setAttribute("VendorOrderExtras", VendorOrderBean.getExtras(orderNo));
        session.setAttribute("OrderDetails", order.getOrderedItems());
        forwardPage = "VendorFinal";
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError("This Order Not Exists in the System<BR/>" + e.getMessage());
        forwardPage = "VendorOrder";
      }
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("SupplierIdForOrder");
      session.removeAttribute("CompanyNameForOrder");
      session.removeAttribute("ForAllCustomers");
      session.removeAttribute("OrderNumber");
      session.removeAttribute("OrderDetails");
      System.gc();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorOrderError", errorBean);
    } else {
      session.removeAttribute("VendorOrderError");
    }

    return (mapping.findForward(forwardPage));

  }

  public VendorOrderBean createOrderBean(String fld, int orderNo, int supId, VendorOrderForm form,
      HttpServletRequest request) throws UserException {
    HttpSession session = request.getSession(false);
    VendorOrderBean voBean = new VendorOrderBean();
    VendorOrderBean oldVOBean = null;
    if (fld.trim().equals("Create")) {
      int ordNo = VendorOrderBean.getMaxOrderNo();
      voBean.setOrderNo(ordNo);
      voBean.setOrderStatus("New");
      voBean.setDeliveredDate("00-00-0000");
    } else {
      oldVOBean = VendorOrderBean.getOrder(orderNo);
      voBean.setOrderNo(orderNo);
      voBean.setOrderStatus("Changed");
      // voBean.setDeliveredDate(DateUtils.getNewUSDate());
      voBean.setDeliveredDate(oldVOBean.getDeliveredDate());
      voBean.setUpdatedInventory(oldVOBean.getUpdatedInventory());
      voBean.setUpdatedPrices(oldVOBean.getUpdatedPrices());
      voBean.setContainerNo(oldVOBean.getContainerNo());
      voBean.setSupplInvNo(oldVOBean.getSupplInvNo());
      voBean.setArrivedDate(oldVOBean.getArrivedDate());
      voBean.setIsFinal(oldVOBean.getIsFinal());
      voBean.setDiscount(oldVOBean.getDiscount());
      voBean.setStickerCharges(oldVOBean.getStickerCharges());
      voBean.setOverheadAmountsTotal(oldVOBean.getOverheadAmountsTotal());
      voBean.setUnitsOrderDoneDate(oldVOBean.getUnitsOrderDoneDate());
      voBean.setPricesDoneDate(oldVOBean.getPricesDoneDate());
      voBean.setInventoryDoneDate(oldVOBean.getInventoryDoneDate());
      voBean.setPaymentTerms(oldVOBean.getPaymentTerms());
      voBean.setPaymentDate(oldVOBean.getPaymentDate());
      voBean.setEstimatedArrivalDate(oldVOBean.getEstimatedArrivalDate());
    }
    voBean.setOrderDate(form.getOrderDate());
    voBean.setSupplierId(supId);
    Vector<OrderDetailsForm> orderDetails = null;
    Object o = session.getAttribute("OrderDetails");
    if (o != null) {
      orderDetails = (Vector<OrderDetailsForm>) o;
    } else {
      throw new UserException("No Items to create in this order");
    }
    double orderTotal = findTotalPrice(orderDetails);
    voBean.setOrderTotal(orderTotal);

    Enumeration<OrderDetailsForm> ennum = orderDetails.elements();
    Vector<VendorOrderedItemsBean> orderDetBean = new Vector<VendorOrderedItemsBean>();
    while (ennum.hasMoreElements()) {
      OrderDetailsForm ordForm = ennum.nextElement();
      VendorOrderedItemsBean voiBean = new VendorOrderedItemsBean();
      voiBean.setOrderNo(voBean.getOrderNo());
      voiBean.setPartNo(ordForm.partNo);
      voiBean.setVendorPartNo(ordForm.vendorPartNo);
      voiBean.setQuantity(ordForm.quantity);
      voiBean.setPrice(ordForm.sellingRate);
      if (ordForm.quantity != 0) {
        orderDetBean.add(voiBean);
      }
    }

    voBean.setTotalItems(orderDetBean.size());
    voBean.setOrderedItems(orderDetBean);

    return voBean;
  }

  public Vector<OrderDetailsForm> createOrderDetails(Vector<VendorItemBean> orderDetFromDB) {
    Vector<OrderDetailsForm> orderDetails = new Vector<OrderDetailsForm>();
    Enumeration<VendorItemBean> ennum = orderDetFromDB.elements();
    while (ennum.hasMoreElements()) {
      VendorItemBean viBean = ennum.nextElement();
      OrderDetailsForm form = new OrderDetailsForm();
      form.partNo = viBean.getPartNo();
      form.vendorPartNo = viBean.getVendorPartNo();
      form.partDescription1 = viBean.getItemDesc1();
      form.partDescription2 = viBean.getItemDesc2();
      if (viBean.getPartNo() != null && !viBean.getPartNo().trim().equals("")) {
        PartsBean part = PartsBean.getPart(viBean.getPartNo(), null);
        if (part != null) {
          form.unitsInStock =
              part.getUnitsInStock() + " / " + part.getUnitsOnOrder() + " / "
                  + part.getReorderLevel();
        }
      } else {
        form.unitsInStock = "";
      }
      form.sellingRate = viBean.getSellingRate();
      form.noOfPieces = viBean.getNoOfPieces();
      form.quantity = 0;
      form.priceForUs = form.sellingRate * form.quantity;
      if (form.priceForUs != 0) {
        if (form.priceForUs >= 1000) {
          form.priceForUs = Double.parseDouble(NumberUtils.cutFractions(form.priceForUs + ""));
        } else {
          form.priceForUs = NumberUtils.cutFractions(form.priceForUs);
        }
      }

      orderDetails.add(form);
    }
    return orderDetails;
  }

  public Vector<OrderDetailsForm> filterOrderDetails(Vector<VendorItemBean> orderDetFromDB) {
    Connection con1 = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    Vector<OrderDetailsForm> orderDetails = new Vector<OrderDetailsForm>();
    Enumeration<VendorItemBean> ennum = orderDetFromDB.elements();
    while (ennum.hasMoreElements()) {
      VendorItemBean viBean = ennum.nextElement();
      OrderDetailsForm form = new OrderDetailsForm();
      form.partNo = viBean.getPartNo();
      form.vendorPartNo = viBean.getVendorPartNo();
      form.partDescription1 = viBean.getItemDesc1();
      form.partDescription2 = viBean.getItemDesc2();
      if (viBean.getPartNo() != null && !viBean.getPartNo().trim().equals("")) {
        try {
          stmt = con1.createStatement();
          rs =
              stmt.executeQuery("Select InterChangeNo, UnitsInStock, UnitsOnOrder, ReOrderLevel From Parts Where PartNo='"
                  + viBean.getPartNo() + "'");
          if (rs.next()) {
            String interNo = rs.getString(1);
            int units = rs.getInt(2);
            int ordered = rs.getInt(3);
            int reorder = rs.getInt(4);
            if (interNo == null) {
              interNo = "";
            }/*
              * else if (!interNo.trim().equals("")) { logger.error(viBean.getPartNo()); }
              */
            if (!interNo.trim().equals("")) {
              stmt1 = con1.createStatement();
              rs1 =
                  stmt1.executeQuery("Select UnitsInStock from Parts Where PartNo='" + interNo
                      + "'");
              if (rs1.next()) {
                units = rs1.getInt(1);
              }
              rs1.close();
              stmt1.close();
            }
            if (units == 2 && reorder < 2) {
              continue;
            } else if (units > 2 && units >= reorder) {
              continue;
            }
            form.unitsInStock = units + " / " + ordered + " / " + reorder;
          }

          rs.close();
          stmt.close();

        } catch (SQLException e) {
          logger.error(e);
        }
        /*
         * PartsBean part = PartsBean.getPart(viBean.getPartNo()); if (part != null) {
         * form.unitsInStock = part.getUnitsInStock() + " / " + part.getUnitsOnOrder() + " / " +
         * part.getReorderLevel(); int units = part.getUnitsInStock(); int ordered =
         * part.getUnitsOnOrder(); int reorder = part.getReorderLevel(); if (units == 2 && reorder <
         * 2) { continue; } else if (units > 2 && units >= reorder) { continue; } }
         */
      } else {
        form.unitsInStock = "";
      }
      form.sellingRate = viBean.getSellingRate();
      form.noOfPieces = viBean.getNoOfPieces();
      if (viBean.getNoOfPieces() == 0) {
        form.quantity = 1;
      } else {
        form.quantity = viBean.getNoOfPieces();
      }
      form.priceForUs = form.sellingRate * form.quantity;
      if (form.priceForUs != 0) {
        if (form.priceForUs >= 1000) {
          form.priceForUs = Double.parseDouble(NumberUtils.cutFractions(form.priceForUs + ""));
        } else {
          form.priceForUs = NumberUtils.cutFractions(form.priceForUs);
        }
      }

      orderDetails.add(form);
    }
    try {
      con1.close();
    } catch (SQLException e) {
      logger.error(e);
    }
    return orderDetails;
  }

  public Vector<OrderDetailsForm> createOrderDetails(int supId, VendorOrderBean orderBean)
      throws UserException {
    try {
      Vector<OrderDetailsForm> orderDetails = new Vector<OrderDetailsForm>();
      Vector<VendorOrderedItemsBean> orderedItems = orderBean.getOrderedItems();
      Enumeration<VendorOrderedItemsBean> ennum = orderedItems.elements();
      VendorItemBean viBean = null;
      PartsBean partBean = null;
      Connection con = DBInterfaceLocal.getSQLConnection();
      while (ennum.hasMoreElements()) {
        VendorOrderedItemsBean voiBean = ennum.nextElement();
        OrderDetailsForm form = new OrderDetailsForm();
        String partNo = voiBean.getPartNo();
        String vendorPartNo = voiBean.getVendorPartNo();
        int qty = voiBean.getQuantity();
        double price = voiBean.getPrice();
        if (vendorPartNo != null && !vendorPartNo.equals("")) {
          viBean = VendorItemBean.getVendorPart(supId, partNo, vendorPartNo);
          if (viBean == null) {
            viBean = VendorItemBean.getThePart(supId, "", vendorPartNo, con);
            if (viBean == null) {
              throw new UserException("Item is null: " + vendorPartNo);
            }
          }
          form.partNo = viBean.getPartNo();
          form.vendorPartNo = viBean.getVendorPartNo();
          form.partDescription1 = viBean.getItemDesc1();
          form.partDescription2 = viBean.getItemDesc2();
          // form.oemNumber = viBean.getOemNo();
          if (viBean.getPartNo() != null && !viBean.getPartNo().trim().equals("")) {
            int units = 0;
            int reorder = 0;
            int unitsonorder = 0;
            PartsBean part = PartsBean.getPart(viBean.getPartNo(), con);
            if (part != null) {
              form.unitsInStock =
                  part.getUnitsInStock() + " / " + part.getUnitsOnOrder() + " / "
                      + part.getReorderLevel();
              /*
               * int committed = 0; committed = getCommitted(part.getPartNo(),
               * part.getInterchangeNo()); if (committed!=0) { form.unitsInStock =
               * part.getUnitsInStock() + " / " + part.getUnitsOnOrder() + "-" + committed + " / " +
               * part.getReorderLevel(); } else { form.unitsInStock = part.getUnitsInStock() + " / "
               * + part.getUnitsOnOrder() + " / " + part.getReorderLevel(); }
               */
              part = null;
            } else {
              form.unitsInStock = "";
            }
            /*
             * try { Statement stmt = con.createStatement(); ResultSet rs =
             * stmt.executeQuery("Select * from parts where Partno='" + viBean.getPartNo().trim() +
             * "'"); if (rs.next()) { units = rs.getInt("UnitsInStock"); reorder =
             * rs.getInt("ReorderLevel"); unitsonorder = rs.getInt("UnitsOnOrder"); if (reorder ==
             * 0) { PartsBean part = PartsBean.getPart(viBean.getPartNo()); if (part != null) {
             * form.unitsInStock = part.getUnitsInStock() + " / " + part.getUnitsOnOrder() + " / " +
             * part.getReorderLevel(); part = null; } } else { form.unitsInStock = units + " / " +
             * unitsonorder + " / " + reorder; } } else { form.unitsInStock = ""; } } catch
             * (Exception e) { form.unitsInStock = ""; }
             */
          } else {
            form.unitsInStock = "";
          }
          if (price == 0) {
            form.sellingRate = viBean.getSellingRate();
          } else {
            form.sellingRate = price;
          }
          form.noOfPieces = viBean.getNoOfPieces();
          form.quantity = qty;
          form.priceForUs = form.sellingRate * form.quantity;
          if (form.priceForUs != 0) {
            if (form.priceForUs >= 1000) {
              form.priceForUs = Double.parseDouble(NumberUtils.cutFractions(form.priceForUs + ""));
            } else {
              form.priceForUs = NumberUtils.cutFractions(form.priceForUs);
            }
          }
        } else {
          partBean = PartsBean.getPart(partNo, con);
          if (partBean == null) {
            throw new UserException("This Part No is not in the System : " + partNo);
          }
          form.partNo = partBean.getPartNo();
          form.vendorPartNo = "";
          form.partDescription1 = MakeModelBean.getMakeModelName(partBean.getMakeModelCode());
          form.partDescription2 = partBean.getPartDescription();

          form.partDescription1 = form.partDescription1.trim();
          int leftBracket = form.partDescription1.indexOf("(");
          int rightBracket = form.partDescription1.indexOf(")");
          if (leftBracket != -1 && rightBracket != -1 && (rightBracket - leftBracket) == 6) {
            form.partDescription1 =
                partBean.getYear() + " " + form.partDescription1.substring(0, leftBracket)
                    + form.partDescription1.substring(rightBracket + 1);
          }

          int committed = 0;
          committed = getCommitted(partBean.getPartNo(), partBean.getInterchangeNo());
          if (committed != 0) {
            form.unitsInStock =
                partBean.getUnitsInStock() + " / " + partBean.getUnitsOnOrder() + "-" + committed
                    + " / " + partBean.getReorderLevel();
          } else {
            form.unitsInStock =
                partBean.getUnitsInStock() + " / " + partBean.getUnitsOnOrder() + " / "
                    + partBean.getReorderLevel();
          }

          // form.oemNumber = partBean.getOemNumber();
          // form.unitsInStock = partBean.getUnitsInStock() + " / " +
          // partBean.getUnitsOnOrder() + " / " +
          // partBean.getReorderLevel();
          // logger.error(price);
          if (price != 0) {
            form.sellingRate = price;
          } else {
            form.sellingRate = 0;
          }
          form.noOfPieces = 0;
          form.quantity = qty;
          form.priceForUs = 0;
          partBean = null;
        }

        orderDetails.add(form);
        viBean = null;
      }

      con.close();
      return orderDetails;

    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Exception--" + e);
    }
  }

  public double findTotalPrice(Vector<OrderDetailsForm> orderDetails) {
    double totalPrice = 0.0;
    Enumeration<OrderDetailsForm> ennum = orderDetails.elements();
    while (ennum.hasMoreElements()) {
      OrderDetailsForm form = ennum.nextElement();
      double priceForUs = form.sellingRate * form.quantity;
      totalPrice += priceForUs;
    }
    // totalPrice = NumberUtils.cutFractions(totalPrice);
    String totPriceStr = totalPrice + "";
    if (totPriceStr.indexOf(".") != -1) {
      int len = totPriceStr.indexOf(".") + 3;
      if (len > totPriceStr.length())
        len = totPriceStr.length();
      totPriceStr = totPriceStr.substring(0, len);
    }
    totalPrice = Double.parseDouble(totPriceStr);

    return totalPrice;
  }

  public void getOrUpdate(int supId, VendorOrderForm form, HttpServletRequest request,
      ErrorBean errorBean) {
    Vector<OrderDetailsForm> orderDetails = new Vector<OrderDetailsForm>();

    int partCount = 0;
    boolean partsAvail = false;
    while (true) {
      partCount++;

      String partNo = (String) request.getParameter("PartNo" + partCount);
      String vendorPartNo = (String) request.getParameter("VendorPartNo" + partCount);
      String partDescription1 = (String) request.getParameter("PartDescription1" + partCount);
      String partDescription2 = (String) request.getParameter("PartDescription2" + partCount);
      // String oemNumber = (String) request.getParameter("OEMNumber" +
      // partCount);
      String unitsInStock = (String) request.getParameter("UnitsInStock" + partCount);
      String sellingRateObj = (String) request.getParameter("SellingRate" + partCount);
      String noOfPiecesObj = (String) request.getParameter("NoOfPieces" + partCount);
      String qtyObj = (String) request.getParameter("Quantity" + partCount);
      double priceForUs = 0.0;

      OrderDetailsForm oForm = null;

      if ((vendorPartNo == null || vendorPartNo.trim().equals(""))
          && (partNo == null || partNo.trim().equals(""))) {
        break;
      } else {
        partsAvail = true;
        oForm = new OrderDetailsForm();
      }

      int quantity = 0;
      try {
        quantity = Integer.parseInt(qtyObj);
      } catch (Exception e) {
        logger.error(e);
        quantity = 0;
      }

      double sellingRate = 0;
      try {
        sellingRate = Double.parseDouble(sellingRateObj);
      } catch (Exception e) {
        logger.error(e);
        sellingRate = 0.0;
      }

      int noOfPieces = 0;

      try {
        noOfPieces = Integer.parseInt(noOfPiecesObj);
      } catch (NumberFormatException e) {
        // logger.error(e);
        noOfPieces = 0;
      }

      if (partDescription2 == null || partDescription2.trim().equals("")) {
        try {
          VendorItemBean iBean = VendorItemBean.getThePart(supId, partNo, vendorPartNo, null);
          if (iBean != null) {
            oForm.partNo = iBean.getPartNo();
            oForm.vendorPartNo = iBean.getVendorPartNo();
            oForm.partDescription1 = iBean.getItemDesc1();
            oForm.partDescription2 = iBean.getItemDesc2();
            if (iBean.getPartNo() != null && !iBean.getPartNo().trim().equals("")) {
              Connection con = DBInterfaceLocal.getSQLConnection();
              oForm.unitsInStock = PartsBean.getQuantity(iBean.getPartNo(), con) + "";
              con.close();
            } else {
              oForm.unitsInStock = "";
            }
            // oForm.oemNumber = iBean.getOemNo();
            oForm.sellingRate = iBean.getSellingRate();
            oForm.noOfPieces = iBean.getNoOfPieces();
            oForm.quantity = quantity;
            oForm.priceForUs = oForm.sellingRate * oForm.quantity;
          } else {
            PartsBean partBean = PartsBean.getPart(partNo, null);
            oForm.partNo = partBean.getPartNo();
            oForm.vendorPartNo = "";
            oForm.partDescription1 = MakeModelBean.getMakeModelName(partBean.getMakeModelCode());
            oForm.partDescription2 = partBean.getPartDescription();
            oForm.unitsInStock = partBean.getUnitsInStock() + "";
            oForm.sellingRate = 0;
            oForm.noOfPieces = 0;
            oForm.quantity = quantity;
            oForm.priceForUs = 0;
          }
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError("The Part with PartNo " + partNo + " and Vendor Part No "
              + vendorPartNo + " is not available" + e);
          quantity = 0;
        }
      } else {
        oForm.partNo = partNo;
        oForm.vendorPartNo = vendorPartNo;
        oForm.partDescription1 = partDescription1;
        oForm.partDescription2 = partDescription2;
        // oForm.oemNumber = oemNumber;
        oForm.unitsInStock = unitsInStock;
        oForm.sellingRate = sellingRate;
        oForm.noOfPieces = noOfPieces;
        oForm.quantity = quantity;
        oForm.priceForUs = sellingRate * quantity;
      }
      if (oForm.priceForUs != 0) {
        if (oForm.priceForUs >= 1000) {
          oForm.priceForUs = Double.parseDouble(NumberUtils.cutFractions(oForm.priceForUs + ""));
        } else {
          oForm.priceForUs = NumberUtils.cutFractions(oForm.priceForUs);
        }
      }
      // if (quantity != 0) {
      orderDetails.add(oForm);
      // }

      if (partsAvail) {
        form.setTotalItems(orderDetails.size() + "");
        form.setOrderTotal(findTotalPrice(orderDetails) + "");
        if (form.getOrderTotal().indexOf(".") == form.getOrderTotal().length() - 2) {
          form.setOrderTotal(form.getOrderTotal() + "0");
        }
        request.getSession(false).setAttribute("OrderDetails", orderDetails);
      }
    }
  }

  public void updatePrices(Vector v, int supId) {
    try {
      // long st = System.currentTimeMillis();
      Connection connection = DBInterfaceLocal.getSQLConnection();
      OrderDetailsForm oForm = null;
      Enumeration ennum = v.elements();
      PreparedStatement pstmt =
          connection
              .prepareStatement("UPDATE VendorItems set SellingRate=? WHERE SupplierId=? AND PartNo=? AND VendorPartNo=?");
      // logger.error("Update Prices 1" +
      // (System.currentTimeMillis() - st));
      while (ennum.hasMoreElements()) {
        oForm = (OrderDetailsForm) ennum.nextElement();
        pstmt.clearParameters();
        pstmt.setDouble(1, oForm.sellingRate);
        pstmt.setInt(2, supId);
        pstmt.setString(3, oForm.partNo);
        pstmt.setString(4, oForm.vendorPartNo);

        if (oForm.partNo != null && !oForm.partNo.trim().equals("") && oForm.sellingRate != 0) {
          pstmt.execute();
          // logger.error("Updated");
        }
        // long st1 = System.currentTimeMillis();
        // logger.error("Update Prices 2" + (st1 - st));
        // st = st1;
        // pstmt.close();
        // connection.close();
      }
    } catch (SQLException e) {
      logger.error("Exception in VendorOrderAction When Updating Prices --- " + e);
    }
  }

  public int getCommitted(String partno, String interNo) {
    int committed = 0;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      String sql = "Select Sum(a.Quantity) From VendorOrderedItems a, VendorOrder b Where ";
      if (interNo.trim().equals("")) {
        Statement stmtXX = con.createStatement();
        ResultSet rsXX =
            stmtXX.executeQuery("Select PartNo From Parts Where InterChangeNo='" + partno + "'");
        sql += " (PartNo='" + partno + "' ";
        while (rsXX.next()) {
          // logger.error("Getting In 1");
          sql += " or PartNo='" + rsXX.getString(1) + "'";
        }
        sql += " ) ";
        rsXX.close();
        stmtXX.close();

      } else {
        sql += " (PartNo='" + partno + "' Or PartNo='" + interNo + "' ";
        Statement stmtXX = con.createStatement();
        ResultSet rsXX =
            stmtXX.executeQuery("Select PartNo From Parts Where InterChangeNo='" + interNo
                + "' and PartNo!='" + partno + "'");
        while (rsXX.next()) {
          // logger.error("Getting In 2");
          sql += " or PartNo='" + rsXX.getString(1) + "'";
        }
        sql += " ) ";
        rsXX.close();
        stmtXX.close();
      }
      sql += " and a.OrderNo=b.OrderNo and b.IsFinal='Y' and b.UpdatedInventory='N' ";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        committed = rs.getInt(1);
      }
      rs.close();
      stmt.close();

      con.close();
    } catch (Exception e) {
      logger.error(e);
    }
    return committed;
  }
}
