package com.bvas.formBeans;

import org.apache.struts.action.ActionForm;

import com.bvas.utils.DateUtils;

public class TodaysOrdersForm extends ActionForm {

  private String buttonClicked = null;

  private String fromTodaysDate = null;

  private String toTodaysDate = null;

  private String fromDateForPers = null;

  private String toDateForPers = null;

  private String fromDateForRt = null;

  private String toDateForRt = null;

  private String fromDateForCust = null;

  private String toDateForCust = null;

  private String fromDateForParts = null;

  private String toDateForParts = null;

  private String fromDateForReturns = null;

  private String toDateForReturns = null;

  private String fromDateForOldPers = null;

  private String toDateForOldPers = null;

  private String salesPerson = null;

  private String invoiceOrderFromDate = null;

  private String invoiceOrderToDate = null;

  private String invoiceSalesPerson = null;

  private String analyseInvoiceFromOrderDate = null;

  private String analyseInvoiceToOrderDate = null;

  private String costOfGoodsFromOrderDate = null;

  private String costOfGoodsToOrderDate = null;

  private String vendorOrderFromDate = null;

  private String vendorOrderToDate = null;

  private String localOrderFromDate = null;

  private String localOrderToDate = null;

  private String localOrderByVendFromDate = null;

  private String localOrderByVendToDate = null;

  private String vendorNo = null;

  private String custFromDate = null;

  private String custToDate = null;

  private String befThisDate = null;

  private String invoiceHistoryFromDate = null;

  private String invoiceHistoryToDate = null;

  private String codPendingFromDate = null;

  private String codPendingToDate = null;

  private String otherPendingFromDate = null;

  private String otherPendingToDate = null;

  private String allPendingFromDate = null;

  private String allPendingToDate = null;

  private String depositsFromDate = null;

  private String depositsToDate = null;

  public String getButtonClicked() {
    return (this.buttonClicked);
  }

  public void setButtonClicked(String buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public String getFromTodaysDate() {
    if (this.fromTodaysDate == null || this.fromTodaysDate.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.fromTodaysDate = todDate;
      this.fromTodaysDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromTodaysDate);
  }

  public void setFromTodaysDate(String fromTodaysDate) {
    this.fromTodaysDate = fromTodaysDate;
  }

  public String getToTodaysDate() {
    if (this.toTodaysDate == null || this.toTodaysDate.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.toTodaysDate = todDate;
      this.toTodaysDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toTodaysDate);
  }

  public void setToTodaysDate(String toTodaysDate) {
    this.toTodaysDate = toTodaysDate;
  }

  public String getFromDateForPers() {
    if (this.fromDateForPers == null || this.fromDateForPers.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.fromDateForPers = todDate;
      this.fromDateForPers = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromDateForPers);
  }

  public void setFromDateForPers(String fromDateForPers) {
    this.fromDateForPers = fromDateForPers;
  }

  public String getFromDateForRt() {
    if (this.fromDateForRt == null || this.fromDateForRt.trim().equals("")) {
      this.fromDateForRt = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromDateForRt);
  }

  public void setFromDateForRt(String fromDateForRt) {
    this.fromDateForRt = fromDateForRt;
  }

  public String getFromDateForCust() {
    if (this.fromDateForCust == null || this.fromDateForCust.trim().equals("")) {
      this.fromDateForCust = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromDateForCust);
  }

  public void setFromDateForCust(String fromDateForCust) {
    this.fromDateForCust = fromDateForCust;
  }

  public String getFromDateForParts() {
    if (this.fromDateForParts == null || this.fromDateForParts.trim().equals("")) {
      this.fromDateForParts = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromDateForParts);
  }

  public void setFromDateForParts(String fromDateForParts) {
    this.fromDateForParts = fromDateForParts;
  }

  public String getToDateForParts() {
    if (this.toDateForParts == null || this.toDateForParts.trim().equals("")) {
      this.toDateForParts = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toDateForParts);
  }

  public void setToDateForParts(String toDateForParts) {
    this.toDateForParts = toDateForParts;
  }

  public String getToDateForPers() {
    if (this.toDateForPers == null || this.toDateForPers.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.toDateForPers = todDate;
      this.toDateForPers = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toDateForPers);
  }

  public void setToDateForPers(String toDateForPers) {
    this.toDateForPers = toDateForPers;
  }

  public String getToDateForRt() {
    if (this.toDateForRt == null || this.toDateForRt.trim().equals("")) {
      this.toDateForRt = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toDateForRt);
  }

  public void setToDateForRt(String toDateForRt) {
    this.toDateForRt = toDateForRt;
  }

  public String getToDateForCust() {
    if (this.toDateForCust == null || this.toDateForCust.trim().equals("")) {
      this.toDateForCust = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toDateForCust);
  }

  public void setToDateForCust(String toDateForCust) {
    this.toDateForCust = toDateForCust;
  }

  public String getFromDateForReturns() {
    if (this.fromDateForReturns == null || this.fromDateForReturns.trim().equals("")) {
      this.fromDateForReturns = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromDateForReturns);
  }

  public void setFromDateForReturns(String fromDateForReturns) {
    this.fromDateForReturns = fromDateForReturns;
  }

  public String getToDateForReturns() {
    if (this.toDateForReturns == null || this.toDateForReturns.trim().equals("")) {
      this.toDateForReturns = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toDateForReturns);
  }

  public void setToDateForReturns(String toDateForReturns) {
    this.toDateForReturns = toDateForReturns;
  }

  public String getFromDateForOldPers() {
    if (this.fromDateForOldPers == null || this.fromDateForOldPers.trim().equals("")) {
      this.fromDateForOldPers = DateUtils.getNewUSDateForInvoice();
    }
    return (this.fromDateForOldPers);
  }

  public void setFromDateForOldPers(String fromDateForOldPers) {
    this.fromDateForOldPers = fromDateForOldPers;
  }

  public String getToDateForOldPers() {
    if (this.toDateForOldPers == null || this.toDateForOldPers.trim().equals("")) {
      this.toDateForOldPers = DateUtils.getNewUSDateForInvoice();
    }
    return (this.toDateForOldPers);
  }

  public void setToDateForOldPers(String toDateForOldPers) {
    this.toDateForOldPers = toDateForOldPers;
  }

  public String getSalesPerson() {
    return (this.salesPerson);
  }

  public void setSalesPerson(String salesPerson) {
    this.salesPerson = salesPerson;
  }

  public String getInvoiceOrderFromDate() {
    if (this.invoiceOrderFromDate == null || this.invoiceOrderFromDate.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.invoiceOrderFromDate = todDate;
      this.invoiceOrderFromDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.invoiceOrderFromDate);
  }

  public void setInvoiceOrderFromDate(String invoiceOrderFromDate) {
    this.invoiceOrderFromDate = invoiceOrderFromDate;
  }

  public String getInvoiceOrderToDate() {
    if (this.invoiceOrderToDate == null || this.invoiceOrderToDate.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.invoiceOrderToDate = todDate;
      this.invoiceOrderToDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.invoiceOrderToDate);
  }

  public void setInvoiceOrderToDate(String invoiceOrderToDate) {
    this.invoiceOrderToDate = invoiceOrderToDate;
  }

  public String getInvoiceSalesPerson() {
    return (this.invoiceSalesPerson);
  }

  public void setInvoiceSalesPerson(String invoiceSalesPerson) {
    this.invoiceSalesPerson = invoiceSalesPerson;
  }

  public String getAnalyseInvoiceFromOrderDate() {
    if (this.analyseInvoiceFromOrderDate == null
        || this.analyseInvoiceFromOrderDate.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.analyseInvoiceFromOrderDate = todDate;
      this.analyseInvoiceFromOrderDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.analyseInvoiceFromOrderDate);
  }

  public void setAnalyseInvoiceFromOrderDate(String analyseInvoiceFromOrderDate) {
    this.analyseInvoiceFromOrderDate = analyseInvoiceFromOrderDate;
  }

  public String getAnalyseInvoiceToOrderDate() {
    if (this.analyseInvoiceToOrderDate == null || this.analyseInvoiceToOrderDate.trim().equals("")) {
      // String todDate = "";
      // java.util.Date dd = new java.util.Date();
      // java.text.SimpleDateFormat sdf = new
      // java.text.SimpleDateFormat("MM-dd-yyyy");
      // todDate = sdf.format(dd);
      // this.analyseInvoiceToOrderDate = todDate;
      this.analyseInvoiceToOrderDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.analyseInvoiceToOrderDate);
  }

  public void setAnalyseInvoiceToOrderDate(String analyseInvoiceToOrderDate) {
    this.analyseInvoiceToOrderDate = analyseInvoiceToOrderDate;
  }

  public String getCostOfGoodsFromOrderDate() {
    if (this.costOfGoodsFromOrderDate == null || this.costOfGoodsFromOrderDate.trim().equals("")) {
      this.costOfGoodsFromOrderDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.costOfGoodsFromOrderDate);
  }

  public void setCostOfGoodsFromOrderDate(String costOfGoodsFromOrderDate) {
    this.costOfGoodsFromOrderDate = costOfGoodsFromOrderDate;
  }

  public String getCostOfGoodsToOrderDate() {
    if (this.costOfGoodsToOrderDate == null || this.costOfGoodsToOrderDate.trim().equals("")) {
      this.costOfGoodsToOrderDate = DateUtils.getNewUSDateForInvoice();
    }
    return (this.costOfGoodsToOrderDate);
  }

  public void setCostOfGoodsToOrderDate(String costOfGoodsToOrderDate) {
    this.costOfGoodsToOrderDate = costOfGoodsToOrderDate;
  }

  public String getVendorOrderFromDate() {
    if (this.vendorOrderFromDate == null || this.vendorOrderFromDate.trim().equals("")) {
      this.vendorOrderFromDate = DateUtils.getNewUSDate();
    }
    return (this.vendorOrderFromDate);
  }

  public void setVendorOrderFromDate(String vendorOrderFromDate) {
    this.vendorOrderFromDate = vendorOrderFromDate;
  }

  public String getVendorOrderToDate() {
    if (this.vendorOrderToDate == null || this.vendorOrderToDate.trim().equals("")) {
      this.vendorOrderToDate = DateUtils.getNewUSDate();
    }
    return (this.vendorOrderToDate);
  }

  public void setVendorOrderToDate(String vendorOrderToDate) {
    this.vendorOrderToDate = vendorOrderToDate;
  }

  public String getLocalOrderFromDate() {
    if (this.localOrderFromDate == null || this.localOrderFromDate.trim().equals("")) {
      this.localOrderFromDate = DateUtils.getNewUSDate();
    }
    return (this.localOrderFromDate);
  }

  public void setLocalOrderFromDate(String localOrderFromDate) {
    this.localOrderFromDate = localOrderFromDate;
  }

  public String getLocalOrderToDate() {
    if (this.localOrderToDate == null || this.localOrderToDate.trim().equals("")) {
      this.localOrderToDate = DateUtils.getNewUSDate();
    }
    return (this.localOrderToDate);
  }

  public void setLocalOrderToDate(String localOrderToDate) {
    this.localOrderToDate = localOrderToDate;
  }

  public String getLocalOrderByVendFromDate() {
    if (this.localOrderByVendFromDate == null || this.localOrderByVendFromDate.trim().equals("")) {
      this.localOrderByVendFromDate = DateUtils.getNewUSDate();
    }
    return (this.localOrderByVendFromDate);
  }

  public void setLocalOrderByVendFromDate(String localOrderByVendFromDate) {
    this.localOrderByVendFromDate = localOrderByVendFromDate;
  }

  public String getLocalOrderByVendToDate() {
    if (this.localOrderByVendToDate == null || this.localOrderByVendToDate.trim().equals("")) {
      this.localOrderByVendToDate = DateUtils.getNewUSDate();
    }
    return (this.localOrderByVendToDate);
  }

  public void setLocalOrderByVendToDate(String localOrderByVendToDate) {
    this.localOrderByVendToDate = localOrderByVendToDate;
  }

  public String getVendorNo() {
    return (this.vendorNo);
  }

  public void setVendorNo(String vendorNo) {
    this.vendorNo = vendorNo;
  }

  public String getCustFromDate() {
    // if (this.custFromDate == null || this.custFromDate.trim().equals(""))
    // {
    // this.custFromDate = DateUtils.getNewUSDate();
    // }
    return (this.custFromDate);
  }

  public void setCustFromDate(String custFromDate) {
    this.custFromDate = custFromDate;
  }

  public String getCustToDate() {
    // if (this.custToDate == null || this.custToDate.trim().equals("")) {
    // this.custToDate = DateUtils.getNewUSDate();
    // }
    return (this.custToDate);
  }

  public void setCustToDate(String custToDate) {
    this.custToDate = custToDate;
  }

  public String getBefThisDate() {
    return (this.befThisDate);
  }

  public void setBefThisDate(String befThisDate) {
    this.befThisDate = befThisDate;
  }

  public String getInvoiceHistoryFromDate() {
    if (this.invoiceHistoryFromDate == null || this.invoiceHistoryFromDate.trim().equals("")) {
      this.invoiceHistoryFromDate = DateUtils.getNewUSDate();
    }
    return (this.invoiceHistoryFromDate);
  }

  public void setInvoiceHistoryFromDate(String invoiceHistoryFromDate) {
    this.invoiceHistoryFromDate = invoiceHistoryFromDate;
  }

  public String getInvoiceHistoryToDate() {
    if (this.invoiceHistoryToDate == null || this.invoiceHistoryToDate.trim().equals("")) {
      this.invoiceHistoryToDate = DateUtils.getNewUSDate();
    }
    return (this.invoiceHistoryToDate);
  }

  public void setInvoiceHistoryToDate(String invoiceHistoryToDate) {
    this.invoiceHistoryToDate = invoiceHistoryToDate;
  }

  public String getCodPendingFromDate() {
    if (this.codPendingFromDate == null || this.codPendingFromDate.trim().equals("")) {
      this.codPendingFromDate = DateUtils.getNewUSDate();
    }
    return (this.codPendingFromDate);
  }

  public void setCodPendingFromDate(String codPendingFromDate) {
    this.codPendingFromDate = codPendingFromDate;
  }

  public String getCodPendingToDate() {
    if (this.codPendingToDate == null || this.codPendingToDate.trim().equals("")) {
      this.codPendingToDate = DateUtils.getNewUSDate();
    }
    return (this.codPendingToDate);
  }

  public void setCodPendingToDate(String codPendingToDate) {
    this.codPendingToDate = codPendingToDate;
  }

  public String getOtherPendingFromDate() {
    if (this.otherPendingFromDate == null || this.otherPendingFromDate.trim().equals("")) {
      this.otherPendingFromDate = DateUtils.getNewUSDate();
    }
    return (this.otherPendingFromDate);
  }

  public void setOtherPendingFromDate(String otherPendingFromDate) {
    this.otherPendingFromDate = otherPendingFromDate;
  }

  public String getOtherPendingToDate() {
    if (this.otherPendingToDate == null || this.otherPendingToDate.trim().equals("")) {
      this.otherPendingToDate = DateUtils.getNewUSDate();
    }
    return (this.otherPendingToDate);
  }

  public void setOtherPendingToDate(String otherPendingToDate) {
    this.otherPendingToDate = otherPendingToDate;
  }

  public String getAllPendingFromDate() {
    if (this.allPendingFromDate == null || this.allPendingFromDate.trim().equals("")) {
      this.allPendingFromDate = DateUtils.getNewUSDate();
    }
    return (this.allPendingFromDate);
  }

  public void setAllPendingFromDate(String allPendingFromDate) {
    this.allPendingFromDate = allPendingFromDate;
  }

  public String getAllPendingToDate() {
    if (this.allPendingToDate == null || this.allPendingToDate.trim().equals("")) {
      this.allPendingToDate = DateUtils.getNewUSDate();
    }
    return (this.allPendingToDate);
  }

  public void setAllPendingToDate(String allPendingToDate) {
    this.allPendingToDate = allPendingToDate;
  }

  public String getDepositsFromDate() {
    if (this.depositsFromDate == null || this.depositsFromDate.trim().equals("")) {
      this.depositsFromDate = DateUtils.getNewUSDate();
    }
    return (this.depositsFromDate);
  }

  public void setDepositsFromDate(String depositsFromDate) {
    this.depositsFromDate = depositsFromDate;
  }

  public String getDepositsToDate() {
    if (this.depositsToDate == null || this.depositsToDate.trim().equals("")) {
      this.depositsToDate = DateUtils.getNewUSDate();
    }
    return (this.depositsToDate);
  }

  public void setDepositsToDate(String depositsToDate) {
    this.depositsToDate = depositsToDate;
  }

}
