package com.bvas.action;

import java.io.IOException;
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

import com.bvas.beans.VendorBean;
import com.bvas.beans.VendorOrderBean;
import com.bvas.formBeans.VendorOrderDetailsForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class VendorOrderDetailsAction extends Action {
  private static final Logger logger = Logger.getLogger(VendorOrderDetailsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    VendorOrderDetailsForm vodForm = (VendorOrderDetailsForm) form;
    String buttonClicked = vodForm.getButtonClicked();
    vodForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorOrderDetailsAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetOrder")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(vodForm.getOrderNo());
        } catch (Exception e) {
          logger.error(e);
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          VendorOrderBean voBean = VendorOrderBean.getOrder(orderNo);
          fillForm(vodForm, voBean);
          session.setAttribute("VendorOrderBeanForDetails", voBean);
        } else {
          throw new UserException("Check Your Order No");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrderDetails";
    } else if (buttonClicked.equals("Clear")) {
      vodForm.reset();
      session.removeAttribute("VendorOrderBeanForDetails");
      forwardPage = "VendorOrderDetails";
    } else if (buttonClicked.equals("Change")) {
      try {
        Object vob = session.getAttribute("VendorOrderBeanForDetails");
        VendorOrderBean voBean = null;
        if (vob != null) {
          voBean = (VendorOrderBean) vob;
          changeDetails(vodForm, voBean);
          errorBean.setError("ORDER CHANGED SUCCESSFULLY");
        } else {
          throw new UserException("Order Not Available - Get The Order First");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorOrderDetails";
    } else if (buttonClicked.equals("PendingPayments")) {
      try {
        Hashtable toShowSales = VendorOrderBean.showPendingOrders();
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("Error .....");
          forwardPage = "VendorOrderDetails";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
        forwardPage = "VendorOrderDetails";
      }

    } else if (buttonClicked.equals("PaidPayments")) {
      // try {

      try {
        Hashtable toShowSales;
        toShowSales = VendorOrderBean.showPaidOrders();
        if (toShowSales != null) {
          session.setAttribute("toShowReports", toShowSales);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("Error .....");
          forwardPage = "VendorOrderDetails";
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
        forwardPage = "VendorOrderDetails";
      }

      // }
      /*
       * catch (Exception e) { logger.error(e); errorBean.setError(e.getMessage()); forwardPage =
       * "VendorOrderDetails";
       */
      // }

    } else if (buttonClicked.equals("BackToMenu")) {
      vodForm.reset();
      session.removeAttribute("VendorOrderBeanForDetails");
      forwardPage = "VendorMenu";
    } else if (buttonClicked.equals("ReturnToMain")) {
      vodForm.reset();
      session.removeAttribute("VendorOrderBeanForDetails");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorOrderDetailsError", errorBean);
    } else {
      session.removeAttribute("VendorOrderDetailsError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void fillForm(VendorOrderDetailsForm vodForm, VendorOrderBean voBean) throws UserException {
    vodForm.setCompanyName(VendorBean.getVendor(voBean.getSupplierId()).getCompanyName());
    vodForm.setContainerNo(voBean.getContainerNo());
    vodForm.setSupplInvNo(voBean.getSupplInvNo());
    vodForm.setOrderDate(voBean.getOrderDate());
    vodForm.setShipDate(voBean.getDeliveredDate());
    vodForm.setArrivedDate(voBean.getArrivedDate());
    vodForm.setIsFinal(voBean.getIsFinal());
    vodForm.setTotalItems(voBean.getTotalItems() + "");
    vodForm.setOrderTotal(voBean.getOrderTotal() + "");
    vodForm.setDiscount(voBean.getDiscount() + "");
    vodForm.setStickerCharges(voBean.getStickerCharges() + "");
    vodForm.setOverheadAmountsTotal(voBean.getOverheadAmountsTotal() + "");
    vodForm.setUnitsOrderDoneDate(voBean.getUnitsOrderDoneDate());
    vodForm.setPricesDoneDate(voBean.getPricesDoneDate());
    vodForm.setInventoryDoneDate(voBean.getInventoryDoneDate());
    vodForm.setPaymentTerms(voBean.getPaymentTerms());
    vodForm.setPaymentDate(voBean.getPaymentDate());
    vodForm.setEstimatedArrivalDate(voBean.getEstimatedArrivalDate());
  }

  public void changeDetails(VendorOrderDetailsForm vodForm, VendorOrderBean voBean)
      throws UserException {
    String containerNo = vodForm.getContainerNo().trim();
    String supplInvNo = vodForm.getSupplInvNo().trim();
    String orderDate = vodForm.getOrderDate().trim();
    String shipDate = vodForm.getShipDate().trim();
    String arrivedDate = vodForm.getArrivedDate().trim();
    boolean isFinal = vodForm.getIsFinal();
    String unitsOrderDoneDate = vodForm.getUnitsOrderDoneDate().trim();
    String pricesDoneDate = vodForm.getPricesDoneDate().trim();
    String invenDoneDate = vodForm.getInventoryDoneDate().trim();
    String paymentTerms = vodForm.getPaymentTerms().trim();
    String paymentDate = vodForm.getPaymentDate().trim();
    String estimatedArrivalDate = vodForm.getEstimatedArrivalDate().trim();

    boolean changedOrder = false;
    if (voBean.getContainerNo() == null && containerNo != null) {
      voBean.setContainerNo(containerNo);
      changedOrder = true;
    } else if (containerNo != null && !containerNo.equals("")
        && !containerNo.equals(voBean.getContainerNo().trim())) {
      voBean.setContainerNo(containerNo);
      changedOrder = true;
    }
    if (voBean.getSupplInvNo() == null && supplInvNo != null) {
      voBean.setSupplInvNo(supplInvNo);
      changedOrder = true;
    } else if (supplInvNo != null && !supplInvNo.equals("")
        && !supplInvNo.equals(voBean.getSupplInvNo().trim())) {
      voBean.setSupplInvNo(supplInvNo);
      changedOrder = true;
    }
    if (voBean.getOrderDate() == null && orderDate != null) {
      voBean.setOrderDate(orderDate);
      changedOrder = true;
    } else if (orderDate != null && !orderDate.equals("")
        && !orderDate.equals(voBean.getOrderDate().trim())) {
      voBean.setOrderDate(orderDate);
      changedOrder = true;
    }
    if (voBean.getDeliveredDate() == null && shipDate != null) {
      voBean.setDeliveredDate(shipDate);
      changedOrder = true;
    } else if (shipDate != null && !shipDate.equals("")
        && !shipDate.equals(voBean.getDeliveredDate().trim())) {
      voBean.setDeliveredDate(shipDate);
      changedOrder = true;
    }
    if (voBean.getArrivedDate() == null && arrivedDate != null) {
      voBean.setArrivedDate(arrivedDate);
      changedOrder = true;
    } else if (arrivedDate != null && !arrivedDate.equals("")
        && !arrivedDate.equals(voBean.getArrivedDate().trim())) {
      voBean.setArrivedDate(arrivedDate);
      changedOrder = true;
    }
    if (isFinal != voBean.getIsFinal()) {
      voBean.setIsFinal(isFinal);
      changedOrder = true;
    }
    if (voBean.getUnitsOrderDoneDate() == null && unitsOrderDoneDate != null) {
      voBean.setUnitsOrderDoneDate(unitsOrderDoneDate);
      changedOrder = true;
    } else if (unitsOrderDoneDate != null && !unitsOrderDoneDate.equals("")
        && !unitsOrderDoneDate.equals(voBean.getUnitsOrderDoneDate().trim())) {
      voBean.setUnitsOrderDoneDate(unitsOrderDoneDate);
      changedOrder = true;
    }
    if (voBean.getPricesDoneDate() == null && pricesDoneDate != null) {
      voBean.setPricesDoneDate(pricesDoneDate);
      changedOrder = true;
    } else if (pricesDoneDate != null && !pricesDoneDate.equals("")
        && !pricesDoneDate.equals(voBean.getPricesDoneDate().trim())) {
      voBean.setPricesDoneDate(pricesDoneDate);
      changedOrder = true;
    }
    if (voBean.getInventoryDoneDate() == null && invenDoneDate != null) {
      voBean.setInventoryDoneDate(invenDoneDate);
      changedOrder = true;
    } else if (invenDoneDate != null && !invenDoneDate.equals("")
        && !invenDoneDate.equals(voBean.getInventoryDoneDate().trim())) {
      voBean.setInventoryDoneDate(invenDoneDate);
      changedOrder = true;
    }
    if (voBean.getPaymentTerms() == null && paymentTerms != null) {
      voBean.setPaymentTerms(paymentTerms);
      changedOrder = true;
    } else if (paymentTerms != null && !paymentTerms.equals("")
        && !paymentTerms.equals(voBean.getPaymentTerms().trim())) {
      voBean.setPaymentTerms(paymentTerms);
      changedOrder = true;
    }
    if (voBean.getPaymentDate() == null && paymentDate != null) {
      voBean.setPaymentDate(paymentDate);
      changedOrder = true;
    } else if (paymentDate != null && !paymentDate.equals(voBean.getPaymentDate().trim())) {
      voBean.setPaymentDate(paymentDate);
      changedOrder = true;
    }
    if (voBean.getEstimatedArrivalDate() == null && estimatedArrivalDate != null) {
      voBean.setEstimatedArrivalDate(estimatedArrivalDate);
      changedOrder = true;
    } else if (estimatedArrivalDate != null
        && !estimatedArrivalDate.equals(voBean.getEstimatedArrivalDate().trim())) {
      voBean.setEstimatedArrivalDate(estimatedArrivalDate);
      changedOrder = true;
    }

    if (changedOrder) {
      logger.error("Order Changed");
      try {
        voBean.changeOrder();
      } catch (Exception e) {
        logger.error(e);
        throw new UserException("Order Not Changed --- " + e.getMessage());
      }
    } else {
      logger.error("Order Not Changed");
      throw new UserException("NOTHING TO CHANGE");
    }
  }

}
