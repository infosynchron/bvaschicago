package com.bvas.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
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
import com.bvas.beans.InvoiceDetailsBean;
import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;
import com.bvas.formBeans.InvoiceArchForm;
import com.bvas.formBeans.InvoiceDetailsForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;

public class InvoiceArchAction extends Action {
  private static final Logger logger = Logger.getLogger(InvoiceArchAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    InvoiceArchForm iForm = (InvoiceArchForm) form;
    String buttonClicked = iForm.getButtonClicked();
    iForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in InvoiceArchAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    UserBean user = (UserBean) session.getAttribute("User");
    logger.error(new java.util.Date(System.currentTimeMillis()) + "-----InvoiceArch-----"
        + user.getUsername());

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetInvoice")) {
      getInvoice(iForm, request, 0);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("Previous")) {
      getInvoice(iForm, request, 1);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("Next")) {
      getInvoice(iForm, request, 2);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("PreviousForCust")) {
      getInvoice(iForm, request, 3);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("NextForCust")) {
      getInvoice(iForm, request, 4);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("PreviousForPers")) {
      getInvoice(iForm, request, 5);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("NextForPers")) {
      getInvoice(iForm, request, 6);
      forwardPage = "InvoiceArch";
    } else if (buttonClicked.equals("GoToFinanceForm")) {
      clearForm(iForm);
      clearInvoiceDetails(request);
      forwardPage = "MaintainFinance";
    } else if (buttonClicked.equals("ReturnToMain")) {
      clearForm(iForm);
      clearInvoiceDetails(request);
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("InvoiceArchError", errorBean);
    } else {
      session.removeAttribute("InvoiceArchError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void getInvoice(InvoiceArchForm iForm, HttpServletRequest request, int typeInv) {

    try {

      String invNoStr = iForm.getInvoiceNumber();
      String custId = iForm.getCustomerId();
      String salesPerson = iForm.getSalesPerson();
      String compName = iForm.getCompanyName();
      if (invNoStr == null)
        invNoStr = "";
      if (custId == null)
        custId = "";
      if (!custId.trim().equals("") || !invNoStr.trim().equals("")
          || !salesPerson.trim().equals("") || !compName.trim().equals("")) {
        int invNo = 0;
        if (!invNoStr.trim().equals("")) {
          invNo = Integer.parseInt(invNoStr);
        }

        InvoiceBean invBean = null;

        if (typeInv == 0 && invNo != 0) {
          invBean = InvoiceBean.getInvoice(invNo);
          getProcessDetails(request, invNo);
          if (invBean != null && invBean.getStatus().trim().equalsIgnoreCase("C")
              && invBean.getBalance() == 0 && invBean.getInvoiceNumber() < 175000) {
            // logger.error("MOVING THE INVOICE TO ARCHIVE:::"
            // + invBean.getInvoiceNumber());
            moveInvoiceToArchive(invBean);
          }
        } else {
          String sql1 = "Select Max(InvoiceNumber) From Invoice Where CustomerId='" + custId + "'";
          String sql2 = "Select Max(InvoiceNumber) From Invoice Where InvoiceNumber<" + invNo;
          String sql3 = "Select Min(InvoiceNumber) From Invoice Where InvoiceNumber>" + invNo;
          String sql4 =
              "Select Max(InvoiceNumber) From Invoice Where InvoiceNumber<" + invNo
                  + " and customerId='" + custId + "'";
          String sql5 =
              "Select Min(InvoiceNumber) From Invoice Where InvoiceNumber>" + invNo
                  + " and customerId='" + custId + "'";
          String sql6 =
              "Select Max(InvoiceNumber) From Invoice Where SalesPerson='" + salesPerson + "'";
          String sql7 =
              "Select Max(InvoiceNumber) From Invoice Where InvoiceNumber<" + invNo
                  + " and SalesPerson='" + salesPerson + "'";
          String sql8 =
              "Select Min(InvoiceNumber) From Invoice Where InvoiceNumber>" + invNo
                  + " and SalesPerson='" + salesPerson + "'";
          Connection con = DBInterfaceLocal.getSQLConnection();
          try {
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            int invN = 0;
            if (typeInv == 0 && !custId.trim().equals("")) {
              rs = stmt.executeQuery(sql1);
            } else if (typeInv == 0 && !salesPerson.trim().equals("")) {
              rs = stmt.executeQuery(sql6);
            } else if (typeInv == 0 && !compName.trim().equals("")) {
              CustomerBean cust = CustomerBean.getCustomer(compName);
              if (cust != null) {
                logger.error("Got Customer");
                rs =
                    stmt.executeQuery("Select Max(InvoiceNumber) From Invoice Where CustomerId='"
                        + cust.getCustomerId() + "'");
              } else {
                logger.error("Did not Get Customer");
              }
            } else if (typeInv == 1) {
              rs = stmt.executeQuery(sql2);
            } else if (typeInv == 2) {
              rs = stmt.executeQuery(sql3);
            } else if (typeInv == 3) {
              rs = stmt.executeQuery(sql4);
            } else if (typeInv == 4) {
              rs = stmt.executeQuery(sql5);
            } else if (typeInv == 5) {
              rs = stmt.executeQuery(sql7);
            } else if (typeInv == 6) {
              rs = stmt.executeQuery(sql8);
            } else {
              throw new Exception("Enter");
            }
            if (rs.next()) {
              invN = rs.getInt(1);
              invBean = InvoiceBean.getInvoice(invN);
              getProcessDetails(request, invN);
              if (typeInv == 2 && invN != 0) {
                if (invBean != null && invBean.getStatus().trim().equalsIgnoreCase("C")
                    && invBean.getBalance() == 0 && invBean.getInvoiceNumber() < 175000) {
                  // logger.error("MOVING THE INVOICE TO ARCHIVE:::"
                  // + invBean.getInvoiceNumber());
                  moveInvoiceToArchive(invBean);
                }
              }
            } else {
              throw new Exception("Enter");
            }
            rs.close();
            stmt.close();
            con.close();
          } catch (Exception e) {

            clearForm(iForm);
            clearInvoiceDetails(request);
            logger.error(e);
          }
        }

        Vector<InvoiceDetailsBean> v = invBean.getInvoiceDetails();
        clearForm(iForm);
        clearInvoiceDetails(request);
        fillForm(iForm, invBean);
        fillInvoiceDetails(v, request);
      } else {
        clearForm(iForm);
        clearInvoiceDetails(request);
      }

    } catch (Exception e) {
      clearForm(iForm);
      clearInvoiceDetails(request);
      logger.error("In InvoiceArchAction - Exception occurred - " + e);
    }
  }

  public void getProcessDetails(HttpServletRequest request, int invNo) {
    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      HttpSession session = (HttpSession) request.getSession(false);
      String processDet = "";

      String name1 = "";
      String name2 = "";

      Statement stmt1 = con.createStatement();
      ResultSet rs1 =
          stmt1.executeQuery("Select Name From PartsPulled Where InvoiceNumber='" + invNo + "'");
      if (rs1.next()) {
        name1 = rs1.getString(1);
      }
      Statement stmt2 = con.createStatement();
      ResultSet rs2 =
          stmt2.executeQuery("Select Name From PartsDelivered Where InvoiceNumber='" + invNo + "'");
      if (rs2.next()) {
        name2 = rs2.getString(1);
      }
      if (name1 != null && !name1.trim().equals("")) {
        processDet += "PARTS::" + name1;
      }
      if (name2 != null && !name2.trim().equals("")) {
        processDet += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DELIVERY::" + name2;
      }
      if (!processDet.trim().equals("")) {
        session.setAttribute("InvoiceProcessDetails", processDet);
      }
      rs1.close();
      stmt1.close();
      rs2.close();
      stmt2.close();
      con.close();

    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  public void fillForm(InvoiceArchForm iForm, InvoiceBean iBean) {
    iForm.setInvoiceNumber(iBean.getInvoiceNumber() + "");
    iForm.setOrderDate(iBean.getOrderDate());
    iForm.setCustomerId(iBean.getCustomerId() + "");
    iForm.setShippedVia(iBean.getShipVia());
    iForm.setSalesPerson(iBean.getSalesPerson());
    iForm.setInvoiceTime(new java.util.Date(iBean.getInvoiceTime()).toString());
    try {
      iForm.setCompanyName(CustomerBean.getCustomer(iBean.getCustomerId()).getCompanyName());
    } catch (Exception e) {
      iForm.setCompanyName("");
      logger.error(e);
    }
    iForm.setAppliedAmount(iBean.getAppliedAmount() + "");
    iForm.setInvoiceTotal(iBean.getInvoiceTotal() + "");
    iForm.setDiscount(iBean.getDiscount() + "");
    iForm.setClientBalance(iBean.getBalance() + "");
  }

  public void fillInvoiceDetails(Vector<InvoiceDetailsBean> v, HttpServletRequest request)
      throws Exception {
    Enumeration<InvoiceDetailsBean> ennum = v.elements();

    Vector<InvoiceDetailsForm> vInvDet = new Vector<InvoiceDetailsForm>();
    int invNo = 0;
    while (ennum.hasMoreElements()) {
      InvoiceDetailsBean invDetBean = ennum.nextElement();
      if (invNo == 0) {
        invNo = invDetBean.getInvoiceNumber();
      } else if (invNo != invDetBean.getInvoiceNumber()) {
        throw new Exception("Invoice Numbers Different---");
      }
      InvoiceDetailsForm iForm = new InvoiceDetailsForm();
      if (!invDetBean.getPartNumber().startsWith("XX")) {
        PartsBean part = PartsBean.getPart(invDetBean.getPartNumber(), null);
        iForm.setParts(part, 0);
        iForm.setCostPrice(invDetBean.getSoldPrice() + "");
        iForm.setMakeModelName(MakeModelBean.getMakeModelName(part.getMakeModelCode()));
        iForm.setQuantity(invDetBean.getQuantity());
      } else {
        iForm.setPartNo(invDetBean.getPartNumber());
        iForm.setMakeModelName("");
        iForm.setPartDescription("Damaged Discount For " + invDetBean.getPartNumber());
        iForm.setCostPrice(invDetBean.getSoldPrice() + "");
        iForm.setListPrice("");
        iForm.setQuantity(invDetBean.getQuantity());
      }

      vInvDet.add(iForm);

    }

    HttpSession session = request.getSession(false);
    clearInvoiceDetails(request);
    session.setAttribute("InvDet" + invNo, vInvDet);
  }

  public void clearForm(InvoiceArchForm iForm) {
    iForm.setInvoiceNumber("");
    iForm.setOrderDate("");
    iForm.setCustomerId("");
    iForm.setShippedVia("");
    iForm.setSalesPerson("");
    iForm.setInvoiceTime("");
    iForm.setCompanyName("");
    iForm.setInvoiceTotal("");
    iForm.setAppliedAmount("");
    iForm.setClientBalance("");
    iForm.setDiscount("");
  }

  public void clearInvoiceDetails(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    Enumeration ennum = session.getAttributeNames();
    while (ennum.hasMoreElements()) {
      Object o = ennum.nextElement();
      String attrName = (String) o;
      if (attrName.indexOf("InvDet") != -1) {
        session.removeAttribute(attrName);
        break;
      }
    }
  }

  public void moveInvoiceToArchive(InvoiceBean invoice) {
    FileWriter wrt1 = null;
    try {
      File returns1 = new File("c:/bvaschicago/Data/Archiving.txt");
      wrt1 = new FileWriter(returns1, true);
      wrt1.write("Archiving Invoice:::" + invoice.getInvoiceNumber() + "\n");

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();

      ResultSet rs =
          stmt.executeQuery("Select * From Invoice Where InvoiceNumber="
              + invoice.getInvoiceNumber());
      String sql = "";
      String sql1 = "";
      String sql2 = "";
      if (rs.next()) {
        int invNo = rs.getInt("InvoiceNumber");
        String orderDate = rs.getString("OrderDate");
        long invTime = rs.getLong("InvoiceTime");
        String custId = rs.getString("CustomerId");
        String paymentTerms = rs.getString("PaymentTerms");
        int retInv = rs.getInt("ReturnedInvoice");
        String shipTo = rs.getString("ShipTo");
        String shipAtt = rs.getString("ShipAttention");
        String shipVia = rs.getString("ShipVia");
        String billAtt = rs.getString("BillAttention");
        double invTotal = rs.getDouble("InvoiceTotal");
        double applAmt = rs.getDouble("AppliedAmount");
        double newAppl = rs.getDouble("NewApplied");
        String dateNew = rs.getString("DateNewApplied");
        double discount = rs.getDouble("Discount");
        double tax = rs.getDouble("Tax");
        double balance = rs.getDouble("Balance");
        String salesPers = rs.getString("SalesPerson");
        String notes = rs.getString("Notes");
        String discType = rs.getString("DiscountType");
        String status = rs.getString("Status");
        String history = rs.getString("History");
        String isPrint = rs.getString("IsPrinted");
        String isDel = rs.getString("IsDelivered");

        sql1 = "InvoiceNumber, ";
        sql2 = invNo + ", ";
        sql1 += "OrderDate, ";
        sql2 += "'" + orderDate + "', ";
        sql1 += "InvoiceTime, ";
        sql2 += invTime + ", ";
        sql1 += "CustomerId, ";
        sql2 += "'" + custId + "', ";
        sql1 += "PaymentTerms, ";
        if (paymentTerms == null) {
          paymentTerms = "";
        }
        sql2 += "'" + paymentTerms + "', ";
        sql1 += "ReturnedInvoice, ";
        sql2 += retInv + ", ";
        sql1 += "ShipTo, ";
        sql2 += "'" + shipTo + "', ";
        sql1 += "ShipAttention, ";
        sql2 += "'" + shipAtt + "', ";
        sql1 += "ShipVia, ";
        sql2 += "'" + shipVia + "', ";
        sql1 += "BillAttention, ";
        sql2 += "'" + billAtt + "', ";
        sql1 += "InvoiceTotal, ";
        sql2 += invTotal + ", ";
        sql1 += "AppliedAmount, ";
        sql2 += applAmt + ", ";
        sql1 += "NewApplied, ";
        sql2 += newAppl + ", ";
        sql1 += "DateNewApplied, ";
        sql2 += "'" + dateNew + "', ";
        sql1 += "Discount, ";
        sql2 += discount + ", ";
        sql1 += "Tax, ";
        sql2 += tax + ", ";
        sql1 += "Balance, ";
        sql2 += balance + ", ";
        sql1 += "SalesPerson, ";
        sql2 += "'" + salesPers + "', ";
        sql1 += "Notes, ";
        sql2 += "'" + notes + "', ";
        sql1 += "DiscountType, ";
        sql2 += "'" + discType + "', ";
        sql1 += "Status, ";
        sql2 += "'" + status + "', ";
        sql1 += "History, ";
        sql2 += "'" + history + "', ";
        sql1 += "IsPrinted, ";
        sql2 += "'" + isPrint + "', ";
        sql1 += "IsDelivered ";
        sql2 += "'" + isDel + "' ";

        if (invNo <= 100000) {
          sql = "Insert Into ArchiveInvoice0 (" + sql1 + ") Values (" + sql2 + ")";
        } else if (invNo > 100000 && invNo <= 200000) {
          sql = "Insert Into ArchiveInvoice1 (" + sql1 + ") Values (" + sql2 + ")";
        } else if (invNo > 200000 && invNo <= 300000) {
          sql = "Insert Into ArchiveInvoice2 (" + sql1 + ") Values (" + sql2 + ")";
        } else if (invNo > 300000 && invNo <= 400000) {
          sql = "Insert Into ArchiveInvoice3 (" + sql1 + ") Values (" + sql2 + ")";
        } else if (invNo > 400000 && invNo <= 500000) {
          sql = "Insert Into ArchiveInvoice4 (" + sql1 + ") Values (" + sql2 + ")";
        }

        wrt1.write(sql + "\n");
        stmt.execute(sql);

        String sql3 = "";
        if (invNo <= 100000) {
          sql3 = "Select * from ArchiveInvoice0 Where InvoiceNumber=" + invNo;
          wrt1.write("Selected Table:::ArchiveInvoice0\n");
        } else if (invNo > 100000 && invNo <= 200000) {
          sql3 = "Select * from ArchiveInvoice1 Where InvoiceNumber=" + invNo;
          wrt1.write("Selected Table:::ArchiveInvoice1\n");
        } else if (invNo > 200000 && invNo <= 300000) {
          sql3 = "Select * from ArchiveInvoice2 Where InvoiceNumber=" + invNo;
          wrt1.write("Selected Table:::ArchiveInvoice2\n");
        } else if (invNo > 300000 && invNo <= 400000) {
          sql3 = "Select * from ArchiveInvoice3 Where InvoiceNumber=" + invNo;
          wrt1.write("Selected Table:::ArchiveInvoice3\n");
        } else if (invNo > 400000 && invNo <= 500000) {
          sql3 = "Select * from ArchiveInvoice4 Where InvoiceNumber=" + invNo;
          wrt1.write("Selected Table:::ArchiveInvoice4\n");
        }
        ResultSet rs1 = stmt.executeQuery(sql3);
        wrt1.write("Inserted Successfully\n");
        if (rs1.next()) {
          stmt.execute("Delete From Invoice Where InvoiceNumber=" + invNo);
          stmt.execute("Delete From InvoiceHistory Where InvoiceNumber=" + invNo);
          wrt1.write("Deleted Successfully\n\n");
        } else {
          logger.error("INVOICE INSERTED INTO ARCHIVE BUT NOT DELETED FROM INVOICE TABLE::: "
              + invNo);
          wrt1.write("***** NOT DELETED --- CHECK\n\n");
        }

      } else {
        logger.error("Moving Invoice::: This invoice not available");
      }

      Statement stmt1 = con.createStatement();
      ResultSet rsX =
          stmt1.executeQuery("Select * From InvoiceDetails Where InvoiceNumber="
              + invoice.getInvoiceNumber());
      String sqlX = "";
      String sqlX1 = "";
      String sqlX2 = "";
      while (rsX.next()) {
        int invNo = rsX.getInt("InvoiceNumber");
        String partNo = rsX.getString("PartNumber");
        int qty = rsX.getInt("Quantity");
        double cost = rsX.getDouble("SoldPrice");
        double actual = rsX.getDouble("ActualPrice");

        sqlX1 = "InvoiceNumber, ";
        sqlX2 = invNo + ", ";
        sqlX1 += "PartNumber, ";
        sqlX2 += "'" + partNo + "', ";
        sqlX1 += "Quantity, ";
        sqlX2 += qty + ", ";
        sqlX1 += "SoldPrice, ";
        sqlX2 += cost + ", ";
        sqlX1 += "ActualPrice";
        sqlX2 += actual + "";

        if (invNo <= 100000) {
          sqlX = "Insert Into InvoiceDetails0 (" + sqlX1 + ") Values (" + sqlX2 + ")";
        } else if (invNo > 100000 && invNo <= 200000) {
          sqlX = "Insert Into InvoiceDetails1 (" + sqlX1 + ") Values (" + sqlX2 + ")";
        } else if (invNo > 200000 && invNo <= 300000) {
          sqlX = "Insert Into InvoiceDetails2 (" + sqlX1 + ") Values (" + sqlX2 + ")";
        } else if (invNo > 300000 && invNo <= 400000) {
          sqlX = "Insert Into InvoiceDetails3 (" + sqlX1 + ") Values (" + sqlX2 + ")";
        } else if (invNo > 400000 && invNo <= 500000) {
          sqlX = "Insert Into InvoiceDetails4 (" + sqlX1 + ") Values (" + sqlX2 + ")";
        }

        wrt1.write(sqlX + "\n");
        Statement stmt2 = con.createStatement();
        stmt2.execute(sqlX);

        String sqlX3 = "";
        if (invNo <= 100000) {
          sqlX3 =
              "Select * from InvoiceDetails0 Where InvoiceNumber=" + invNo + " And PartNumber='"
                  + partNo + "'";
          wrt1.write("Selected Table:::InvoiceDetails0\n");
        } else if (invNo > 100000 && invNo <= 200000) {
          sqlX3 =
              "Select * from InvoiceDetails1 Where InvoiceNumber=" + invNo + " And PartNumber='"
                  + partNo + "'";
          wrt1.write("Selected Table:::InvoiceDetails1\n");
        } else if (invNo > 200000 && invNo <= 300000) {
          sqlX3 =
              "Select * from InvoiceDetails2 Where InvoiceNumber=" + invNo + " And PartNumber='"
                  + partNo + "'";
          wrt1.write("Selected Table:::InvoiceDetails2\n");
        } else if (invNo > 300000 && invNo <= 400000) {
          sqlX3 =
              "Select * from InvoiceDetails3 Where InvoiceNumber=" + invNo + " And PartNumber='"
                  + partNo + "'";
          wrt1.write("Selected Table:::InvoiceDetails3\n");
        } else if (invNo > 400000 && invNo <= 500000) {
          sqlX3 =
              "Select * from InvoiceDetails4 Where InvoiceNumber=" + invNo + " And PartNumber='"
                  + partNo + "'";
          wrt1.write("Selected Table:::InvoiceDetails4\n");
        }

        ResultSet rsX1 = stmt2.executeQuery(sqlX3);
        wrt1.write("Inserted Successfully\n");
        if (rsX1.next()) {
          stmt2.execute("Delete From InvoiceDetails Where InvoiceNumber=" + invNo
              + " And PartNumber='" + partNo + "'");
          wrt1.write("Deleted Successfully\n\n");
        } else {
          logger
              .error("INVOICE INSERTED INTO ARCHIVE BUT NOT DELETED FROM InvoiceDetails TABLE::: "
                  + invNo);
          wrt1.write("***** NOT DELETED --- CHECK\n\n");
        }
        rsX1.close();
        stmt2.close();

      }
      rs.close();      
      rsX.close();
      stmt1.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      try {
        wrt1.write("***** SQL EXCEPTION:: " + e.getMessage() + "\n\n");
      } catch (Exception ex) {
        logger.error(e);
      }
      logger.error("EXCEPTION HAPPENED WHEN INVOICE TO ARCHIVE::" + e.getMessage());
    } catch (Exception e) {
      try {
        wrt1.write("***** EXCEPTION:: " + e.getMessage() + "\n\n");
      } catch (Exception ex) {
        logger.error(e);
      }
      logger.error("EXCEPTION HAPPENED WHEN INVOICE TO ARCHIVE::" + e.getMessage());
    } finally {
      try {
        wrt1.close();
      } catch (Exception ex) {
        logger.error(ex);
      }
    }
  }
}
