package com.bvas.action;

import java.io.IOException;
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

import com.bvas.beans.VendorOrderBean;
import com.bvas.formBeans.OrderFinalExtrasForm;
import com.bvas.formBeans.VendorOrderFinalForm;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;

public class OrderFinalAction extends Action {
  private static final Logger logger = Logger.getLogger(OrderFinalAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    VendorOrderFinalForm finalForm = (VendorOrderFinalForm) form;
    String buttonClicked = finalForm.getButtonClicked();
    finalForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorOrderFinalAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int orderNo = 0;
    int orderNoFromSession = 0;
    try {
      orderNo = Integer.parseInt(finalForm.getOrderNo());
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
    if (orderNoFromSession != 0) {
      orderNo = orderNoFromSession;
      finalForm.setOrderNo(orderNoFromSession + "");
    }

    int supplierId = 0;
    try {
      supplierId = Integer.parseInt((String) session.getAttribute("SupplierIdForOrder"));
    } catch (Exception e) {
      logger.error(e);
    }
    String companyName = (String) session.getAttribute("CompanyNameForOrder");

    finalForm.setSupplierId(supplierId + "");
    finalForm.setSupplierName(companyName);

    VendorOrderBean order = null;
    try {
      order = VendorOrderBean.getOrder(orderNo);
    } catch (Exception e) {
      logger.error("Order Not Available" + e);
    }
    if (order == null) {
      forwardPage = "VendorOrder";
      session.removeAttribute("OrderDetails");
      session.removeAttribute("OrderNumber");
      return (mapping.findForward(forwardPage));
    }

    session.removeAttribute("VendorOrderExtras");
    Vector<OrderFinalExtrasForm> orderExtras = findOrderExtras(request, finalForm, order);
    session.setAttribute("VendorOrderExtras", orderExtras);

    if (finalForm.getOrderDate() == null || finalForm.getOrderDate().trim().equals("")) {
      finalForm.setOrderDate(order.getOrderDate());
    }
    if (finalForm.getOrderPrice() == null || finalForm.getOrderPrice().trim().equals("")) {
      finalForm.setOrderPrice(order.getOrderTotal() + "");
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetOrUpdate")) {
      try {
        VendorOrderBean.removeExtras(orderNo);
        VendorOrderBean.addExtras(orderExtras);
      } catch (Exception e) {
        errorBean.setError("Expenditure details not updated" + e);
        logger.error(e);
      }
      forwardPage = "VendorFinal";

    } else if (buttonClicked.equals("DoFinalSteps")) {
      doingFinalSteps(orderExtras, supplierId, order, finalForm);
      errorBean.setError("THIS ORDER CHANGED SUCCESSFULLY!!!");
      forwardPage = "VendorFinal";
    } else if (buttonClicked.equals("PrintOrderFinal")) {
      forwardPage = "VendorFinal";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("SupplierIdForOrder");
      session.removeAttribute("CompanyNameForOrder");
      session.removeAttribute("ForAllCustomers");
      session.removeAttribute("OrderNumber");
      session.removeAttribute("OrderDetails");
      session.removeAttribute("VendorOrderExtras");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorOrderFinalError", errorBean);
    } else {
      session.removeAttribute("VendorOrderFinalError");
    }

    return (mapping.findForward(forwardPage));

  }

  public Vector<OrderFinalExtrasForm> findOrderExtras(HttpServletRequest request,
      VendorOrderFinalForm finalForm, VendorOrderBean order) {
    Vector<OrderFinalExtrasForm> orderExtras = new Vector<OrderFinalExtrasForm>();

    int orderNo = 0;
    try {
      orderNo = Integer.parseInt(finalForm.getOrderNo());
    } catch (Exception e) {
      logger.error(e);
    }
    double orderTotal = 0.0;
    try {
      orderTotal = order.getOrderTotal();
    } catch (Exception e) {
      logger.error(e);
    }

    int partCount = 0;
    boolean partsAvail = false;
    while (true) {
      partCount++;

      String reason = (String) request.getParameter("Reason" + partCount);
      String date = (String) request.getParameter("Date" + partCount);
      String amountStr = (String) request.getParameter("Amount" + partCount);

      OrderFinalExtrasForm oForm = null;

      if (reason == null) {
        break;
      } else if (reason.trim().equals("")) {
        continue;
      } else {
        partsAvail = true;
        oForm = new OrderFinalExtrasForm();
        try {
          oForm.orderNo = orderNo;
        } catch (Exception e) {
          logger.error(e);
        }
        oForm.extraReason = reason;
        oForm.addedDate = date;
        if (oForm.addedDate == null || oForm.addedDate.trim().equals("")) {
          oForm.addedDate = DateUtils.getNewUSDate();
        }
        try {
          oForm.extraAmount = Double.parseDouble(amountStr);
          orderTotal += oForm.extraAmount;
        } catch (Exception e) {
          oForm.extraAmount = 0.0;
          logger.error(e);
        }

      }
      orderExtras.add(oForm);
    }

    finalForm.setOrderTotal(orderTotal + "");
    return orderExtras;
  }

  public void doingFinalSteps(Vector<OrderFinalExtrasForm> orderExtras, int supplierId,
      VendorOrderBean order, VendorOrderFinalForm finalForm) {
    boolean findErrors = finalForm.getFindErrors();
    boolean updateInventory = finalForm.getDoInvenUpdate();
    // logger.error("UpdateInventory Is: " + updateInventory);
    boolean updatePrices = finalForm.getDoPriceCal();
    // logger.error("UpdatePrices Is: " + updatePrices);
    order.doFinalSteps(orderExtras, supplierId, findErrors, updateInventory, updatePrices);
  }

}
