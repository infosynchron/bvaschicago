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
import com.bvas.beans.UserBean;
import com.bvas.formBeans.ClientMaintForm;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class ClientMaintAction extends Action {
  private static final Logger logger = Logger.getLogger(ClientMaintAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    ClientMaintForm custForm = (ClientMaintForm) form;
    String buttonClicked = custForm.getButtonClicked();
    custForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    UserBean user = null;
    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MainMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    } else {
      user = (UserBean) session.getAttribute("User");
    }

    String custId = custForm.getClientId();
    String custName = custForm.getCompanyName();
    String idOrName = "";
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
      if (custId != null && (!custId.trim().equals(""))) {
        idOrName = custId;
      } else if (custName != null && (!custName.trim().equals(""))) {
        idOrName = custName;
      } else {
        idOrName = "";
        errorBean.setError("Enter Customer Id or Name and press 'Get...' button");
      }

      if (!idOrName.trim().equals("")) {
        try {
          custBean = CustomerBean.getCustomer(idOrName);
          if (custBean != null) {
            fillForm(custForm, custBean);
            session.setAttribute("Customer", custBean);
          } else {
            errorBean.setError("Error: Please check your Customer ID");
          }
        } catch (UserException e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
        }

      }

      forwardPage = "ClientMaint";
    } else if (buttonClicked.equals("AddNewClient")) {
      try {
        if (custId != null && (!custId.trim().equals(""))) {
          custBean = new CustomerBean();
          custBean.setCustomerId(custForm.getClientId());
          fillBean(custForm, custBean, user);
          custBean.addNewCustomer();
          session.removeAttribute("Customer");
          session.setAttribute("Customer", custBean);
          errorBean.setError("Customer added Successfully");
        } else {
          errorBean.setError("Enter Customer ID");;
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "ClientMaint";
    } else if (buttonClicked.equals("ChangeClient")) {
      try {
        if (custBean != null) {

          fillBean(custForm, custBean, user);
          custBean.changeCustomer();
          session.setAttribute("Customer", custBean);

        } else {
          errorBean
              .setError("Customer Not available, <BR/>Get the customer first using the 'Get...' Buttom");;
        }
        errorBean.setError("Customer changed Successfully");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "ClientMaint";
    } else if (buttonClicked.equals("DeleteClient")) {
      try {
        if (custBean != null) {

          custBean.deleteCustomer();
          custForm.resetForm();

        } else {
          errorBean.setError("No Customer Found - Get the customer first using 'Get...' button");
        }
        errorBean.setError("Customer deleted Successfully");
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }

      forwardPage = "ClientMaint";
    } else if (buttonClicked.equals("ClearClient")) {
      forwardPage = "ClientMaint";
      session.removeAttribute("Customer");
      custForm.resetForm();
    } else if (buttonClicked.equals("PrintClientListing")) {
      logger.error("---Going to print Client Listing---");
      forwardPage = "ClientMaint";
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
      session.removeAttribute("Customer");
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("ClientMaintError", errorBean);
    } else {
      session.removeAttribute("ClientMaintError");
    }
    return (mapping.findForward(forwardPage));

  }

  public void fillForm(ClientMaintForm custForm, CustomerBean custBean) {
    custForm.setClientId(custBean.getCustomerId());
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
    if (custBean.getPaymentTerms() != null && !custBean.getPaymentTerms().trim().equals("")) {
      custForm.setPaymentTerms(custBean.getPaymentTerms());
    }
    if (custBean.getCreditBalance() == 0) {
      custForm.setCreditBalance("");
    } else {
      custForm.setCreditBalance(custBean.getCreditBalance() + "");
    }
    if (custBean.getCreditLimit() == 0) {
      custForm.setCreditLimit("");
    } else {
      custForm.setCreditLimit(custBean.getCreditLimit() + "");
    }

    custForm.setCustomerLevel(custBean.getCustomerLevel() + "");

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

  public void fillBean(ClientMaintForm form, CustomerBean customer, UserBean user) {
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
    for (int i = 0; i < form.getNotes().length(); i++) {
      String xx = form.getNotes().substring(i, i + 1);
      if (xx.matches("\n") == true) {
        form.setNotes(form.getNotes().substring(0, i - 1) + " " + form.getNotes().substring(i + 1));
      }
    }
    customer.setNotes(form.getNotes());
    if ((user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim()
        .equalsIgnoreCase("Acct"))
        && (!user.getUsername().trim().equalsIgnoreCase("Marcie"))) {
      try {
        customer.setPaymentTerms(form.getPaymentTerms());
      } catch (Exception e) {
        logger.error(e);
        logger.error("Exception:::" + e);
      }
    }
    if (user.getRole().trim().equalsIgnoreCase("High")) {
      try {
        customer.setCreditLimit(Double.parseDouble(form.getCreditLimit()));
      } catch (Exception e) {
        logger.error(e);
      }
    }
    if (customer.getCreditLimit() == 0) {
      customer.setCreditLimit(1000.00);
    }
    if (user.getRole().trim().equalsIgnoreCase("High")) {
      try {
        customer.setCustomerLevel(Integer.parseInt(form.getCustomerLevel()));
      } catch (Exception e) {
        logger.error(e);
        customer.setCustomerLevel(0);
      }
    }

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
