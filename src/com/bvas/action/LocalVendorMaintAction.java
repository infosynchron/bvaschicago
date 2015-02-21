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

import com.bvas.beans.AddressBean;
import com.bvas.beans.LocalVendorBean;
import com.bvas.formBeans.LocalVendorMaintForm;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;

public class LocalVendorMaintAction extends Action {
  private static final Logger logger = Logger.getLogger(LocalVendorMaintAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    LocalVendorMaintForm vForm = (LocalVendorMaintForm) form;
    String buttonClicked = vForm.getButtonClicked();
    vForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in LocalVendorMaintAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int supplierId = 0;
    String supId = vForm.getSupplierId();
    try {
      supplierId = Integer.parseInt(supId);
    } catch (Exception e) {
      logger.error(e);
    }

    Object o = session.getAttribute("LocalVendor");
    LocalVendorBean vendor = null;
    if (o != null) {
      vendor = (LocalVendorBean) o;
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetVendor")) {
      try {
        if (supplierId != 0) {
          vendor = LocalVendorBean.getLocalVendor(supplierId);
          fillForm(vForm, vendor);
          session.removeAttribute("LocalVendor");
          session.setAttribute("LocalVendor", vendor);
        } else {
          errorBean.setError("PLEASE ENTER VENDOR ID");
        }
      } catch (Exception e) {
        errorBean.setError(e.getMessage());
        vForm.reset();
        logger.error(e);
      }
      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.equals("AddNewVendor")) {
      try {
        if (supplierId != 0) {
          vendor = fillBean(vForm);
          vendor.addNewLocalVendor();
          session.setAttribute("LocalVendor", vendor);
          errorBean.setError("VENDOR ADDED SUCCESSFULLY!!!");
        } else {
          errorBean.setError("VENDOR NOT ADDED");
        }
      } catch (Exception e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }
      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.equals("ChangeVendor")) {
      try {
        if (supplierId != 0 && vendor != null && supplierId == vendor.getSupplierId()) {
          fillBeanForChange(vForm, vendor);
          vendor.changeLocalVendor();
          session.setAttribute("LocalVendor", vendor);
          errorBean.setError("VENDOR CHANGED SUCCESSFULLY!!!");
        } else {
          errorBean.setError("VENDOR NOT CHANGED---");
        }

      } catch (Exception e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }

      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.equals("DeleteVendor")) {
      try {
        if (supplierId != 0 && vendor != null) {
          vendor.deleteLocalVendor();
          vForm.reset();
          session.removeAttribute("LocalVendor");
          errorBean.setError("VENDOR DELETED SUCCESSFULLY!!!");
        }
      } catch (Exception e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }
      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.equals("PrintVendorListing")) {
      // logger.error("---Going to print vendor Listing---");
      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.equals("Clear")) {
      vForm.reset();
      session.removeAttribute("LocalVendor");
      forwardPage = "LocalVendorMaint";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("LocalVendor");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("LocalVendorMaintError", errorBean);
    } else {
      session.removeAttribute("LocalVendorMaintError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillForm(LocalVendorMaintForm vForm, LocalVendorBean vBean) {
    vForm.setSupplierId(vBean.getSupplierId() + "");
    vForm.setCompanyName(vBean.getCompanyName());
    vForm.setContactName(vBean.getContactName());
    vForm.setContactTitle(vBean.getContactTitle());
    vForm.setHomePage(vBean.getHomePage());
    vForm.setEmail(vBean.getEMail());
    vForm.setComments(vBean.getComments());

    AddressBean address = vBean.getAddress();

    vForm.setPhone(address.getPhone());
    vForm.setFax(address.getFax());
    vForm.setAddress1(address.getAddress1());
    vForm.setAddress2(address.getAddress2());
    vForm.setCity(address.getCity());
    vForm.setRegion(address.getRegion());
    vForm.setPostalCode(address.getPostalCode());
    vForm.setCountry(address.getCountry());

  }

  public LocalVendorBean fillBean(LocalVendorMaintForm vForm) {
    LocalVendorBean vBean = new LocalVendorBean();

    vBean.setSupplierId(Integer.parseInt(vForm.getSupplierId()));
    vBean.setCompanyName(vForm.getCompanyName());
    vBean.setContactName(vForm.getContactName());
    vBean.setContactTitle(vForm.getContactTitle());
    vBean.setHomePage(vForm.getHomePage());
    vBean.setEMail(vForm.getEmail());
    vBean.setComments(vForm.getComments());

    AddressBean address = new AddressBean();
    address.setPhone(vForm.getPhone());
    address.setFax(vForm.getFax());
    address.setAddress1(vForm.getAddress1());
    address.setAddress2(vForm.getAddress2());
    address.setCity(vForm.getCity());
    address.setRegion(vForm.getRegion());
    address.setPostalCode(vForm.getPostalCode());
    address.setCountry(vForm.getCountry());
    address.setId(vForm.getSupplierId().trim());
    address.setType("");
    address.setWho("LocVend");
    address.setDateCreated(DateUtils.getNewUSDate());
    address.setInvoiceNumber(0);
    address.setState("");
    address.setExt("");

    vBean.setAddress(address);

    return vBean;
  }

  public void fillBeanForChange(LocalVendorMaintForm vForm, LocalVendorBean vendor) {
    vendor.setCompanyName(vForm.getCompanyName());
    vendor.setContactName(vForm.getContactName());
    vendor.setContactTitle(vForm.getContactTitle());
    vendor.setHomePage(vForm.getHomePage());
    vendor.setEMail(vForm.getEmail());
    vendor.setComments(vForm.getComments());

    vendor.getAddress().setInvoiceNumber(0);
    vendor.getAddress().setPhone(vForm.getPhone());
    vendor.getAddress().setFax(vForm.getFax());
    vendor.getAddress().setAddress1(vForm.getAddress1());
    vendor.getAddress().setAddress2(vForm.getAddress2());
    vendor.getAddress().setCity(vForm.getCity());
    vendor.getAddress().setRegion(vForm.getRegion());
    vendor.getAddress().setPostalCode(vForm.getPostalCode());
    vendor.getAddress().setCountry(vForm.getCountry());
    vendor.getAddress().setId(vForm.getSupplierId().trim());
    vendor.getAddress().setType("");
    vendor.getAddress().setWho("LocVend");
    vendor.getAddress().setDateCreated(DateUtils.getNewUSDate());
    vendor.getAddress().setExt("");
    vendor.getAddress().setState("");
  }

}
