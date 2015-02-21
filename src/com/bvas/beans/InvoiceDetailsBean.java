package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;

public class InvoiceDetailsBean implements Serializable {
  private static final Logger logger = Logger.getLogger(InvoiceDetailsBean.class);

  private int invoiceNumber;

  private String partNumber = null;
  private double  totalSoldPrice;
  private double totalOurPrice;
  private int totalItems;
  public double getTotalSoldPrice() {
	return totalSoldPrice;
}

public void setTotalSoldPrice(double totalSoldPrice) {
	this.totalSoldPrice = totalSoldPrice;
}

public double getTotalOurPrice() {
	return totalOurPrice;
}

public void setTotalOurPrice(double totalOurPrice) {
	this.totalOurPrice = totalOurPrice;
}

public int getTotalItems() {
	return totalItems;
}

public void setTotalItems(int totalItems) {
	this.totalItems = totalItems;
}

private int quantity;

  private double soldPrice;

  private double listPrice;

  private double actualPrice;

  private Vector invoiceDetails = null;

  public int getInvoiceNumber() {
    return (this.invoiceNumber);
  }

  public String getPartNumber() {
    return (this.partNumber);
  }

  public int getQuantity() {
    return (this.quantity);
  }

  public double getSoldPrice() {
    return (this.soldPrice);
  }

  public double getListPrice() {
    return (this.listPrice);
  }

  public double getActualPrice() {
    return (this.actualPrice);
  }

  public void setInvoiceNumber(int invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setSoldPrice(double soldPrice) {
    this.soldPrice = soldPrice;
  }

  public void setListPrice(double listPrice) {
    this.listPrice = listPrice;
  }

  public void setActualPrice(double actualPrice) {
    this.actualPrice = actualPrice;
  }

  public static Vector<InvoiceDetailsBean> getInvoiceDetails(int invoiceNo) throws SQLException {

    Vector<InvoiceDetailsBean> v = new Vector<InvoiceDetailsBean>();

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * from InvoiceDetails where InvoiceNumber = '" + invoiceNo
              + "'");

      while (rs.next()) {
        InvoiceDetailsBean invDet = new InvoiceDetailsBean();
        invDet.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        invDet.setPartNumber(rs.getString("PartNumber"));
        invDet.setQuantity(rs.getInt("Quantity"));
        invDet.setSoldPrice(rs.getDouble("SoldPrice"));
        invDet.setActualPrice(rs.getDouble("ActualPrice"));
        v.add(invDet);
      }
      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw e;
    }

    return v;

  }

  public static Vector<InvoiceDetailsBean> getInvoiceDetails(int invoiceNo, Connection con1)
      throws SQLException {

    Vector<InvoiceDetailsBean> v = new Vector<InvoiceDetailsBean>();

    try {
      Connection con = DBInterfaceLocal.getSQLConnection();
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * from InvoiceDetails where InvoiceNumber = '" + invoiceNo
              + "'");

      while (rs.next()) {
        InvoiceDetailsBean invDet = new InvoiceDetailsBean();
        invDet.setInvoiceNumber(rs.getInt("InvoiceNumber"));
        invDet.setPartNumber(rs.getString("PartNumber"));
        invDet.setQuantity(rs.getInt("Quantity"));
        invDet.setSoldPrice(rs.getDouble("SoldPrice"));
        invDet.setActualPrice(rs.getDouble("ActualPrice"));
        v.add(invDet);
      }

      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error(e);
      throw e;
    }

    return v;

  }
  
  public static InvoiceDetailsBean getInvoiceTotalDetails(Connection con ,int invoiceNo) throws SQLException {

	    InvoiceDetailsBean invDet = null;

	    try {
	        if(con==null){
	        	System.err.println("&&&&"+invoiceNo);
	             con = DBInterfaceLocal.getSQLConnection();
	        } 
	      Statement stmt1 = con.createStatement();
	      String sql="SELECT sum(ActualPrice*Quantity) as TotalOurPrice," +
          "SUM(SoldPrice*Quantity) as TotalSoldPrice, SUM(Quantity) as TotalItems" +
          " from InvoiceDetails where InvoiceNumber = '" + invoiceNo+ "'";
	      ResultSet rs1 =
	          stmt1.executeQuery(sql);

	      if(rs1.next()) {
	        invDet = new InvoiceDetailsBean();
	        invDet.setTotalItems(rs1.getInt("TotalItems"));
	        invDet.setTotalOurPrice(rs1.getDouble("TotalOurPrice"));
	        invDet.setTotalSoldPrice(rs1.getDouble("TotalSoldPrice"));	       
	      }
	      rs1.close();
	      stmt1.close();
	      //con.close();

	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	      logger.error(e);
	      throw e;
	    }

	    return invDet;

	  }


  public Map<String,Vector<InvoiceDetailsBean>> getInvoiceDetailsMap()
  throws SQLException {

	  Map<String,Vector<InvoiceDetailsBean>> v = new HashMap<String,Vector<InvoiceDetailsBean>>();

try {
  Connection con = DBInterfaceLocal.getSQLConnection();
  Statement stmt = con.createStatement();
  ResultSet rs =
      stmt.executeQuery("SELECT * from InvoiceDetails ");
int i=0;
  while (rs.next()) {
	  i=i+1;
	  System.out.println("****"+i);
	  if(v.containsKey(rs.getString("InvoiceNumber"))){
		  Vector<InvoiceDetailsBean> map=v.get(rs.getString("InvoiceNumber"));
		  InvoiceDetailsBean invDet = new InvoiceDetailsBean();
		    invDet.setInvoiceNumber(rs.getInt("InvoiceNumber"));
		    invDet.setPartNumber(rs.getString("PartNumber"));
		    invDet.setQuantity(rs.getInt("Quantity"));
		    invDet.setSoldPrice(rs.getDouble("SoldPrice"));
		    invDet.setActualPrice(rs.getDouble("ActualPrice"));
		 map.add( invDet);
		  
	  } else{
		  Vector<InvoiceDetailsBean> map=new Vector<InvoiceDetailsBean>();
		  InvoiceDetailsBean invDet = new InvoiceDetailsBean();
		    invDet.setInvoiceNumber(rs.getInt("InvoiceNumber"));
		    invDet.setPartNumber(rs.getString("PartNumber"));
		    invDet.setQuantity(rs.getInt("Quantity"));
		    invDet.setSoldPrice(rs.getDouble("SoldPrice"));
		    invDet.setActualPrice(rs.getDouble("ActualPrice"));
		 map.add(invDet);
		 v.put(rs.getString("InvoiceNumber"), map);		  
	  }
   
  }
  rs.close();
  stmt.close();
  con.close();

} catch (SQLException e) {
  logger.error(e);
  throw e;
}

return v;

}

  public static void addInvoiceDetails(Vector<InvoiceDetailsBean> invoiceDetails) {
    Enumeration<InvoiceDetailsBean> ennum = invoiceDetails.elements();
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      PreparedStatement pstmt =
          con.prepareStatement("INSERT INTO InvoiceDetails (InvoiceNumber, PartNumber, Quantity, SoldPrice, ActualPrice) VALUES (?, ?, ?, ?, ?) ");
      while (ennum.hasMoreElements()) {
        InvoiceDetailsBean bean = ennum.nextElement();

        PartsBean part = PartsBean.getPart(bean.getPartNumber(), con);
        if (part == null && bean.getPartNumber().startsWith("XX")) {
          part = PartsBean.getPart(bean.getPartNumber().substring(2), con);
        }

        if (part.getUnitsInStock() > 0) {
          if (part.getActualPrice() != 0) {
            bean.setActualPrice(part.getActualPrice());
          } else if (part.getInterchangeNo() != null && !part.getInterchangeNo().trim().equals("")
              && PartsBean.getPart(part.getInterchangeNo(), con).getActualPrice() != 0) {
            bean.setActualPrice(PartsBean.getPart(part.getInterchangeNo(), con).getActualPrice());
          } else {
            Vector<PartsBean> v = part.getAllInterChangeParts(con);
            if (v == null) {
              bean.setActualPrice(0.0);
            } else {
              Enumeration<PartsBean> enum1 = v.elements();
              while (enum1.hasMoreElements()) {
                PartsBean p = enum1.nextElement();
                if (p.getActualPrice() != 0) {
                  bean.setActualPrice(p.getActualPrice());
                  break;
                }
              }
            }
          }
        } else {
          bean.setActualPrice(0.0);
        }
        pstmt.clearParameters();
        pstmt.setInt(1, bean.getInvoiceNumber());
        pstmt.setString(2, bean.getPartNumber());
        pstmt.setInt(3, bean.getQuantity());
        pstmt.setDouble(4, bean.getSoldPrice());
        pstmt.setDouble(5, bean.getActualPrice());
        pstmt.execute();

        int qty = PartsBean.getQuantity(bean.getPartNumber(), con);
        // logger.error("Quantity for:"+bean.getPartNumber()+" is : "
        // + qty);
        if (!bean.getPartNumber().startsWith("XX")) {
          qty -= bean.getQuantity();
          PartsBean.changeQuantity(bean.getPartNumber(), qty);
        }
      }

      pstmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      logger.error("In InvoiceDetailsBean - Unable to add the Invoice Details" + e);
    }
  }

  public static void deleteInvoiceDetails(int invNo) {
    Connection con = DBInterfaceLocal.getSQLConnection();

    try {
      PreparedStatement pstmt =
          con.prepareStatement("DELETE FROM InvoiceDetails WHERE InvoiceNumber=?");
      pstmt.clearParameters();
      pstmt.setInt(1, invNo);

      pstmt.execute();

      pstmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error("In InvoiceDetailsBean - Unable to delete the Invoice Details" + e);
    }
  }

}
