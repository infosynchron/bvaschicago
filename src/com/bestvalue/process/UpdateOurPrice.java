package com.bestvalue.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bestvalue.dto.InvoiceDetailsOurPrice;
import com.bestvalue.ultimateutils.utils.BvasConFatory;


public class UpdateOurPrice {

  public static void main(String[] args) {
    System.out.println("Processing..................");
    String targetdDB = "CH";
    List<InvoiceDetailsOurPrice> missingourpricelist = getMissingOurPrice(targetdDB);
    updateOurPrices(missingourpricelist, targetdDB);



    System.out.println("Finished Processing..................");
  }

  private static void updateOurPrices(List<InvoiceDetailsOurPrice> missingourpricelist,
      String targetdDB) {
    String findpriceSql1 =
        "select actualprice FROM InvoiceDetails  where partnumber=? and actualprice > 0 order by invoicenumber desc limit 1";
    String findpriceSql2 =
        "select actualprice FROM InvoiceDetailsArch  where partnumber=? and actualprice > 0  order by invoicenumber desc limit 1";
    String findpriceSql3 = "select actualprice from parts where partno =?";
    String findpriceSql4 =
        "select sellingrate from vendoritems where partno =? and sellingrate > 0 limit 1";
    String updtInvoiceSql =
        "UPDATE invoicedetails  SET  actualprice =? WHERE partnumber=? and invoicenumber=?";



    Connection conn = null;
    PreparedStatement pstmt1 = null;
    ResultSet rs1 = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs2 = null;
    PreparedStatement pstmt3 = null;
    ResultSet rs3 = null;
    PreparedStatement pstmt4 = null;
    ResultSet rs4 = null;
    PreparedStatement pstmtUpdate = null;

    BvasConFatory bvasConFactory = new BvasConFatory(targetdDB);
    try {
      conn = bvasConFactory.getConnection();
      for (InvoiceDetailsOurPrice invoicedetails : missingourpricelist) {
        Float actualprice = 0.00f;
        pstmt1 = conn.prepareStatement(findpriceSql1);
        pstmt1.setString(1, invoicedetails.getPartno());
        rs1 = pstmt1.executeQuery();

        while (rs1.next()) {
          actualprice = rs1.getFloat("actualprice");
        }
        if (actualprice > 0) {
          pstmtUpdate = conn.prepareStatement(updtInvoiceSql);
          pstmtUpdate.setFloat(1, actualprice);
          pstmtUpdate.setString(2, invoicedetails.getPartno());
          pstmtUpdate.setInt(3, invoicedetails.getInvoicenumber());
          pstmtUpdate.executeUpdate();
        } else {
          pstmt2 = conn.prepareStatement(findpriceSql2);
          pstmt2.setString(1, invoicedetails.getPartno());
          rs2 = pstmt2.executeQuery();

          while (rs2.next()) {
            actualprice = rs2.getFloat("actualprice");
          }
          if (actualprice > 0) {
            pstmtUpdate = conn.prepareStatement(updtInvoiceSql);
            pstmtUpdate.setFloat(1, actualprice);
            pstmtUpdate.setString(2, invoicedetails.getPartno());
            pstmtUpdate.setInt(3, invoicedetails.getInvoicenumber());
            pstmtUpdate.executeUpdate();
          } else {
            pstmt3 = conn.prepareStatement(findpriceSql3);
            pstmt3.setString(1, invoicedetails.getPartno());
            rs3 = pstmt3.executeQuery();

            while (rs3.next()) {
              actualprice = rs3.getFloat("actualprice");
            }
            if (actualprice > 0) {
              pstmtUpdate = conn.prepareStatement(updtInvoiceSql);
              pstmtUpdate.setFloat(1, actualprice);
              pstmtUpdate.setString(2, invoicedetails.getPartno());
              pstmtUpdate.setInt(3, invoicedetails.getInvoicenumber());
              pstmtUpdate.executeUpdate();
            } else {
              pstmt4 = conn.prepareStatement(findpriceSql4);
              pstmt4.setString(1, invoicedetails.getPartno());
              rs4 = pstmt4.executeQuery();

              while (rs4.next()) {
                actualprice = (float) (rs4.getFloat("sellingrate") * 1.5);
                BigDecimal bd = new BigDecimal(actualprice).setScale(2, RoundingMode.HALF_EVEN);
                actualprice = bd.floatValue();
              }
              if (actualprice > 0) {
                pstmtUpdate = conn.prepareStatement(updtInvoiceSql);
                pstmtUpdate.setFloat(1, actualprice);
                pstmtUpdate.setString(2, invoicedetails.getPartno());
                pstmtUpdate.setInt(3, invoicedetails.getInvoicenumber());
                pstmtUpdate.executeUpdate();
              } else {
                System.out.println("'" + invoicedetails.getPartno() + "',");

              }

            }
          }
        }
      }
      if (rs1 != null) {
        rs1.close();
      }
      if (pstmt1 != null) {
        pstmt1.close();
      }
      if (rs2 != null) {
        rs2.close();
      }
      if (pstmt2 != null) {
        pstmt2.close();
      }
      if (rs3 != null) {
        rs3.close();
      }
      if (pstmt3 != null) {
        pstmt3.close();
      }
      if (rs4 != null) {
        rs4.close();
      }
      if (pstmt4 != null) {
        pstmt4.close();
      }
      if (pstmtUpdate != null) {
        pstmtUpdate.close();
      }
      if (conn != null) {
        conn.close();
      }

      System.out.println("all done");
    } catch (SQLException e) {
      System.out.println(e.toString());
    } finally {
      if (pstmt1 != null) {
        try {
          pstmt1.close();
        } catch (SQLException e) {
          System.out.println(e.toString());
        }
      }

      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          System.out.println(e.toString());
        }
      }
    }
  }

  private static List<InvoiceDetailsOurPrice> getMissingOurPrice(String db) {
    String sql =
        "SELECT invoicenumber, partnumber from invoicedetails where actualprice <= 0  and partnumber not like 'ZZ%' and partnumber not like 'XX%' order by partnumber";
    System.out.println(sql);
    Connection conn = null;
    PreparedStatement pstmt1 = null;
    ResultSet rs1 = null;
    List<InvoiceDetailsOurPrice> missingourpricelist = new ArrayList<InvoiceDetailsOurPrice>();
    try {
      BvasConFatory bvasConFactory = new BvasConFatory(db);
      conn = bvasConFactory.getConnection();
      pstmt1 = conn.prepareStatement(sql);
      rs1 = pstmt1.executeQuery();
      while (rs1.next()) {
        InvoiceDetailsOurPrice missingourprice = new InvoiceDetailsOurPrice();
        missingourprice.setInvoicenumber(rs1.getInt("invoicenumber"));
        missingourprice.setPartno(rs1.getString("partnumber"));


        // System.out.println(rs1.getString("partnumber") + ":"
        // + rs1.getInt("invoicenumber"));
        missingourpricelist.add(missingourprice);
      }
      // System.out.println(missingourpricelist.size());
      rs1.close();
      pstmt1.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println(e.toString());
    } finally {
      if (pstmt1 != null) {
        try {
          pstmt1.close();
        } catch (SQLException e) {
          System.out.println(e.toString());
        }
      }

      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          System.out.println(e.toString());
        }
      }
    }
    return missingourpricelist;
  }


}
