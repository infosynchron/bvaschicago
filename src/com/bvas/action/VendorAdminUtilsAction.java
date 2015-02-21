package com.bvas.action;

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
import com.bvas.beans.VendorOrderBean;
import com.bvas.formBeans.VendorAdminUtilsForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class VendorAdminUtilsAction extends Action {
  private static final Logger logger = Logger.getLogger(VendorAdminUtilsAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    VendorAdminUtilsForm utilForm = (VendorAdminUtilsForm) form;
    String buttonClicked = utilForm.getButtonClicked();
    utilForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in TodaysOrdersAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("ChangeUnitsOnOrder")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForUnitsOrder());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Doing Units On Order");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          VendorOrderBean orderBean = VendorOrderBean.getOrder(orderNo);
          orderBean.changeUnitsOnOrder();
          errorBean.setError("UNITS ON ORDER CHANGED SUCCESSFULLY");
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("DeleteTheOrder")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForDeleteOrder());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Deleting The Order");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          boolean success = VendorOrderBean.deleteOrder(orderNo);
          if (success) {
            errorBean.setError("ORDER DELETED SUCCESSFULLY");
          } else {
            errorBean.setError("Order Not Deleted");
          }
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("DeleteItemsFromOrder")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForDeleteItems());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Deleting Items");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          boolean success = VendorOrderBean.deleteItems(orderNo);
          if (success) {
            errorBean.setError("ITEMS DELETED SUCCESSFULLY FROM THE ORDER");
          } else {
            errorBean.setError("Items Not Deleted From Order");
          }
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("CreateOrderForOthers")) {
      try {
        int orderNo1 = 0;
        int orderNo2 = 0;
        int supplierId = 0;
        try {
          orderNo1 = Integer.parseInt(utilForm.getOrderNoForCreateOrder1());
        } catch (Exception e) {
          logger.error(e);
          orderNo1 = 0;
          throw new UserException("Enter the Frist Order No For Creating Order");
        }
        try {
          orderNo2 = Integer.parseInt(utilForm.getOrderNoForCreateOrder2());
        } catch (Exception e) {
          logger.error(e);
          orderNo2 = 0;
          throw new UserException("Enter the Second Order No For Creating Order");
        }
        try {
          supplierId = Integer.parseInt(utilForm.getSupplierIdForCreateOrder());
        } catch (Exception e) {
          logger.error(e);
          supplierId = 0;
          throw new UserException("Enter Supplier Id For Creating Order");
        }

        if (VendorOrderBean.isAvailable(orderNo1) && !VendorOrderBean.isAvailable(orderNo2)) {
          boolean success = VendorOrderBean.createOrderForOthers(orderNo1, orderNo2, supplierId);
          if (success) {
            errorBean.setError("NEW ORDER CREATED SUCCESSFULLY");
          } else {
            errorBean.setError("UNABLE TO CREATE NEW ORDER");
          }
        } else {
          throw new UserException("Please check your order No's : " + orderNo1 + ", " + orderNo2);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("DeleteItemsFromOrder1")) {
      try {
        int orderNo1 = 0;
        int orderNo2 = 0;
        try {
          orderNo1 = Integer.parseInt(utilForm.getOrderNoForDeleteItems1());
        } catch (Exception e) {
          logger.error(e);
          orderNo1 = 0;
          throw new UserException("Enter the First Order No For Deleting Items From Order 1");
        }
        try {
          orderNo2 = Integer.parseInt(utilForm.getOrderNoForDeleteItems2());
        } catch (Exception e) {
          logger.error(e);
          orderNo2 = 0;
          throw new UserException("Enter the Second Order No For Deleting Items From Order 1");
        }
        if (VendorOrderBean.isAvailable(orderNo1) && VendorOrderBean.isAvailable(orderNo2)) {
          boolean success = VendorOrderBean.deleteItemsFromOrder1(orderNo1, orderNo2);
          if (success) {
            errorBean.setError("ITEMS DELETED FROM THE FIRST ORDER SUCCESSFULLY");
          } else {
            errorBean.setError("UNABLE TO DELETE ITEMS");
          }
        } else {
          throw new UserException("Please check your order No's : " + orderNo1 + ", " + orderNo2);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("FindCuft")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForCuft());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For getting Cuft");
        }
        /*
         * if (VendorOrderBean.isAvailable(orderNo)) { ReportUtils.showCuft(orderNo); } else { throw
         * new UserException ("Check your order No : " + orderNo); }
         */
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("CompareForPrices")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForComparePrices());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Comparing");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          VendorOrderBean.compareForPrices(orderNo);
          errorBean.setError("ORDER COMPARED SUCCESSFULLY");
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("MergeOrders")) {
      try {
        int orderNo1 = 0;
        int orderNo2 = 0;
        try {
          orderNo1 = Integer.parseInt(utilForm.getOrderNoForMergeOrders1());
        } catch (Exception e) {
          logger.error(e);
          orderNo1 = 0;
          throw new UserException("Enter the Frist Order No For Merging Orders");
        }
        try {
          orderNo2 = Integer.parseInt(utilForm.getOrderNoForMergeOrders2());
        } catch (Exception e) {
          logger.error(e);
          orderNo2 = 0;
          throw new UserException("Enter the Second Order No For Merging Orders");
        }

        if (VendorOrderBean.isAvailable(orderNo1) && VendorOrderBean.isAvailable(orderNo2)) {
          boolean success = VendorOrderBean.mergeOrders(orderNo1, orderNo2);
          if (success) {
            errorBean.setError("ORDERS MERGED SUCCESSFULLY");
          } else {
            errorBean.setError("UNABLE TO MERGE ORDERS");
          }
        } else {
          throw new UserException("Please check your order No's : " + orderNo1 + ", " + orderNo2);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("ChangeOrder")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForChange());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Changing");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          VendorOrderBean.removeAlreadyOrdered(orderNo);
          errorBean.setError("ORDER Changes SUCCESSFULLY");
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("MakeFinal")) {
      try {
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForMakeFinal());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Changing");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          VendorOrderBean.makeFinal(orderNo);
          errorBean.setError("ORDER IS MADE AS FINAL !!!!!");
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("RemoveFinal")) {
      try {
        logger.error(1);
        int orderNo = 0;
        try {
          orderNo = Integer.parseInt(utilForm.getOrderNoForRemoveFinal());
        } catch (Exception e) {
          logger.error(e);
          orderNo = 0;
          throw new UserException("Enter the Order No For Change");
        }
        if (VendorOrderBean.isAvailable(orderNo)) {
          VendorOrderBean.removeFinal(orderNo);
          errorBean.setError("ORDER REMOVED FROM THE FINAL LIST !!!!!");
        } else {
          throw new UserException("Check your order No : " + orderNo);
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorAdminUtilsError", errorBean);
    } else {
      session.removeAttribute("VendorAdminUtilsError");
    }

    return (mapping.findForward(forwardPage));

  }
}
