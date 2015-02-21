package com.bvas.action;

import java.io.IOException;
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

import com.bvas.beans.CustomerBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;
import com.bvas.beans.VendorOrderBean;
import com.bvas.formBeans.InvenAvailForm;
import com.bvas.formBeans.InvoiceDetailsForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.NumberUtils;

public class InvenAvailAction extends Action {
  private static final Logger logger = Logger.getLogger(InvenAvailAction.class);

  @SuppressWarnings("static-access")
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    InvenAvailForm invenForm = (InvenAvailForm) form;
    String buttonClicked = invenForm.getButtonClicked();
    invenForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    String custIdSelected = (String) session.getAttribute("CustFromLookup");
    if (custIdSelected == null) {
      custIdSelected = "";
    }

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in InvenAvailAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("AddToOrder")) {

      String partNo = invenForm.getPartNo();
      String interNo = invenForm.getInterchangeNo();
      if (interNo != null && !interNo.trim().equals("")) {
        PartsBean.getPart(interNo, null).addToOrder();
      } else if (partNo != null && !partNo.trim().equals("")) {
        PartsBean.getPart(partNo, null).addToOrder();
      }
      Object o3 = request.getParameter("SelectPart");
      if (o3 != null) {
        String partDescsSelected = (String) o3;
        session.setAttribute("partsDescsSelected", partDescsSelected);
        // session.setAttribute(part.getPartNo(), part);
        session.setAttribute("FocusVal", "SelectPart");
      }
      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("AddToInvoice")) {

      String partNo = invenForm.getPartNo();
      if (partNo != null && !partNo.trim().equals("")) {
        Object o = session.getAttribute("InvoiceDetails");
        Hashtable<String, InvoiceDetailsForm> ht = null;
        if (o == null) {
          ht = new Hashtable<String, InvoiceDetailsForm>();
        } else {
          ht = (Hashtable<String, InvoiceDetailsForm>) o;
          // ht.remove("XXXXX");
        }
        InvoiceDetailsForm iForm = new InvoiceDetailsForm();
        iForm.setParts(PartsBean.getPart(partNo, null),
            CustomerBean.getCustomerLevel(custIdSelected));
        ht.put(partNo, iForm);
        // ht.put("XXXXX", new InvoiceDetailsForm());
        session.setAttribute("InvoiceDetails", ht);
      }

      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("GoToInvoice")) {

      clearForm(session, invenForm);
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
      forwardPage = "InvoiceGen";
    } else if (buttonClicked.equals("GetInterChangeable")) {

      try {
        logger.error(1);
        String partNo = "";
        String interChangeNo = "";
        String make = "";
        String model = "";
        partNo = invenForm.getPartNo();
        logger.error(2);
        if (partNo != null && !partNo.trim().equals("")) {
          logger.error(3);

          PartsBean origPart = PartsBean.getPart(partNo, null);
          if (origPart != null) {
            // logger.error(4);
            interChangeNo = origPart.getInterchangeNo();
            if (interChangeNo != null && !interChangeNo.trim().equals("")) {
              // logger.error(5);
              PartsBean interPart = PartsBean.getPart(interChangeNo, null);
              if (interPart != null) {
                // logger.error("PartNo :" +
                // interPart.getPartNo());

                clearForm(session, invenForm);
                MakeModelBean makeModelBean = new MakeModelBean();
                makeModelBean.setMakeModelCode(interPart.getMakeModelCode());
                // logger.error("MakeModelCode :" +
                // interPart.getMakeModelCode());
                makeModelBean.getMakeModel();
                Hashtable<String, String> h =
                    MakeModelBean.getMakeModelForManufacId(makeModelBean.getManufacturerId());
                // logger.error("Manufacturer Id :" +
                // makeModelBean.getManufacturerId());
                session.setAttribute("manufacSelected", makeModelBean.getManufacturerId() + "");
                session.setAttribute("InvenAvailMakeModel", h);
                Hashtable<String, String> h1 =
                    PartsBean.getPartDescByCriteria(makeModelBean.getManufacturerId(),
                        interPart.getMakeModelCode());
                session.setAttribute("makeModelSelected", interPart.getMakeModelCode());
                session.setAttribute("InvenAvailPartsDescs", h1);
                session.removeAttribute(origPart.getPartNo());
                session.setAttribute("partsDescsSelected", interPart.getPartNo());
                // String etaDate =
                // VendorOrderBean.getETADate(interPart);
                fillForm(invenForm, interPart, custIdSelected);
                // session.setAttribute("ETADate", etaDate);
                session.setAttribute(interChangeNo, interPart);
                // interPart.addToEnquiry();
                session.setAttribute("FocusVal", "SelectPart");

              } else {
                errorBean.setError("This Inter Changeable Number is not Valid");
              }
            } else {
              errorBean.setError("Please Check your Inter Changeable Number");
            }
          } else {
            errorBean.setError("Please check the part you selected");
          }

        } else {
          errorBean.setError("Please check the part you selected");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }

      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("ClientLookup")) {

      clearForm(session, invenForm);
      forwardPage = "ClientLookup";
    } else if (buttonClicked.equals("MaintainInventory")) {
      String partNum = (String) session.getAttribute("partsDescsSelected");
      String makeModel = (String) session.getAttribute("makeModelSelected");
      String makeModelName = "";
      Hashtable hh = (Hashtable) session.getAttribute("InvenAvailMakeModel");
      if (partNum == null)
        partNum = "";
      if (makeModel == null) {
        makeModel = "";
      } else {
        MakeModelBean model = new MakeModelBean();
        model.setMakeModelCode(makeModel);
        try {
          model.getMakeModel();
        } catch (Exception e) {
          logger.error(e);
        }
        if (model.getInterChangeModel() != null && !model.getInterChangeModel().trim().equals("")) {
          makeModel = model.getInterChangeModel();
        }
      }

      if (hh != null) {
        makeModelName = (String) hh.get(makeModel);
      }
      session.setAttribute("PartForMaintain", partNum);
      session.setAttribute("MakeModelForMaintain", makeModelName);
      session.removeAttribute(partNum.trim());
      // clearForm(session, invenForm);
      forwardPage = "InvenMaint";
    } else if (buttonClicked.equals("CheckOtherInventory")) {
      String partNum = (String) session.getAttribute("partsDescsSelected");
      if (partNum == null || partNum.trim().equals("")) {
        errorBean.setError("You must select a Part for checking Inventory");
        forwardPage = "InvenAvail";
      } else {
        Object obj = session.getAttribute(partNum.trim());
        if (obj == null) {
          errorBean.setError("You must select a Part for checking Inventory");
          forwardPage = "InvenAvail";
        } else {
          PartsBean partBean = (PartsBean) obj;
          Vector<String[]> vv = null;
          vv = partBean.checkOtherInven(partBean.getPartNo());
          if (vv == null) {
            errorBean.setError("No Inventory Available From Other Places.....");
            forwardPage = "InvenAvail";
          } else {
            session.setAttribute("OthersInventory", vv);
            forwardPage = "CheckOtherInven";
          }
        }
      }

      /*
       * String makeModel = (String) session.getAttribute("makeModelSelected"); String makeModelName
       * = ""; Hashtable hh = (Hashtable) session.getAttribute("InvenAvailMakeModel"); if (partNum
       * == null) partNum = ""; if (makeModel == null) { makeModel = ""; } else { MakeModelBean
       * model = new MakeModelBean(); model.setMakeModelCode(makeModel); try { model.getMakeModel();
       * } catch (Exception e) { logger.error(e); } if (model.getInterChangeModel() != null && !
       * model.getInterChangeModel().trim().equals("")) { makeModel = model.getInterChangeModel(); }
       * }
       * 
       * if (hh != null) { makeModelName = (String) hh.get(makeModel); }
       * session.setAttribute("PartForMaintain", partNum); session.removeAttribute(partNum.trim());
       */

      // clearForm(session, invenForm);
    } else if (buttonClicked.equals("NewSearch")) {

      clearForm(session, invenForm);
      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("GetValues")) {
      String manufacSelected = null;
      String makeModelSelected = null;
      String partDescsSelected = null;
      Object o1 = request.getParameter("SelectManufacName");
      Object o2 = request.getParameter("SelectMakeModel");
      Object o3 = request.getParameter("SelectPart");
      Object o4 = session.getAttribute("manufacSelected");
      Object o5 = session.getAttribute("makeModelSelected");
      Object o6 = session.getAttribute("partsDescsSelected");

      // If the Manufacturer Name is selected then 'o1' is NOT NULL
      if (o1 != null) {
        manufacSelected = (String) o1;
        String manufacFromSession = (o4 != null) ? (String) o4 : "";

        // Manufacturer selection is changed
        if (!manufacSelected.trim().equals(manufacFromSession.trim())) {
          Hashtable<String, String> h =
              MakeModelBean.getMakeModelForManufacId(Integer.parseInt(manufacSelected));
          session.setAttribute("manufacSelected", manufacSelected);
          session.setAttribute("InvenAvailMakeModel", h);

          session.removeAttribute("makeModelSelected");
          session.removeAttribute("partsDescsSelected");
          session.removeAttribute("InvenAvailPartsDescs");

          // Manufacturer selection is same as previous one......
        } else {
          if (o2 != null) {
            makeModelSelected = (String) o2;
            String makeModelFromSession = (o5 != null) ? (String) o5 : "";

            // Make Model selection is New
            if (!makeModelSelected.trim().equals(makeModelFromSession.trim())) {
              Hashtable<String, String> h1 =
                  PartsBean.getPartDescByCriteria(Integer.parseInt(manufacSelected),
                      makeModelSelected);

              MakeModelBean model = new MakeModelBean();
              model.setMakeModelCode(makeModelSelected);
              try {
                model.getMakeModel();
              } catch (Exception e) {
                logger.error(e);
              }
              if (model.getInterChangeModel() != null
                  && !model.getInterChangeModel().trim().equals("")) {
                Hashtable<String, String> h2 =
                    PartsBean.getPartDescByCriteria(Integer.parseInt(manufacSelected),
                        model.getInterChangeModel());
                Enumeration<String> ennum = h2.keys();
                while (ennum.hasMoreElements()) {
                  String key = ennum.nextElement();
                  String val = h2.get(key);
                  h1.put(key, val);
                }
              }

              session.setAttribute("makeModelSelected", makeModelSelected);
              session.setAttribute("InvenAvailPartsDescs", h1);

              session.removeAttribute("partsDescsSelected");
              session.setAttribute("FocusVal", "SelectMakeModel");
              // Make Model selection is same as previous
              // one......
            } else {
              if (o3 != null) {
                session.removeAttribute("ShowInvenButton");
                partDescsSelected = (String) o3;
                String partsDescsFromSession = (o6 != null) ? (String) o6 : "";
                if (!partDescsSelected.trim().equals(partsDescsFromSession.trim())) {
                  session.removeAttribute(partsDescsFromSession.trim());
                  session.setAttribute("partsDescsSelected", partDescsSelected);
                  PartsBean part = PartsBean.getPart(partDescsSelected, null);
                  fillForm(invenForm, part, custIdSelected);
                  String etaDate = "";
                  if (part.getUnitsOnOrder() != 0) {
                    etaDate = VendorOrderBean.getETADate(part);
                    session.setAttribute("ETADate", etaDate);
                  }
                  if (part.getUnitsInStock() < 1) {
                    session.setAttribute("ShowInvenButton", "True");
                  }
                  session.setAttribute(part.getPartNo(), part);
                  // part.addToEnquiry();
                  session.setAttribute("FocusVal", "SelectPart");
                }
              } else {
                errorBean.setError("You must select a Part");
                session.removeAttribute("partsDescsSelected");
              }
            }
          } else {
            errorBean.setError("You must select a Make Model");
            session.removeAttribute("makeModelSelected");
            session.removeAttribute("partsDescsSelected");
            session.removeAttribute("SelectPart");
          }
        }

      } else {
        errorBean.setError("You must select a choice");
        clearForm(session, invenForm);
      }
      forwardPage = "InvenAvail";
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("InvenAvailError", errorBean);
    } else {
      session.removeAttribute("InvenAvailError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void clearForm(HttpSession session, InvenAvailForm invenForm) {
    session.removeAttribute("InvenAvailMakeModel");
    session.removeAttribute("InvenAvailPartsDescs");
    session.removeAttribute("manufacSelected");
    session.removeAttribute("makeModelSelected");
    session.removeAttribute("partsDescsSelected");
    invenForm.reset();
  }

  public void fillForm(InvenAvailForm invenForm, PartsBean part, String custIdSelected) {
    invenForm.setPartNo(part.getPartNo());
    invenForm.setInterchangeNo(part.getInterchangeNo());
    invenForm.setMakeModelName(MakeModelBean.getMakeModelName(part.getMakeModelCode()));
    invenForm.setSupplierId(part.getSupplierId() + "");
    invenForm.setYear(part.getYear());
    if (!custIdSelected.trim().equals("1111111111")) {
      int lvl = CustomerBean.getCustomerLevel(custIdSelected);
      if (lvl != 0) {
        invenForm.setCostPrice(part.getCostPrice(lvl) + "");
      } else {
        invenForm.setCostPrice(part.getCostPrice() + "");
      }
    } else {
      invenForm.setCostPrice(part.getCostPrice() + "");
    }
    double walkPrice = part.getListPrice() * 0.8;
    if (walkPrice != 0) {
      try {
        walkPrice = Double.parseDouble(NumberUtils.cutFractions(walkPrice + ""));
      } catch (Exception e) {
        logger.error(e);
      }
    }
    invenForm.setWalkinPrice(walkPrice + "");
    invenForm.setListPrice(part.getListPrice() + "");
    invenForm.setActualPrice(part.getActualPrice() + "");
    invenForm.setUnitsInStock(part.getUnitsInStock() + "");
    invenForm.setUnitsOnOrder(part.getUnitsOnOrder() + "");
    invenForm.setReorderLevel(part.getReorderLevel() + "");
    //invenForm.setCompPrice(part.getCompPrice1() + "");
    invenForm.setKeystoneNumber(part.getKeystoneNumber());
    invenForm.setOemNumber(part.getOemNumber());
    invenForm.setLocation(part.getLocation());
  }
}
