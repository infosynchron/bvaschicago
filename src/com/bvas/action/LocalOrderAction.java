package com.bvas.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.InvoiceBean;
import com.bvas.beans.InvoiceDetailsBean;
import com.bvas.beans.LocalOrderBean;
import com.bvas.beans.LocalVendorBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.LocalOrderForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class LocalOrderAction extends Action {
  private static final Logger logger = Logger.getLogger(LocalOrderAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    LocalOrderForm ordForm = (LocalOrderForm) form;
    String buttonClicked = ordForm.getButtonClicked();
    ordForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in LocalOrderAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int supplierId = 0;
    try {
      supplierId = Integer.parseInt((String) session.getAttribute("LocalSupplierId"));
    } catch (Exception e) {
      logger.error(e);
    }
    String companyName = (String) session.getAttribute("LocalCompanyName");

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("Get")) {
      int invoiceNo = 0;
      String partNo = "";
      String vendorInvNo = "";
      try {
        invoiceNo = Integer.parseInt(ordForm.getInvoiceNo());
      } catch (Exception e) {
        invoiceNo = 0;
        logger.error(e);
      }
      partNo = ordForm.getPartNo();
      if (partNo == null) {
        partNo = "";
      } else {
        partNo = partNo.trim();
      }
      vendorInvNo = ordForm.getVendInvNo();
      if (vendorInvNo == null) {
        vendorInvNo = "";
      } else {
        vendorInvNo = vendorInvNo.trim();
      }
      if (invoiceNo == 0 && partNo.trim().equals("") && vendorInvNo.trim().equals("")) {
        errorBean
            .setError("YOU MUST ENTER ANY ONE FROM OUR INVOICE NO, PART NO, VENDOR INVOICE NO !!!!!!");
      } else {
        try {
          LocalOrderBean orderBean =
              LocalOrderBean.getLocalOrder(supplierId, invoiceNo, partNo, vendorInvNo);
          if (orderBean == null) {
            errorBean.setError("This Order Not In System");
            ordForm.reset();
          } else {
            fillToForm(orderBean, ordForm);
            session.setAttribute("LocalSupplierId", orderBean.getSupplierId() + "");
            session.setAttribute("LocalCompanyName",
                LocalVendorBean.getLocalVendor(orderBean.getSupplierId()).getCompanyName());
            session.setAttribute("LocalOrderBean", orderBean);
          }
        } catch (Exception e) {
          errorBean.setError("Exception in LocalOrderAction : " + e.getMessage());
          ordForm.reset();
          logger.error(e);
        }
      }
      forwardPage = "LocalOrder";
    } else if (buttonClicked.equals("CreateNewOrder")) {
      try {
        LocalOrderBean orderBean = createOrderBean(supplierId, ordForm, request);
        orderBean.createLocalOrder();
        // PrintUtils.printVendorOrder(orderBean, false);
        errorBean.setError("NEW ORDER CREATED SUCCESSFULLY");
      } catch (UserException e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }
      forwardPage = "LocalOrder";
    } else if (buttonClicked.equals("DeleteOrder")) {
      try {
        LocalOrderBean orderBean = null;
        Object o = session.getAttribute("LocalOrderBean");
        if (o == null) {
          throw new UserException("Order Not Found");
        } else {
          orderBean = (LocalOrderBean) o;
        }
        orderBean.deleteLocalOrder();
        ordForm.reset();
        session.removeAttribute("LocalOrderBean");
        errorBean.setError("ORDER DELETED SUCCESSFULLY");
      } catch (UserException e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }
      forwardPage = "LocalOrder";
    } else if (buttonClicked.equals("ClearOrder")) {
      ordForm.reset();
      forwardPage = "LocalOrder";
    } else if (buttonClicked.equals("PrintOrder")) {
      forwardPage = "LocalOrder";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("LocalSupplierId");
      session.removeAttribute("LocalCompanyName");
      ordForm.reset();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("LocalOrderError", errorBean);
    } else {
      session.removeAttribute("LocalOrderError");
    }

    return (mapping.findForward(forwardPage));

  }

  public LocalOrderBean createOrderBean(int supId, LocalOrderForm form, HttpServletRequest request)
      throws UserException {
    HttpSession session = request.getSession(false);
    UserBean user = (UserBean) session.getAttribute("User");
    LocalOrderBean voBean = new LocalOrderBean();
    try {
      voBean.setInvoiceNo(Integer.parseInt(form.getInvoiceNo()));
    } catch (Exception e) {
      voBean.setInvoiceNo(0);
      logger.error(e);
      // throw new UserException ("This Invoice Number is not valid");
    }
    InvoiceBean invoice = null;
    if (voBean.getInvoiceNo() != 0) {
      invoice = InvoiceBean.getInvoice(voBean.getInvoiceNo());
    }
    voBean.setDateEntered(form.getDateEntered());;
    voBean.setSupplierId(supId);
    voBean.setPartNo(form.getPartNo());

    voBean.setLocalVendorNo(form.getLocalVendorNo());
    try {
      voBean.setQuantity(Integer.parseInt(form.getQuantity()));
    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Quantity is not valid");
    }

    if (voBean.getQuantity() > 0 && !user.getRole().trim().equalsIgnoreCase("High")
        && voBean.getInvoiceNo() == 0) {
      if (supId != 37 && supId != 38) {
        throw new UserException("CANNOT ENTER!!! YOU MUST ENTER INVOICE NUMBER!!!");
      }
    }

    if (voBean.getInvoiceNo() != 0) {
      LocalOrderBean locBean =
          LocalOrderBean.getLocalOrder(0, voBean.getInvoiceNo(), voBean.getPartNo(), "");
      if (locBean != null) {
        throw new UserException("CANNOT ENTER!!! THIS ORDER IS ALREADY IN THE SYSTEM!!!");
      }
    }

    InvoiceDetailsBean invDet = null;
    if (invoice != null) {
      Enumeration<InvoiceDetailsBean> ennum = invoice.getInvoiceDetails().elements();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean tmpDets = ennum.nextElement();
        if (tmpDets.getPartNumber().trim().equalsIgnoreCase(voBean.getPartNo().trim())) {
          invDet = tmpDets;
        }
      }
    }
    if (voBean.getInvoiceNo() != 0 && invDet == null) {
      throw new UserException("CANNOT ENTER!!! THIS PART NUMBER NOT IN THIS INVOICE!!!");
    }
    if (!user.getRole().trim().equalsIgnoreCase("High") && invoice != null
        && voBean.getQuantity() != invDet.getQuantity()) {
      throw new UserException(
          "CANNOT ENTER!!! QUANTITY ENTERED IS NOT MATCHING WITH THE INVOICE QUANTITY!!!");
    }

    try {
      voBean.setPrice(Double.parseDouble(form.getPrice()));
    } catch (Exception e) {
      logger.error(e);
      throw new UserException("Price is not valid");
    }

    if (!user.getRole().trim().equalsIgnoreCase("High") && invoice != null
        && voBean.getPrice() > invDet.getSoldPrice()) {
      throw new UserException("CANNOT ENTER!!! THIS PRICE IS MORE THAN SOLD PRICE!!!");
    }

    if (form.getVendInvNo() == null || form.getVendInvNo().trim().equals("")) {
      throw new UserException("You must enter all the fields");
    } else {
      voBean.setVendInvNo(form.getVendInvNo());
    }
    if (form.getVendInvDate() == null || form.getVendInvDate().trim().equals("")) {
      throw new UserException("You must enter all the fields");
    } else {
      voBean.setVendInvDate(form.getVendInvDate());
    }

    return voBean;
  }

  public void fillToForm(LocalOrderBean orderBean, LocalOrderForm orderForm) {
    orderForm.setInvoiceNo(orderBean.getInvoiceNo() + "");
    orderForm.setDateEntered(orderBean.getDateEntered());
    orderForm.setPartNo(orderBean.getPartNo());
    orderForm.setLocalVendorNo(orderBean.getLocalVendorNo());
    orderForm.setQuantity(orderBean.getQuantity() + "");
    orderForm.setPrice(orderBean.getPrice() + "");
    orderForm.setVendInvNo(orderBean.getVendInvNo());
    orderForm.setVendInvDate(orderBean.getVendInvDate());
  }

}
