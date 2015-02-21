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
import com.bvas.beans.VendorBean;
import com.bvas.formBeans.VendorMenuForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;

public class VendorMenuAction extends Action {
  private static final Logger logger = Logger.getLogger(VendorMenuAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((VendorMenuForm) form).getButtonClicked();
    ((VendorMenuForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    logger.error(new java.util.Date(System.currentTimeMillis()) + "-----VendorMenu-----"
        + user.getUsername());

    String vendorSel = ((VendorMenuForm) form).getVendorSelect();
    int supplierId = 0;
    String companyName = "";
    logger.error("Vendor Selected=" + vendorSel);
    try {
      supplierId = Integer.parseInt(vendorSel);
    } catch (Exception e) {
      logger.error(e);
    }
    if (supplierId != 0) {
      try {
        companyName = VendorBean.getVendor(supplierId).getCompanyName();
      } catch (Exception e) {
        logger.error(e);
        companyName = "";
      }
      session.setAttribute("SupplierIdForItem", supplierId + "");
      session.setAttribute("CompanyNameForItem", companyName);
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.trim().equals("VendorMaint")) {
      PrintUtils.createVendorListing();
      forwardPage = "VendorMaint";
    } else if (buttonClicked.trim().equals("VendorItem")) {
      if (supplierId == 0) {
        errorBean.setError("Please select the Vendor from the list");
        forwardPage = "VendorMenu";
      } else {
        session.setAttribute("SupplierIdForItem", supplierId + "");
        session.setAttribute("CompanyNameForItem", companyName);
        forwardPage = "VendorItem";
      }
    } else if (buttonClicked.trim().equals("VendorOrder")) {
      if (supplierId == 0) {
        errorBean.setError("Please select the Vendor from the list");
        forwardPage = "VendorMenu";
      } else {
        session.setAttribute("SupplierIdForOrder", supplierId + "");
        session.setAttribute("CompanyNameForOrder", companyName);
        forwardPage = "VendorOrder";
      }
    } else if (buttonClicked.trim().equals("VendorOurPart")) {
      session.setAttribute("SupplierIdForOrder", supplierId + "");
      session.setAttribute("CompanyNameForOrder", companyName);
      forwardPage = "VendorOurPart";
    } else if (buttonClicked.trim().equals("AdminUtilities")) {
      if (supplierId != 0) {
        session.setAttribute("SupplierIdForOrder", supplierId + "");
      }
      if (companyName != null && !companyName.trim().equals("")) {
        session.setAttribute("CompanyNameForOrder", companyName);
      }
      forwardPage = "VendorAdminUtils";
    } else if (buttonClicked.trim().equals("OrderDetails")) {
      forwardPage = "VendorOrderDetails";
    } else if (buttonClicked.trim().equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorMenuError", errorBean);
    } else {
      session.removeAttribute("VendorMenuError");
    }

    return (mapping.findForward(forwardPage));

  }

}
