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
import com.bvas.beans.CustomerBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.ClientLookupForm;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class ClientLookupAction extends Action {
  private static final Logger logger = Logger.getLogger(ClientLookupAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    ClientLookupForm custForm = (ClientLookupForm) form;
    String buttonClicked = custForm.getButtonClicked();
    custForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MainMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    String custId = custForm.getClientId();

    String idFromName = (String) request.getParameter("SelectClientName");
    String custName = "";
    String idOrName = "";
    if (idFromName == null || idFromName.trim().equals("0000000000")) {
      if (custId != null && !custId.trim().equals("")) {
        idOrName = custId;
      } else {
        idOrName = "";
      }
    } else {
      idOrName = idFromName;
    }
    CustomerBean custBean = null;
    Object o = session.getAttribute("Customer");
    if (o != null) {
      custBean = (CustomerBean) o;
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetClient")) {
      session.removeAttribute("Customer");
      custForm.resetForm();

      try {
        custBean = CustomerBean.getCustomer(idOrName);
        logger.error(idOrName);
        if (custBean != null) {
          fillForm(custForm, custBean);
          session.setAttribute("Customer", custBean);
          logger.error("Cust:" + idOrName);
          session.setAttribute("LookupCustomer", idOrName);
        } else {
          errorBean.setError("Error: Please check your Customer ID");
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }

      forwardPage = "ClientLookup";
    } else if (buttonClicked.equals("GoToInvoice")) {

      int invNo = 0;
      try {
        invNo = Integer.parseInt((String) session.getAttribute("InvoiceNoForGen"));
      } catch (Exception e) {
        logger.error(e);
      }
      try {
        if (invNo == 0 || InvoiceBean.isAvailable(invNo)) {
          invNo = InvoiceBean.getNewInvoiceNo(user.getUsername());
          session.setAttribute("InvoiceNoForGen", invNo + "");
        }
      } catch (Exception e) {
        logger.error(e);
      }
      if (custBean == null) {
        session.removeAttribute("LookupCustomer");
        session.removeAttribute("Customer");
        forwardPage = "InvoiceGen";
      } else {
        session.setAttribute("CustFromLookup", custBean.getCustomerId());
        session.removeAttribute("LookupCustomer");
        session.removeAttribute("Customer");
        forwardPage = "InvoiceGen";
      }
    } else if (buttonClicked.equals("InventAvail")) {

      if (custBean == null) {
        session.removeAttribute("LookupCustomer");
        session.removeAttribute("Customer");
        forwardPage = "InvenAvail";
      } else {
        session.setAttribute("CustFromLookup", custBean.getCustomerId());
        session.removeAttribute("LookupCustomer");
        session.removeAttribute("Customer");
        forwardPage = "InvenAvail";
      }
    } else if (buttonClicked.equals("ClearClient")) {
      forwardPage = "ClientLookup";
      session.removeAttribute("Customer");
      session.removeAttribute("LookupCustomer");
      custForm.resetForm();
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
      custForm.resetForm();
      session.removeAttribute("LookupCustomer");
      session.removeAttribute("Customer");
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("ClientLookupError", errorBean);
    } else {
      session.removeAttribute("ClientLookupError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void fillForm(ClientLookupForm custForm, CustomerBean custBean) {
    custForm.setClientId(custBean.getCustomerId());
    custForm.setMostRecentInvoice(custBean.getMostRecentInvoice());
    custForm.setCompanyName(custBean.getCompanyName());
    custForm.setContactName(custBean.getContactName());
    custForm.setContactTitle(custBean.getContactTitle());
    custForm.setTerms(custBean.getTerms());
    if (custBean.getTaxId() != null && !custBean.getTaxId().trim().equals("")
        && custBean.getTaxId().trim().equals("Y")) {
      custForm.setTaxId(true);
    }
    custForm.setTaxIdNumber(custBean.getTaxIdNumber());
    custForm.setNotes(custBean.getNotes());

    custForm.setAddress1(custBean.getAddress().getAddress1());
    custForm.setAddress2(custBean.getAddress().getAddress2());
    custForm.setCity(custBean.getAddress().getCity());
    custForm.setState(custBean.getAddress().getState());
    custForm.setRegion(custBean.getAddress().getRegion());
    custForm.setPostalCode(custBean.getAddress().getPostalCode());
    custForm.setCountry(custBean.getAddress().getCountry());
    custForm.setPhone(custBean.getAddress().getPhone());
    custForm.setExt(custBean.getAddress().getExt());
    custForm.setFax(custBean.getAddress().getFax());

  }

  public void fillBean(ClientLookupForm form, CustomerBean customer) {
    customer.setCompanyName(form.getCompanyName());
    customer.setContactName(form.getContactName());
    customer.setContactTitle(form.getContactTitle());
    customer.setTerms(form.getTerms());
    if (form.getTaxId() == true) {
      customer.setTaxId("Y");
    } else {
      customer.setTaxId("N");
    }
    customer.setTaxIdNumber(form.getTaxIdNumber());
    customer.setNotes(form.getNotes());

    AddressBean address = new AddressBean();

    address.setId(form.getClientId().trim());
    address.setType("Standard");
    address.setWho("Cust");
    address.setDateCreated(DateUtils.getNewUSDate());
    address.setAddress1(form.getAddress1());
    address.setAddress2(form.getAddress2());
    address.setCity(form.getCity());
    address.setState(form.getState());
    address.setPostalCode(form.getPostalCode());
    address.setRegion(form.getRegion());
    address.setCountry(form.getCountry());
    address.setPhone(form.getPhone());
    address.setExt(form.getExt());
    address.setFax(form.getFax());

    customer.setAddress(address);
  }

}
