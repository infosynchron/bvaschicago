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

import com.bvas.beans.LocalVendorBean;
import com.bvas.formBeans.LocalVendorMenuForm;
import com.bvas.utils.ErrorBean;

public class LocalVendorMenuAction extends Action {
  private static final Logger logger = Logger.getLogger(LocalVendorMenuAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    String buttonClicked = ((LocalVendorMenuForm) form).getButtonClicked();
    ((LocalVendorMenuForm) form).setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    String vendorSel = ((LocalVendorMenuForm) form).getVendorSelect();
    int supplierId = 0;
    String companyName = "";
    // logger.error("Vendor Selected="+vendorSel);
    try {
      supplierId = Integer.parseInt(vendorSel);
    } catch (Exception e) {
      logger.error(e);
    }
    if (supplierId != 0) {
      try {
        companyName = LocalVendorBean.getLocalVendor(supplierId).getCompanyName();
      } catch (Exception e) {
        companyName = "";
        logger.error(e);
      }
      session.setAttribute("LocalSupplierIdForItem", supplierId + "");
      session.setAttribute("LocalCompanyNameForItem", companyName);
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.trim().equals("VendorMaint")) {
      // PrintUtils.createLocalVendorListing();
      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.trim().equals("LocalOrder")) {
      if (supplierId == 0) {
        errorBean.setError("Please select the Vendor from the list");
        forwardPage = "LocalVendorMenu";
      } else {
        session.setAttribute("LocalSupplierId", supplierId + "");
        session.setAttribute("LocalCompanyName", companyName);
        forwardPage = "LocalOrder";
      }
    }
    /*
     * else if (buttonClicked.trim().equals("VendorOrder")) { if (supplierId == 0) {
     * errorBean.setError("Please select the Vendor from the list"); forwardPage = "VendorMenu"; }
     * else { session.setAttribute("SupplierIdForOrder", supplierId+"");
     * session.setAttribute("CompanyNameForOrder", companyName); forwardPage = "VendorOrder"; } }
     * else if (buttonClicked.trim().equals("VendorOurPart")) {
     * session.setAttribute("SupplierIdForOrder", supplierId+"");
     * session.setAttribute("CompanyNameForOrder", companyName); forwardPage = "VendorOurPart"; }
     */
    else if (buttonClicked.trim().equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("LocalVendorMenuError", errorBean);
    } else {
      session.removeAttribute("LocalVendorMenuError");
    }

    return (mapping.findForward(forwardPage));

  }

}
