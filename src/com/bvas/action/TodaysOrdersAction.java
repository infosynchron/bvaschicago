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

import com.bvas.beans.LocalOrderBean;
import com.bvas.beans.UserBean;
import com.bvas.beans.VendorOrderBean;
import com.bvas.formBeans.TodaysOrdersForm;
import com.bvas.utils.DateUtils;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;
import com.bvas.utils.ReportUtils;
import com.bvas.utils.UserException;

public class TodaysOrdersAction extends Action {
  private static final Logger logger = Logger.getLogger(SubCategoryAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    TodaysOrdersForm todForm = (TodaysOrdersForm) form;
    String buttonClicked = todForm.getButtonClicked();
    todForm.setButtonClicked("");
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
    } else if (buttonClicked.equals("ShowDaysSales")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowDaysSales-----"
          + user.getUsername());
      try {
        String fromTodaysDate = todForm.getFromTodaysDate();
        String toTodaysDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")
            && !user.getUsername().trim().equalsIgnoreCase("CorrinaNY")
            && !user.getUsername().trim().equalsIgnoreCase("Nancy")
            && !user.getUsername().trim().equalsIgnoreCase("Jose")) {
          toTodaysDate = DateUtils.getNewUSDateForInvoice();
        } else {
          toTodaysDate = todForm.getToTodaysDate();
        }
        String salesPerson = todForm.getSalesPerson();

        if (!toTodaysDate.trim().equals("")) {
          Hashtable toShowSales =
              ReportUtils.showDaysSales(user, fromTodaysDate, toTodaysDate, salesPerson);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowSalesForPers")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowSalesForPers-----"
          + user.getUsername());
      try {
        long st = System.currentTimeMillis();
        String fromDate = todForm.getFromDateForPers();
        String toDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          fromDate = DateUtils.getNewUSDateForInvoice();
          toDate = DateUtils.getNewUSDateForInvoice();
        } else {
          toDate = todForm.getToDateForPers();
        }

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.showSalesForPers(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
        logger.error("Sales" + (System.currentTimeMillis() - st));
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowSalesForRt")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowSalesForRt-----"
          + user.getUsername());
      try {
        long st = System.currentTimeMillis();
        String fromDate = todForm.getFromDateForRt();
        String toDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")
            && !user.getUsername().trim().equalsIgnoreCase("Marcie")) {
          fromDate = DateUtils.getNewUSDateForInvoice();
          toDate = DateUtils.getNewUSDateForInvoice();
        } else {
          toDate = todForm.getToDateForRt();
        }

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.showSalesForRt(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
        logger.error("Sales" + (System.currentTimeMillis() - st));
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowCustomerSales")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowCustomerSales-----"
          + user.getUsername());
      try {
        String fromDate = todForm.getFromDateForCust();
        String toDate = todForm.getToDateForCust();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.showSalesForCust(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowPartsSold")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowPartsSold-----"
          + user.getUsername());
      try {
        String fromDate = todForm.getFromDateForParts();
        String toDate = todForm.getToDateForParts();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.showPartsSold(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowReturns")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowReturns-----"
          + user.getUsername());
      try {
        String fromDate = todForm.getFromDateForReturns();
        String toDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          toDate = DateUtils.getNewUSDateForInvoice();
        } else {
          toDate = todForm.getToDateForReturns();
        }

        if (!toDate.trim().equals("")) {
          Hashtable toShowReturns = ReportUtils.showReturns(user, fromDate, toDate);
          if (toShowReturns != null) {
            session.setAttribute("toShowReports", toShowReturns);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("OldShowSalesForPers")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----OldShowSalesForPers-----"
          + user.getUsername());
      try {
        String fromDate = todForm.getFromDateForOldPers();
        String toDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          toDate = DateUtils.getNewUSDateForInvoice();
        } else {
          toDate = todForm.getToDateForOldPers();
        }

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.showSalesForOldPers(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the orders to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowInvoices")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----ShowInvoices-----"
          + user.getUsername());
      try {
        String invoiceOrderFromDate = "";
        String invoiceOrderToDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")
            && !user.getUsername().trim().equalsIgnoreCase("Nancy")) {
          invoiceOrderFromDate = DateUtils.getNewUSDate();
          invoiceOrderToDate = DateUtils.getNewUSDateForInvoice();
        } else {
          invoiceOrderFromDate = todForm.getInvoiceOrderFromDate();
          invoiceOrderToDate = todForm.getInvoiceOrderToDate();
        }
        String invoiceSalesPerson = todForm.getInvoiceSalesPerson();

        if (!invoiceOrderToDate.trim().equals("")) {
          Hashtable toShowInvoices =
              ReportUtils.showInvoices(user, invoiceOrderFromDate, invoiceOrderToDate,
                  invoiceSalesPerson);
          if (toShowInvoices != null) {
            session.setAttribute("toShowReports", toShowInvoices);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the Invoices to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("AnalyseInvoices")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----AnalyseInvoices-----"
          + user.getUsername());
      try {
        String analyseInvoiceFromOrderDate = "";
        String analyseInvoiceToOrderDate = "";
        analyseInvoiceFromOrderDate = todForm.getAnalyseInvoiceFromOrderDate();
        analyseInvoiceToOrderDate = todForm.getAnalyseInvoiceToOrderDate();
        if (!analyseInvoiceFromOrderDate.trim().equals("")
            && !analyseInvoiceToOrderDate.trim().equals("")) {
          Hashtable toAnalyseInvoices =
              ReportUtils.analyseInvoices(user, analyseInvoiceFromOrderDate,
                  analyseInvoiceToOrderDate);
          if (toAnalyseInvoices != null) {
            session.setAttribute("toShowReports", toAnalyseInvoices);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the Invoices to Analyse .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("CostOfGoodsSold")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----CostOfGoodsSold-----"
          + user.getUsername());
      try {
        String costOfGoodsFromOrderDate = "";
        String costOfGoodsToOrderDate = "";
        costOfGoodsFromOrderDate = todForm.getCostOfGoodsFromOrderDate();
        costOfGoodsToOrderDate = todForm.getCostOfGoodsToOrderDate();
        if (!costOfGoodsFromOrderDate.trim().equals("")
            && !costOfGoodsToOrderDate.trim().equals("")) {
          Hashtable toCostOfGoodsInvoices =
              ReportUtils.costOfGoodsInvoices(user, costOfGoodsFromOrderDate,
                  costOfGoodsToOrderDate);
          if (toCostOfGoodsInvoices != null) {
            session.setAttribute("toShowReports", toCostOfGoodsInvoices);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the Invoices to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("VendorPurchases")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----VendorPurchases-----"
          + user.getUsername());
      try {
        String vendorOrderFromDate = "";
        String vendorOrderToDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          vendorOrderFromDate = DateUtils.getNewUSDate();
          vendorOrderToDate = DateUtils.getNewUSDate();
        } else {
          vendorOrderFromDate = todForm.getVendorOrderFromDate();
          vendorOrderToDate = todForm.getVendorOrderToDate();
        }

        if (!vendorOrderFromDate.trim().equals("") && !vendorOrderToDate.trim().equals("")) {
          Hashtable toShowPurchases =
              VendorOrderBean.showVendorPurchases(user, vendorOrderFromDate, vendorOrderToDate);
          if (toShowPurchases != null) {
            session.setAttribute("toShowReports", toShowPurchases);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Dates .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the dates for the Purchases to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("LocalPurchases")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----LocalPurchases-----"
          + user.getUsername());
      try {
        String localOrderFromDate = "";
        String localOrderToDate = "";
        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          localOrderFromDate = DateUtils.getNewUSDate();
          localOrderToDate = DateUtils.getNewUSDate();
        } else {
          localOrderFromDate = todForm.getLocalOrderFromDate();
          localOrderToDate = todForm.getLocalOrderToDate();
        }

        if (!localOrderFromDate.trim().equals("") && !localOrderToDate.trim().equals("")) {
          Hashtable toShowPurchases =
              LocalOrderBean.showLocalPurchases(user, localOrderFromDate, localOrderToDate);
          if (toShowPurchases != null) {
            session.setAttribute("toShowReports", toShowPurchases);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Dates .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the dates for the Purchases to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("LocalPurchasesByVend")) {
      logger.error(new java.util.Date(System.currentTimeMillis())
          + "-----LocalPurchasesByVend-----" + user.getUsername());
      try {
        String localOrderByVendFromDate = "";
        String localOrderByVendToDate = "";
        int vendorNo = 0;

        if (!user.getRole().trim().equalsIgnoreCase("High")) {
          localOrderByVendFromDate = DateUtils.getNewUSDate();
          localOrderByVendToDate = DateUtils.getNewUSDate();
        } else {
          localOrderByVendFromDate = todForm.getLocalOrderByVendFromDate();
          localOrderByVendToDate = todForm.getLocalOrderByVendToDate();
        }
        try {
          vendorNo = Integer.parseInt(todForm.getVendorNo());
        } catch (Exception e) {
          logger.error(e);
          throw new UserException("Check Your Vendor No.....");
        }

        if (!localOrderByVendFromDate.trim().equals("")
            && !localOrderByVendToDate.trim().equals("")) {
          Hashtable toShowPurchases =
              LocalOrderBean.showLocalPurchasesForVendor(user, localOrderByVendFromDate,
                  localOrderByVendToDate, vendorNo);
          if (toShowPurchases != null) {
            session.setAttribute("toShowReports", toShowPurchases);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Dates .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the dates for the Purchases to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("GetCustomers")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----GetCustomers-----"
          + user.getUsername());
      try {
        String custFromDate = todForm.getCustFromDate();
        String custToDate = todForm.getCustToDate();
        String befThisDate = todForm.getBefThisDate();
        PrintUtils.createClientListing(custFromDate, custToDate, befThisDate);
        errorBean.setError("Client Listing Created Successfully !!!");
        forwardPage = "TodaysOrders";
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("InventoryOnHand")) {
      logger.error(new java.util.Date(System.currentTimeMillis()) + "-----InventoryOnHand-----"
          + user.getUsername());
      try {
        Hashtable inventory = ReportUtils.inventoryOnHand(user, false);
        if (inventory != null) {
          session.setAttribute("toShowReports", inventory);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("System Error...  Try Later   !!!!!!!!");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("System Error...  Try Later   !!!!!!!! " + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("InventoryOnHandByCat")) {
      logger.error(new java.util.Date(System.currentTimeMillis())
          + "-----InventoryOnHandByCat-----" + user.getUsername());
      try {
        Hashtable inventory = ReportUtils.inventoryOnHand(user, true);
        if (inventory != null) {
          session.setAttribute("toShowReports", inventory);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("System Error...  Try Later   !!!!!!!!");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("System Error...  Try Later   !!!!!!!! " + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowInvoicesNotPrinted")) {
      logger.error(new java.util.Date(System.currentTimeMillis())
          + "-----ShowInvoicesNotPrinted-----" + user.getUsername());
      try {
        Hashtable showInv = ReportUtils.showInvoicesNotPrinted(user);
        if (showInv != null) {
          session.setAttribute("toShowReports", showInv);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No More Invoices");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("System Error...  Try Later   !!!!!!!! " + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowInvoicesNotDelivered")) {
      logger.error(new java.util.Date(System.currentTimeMillis())
          + "-----ShowInvoicesNotDelivered-----" + user.getUsername());
      forwardPage = "IsDelivered";
    } else if (buttonClicked.equals("PartsPending")) {
      forwardPage = "PartsPending";
    } else if (buttonClicked.equals("ShowInvoicesNotPickedUp")) {
      logger.error(new java.util.Date(System.currentTimeMillis())
          + "-----ShowInvoicesNotPickedUp-----" + user.getUsername());
      try {
        Hashtable showInv = ReportUtils.showInvoicesNotPickedUp(user, true);
        if (showInv != null) {
          session.setAttribute("toShowReports", showInv);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No More Invoices");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("System Error...  Try Later   !!!!!!!! " + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowInvoicesNotPickedUpAccts")) {
      try {
        Hashtable showInv = ReportUtils.showInvoicesNotPickedUp(user, false);
        if (showInv != null) {
          session.setAttribute("toShowReports", showInv);
          forwardPage = "ShowReports";
        } else {
          errorBean.setError("No More Invoices");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("System Error...  Try Later   !!!!!!!! " + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowInvoiceHistory")) {
      try {
        String fromDate = todForm.getInvoiceHistoryFromDate();
        String toDate = todForm.getInvoiceHistoryToDate();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.createHistoryReport(fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date for the history to Show .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowCODPending")) {
      try {
        String fromDate = todForm.getCodPendingFromDate();
        String toDate = todForm.getCodPendingToDate();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.codPendingInvoices(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date  .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowOtherPending")) {
      try {
        String fromDate = todForm.getOtherPendingFromDate();
        String toDate = todForm.getOtherPendingToDate();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.otherPendingInvoices(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date  .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowAllPending")) {
      try {
        String fromDate = todForm.getAllPendingFromDate();
        String toDate = todForm.getAllPendingToDate();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.allPendingInvoices(user, fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date  .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ShowDeposits")) {
      try {
        String fromDate = todForm.getDepositsFromDate();
        String toDate = todForm.getDepositsToDate();

        if (!toDate.trim().equals("")) {
          Hashtable toShowSales = ReportUtils.showDeposits(fromDate, toDate);
          if (toShowSales != null) {
            session.setAttribute("toShowReports", toShowSales);
            forwardPage = "ShowReports";
          } else {
            errorBean.setError("Please Check the Date .....");
            forwardPage = "TodaysOrders";
          }
        } else {
          errorBean.setError("Please Enter the date  .....");
          forwardPage = "TodaysOrders";
        }
      } catch (UserException e) {
        logger.error(e);
        errorBean.setError("Please check your Input:" + e.getMessage());
        forwardPage = "TodaysOrders";
      }

    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("TodaysOrdersError", errorBean);
    } else {
      session.removeAttribute("TodaysOrdersError");
    }

    return (mapping.findForward(forwardPage));

  }
}
