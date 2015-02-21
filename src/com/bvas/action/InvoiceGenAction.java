package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
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

import com.bvas.beans.AddressBean;
import com.bvas.beans.CustomerBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.InvoiceDetailsBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.InvoiceDetailsForm;
import com.bvas.formBeans.InvoiceGenForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.NumberUtils;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.UserException;

public class InvoiceGenAction extends Action {
  private static final Logger logger = Logger.getLogger(InvoiceGenAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    InvoiceGenForm invForm = (InvoiceGenForm) form;
    String buttonClicked = invForm.getButtonClicked();
    invForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in InvoiceGenAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int invoiceNo = 0;
    try {
      Object o = request.getParameter("invoiceNumber");
      if (o != null) {
        try {
          invoiceNo = Integer.parseInt((String) o);
          // session.setAttribute("InvoiceNoForGen", invoiceNo+"");
        } catch (NumberFormatException e) {

        }
      } else {
        invoiceNo = 0;
        // invoiceNo = Integer.parseInt((String)
        // session.getAttribute("InvoiceNoForGen"));
      }
    } catch (Exception e) {
      invoiceNo = 0;
      logger.error("In InvoiceGenAction - Invoice No Not Found" + e);
    }

    int invNoSession = 0;
    try {
      invNoSession = Integer.parseInt((String) session.getAttribute("InvoiceNoForGen"));
    } catch (Exception e) {
      // logger.error(e);
    }
    if (invNoSession != 0) {// put comments for allowing to change the
      // invoice no
      // This makes the invoice no locked to what ever is entered
      // previously
      // for update or what ever got for the new invoice no.
      invoiceNo = invNoSession;
    } // put comments for allowing to change the invoice no

    UserBean user = (UserBean) session.getAttribute("User");

    String custIdSelected = invForm.getCustomerId();
    int customerLevel = 0;
    boolean customerSetFirstTime = false;
    String cst = (String) session.getAttribute("CustomerSetInInvoice");

    boolean goodCredit = false;
    if (custIdSelected != null && !custIdSelected.trim().equals("")) {
      boolean avail = CustomerBean.isAvailable(custIdSelected);
      if (avail == false) {
        session.removeAttribute("CustomerSetInInvoice");
        errorBean.setError("This customer Not exists : Enter customer information first");
      } else {
        try {
          goodCredit = CustomerBean.getCustomer(custIdSelected).hasGoodCredit();
        } catch (Exception e) {
          logger.error(e);
          errorBean.setError(e.getMessage());
        }
        if (goodCredit) {
          session.setAttribute("CustSelected", custIdSelected);
        } else {
          errorBean.setError("*** TO MAKE INVOICES FOR THIS CUSTOMER CONTACT ACCOUNTING ***");
        }
        CustomerBean custBean = null;
        try {
          custBean = CustomerBean.getCustomer(custIdSelected);
        } catch (Exception e) {
          logger.error(e);
        }
        customerLevel = custBean.getCustomerLevel();
        if (custBean.getCreditBalance() == 0) {
          invForm.setCreditBalance("");
        } else {
          invForm.setCreditBalance(custBean.getCreditBalance() + "");
        }
        if (custBean.getPaymentTerms().equalsIgnoreCase("C")) {
          invForm.setPaymentTerms("COD");
        } else if (custBean.getPaymentTerms().equalsIgnoreCase("O")) {
          invForm.setPaymentTerms("CASH");
        } else if (custBean.getPaymentTerms().equalsIgnoreCase("B")) {
          invForm.setPaymentTerms("Bi-Wkly");
        } else if (custBean.getPaymentTerms().equalsIgnoreCase("M")) {
          invForm.setPaymentTerms("Mthly");
        } else if (custBean.getPaymentTerms().equalsIgnoreCase("W")) {
          invForm.setPaymentTerms("Wkly");
        }
        double writeOffAmount = CustomerBean.checkWriteOff(custIdSelected);
        if (writeOffAmount != 0) {
          session.setAttribute("WriteOffAmount", writeOffAmount + "");
        }
        if (cst == null || cst.trim().equals("")) {
          customerSetFirstTime = true;
        } else if (!cst.trim().equals(custIdSelected)) {
          customerSetFirstTime = true;
        }
        fillAddress(invForm, custIdSelected, customerSetFirstTime);
        session.setAttribute("CustomerSetInInvoice", custIdSelected);
      }
    } else {
      session.removeAttribute("CustomerSetInInvoice");
      errorBean.setError("You must enter a Customer Id");
    }

    String shipMethod = (String) request.getParameter("shippingMethods");
    if (shipMethod != null && !shipMethod.trim().equals("Select")) {
      session.setAttribute("ShipMethodSelected", shipMethod);
    } else {
      errorBean.setError("You must select a Shipping Method");
    }

    /*
     * if (session.getAttribute("InvoiceDetails") == null) { errorBean.setError(
     * "You must select a product <BR/> Select a product and get the information first" ); }
     */

    // Here the original Processing Starts
    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetOrUpdate")) {
      try {
        String forUpdate = (String) session.getAttribute("ForUpdate");
        // This Invoice is Available and it should be for change only no
        // create
        if (InvoiceBean.isAvailable(invoiceNo)) {

          if (forUpdate != null && forUpdate.trim().equals("Yes")) {
            getOrUpdate(request, errorBean, custIdSelected, customerLevel, customerSetFirstTime);
            applyTaxAndOthers(invoiceNo, invForm, request, custIdSelected, user, customerLevel,
                customerSetFirstTime);
          } else {
            try {
              InvoiceBean invBean = InvoiceBean.getInvoice(invoiceNo);
              fillInvoiceToForm(request, invForm, invBean, customerLevel);
              double writeOffAmount = CustomerBean.checkWriteOff(invBean.getCustomerId());
              if (writeOffAmount != 0) {
                session.setAttribute("WriteOffAmount", writeOffAmount + "");
              }

              errorBean = null;
              errorBean = new ErrorBean();
              session.setAttribute("InvoiceNoForGen", invoiceNo + "");
              session.setAttribute("OrigSalesPerson", invBean.getSalesPerson());
              session.setAttribute("CustomerSetInInvoice", custIdSelected);
              session.setAttribute("ForUpdate", "Yes");
              if (invBean.getStatus().trim().equals("C") || invBean.getStatus().trim().equals("W")
                  || invBean.getStatus().trim().equals("P")) {
                errorBean.setError("THIS INVOICE CANNOT BE CHANGED");
              }
              Connection con = DBInterfaceLocal.getSQLConnection();
              Statement stmt = con.createStatement();
              ResultSet rs =
                  stmt.executeQuery("Select Remarks1 from InvoiceHistory Where InvoiceNumber="
                      + invoiceNo + " And Remarks1 like 'Printed%' Order By ModifiedOrder ");
              while (rs.next()) {
                errorBean.setError(rs.getString(1));
              }
              rs.close();
              stmt.close();
              con.close();
            } catch (Exception e) {
              logger.error(e);
            }
          }
          // This Invoice is the new Invoice
        } else {
          // logger.error("I am Here" +
          // InvoiceBean.getNewInvoiceNo(user.getUsername()));
          // if (invoiceNo == 0) { //remove comments for allowing to
          // change the invoice no
          session.setAttribute("InvoiceNoForGen", InvoiceBean.getNewInvoiceNo(user.getUsername())
              + "");
          // } else { //remove comments for allowing to change the
          // invoice no
          // session.setAttribute("InvoiceNoForGen", invoiceNo+"");
          // //remove comments for allowing to change the invoice no
          // } //remove comments for allowing to change the invoice no
          getOrUpdate(request, errorBean, custIdSelected, customerLevel, customerSetFirstTime);
          applyTaxAndOthers(invoiceNo, invForm, request, custIdSelected, user, customerLevel,
              customerSetFirstTime);
        }
      } catch (UserException e) {
        errorBean.setError(e.getMessage());
        logger.error(e);
      }

      forwardPage = "InvoiceGen";
    }
    /*
     * else if (buttonClicked.equals("Apply")) { applyTaxAndOthers(invForm, request,
     * custIdSelected); forwardPage = "InvoiceGen"; }
     */
    else if (buttonClicked.equals("CreateNewInvoice")) {
      if (errorBean.getError() == null) {
        InvoiceBean invoice = null;
        try {
          // if (invoiceNo == 0 || InvoiceBean.isAvailable(invoiceNo))
          // {
          if (invoiceNo == 0) {
            invoiceNo = InvoiceBean.getNewInvoiceNo(user.getUsername());
            session.setAttribute("InvoiceNoForGen", invoiceNo + "");
          }
          invoice =
              createInvoice(invForm, invoiceNo, shipMethod, request, errorBean, user, "N",
                  customerLevel, customerSetFirstTime);
          if ((errorBean.getError() == null || errorBean.getError().trim().equals(""))
              && goodCredit) {
            invoice.addInvoice();
            errorBean.setError("INVOICE CREATED SUCCESSFULLY!!!");

            String userAgent = request.getHeader("User-Agent");
            boolean is60 = false;
            boolean createdPrint = false;
            createdPrint = PrintUtils.createInvoiceFor60(invoice);
            // createdPrint = PrintUtils.createNewInvoice(invoice);
            if (createdPrint) {
              logger.error("INVOICE PRINT CREATED SUCCESSFULLY :: " + invoice.getInvoiceNumber());
            } else {
              errorBean.setError("CANNOT CREATE INVOICE PRINT");
            }
            /*
             * if (userAgent.indexOf("MSIE 6.0") != -1) { PrintUtils.createInvoiceFor60(invoice); }
             * else { PrintUtils.createInvoice(invoice); }
             */

          } else {
            errorBean.setError("Please check the errors");
          }
        } catch (UserException e) {
          errorBean.setError(e.getMessage());
          logger.error(e);
        }
      } else {
        errorBean.setError("Please check the errors...");
      }
      session.removeAttribute("CustomerSetInInvoice");

      /*
       * try { this.wait(25000000); } catch (Exception e) { }
       */
      forwardPage = "InvoiceGen";
    } else if (buttonClicked.equals("PrintInvoice")) {
      if (invoiceNo != 0) {
        boolean avail = InvoiceBean.isAvailable(invoiceNo);
        if (avail) {
          logger.error("Going to print the back copy for the Invoice:" + invoiceNo);
          InvoiceBean.changeIsPrinted(invoiceNo);
          try {
            Connection con = DBInterfaceLocal.getSQLConnection();
            int ordCnt = 0;
            Statement stmt1 = con.createStatement();
            ResultSet rs1 =
                stmt1.executeQuery("Select count(*) From InvoiceHistory Where InvoiceNumber="
                    + invoiceNo);
            if (rs1.next()) {
              ordCnt = rs1.getInt(1) + 1;
              logger.error("Got Order Count:::" + ordCnt);
            } else {
              ordCnt = 1;
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yy h:mm a");
            String rmk1 =
                "Printed by " + user.getUsername() + " at "
                    + sdf.format(new java.util.Date(System.currentTimeMillis()));
            Statement stmt = con.createStatement();

            stmt.execute("Insert into InvoiceHistory (InvoiceNumber, ModifiedBy, ModifiedDate, ModifiedOrder, Remarks1) Values ("
                + invoiceNo
                + ", '"
                + user.getUsername()
                + "', '"
                + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
                + "', "
                + ordCnt
                + ", '" + rmk1 + "') ");
            rs1.close();
            stmt.close();
            stmt1.close();
            con.close();
          } catch (Exception e) {
            logger.error(e.getMessage());
          }
          try {
            java.io.File ff =
                new java.io.File("/tomcat/webapps/bvaschicago/html/reports/Invoice" + invoiceNo
                    + ".html");
            // ff.renameTo(new
            // java.io.File("/tomcat/webapps/BVAS/html/reports/invs/Invoice"+
            // invoiceNo + ".html"));
            ff.delete();
            // ff.close();
          } catch (Exception e) {
            logger.error(e.getMessage());
          }
        } else {
          errorBean.setError("You must create an Invoice first");
        }
      } else {
        errorBean.setError("No Invoice available");
      }
      session.setAttribute("isPrinting", "YES");
      forwardPage = "InvoiceGen";
    } else if (buttonClicked.equals("GetMoreParts")) {
      session.setAttribute("CustFromLookup", custIdSelected);
      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("InvoiceArch")) {
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("ChangeInvoice")) {
      String forUpdate = (String) session.getAttribute("ForUpdate");
      if (forUpdate == null || forUpdate.trim().equals("")) {
        errorBean
            .setError("YOU MUST GET THE INVOICE FIRST <BR/>Enter Invoice Number and Press 'Get Or Update' Button");
      } else {
        InvoiceBean oldInvoice = null;
        String origSalesPerson = (String) session.getAttribute("OrigSalesPerson");
        if (errorBean.getError() == null) {
          InvoiceBean invoice = null;
          String status = "";

          try {
            oldInvoice = InvoiceBean.getInvoice(invoiceNo);
            status = oldInvoice.getStatus();
            String cust = oldInvoice.getCustomerId();

            if (origSalesPerson == null) {
              errorBean.setError("Can Not Be Changed --- Original Sales Person Not Found");
            }
            if (!user.getRole().trim().equalsIgnoreCase("high")
                && !user.getRole().trim().equalsIgnoreCase("Acct")
                && !user.getUsername().trim().equalsIgnoreCase("Marcie")) {
              errorBean
                  .setError("Can Not Be Changed --- Invoices Will Be Changed Only By Accounting");
            }
            if ((status.trim().equalsIgnoreCase("C") || status.trim().equalsIgnoreCase("W"))
                && !user.getRole().trim().equalsIgnoreCase("high")) {
              errorBean.setError("Can Not Be Changed --- This Invoice Is Already Closed ");
            }
            if (!invForm.getSalesPerson().trim().equalsIgnoreCase(origSalesPerson.trim())) {
              invForm.setSalesPerson(origSalesPerson.trim());
              errorBean.setError("Can Not Be Changed --- Do Not Change The Sales Person's Name ");
            }

            // GETTING THE INVOICE FROM THE INFORMATION
            invoice =
                createInvoice(invForm, invoiceNo, shipMethod, request, errorBean, user, "M",
                    customerLevel, customerSetFirstTime);

            if (!invoice.getCustomerId().trim().equals(oldInvoice.getCustomerId().trim())) {
              errorBean.setError("Can Not Be Changed --- Do Not Change The Customer Id ");
            }

            if (!cutOffGood(invoice) && !user.getRole().trim().equalsIgnoreCase("high")) {
              throw new UserException(" *****  THIS INVOICE CANNOT BE CHANGED  ***** ");
            }
            if (goodCredit
                && (errorBean.getError() == null || errorBean.getError().trim().equals(""))) {
              // changing Invoice
              InvoiceBean.createHistory(user, oldInvoice, invoice);
              invoice.changeInvoice();
              if (status.trim().equalsIgnoreCase("W")) {
                Connection con = DBInterfaceLocal.getSQLConnection();
                Statement stmt = con.createStatement();
                stmt.executeQuery("Delete from WriteOff Where InvoiceNo=" + invoiceNo);
                stmt.close();
                con.close();
              }
              errorBean.setError("INVOICE CHANGED SUCCESSFULLY!!!");
              String userAgent = request.getHeader("User-Agent");
              boolean is60 = false;
              boolean createdPrint = false;
              createdPrint = PrintUtils.createInvoiceFor60(invoice);
              // createdPrint =
              // PrintUtils.createNewInvoice(invoice);
              if (createdPrint) {
                logger.error("INVOICE PRINT CHANGED SUCCESSFULLY :: " + invoice.getInvoiceNumber());
              } else {
                errorBean.setError("CANNOT CHANGE INVOICE PRINT");
              }
              // if (userAgent.indexOf("MSIE 6.0") != -1) {
              // PrintUtils.createInvoiceFor60(invoice);
              // } else {
              // PrintUtils.createInvoice(invoice);
              // }
            } else {
              errorBean.setError("Please check the errors");
            }
          } catch (Exception e) {
            errorBean.setError(e.getMessage());
            logger.error(e);
          }
        } else {
          errorBean.setError("Please check the errors...");
        }
      }
      session.removeAttribute("CustomerSetInInvoice");
      session.removeAttribute("ForUpdate");
      session.removeAttribute("WriteOffAmount");
      forwardPage = "InvoiceGen";
    } else if (buttonClicked.equals("ClearInvoice")) {
      session.removeAttribute("InvoiceDetails");
      session.removeAttribute("InvoiceNoForGen");
      session.removeAttribute("CustSelected");
      session.removeAttribute("ShipMethodSelected");
      session.removeAttribute("OrigSalesPerson");
      session.removeAttribute("ForUpdate");
      session.removeAttribute("WriteOffAmount");
      getServlet().getServletConfig().getServletContext().removeAttribute("AllCustomers");
      errorBean = null;
      errorBean = new ErrorBean();
      invForm.reset();
      session.removeAttribute("CustomerSetInInvoice");
      forwardPage = "InvoiceGen";
    } else if (buttonClicked.equals("ReturnToMain")) {
      session.removeAttribute("InvoiceDetails");
      session.removeAttribute("InvoiceNoForGen");
      session.removeAttribute("CustSelected");
      session.removeAttribute("ShipMethodSelected");
      session.removeAttribute("OrigSalesPerson");
      session.removeAttribute("ForUpdate");
      session.removeAttribute("CustomerSetInInvoice");
      session.removeAttribute("WriteOffAmount");
      getServlet().getServletConfig().getServletContext().removeAttribute("AllCustomers");
      errorBean = null;
      errorBean = new ErrorBean();
      invForm.reset();
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("InvoiceGenError", errorBean);
    } else {
      session.removeAttribute("InvoiceGenError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillInvoiceToForm(HttpServletRequest request, InvoiceGenForm invForm,
      InvoiceBean invBean, int customerLevel) {
    invForm.reset();

    HttpSession session = request.getSession(false);

    UserBean user = (UserBean) session.getAttribute("User");

    invForm.setAddressSame(false);

    CustomerBean custBean = null;
    try {
      custBean = CustomerBean.getCustomer(invBean.getCustomerId());
    } catch (Exception e) {
      logger.error(e);
    }
    if (custBean.getCreditBalance() == 0) {
      invForm.setCreditBalance("");
    } else {
      invForm.setCreditBalance(custBean.getCreditBalance() + "");
    }
    if (custBean.getPaymentTerms().equalsIgnoreCase("C")) {
      invForm.setPaymentTerms("COD");
    } else if (custBean.getPaymentTerms().equalsIgnoreCase("O")) {
      invForm.setPaymentTerms("CASH");
    } else if (custBean.getPaymentTerms().equalsIgnoreCase("B")) {
      invForm.setPaymentTerms("Bi-Wkly");
    } else if (custBean.getPaymentTerms().equalsIgnoreCase("M")) {
      invForm.setPaymentTerms("Mthly");
    } else if (custBean.getPaymentTerms().equalsIgnoreCase("W")) {
      invForm.setPaymentTerms("Wkly");
    }

    // No need to set the invoice number, because it will always come from
    // the session
    invForm.setCustomerId(invBean.getCustomerId());
    session.setAttribute("CustSelected", invBean.getCustomerId());
    invForm.setOrderDate(invBean.getOrderDate());
    invForm.setSalesPerson(invBean.getSalesPerson());
    invForm.setReturnedInvoice(invBean.getReturnedInvoice() + "");
    invForm.setNotes(invBean.getNotes());
    session.setAttribute("ShipMethodSelected", invBean.getShipVia());

    AddressBean billAddress = invBean.getBillToAddress();
    invForm.setBillAddress1(billAddress.getAddress1());
    invForm.setBillAddress2(billAddress.getAddress2());
    invForm.setBillAttention(invBean.getBillAttention());
    invForm.setBillCity(billAddress.getCity());
    invForm.setBillCountry(billAddress.getCountry());
    try {
      invForm.setBillCustomerName(CustomerBean.getCustomer(invBean.getCustomerId())
          .getCompanyName());
    } catch (UserException e) {
      invForm.setBillCustomerName("");
      logger.error(e);
    }
    invForm.setBillRegion(billAddress.getRegion());
    invForm.setBillState(billAddress.getState());
    invForm.setBillZip(billAddress.getPostalCode());

    AddressBean shipAddress = invBean.getShipToAddress();
    invForm.setShipAddress1(shipAddress.getAddress1());
    invForm.setShipAddress2(shipAddress.getAddress2());
    invForm.setShipAttention(invBean.getShipAttention());
    invForm.setShipCity(shipAddress.getCity());
    invForm.setShipCountry(shipAddress.getCountry());
    invForm.setShipCustomerName(invBean.getShipTo());
    invForm.setShipRegion(shipAddress.getRegion());
    invForm.setShipState(shipAddress.getState());
    invForm.setShipZip(shipAddress.getPostalCode());

    invForm.setAmountVowed(invBean.getBalance() + "");
    invForm.setDiscount(invBean.getDiscount() + "");
    invForm.setInvoiceTax(invBean.getTax() + "");
    invForm.setInvoiceTotal(invBean.getInvoiceTotal() + "");

    Hashtable<String, InvoiceDetailsForm> invoiceDetails =
        new Hashtable<String, InvoiceDetailsForm>();

    Vector<InvoiceDetailsBean> details = invBean.getInvoiceDetails();
    Enumeration<InvoiceDetailsBean> ennum = details.elements();
    while (ennum.hasMoreElements()) {
      InvoiceDetailsBean iBean = ennum.nextElement();
      try {
        InvoiceDetailsForm iForm =
            addNewPart(iBean.getPartNumber(), invBean.getCustomerId(), user, customerLevel);
        iForm.setQuantity(iBean.getQuantity());
        iForm.setCostPrice(iBean.getSoldPrice() + "");
        invoiceDetails.put(iBean.getPartNumber(), iForm);
      } catch (Exception e) {
        logger.error(e);
      }
    }

    // invoiceDetails.put("XXXXX", new InvoiceDetailsForm());
    session.setAttribute("InvoiceDetails", invoiceDetails);

  }

  public InvoiceBean createInvoice(InvoiceGenForm invForm, int invoiceNo, String shipMethod,
      HttpServletRequest request, ErrorBean errorBean, UserBean user, String status,
      int customerLevel, boolean customerSetFirstTime) throws UserException {
    InvoiceBean invoice = new InvoiceBean();
    invoice.setInvoiceNumber(invoiceNo);
    invoice.setOrderDate(invForm.getOrderDate());
    invoice.setCustomerId(invForm.getCustomerId());
    if (InvoiceBean.isAvailable(invoiceNo)) {
      InvoiceBean invBean = InvoiceBean.getInvoice(invoiceNo);
      String stat = invBean.getStatus();
      if ((stat.trim().equalsIgnoreCase("C") || stat.trim().equalsIgnoreCase("W") || stat.trim()
          .equalsIgnoreCase("P")) && !user.getRole().trim().equalsIgnoreCase("High")) {
        invoice.setStatus(stat.trim());
      } else {
        invoice.setStatus(status);
      }
      if (invBean.getDiscountType().trim().equals("")) {
        invoice.setDiscountType("G");
      } else {
        invoice.setDiscountType(invBean.getDiscountType());
      }

    } else {
      invoice.setDiscountType("G");
      invoice.setStatus(status);
    }

    try {
      if (user.getRole().trim().equalsIgnoreCase("High")
          || user.getRole().trim().equalsIgnoreCase("Acct")) {
        invoice.setReturnedInvoice(Integer.parseInt(invForm.getReturnedInvoice()));
      } else {
        invoice.setReturnedInvoice(0);
        invForm.setReturnedInvoice("");
      }
    } catch (Exception e) {
      invoice.setReturnedInvoice(0);
      logger.error(e);
    }
    invoice.setNotes(invForm.getNotes());
    invoice.setShipVia(shipMethod);
    // invoice.setSalesPerson(invForm.getSalesPerson());
    if (status.trim().equalsIgnoreCase("M")) {
      invoice.setSalesPerson(invForm.getSalesPerson());
    } else {
      invoice.setSalesPerson(user.getUsername());
    }

    getAmounts(invoice, invForm, request, user, customerLevel, customerSetFirstTime);

    invoice.setBillAttention(invForm.getBillAttention());

    AddressBean shipAddress = new AddressBean();
    AddressBean billAddress = new AddressBean();

    billAddress.setId(invForm.getCustomerId());
    billAddress.setType("Bill");
    billAddress.setWho("Cust");
    billAddress.setDateCreated(invForm.getOrderDate());
    billAddress.setInvoiceNumber(invoice.getInvoiceNumber());
    billAddress.setAddress1(invForm.getBillAddress1());
    billAddress.setAddress2(invForm.getBillAddress2());
    billAddress.setCity(invForm.getBillCity());
    billAddress.setState(invForm.getBillState());
    billAddress.setRegion(invForm.getBillRegion());
    billAddress.setPostalCode(invForm.getBillZip());
    billAddress.setCountry(invForm.getBillCountry());
    billAddress.setPhone("");
    billAddress.setExt("");
    billAddress.setFax("");

    boolean addressSame = invForm.getAddressSame();
    // logger.error("Address Same Checkbox: " + addressSame);
    if (addressSame) {
      invoice.setShipTo(invForm.getBillCustomerName());
      invoice.setShipAttention(invForm.getBillAttention());

      shipAddress = billAddress;
      shipAddress.setType("Ship");

    } else {
      invoice.setShipTo(invForm.getShipCustomerName());
      invoice.setShipAttention(invForm.getShipAttention());

      shipAddress.setId(invForm.getCustomerId());
      shipAddress.setType("Ship");
      shipAddress.setWho("Cust");
      shipAddress.setDateCreated(invForm.getOrderDate());
      shipAddress.setInvoiceNumber(invoice.getInvoiceNumber());
      shipAddress.setAddress1(invForm.getShipAddress1());
      shipAddress.setAddress2(invForm.getShipAddress2());
      shipAddress.setCity(invForm.getShipCity());
      shipAddress.setState(invForm.getShipState());
      shipAddress.setRegion(invForm.getShipRegion());
      shipAddress.setPostalCode(invForm.getShipZip());
      shipAddress.setCountry(invForm.getShipCountry());
      shipAddress.setPhone("");
      shipAddress.setExt("");
      shipAddress.setFax("");
    }

    Vector<InvoiceDetailsBean> invoiceDetails = null;
    invoiceDetails = createInvoiceDetails(invoiceNo, request, errorBean);

    invoice.setShipToAddress(shipAddress);
    invoice.setBillToAddress(billAddress);
    invoice.setInvoiceDetails(invoiceDetails);

    return invoice;
  }

  public Vector<InvoiceDetailsBean> createInvoiceDetails(int invNo, HttpServletRequest request,
      ErrorBean errorBean) {
    Vector<InvoiceDetailsBean> invoiceDetails = new Vector<InvoiceDetailsBean>();

    HttpSession session = request.getSession(false);
    Object o = session.getAttribute("InvoiceDetails");
    Hashtable ht = null;
    if (o != null) {
      ht = (Hashtable) o;
      Enumeration ennum = ht.elements();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsForm iForm = (InvoiceDetailsForm) ennum.nextElement();
        InvoiceDetailsBean iBean = new InvoiceDetailsBean();
        String partNo = iForm.getPartNo();
        if (partNo != null && (!partNo.trim().equals(""))) {
          iBean.setInvoiceNumber(invNo);
          iBean.setPartNumber(partNo);
          iBean.setQuantity(iForm.getQuantity());
          //RBRT review get cost to be precise
          iBean.setSoldPrice(Double.parseDouble(iForm.getCostPrice()));
          iBean.setListPrice(Double.parseDouble(iForm.getListPrice()));
          if (iBean.getQuantity() == 0) {
            errorBean.setError("Quantity is zero for the Part: " + iBean.getPartNumber());
          }

          invoiceDetails.add(iBean);
        }

      }
    } else {
      errorBean.setError("Select a product and get the information first");
    }

    return invoiceDetails;
  }

  public void applyTaxAndOthers(int invoiceNo, InvoiceGenForm invForm, HttpServletRequest request,
      String CustId, UserBean user, int customerLevel, boolean customerSetFirstTime)
      throws UserException {

    HttpSession session = request.getSession(false);
    Object o = session.getAttribute("InvoiceDetails");
    Hashtable invoiceDetails = null;
    if (o != null) {
      invoiceDetails = (Hashtable) o;
      Enumeration ennum = invoiceDetails.keys();
      double invoiceTotal = 0.0;
      double invoiceTax = 0.0;

      boolean isThereReturnedParts = false;
      int returnedInvoice = 0;
      try {
        if (!user.getRole().trim().equalsIgnoreCase("High")
            && !user.getRole().trim().equalsIgnoreCase("Acct")) {
          invForm.setReturnedInvoice("");
        } else {
          returnedInvoice = Integer.parseInt(invForm.getReturnedInvoice());
          InvoiceBean invv = InvoiceBean.getInvoice(returnedInvoice);
          double disc = 0;
          try {
            disc = Double.parseDouble(invForm.getDiscount());
          } catch (Exception e) {
            // logger.error(e);
          }
          if (invv.getDiscount() != 0 && disc == 0) {
            invForm.setDiscount((invv.getDiscount() * -1) + "");
          }
        }
      } catch (Exception e) {
        // logger.error(e);
      }

      while (ennum.hasMoreElements()) {
        String partNo = "";
        Object obj = ennum.nextElement();
        if (obj != null) {
          partNo = (String) obj;
        }
        if (partNo != null && (!partNo.trim().equals(""))) {
          InvoiceDetailsForm form = null;
          Object ob = invoiceDetails.get(partNo);
          if (ob != null) {
            form = (InvoiceDetailsForm) ob;
            double costPrice = 0.0;
            double listPrice = 0.0;
            int quantity = 0;
            try {
              costPrice = Double.parseDouble(form.getCostPrice());
              listPrice = Double.parseDouble(form.getListPrice());
              if (!partNo.startsWith("XX")) {
                double costFromPart = PartsBean.getPart(partNo, null).getCostPrice(customerLevel);
                String forUpdate = (String) session.getAttribute("ForUpdate");
                if (forUpdate == null)
                  forUpdate = "";
                if ((costPrice != costFromPart && customerSetFirstTime && !forUpdate.trim()
                    .equalsIgnoreCase("Yes"))
                    || (costPrice < costFromPart && !user.getRole().trim().equalsIgnoreCase("High"))) {
                  costPrice = costFromPart;
                  form.setCostPrice(costPrice + "");
                }
                double listFromPart = PartsBean.getPart(partNo, null).getListPrice();
                if (listPrice < listFromPart && !user.getRole().trim().equalsIgnoreCase("High")) {
                  listPrice = listFromPart;
                  form.setListPrice(listPrice + "");
                }
              }
            } catch (Exception e) {
              costPrice = 0.0;
              logger.error(e);
            }
            try {
              quantity = form.getQuantity();
            } catch (Exception e) {
              quantity = 0;
              logger.error(e);
            }

            // This is for handling returns. Quantity never less
            // than zero, except for returns
            if (quantity < 0) {
              if (user.getRole().trim().equalsIgnoreCase("High")
                  || user.getRole().trim().equalsIgnoreCase("Acct")) {
                isThereReturnedParts = true;
                if (returnedInvoice != 0
                    && !InvoiceBean.isAvailableInInvoice(returnedInvoice, partNo, quantity)) {
                  throw new UserException("*****  THE PART " + partNo
                      + " IS NOT AVAILABLE IN THE INVOICE " + returnedInvoice + "  ***** ");
                } else if (returnedInvoice != 0
                    && InvoiceBean.isDoubleReturn(invoiceNo, returnedInvoice, partNo, quantity)) {
                  throw new UserException("*****  THE PART " + partNo
                      + " ALREADY HAS A RETURNED INVOICE  ***** ");
                } else if (returnedInvoice != 0) {
                  Vector<InvoiceDetailsBean> v =
                      InvoiceBean.getInvoice(returnedInvoice).getInvoiceDetails();
                  Enumeration<InvoiceDetailsBean> ennumXX = v.elements();
                  while (ennumXX.hasMoreElements()) {
                    InvoiceDetailsBean idt = ennumXX.nextElement();
                    if (idt.getPartNumber().trim().equalsIgnoreCase(partNo)) {
                      double cst = idt.getSoldPrice();
                      if (cst != 0 && cst != costPrice) {
                        costPrice = cst;
                        form.setCostPrice(costPrice + "");
                      }
                      break;
                    }
                  }
                }
              } else {
                quantity = quantity * -1;
                form.setQuantity(quantity);
                invForm.setReturnedInvoice("");
              }
            }

            double tot = costPrice * quantity;
            if (partNo.startsWith("XX")) {
              tot = tot * -1;
            }
            invoiceTotal += tot;
          }
        }
      }
      if (invoiceTotal != 0.0 && invoiceTotal < 1000) {
        // logger.error("Error in first place");
        // invoiceTotal = NumberUtils.cutFractions(invoiceTotal);
        invoiceTotal = Double.parseDouble(NumberUtils.cutFractions(invoiceTotal + ""));
      } else if (invoiceTotal >= 1000) {
        invoiceTotal = Double.parseDouble(NumberUtils.cutFractions(invoiceTotal + ""));

      }
      double discount = 0.0;
      double amountVowed = 0.0;
      try {
        discount = Double.parseDouble(invForm.getDiscount());
      } catch (Exception e) {
        discount = 0.0;

      }
      logger.error(user.getRole() + "-----" + user.getUsername());
      /*
       * if (discount != 0 && !user.getRole().trim().equalsIgnoreCase("High") &&
       * !user.getUsername().trim().equalsIgnoreCase("Bob")) { discount = 0; throw new UserException
       * ("Please do not add discount"); }
       */
      String taxId = CustomerBean.getTaxId(CustId);
      if (taxId != null && !taxId.trim().equals("") && taxId.trim().equals("Y")) {
        invoiceTax = 0.0;
      } else {
        // Changed on 8/1/09
        invoiceTax = (invoiceTotal - discount) * 0.0925;
        // if (DateUtils.getNewUSDateForInvoice().equals("07-01-2005"))
        // {
        // invoiceTax = (invoiceTotal - discount) * 0.0900;
        // } else {
        // invoiceTax = (invoiceTotal - discount) * 0.0875;
        // }
      }
      if (invoiceTax != 0) {
        invoiceTax = NumberUtils.cutFractions(invoiceTax);
      }
      amountVowed = invoiceTotal + invoiceTax - discount;
      if (amountVowed > 0 && amountVowed < 1000) {
        amountVowed = NumberUtils.cutFractions(amountVowed);
      } else {
        amountVowed = Double.parseDouble(NumberUtils.cutFractions(amountVowed + ""));
      }
      invForm.setInvoiceTotal(invoiceTotal + "");
      invForm.setInvoiceTax(invoiceTax + "");
      invForm.setDiscount(discount + "");
      invForm.setAmountVowed(amountVowed + "");

      // This is for checking original invoice when there are returns
      if (isThereReturnedParts && returnedInvoice == 0) {
        throw new UserException(
            "You must enter the original Invoice Number, </br>when there are returned parts");
      }

    }

  }

  public void getAmounts(InvoiceBean invoice, InvoiceGenForm invForm, HttpServletRequest request,
      UserBean user, int customerLevel, boolean customerSetFirstTime) throws UserException {

    HttpSession session = request.getSession(false);
    Object o = session.getAttribute("InvoiceDetails");
    Hashtable invoiceDetails = null;
    if (o != null) {
      invoiceDetails = (Hashtable) o;
      Enumeration ennum = invoiceDetails.keys();
      double invoiceTotal = 0.0;
      double invoiceTax = 0.0;
      boolean isThereReturnedParts = false;
      while (ennum.hasMoreElements()) {
        String partNo = "";
        Object obj = ennum.nextElement();
        if (obj != null) {
          partNo = (String) obj;
        }
        if (partNo != null && (!partNo.trim().equals(""))) {
          InvoiceDetailsForm form = null;
          Object ob = invoiceDetails.get(partNo);
          if (ob != null) {
            form = (InvoiceDetailsForm) ob;
            double costPrice = 0.0;
            double listPrice = 0.0;
            int quantity = 0;
            try {
              listPrice = Double.parseDouble(form.getListPrice());
              costPrice = Double.parseDouble(form.getCostPrice());
              if (invoice.getCustomerId().trim().equals("1111111111")) {
                if (listPrice < PartsBean.getPart(partNo, null).getListPrice()
                    && !user.getRole().trim().equalsIgnoreCase("High")) {
                  listPrice = PartsBean.getPart(partNo, null).getListPrice();
                }
                costPrice = listPrice * 0.8;
                form.setCostPrice(NumberUtils.cutFractions(costPrice) + "");
                form.setListPrice(listPrice + "");

              } else {
                if (!partNo.startsWith("XX")) {
                  double costFromPart = PartsBean.getPart(partNo, null).getCostPrice(customerLevel);
                  String forUpdate = (String) session.getAttribute("ForUpdate");
                  if (forUpdate == null)
                    forUpdate = "";
                  if ((costPrice != costFromPart && customerSetFirstTime && !forUpdate.trim()
                      .equalsIgnoreCase("Yes"))
                      || (costPrice < costFromPart && !user.getRole().trim()
                          .equalsIgnoreCase("High"))) {
                    costPrice = costFromPart;
                    form.setCostPrice(costPrice + "");
                  }
                  double listFromPart = PartsBean.getPart(partNo, null).getListPrice();
                  if (listPrice < listFromPart && !user.getRole().trim().equalsIgnoreCase("High")) {
                    listPrice = listFromPart;
                    form.setListPrice(listPrice + "");
                  }
                }
              }
            } catch (Exception e) {
              costPrice = 0.0;
              logger.error(e);
            }
            try {
              quantity = form.getQuantity();
            } catch (Exception e) {
              quantity = 0;
              logger.error(e);
            }

            // This is for handling returns. Quantity never less
            // than zero, except for returns
            if (quantity < 0) {
              if (user.getRole().trim().equalsIgnoreCase("High")
                  || user.getRole().trim().equalsIgnoreCase("Acct")) {
                isThereReturnedParts = true;
                if (invoice.getReturnedInvoice() != 0
                    && !InvoiceBean.isAvailableInInvoice(invoice.getReturnedInvoice(), partNo,
                        quantity)) {
                  throw new UserException("*****  THE PART " + partNo
                      + " IS NOT AVAILABLE IN THE INVOICE " + invoice.getReturnedInvoice()
                      + "  ***** ");
                } else if (invoice.getReturnedInvoice() != 0
                    && InvoiceBean.isDoubleReturn(invoice.getInvoiceNumber(),
                        invoice.getReturnedInvoice(), partNo, quantity)) {
                  throw new UserException("*****  THE PART " + partNo
                      + " ALREADY HAS A RETURNED INVOICE  ***** ");
                } else if (invoice.getReturnedInvoice() != 0) {
                  Vector<InvoiceDetailsBean> v =
                      InvoiceBean.getInvoice(invoice.getReturnedInvoice()).getInvoiceDetails();
                  Enumeration<InvoiceDetailsBean> ennumXX = v.elements();
                  while (ennumXX.hasMoreElements()) {
                    InvoiceDetailsBean idt = ennumXX.nextElement();
                    if (idt.getPartNumber().trim().equalsIgnoreCase(partNo)) {
                      double cst = idt.getSoldPrice();
                      if (cst != 0 && cst != costPrice) {
                        costPrice = cst;
                        form.setCostPrice(costPrice + "");
                      }
                      break;
                    }
                  }
                }
              } else {
                quantity = quantity * -1;
                form.setQuantity(quantity);
                invoice.setReturnedInvoice(0);
                invForm.setReturnedInvoice("");
              }
            }

            double tot = costPrice * quantity;

            // For handling damaged discount part
            if (partNo.startsWith("XX")) {
              tot = tot * -1;
            }

            invoiceTotal += tot;
          }
        }
      }
      if (invoiceTotal < 1000) {
        invoiceTotal = Double.parseDouble(NumberUtils.cutFractions(invoiceTotal + ""));
      } else {
        invoiceTotal = Double.parseDouble(NumberUtils.cutFractions(invoiceTotal + ""));
      }
      double discount = 0.0;
      double amountVowed = 0.0;
      try {
        discount = Double.parseDouble(invForm.getDiscount());
        if (discount == 0 && invoice.getReturnedInvoice() != 0) {
          discount = (InvoiceBean.getInvoice(invoice.getReturnedInvoice()).getDiscount() * -1);
          invForm.setDiscount(discount + "");
        }
      } catch (Exception e) {
        discount = 0.0;
        logger.error(e);
      }
      /*
       * if (discount != 0 && !user.getRole().trim().equalsIgnoreCase("High")) { discount = 0; throw
       * new UserException (
       * "You are not authorized to give discount!!!\nContact your manager instead....." ); }
       */

      // logger.error("invoiceTax"+invoiceTax);
      // if (invoiceTax != 0) {
      // invoiceTax =
      // Double.parseDouble(NumberUtils.cutFractions(invoiceTax+""));
      // }
      // logger.error("invoiceTax"+invoiceTax);
      // logger.error("invoiceTotal"+invoiceTotal);
      // logger.error("discount"+discount);
      String taxId = CustomerBean.getTaxId(invoice.getCustomerId());
      if (taxId != null && !taxId.trim().equals("") && taxId.trim().equals("Y")) {
        invoiceTax = 0.0;
      } else {
        // Changed on 8/1/09
        invoiceTax = (invoiceTotal - discount) * 0.0925;
        // if (DateUtils.getNewUSDateForInvoice().equals("07-01-2005"))
        // {
        // invoiceTax = (invoiceTotal - discount) * 0.0900;
        // } else {
        // invoiceTax = (invoiceTotal - discount) * 0.0875;
        // }
      }
      if (invoiceTax != 0) {
        invoiceTax = NumberUtils.cutFractions(invoiceTax);
      }
      amountVowed = invoiceTotal + invoiceTax - discount;
      // if (amountVowed != invoiceTotal + invoiceTax - discount) {
      // logger.error("Difference is:" + (amountVowed -
      // (invoiceTotal + invoiceTax - discount)));
      // }
      // logger.error("amountVowed"+amountVowed);
      if (amountVowed != 0) {
        try {
          if (amountVowed > 0 && amountVowed < 1000) {
            amountVowed = NumberUtils.cutFractions(amountVowed);
          } else {
            amountVowed = Double.parseDouble(NumberUtils.cutFractions(amountVowed + ""));
          }
        } catch (Exception e) {
          logger.error(e);
        }
      }
      // logger.error("amountVowed"+amountVowed);
      invoice.setInvoiceTotal(invoiceTotal);
      invoice.setTax(invoiceTax);
      invoice.setDiscount(discount);
      invoice.setBalance(amountVowed);
      invForm.setInvoiceTotal(invoiceTotal + "");
      invForm.setInvoiceTax(invoiceTax + "");
      invForm.setDiscount(discount + "");
      invForm.setAmountVowed(amountVowed + "");

      // This is for checking original invoice when there are returns
      if (isThereReturnedParts && invoice.getReturnedInvoice() == 0) {
        throw new UserException(
            "You must enter the original Invoice Number, </br>when there are returned parts");
      }

    }

  }

  public void getOrUpdate(HttpServletRequest request, ErrorBean errorBean, String custIdSelected,
      int customerLevel, boolean customerSetFirstTime) {

    HttpSession session = request.getSession(false);
    session.removeAttribute("InvoiceDetails");
    Hashtable<String, InvoiceDetailsForm> invoiceDetails =
        new Hashtable<String, InvoiceDetailsForm>();

    UserBean user = (UserBean) session.getAttribute("User");

    int partCount = 0;
    boolean partsAvail = false;
    while (true) {
      partCount++;
      String partNo = (String) request.getParameter("PartNo" + partCount);
      String makeModelName = (String) request.getParameter("MakeModelName" + partCount);
      String partDescription = (String) request.getParameter("PartDescription" + partCount);
      String year = (String) request.getParameter("Year" + partCount);
      String unitsInStockObj = (String) request.getParameter("UnitsInStock" + partCount);
      String costPriceObj = (String) request.getParameter("CostPrice" + partCount);
      String listPriceObj = (String) request.getParameter("ListPrice" + partCount);
      String quantityObj = (String) request.getParameter("Quantity" + partCount);

      InvoiceDetailsForm iForm = null;

      if (partNo == null || partNo.trim().equals("")) {
        break;
      } else {
        partsAvail = true;
        iForm = new InvoiceDetailsForm();
      }

      // logger.error("PartNo = "+partNo);

      int quantity = 0;
      try {
        quantity = Integer.parseInt(quantityObj);
      } catch (Exception e) {
        quantity = 1;
        // logger.error(e);
      }

      double costPrice = 0.0;
      try {
        costPrice = Double.parseDouble(costPriceObj);
      } catch (Exception e) {
        costPrice = 0.0;
        // logger.error(e);
      }
      double listPrice = 0.0;
      try {
        listPrice = Double.parseDouble(listPriceObj);
      } catch (Exception e) {
        listPrice = 0.0;
        // logger.error(e);
      }

      int unitsInStock = 0;
      try {
        unitsInStock = Integer.parseInt(unitsInStockObj);
      } catch (Exception e) {
        unitsInStock = 0;
        // logger.error(e);
      }

      if ((makeModelName == null || makeModelName.trim().equals(""))
          && (partDescription == null || partDescription.trim().equals("")) && unitsInStock == 0) {
        // logger.error("This is a new Part");
        try {
          iForm = addNewPart(partNo, custIdSelected, user, customerLevel);
          iForm.setQuantity(quantity);
          invoiceDetails.put(partNo, iForm);
        } catch (Exception e) {
          errorBean.setError(e.getMessage());
          logger.error(e);
        }
      } else {
        // logger.error("This is the old Part");
        iForm.setPartNo(partNo);
        iForm.setPartDescription(partDescription);
        iForm.setMakeModelName(makeModelName);
        iForm.setYear(year);
        iForm.setUnitsInStock(unitsInStock + "");

        // No body can change the listPrice for the damaged discount
        // part
        /*
         * if (partNo.startsWith("XX")) { listPrice = 0.0; }
         */

        double listP = listPrice;
        double costP = 0.0;
        if (custIdSelected != null && custIdSelected.trim().equals("1111111111") && listP != 0) {
          costP = listP * 0.8;
          iForm.setCostPrice(NumberUtils.cutFractions(costP) + "");
          iForm.setListPrice(listP + "");
        } else {
          if (!partNo.startsWith("XX")) {
            double costFromPart = PartsBean.getPart(partNo, null).getCostPrice(customerLevel);
            String forUpdate = (String) session.getAttribute("ForUpdate");
            if (forUpdate == null)
              forUpdate = "";
            if ((costPrice != costFromPart && customerSetFirstTime && !forUpdate.trim()
                .equalsIgnoreCase("Yes"))
                || (costPrice < costFromPart && !user.getRole().trim().equalsIgnoreCase("High"))) {
              iForm.setCostPrice(costFromPart + "");
            } else {
              iForm.setCostPrice(costPrice + "");
            }
            double listFromPart = PartsBean.getPart(partNo, null).getListPrice();
            if (listPrice < listFromPart && !user.getRole().trim().equalsIgnoreCase("High")) {
              iForm.setListPrice(listFromPart + "");
            } else {
              iForm.setListPrice(listPrice + "");
            }
          } else {
            iForm.setCostPrice(costPrice + "");
            iForm.setListPrice(listPrice + "");
          }
        }
        iForm.setQuantity(quantity);

        // For removing the items when quantity is zero(0)
        if (quantity != 0) {
          invoiceDetails.put(partNo, iForm);
        }
      }

    }
    if (partsAvail) {
      // invoiceDetails.put("XXXXX", new InvoiceDetailsForm());
      session.setAttribute("InvoiceDetails", invoiceDetails);
    }

  }

  /*
   * This method creates the new Invoice Details Form and adds Parts to the form
   */
  public InvoiceDetailsForm addNewPart(String partNo, String custIdSelected, UserBean user,
      int customerLevel) throws UserException {
    InvoiceDetailsForm form = new InvoiceDetailsForm();

    PartsBean part = null;

    // This is for handling Damaged Discount for Parts
    if (!partNo.toUpperCase().startsWith("XX")) {
      part = PartsBean.getPart(partNo, null);
      if (part != null) {
        form.setParts(part, customerLevel);
        double listPrice = part.getListPrice();
        double costPrice = 0.0;
        // ******************** REMOVE THESE LINES LATER AND REMOVE
        // COMMENTS LATER
        costPrice = part.getCostPrice(customerLevel);
        form.setCostPrice(costPrice + "");
        // *****************
        // FOR CALCULATING THE 20 % FOR ALL WALK-IN CUSTOMERS
        if (custIdSelected != null && custIdSelected.trim().equals("1111111111") && listPrice != 0) {
          costPrice = listPrice * 80 / 100;
          form.setCostPrice(NumberUtils.cutFractions(costPrice) + "");
        }

      } else {
        throw new UserException("The part " + partNo + " Not exists");
      }
    } else {

      /*
       * if (!user.getRole().trim().equalsIgnoreCase("high") &&
       * !user.getUsername().trim().equalsIgnoreCase("Bob")) { throw new UserException("The part " +
       * partNo + " Not exists"); }
       */
      form.setPartNo(partNo.toUpperCase());
      form.setMakeModelName("");
      form.setYear("");
      form.setUnitsInStock("");
      form.setQuantity(1);
      form.setCostPrice("0.0");
      form.setListPrice("0.0");
      form.setPartDescription("DAMAGED DISCOUNT FOR " + partNo.substring(2).toUpperCase());

    }

    return form;
  }

  // THIS METHOD IS USED FOR MODIFYING THE INVOICE.
  // INVOICES CANNOT BE MODIFIED FROM THE NEXT DAY ONWARDS...
  public boolean cutOffGood(InvoiceBean invoice) {
    long invTime = invoice.getInvoiceTime();
    long timeToAdd = 86400000;
    Calendar cc = java.util.Calendar.getInstance();
    cc.setTime(new java.util.Date(invTime));
    if (cc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
      timeToAdd *= 2;
    }

    // logger.error(new java.util.Date(invTime).toString());
    // logger.error("Day Of Week:" + cc.get(cc.DAY_OF_WEEK) + "  :" +
    // Calendar.SATURDAY);
    // logger.error(invTime);
    // logger.error(System.currentTimeMillis());
    // logger.error(invoice.getOrderDate());
    // logger.error(DateUtils.getNewUSDate());
    // logger.error(DateUtils.getNewUSDateForInvoice());

    if (invTime != 0 && (System.currentTimeMillis() < invTime + timeToAdd)) {
      return true;
    } else if (invoice.getOrderDate().trim().equals(DateUtils.getNewUSDate())
        || invoice.getOrderDate().trim().equals(DateUtils.getNewUSDateForInvoice())) {
      return true;
    } else {
      return false;
    }
    // int invMonth =
    // Integer.parseInt(invoice.getOrderDate().substring(0,2));
    // logger.error("InvMonth="+invMonth);
    // Calendar cc = java.util.Calendar.getInstance();
    // cc.setTime(new java.util.Date());
    // int mm = cc.get(cc.MONTH) + 1;
    // logger.error("MM="+mm);
    // if (invMonth == mm) {
    // return true;
    // } else {
    // return false;
    // }
  }

  public void fillAddress(InvoiceGenForm invForm, String custIdSelected, boolean custSetFirstTime) {
    AddressBean address = null;
    try {
      address = AddressBean.getAddress(custIdSelected, "Standard", "Cust", "", 0);
    } catch (Exception e) {
      address = new AddressBean();
      logger.error(e);
    }

    if (custSetFirstTime) {
      if (address.getAddress1() != null) {
        invForm.setBillAddress1(address.getAddress1());
      } else {
        invForm.setBillAddress1("");
      }
      if (address.getAddress2() != null) {
        invForm.setBillAddress2(address.getAddress2());
      } else {
        invForm.setBillAddress2("");
      }
      if (address.getCity() != null) {
        invForm.setBillCity(address.getCity());
      } else {
        invForm.setBillCity("");
      }
      if (address.getRegion() != null) {
        invForm.setBillRegion(address.getRegion());
      } else {
        invForm.setBillRegion("");
      }
      if (address.getState() != null) {
        invForm.setBillState(address.getState());
      } else {
        invForm.setBillState("");
      }
      if (address.getPostalCode() != null) {
        invForm.setBillZip(address.getPostalCode());
      } else {
        invForm.setBillZip("");
      }
      if (address.getCountry() != null) {
        invForm.setBillCountry(address.getCountry());
      } else {
        invForm.setBillCountry("");
      }
      // invForm.setAttention(address.getAttention());
    } else {
      if (invForm.getBillAddress1() == null || invForm.getBillAddress1().trim().equals("")) {
        if (address.getAddress1() != null) {
          invForm.setBillAddress1(address.getAddress1());
        }
      }
      if (invForm.getBillAddress2() == null || invForm.getBillAddress2().trim().equals("")) {
        if (address.getAddress2() != null) {
          invForm.setBillAddress2(address.getAddress2());
        }
      }
      if (invForm.getBillCity() == null || invForm.getBillCity().trim().equals("")) {
        if (address.getCity() != null) {
          invForm.setBillCity(address.getCity());
        }
      }
      if (invForm.getBillRegion() == null || invForm.getBillRegion().trim().equals("")) {
        if (address.getRegion() != null) {
          invForm.setBillRegion(address.getRegion());
        }
      }
      if (invForm.getBillState() == null || invForm.getBillState().trim().equals("")) {
        if (address.getState() != null) {
          invForm.setBillState(address.getState());
        }
      }
      if (invForm.getBillZip() == null || invForm.getBillZip().trim().equals("")) {
        if (address.getPostalCode() != null) {
          invForm.setBillZip(address.getPostalCode());
        }
      }
      if (invForm.getBillCountry() == null || invForm.getBillCountry().trim().equals("")) {
        if (address.getCountry() != null) {
          invForm.setBillCountry(address.getCountry());
        }
      }
    }
  }

}
