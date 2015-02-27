package com.bvas.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.beans.BouncedChecksBean;
import com.bvas.beans.CustomerBean;
import com.bvas.beans.FaxBean;
import com.bvas.beans.InvoiceBean;
import com.bvas.beans.InvoiceDetailsBean;
import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.UserBean;
import com.bvas.beans.VendorItemBean;
import com.bvas.beans.VendorOrderBean;
import com.bvas.beans.VendorOrderedItemsBean;

public class PrintUtils {
  private static final Logger logger = Logger.getLogger(PrintUtils.class);

  // public static void createInvoiceFor60(InvoiceBean invoice)
  // public static void createTodaysOrders(UserBean user)
  // public static void createClientListing()
  // public static void createVendorListing()
  // public static void createFax(FaxBean faxBean)
  // public static void createFinanceNotice(String custId)
  // public static void createFinanceNotice(String custId, Hashtable
  // checkedInvoices)
  // public static void createFinanceStatement()
  // public static void createInvoice(InvoiceBean invoice)
  // public static void createOldInvoice(InvoiceBean invoice)
  // public static String getHeaders() {
  // public static String getFooters() {
  // public static String padSpaces(String str, int len) {
  // public static String newLines(String str) {
  // public static String cutModel(String model) {
  // public static int calLen(String str, int totSize) {
  // public static void printVendorOrder(VendorOrderBean orderBean, boolean
  // woPrice)
  // public static void getVendorHeader(FileWriter ft, int supId, boolean
  // woPrice) throws IOException

  public static boolean createInvoiceFor120(InvoiceBean invoice) {
    boolean fine = true;

    File file =
        new File("c:/Tomcat/webapps/bvaschicago/html/reports/Invoice" + invoice.getInvoiceNumber()
            + ".html");

    // if file doesnt exists, then create it
    if (!file.exists()) {
      try {
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter ft = new BufferedWriter(fw);
        ft.write("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>");
        ft.write("<HTML>");
        ft.write("<HEAD>");
        ft.write("<meta http-equiv='Content-type' content='text/html;charset=UTF-8'>");
        ft.write("<TITLE>invoiceprint.html</TITLE>");
        ft.write("</HEAD>");
        ft.write("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
        ft.write("<BODY onload='PrintPage()'>");

        logger.error("Creating the Invoice Print for MSIE 6.0");
        ft.write("<P><BR>");
        ft.write("<BR>");
        ft.write("<BR>");
        ft.write("<BR>");
        ft.write("</P>");
        ft.write("<TABLE width='850' height='625'>");
        ft.write("    <TBODY>");
        ft.write("        <TR valign='bottom'>");
        ft.write("            <TD width='787' valign='bottom'>");
        ft.write("            <TABLE width='764' height='25'>");
        ft.write("                <TBODY>");
        ft.write("                    <TR>");
        String invoiceNoStr = invoice.getInvoiceNumber() + "";
        if (invoice.getReturnedInvoice() != 0) {
          invoiceNoStr = "C" + invoiceNoStr;
        }
        String paymentTerms = "";
        CustomerBean custBean = CustomerBean.getCustomer(invoice.getCustomerId());
        if (custBean.getPaymentTerms().trim().equals("O")) {
          paymentTerms = "* CASH ONLY *";
        } else if (custBean.getPaymentTerms().trim().equals("C")) {
          paymentTerms = " ** COD ** ";
        } else if (custBean.getPaymentTerms().trim().equals("B")) {
          paymentTerms = " * BI-WKLY * ";
        } else if (custBean.getPaymentTerms().trim().equals("W")) {
          paymentTerms = " * WKLY * ";
        } else if (custBean.getPaymentTerms().trim().equals("M")) {
          paymentTerms = " * MTHLY * ";
        }
        ft.write("                        <TD width='119' align='center'>"
            + invoice.getSalesPerson().toUpperCase() + "</TD>");
        // ft.write("                        <TD width='142'>&nbsp;&nbsp;&nbsp;"
        // + invoice.getOrderDate() + "</TD>");
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yy h:mm a");
        ft.write("                        <TD width='142'>&nbsp;&nbsp;&nbsp;"
            + sdf.format(new java.util.Date(invoice.getInvoiceTime())) + "</TD>");

        ft.write("                        <TD width='125'>" + invoice.getCustomerId() + "</TD>");
        // ft.write("                        <TD width='147'>" +
        // invoice.getShipVia() + "</TD>");
        ft.write("                        <TD width='120'>" + invoice.getShipVia() + "</TD>");
        ft.write("                        <TD style='font-size: 16pt;' width='145' align='left'>"
            + invoiceNoStr + "</TD>");
        ft.write("                    </TR>");
        ft.write("                </TBODY>");
        ft.write("            </TABLE>");
        ft.write("            </TD>");
        ft.write("        </TR>");
        ft.write("        <TR valign='top' style='font-size: 11pt;' height='60'>");
        ft.write("            <TD valign='top'>");
        ft.write("            <TABLE>");
        ft.write("                <TBODY>");
        ft.write("                    <TR>");
        ft.write("                        <TD valign='top'><BR>");
        ft.write("                        <BR>");
        ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
        ft.write("                            <TBODY>");
        ft.write("                                <TR>");
        ft.write("                                    <TD width='10'></TD>");
        ft.write("                                    <TD width='142'></TD>");
        ft.write("                                    <TD width='35'></TD>");
        ft.write("                                    <TD width='51'></TD>");
        ft.write("                                    <TD width='60'></TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                	<TD></TD>");
        ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + custBean.getCompanyName() + "&nbsp;" + paymentTerms + "</TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + invoice.getBillToAddress().getAddress1() + " </TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD>" + invoice.getBillToAddress().getCity()
            + "</TD>");
        ft.write("                                    <TD colspan='2'>"
            + invoice.getBillToAddress().getState() + "</TD>");
        ft.write("                                    <TD>"
            + invoice.getBillToAddress().getPostalCode() + "</TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + invoice.getBillToAddress().getRegion() + "</TD>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD colspan='2'>"
            + invoice.getBillAttention() + "</TD>");
        ft.write("                                </TR>");
        ft.write("                            </TBODY>");
        ft.write("                        </TABLE>");
        ft.write("                        </TD>");
        ft.write("                        <TD valign='top'><BR>");
        ft.write("                        <BR>");
        ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
        ft.write("                            <TBODY>");
        ft.write("                                <TR>");
        ft.write("                                    <TD width='67'></TD>");
        ft.write("                                    <TD width='146'></TD>");
        ft.write("                                    <TD width='43'></TD>");
        ft.write("                                    <TD width='45'></TD>");
        ft.write("                                    <TD width='50'></TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                	<TD></TD>");
        ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + invoice.getShipTo() + "</TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + invoice.getShipToAddress().getAddress1() + "</TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD>" + invoice.getShipToAddress().getCity()
            + "</TD>");
        ft.write("                                    <TD colspan='2'>&nbsp;&nbsp;"
            + invoice.getShipToAddress().getState() + "</TD>");
        ft.write("                                    <TD>&nbsp;&nbsp;"
            + invoice.getShipToAddress().getPostalCode() + "</TD>");
        ft.write("                                </TR>");
        ft.write("                                <TR>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + invoice.getShipToAddress().getRegion() + "</TD>");
        ft.write("                                    <TD></TD>");
        ft.write("                                    <TD colspan='2'>"
            + invoice.getShipAttention() + "</TD>");
        ft.write("                                </TR>");
        ft.write("                            </TBODY>");
        ft.write("                        </TABLE>");
        ft.write("                        </TD>");
        ft.write("                    </TR>");
        ft.write("                </TBODY>");
        ft.write("            </TABLE>");
        ft.write("            </TD>");
        ft.write("        </TR>");
        ft.write("        <TR height='445' valign='top'>");
        ft.write("            <TD width='787' valign='top'><BR>");
        ft.write("            <BR>");
        ft.write("            <BR>");
        ft.write("            <TABLE cellpadding='0' cellspacing='0'>");
        ft.write("                <TBODY>");

        // logger.error("9");
        Vector<InvoiceDetailsBean> invoiceDetails = invoice.getInvoiceDetails();
        Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
        int noElements = invoiceDetails.size();
        while (ennum.hasMoreElements()) {
          InvoiceDetailsBean iBean = ennum.nextElement();
          String partNumber = iBean.getPartNumber();
          int qty = iBean.getQuantity();
          String location = "";
          String model = "";
          String year = "";
          String desc = "";
          double list = 0.0;
          double cost = iBean.getSoldPrice();
          double totalCost = qty * cost;

          if (partNumber.startsWith("XX")) {
            totalCost = totalCost * -1;
            desc = "Damaged Discount For " + partNumber.substring(2);
          } else {
            PartsBean part = PartsBean.getPart(partNumber, null);
            location = part.getLocation();
            model = MakeModelBean.getMakeModelName(part.getMakeModelCode());
            String interModel = "";
            if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")) {
              interModel =
                  MakeModelBean.getMakeModelName(PartsBean.getPart(part.getInterchangeNo(), null)
                      .getMakeModelCode());
              interModel = cutModel(interModel);
            } else {
              Connection conX = DBInterfaceLocal.getSQLConnection();
              Statement stmtX = conX.createStatement();
              ResultSet rsX =
                  stmtX.executeQuery("Select * from Parts where InterChangeNo='" + part.getPartNo()
                      + "'");
              while (rsX.next()) {
                String interModel1 = "";
                String pNo = rsX.getString("PartNo");
                interModel1 =
                    MakeModelBean.getMakeModelName(PartsBean.getPart(pNo.trim(), null)
                        .getMakeModelCode());
                interModel1 = cutModel(interModel1);
                if (interModel.trim().equals("")) {
                  interModel += interModel1;
                } else {
                  interModel += "/ " + interModel1;
                }
              }
              conX.close();
              stmtX.close();
              rsX.close();
            }
            model = cutModel(model);
            if (!interModel.trim().equals("")) {
              model += "/ " + interModel;
            }
            year = part.getYear();
            desc = part.getPartDescription();
            list = iBean.getListPrice();
          }

          if (totalCost != 0) {
            totalCost = Double.parseDouble(NumberUtils.cutFractions(totalCost + ""));
          }
          String listStr = list + "";
          String costStr = cost + "";
          String totalCostStr = totalCost + "";
          /*
           * if (totalCost < 0) { totalCostStr = (totalCost * -1) + "C"; } else { totalCostStr =
           * totalCost+""; }
           */
          if (listStr.indexOf(".") == listStr.length() - 2)
            listStr += "0";
          if (costStr.indexOf(".") == costStr.length() - 2)
            costStr += "0";
          if (totalCostStr.indexOf(".") == totalCostStr.length() - 2)
            totalCostStr += "0";
          if (totalCostStr.indexOf("-") != -1) {
            totalCostStr =
                totalCostStr.substring(0, totalCostStr.indexOf("-"))
                    + totalCostStr.substring(totalCostStr.indexOf("-") + 1) + "C";
          }
          ft.write("                    <TR>");
          ft.write("                        <TD width='40'><B>" + partNumber + "</B></TD>");
          // ft.write("                        <TD width='40' style='font-size: 8pt;'>&nbsp;&nbsp;"
          // + location + "&nbsp;&nbsp;</TD>");
          ft.write("                        <TD width='40' style='font-size: 10pt;' >&nbsp;&nbsp;"
              + location + "&nbsp;&nbsp;</TD>");
          ft.write("                        <TD width='153' style='font-size: 10pt;'>" + model
              + "</TD>");
          ft.write("                        <TD width='60' style='font-size: 10pt;' >" + year
              + "</TD>");
          ft.write("                        <TD width='175' style='font-size: 10pt;'>" + desc
              + "</TD>");
          ft.write("                        <TD width='32'>" + (qty < 0 ? qty * -1 : qty) + "</TD>");
          ft.write("                        <TD width='54' >" + listStr + "</TD>");
          ft.write("                        <TD width='54' >" + costStr + "</TD>");
          ft.write("                        <TD width='50'>" + totalCostStr + "</TD>");
          ft.write("                    </TR>");
        }

        ft.write("                </TBODY>");
        ft.write("            </TABLE>");
        ft.write("            </TD>");
        ft.write("        </TR>");
        ft.write("    </TBODY>");
        ft.write("</TABLE>");
        ft.write("<table>");
        ft.write("	<TR>");
        String notes = "";
        if (invoice.getReturnedInvoice() != 0) {
          notes += "Returned Invoice # " + invoice.getReturnedInvoice() + " --- ";
        }
        if (invoice.getNotes() != null && !invoice.getNotes().trim().equals("")) {
          notes += invoice.getNotes();
        }
        String notes1 = "";
        String notes2 = "";
        if (notes.length() > 60) {
          notes1 = notes.substring(0, 60);
          if (notes.length() > 120)
            notes2 = notes.substring(61, 120);
          else
            notes2 = notes.substring(61);
        } else {
          notes1 = notes;
        }
        String tot = invoice.getInvoiceTotal() + "";
        if (tot.indexOf(".") == tot.length() - 2)
          tot += "0";
        if (tot.indexOf("-") != -1) {
          tot = tot.substring(0, tot.indexOf("-")) + tot.substring(tot.indexOf("-") + 1) + "C";
        }
        ft.write("		<TD align='left' width='600' style='font-size: 10pt;'>&nbsp;&nbsp;" + notes1
            + "</TD>");
        ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + tot + "</TD>");
        ft.write("	</TR>");
        ft.write("	<TR>");
        String tax = invoice.getTax() + "";
        if (tax.indexOf(".") == tax.length() - 2)
          tax += "0";
        if (tax.indexOf("-") != -1) {
          tax = tax.substring(0, tax.indexOf("-")) + tax.substring(tax.indexOf("-") + 1) + "C";
        }
        ft.write("		<TD align='left' width='600' style='font-size: 10pt;'>&nbsp;&nbsp;" + notes2
            + "</TD>");
        ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + tax + "</TD>");
        ft.write("	</TR>");

        ft.write("	<TR>");
        double total = invoice.getInvoiceTotal() + invoice.getTax() - invoice.getDiscount();
        if (total != 0) {
          try {
            if (total > 0 && total < 1000) {
              total = NumberUtils.cutFractions(total);
            } else {
              total = Double.parseDouble(NumberUtils.cutFractions(total + ""));
            }
          } catch (Exception e) {
          }
        }
        String totalStr = total + "";
        if (totalStr.indexOf(".") == totalStr.length() - 2)
          totalStr += "0";
        totalStr = NumberUtils.cutFractions(totalStr);
        if (totalStr.indexOf("-") != -1) {
          totalStr =
              totalStr.substring(0, totalStr.indexOf("-"))
                  + totalStr.substring(totalStr.indexOf("-") + 1) + "C";
        }

        ft.write("		<TD align='left' width='600' style='font-size: 10pt;'></TD>");
        ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + totalStr
            + "<BR/></TD>");
        ft.write("	</TR>");
        if (invoice.getDiscount() != 0) {
          ft.write("	<TR>");
          String disc = invoice.getDiscount() + "";
          if (disc.indexOf(".") == disc.length() - 2)
            disc += "0";
          if (invoice.getDiscountType().trim().equals("N")) {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>New Customer Disc.:  "
                + disc + "</TD>");
          } else if (invoice.getDiscountType().trim().equals("M")) {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Month End Sale Disc.:  "
                + disc + "</TD>");
          } else if (invoice.getDiscountType().trim().equals("X")) {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>New Customer + Over $500.00 Disc.:  "
                + disc + "</TD>");
          } else if (invoice.getDiscountType().trim().equals("R")) {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Return Customer Disc.:  "
                + disc + "</TD>");
          } else if (invoice.getDiscountType().trim().equals("O")) {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Over $500.00 Disc.:  "
                + disc + "</TD>");
          } else if (invoice.getDiscountType().trim().equals("P")) {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Sale Items Disc.:  "
                + disc + "</TD>");
          } else {
            ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Special Discount:  "
                + disc + "</TD>");
          }
          ft.write("	</TR>");
          ft.write("	<TR>");
        }
        ft.write("	<TR>");
        ft.write("		<TD align='left' width='550' style='font-size: 10pt;'></TD>");
        ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + paymentTerms
            + "<BR/></TD>");
        ft.write("	</TR>");
        ft.write("</table>");

        ft.write("</BODY>");
        ft.write("</HTML>");

        ft.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (UserException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return fine;

  }

  public static boolean createInvoiceFor60(InvoiceBean invoice) {
    boolean fine = true;
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/Invoice"
              + invoice.getInvoiceNumber() + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>");
      ft.write("<HTML>");
      ft.write("<HEAD>");
      ft.write("<meta http-equiv='Content-type' content='text/html;charset=UTF-8'>");
      ft.write("<TITLE>invoiceprint.html</TITLE>");
      ft.write("</HEAD>");
      ft.write("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
      ft.write("<BODY onload='PrintPage()'>");

      logger.error("Creating the Invoice Print for MSIE 6.0");
      ft.write("<P><BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("</P>");
      ft.write("<TABLE width='850' height='625'>");
      ft.write("    <TBODY>");
      ft.write("        <TR valign='bottom'>");
      ft.write("            <TD width='787' valign='bottom'>");
      ft.write("            <TABLE width='764' height='25'>");
      ft.write("                <TBODY>");
      ft.write("                    <TR>");
      String invoiceNoStr = invoice.getInvoiceNumber() + "";
      if (invoice.getReturnedInvoice() != 0) {
        invoiceNoStr = "C" + invoiceNoStr;
      }
      String paymentTerms = "";
      CustomerBean custBean = CustomerBean.getCustomer(invoice.getCustomerId());
      if (custBean.getPaymentTerms().trim().equals("O")) {
        paymentTerms = "* CASH ONLY *";
      } else if (custBean.getPaymentTerms().trim().equals("C")) {
        paymentTerms = " ** COD ** ";
      } else if (custBean.getPaymentTerms().trim().equals("B")) {
        paymentTerms = " * BI-WKLY * ";
      } else if (custBean.getPaymentTerms().trim().equals("W")) {
        paymentTerms = " * WKLY * ";
      } else if (custBean.getPaymentTerms().trim().equals("M")) {
        paymentTerms = " * MTHLY * ";
      }
      ft.write("                        <TD width='119' align='center'>"
          + invoice.getSalesPerson().toUpperCase() + "</TD>");
      // ft.write("                        <TD width='142'>&nbsp;&nbsp;&nbsp;"
      // + invoice.getOrderDate() + "</TD>");
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yy h:mm a");
      ft.write("                        <TD width='142'>&nbsp;&nbsp;&nbsp;"
          + sdf.format(new java.util.Date(invoice.getInvoiceTime())) + "</TD>");

      ft.write("                        <TD width='125'>" + invoice.getCustomerId() + "</TD>");
      // ft.write("                        <TD width='147'>" +
      // invoice.getShipVia() + "</TD>");
      ft.write("                        <TD width='120'>" + invoice.getShipVia() + "</TD>");
      ft.write("                        <TD style='font-size: 16pt;' width='145' align='left'>"
          + invoiceNoStr + "</TD>");
      ft.write("                    </TR>");
      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR valign='top' style='font-size: 11pt;' height='60'>");
      ft.write("            <TD valign='top'>");
      ft.write("            <TABLE>");
      ft.write("                <TBODY>");
      ft.write("                    <TR>");
      ft.write("                        <TD valign='top'><BR>");
      ft.write("                        <BR>");
      ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
      ft.write("                            <TBODY>");
      ft.write("                                <TR>");
      ft.write("                                    <TD width='10'></TD>");
      ft.write("                                    <TD width='142'></TD>");
      ft.write("                                    <TD width='35'></TD>");
      ft.write("                                    <TD width='51'></TD>");
      ft.write("                                    <TD width='60'></TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                	<TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + custBean.getCompanyName() + "&nbsp;" + paymentTerms + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getBillToAddress().getAddress1() + " </TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getBillToAddress().getCity()
          + "</TD>");
      ft.write("                                    <TD colspan='2'>"
          + invoice.getBillToAddress().getState() + "</TD>");
      ft.write("                                    <TD>"
          + invoice.getBillToAddress().getPostalCode() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getBillToAddress().getRegion() + "</TD>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='2'>" + invoice.getBillAttention()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                            </TBODY>");
      ft.write("                        </TABLE>");
      ft.write("                        </TD>");
      ft.write("                        <TD valign='top'><BR>");
      ft.write("                        <BR>");
      ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
      ft.write("                            <TBODY>");
      ft.write("                                <TR>");
      ft.write("                                    <TD width='67'></TD>");
      ft.write("                                    <TD width='146'></TD>");
      ft.write("                                    <TD width='43'></TD>");
      ft.write("                                    <TD width='45'></TD>");
      ft.write("                                    <TD width='50'></TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                	<TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getShipTo() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getShipToAddress().getAddress1() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getShipToAddress().getCity()
          + "</TD>");
      ft.write("                                    <TD colspan='2'>&nbsp;&nbsp;"
          + invoice.getShipToAddress().getState() + "</TD>");
      ft.write("                                    <TD>&nbsp;&nbsp;"
          + invoice.getShipToAddress().getPostalCode() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getShipToAddress().getRegion() + "</TD>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='2'>" + invoice.getShipAttention()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                            </TBODY>");
      ft.write("                        </TABLE>");
      ft.write("                        </TD>");
      ft.write("                    </TR>");
      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR height='445' valign='top'>");
      ft.write("            <TD width='787' valign='top'><BR>");
      ft.write("            <BR>");
      ft.write("            <BR>");
      ft.write("            <TABLE cellpadding='0' cellspacing='0'>");
      ft.write("                <TBODY>");

      // logger.error("9");
      Vector<InvoiceDetailsBean> invoiceDetails = invoice.getInvoiceDetails();
      Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
      int noElements = invoiceDetails.size();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean iBean = ennum.nextElement();
        String partNumber = iBean.getPartNumber();
        int qty = iBean.getQuantity();
        String location = "";
        String model = "";
        String year = "";
        String desc = "";
        double list = 0.0;
        double cost = iBean.getSoldPrice();
        double totalCost = qty * cost;

        if (partNumber.startsWith("XX")) {
          totalCost = totalCost * -1;
          desc = "Damaged Discount For " + partNumber.substring(2);
        } else {
          PartsBean part = PartsBean.getPart(partNumber, null);
          location = part.getLocation();
          model = MakeModelBean.getMakeModelName(part.getMakeModelCode());
          String interModel = "";
          if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")) {
            interModel =
                MakeModelBean.getMakeModelName(PartsBean.getPart(part.getInterchangeNo(), null)
                    .getMakeModelCode());
            interModel = cutModel(interModel);
          } else {
            Connection conX = DBInterfaceLocal.getSQLConnection();
            Statement stmtX = conX.createStatement();
            ResultSet rsX =
                stmtX.executeQuery("Select * from Parts where InterChangeNo='" + part.getPartNo()
                    + "'");
            while (rsX.next()) {
              String interModel1 = "";
              String pNo = rsX.getString("PartNo");
              interModel1 =
                  MakeModelBean.getMakeModelName(PartsBean.getPart(pNo.trim(), null)
                      .getMakeModelCode());
              interModel1 = cutModel(interModel1);
              if (interModel.trim().equals("")) {
                interModel += interModel1;
              } else {
                interModel += "/ " + interModel1;
              }
            }
            conX.close();
            stmtX.close();
            rsX.close();
          }
          model = cutModel(model);
          if (!interModel.trim().equals("")) {
            model += "/ " + interModel;
          }
          year = part.getYear();
          desc = part.getPartDescription();
          list = iBean.getListPrice();
        }

        if (totalCost != 0) {
          totalCost = Double.parseDouble(NumberUtils.cutFractions(totalCost + ""));
        }
        String listStr = list + "";
        String costStr = cost + "";
        String totalCostStr = totalCost + "";
        /*
         * if (totalCost < 0) { totalCostStr = (totalCost * -1) + "C"; } else { totalCostStr =
         * totalCost+""; }
         */
        if (listStr.indexOf(".") == listStr.length() - 2)
          listStr += "0";
        if (costStr.indexOf(".") == costStr.length() - 2)
          costStr += "0";
        if (totalCostStr.indexOf(".") == totalCostStr.length() - 2)
          totalCostStr += "0";
        if (totalCostStr.indexOf("-") != -1) {
          totalCostStr =
              totalCostStr.substring(0, totalCostStr.indexOf("-"))
                  + totalCostStr.substring(totalCostStr.indexOf("-") + 1) + "C";
        }
        ft.write("                    <TR>");
        ft.write("                        <TD width='40'><B>" + partNumber + "</B></TD>");
        // ft.write("                        <TD width='40' style='font-size: 8pt;'>&nbsp;&nbsp;"
        // + location + "&nbsp;&nbsp;</TD>");
        ft.write("                        <TD width='40' style='font-size: 10pt;' >&nbsp;&nbsp;"
            + location + "&nbsp;&nbsp;</TD>");
        ft.write("                        <TD width='153' style='font-size: 10pt;'>" + model
            + "</TD>");
        ft.write("                        <TD width='60' style='font-size: 10pt;' >" + year
            + "</TD>");
        ft.write("                        <TD width='175' style='font-size: 10pt;'>" + desc
            + "</TD>");
        ft.write("                        <TD width='32'>" + (qty < 0 ? qty * -1 : qty) + "</TD>");
        ft.write("                        <TD width='54' >" + listStr + "</TD>");
        ft.write("                        <TD width='54' >" + costStr + "</TD>");
        ft.write("                        <TD width='50'>" + totalCostStr + "</TD>");
        ft.write("                    </TR>");
      }

      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("    </TBODY>");
      ft.write("</TABLE>");
      ft.write("<table>");
      ft.write("	<TR>");
      String notes = "";
      if (invoice.getReturnedInvoice() != 0) {
        notes += "Returned Invoice # " + invoice.getReturnedInvoice() + " --- ";
      }
      if (invoice.getNotes() != null && !invoice.getNotes().trim().equals("")) {
        notes += invoice.getNotes();
      }
      String notes1 = "";
      String notes2 = "";
      if (notes.length() > 60) {
        notes1 = notes.substring(0, 60);
        if (notes.length() > 120)
          notes2 = notes.substring(61, 120);
        else
          notes2 = notes.substring(61);
      } else {
        notes1 = notes;
      }
      String tot = invoice.getInvoiceTotal() + "";
      if (tot.indexOf(".") == tot.length() - 2)
        tot += "0";
      if (tot.indexOf("-") != -1) {
        tot = tot.substring(0, tot.indexOf("-")) + tot.substring(tot.indexOf("-") + 1) + "C";
      }
      ft.write("		<TD align='left' width='600' style='font-size: 10pt;'>&nbsp;&nbsp;" + notes1
          + "</TD>");
      ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + tot + "</TD>");
      ft.write("	</TR>");
      ft.write("	<TR>");
      String tax = invoice.getTax() + "";
      if (tax.indexOf(".") == tax.length() - 2)
        tax += "0";
      if (tax.indexOf("-") != -1) {
        tax = tax.substring(0, tax.indexOf("-")) + tax.substring(tax.indexOf("-") + 1) + "C";
      }
      ft.write("		<TD align='left' width='600' style='font-size: 10pt;'>&nbsp;&nbsp;" + notes2
          + "</TD>");
      ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + tax + "</TD>");
      ft.write("	</TR>");

      ft.write("	<TR>");
      double total = invoice.getInvoiceTotal() + invoice.getTax() - invoice.getDiscount();
      if (total != 0) {
        try {
          if (total > 0 && total < 1000) {
            total = NumberUtils.cutFractions(total);
          } else {
            total = Double.parseDouble(NumberUtils.cutFractions(total + ""));
          }
        } catch (Exception e) {
        }
      }
      String totalStr = total + "";
      if (totalStr.indexOf(".") == totalStr.length() - 2)
        totalStr += "0";
      totalStr = NumberUtils.cutFractions(totalStr);
      if (totalStr.indexOf("-") != -1) {
        totalStr =
            totalStr.substring(0, totalStr.indexOf("-"))
                + totalStr.substring(totalStr.indexOf("-") + 1) + "C";
      }

      ft.write("		<TD align='left' width='600' style='font-size: 10pt;'></TD>");
      ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + totalStr + "<BR/></TD>");
      ft.write("	</TR>");
      if (invoice.getDiscount() != 0) {
        ft.write("	<TR>");
        String disc = invoice.getDiscount() + "";
        if (disc.indexOf(".") == disc.length() - 2)
          disc += "0";
        if (invoice.getDiscountType().trim().equals("N")) {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>New Customer Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("M")) {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Month End Sale Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("X")) {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>New Customer + Over $500.00 Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("R")) {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Return Customer Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("O")) {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Over $500.00 Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("P")) {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Sale Items Disc.:  "
              + disc + "</TD>");
        } else {
          ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Special Discount:  "
              + disc + "</TD>");
        }
        ft.write("	</TR>");
        ft.write("	<TR>");
      }
      ft.write("	<TR>");
      ft.write("		<TD align='left' width='550' style='font-size: 10pt;'></TD>");
      ft.write("		<TD align='right' width='60' style='font-size: 14pt;'>" + paymentTerms
          + "<BR/></TD>");
      ft.write("	</TR>");
      ft.write("</table>");

      ft.write("</BODY>");
      ft.write("</HTML>");

      ft.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
    return fine;

  }

  public static boolean createNewInvoice(InvoiceBean invoice) {
    boolean fine = true;
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/Invoice"
              + invoice.getInvoiceNumber() + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>");
      ft.write("<HTML>");
      ft.write("<HEAD>");
      ft.write("<meta http-equiv='Content-type' content='text/html;charset=UTF-8'>");
      ft.write("<TITLE>invoiceprint.html</TITLE>");
      ft.write("</HEAD>");
      ft.write("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
      ft.write("<BODY onload='PrintPage()'>");

      logger.error("Creating the Invoice Print for MSIE 6.0");
      ft.write("<P><BR>");
      ft.write("<TABLE>");
      ft.write("        <TR>");
      ft.write("            <TD style='font-size: 18pt;' aling='left'>");
      ft.write("            BEST VALUE AUTO PARTS INC.");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR>");
      ft.write("            <TD style='font-size: 16pt;' aling='left'>");
      ft.write("            D/B/A MAXXAM AUTO PARTS");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR>");
      ft.write("            <TD style='font-size: 11pt;' aling='left'>");
      ft.write("            132-10 11th AVE, COLLEGE POINT, NY - 11356");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR>");
      ft.write("            <TD style='font-size: 11pt;' aling='left'>");
      ft.write("            PH: (718) 746-6688   FX: (718) 746-0353");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("</TABLE>");
      ft.write("</P>");
      ft.write("<TABLE width='850' height='625'>");
      ft.write("    <TBODY>");
      ft.write("        <TR valign='bottom'>");
      ft.write("            <TD width='787' valign='bottom'>");
      ft.write("            <TABLE width='764' height='25'>");
      ft.write("                <TBODY>");

      ft.write("                    <TR>");
      ft.write("                        <TD width='119' align='center'>SALES REP</TD>");
      ft.write("                        <TD width='142'>&nbsp;&nbsp;DATE&TIME</TD>");
      ft.write("                        <TD width='125'>CUSTOMER ID.</TD>");
      ft.write("                        <TD width='120'>SHIP-VIA</TD>");
      ft.write("                        <TD style='font-size: 16pt;' width='145' align='left'>INVOICE</TD>");
      ft.write("                    </TR>");

      ft.write("<tr>");
      ft.write("<td colspan='5'>");
      ft.write("<hr align='left' noshade size='1px' width='680px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("                    <TR>");
      String invoiceNoStr = invoice.getInvoiceNumber() + "";
      if (invoice.getReturnedInvoice() != 0) {
        invoiceNoStr = "C" + invoiceNoStr;
      }
      String paymentTerms = "";
      CustomerBean custBean = CustomerBean.getCustomer(invoice.getCustomerId());
      if (custBean.getPaymentTerms().trim().equals("O")) {
        paymentTerms = "* CASH ONLY *";
      } else if (custBean.getPaymentTerms().trim().equals("C")) {
        paymentTerms = " ** COD ** ";
      } else if (custBean.getPaymentTerms().trim().equals("B")) {
        paymentTerms = " * BI-WKLY * ";
      } else if (custBean.getPaymentTerms().trim().equals("W")) {
        paymentTerms = " * WKLY * ";
      } else if (custBean.getPaymentTerms().trim().equals("M")) {
        paymentTerms = " * MTHLY * ";
      }
      ft.write("                        <TD width='119' align='center'>"
          + invoice.getSalesPerson().toUpperCase() + "</TD>");
      // ft.write("                        <TD width='142'>&nbsp;&nbsp;&nbsp;"
      // + invoice.getOrderDate() + "</TD>");
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yy h:mm a");
      ft.write("                        <TD width='142'>&nbsp;&nbsp;&nbsp;"
          + sdf.format(new java.util.Date(invoice.getInvoiceTime())) + "</TD>");

      ft.write("                        <TD width='125'>" + invoice.getCustomerId() + "</TD>");
      // ft.write("                        <TD width='147'>" +
      // invoice.getShipVia() + "</TD>");
      ft.write("                        <TD width='120'>" + invoice.getShipVia() + "</TD>");
      ft.write("                        <TD style='font-size: 16pt;' width='145' align='left'>"
          + invoiceNoStr + "</TD>");
      ft.write("                    </TR>");
      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR valign='top' style='font-size: 11pt;' height='60'>");
      ft.write("            <TD valign='top'>");
      ft.write("            <TABLE>");
      ft.write("                <TBODY>");
      ft.write("                    <TR>");
      ft.write("                        <TD valign='top'><BR>");
      // ft.write("                        <BR>");
      ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
      ft.write("                            <TBODY>");
      ft.write("                                <TR>");
      ft.write("                                    <TD width='10'></TD>");
      ft.write("                                    <TD width='142'></TD>");
      ft.write("                                    <TD width='35'></TD>");
      ft.write("                                    <TD width='51'></TD>");
      ft.write("                                    <TD width='60'></TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD colspan='5' style='font-size: 14pt;' align='left'>BILL TO:</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                	<TD></TD>");
      ft.write("                                    <TD colspan='4'>" + custBean.getCompanyName()
          + "" + paymentTerms + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='4'>"
          + invoice.getBillToAddress().getAddress1() + " </TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getBillToAddress().getCity()
          + "</TD>");
      ft.write("                                    <TD colspan='2'>"
          + invoice.getBillToAddress().getState() + "</TD>");
      ft.write("                                    <TD>"
          + invoice.getBillToAddress().getPostalCode() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getBillToAddress().getRegion()
          + "</TD>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='2'>" + invoice.getBillAttention()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                            </TBODY>");
      ft.write("                        </TABLE>");
      ft.write("                        </TD>");
      ft.write("                        <TD valign='top'><BR>");
      // ft.write("                        <BR>");
      ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
      ft.write("                            <TBODY>");
      ft.write("                                <TR>");
      ft.write("                                    <TD width='67'></TD>");
      ft.write("                                    <TD width='146'></TD>");
      ft.write("                                    <TD width='43'></TD>");
      ft.write("                                    <TD width='45'></TD>");
      ft.write("                                    <TD width='50'></TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD colspan='5' style='font-size: 14pt;' align='left'>SHIP TO:</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                	<TD></TD>");
      ft.write("                                    <TD colspan='4'>" + invoice.getShipTo()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='4'>"
          + invoice.getShipToAddress().getAddress1() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getShipToAddress().getCity()
          + "</TD>");
      ft.write("                                    <TD colspan='2'>"
          + invoice.getShipToAddress().getState() + "</TD>");
      ft.write("                                    <TD>"
          + invoice.getShipToAddress().getPostalCode() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getShipToAddress().getRegion()
          + "</TD>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='2'>" + invoice.getShipAttention()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                            </TBODY>");
      ft.write("                        </TABLE>");
      ft.write("                        </TD>");
      ft.write("                    </TR>");
      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR height='445' valign='top'>");
      ft.write("            <TD width='787' valign='top'><BR>");

      ft.write("            <TABLE cellpadding='0' cellspacing='0'>");
      ft.write("                <TBODY>");
      ft.write("                    <TR>");
      ft.write("                        <TD width='40'>Part #</TD>");
      ft.write("                        <TD width='40' style='font-size: 10pt;' >Location&nbsp;&nbsp;</TD>");
      ft.write("                        <TD width='153' style='font-size: 10pt;'>Model</TD>");
      ft.write("                        <TD width='60' style='font-size: 10pt;' >Year</TD>");
      ft.write("                        <TD width='175' style='font-size: 10pt;'>Description</TD>");
      ft.write("                        <TD width='32'>Qty</TD>");
      ft.write("                        <TD width='54' >List</TD>");
      ft.write("                        <TD width='54' >Cost</TD>");
      ft.write("                        <TD width='50'>Amount</TD>");
      ft.write("                    </TR>");
      ft.write("<tr>");
      ft.write("<td colspan='9'>");
      ft.write("<hr align='left' noshade size='1px' width='680px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      // logger.error("9");
      Vector<InvoiceDetailsBean> invoiceDetails = invoice.getInvoiceDetails();
      Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
      int noElements = invoiceDetails.size();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean iBean = ennum.nextElement();
        String partNumber = iBean.getPartNumber();
        int qty = iBean.getQuantity();
        String location = "";
        String model = "";
        String year = "";
        String desc = "";
        double list = 0.0;
        double cost = iBean.getSoldPrice();
        double totalCost = qty * cost;

        if (partNumber.startsWith("XX")) {
          totalCost = totalCost * -1;
          desc = "Damaged Discount For " + partNumber.substring(2);
        } else {
          PartsBean part = PartsBean.getPart(partNumber, null);
          location = part.getLocation();
          model = MakeModelBean.getMakeModelName(part.getMakeModelCode());
          String interModel = "";
          if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")) {
            interModel =
                MakeModelBean.getMakeModelName(PartsBean.getPart(part.getInterchangeNo(), null)
                    .getMakeModelCode());
            interModel = cutModel(interModel);
          } else {
            Connection conX = DBInterfaceLocal.getSQLConnection();
            Statement stmtX = conX.createStatement();
            ResultSet rsX =
                stmtX.executeQuery("Select * from Parts where InterChangeNo='" + part.getPartNo()
                    + "'");
            while (rsX.next()) {
              String interModel1 = "";
              String pNo = rsX.getString("PartNo");
              interModel1 =
                  MakeModelBean.getMakeModelName(PartsBean.getPart(pNo.trim(), null)
                      .getMakeModelCode());
              interModel1 = cutModel(interModel1);
              if (interModel.trim().equals("")) {
                interModel += interModel1;
              } else {
                interModel += "/ " + interModel1;
              }
            }
            rsX.close();
            stmtX.close();
            conX.close();
          }

          model = cutModel(model);
          if (!interModel.trim().equals("")) {
            model += "/ " + interModel;
          }
          year = part.getYear();
          desc = part.getPartDescription();
          list = iBean.getListPrice();
        }

        if (totalCost != 0) {
          totalCost = Double.parseDouble(NumberUtils.cutFractions(totalCost + ""));
        }
        String listStr = list + "";
        String costStr = cost + "";
        String totalCostStr = totalCost + "";
        /*
         * if (totalCost < 0) { totalCostStr = (totalCost * -1) + "C"; } else { totalCostStr =
         * totalCost+""; }
         */
        if (listStr.indexOf(".") == listStr.length() - 2)
          listStr += "0";
        if (costStr.indexOf(".") == costStr.length() - 2)
          costStr += "0";
        if (totalCostStr.indexOf(".") == totalCostStr.length() - 2)
          totalCostStr += "0";
        if (totalCostStr.indexOf("-") != -1) {
          totalCostStr =
              totalCostStr.substring(0, totalCostStr.indexOf("-"))
                  + totalCostStr.substring(totalCostStr.indexOf("-") + 1) + "C";
        }
        ft.write("                    <TR>");
        ft.write("                        <TD width='40'><B>" + partNumber + "</B></TD>");
        // ft.write("                        <TD width='40' style='font-size: 8pt;'>&nbsp;&nbsp;"
        // + location + "&nbsp;&nbsp;</TD>");
        ft.write("                        <TD width='40' style='font-size: 10pt;' >&nbsp;&nbsp;"
            + location + "&nbsp;&nbsp;</TD>");
        ft.write("                        <TD width='153' style='font-size: 10pt;'>" + model
            + "</TD>");
        ft.write("                        <TD width='60' style='font-size: 10pt;' >" + year
            + "</TD>");
        ft.write("                        <TD width='175' style='font-size: 10pt;'>" + desc
            + "</TD>");
        ft.write("                        <TD width='32'>" + (qty < 0 ? qty * -1 : qty) + "</TD>");
        ft.write("                        <TD width='54' >" + listStr + "</TD>");
        ft.write("                        <TD width='54' >" + costStr + "</TD>");
        ft.write("                        <TD width='50'>" + totalCostStr + "</TD>");
        ft.write("                    </TR>");
      }

      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("    </TBODY>");
      ft.write("</TABLE>");
      ft.write("<table>");
      ft.write("	<TR>");
      String notes = "";
      if (invoice.getReturnedInvoice() != 0) {
        notes += "Returned Invoice # " + invoice.getReturnedInvoice() + " --- ";
      }
      if (invoice.getNotes() != null && !invoice.getNotes().trim().equals("")) {
        notes += invoice.getNotes();
      }
      String notes1 = "";
      String notes2 = "";
      if (notes.length() > 60) {
        notes1 = notes.substring(0, 60);
        if (notes.length() > 120)
          notes2 = notes.substring(61, 120);
        else
          notes2 = notes.substring(61);
      } else {
        notes1 = notes;
      }
      String tot = invoice.getInvoiceTotal() + "";
      if (tot.indexOf(".") == tot.length() - 2)
        tot += "0";
      if (tot.indexOf("-") != -1) {
        tot = tot.substring(0, tot.indexOf("-")) + tot.substring(tot.indexOf("-") + 1) + "C";
      }
      ft.write("		<TD align='left' colspan='2' width='660' style='font-size: 10pt;'>&nbsp;&nbsp;Notes:"
          + notes1 + "</TD>");
      ft.write("	</TR>");
      ft.write("	<TR>");
      ft.write("		<TD align='right' width='560' style='font-size: 12pt;'>Sub Total:</TD>");
      ft.write("		<TD align='right' width='100' style='font-size: 12pt;'>" + tot + "</TD>");
      ft.write("	</TR>");
      ft.write("	<TR>");
      String tax = invoice.getTax() + "";
      if (tax.indexOf(".") == tax.length() - 2)
        tax += "0";
      if (tax.indexOf("-") != -1) {
        tax = tax.substring(0, tax.indexOf("-")) + tax.substring(tax.indexOf("-") + 1) + "C";
      }
      ft.write("		<TD align='right' width='560' style='font-size: 12pt;'>Tax:</TD>");
      ft.write("		<TD align='right' width='100' style='font-size: 12pt;'>" + tax + "</TD>");
      ft.write("	</TR>");

      double total = invoice.getInvoiceTotal() + invoice.getTax() - invoice.getDiscount();
      if (total != 0) {
        try {
          if (total > 0 && total < 1000) {
            total = NumberUtils.cutFractions(total);
          } else {
            total = Double.parseDouble(NumberUtils.cutFractions(total + ""));
          }
        } catch (Exception e) {
        }
      }
      String totalStr = total + "";
      if (totalStr.indexOf(".") == totalStr.length() - 2)
        totalStr += "0";
      totalStr = NumberUtils.cutFractions(totalStr);
      if (totalStr.indexOf("-") != -1) {
        totalStr =
            totalStr.substring(0, totalStr.indexOf("-"))
                + totalStr.substring(totalStr.indexOf("-") + 1) + "C";
      }

      if (invoice.getDiscount() != 0) {
        ft.write("	<TR>");
        String disc = invoice.getDiscount() + "";
        if (disc.indexOf(".") == disc.length() - 2)
          disc += "0";
        if (invoice.getDiscountType().trim().equals("N")) {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>New Customer Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("M")) {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>Month End Sale Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("X")) {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>New Customer + Over $500.00 Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("R")) {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>Return Customer Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("O")) {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>Over $500.00 Disc.:  "
              + disc + "</TD>");
        } else if (invoice.getDiscountType().trim().equals("P")) {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>Sale Items Disc.:  "
              + disc + "</TD>");
        } else {
          ft.write("		<TD align='right' colspan='2' width='660' style='font-size: 12pt;'>Special Discount:  "
              + disc + "</TD>");
        }
        ft.write("	</TR>");
      }

      ft.write("	<TR>");
      ft.write("		<TD align='right' width='560' style='font-size: 12pt;'>Total Amount:</TD>");
      ft.write("		<TD align='right' width='100' style='font-size: 12pt;'>" + totalStr
          + "<BR/></TD>");
      ft.write("	</TR>");

      ft.write("	<TR>");
      ft.write("		<TD align='left' width='560' style='font-size: 10pt;'></TD>");
      ft.write("		<TD align='right' width='100' style='font-size: 14pt;'>" + paymentTerms
          + "<BR/></TD>");
      ft.write("	</TR>");

      ft.write("</table>");

      ft.write("</BODY>");
      ft.write("</HTML>");

      ft.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
    /*
     * try { File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/Invoice" +
     * invoice.getInvoiceNumber() + ".html"); if (fileHtml.exists()) { logger.error("File Created");
     * fine = true; } else { logger.error("File Not Created"); fine = false; } } catch (Exception e)
     * { }
     */
    return fine;

  }

  public static void createTodaysOrders(UserBean user) {
    try {

      File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/TodaysOrders.html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td colspan='8' align='center' style='font-size: 16pt '>");
      ft.write("<B>Best Value Auto Body Supply - Todays Orders</B>");
      ft.write("<BR/>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr style='border: thin;'>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Invoice<BR/>Number", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Customer<BR/>ID", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Customer<BR/>Name", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Part No", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Location", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Description", 25));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Quantity", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Ship Via", 10));
      ft.write("</B></td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='8'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql = "SELECT a.InvoiceNumber, a.CustomerId, c.CompanyName, ";
      sql += "b.PartNumber, d.Location, d.PartDescription, b.Quantity, a.ShipVia ";
      sql += "FROM Invoice a, InvoiceDetails b, Customer c, Parts d ";
      sql += "WHERE ";
      sql +=
          "a.OrderDate = '" + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate()) + "' AND";
      // sql += "a.OrderDate = '" +
      // DateUtils.convertUSToMySQLFormat("11-27-2002") + "' AND";

      // if (!user.getRole().trim().equalsIgnoreCase("High") &&
      // !user.getUsername().trim().equalsIgnoreCase("bob")) {
      if (!user.getRole().trim().equalsIgnoreCase("High")) {
        sql += " a.SalesPerson='" + user.getUsername() + "' AND ";
      }

      sql += " a.InvoiceNumber=b.InvoiceNumber AND ";
      sql += " a.CustomerId=c.CustomerId AND ";
      sql += " b.PartNumber=d.PartNo ";
      sql += " ORDER BY 1 ASC, 4 ASC ";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String invNo = rs.getString(1);
        String custId = rs.getString(2);
        String compName = rs.getString(3);
        String partNo = rs.getString(4);
        String location = rs.getString(5);
        String desc = rs.getString(6);
        String qty = rs.getString(7);
        String ship = rs.getString(8);

        ft.write("<tr>");
        ft.write("<td>");
        ft.write(padSpaces(invNo, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(custId, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(compName, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(partNo, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(location, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(desc, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(qty, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(ship, 10));
        ft.write("</td>");
        ft.write("</tr>");
      }

      ft.write("<table/>");
      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static void createClientListing() {
    Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    try {
      con = DBInterfaceLocal.getSQLConnection();
      File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/ClientListing.html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td colspan='6' align='center' style='font-size: 20pt '>");
      ft.write("<B>BEST VALUE Auto Body Supply - Client Listing</B>");
      ft.write("<BR/>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Client Id", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Company Name", 30));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Contact Name", 20));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Contact Title", 20));
      ft.write("</B></td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("", 10));
      ft.write("</B></td>");
      ft.write("<td align='center' colspan='3'><B>");
      ft.write(padSpaces("Address", 100));
      ft.write("</B></td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      stmt = con.createStatement();
      // String sql =
      // "SELECT cust.CustomerId, cust.CompanyName, cust.ContactName, cust.ContactTitle, ";
      // sql +=
      // "addr.Addr1, addr.Addr2, addr.City, addr.State, addr.Region, addr.PostalCode, ";
      // sql += "addr.Country, addr.Phone, addr.Ext, addr.Fax ";
      // sql += "FROM Customer cust, Address addr ";
      // sql +=
      // "WHERE cust.CustomerId=addr.ID AND (addr.Type='Standard' OR addr.Who='Cust') ";
      // sql += " ORDER BY 2 ASC ";
      String sql = "SELECT CustomerId, CompanyName, ContactName, ContactTitle ";
      sql += " FROM Customer ";
      sql += " ORDER BY 2 ASC ";
      rs = stmt.executeQuery(sql);

      String sql2 =
          "SELECT Addr1, Addr2, City, State, Region, PostalCode, Country, Phone, Ext, Fax ";
      sql2 += "FROM Address WHERE ID=? AND Type='Standard' AND Who='Cust'";
      pstmt = con.prepareStatement(sql2);

      while (rs.next()) {
        String custId = rs.getString(1);
        String compName = rs.getString(2);
        String contName = rs.getString(3);
        String contTitle = rs.getString(4);

        // String recentInvoice =
        // CustomerBean.getCustomer(custId).getMostRecentInvoice();
        // if (recentInvoice.trim().equals("")) {
        // compName += " *** N O *** ";
        // } else {
        // compName += " * " + recentInvoice + " * ";
        // }

        pstmt.clearParameters();
        pstmt.setString(1, custId);

        rs2 = pstmt.executeQuery();

        String addr1 = "";
        String addr2 = "";
        String city = "";
        String state = "";
        String region = "";
        String postalCode = "";
        String country = "";
        String phone = "";
        String ext = "";
        String fax = "";

        if (rs2.next()) {
          addr1 = rs2.getString(1);
          addr2 = rs2.getString(2);
          city = rs2.getString(3);
          state = rs2.getString(4);
          region = rs2.getString(5);
          postalCode = rs2.getString(6);
          country = rs2.getString(7);
          phone = rs2.getString(8);
          ext = rs2.getString(9);
          fax = rs2.getString(10);
        }

        String address = "";

        if (addr1 != null && !addr1.trim().equals("")) {
          address += addr1.trim() + ", ";
        }
        if (addr2 != null && !addr2.trim().equals("")) {
          address += addr2.trim() + ", ";
        }
        if (city != null && !city.trim().equals("")) {
          address += city.trim() + ", ";
        }
        if (state != null && !state.trim().equals("")) {
          address += state.trim();
        }
        if (postalCode != null && !postalCode.trim().equals("")) {
          address += " - " + postalCode.trim() + ", ";
        }
        if (region != null && !region.trim().equals("")) {
          address += "  Re: " + region.trim() + ", ";
        }
        if (country != null && !country.trim().equals("")) {
          address += country.trim() + ", ";
        }
        if (phone != null && !phone.trim().equals("")) {
          address += "  Ph: " + phone.trim();
        }
        if (ext != null && !ext.trim().equals("")) {
          address += " X " + ext.trim();
        }
        if (fax != null && !fax.trim().equals("")) {
          address += "  Fax: " + fax.trim();
        }
        // if (fax != null && !fax.trim().equals("") &&
        // !fax.trim().equalsIgnoreCase("no fax")) { continue; }

        ft.write("<tr>");
        ft.write("<td><B>");
        ft.write(padSpaces(custId, 10));
        ft.write("</B></td>");
        ft.write("<td><B>");
        ft.write(padSpaces(compName, 10));
        ft.write("</B></td>");
        ft.write("<td><B>");
        ft.write(padSpaces(contName, 10));
        ft.write("</B></td>");
        ft.write("<td>");
        ft.write(padSpaces(contTitle, 10));
        ft.write("</td>");
        ft.write("</tr>");

        ft.write("<tr>");
        ft.write("<td>");
        ft.write(padSpaces("", 10));
        ft.write("</td>");
        ft.write("<td colspan='3'>");
        ft.write(address);
        ft.write("</td>");
        ft.write("</tr>");
      }

      ft.write("<table/>");

      ft.write(getFooters());
      ft.close();

      rs.close();
      rs2.close();
      stmt.close();
      pstmt.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static void createClientListing(String fromDate, String toDate, String befDate)
      throws UserException {

    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/ClientListing" + fromDate + toDate
              + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td colspan='6' align='center' style='font-size: 20pt '>");
      ft.write("<B>BEST VALUE Auto Body Supply - Client Listing</B>");
      ft.write("<BR/>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Client Id", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Company Name", 30));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Contact Name", 20));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Contact Title", 20));
      ft.write("</B></td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("", 10));
      ft.write("</B></td>");
      ft.write("<td align='center' colspan='3'><B>");
      ft.write(padSpaces("Address", 100));
      ft.write("</B></td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Connection con = DBInterfaceLocal.getSQLConnection();
      stmt = con.createStatement();
      // String sql =
      // "SELECT cust.CustomerId, cust.CompanyName, cust.ContactName, cust.ContactTitle, ";
      // sql +=
      // "addr.Addr1, addr.Addr2, addr.City, addr.State, addr.Region, addr.PostalCode, ";
      // sql += "addr.Country, addr.Phone, addr.Ext, addr.Fax ";
      // sql += "FROM Customer cust, Address addr ";
      // sql +=
      // "WHERE cust.CustomerId=addr.ID AND (addr.Type='Standard' OR addr.Who='Cust') ";
      // sql += " ORDER BY 2 ASC ";
      String sql = "SELECT CustomerId, CompanyName, ContactName, ContactTitle ";
      sql += " FROM Customer ";
      sql += " ORDER BY 2 ASC ";
      rs = stmt.executeQuery(sql);

      String sql2 =
          "SELECT Addr1, Addr2, City, State, Region, PostalCode, Country, Phone, Ext, Fax ";
      sql2 += "FROM Address WHERE ID=? AND Type='Standard' AND Who='Cust'";
      pstmt = con.prepareStatement(sql2);

      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
      logger.error(fromDate);
      java.util.Date dd1 = null;
      long time1 = 0;
      if (fromDate.trim().equals("")) {
        time1 = 0;
      } else {
        dd1 = sdf.parse(fromDate);
        time1 = dd1.getTime();
      }
      logger.error(time1);

      logger.error(toDate);
      java.util.Date dd2 = null;
      long time2 = 0;
      if (toDate.trim().equals("")) {
        time2 = 0;
      } else {
        dd2 = sdf.parse(toDate);
        time2 = dd2.getTime();
      }
      logger.error(time2);

      logger.error(befDate);
      java.util.Date dd3 = null;
      long time3 = 0;
      if (befDate.trim().equals("")) {
        time3 = 0;
      } else {
        dd3 = sdf.parse(befDate);
        time3 = dd3.getTime();
      }
      logger.error(time3);

      boolean neverCust = false;
      if (time1 == 0 && time2 == 0 && time3 == 0) {
        neverCust = true;
      }
      logger.error("Never Cust Is: " + neverCust);
      int totalCust = 0;
      int printCust = 0;
      int skipCust = 0;

      while (rs.next()) {
        String custId = rs.getString(1);
        String compName = rs.getString(2);
        String contName = rs.getString(3);
        String contTitle = rs.getString(4);

        totalCust++;
        String recentInvoice = CustomerBean.getCustomer(custId).getMostRecentInvoice();

        if (recentInvoice.trim().equals("") && !neverCust) {
          // logger.error("Skipping:"+custId+":"+recentInvoice);
          skipCust++;
          continue;
        } else if (!recentInvoice.trim().equals("") && neverCust) {
          skipCust++;
          continue;
        }

        if (!neverCust) {
          java.util.Date dd4 = sdf.parse(recentInvoice);
          if (time3 != 0) {
            if (dd4.getTime() > time3) {
              // logger.error("Skipping:"+custId+":"+recentInvoice);
              skipCust++;
              continue;
            }
            printCust++;
          } else if (time1 != 0 && time2 != 0) {
            if (dd4.getTime() < time1 || dd4.getTime() > time2) {
              // logger.error("Skipping:"+custId+":"+recentInvoice);
              skipCust++;
              continue;
            } else {
              printCust++;
              // logger.error("Good:"+custId+":"+recentInvoice);
            }
          } else {
            throw new UserException("Please enter some dates");
          }
        }

        compName += " * " + recentInvoice + " * ";

        pstmt.clearParameters();
        pstmt.setString(1, custId);

        rs2 = pstmt.executeQuery();

        String addr1 = "";
        String addr2 = "";
        String city = "";
        String state = "";
        String region = "";
        String postalCode = "";
        String country = "";
        String phone = "";
        String ext = "";
        String fax = "";

        if (rs2.next()) {
          addr1 = rs2.getString(1);
          addr2 = rs2.getString(2);
          city = rs2.getString(3);
          state = rs2.getString(4);
          region = rs2.getString(5);
          postalCode = rs2.getString(6);
          country = rs2.getString(7);
          phone = rs2.getString(8);
          ext = rs2.getString(9);
          fax = rs2.getString(10);
        }

        String address = "";

        if (addr1 != null && !addr1.trim().equals("")) {
          address += addr1.trim() + ", ";
        }
        if (addr2 != null && !addr2.trim().equals("")) {
          address += addr2.trim() + ", ";
        }
        if (city != null && !city.trim().equals("")) {
          address += city.trim() + ", ";
        }
        if (state != null && !state.trim().equals("")) {
          address += state.trim();
        }
        if (postalCode != null && !postalCode.trim().equals("")) {
          address += " - " + postalCode.trim() + ", ";
        }
        if (region != null && !region.trim().equals("")) {
          address += "  Re: " + region.trim() + ", ";
        }
        if (country != null && !country.trim().equals("")) {
          address += country.trim() + ", ";
        }
        if (phone != null && !phone.trim().equals("")) {
          address += "  Ph: " + phone.trim();
        }
        if (ext != null && !ext.trim().equals("")) {
          address += " X " + ext.trim();
        }
        if (fax != null && !fax.trim().equals("")) {
          address += "  Fax: " + fax.trim();
        }

        ft.write("<tr>");
        ft.write("<td><B>");
        ft.write(padSpaces(custId, 10));
        ft.write("</B></td>");
        ft.write("<td><B>");
        ft.write(padSpaces(compName, 10));
        ft.write("</B></td>");
        ft.write("<td><B>");
        ft.write(padSpaces(contName, 10));
        ft.write("</B></td>");
        ft.write("<td>");
        ft.write(padSpaces(contTitle, 10));
        ft.write("</td>");
        ft.write("</tr>");

        ft.write("<tr>");
        ft.write("<td>");
        ft.write(padSpaces("", 10));
        ft.write("</td>");
        ft.write("<td colspan='3'>");
        ft.write(address);
        ft.write("</td>");
        ft.write("</tr>");
      }

      logger.error("Total No. Of Customers: " + totalCust);
      logger.error("Printed Customers: " + printCust);
      logger.error("Skipped Customers: " + skipCust);

      ft.write("<table/>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      pstmt.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createSecondClientListing: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createSecondClientListing: " + e);
    }
  }

  public static void createVendorListing() {
    try {

      File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/VendorListing.html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td colspan='6' align='center' style='font-size: 16pt '>");
      ft.write("<B>Best Value Auto Body Supply - Vendor Listing</B>");
      ft.write("<BR/>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Vendor Id", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Company Name", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Contact Name", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Contact Title", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("Home Page", 10));
      ft.write("</B></td>");
      ft.write("<td align='center'><B>");
      ft.write(padSpaces("eMail", 25));
      ft.write("</B></td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6'>");
      ft.write("<hr align='center' noshade size='2px' width='700px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      String sql = "SELECT SupplierId, CompanyName, ContactName, ";
      sql += "ContactTitle, Homepage, eMail ";
      sql += "FROM Vendors ";
      sql += " ORDER BY 2 ASC ";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String vendId = rs.getString(1);
        String compName = rs.getString(2);
        String contName = rs.getString(3);
        String contTitle = rs.getString(4);
        String homePage = rs.getString(5);
        String eMail = rs.getString(6);

        ft.write("<tr>");
        ft.write("<td>");
        ft.write(padSpaces(vendId, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(compName, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(contName, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(contTitle, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(homePage, 10));
        ft.write("</td>");
        ft.write("<td>");
        ft.write(padSpaces(eMail, 10));
        ft.write("</td>");
        ft.write("</tr>");
      }

      ft.write("<table/>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static void createFax(FaxBean faxBean) {
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/Fax" + faxBean.getFaxNumber()
              + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      ft.write("<table>");

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td style='font-size: 26pt'>");
      ft.write("<B>BEST VALUE</B>");
      ft.write("</td>");
      ft.write("<td align='bottom' style='font-size: 20pt'>");
      ft.write("AUTO BODY SUPPLY, INC.");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("</table>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("Select Value From Properties Where Name='ThisLocation' ");
      int locat = 0;
      if (rs.next()) {
        locat = rs.getInt(1);
      }
      if (locat == 1) {
        ft.write("<BR/>4425 W.16TH.STREET.<BR/>CHICAGO, IL. 60623<BR/>PHONE: 773-762-1000<BR/>");
        ft.write("FAX:   773-542-5854<BR/>E-MAIL: <B>BESTVALUE@AMERITECH.NET</B><BR/><BR/>");
      } else if (locat == 2) {
        ft.write("<BR/>600 Webster St. NW<BR/>GRAND RAPIDS, MI. 49504<BR/>PHONE: 616-458-0200<BR/>");
        ft.write("FAX:   616-458-7299<BR/>E-MAIL: <B>BESTVALUE@AMERITECH.NET</B><BR/><BR/>");
      } else if (locat == 3) {
        ft.write("<BR/>130-10, 11 AVE<BR/>COLLEGE POINT, NY. 11356<BR/>PHONE: 718-746-6688<BR/>");
        ft.write("FAX:   718-746-0353<BR/>E-MAIL: <B>BESTVALUE@AMERITECH.NET</B><BR/><BR/>");
      } else {
        ft.write("<BR/>4425 W.16TH.STREET.<BR/>CHICAGO, IL. 60623<BR/>PHONE: 773-762-1000<BR/>");
        ft.write("FAX:   773-542-5854<BR/>E-MAIL: <B>BESTVALUE@AMERITECH.NET</B><BR/><BR/>");
      }
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td width='565' height='50'>");
      ft.write("<table border='1' style='border-style: solid;'>");
      ft.write("<tr>");
      ft.write("<td width='565' height='50'>");
      ft.write("Send to: <B>" + faxBean.getToWhom() + "</B>");
      ft.write("</td>");
      ft.write("<td width='565' height='50'>");
      ft.write("From: <B>" + faxBean.getFromWhom() + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td width='565' height='50'>");
      ft.write("Attention: <B>" + faxBean.getAttention() + "</B>");
      ft.write("</td>");
      ft.write("<td width='565' height='50'>");
      ft.write("Date: " + faxBean.getFaxDate());
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td width='565' height='50'>");
      ft.write("Fax Number: " + faxBean.getFaxTo());
      ft.write("</td>");
      ft.write("<td width='565' height='50'>");
      ft.write("Phone Number: " + faxBean.getPhoneTo());
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("</table>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      String checked1 = "";
      String checked2 = "";
      String checked3 = "";
      String checked4 = "";
      String checked5 = "";
      if (faxBean.getPriority() == 1) {
        checked1 = "CHECKED";
      } else if (faxBean.getPriority() == 2) {
        checked2 = "CHECKED";
      } else if (faxBean.getPriority() == 3) {
        checked3 = "CHECKED";
      } else if (faxBean.getPriority() == 4) {
        checked4 = "CHECKED";
      } else if (faxBean.getPriority() == 5) {
        checked5 = "CHECKED";
      } else {
        checked2 = "CHECKED";
      }

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<table><form action=''>");
      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<input type='checkbox' " + checked1 + "></input>");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("Urgent");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("<input type='checkbox' " + checked2 + "></input>");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("Reply ASAP");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("<input type='checkbox' " + checked3 + "></input>");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("Please comment");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("<input type='checkbox' " + checked4 + "></input>");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("Please review");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("<input type='checkbox' " + checked5 + "></input>");
      ft.write("</td>");
      ft.write("<td>");
      ft.write("For your information");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("</form></table>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td style='font-size: 15pt;'>");
      ft.write("Total pages, including cover: <B>" + faxBean.getPages() + "</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<B>Comments:</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");
      ft.write("<table border='1' style='border-style: solid;' width='600px' height='350px'><form action=''>");
      ft.write("<tr>");
      ft.write("<td style='font-size: " + faxBean.getCommentsSize() + "pt;'>");
      ft.write(newLines(faxBean.getComments()));
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("</form></table>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static void createFinanceNotice(String custId) {
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/FinanceNotice" + custId.trim()
              + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      String custName = "";
      String contName = "";
      String address = "";
      String city = "";
      String state = "";
      String zip = "";
      String region = "";
      String faxNo = "";
      String phone = "";

      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt1 = con.createStatement();
      String sql1 =
          "SELECT companyName, contactName from customer where customerid='" + custId + "'";
      ResultSet rs1 = stmt1.executeQuery(sql1);
      if (rs1.next()) {
        custName = rs1.getString("CompanyName");
        contName = rs1.getString("ContactName");
        if (custName == null || custName.trim().equalsIgnoreCase("null")) {
          custName = "";
        }
        if (contName == null || contName.trim().equalsIgnoreCase("null")) {
          contName = "";
        }
      }

      Statement stmt2 = con.createStatement();
      String sql2 =
          "SELECT * FROM Address WHERE Id='" + custId + "' AND Type='Standard' AND Who='Cust'";
      ResultSet rs2 = stmt2.executeQuery(sql2);
      if (rs2.next()) {
        address = rs2.getString("Addr1");
        String addr2 = rs2.getString("Addr2");
        if (addr2 != null && !addr2.trim().equalsIgnoreCase("null")) {
          address += ", " + addr2;
        }
        city = rs2.getString("City");
        state = rs2.getString("State");
        zip = rs2.getString("PostalCode");
        region = rs2.getString("Region");
        faxNo = rs2.getString("Fax");
        phone = rs2.getString("Phone");
        String ext = rs2.getString("Ext");
        if (ext != null && !ext.trim().equals("") && !ext.trim().equalsIgnoreCase("null")) {
          phone += " x " + ext;
        }

      }

      ft.write("<table>");

      ft.write("<tr>");
      ft.write("<td align='center' colspan='4' style='font-size: 24pt'>");
      ft.write("<B>BEST VALUE Auto Body Supply, Inc.</B><BR/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");

      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("Select Value From Properties Where Name='ThisLocation' ");
      int locat = 0;
      if (rs.next()) {
        locat = rs.getInt(1);
      }
      if (locat == 1) {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      } else if (locat == 2) {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>600 Webster St. NW, GRAND RAPIDS, MI. 49504</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (616) 458-0200  Fax: (616) 458-7299</B><BR/>");
      } else if (locat == 3) {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>132-10, 11 AVE, COLLEGE POINT, NY. 11356</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (718) 746-6688  Fax: (718) 746-0353</B><BR/>");
      } else {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      }

      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td align='center' colspan='4' style='font-size: 20pt'>");
      ft.write("<B>Pending Invoices for Customer</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4'>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='right' colspan='4'>");
      if (!contName.trim().equals("")) {
        ft.write("<BR/><B>ATTN : " + contName + "</B>");
      }
      if (!custName.trim().equals("")) {
        ft.write("<BR/><B>" + custName + "</B>");
      }
      ft.write("<BR/>" + address + "<BR/>" + city + ", " + state + ". " + zip);
      if (phone != null && !phone.trim().equals("") && !phone.trim().equalsIgnoreCase("null")) {
        ft.write("<BR/>PHONE: " + phone);
      }
      if (faxNo != null && !faxNo.trim().equals("") && !faxNo.trim().equalsIgnoreCase("null")) {
        ft.write(",&nbsp;&nbsp;FAX: " + faxNo);
      }
      if (region != null && !region.trim().equals("")) {
        ft.write("<BR/>ROUTE #:" + region);
      }
      ft.write("<BR/><BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr align='center' colspan='4' noshade size='2px' width='600px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Invoice Number</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Order Date</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Invoice Total</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Amount Pending</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr align='center' colspan='4' noshade size='2px' width='600px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Statement stmt3 = con.createStatement();
      String sql3 =
          "SELECT InvoiceNumber, OrderDate, (InvoiceTotal - Discount + Tax) total, Balance FROM Invoice WHERE CustomerId='"
              + custId + "' AND Balance!=0 Order by 1";
      ResultSet rs3 = stmt3.executeQuery(sql3);
      double totalPending = 0.0;
      while (rs3.next()) {
        int invNo = rs3.getInt("InvoiceNumber");
        String ordDate = DateUtils.convertMySQLToUSFormat(rs3.getString("OrderDate"));
        double invTotal = rs3.getDouble("total");
        // double tax = rs3.getDouble("Tax");
        double balance = rs3.getDouble("Balance");
        totalPending += balance;
        // double total = invTotal + tax;
        ft.write("<tr>");
        ft.write("<td width='150'>");
        ft.write(invNo + "");
        ft.write("</td>");
        ft.write("<td width='150'>");
        ft.write(ordDate);
        ft.write("</td>");
        ft.write("<td width='150'>");
        ft.write(invTotal + "");
        ft.write("</td>");
        ft.write("<td width='150'>");
        ft.write(balance + "");
        ft.write("</td>");
        ft.write("</tr>");
      }

      Vector<BouncedChecksBean> v = BouncedChecksBean.getAllBouncedChecks(custId);
      if (v != null) {
        Enumeration<BouncedChecksBean> ennumX = v.elements();
        while (ennumX.hasMoreElements()) {
          BouncedChecksBean bBean = ennumX.nextElement();
          totalPending += bBean.getBalance();
          ft.write("<tr>");
          ft.write("<td width='150'>");
          ft.write(bBean.getCheckNo() + "");
          ft.write("</td>");
          ft.write("<td width='150'>");
          ft.write(bBean.getCheckDate());
          ft.write("</td>");
          ft.write("<td width='150'>");
          ft.write("BNCE CHK " + bBean.getBouncedAmount() + " + FEE $ 25.00");
          ft.write("</td>");
          ft.write("<td width='150'>");
          ft.write(bBean.getBalance() + "");
          ft.write("</td>");
          ft.write("</tr>");

        }
      }

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<BR/><BR/><hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4' style='font-size: 16pt'>");
      totalPending = Double.parseDouble(NumberUtils.cutFractions(totalPending + ""));
      String totalPendingStr = totalPending + "";
      if (totalPendingStr.indexOf(".") == totalPendingStr.length() - 2)
        totalPendingStr += "0";
      ft.write("<B>Total Pending Amount: " + totalPendingStr + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4' style='font-size: 16pt' align='right'>");
      ft.write("<BR/><BR/><B>&quot Thank You For Choosing Best Value &quot</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();
      rs2.close();
      stmt2.close();
      rs3.close();
      stmt3.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static void createFinanceNotice(String custId, Hashtable<String, String> checkedInvoices) {

    java.io.File ff = new java.io.File("c:/Tomcat/webapps/bvaschicago/html/reports/SFN.html");
    ff.setWritable(true);
    ff.delete();
    org.apache.commons.io.FileUtils.deleteQuietly(ff);

    File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/SFN.html");
    FileWriter ft;
    Connection con = null;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    Statement stmt2 = null;
    ResultSet rs2 = null;
    Statement stmt = null;
    ResultSet rs = null;
    Statement stmt3 = null;
    ResultSet rs3 = null;

    try {

      ft = new FileWriter(fileHtml, true);
      ft.write(getHeaders());
      String custName = "";
      String contName = "";
      String address = "";
      String city = "";
      String state = "";
      String zip = "";
      String region = "";
      String faxNo = "";
      String phone = "";

      con = DBInterfaceLocal.getSQLConnection();
      stmt1 = con.createStatement();
      String sql1 =
          "SELECT companyName, contactName from customer where customerid='" + custId + "'";
      rs1 = stmt1.executeQuery(sql1);
      if (rs1.next()) {
        custName = rs1.getString("CompanyName");
        contName = rs1.getString("ContactName");
        if (custName == null || custName.trim().equalsIgnoreCase("null")) {
          custName = "";
        }
        if (contName == null || contName.trim().equalsIgnoreCase("null")) {
          contName = "";
        }
      }// if
      stmt2 = con.createStatement();
      String sql2 =
          "SELECT * FROM Address WHERE Id='" + custId + "' AND Type='Standard' AND Who='Cust'";
      rs2 = stmt2.executeQuery(sql2);
      if (rs2.next()) {
        address = rs2.getString("Addr1");
        String addr2 = rs2.getString("Addr2");
        if (addr2 != null && !addr2.trim().equalsIgnoreCase("null")) {
          address += ", " + addr2;
        }
        city = rs2.getString("City");
        state = rs2.getString("State");
        zip = rs2.getString("PostalCode");
        region = rs2.getString("Region");
        faxNo = rs2.getString("Fax");
        phone = rs2.getString("Phone");
        String ext = rs2.getString("Ext");
        if (ext != null && !ext.trim().equals("") && !ext.trim().equalsIgnoreCase("null")) {
          phone += " x " + ext;
        }

      } // if rs2
      ft.write("<table>");

      ft.write("<tr>");
      ft.write("<td align='center' colspan='4' style='font-size: 24pt'>");
      ft.write("<B>BEST VALUE Auto Body Supply, Inc.</B><BR/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");

      stmt = con.createStatement();
      rs = stmt.executeQuery("Select Value From Properties Where Name='ThisLocation' ");
      int locat = 0;
      if (rs.next()) {
        locat = rs.getInt(1);
      }

      if (locat == 1) {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      } else if (locat == 2) {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>600 Webster St. NW, GRAND RAPIDS, MI. 49504</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (616) 458-0200  Fax: (616) 458-7299</B><BR/>");
      } else if (locat == 3) {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>132-10, 11 AVE, COLLEGE POINT, NY. 11356</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (718) 746-6688  Fax: (718) 746-0353</B><BR/>");
      } else {
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      }

      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td align='center' colspan='4' style='font-size: 20pt'>");
      ft.write("<B>Statement of Account As of " + DateUtils.getNewUSDate() + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4'>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='right' colspan='4'>");
      if (!contName.trim().equals("")) {
        ft.write("<BR/><B>ATTN : " + contName + "</B>");
      }
      if (!custName.trim().equals("")) {
        ft.write("<BR/><B>" + custName + "</B>");
      }
      ft.write("<BR/>" + address + "<BR/>" + city + ", " + state + ". " + zip);
      if (phone != null && !phone.trim().equals("") && !phone.trim().equalsIgnoreCase("null")) {
        ft.write("<BR/>PHONE: " + phone);
      }
      if (faxNo != null && !faxNo.trim().equals("") && !faxNo.trim().equalsIgnoreCase("null")) {
        ft.write(",&nbsp;&nbsp;FAX: " + faxNo);
      }
      if (region != null && !region.trim().equals("")) {
        ft.write("<BR/>ROUTE #: " + region);
      }
      ft.write("<BR/><BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr align='center' colspan='4' noshade size='2px' width='600px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Invoice Number</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Order Date</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Invoice Total</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 14pt;' width='150'>");
      ft.write("<B>Amount Pending</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr align='center' colspan='4' noshade size='2px' width='600px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      stmt3 = con.createStatement();
      String sql3 =
          "SELECT InvoiceNumber, OrderDate, (InvoiceTotal - Discount + Tax) total, Balance FROM Invoice WHERE CustomerId='"
              + custId + "' AND Balance!=0 Order by 1";
      rs3 = stmt3.executeQuery(sql3);
      double totalPending = 0.0;

      while (rs3.next()) {
        int invNo = rs3.getInt("InvoiceNumber");
        String checked = checkedInvoices.get(invNo + "");
        if (checked == null || checked.trim().equals("")) {
          continue;
        }
        String ordDate = DateUtils.convertMySQLToUSFormat(rs3.getString("OrderDate"));
        double invTotal = rs3.getDouble("total");
        // double tax = rs3.getDouble("Tax");
        double balance = rs3.getDouble("Balance");
        totalPending += balance;
        // double total = invTotal + tax;
        ft.write("<tr>");
        ft.write("<td width='150'>");
        ft.write(invNo + "");
        ft.write("</td>");
        ft.write("<td width='150'>");
        ft.write(ordDate);
        ft.write("</td>");
        ft.write("<td width='150'>");
        ft.write(invTotal + "");
        ft.write("</td>");
        ft.write("<td width='150'>");
        ft.write(balance + "");
        ft.write("</td>");
        ft.write("</tr>");
      }

      Vector<BouncedChecksBean> v = BouncedChecksBean.getAllBouncedChecks(custId);
      if (v != null) {
        Enumeration<BouncedChecksBean> ennumX = v.elements();
        while (ennumX.hasMoreElements()) {
          BouncedChecksBean bBean = ennumX.nextElement();
          totalPending += bBean.getBalance();
          ft.write("<tr>");
          ft.write("<td width='150'>");
          ft.write("BC" + bBean.getCheckId());
          ft.write("</td>");
          ft.write("<td width='150'>");
          ft.write(bBean.getCheckNo() + "/ " + bBean.getCheckDate());
          ft.write("</td>");
          ft.write("<td width='150'>");
          ft.write("BNCE CHK " + bBean.getBouncedAmount() + " + FEE $ 25.00");
          ft.write("</td>");
          ft.write("<td width='150'>");
          ft.write(bBean.getBalance() + "");
          ft.write("</td>");
          ft.write("</tr>");

        }
      }// if

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<BR/><BR/><hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4' style='font-size: 16pt'>");
      totalPending = Double.parseDouble(NumberUtils.cutFractions(totalPending + ""));
      String totalPendingStr = totalPending + "";
      if (totalPendingStr.indexOf(".") == totalPendingStr.length() - 2)
        totalPendingStr += "0";
      ft.write("<B>Total Pending Amount: " + totalPendingStr + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4' style='font-size: 16pt' align='right'>");
      ft.write("<BR/><BR/><B>&quot Thank You For Choosing Best Value &quot</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");

      ft.write(getFooters());
      ft.flush();
      ft.close();

      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();
      rs2.close();
      stmt2.close();
      rs3.close();
      stmt3.close();
      con.close();

      ft = null;
      System.gc();

    } catch (IOException e) {
      logger.error(e.toString());
    } catch (SQLException e) {
      logger.error(e.toString());
    }

  }

  public static void createFinanceNotice2(String custId, Hashtable<String, String> checkedInvoices) {/*
                                                                                                      * try
                                                                                                      * {
                                                                                                      * 
                                                                                                      * File
                                                                                                      * fileHtml
                                                                                                      * =
                                                                                                      * new
                                                                                                      * File
                                                                                                      * (
                                                                                                      * "c:/Tomcat/webapps/bvaschicago/html/reports/SFN.html"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * FileWriter
                                                                                                      * ft
                                                                                                      * =
                                                                                                      * new
                                                                                                      * FileWriter
                                                                                                      * (
                                                                                                      * fileHtml
                                                                                                      * ,
                                                                                                      * false
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * getHeaders
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * String
                                                                                                      * custName
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * contName
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * address
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * city
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * state
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * zip
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * region
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * faxNo
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * phone
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * Connection
                                                                                                      * con
                                                                                                      * =
                                                                                                      * DBInterfaceLocal
                                                                                                      * .
                                                                                                      * getSQLConnection
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * Statement
                                                                                                      * stmt1
                                                                                                      * =
                                                                                                      * con
                                                                                                      * .
                                                                                                      * createStatement
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * sql1
                                                                                                      * =
                                                                                                      * "SELECT companyName, contactName from customer where customerid='"
                                                                                                      * +
                                                                                                      * custId
                                                                                                      * +
                                                                                                      * "'"
                                                                                                      * ;
                                                                                                      * ResultSet
                                                                                                      * rs1
                                                                                                      * =
                                                                                                      * stmt1
                                                                                                      * .
                                                                                                      * executeQuery
                                                                                                      * (
                                                                                                      * sql1
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * rs1
                                                                                                      * .
                                                                                                      * next
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * custName
                                                                                                      * =
                                                                                                      * rs1
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "CompanyName"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * contName
                                                                                                      * =
                                                                                                      * rs1
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "ContactName"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * custName
                                                                                                      * ==
                                                                                                      * null
                                                                                                      * ||
                                                                                                      * custName
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equalsIgnoreCase
                                                                                                      * (
                                                                                                      * "null"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * custName
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * if
                                                                                                      * (
                                                                                                      * contName
                                                                                                      * ==
                                                                                                      * null
                                                                                                      * ||
                                                                                                      * contName
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equalsIgnoreCase
                                                                                                      * (
                                                                                                      * "null"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * contName
                                                                                                      * =
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * }
                                                                                                      * 
                                                                                                      * Statement
                                                                                                      * stmt2
                                                                                                      * =
                                                                                                      * con
                                                                                                      * .
                                                                                                      * createStatement
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * sql2
                                                                                                      * =
                                                                                                      * "SELECT * FROM Address WHERE Id='"
                                                                                                      * +
                                                                                                      * custId
                                                                                                      * +
                                                                                                      * "' AND Type='Standard' AND Who='Cust'"
                                                                                                      * ;
                                                                                                      * ResultSet
                                                                                                      * rs2
                                                                                                      * =
                                                                                                      * stmt2
                                                                                                      * .
                                                                                                      * executeQuery
                                                                                                      * (
                                                                                                      * sql2
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * next
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * address
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "Addr1"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * addr2
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "Addr2"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * addr2
                                                                                                      * !=
                                                                                                      * null
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * addr2
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equalsIgnoreCase
                                                                                                      * (
                                                                                                      * "null"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * address
                                                                                                      * +=
                                                                                                      * ", "
                                                                                                      * +
                                                                                                      * addr2
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * city
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "City"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * state
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "State"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * zip
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "PostalCode"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * region
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "Region"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * faxNo
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "Fax"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * phone
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "Phone"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * ext
                                                                                                      * =
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "Ext"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * ext
                                                                                                      * !=
                                                                                                      * null
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * ext
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * ext
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equalsIgnoreCase
                                                                                                      * (
                                                                                                      * "null"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * phone
                                                                                                      * +=
                                                                                                      * " x "
                                                                                                      * +
                                                                                                      * ext
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * 
                                                                                                      * }
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<table>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 24pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>BEST VALUE Auto Body Supply, Inc.</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * Statement
                                                                                                      * stmt
                                                                                                      * =
                                                                                                      * con
                                                                                                      * .
                                                                                                      * createStatement
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ResultSet
                                                                                                      * rs
                                                                                                      * =
                                                                                                      * stmt
                                                                                                      * .
                                                                                                      * executeQuery
                                                                                                      * (
                                                                                                      * "Select Value From Properties Where Name='ThisLocation' "
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * int
                                                                                                      * locat
                                                                                                      * =
                                                                                                      * 0
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * rs
                                                                                                      * .
                                                                                                      * next
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * locat
                                                                                                      * =
                                                                                                      * rs
                                                                                                      * .
                                                                                                      * getInt
                                                                                                      * (
                                                                                                      * 1
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * if
                                                                                                      * (
                                                                                                      * locat
                                                                                                      * ==
                                                                                                      * 1
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * else
                                                                                                      * if
                                                                                                      * (
                                                                                                      * locat
                                                                                                      * ==
                                                                                                      * 2
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>600 Webster St. NW, GRAND RAPIDS, MI. 49504</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Phone: (616) 458-0200  Fax: (616) 458-7299</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * else
                                                                                                      * if
                                                                                                      * (
                                                                                                      * locat
                                                                                                      * ==
                                                                                                      * 3
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>132-10, 11 AVE, COLLEGE POINT, NY. 11356</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Phone: (718) 746-6688  Fax: (718) 746-0353</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * else
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='center' colspan='4' style='font-size: 20pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Statement of Account As of "
                                                                                                      * +
                                                                                                      * DateUtils
                                                                                                      * .
                                                                                                      * getNewUSDate
                                                                                                      * (
                                                                                                      * )
                                                                                                      * +
                                                                                                      * "</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<hr align='center' noshade size='2px' width='500px'/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td align='right' colspan='4'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * !
                                                                                                      * contName
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/><B>ATTN : "
                                                                                                      * +
                                                                                                      * contName
                                                                                                      * +
                                                                                                      * "</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * if
                                                                                                      * (
                                                                                                      * !
                                                                                                      * custName
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/><B>"
                                                                                                      * +
                                                                                                      * custName
                                                                                                      * +
                                                                                                      * "</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/>"
                                                                                                      * +
                                                                                                      * address
                                                                                                      * +
                                                                                                      * "<BR/>"
                                                                                                      * +
                                                                                                      * city
                                                                                                      * +
                                                                                                      * ", "
                                                                                                      * +
                                                                                                      * state
                                                                                                      * +
                                                                                                      * ". "
                                                                                                      * +
                                                                                                      * zip
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * phone
                                                                                                      * !=
                                                                                                      * null
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * phone
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * phone
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equalsIgnoreCase
                                                                                                      * (
                                                                                                      * "null"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/>PHONE: "
                                                                                                      * +
                                                                                                      * phone
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * if
                                                                                                      * (
                                                                                                      * faxNo
                                                                                                      * !=
                                                                                                      * null
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * faxNo
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * faxNo
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equalsIgnoreCase
                                                                                                      * (
                                                                                                      * "null"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * ",&nbsp;&nbsp;FAX: "
                                                                                                      * +
                                                                                                      * faxNo
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * if
                                                                                                      * (
                                                                                                      * region
                                                                                                      * !=
                                                                                                      * null
                                                                                                      * &&
                                                                                                      * !
                                                                                                      * region
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/>ROUTE #: "
                                                                                                      * +
                                                                                                      * region
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/><BR/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4' align='left'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<hr align='center' colspan='4' noshade size='2px' width='600px'/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td style='font-size: 14pt;' width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Invoice Number</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td style='font-size: 14pt;' width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Order Date</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td style='font-size: 14pt;' width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Invoice Total</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td style='font-size: 14pt;' width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Amount Pending</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4' align='left'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<hr align='center' colspan='4' noshade size='2px' width='600px'/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * Statement
                                                                                                      * stmt3
                                                                                                      * =
                                                                                                      * con
                                                                                                      * .
                                                                                                      * createStatement
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * sql3
                                                                                                      * =
                                                                                                      * "SELECT InvoiceNumber, OrderDate, (InvoiceTotal - Discount + Tax) total, Balance FROM Invoice WHERE CustomerId='"
                                                                                                      * +
                                                                                                      * custId
                                                                                                      * +
                                                                                                      * "' AND Balance!=0 Order by 1"
                                                                                                      * ;
                                                                                                      * ResultSet
                                                                                                      * rs3
                                                                                                      * =
                                                                                                      * stmt3
                                                                                                      * .
                                                                                                      * executeQuery
                                                                                                      * (
                                                                                                      * sql3
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * double
                                                                                                      * totalPending
                                                                                                      * =
                                                                                                      * 0.0
                                                                                                      * ;
                                                                                                      * while
                                                                                                      * (
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * next
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * int
                                                                                                      * invNo
                                                                                                      * =
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * getInt
                                                                                                      * (
                                                                                                      * "InvoiceNumber"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * checked
                                                                                                      * =
                                                                                                      * checkedInvoices
                                                                                                      * .
                                                                                                      * get
                                                                                                      * (
                                                                                                      * invNo
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * checked
                                                                                                      * ==
                                                                                                      * null
                                                                                                      * ||
                                                                                                      * checked
                                                                                                      * .
                                                                                                      * trim
                                                                                                      * (
                                                                                                      * )
                                                                                                      * .
                                                                                                      * equals
                                                                                                      * (
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * continue
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * String
                                                                                                      * ordDate
                                                                                                      * =
                                                                                                      * DateUtils
                                                                                                      * .
                                                                                                      * convertMySQLToUSFormat
                                                                                                      * (
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * getString
                                                                                                      * (
                                                                                                      * "OrderDate"
                                                                                                      * )
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * double
                                                                                                      * invTotal
                                                                                                      * =
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * getDouble
                                                                                                      * (
                                                                                                      * "total"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * /
                                                                                                      * /
                                                                                                      * double
                                                                                                      * tax
                                                                                                      * =
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * getDouble
                                                                                                      * (
                                                                                                      * "Tax"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * double
                                                                                                      * balance
                                                                                                      * =
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * getDouble
                                                                                                      * (
                                                                                                      * "Balance"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * totalPending
                                                                                                      * +=
                                                                                                      * balance
                                                                                                      * ;
                                                                                                      * /
                                                                                                      * /
                                                                                                      * double
                                                                                                      * total
                                                                                                      * =
                                                                                                      * invTotal
                                                                                                      * +
                                                                                                      * tax
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * invNo
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * ordDate
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * invTotal
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * balance
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * 
                                                                                                      * Vector
                                                                                                      * <
                                                                                                      * BouncedChecksBean
                                                                                                      * >
                                                                                                      * v
                                                                                                      * =
                                                                                                      * BouncedChecksBean
                                                                                                      * .
                                                                                                      * getAllBouncedChecks
                                                                                                      * (
                                                                                                      * custId
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * v
                                                                                                      * !=
                                                                                                      * null
                                                                                                      * )
                                                                                                      * {
                                                                                                      * Enumeration
                                                                                                      * <
                                                                                                      * BouncedChecksBean
                                                                                                      * >
                                                                                                      * ennumX
                                                                                                      * =
                                                                                                      * v
                                                                                                      * .
                                                                                                      * elements
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * while
                                                                                                      * (
                                                                                                      * ennumX
                                                                                                      * .
                                                                                                      * hasMoreElements
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * {
                                                                                                      * BouncedChecksBean
                                                                                                      * bBean
                                                                                                      * =
                                                                                                      * ennumX
                                                                                                      * .
                                                                                                      * nextElement
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * totalPending
                                                                                                      * +=
                                                                                                      * bBean
                                                                                                      * .
                                                                                                      * getBalance
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "BC"
                                                                                                      * +
                                                                                                      * bBean
                                                                                                      * .
                                                                                                      * getCheckId
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * bBean
                                                                                                      * .
                                                                                                      * getCheckNo
                                                                                                      * (
                                                                                                      * )
                                                                                                      * +
                                                                                                      * "/ "
                                                                                                      * +
                                                                                                      * bBean
                                                                                                      * .
                                                                                                      * getCheckDate
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "BNCE CHK "
                                                                                                      * +
                                                                                                      * bBean
                                                                                                      * .
                                                                                                      * getBouncedAmount
                                                                                                      * (
                                                                                                      * )
                                                                                                      * +
                                                                                                      * " + FEE $ 25.00"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td width='150'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * bBean
                                                                                                      * .
                                                                                                      * getBalance
                                                                                                      * (
                                                                                                      * )
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * }
                                                                                                      * }
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4' align='left'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/><BR/><hr noshade size='2px' width='500px'/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4' style='font-size: 16pt'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * totalPending
                                                                                                      * =
                                                                                                      * Double
                                                                                                      * .
                                                                                                      * parseDouble
                                                                                                      * (
                                                                                                      * NumberUtils
                                                                                                      * .
                                                                                                      * cutFractions
                                                                                                      * (
                                                                                                      * totalPending
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * )
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * String
                                                                                                      * totalPendingStr
                                                                                                      * =
                                                                                                      * totalPending
                                                                                                      * +
                                                                                                      * ""
                                                                                                      * ;
                                                                                                      * if
                                                                                                      * (
                                                                                                      * totalPendingStr
                                                                                                      * .
                                                                                                      * indexOf
                                                                                                      * (
                                                                                                      * "."
                                                                                                      * )
                                                                                                      * ==
                                                                                                      * totalPendingStr
                                                                                                      * .
                                                                                                      * length
                                                                                                      * (
                                                                                                      * )
                                                                                                      * -
                                                                                                      * 2
                                                                                                      * )
                                                                                                      * totalPendingStr
                                                                                                      * +=
                                                                                                      * "0"
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<B>Total Pending Amount: "
                                                                                                      * +
                                                                                                      * totalPendingStr
                                                                                                      * +
                                                                                                      * "</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4' align='left'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<hr noshade size='2px' width='500px'/>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<td colspan='4' style='font-size: 16pt' align='right'>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "<BR/><BR/><B>&quot Thank You For Choosing Best Value &quot</B>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</td>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</tr>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * "</table>"
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * 
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * write
                                                                                                      * (
                                                                                                      * getFooters
                                                                                                      * (
                                                                                                      * )
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * ft
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * rs
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * stmt
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * rs1
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * stmt1
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * rs2
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * stmt2
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * rs3
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * stmt3
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * con
                                                                                                      * .
                                                                                                      * close
                                                                                                      * (
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * catch
                                                                                                      * (
                                                                                                      * IOException
                                                                                                      * ioe
                                                                                                      * )
                                                                                                      * {
                                                                                                      * logger
                                                                                                      * .
                                                                                                      * error
                                                                                                      * (
                                                                                                      * "Exception in PrintUtils.createInvoice: "
                                                                                                      * +
                                                                                                      * ioe
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      * catch
                                                                                                      * (
                                                                                                      * Exception
                                                                                                      * e
                                                                                                      * )
                                                                                                      * {
                                                                                                      * logger
                                                                                                      * .
                                                                                                      * error
                                                                                                      * (
                                                                                                      * "Exception in PrintUtils.createInvoice: "
                                                                                                      * +
                                                                                                      * e
                                                                                                      * )
                                                                                                      * ;
                                                                                                      * }
                                                                                                      */
  }

  public static void createFinanceStatement() {
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/FS" + DateUtils.getNewUSDate()
              + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      Connection con = DBInterfaceLocal.getSQLConnection();

      ft.write("<table>");

      ft.write("<tr>");
      ft.write("<td align='center' colspan='6' style='font-size: 16pt'>");
      ft.write("<B>BEST VALUE Auto Body Supply, Inc.</B><BR/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");

      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("Select Value From Properties Where Name='ThisLocation' ");
      int locat = 0;
      if (rs.next()) {
        locat = rs.getInt(1);
      }
      if (locat == 1) {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      } else if (locat == 2) {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>600 Webster St. NW, GRAND RAPIDS, MI. 49504</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (616) 458-0200  Fax: (616) 458-7299</B><BR/>");
      } else if (locat == 3) {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>132-10, 11 AVE, COLLEGE POINT, NY. 11356</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (718) 746-6688  Fax: (718) 746-0353</B><BR/>");
      } else {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      }

      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td align='center' colspan='6' style='font-size: 14pt'>");
      ft.write("<B>Finance Statement   -   " + DateUtils.getNewUSDate() + "</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='6' align='left'>");
      ft.write("<hr align='center' colspan='6' noshade size='2px' width='660px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td style='font-size: 12pt;' width='70'>");
      ft.write("<B>Sl. No</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Invoice No</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='320'>");
      ft.write("<B>Customer</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Cash</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Check No</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Amount</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr align='center' colspan='4' noshade size='2px' width='660px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Statement stmt1 = con.createStatement();
      String sql1 =
          "SELECT a.InvoiceNumber, a.AppliedAmount, a.PaymentType, c.CompanyName FROM AppliedAmounts a, Invoice b, Customer c WHERE AppliedDate='"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "' and a.InvoiceNumber Not Like 'BC%' and a.InvoiceNumber=b.InvoiceNumber and b.CustomerId=c.CustomerId Order By InvoiceNumber";
      ResultSet rs1 = stmt1.executeQuery(sql1);
      int slNo = 0;
      int totalTrans = 0;
      int cashTrans = 0;
      int checkTrans = 0;
      double totalCash = 0.0;
      double totalChecks = 0.0;
      double totalAmount = 0.0;

      while (rs1.next()) {
        slNo++;
        int invNo = rs1.getInt("InvoiceNumber");
        double cash = 0.0;
        double check = 0.0;
        String cashStr = "&nbsp;";
        String checkStr = "&nbsp;";
        String checkNo = rs1.getString("PaymentType");
        if (checkNo == null)
          checkNo = "";
        if (checkNo.trim().equals("")) {
          checkNo = "&nbsp;";
          cash = rs1.getDouble("AppliedAmount");
          totalCash += cash;
          cashTrans++;
          cashStr = cash + "";
        } else {
          check = rs1.getDouble("AppliedAmount");
          totalChecks += check;
          totalChecks = Double.parseDouble(NumberUtils.cutFractions(totalChecks + ""));
          // logger.error("Adding Amount: " + check);
          // logger.error("Total Amount: " + totalChecks);
          checkTrans++;
          checkStr = check + "";
        }

        // String custName =
        // CustomerBean.getCustomer(InvoiceBean.getInvoice(invNo).getCustomerId()).getCompanyName();
        String custName = rs1.getString("CompanyName");
        ft.write("<tr>");
        ft.write("<td width='70' style='font-size: 10pt;'>");
        ft.write(slNo + "");
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(invNo + "");
        ft.write("</td>");
        ft.write("<td width='320' style='font-size: 10pt;'>");
        ft.write(custName);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(cashStr);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkNo);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkStr);
        ft.write("</td>");
        ft.write("</tr>");
      }

      Statement stmt2 = con.createStatement();
      String sql2 =
          "SELECT InvoiceNumber, AppliedAmount, PaymentType FROM AppliedAmounts WHERE AppliedDate='"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "' and InvoiceNumber Like 'BC%' Order By InvoiceNumber";
      ResultSet rs2 = stmt1.executeQuery(sql2);

      while (rs2.next()) {
        slNo++;
        String chkId = rs2.getString("InvoiceNumber");
        // logger.error(chkId);
        double cash = 0.0;
        double check = 0.0;
        String cashStr = "&nbsp;";
        String checkStr = "&nbsp;";
        String checkNo = rs2.getString("PaymentType");
        if (checkNo == null)
          checkNo = "";
        if (checkNo.trim().equals("")) {
          checkNo = "&nbsp;";
          cash = rs2.getDouble("AppliedAmount");
          totalCash += cash;
          cashTrans++;
          cashStr = cash + "";
        } else {
          check = rs2.getDouble("AppliedAmount");
          totalChecks += check;
          checkTrans++;
          checkStr = check + "";
        }

        String custName =
            CustomerBean.getCustomer(
                BouncedChecksBean.getBouncedCheck(Integer.parseInt(chkId.substring(2)), "")
                    .getCustomerId()).getCompanyName();
        // String custName = rs1.getString("CompanyName");
        ft.write("<tr>");
        ft.write("<td width='70' style='font-size: 10pt;'>");
        ft.write(slNo + "");
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(chkId + "");
        ft.write("</td>");
        ft.write("<td width='320' style='font-size: 10pt;'>");
        ft.write(custName);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(cashStr);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkNo);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkStr);
        ft.write("</td>");
        ft.write("</tr>");
      }

      totalTrans = cashTrans + checkTrans;
      totalAmount = totalCash + totalChecks;

      ft.write("<tr>");
      ft.write("<td colspan='6' align='left'>");
      ft.write("<BR/><BR/><hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      ft.write("<B>Cash Transactions: " + cashTrans + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      totalCash = Double.parseDouble(NumberUtils.cutFractions(totalCash + ""));
      String totalCashStr = totalCash + "";
      if (totalCashStr.indexOf(".") == totalCashStr.length() - 2)
        totalCashStr += "0";
      ft.write("<B>Total Cash Amount: " + totalCashStr + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      ft.write("<B>Check Transactions: " + checkTrans + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      totalChecks = Double.parseDouble(NumberUtils.cutFractions(totalChecks + ""));
      String totalChecksStr = totalChecks + "";
      if (totalChecksStr.indexOf(".") == totalChecksStr.length() - 2)
        totalChecksStr += "0";
      ft.write("<B>Total Check Amount: " + totalChecksStr + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      ft.write("<B>All Transactions: " + totalTrans + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      totalAmount = Double.parseDouble(NumberUtils.cutFractions(totalAmount + ""));
      String totalAmountStr = totalAmount + "";
      if (totalAmountStr.indexOf(".") == totalAmountStr.length() - 2)
        totalAmountStr += "0";
      ft.write("<B>Total Amount: " + totalAmountStr + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' align='left'>");
      ft.write("<hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();
      rs2.close();
      stmt2.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createFinanceStatement: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createFinanceStatement: " + e);
    }
  }

  public static void createFinanceStatement1() {
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/FA" + DateUtils.getNewUSDate()
              + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      Connection con = DBInterfaceLocal.getSQLConnection();

      ft.write("<table>");

      ft.write("<tr>");
      ft.write("<td align='center' colspan='6' style='font-size: 16pt'>");
      ft.write("<B>BEST VALUE Auto Body Supply, Inc.</B><BR/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");

      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("Select Value From Properties Where Name='ThisLocation' ");
      int locat = 0;
      if (rs.next()) {
        locat = rs.getInt(1);
      }
      if (locat == 1) {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      } else if (locat == 2) {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>600 Webster St. NW, GRAND RAPIDS, MI. 49504</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (616) 458-0200  Fax: (616) 458-7299</B><BR/>");
      } else if (locat == 3) {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>132-10, 11 AVE, COLLEGE POINT, NY. 11356</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (718) 746-6688  Fax: (718) 746-0353</B><BR/>");
      } else {
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
        ft.write("</td>");
        ft.write("</tr>");
        ft.write("<tr>");
        ft.write("<td align='center' colspan='6' style='font-size: 12pt'>");
        ft.write("<B>Phone: (773) 762-1000  Fax: (773) 542-5854</B><BR/>");
      }

      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td align='center' colspan='6' style='font-size: 14pt'>");
      ft.write("<B>Adjustments For   -   " + DateUtils.getNewUSDate() + "</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='6' align='left'>");
      ft.write("<hr align='center' colspan='6' noshade size='2px' width='660px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td style='font-size: 12pt;' width='70'>");
      ft.write("<B>Sl. No</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Invoice No</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='320'>");
      ft.write("<B>Company Name</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Remark</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 12pt;' width='100'>");
      ft.write("<B>Amount</B>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td colspan='4' align='left'>");
      ft.write("<hr align='center' colspan='4' noshade size='2px' width='660px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      Statement stmt1 = con.createStatement();
      String sql1 =
          "SELECT a.InvoiceNumber, a.AppliedAmount, a.PaymentType, c.CompanyName FROM AppliedAmounts a, Invoice b, Customer c WHERE AppliedDate='"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "' and a.InvoiceNumber Not Like 'BC%' and (a.PaymentType like 'apto%' or a.PaymentType like 'ap to%' or a.PaymentType like 'apcr%' or a.PaymentType like 'ap cr%') and a.InvoiceNumber=b.InvoiceNumber and b.CustomerId=c.CustomerId Order By InvoiceNumber";
      ResultSet rs1 = stmt1.executeQuery(sql1);
      int slNo = 0;
      int totalTrans = 0;
      int checkTrans = 0;
      double totalChecks = 0.0;
      double totalAmount = 0.0;

      while (rs1.next()) {
        slNo++;
        int invNo = rs1.getInt("InvoiceNumber");
        double check = 0.0;
        String checkStr = "&nbsp;";
        String checkNo = rs1.getString("PaymentType");
        if (checkNo == null)
          checkNo = "";
        check = rs1.getDouble("AppliedAmount");
        totalChecks += check;
        totalChecks = Double.parseDouble(NumberUtils.cutFractions(totalChecks + ""));
        checkTrans++;
        checkStr = check + "";

        // String custName =
        // CustomerBean.getCustomer(InvoiceBean.getInvoice(invNo).getCustomerId()).getCompanyName();
        String custName = rs1.getString("CompanyName");
        ft.write("<tr>");
        ft.write("<td width='70' style='font-size: 10pt;'>");
        ft.write(slNo + "");
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(invNo + "");
        ft.write("</td>");
        ft.write("<td width='320' style='font-size: 10pt;'>");
        ft.write(custName);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkNo);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkStr);
        ft.write("</td>");
        ft.write("</tr>");
      }

      Statement stmt2 = con.createStatement();
      String sql2 =
          "SELECT InvoiceNumber, AppliedAmount, PaymentType FROM AppliedAmounts WHERE AppliedDate='"
              + DateUtils.convertUSToMySQLFormat(DateUtils.getNewUSDate())
              + "' and InvoiceNumber Like 'BC%' and (PaymentType like 'apto%' or PaymentType like 'ap to%' or PaymentType like 'apcr%' or PaymentType like 'ap cr%') Order By InvoiceNumber";
      ResultSet rs2 = stmt1.executeQuery(sql2);

      while (rs2.next()) {
        slNo++;
        String chkId = rs2.getString("InvoiceNumber");
        // logger.error(chkId);
        double check = 0.0;
        String checkStr = "&nbsp;";
        String checkNo = rs2.getString("PaymentType");
        if (checkNo == null)
          checkNo = "";

        check = rs2.getDouble("AppliedAmount");
        totalChecks += check;
        checkTrans++;
        checkStr = check + "";

        String custName =
            CustomerBean.getCustomer(
                BouncedChecksBean.getBouncedCheck(Integer.parseInt(chkId.substring(2)), "")
                    .getCustomerId()).getCompanyName();
        // String custName = rs1.getString("CompanyName");
        ft.write("<tr>");
        ft.write("<td width='70' style='font-size: 10pt;'>");
        ft.write(slNo + "");
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(chkId + "");
        ft.write("</td>");
        ft.write("<td width='320' style='font-size: 10pt;'>");
        ft.write(custName);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkNo);
        ft.write("</td>");
        ft.write("<td width='100' style='font-size: 10pt;'>");
        ft.write(checkStr);
        ft.write("</td>");
        ft.write("</tr>");
      }

      totalTrans = checkTrans;
      totalAmount = totalChecks;

      ft.write("<tr>");
      ft.write("<td colspan='6' align='left'>");
      ft.write("<BR/><BR/><hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      ft.write("<B>No. of Adjustments: " + checkTrans + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' style='font-size: 12pt'>");
      totalChecks = Double.parseDouble(NumberUtils.cutFractions(totalChecks + ""));
      String totalChecksStr = totalChecks + "";
      if (totalChecksStr.indexOf(".") == totalChecksStr.length() - 2)
        totalChecksStr += "0";
      ft.write("<B>Total Amount: " + totalChecksStr + "</B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='6' align='left'>");
      ft.write("<hr noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      rs1.close();
      stmt1.close();
      rs2.close();
      stmt2.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createFinanceStatement: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createFinanceStatement: " + e);
    }
  }

  public static void createInvoice(InvoiceBean invoice) {
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/Invoice"
              + invoice.getInvoiceNumber() + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      // ft.write(getHeaders());
      ft.write("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>");
      ft.write("<HTML>");
      ft.write("<HEAD>");
      ft.write("<meta http-equiv='Content-type' content='text/html;charset=UTF-8'>");
      ft.write("<TITLE>invoiceprint.html</TITLE>");
      ft.write("</HEAD>");
      ft.write("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
      ft.write("<BODY onload='PrintPage()'>");

      ft.write("<P><BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("<BR>");
      ft.write("</P>");
      ft.write("<TABLE width='850' height='610'>");
      ft.write("    <TBODY>");
      ft.write("        <TR>");
      ft.write("            <TD width='787'>");
      ft.write("            <TABLE width='764'>");
      ft.write("                <TBODY>");
      ft.write("                    <TR>");
      ft.write("                        <TD style='font-size: 14pt;' width='197'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getInvoiceNumber() + "</TD>");
      ft.write("                        <TD width='142'>" + invoice.getOrderDate() + "</TD>");
      ft.write("                        <TD width='125'>" + invoice.getCustomerId() + "</TD>");
      // ft.write("                        <TD width='147'>" +
      // invoice.getShipVia() + "</TD>");
      ft.write("                        <TD width='110'>" + invoice.getShipVia() + "</TD>");
      ft.write("                        <TD width='119'>" + invoice.getSalesPerson().toUpperCase()
          + "</TD>");
      ft.write("                    </TR>");
      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR valign='top' style='font-size: 11pt;' height='70'>");
      ft.write("            <TD valign='top'>");
      ft.write("            <TABLE>");
      ft.write("                <TBODY>");
      ft.write("                    <TR>");
      ft.write("                        <TD valign='top'><BR>");
      ft.write("                        <BR>");
      ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
      ft.write("                            <TBODY>");
      ft.write("                                <TR>");
      ft.write("                                    <TD width='72'></TD>");
      ft.write("                                    <TD width='142'></TD>");
      ft.write("                                    <TD width='35'></TD>");
      ft.write("                                    <TD width='51'></TD>");
      ft.write("                                    <TD width='60'></TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                	<TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + CustomerBean.getCustomer(invoice.getCustomerId()).getCompanyName() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getBillToAddress().getAddress1() + " </TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getBillToAddress().getCity()
          + "</TD>");
      ft.write("                                    <TD colspan='2'>"
          + invoice.getBillToAddress().getState() + "</TD>");
      ft.write("                                    <TD>"
          + invoice.getBillToAddress().getPostalCode() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getBillToAddress().getRegion() + "</TD>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='2'>" + invoice.getBillAttention()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                            </TBODY>");
      ft.write("                        </TABLE>");
      ft.write("                        </TD>");
      ft.write("                        <TD valign='top'><BR>");
      ft.write("                        <BR>");
      ft.write("                        <TABLE cellspacing='0' cellpadding='0'>");
      ft.write("                            <TBODY>");
      ft.write("                                <TR>");
      ft.write("                                    <TD width='67'></TD>");
      ft.write("                                    <TD width='146'></TD>");
      ft.write("                                    <TD width='43'></TD>");
      ft.write("                                    <TD width='45'></TD>");
      ft.write("                                    <TD width='50'></TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                	<TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getShipTo() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getShipToAddress().getAddress1() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>" + invoice.getShipToAddress().getCity()
          + "</TD>");
      ft.write("                                    <TD colspan='2'>&nbsp;&nbsp;"
          + invoice.getShipToAddress().getState() + "</TD>");
      ft.write("                                    <TD>&nbsp;&nbsp;"
          + invoice.getShipToAddress().getPostalCode() + "</TD>");
      ft.write("                                </TR>");
      ft.write("                                <TR>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
          + invoice.getShipToAddress().getRegion() + "</TD>");
      ft.write("                                    <TD></TD>");
      ft.write("                                    <TD colspan='2'>" + invoice.getShipAttention()
          + "</TD>");
      ft.write("                                </TR>");
      ft.write("                            </TBODY>");
      ft.write("                        </TABLE>");
      ft.write("                        </TD>");
      ft.write("                    </TR>");
      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("        <TR height='445' valign='top'>");
      ft.write("            <TD width='787' valign='top'><BR>");
      ft.write("            <BR>");
      ft.write("            <BR>");
      ft.write("            <TABLE>");
      ft.write("                <TBODY>");

      logger.error("9");
      Vector<InvoiceDetailsBean> invoiceDetails = invoice.getInvoiceDetails();
      Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
      int noElements = invoiceDetails.size();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean iBean = ennum.nextElement();

        String partNumber = iBean.getPartNumber();
        int qty = iBean.getQuantity();
        String location = "";
        String model = "";
        String year = "";
        String desc = "";
        double list = 0.0;
        double cost = iBean.getSoldPrice();
        double totalCost = qty * cost;

        if (partNumber.startsWith("XX")) {
          totalCost = totalCost * -1;
          desc = "Damaged Discount For " + partNumber.substring(2);
        } else {
          PartsBean part = PartsBean.getPart(partNumber, null);
          location = part.getLocation();
          model = MakeModelBean.getMakeModelName(part.getMakeModelCode());
          String interModel = "";
          if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")) {
            interModel =
                MakeModelBean.getMakeModelName(PartsBean.getPart(part.getInterchangeNo(), null)
                    .getMakeModelCode());
            interModel = cutModel(interModel);
          } else {
            Statement stmtX = DBInterfaceLocal.getSQLConnection().createStatement();
            ResultSet rsX =
                stmtX.executeQuery("Select * from Parts where InterChangeNo='" + part.getPartNo()
                    + "'");
            while (rsX.next()) {
              String interModel1 = "";
              String pNo = rsX.getString("PartNo");
              interModel1 =
                  MakeModelBean.getMakeModelName(PartsBean.getPart(pNo.trim(), null)
                      .getMakeModelCode());
              interModel1 = cutModel(interModel1);
              if (interModel.trim().equals("")) {
                interModel += interModel1;
              } else {
                interModel += "/ " + interModel1;
              }
            }
            rsX.close();
            stmtX.close();
          }
          model = cutModel(model);
          if (!interModel.trim().equals("")) {
            model += "/ " + interModel;
          }
          year = part.getYear();
          desc = part.getPartDescription();
          list = part.getListPrice();
        }

        // String partNumber = iBean.getPartNumber();
        // int qty = iBean.getQuantity();
        // PartsBean part = PartsBean.getPart(partNumber);
        // String location = part.getLocation();
        // String model =
        // MakeModelBean.getMakeModelName(part.getMakeModelCode());
        // model = cutModel(model);
        // String year = part.getYear();
        // String desc = part.getPartDescription();
        // double list = part.getListPrice();
        // double cost = part.getCostPrice();
        // double cost = iBean.getSoldPrice();
        // double totalCost = qty*cost;

        totalCost = NumberUtils.cutFractions(totalCost);
        String listStr = "";
        if (list != 0) {
          listStr = list + "";
        }
        String costStr = cost + "";
        String totalCostStr = totalCost + "";
        if (listStr.indexOf(".") == listStr.length() - 2)
          listStr += "0";
        if (costStr.indexOf(".") == costStr.length() - 2)
          costStr += "0";
        if (totalCostStr.indexOf(".") == totalCostStr.length() - 2)
          totalCostStr += "0";

        ft.write("                    <TR>");
        ft.write("                        <TD width='108'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + partNumber + "</TD>");
        ft.write("                        <TD width='40' style='font-size: 8pt;'>&nbsp;&nbsp;"
            + location + "&nbsp;&nbsp;</TD>");
        ft.write("                        <TD width='157' style='font-size: 10pt;'>" + model
            + "</TD>");
        ft.write("                        <TD width='60'>" + year + "</TD>");
        ft.write("                        <TD width='180' style='font-size: 10pt;'>" + desc
            + "</TD>");
        ft.write("                        <TD width='23'>" + qty + "</TD>");
        ft.write("                        <TD width='45'>" + listStr + "</TD>");
        ft.write("                        <TD width='45'>" + costStr + "</TD>");
        ft.write("                        <TD width='50'>" + totalCostStr + "</TD>");
        ft.write("                    </TR>");
      }

      ft.write("                </TBODY>");
      ft.write("            </TABLE>");
      ft.write("            </TD>");
      ft.write("        </TR>");
      ft.write("    </TBODY>");
      ft.write("</TABLE>");
      ft.write("<table cellspacing='0' cellpadding='0'>");
      ft.write("	<TR>");
      String tot = invoice.getInvoiceTotal() + "";
      if (tot.indexOf(".") == tot.length() - 2)
        tot += "0";
      ft.write("		<TD align='right' width='670' style='font-size: 14pt;'>" + tot + "</TD>");
      ft.write("	</TR>");
      ft.write("	<TR>");
      String tax = invoice.getTax() + "";
      if (tax.indexOf(".") == tax.length() - 2)
        tax += "0";
      ft.write("		<TD align='right' width='670' style='font-size: 14pt;'>" + tax + "</TD>");
      ft.write("	</TR>");
      ft.write("	<TR>");

      double total = invoice.getInvoiceTotal() + invoice.getTax() - invoice.getDiscount();
      if (total < 1000) {
        total = NumberUtils.cutFractions(total);
      } else {
        total = Double.parseDouble(NumberUtils.cutFractions(total + ""));
      }
      String totalStr = total + "";
      if (totalStr.indexOf(".") == totalStr.length() - 2)
        totalStr += "0";

      ft.write("		<TD align='right' width='670' style='font-size: 14pt;'>" + totalStr + "</TD>");
      ft.write("	</TR>");
      if (invoice.getDiscount() != 0) {
        ft.write("	<TR>");
        String disc = invoice.getDiscount() + "";
        if (disc.indexOf(".") == disc.length() - 2)
          disc += "0";
        ft.write("		<TD align='right' width='660' style='font-size: 14pt;'>Discount:  " + disc
            + "</TD>");
        ft.write("	</TR>");
        ft.write("	<TR>");
      }
      ft.write("</table>");

      ft.write("</BODY>");
      ft.write("</HTML>");

      // ft.write(getFooters());
      ft.close();

    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }

  }

  public static void createOldInvoice(InvoiceBean invoice) {
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/Invoice"
              + invoice.getInvoiceNumber() + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      ft.write("<BR/><BR/><BR/><BR/><BR/><BR/><BR/><BR/><BR/>");
      ft.write(padSpaces("", 20)
          + padSpaces(invoice.getInvoiceNumber() + "", calLen(invoice.getInvoiceNumber() + "", 31))
          + padSpaces(invoice.getOrderDate(), calLen(invoice.getOrderDate(), 39))
          + padSpaces(invoice.getCustomerId(), calLen(invoice.getCustomerId(), 36))
          + padSpaces(invoice.getShipVia(), calLen(invoice.getShipVia(), 42))
          + invoice.getSalesPerson());
      ft.write("<BR/><BR/><BR/><BR/>");
      String custName = CustomerBean.getCustomer(invoice.getCustomerId()).getCompanyName();
      ft.write(padSpaces("", 20) + padSpaces(custName, calLen(custName, 103)) + invoice.getShipTo());
      ft.write("<BR/>");
      ft.write(padSpaces("", 22)
          + padSpaces(invoice.getBillToAddress().getAddress1(),
              calLen(invoice.getBillToAddress().getAddress1(), 93))
          + invoice.getShipToAddress().getAddress1());
      ft.write("<BR/>");
      ft.write(padSpaces("", 18)
          + padSpaces(invoice.getBillToAddress().getCity(),
              calLen(invoice.getBillToAddress().getCity(), 46))
          + padSpaces(invoice.getBillToAddress().getState(),
              calLen(invoice.getBillToAddress().getState(), 19))
          + padSpaces(invoice.getBillToAddress().getPostalCode(),
              calLen(invoice.getBillToAddress().getPostalCode(), 42))
          + padSpaces(invoice.getShipToAddress().getCity(),
              calLen(invoice.getShipToAddress().getCity(), 55))
          + padSpaces(invoice.getShipToAddress().getState(),
              calLen(invoice.getShipToAddress().getState(), 22))
          + invoice.getShipToAddress().getPostalCode());
      ft.write("<BR/>");
      ft.write(padSpaces("", 20)
          + padSpaces(invoice.getBillToAddress().getRegion(),
              calLen(invoice.getBillToAddress().getRegion(), 41))
          + padSpaces(invoice.getBillAttention(), calLen(invoice.getBillAttention(), 53))
          + padSpaces(invoice.getShipToAddress().getRegion(),
              calLen(invoice.getShipToAddress().getRegion(), 40)) + invoice.getShipAttention());
      ft.write("<BR/><BR/><BR/><BR/>");

      Vector<InvoiceDetailsBean> invoiceDetails = invoice.getInvoiceDetails();
      Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
      int noElements = invoiceDetails.size();
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean iBean = ennum.nextElement();
        String partNumber = iBean.getPartNumber();
        int qty = iBean.getQuantity();
        PartsBean part = PartsBean.getPart(partNumber, null);
        String location = part.getLocation();
        String model = MakeModelBean.getMakeModelName(part.getMakeModelCode());
        String interModel = "";
        if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")) {
          interModel =
              MakeModelBean.getMakeModelName(PartsBean.getPart(part.getInterchangeNo(), null)
                  .getMakeModelCode());
          interModel = cutModel(interModel);
        } else {
          Statement stmtX = DBInterfaceLocal.getSQLConnection().createStatement();
          ResultSet rsX =
              stmtX.executeQuery("Select * from Parts where InterChangeNo='" + part.getPartNo()
                  + "'");
          while (rsX.next()) {
            String interModel1 = "";
            String pNo = rsX.getString("PartNo");
            interModel1 =
                MakeModelBean.getMakeModelName(PartsBean.getPart(pNo.trim(), null)
                    .getMakeModelCode());
            interModel1 = cutModel(interModel1);
            if (interModel.trim().equals("")) {
              interModel += interModel1;
            } else {
              interModel += "/ " + interModel1;
            }
          }
          rsX.close();
          stmtX.close();
        }
        model = cutModel(model);
        if (!interModel.trim().equals("")) {
          model += "/ " + interModel;
        }
        String year = part.getYear();
        String desc = part.getPartDescription();
        double list = part.getListPrice();
        double cost = part.getCostPrice();

        ft.write(padSpaces("", 10) + padSpaces(partNumber, calLen(partNumber, 21))
            + padSpaces(location, calLen(location, 13)) + padSpaces(model, calLen(model, 28))
            + padSpaces(year, calLen(year, 25)) + padSpaces(desc, calLen(desc, 52))
            + padSpaces(qty + "", calLen(qty + "", 13))
            + padSpaces(list + "", calLen(list + "", 17)) + cost);
        ft.write("<BR/>");
      }

      for (int i = 0; i < 23 - noElements; i++) {
        ft.write("<BR/>");
      }

      ft.write(padSpaces("", 160) + invoice.getInvoiceTotal());
      ft.write("<BR/>");
      ft.write(padSpaces("", 160) + invoice.getTax());
      ft.write("<BR/>");
      double total = invoice.getInvoiceTotal() + invoice.getTax();
      ft.write(padSpaces("", 160) + total);
      ft.write(getFooters());
      ft.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }

  }

  public static String getHeaders() {
    StringBuffer headers = new StringBuffer("");
    headers
        .append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
    headers
        .append("<HTML><HEAD><meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
    // headers.append("<STYLE TYPE='text/css'> <!-- p { font-size: 13pt; } --> </STYLE> ");
    headers.append("<TITLE>Print Invoice</TITLE></HEAD>");
    headers
        .append("<script language='JavaScript'>	function PrintPage() {	window.print();	window.close(); }</script>");
    headers.append("<BODY onload='PrintPage()'>");
    return headers.toString();
  }

  public static String getFooters() {
    StringBuffer footers = new StringBuffer("");
    footers.append("</BODY></HTML>");

    return footers.toString();
  }

  public static String padSpaces(String str, int len) {
    if (str == null || str.trim().equals("null"))
      str = "";
    str = str.trim();
    int len1 = str.length();
    while (len1 < len) {
      str += "&nbsp;";
      len1++;
    }
    str += "&nbsp;";
    return str;
  }

  public static String newLines(String str) {
    int len = 0;
    if (str != null && (len = str.indexOf("\n")) != -1) {
      str = str.substring(0, len) + "<BR/>" + newLines(str.substring(len + 1));
    }
    return str;
  }

  public static String cutModel(String model) {
    int len = 0;
    if (model.length() > 15) {
      model = model.substring(0, 15);
    }
    if ((len = model.indexOf("(")) != -1) {
      model = model.substring(0, len);
    }
    return model;
  }

  public static int calLen(String str, int totSize) {
    int len = str.length();
    totSize = totSize - (len * 2);
    for (int i = 0; i < 6; i++) {
      if (str.indexOf(" ") != -1) {
        totSize = totSize - 1;
        str = str.substring(str.indexOf(" ") + 1);
      }
    }
    return totSize;
  }

  public static void printVendorOrder(VendorOrderBean orderBean) {
    try {

      Connection con1 = DBInterfaceLocal.getSQLConnection();
      Statement stmt = null;
      Statement stmtX = null;
      ResultSet rsX = null;

      File fileHtml = null;
      File fileHtmlWO = null;
      fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/VendorOrder"
              + orderBean.getOrderNo() + ".html");
      fileHtmlWO =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/VendorOrderWOP"
              + orderBean.getOrderNo() + ".html");
      FileWriter ft = new FileWriter(fileHtml);
      FileWriter ftWO = new FileWriter(fileHtmlWO);
      int supId = orderBean.getSupplierId();
      ft.write(getHeaders());
      ftWO.write(getHeaders());

      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td colspan='8' align='center' style='font-size: 16pt '>");
      ft.write("<table>");
      ft.write("<tr>");
      ft.write("<td style='font-size: 20pt '>");
      ft.write("<B>BEST VALUE</B>");
      ft.write("</td>");
      ft.write("<td style='font-size: 16pt '>");
      ft.write("<B> Auto Body Supply - Vendor Order</B>");
      ft.write("</td>");
      // ft.write("<BR/>");
      ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='2'>");
      ft.write("<B><hr align='center' noshade size='2px' width='700px'/></B>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("</table>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td>");

      ftWO.write("<table>");
      ftWO.write("<tr>");
      ftWO.write("<td colspan='8' align='center' style='font-size: 16pt '>");
      ftWO.write("<table>");
      ftWO.write("<tr>");
      ftWO.write("<td style='font-size: 20pt '>");
      ftWO.write("<B>BEST VALUE</B>");
      ftWO.write("</td>");
      ftWO.write("<td style='font-size: 16pt '>");
      ftWO.write("<B> Auto Body Supply - Vendor Order</B>");
      ftWO.write("</td>");
      // ft.write("<BR/>");
      ftWO.write("</tr>");
      ftWO.write("<tr>");
      ftWO.write("<td colspan='2'>");
      ftWO.write("<B><hr align='center' noshade size='2px' width='700px'/></B>");
      ftWO.write("</td>");
      ftWO.write("</tr>");
      ftWO.write("</table>");
      ftWO.write("</td>");
      ftWO.write("</tr>");

      ftWO.write("<tr>");
      ftWO.write("<td>");

      int pageNo = 1;
      int lineNo = 0;
      Enumeration<VendorOrderedItemsBean> ennum = orderBean.getOrderedItems().elements();

      int range1 = 35;
      int range2 = 38;
      if (supId == 2) {
        range1 = 20;
        range2 = 20;
      }

      double totalCuft = 0.0;
      int withCuft = 0;
      int totItems = 0;

      while (ennum.hasMoreElements()) {
        totItems++;
        lineNo++;
        if (lineNo == 1) {
          getVendorHeader(ft, supId, false);
          getVendorHeader(ftWO, supId, true);
        } else if (pageNo == 1 && lineNo == range1) {
          ft.write("</table>");
          ft.write("</td>");
          ft.write("</tr>");
          ft.write("<tr>");
          ft.write("<td>");
          ft.write("<br/><br/>");
          ft.write("</td>");
          ft.write("</tr>");
          ft.write("<tr>");
          ft.write("<td>");

          ftWO.write("</table>");
          ftWO.write("</td>");
          ftWO.write("</tr>");
          ftWO.write("<tr>");
          ftWO.write("<td>");
          ftWO.write("<br/><br/>");
          ftWO.write("</td>");
          ftWO.write("</tr>");
          ftWO.write("<tr>");
          ftWO.write("<td>");

          getVendorHeader(ft, supId, false);
          getVendorHeader(ftWO, supId, true);
          lineNo = 1;
          pageNo++;
        } else if (pageNo > 1 && lineNo == range2) {
          ft.write("</table>");
          ft.write("</td>");
          ft.write("</tr>");
          ft.write("<tr>");
          ft.write("<td>");
          ft.write("<br/>");
          ft.write("</td>");
          ft.write("</tr>");
          ft.write("<tr>");
          ft.write("<td>");

          ftWO.write("</table>");
          ftWO.write("</td>");
          ftWO.write("</tr>");
          ftWO.write("<tr>");
          ftWO.write("<td>");
          ftWO.write("<br/>");
          ftWO.write("</td>");
          ftWO.write("</tr>");
          ftWO.write("<tr>");
          ftWO.write("<td>");

          getVendorHeader(ft, supId, false);
          getVendorHeader(ftWO, supId, true);
          lineNo = 1;
          pageNo++;
        }
        VendorOrderedItemsBean voiBean = ennum.nextElement();
        String partNo = voiBean.getPartNo();
        String vendorPartNo = voiBean.getVendorPartNo();
        int qty = voiBean.getQuantity();
        String partDesc1 = "";
        String partDesc2 = "";
        String plNumber = "";
        String oemNumber = "";
        String vendorPrice = "";
        String cuft = "";
        String ourCuft = "";
        String capa = "N";
        VendorItemBean viBean =
            VendorItemBean.getThePart(orderBean.getSupplierId(), "", vendorPartNo, con1);
        if (viBean != null) {
          partDesc1 = viBean.getItemDesc1();
          partDesc2 = viBean.getItemDesc2();
          plNumber = viBean.getPlNo();
          oemNumber = viBean.getOemNo();
          vendorPrice = voiBean.getPrice() + "";
          if (vendorPrice.indexOf(".") == vendorPrice.length() - 2)
            vendorPrice += "0";
          cuft = viBean.getNoOfPieces() + "PCS/ " + viBean.getItemSize();
          if (viBean.getItemSize() != 0) {
            double ourItemSize = viBean.getItemSize() * (qty / viBean.getNoOfPieces());
            ourItemSize = NumberUtils.cutFractions(ourItemSize);
            totalCuft += ourItemSize;
            withCuft++;
            String ourItemSizeStr = ourItemSize + "";
            ourItemSizeStr = ourItemSizeStr.substring(0, ourItemSizeStr.indexOf(".") + 2);
            // ourCuft = qty + "PCS/ " + ourItemSizeStr;
            ourCuft = qty + "";
          } else {
            ourCuft = qty + "";
          }
          PartsBean partBean = PartsBean.getPart(partNo, con1);
          if (partBean != null) {
            if (partBean.getPartDescription().indexOf("CAPA") != -1
                || partBean.getPartDescription().indexOf("Capa") != -1
                || partBean.getPartDescription().indexOf("capa") != -1) {
              capa = "Y";
            }
            if (oemNumber == null || oemNumber.trim().equals("")) {
              if (partBean.getOemNumber() != null && !partBean.getOemNumber().trim().equals("")) {
                oemNumber = partBean.getOemNumber();
              }
            }
            /*
             * if (plNumber == null || plNumber.trim().equals("")) { if (partNeam.getPlNumber() !=
             * null && !partBean.getPlNumber().trim().equals("")) { plNumber =
             * partBean.getOemNumber(); } }
             */
          }
        } else {
          PartsBean partBean = PartsBean.getPart(partNo, con1);
          partDesc1 = MakeModelBean.getMakeModelName(partBean.getMakeModelCode());
          partDesc2 = partBean.getPartDescription();

          if (partBean.getPartDescription().indexOf("CAPA") != -1
              || partBean.getPartDescription().indexOf("Capa") != -1
              || partBean.getPartDescription().indexOf("capa") != -1) {
            capa = "Y";
          }
          partDesc1 = partDesc1.trim();
          int leftBracket = partDesc1.indexOf("(");
          int rightBracket = partDesc1.indexOf(")");
          if (leftBracket != -1 && rightBracket != -1 && (rightBracket - leftBracket) == 6) {
            partDesc1 =
                partBean.getYear() + " " + partDesc1.substring(0, leftBracket)
                    + partDesc1.substring(rightBracket + 1);
          }

          plNumber = "";

          if (oemNumber == null || oemNumber.trim().equals("0") || oemNumber.trim().equals("")) {
            oemNumber = partBean.getOemNumber();
          }

          if (voiBean.getPrice() != 0) {
            vendorPrice = voiBean.getPrice() + "";
            if (vendorPrice.indexOf(".") == vendorPrice.length() - 2)
              vendorPrice += "0";
          } else {
            vendorPrice = "";
          }
          cuft = "";
          ourCuft = qty + "";
        }

        if ((oemNumber == null || oemNumber.trim().equals("0") || oemNumber.trim().equals(""))
            && partNo != null && !partNo.trim().equals("")) {
          try {
            stmtX = con1.createStatement();
            rsX =
                stmtX.executeQuery("Select OEMNo From VendorItems where partNo='" + partNo
                    + "' and OEMNo!=''");
            if (rsX.next()) {
              oemNumber = rsX.getString(1);
            } else {
              oemNumber = "";
            }
            rsX.close();
            stmtX.close();
            // logger.error("Got OEMNumber");
          } catch (Exception e) {
            oemNumber = "";
            logger.error("Exception when getting OEM No. In PrintUtils.printVendorOrder()");
          }
        }
        oemNumber = oemNumber.trim();

        if ((plNumber == null || plNumber.trim().equals("0") || plNumber.trim().equals(""))
            && partNo != null && !partNo.trim().equals("")) {
          try {
            stmtX = con1.createStatement();
            rsX =
                stmtX.executeQuery("Select PlNo From VendorItems where partNo='" + partNo
                    + "' and PlNo!=''");
            if (rsX.next()) {
              plNumber = rsX.getString(1);
            } else {
              plNumber = "";
            }
            rsX.close();
            stmtX.close();
            // logger.error("Got PLNumber");
          } catch (Exception e) {
            oemNumber = "";
            logger.error("Exception when getting plNumber. In PrintUtils.printVendorOrder()");
          }
        }
        plNumber = plNumber.trim();

        ft.write("<tr>");
        ft.write("<td style='font-size: 8pt;' width='50px'>");
        ft.write(padSpaces(partNo, 10));
        ft.write("</td>");
        ft.write("<td style='font-size: 8pt;' width='90px'>");
        ft.write(padSpaces(vendorPartNo, 12));
        ft.write("</td>");

        ftWO.write("<tr>");
        ftWO.write("<td style='font-size: 8pt;' width='50px'>");
        ftWO.write(padSpaces(partNo, 10));
        ftWO.write("</td>");
        ftWO.write("<td style='font-size: 8pt;' width='90px'>");
        ftWO.write(padSpaces(vendorPartNo, 12));
        ftWO.write("</td>");

        if (supId != 1 && supId != 4 && supId != 5 && supId != 6 && supId != 8) {
          ft.write("<td style='font-size: 8pt;' width='120px'>");
          ft.write(padSpaces(partDesc1, 10));
          ft.write("</td>");
          ft.write("<td style='font-size: 8pt;' width='320px'>");
          ft.write(padSpaces(partDesc2, 10));
          ft.write("</td>");

          ftWO.write("<td style='font-size: 8pt;' width='120px'>");
          ftWO.write(padSpaces(partDesc1, 10));
          ftWO.write("</td>");
          ftWO.write("<td style='font-size: 8pt;' width='320px'>");
          ftWO.write(padSpaces(partDesc2, 10));
          ftWO.write("</td>");
        } else {
          ft.write("<td style='font-size: 8pt;' width='320px'>");
          ft.write(padSpaces(partDesc1.trim() + " " + partDesc2.trim(), 20));
          ft.write("</td>");
          ftWO.write("<td style='font-size: 8pt;' width='320px'>");
          ftWO.write(padSpaces(partDesc1.trim() + " " + partDesc2.trim(), 20));
          ftWO.write("</td>");
        }
        ft.write("<td style='font-size: 8pt;' width='95px'>");
        ft.write(padSpaces(capa, 2));
        ft.write("</td>");
        ft.write("<td style='font-size: 8pt;' width='95px'>");
        ft.write(padSpaces(plNumber, 10));
        ft.write("</td>");
        ft.write("<td style='font-size: 8pt;' width='150px'>");
        ft.write(padSpaces(oemNumber, 10));
        ft.write("</td>");

        ftWO.write("<td style='font-size: 8pt;' width='95px'>");
        ftWO.write(padSpaces(capa, 2));
        ftWO.write("</td>");
        ftWO.write("<td style='font-size: 8pt;' width='95px'>");
        ftWO.write(padSpaces(plNumber, 10));
        ftWO.write("</td>");
        ftWO.write("<td style='font-size: 8pt;' width='150px'>");
        ftWO.write(padSpaces(oemNumber, 10));
        ftWO.write("</td>");

        ft.write("<td style='font-size: 8pt;' width='40px'>");
        ft.write(padSpaces(vendorPrice, 10));
        ft.write("</td>");

        ft.write("<td style='font-size: 8pt;' width='65px'>");
        ft.write(padSpaces(ourCuft, 10));
        ft.write("</td>");
        ft.write("</tr>");

        ftWO.write("<td style='font-size: 8pt;' width='65px'>");
        ftWO.write(padSpaces(ourCuft, 10));
        ftWO.write("</td>");
        ftWO.write("</tr>");
      }

      logger.error("TOTAL CUFT FOR THE ORDER # " + orderBean.getOrderNo() + " IS " + totalCuft);
      logger
          .error("TOTAL # OF ITEMS FOR THE ORDER # " + orderBean.getOrderNo() + " IS " + totItems);
      logger.error("TOTAL ITEMS WITH CUFT FOR THE ORDER # " + orderBean.getOrderNo() + " IS "
          + withCuft);

      ft.write("</table>");
      ft.write("</td>");
      ft.write("</tr>");
      ft.write("</table>");
      ft.write(getFooters());
      ft.close();

      ftWO.write("</table>");
      ftWO.write("</td>");
      ftWO.write("</tr>");
      ftWO.write("</table>");
      ftWO.write(getFooters());
      ftWO.close();

      con1.close();

    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (Exception e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  public static void createRouteSheet(String userName, String driverName,
      Hashtable<String, String> selectedInvoices) {
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {

      File fileHtml =
          new File("c:/Tomcat/webapps/bvaschicago/html/reports/RS" + driverName + "_"+DateUtils.getNewUSDate()+ ".html");
      //new File("E:/projects/myeclipse/.metadata/.me_tcat/webapps/bvaschicago/html/reports/RS" + driverName + "_"+DateUtils.getNewUSDate()+  ".html");
      FileWriter ft = new FileWriter(fileHtml);
      ft.write(getHeaders());

      con = DBInterfaceLocal.getSQLConnection();

      ft.write("<table>");

      ft.write("<tr>");
      ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
      ft.write("<B>BEST VALUE Auto Body Supply, Inc.  DRIVER LOG IN SHEET</B><BR/>");
      ft.write("</td>");
      ft.write("</tr>");
      // ft.write("<tr>");
      // ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
      // ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>");
      // ft.write("</td>");
      // ft.write("</tr>");
      ft.write("<tr>");
      ft.write("<td colspan='4'>");
      ft.write("<hr align='center' noshade size='2px' width='500px'/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='left' colspan='8'>");
      ft.write("<B>DRIVER NAME:&nbsp;&nbsp;&nbsp;"
          + driverName
          + "</B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; DATE:&nbsp;&nbsp;&nbsp;"
          + DateUtils.getNewUSDate()
          + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ROUTE #:&nbsp;&nbsp;___________");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='left' colspan='8'> &nbsp;");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='left' colspan='8'>");
      ft.write("VAN MILEAGE &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OUT:&nbsp;&nbsp;&nbsp;__________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; IN:&nbsp;&nbsp;&nbsp;__________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Print:&nbsp;&nbsp;"
          + userName);
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='left' colspan='8'> &nbsp;");
      ft.write("</td>");
      ft.write("</tr>");

      if (selectedInvoices.size() > 0) {
        stmt = con.createStatement();
        String sql =
            "SELECT a.InvoiceNumber, CompanyName, b.PaymentTerms, a.Balance, c.Region From Invoice a, Customer b, Address c Where ";

        Enumeration<String> ennum = selectedInvoices.keys();
        int strtCnt = 0;
        while (ennum.hasMoreElements()) {
          int invNo = Integer.parseInt(ennum.nextElement());
          if (strtCnt == 0) {
            sql += "( a.InvoiceNumber=" + invNo;
          } else {
            sql += " or a.InvoiceNumber=" + invNo;
          }
          strtCnt++;
        }
        sql +=
            ") and a.CustomerId=b.CustomerId and b.CustomerId=c.Id and c.Type='Standard' Order By 5, 2, 1 ";
        // logger.error(sql);
        rs = stmt.executeQuery(sql);
        strtCnt = 0;
        while (rs.next()) {
          int invNo = rs.getInt(1);
          String companyName = rs.getString(2);
          String terms = rs.getString(3);
          double bal = rs.getDouble(4);
          String region = rs.getString(5);

          if (companyName != null && !companyName.trim().equals("")
              && companyName.trim().length() > 26) {
            companyName = companyName.substring(0, 25);
          }
          if (terms == null) {
            terms = "";
          } else if (terms.trim().equalsIgnoreCase("O")) {
            terms = "CASH";
          } else if (terms.trim().equalsIgnoreCase("C")) {
            terms = "COD";
          } else if (terms.trim().equalsIgnoreCase("W")) {
            terms = "WKLY";
          } else if (terms.trim().equalsIgnoreCase("B")) {
            terms = "BI-WK";
          } else if (terms.trim().equalsIgnoreCase("M")) {
            terms = "MTHLY";
          }
          if (strtCnt == 0) {
            ft.write("<tr>");
            ft.write("<td>");
            // ft.write("<table border='1' style='border-style: single'>");
            ft.write("<table border='1' cellspacing='0'>");
            ft.write("<tr>");
            ft.write("<td style='font-size: 12pt;' width='30'>");
            ft.write("In");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='30'>");
            ft.write("Out");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='90'>");
            ft.write("<B>Inv #</B>");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='300'>");
            ft.write("<B>Terms + Company</B>");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='45'>");
            ft.write("<B>Amt</B>");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='70'>");
            ft.write("<B>Paid</B>");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='70'>");
            ft.write("<B>Pay Type</B>");
            ft.write("</td>");
            ft.write("<td style='font-size: 12pt;' width='90'>");
            ft.write("<B>Remarks</B>");
            ft.write("</td>");
            ft.write("</tr>");
            strtCnt++;
          }

          ft.write("<tr>");
          ft.write("<td width='30'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='30'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td  style='font-size: 10pt;' width='90'><B>");
          ft.write(invNo + "");
          ft.write("</B></td>");
          ft.write("<td  style='font-size: 10pt;' width='300'>");
          ft.write("** " + terms + " **&nbsp;&nbsp;&nbsp;" + companyName);
          ft.write("</td>");
          ft.write("<td style='font-size: 10pt;' width='45'>");
          ft.write(bal + "");
          ft.write("</td>");
          ft.write("<td width='70'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='70'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='90'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("</tr>");
        }

        for (int i = 0; i < 10; i++) {
          ft.write("<tr>");
          ft.write("<td width='30'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='30'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td  style='font-size: 10pt;' width='90'><B>");
          ft.write("&nbsp;");
          ft.write("</B></td>");
          ft.write("<td  style='font-size: 10pt;' width='300'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td style='font-size: 10pt;' width='45'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='70'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='70'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("<td width='90'>");
          ft.write("&nbsp;");
          ft.write("</td>");
          ft.write("</tr>");
        }

        ft.write("</table>");
        ft.write("</td>");
        ft.write("</tr>");

      }

      ft.write("<tr>");
      ft.write("<td align='left' colspan='8'>");
      ft.write("<BR/>AM&nbsp;&nbsp;/&nbsp;&nbsp;PM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TIME LEFT: ________&nbsp;&nbsp;&nbsp;&nbsp;ARRIVED: ________&nbsp;&nbsp;&nbsp;&nbsp; DRIVER\'S SIGNATURE:&nbsp;____________");
      ft.write("<BR/><BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("<tr>");
      ft.write("<td align='left' colspan='8'>");
      ft.write("<BR/>CHECKED BY: ____________________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TIME: __________");
      ft.write("<BR/><BR/>");
      ft.write("</td>");
      ft.write("</tr>");

      ft.write("</table>");

      ft.write(getFooters());
      ft.close();
      rs.close();
      stmt.close();
      con.close();
    } catch (IOException ioe) {
      logger.error("Exception in PrintUtils.createInvoice: " + ioe);
    } catch (SQLException e) {
      logger.error("Exception in PrintUtils.createInvoice: " + e);
    }
  }

  /*
   * public static void createPartsPendingList(String userName) { try {
   * 
   * File fileHtml = new File("c:/Tomcat/webapps/bvaschicago/html/reports/RS" + driverName +
   * ".html"); FileWriter ft = new FileWriter(fileHtml); ft.write(getHeaders());
   * 
   * Connection con = DBInterfaceLocal.getMySQLConnection();
   * 
   * ft.write("<table>");
   * 
   * ft.write("<tr>"); ft.write("<td align='center' colspan='4' style='font-size: 16pt'>"); ft.write
   * ("<B>BEST VALUE Auto Body Supply, Inc.  DRIVER LOG IN SHEET</B><BR/>"); ft.write("</td>");
   * ft.write("</tr>"); //ft.write("<tr>");
   * //ft.write("<td align='center' colspan='4' style='font-size: 16pt'>");
   * //ft.write("<B>4425 W. 16Th St, CHICAGO, IL-60623</B><BR/>"); //ft.write("</td>");
   * //ft.write("</tr>"); ft.write("<tr>"); ft.write("<td colspan='4'>");
   * ft.write("<hr align='center' noshade size='2px' width='500px'/>"); ft.write("</td>");
   * ft.write("</tr>");
   * 
   * 
   * ft.write("<tr>"); ft.write("<td align='left' colspan='8'>");
   * ft.write("<B>DRIVER NAME:&nbsp;&nbsp;&nbsp;"+ driverName +
   * "</B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; DATE:&nbsp;&nbsp;&nbsp;"
   * + DateUtils.getNewUSDate() +
   * "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ROUTE #:&nbsp;&nbsp;___________"
   * ); ft.write("</td>"); ft.write("</tr>");
   * 
   * ft.write("<tr>"); ft.write("<td align='left' colspan='8'> &nbsp;"); ft.write("</td>");
   * ft.write("</tr>");
   * 
   * ft.write("<tr>"); ft.write("<td align='left' colspan='8'>"); ft.write(
   * "VAN MILEAGE &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OUT:&nbsp;&nbsp;&nbsp;__________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; IN:&nbsp;&nbsp;&nbsp;__________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Print:&nbsp;&nbsp;"
   * + userName); ft.write("</td>"); ft.write("</tr>");
   * 
   * ft.write("<tr>"); ft.write("<td align='left' colspan='8'> &nbsp;"); ft.write("</td>");
   * ft.write("</tr>");
   * 
   * 
   * if (selectedInvoices.size()>0) { Statement stmt = con.createStatement(); String sql =
   * "SELECT a.InvoiceNumber, CompanyName, b.PaymentTerms, a.Balance, Region From Invoice a, Customer b, Address c Where "
   * ;
   * 
   * Enumeration ennum = selectedInvoices.keys(); int strtCnt = 0; while (ennum.hasMoreElements()) {
   * int invNo = Integer.parseInt((String) ennum.nextElement()); if (strtCnt == 0) { sql +=
   * "( a.InvoiceNumber=" + invNo; } else { sql += " or a.InvoiceNumber=" + invNo; } strtCnt++; }
   * sql +=
   * ") and a.CustomerId=b.CustomerId and b.CustomerId=c.Id and c.Type='Standard' Order By 5, 2, 1 "
   * ; //logger.error(sql); ResultSet rs = stmt.executeQuery(sql); strtCnt = 0; while (rs.next()) {
   * int invNo = rs.getInt(1); String companyName = rs.getString(2); String terms = rs.getString(3);
   * double bal = rs.getDouble(4); String region = rs.getString(5);
   * 
   * if (companyName != null && !companyName.trim().equals("") && companyName.trim().length()>26) {
   * companyName = companyName.substring(0, 25); } if (terms == null) { terms = ""; } else if
   * (terms.trim().equalsIgnoreCase("O")) { terms = "CASH"; } else if
   * (terms.trim().equalsIgnoreCase("C")) { terms = "COD"; } else if
   * (terms.trim().equalsIgnoreCase("W")) { terms = "WKLY"; } else if
   * (terms.trim().equalsIgnoreCase("B")) { terms = "BI-WK"; } else if
   * (terms.trim().equalsIgnoreCase("M")) { terms = "MTHLY"; } if (strtCnt == 0) { ft.write("<tr>");
   * ft.write("<td>"); //ft.write("<table border='1' style='border-style: single'>");
   * ft.write("<table border='1' cellspacing='0'>"); ft.write("<tr>");
   * ft.write("<td style='font-size: 12pt;' width='30'>"); ft.write("In"); ft.write("</td>");
   * ft.write("<td style='font-size: 12pt;' width='30'>"); ft.write("Out"); ft.write("</td>");
   * ft.write("<td style='font-size: 12pt;' width='60'>"); ft.write("<B>Inv #</B>");
   * ft.write("</td>"); ft.write("<td style='font-size: 12pt;' width='300'>");
   * ft.write("<B>Terms + Company</B>"); ft.write("</td>");
   * ft.write("<td style='font-size: 12pt;' width='45'>"); ft.write("<B>Amt</B>");
   * ft.write("</td>"); ft.write("<td style='font-size: 12pt;' width='70'>");
   * ft.write("<B>Paid</B>"); ft.write("</td>");
   * ft.write("<td style='font-size: 12pt;' width='70'>"); ft.write("<B>Pay Type</B>");
   * ft.write("</td>"); ft.write("<td style='font-size: 12pt;' width='120'>");
   * ft.write("<B>Remarks</B>"); ft.write("</td>"); ft.write("</tr>"); strtCnt++; }
   * 
   * ft.write("<tr>"); ft.write("<td width='30'>"); ft.write("&nbsp;"); ft.write("</td>");
   * ft.write("<td width='30'>"); ft.write("&nbsp;"); ft.write("</td>");
   * ft.write("<td  style='font-size: 10pt;' width='60'><B>"); ft.write(invNo+"");
   * ft.write("</B></td>"); ft.write("<td  style='font-size: 10pt;' width='300'>"); ft.write("** " +
   * terms + " **&nbsp;&nbsp;&nbsp;" + companyName); ft.write("</td>");
   * ft.write("<td style='font-size: 10pt;' width='45'>"); ft.write(bal+""); ft.write("</td>");
   * ft.write("<td width='70'>"); ft.write("&nbsp;"); ft.write("</td>");
   * ft.write("<td width='70'>"); ft.write("&nbsp;"); ft.write("</td>");
   * ft.write("<td width='120'>"); ft.write("&nbsp;"); ft.write("</td>"); ft.write("</tr>"); }
   * 
   * for (int i=0; i<10; i++) { ft.write("<tr>"); ft.write("<td width='30'>"); ft.write("&nbsp;");
   * ft.write("</td>"); ft.write("<td width='30'>"); ft.write("&nbsp;"); ft.write("</td>");
   * ft.write("<td  style='font-size: 10pt;' width='60'><B>"); ft.write("&nbsp;");
   * ft.write("</B></td>"); ft.write("<td  style='font-size: 10pt;' width='300'>");
   * ft.write("&nbsp;"); ft.write("</td>"); ft.write("<td style='font-size: 10pt;' width='45'>");
   * ft.write("&nbsp;"); ft.write("</td>"); ft.write("<td width='70'>"); ft.write("&nbsp;");
   * ft.write("</td>"); ft.write("<td width='70'>"); ft.write("&nbsp;"); ft.write("</td>");
   * ft.write("<td width='120'>"); ft.write("&nbsp;"); ft.write("</td>"); ft.write("</tr>"); }
   * 
   * ft.write("</table>"); ft.write("</td>"); ft.write("</tr>"); }
   * 
   * 
   * ft.write("<tr>"); ft.write("<td align='left' colspan='8'>"); ft.write(
   * "<BR/>AM&nbsp;&nbsp;/&nbsp;&nbsp;PM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TIME LEFT: ________&nbsp;&nbsp;&nbsp;&nbsp;ARRIVED: ________&nbsp;&nbsp;&nbsp;&nbsp; DRIVER\'S SIGNATURE:&nbsp;____________"
   * ); ft.write("<BR/><BR/>"); ft.write("</td>"); ft.write("</tr>");
   * 
   * ft.write("<tr>"); ft.write("<td align='left' colspan='8'>"); ft.write(
   * "<BR/>CHECKED BY: ____________________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TIME: __________" );
   * ft.write("<BR/><BR/>"); ft.write("</td>"); ft.write("</tr>");
   * 
   * ft.write("</table>");
   * 
   * ft.write(getFooters()); ft.close(); } catch (IOException ioe) {
   * logger.error("Exception in PrintUtils.createInvoice: " + ioe); } catch (Exception e) {
   * logger.error("Exception in PrintUtils.createInvoice: " + e); } }
   */

  public static void getVendorHeader(FileWriter ft, int supId, boolean woPrice) throws IOException {
    ft.write("<table border='1' cellspacing='0' style='font-size: 10pt'>");
    ft.write("<tr>");
    ft.write("<td align='center'><B>");
    ft.write("BV Part No");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("ITEM NO");
    ft.write("</B></td>");
    if (supId != 1 && supId != 4 && supId != 5 && supId != 6 && supId != 8) {
      ft.write("<td align='center'><B>");
      ft.write("Desc 1");
      ft.write("</B></td>");
    }
    ft.write("<td align='center'><B>");
    ft.write("Desc 2");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("CAPA");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("P/L Number");
    ft.write("</B></td>");
    ft.write("<td align='center'><B>");
    ft.write("OEM Number");
    ft.write("</B></td>");
    if (!woPrice) {
      ft.write("<td align='center'><B>");
      ft.write("NR");
      ft.write("</B></td>");
    }
    ft.write("<td align='center'><B>");
    ft.write("Order");
    ft.write("</B></td>");
    ft.write("</tr>");
  }

}
