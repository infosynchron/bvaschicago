import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class PrintServer extends UnicastRemoteObject
                           implements PrintInterface
{
    public PrintServer() throws RemoteException {
        super();
    }


    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        String name = "//BVASSYST/TheSystem";
        try {
            PrintInterface iface = new PrintServer();
            Naming.rebind(name, iface);
            logger.error("RMI Server bound");
        } catch (Exception e) {
           logger.error("RMI Server exception: " +
			       e.getMessage());
            e.printStackTrace();
        }
    }


    public Object[][] getTodaysOrders() {
		Object[][] todaysOrders = null;
		Object[] singleArray = null;
		ArrayList list = new ArrayList();
		logger.error("Yes: Success");
	    Connection con = getConnection();

		String sql = "SELECT a.InvoiceNumber, a.CustomerId, c.CompanyName, ";
		sql += "b.PartNumber, d.Location, d.PartDescription, b.Quantity, a.ShipVia ";
		sql += "FROM Invoice a, InvoiceDetails b, Customer c, Parts d ";
		sql += "WHERE ";
		//sql += "a.OrderDate = '' ";
		sql += " a.InvoiceNumber=b.InvoiceNumber AND ";
		sql += " a.CustomerId=c.CustomerId AND ";
		sql += " b.PartNumber=d.PartNo ";
		sql += " ORDER BY 1 ASC, 4 ASC ";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int cntRow = 0;
			int cntCol = 0;
			while (rs.next()) {
				Object [] temp = new Object[8];
				temp[0] = rs.getObject(1);
				temp[1] = rs.getObject(2);
				temp[2] = rs.getObject(3);
				temp[3] = rs.getObject(4);
				temp[4] = rs.getObject(5);
				temp[5] = rs.getObject(6);
				temp[6] = rs.getObject(7);
				temp[7] = rs.getObject(8);
				list.add(temp);
			}
		} catch (Exception e) {
			logger.error("Exception in PrintServer - " + e);
		}
		singleArray = list.toArray();
		todaysOrders = new Object[singleArray.length][];
		for (int i=0; i<singleArray.length; i++) {
			todaysOrders[i] = (Object []) singleArray[i];
		}

        return todaysOrders;
    }



    public InvoicePrintBean getInvoiceForPrint(int invNo) {

		logger.error("Invoice No Got is: " + invNo);

		InvoicePrintBean invBean = new InvoicePrintBean();
	    Connection con = getConnection();

		if (invNo == 0) {
			try {
				logger.error(1);
				Statement stmt0 = con.createStatement();
				logger.error(1);
				ResultSet rs0 = stmt0.executeQuery("SELECT max(InvoiceNumber) FROM Invoice");
				logger.error(1);
				rs0.next();
				invNo = rs0.getInt(1);
				logger.error("Got the Maximum invoice number: " + invNo);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		try {
			Statement stmt = con.createStatement();
			String getSQL = "";
			getSQL += "select * from Invoice where InvoiceNumber = " + invNo;
			ResultSet rs = stmt.executeQuery(getSQL);
			if (rs.next()) {
				invBean.invNo = rs.getInt("InvoiceNumber");
				invBean.ordDate = rs.getString("OrderDate");
				logger.error("Order Date is : " + invBean.ordDate);
				invBean.custId = rs.getString("CustomerId");
				invBean.shipVia = rs.getString("ShipVia");
				invBean.shipCust = rs.getString("ShipTo");
				invBean.salesPerson = rs.getString("SalesPerson");
				invBean.tax = rs.getDouble("Tax");
				invBean.total = rs.getDouble("InvoiceTotal");
				invBean.subTotal = invBean.total - invBean.tax;
				invBean.shipAttention = rs.getString("ShipAttention");
				invBean.billAttention = rs.getString("BillAttention");
			}

			Statement stmt1 = con.createStatement();
			String billAddSQL = "SELECT * from Address WHERE Id = '"+invBean.invNo+"' AND Type = 'Bill' and Who='Cust' and DateCreated='"+invBean.ordDate+"' and InvoiceNumber="+invNo;
			ResultSet rs1 = stmt1.executeQuery(billAddSQL);
			if (rs1.next()) {
				invBean.billAddress = rs1.getString("Addr1") + ", " + rs1.getString("Addr2");
				invBean.billCity = rs1.getString("City");
				invBean.billState = rs1.getString("State");
				invBean.billZip = rs1.getString("PostalCode");
				invBean.billRegion = rs1.getString("Region");
			}

			Statement stmt2 = con.createStatement();
			String shipAddSQL = "SELECT * from Address WHERE Id = '"+invBean.invNo+"' AND Type = 'Ship' and Who='Cust' and DateCreated='"+invBean.ordDate+"' and InvoiceNumber="+invNo;
			ResultSet rs2 = stmt2.executeQuery(shipAddSQL);
			if (rs2.next()) {
				invBean.shipAddress = rs2.getString("Addr1") + ", " + rs2.getString("Addr2");
				invBean.shipCity = rs2.getString("City");
				invBean.shipState = rs2.getString("State");
				invBean.shipZip = rs2.getString("PostalCode");
				invBean.shipRegion = rs2.getString("Region");
			}

			Statement stmt3 = con.createStatement();
			String custSQL = "SELECT CompanyName from Customer WHERE CustomerId='"+invBean.custId+"'";
			ResultSet rs3 = stmt3.executeQuery(custSQL);
			if (rs3.next()) {
				invBean.billCust = rs3.getString("CompanyName");
			}

			Statement stmt4 = con.createStatement();
			Vector v = new Vector();
			ResultSet rs4 = stmt4.executeQuery("SELECT * from InvoiceDetails WHERE InvoiceNumber="+invBean.invNo);
			while (rs4.next()) {
				PartsPrintBean partsBean = new PartsPrintBean();
				partsBean.partNo = rs4.getString("PartNumber");
				logger.error("Part No is : " + partsBean.partNo);
				partsBean.qty = rs4.getInt("Quantity");
				Statement stmt5 = con.createStatement();
				ResultSet rs5 = stmt5.executeQuery("SELECT * FROM Parts WHERE PartNo='"+partsBean.partNo+"'");
				if (rs5.next()) {
					partsBean.location = rs5.getString("Location");
					partsBean.descrip = rs5.getString("PartDescription");
					partsBean.year = rs5.getString("Year");
					partsBean.listPrice = rs5.getDouble("ListPrice");
					partsBean.costPrice = rs5.getDouble("CostPrice");
					Statement stmt6 = con.createStatement();
					ResultSet rs6 = stmt6.executeQuery("SELECT MakeModelName FROM MakeModel WHERE MakeModelCode='"+rs5.getString("MakeModelCode")+"'");
					if (rs.next()) {
						partsBean.makeModel = rs6.getString("MakeModelName");
					}
				}
				stmt5.close();
				v.add(partsBean);
			}
			invBean.items = v;
		} catch (SQLException e) {
			logger.error("Error:"+e);
		}

		return invBean;
	}

	public FinancePrintBean getFinance(int in) {
		return new FinancePrintBean();
	}

	public Object[][] getClients() {
		Object[][] clients = null;
		Object[] singleArray = null;
		ArrayList list = new ArrayList();
		logger.error("Yes: Success");
	    Connection con = getConnection();

		String sql = "SELECT CustomerId, CompanyName, ContactName, ";
		sql += "ContactTitle, Terms, TaxId, TaxIdNumber ";
		sql += "FROM Customer ";
		sql += " ORDER BY 2 ASC ";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int cntRow = 0;
			int cntCol = 0;
			while (rs.next()) {
				Object [] temp = new Object[7];
				temp[0] = rs.getObject(1);
				temp[1] = rs.getObject(2);
				temp[2] = rs.getObject(3);
				temp[3] = rs.getObject(4);
				temp[4] = rs.getObject(5);
				temp[5] = rs.getObject(6);
				temp[6] = rs.getObject(7);
				list.add(temp);
			}
		} catch (Exception e) {
			logger.error("Exception in PrintServer When getting Customers - " + e);
		}

		singleArray = list.toArray();
		clients = new Object[singleArray.length][];
		for (int i=0; i<singleArray.length; i++) {
			clients[i] = (Object []) singleArray[i];
		}
        return clients;
	}

	public Object[][] getVendors() {
		Object[][] vendors = null;
		Object[] singleArray = null;
		ArrayList list = new ArrayList();
		logger.error("Yes: Success");
	    Connection con = getConnection();

		String sql = "SELECT SupplierId, CompanyName, ContactName, ";
		sql += "ContactTitle, Homepage, eMail ";
		sql += "FROM Vendors ";
		sql += " ORDER BY 2 ASC ";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int cntRow = 0;
			int cntCol = 0;
			while (rs.next()) {
				Object [] temp = new Object[6];
				temp[0] = rs.getObject(1);
				temp[1] = rs.getObject(2);
				temp[2] = rs.getObject(3);
				temp[3] = rs.getObject(4);
				temp[4] = rs.getObject(5);
				temp[5] = rs.getObject(6);
				list.add(temp);
			}
		} catch (Exception e) {
			logger.error("Exception in PrintServer - " + e);
		}

		singleArray = list.toArray();
		vendors = new Object[singleArray.length][];
		for (int i=0; i<singleArray.length; i++) {
			vendors[i] = (Object []) singleArray[i];
		}

        return vendors;
	}

	public FaxPrintBean getFax(int faxNo) {
		FaxPrintBean faxBean = new FaxPrintBean();
	    Connection con = getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * from fax WHERE FaxNumber=" + faxNo);
			if (rs.next()) {
				faxBean.faxNo = rs.getInt("FaxNumber");
				faxBean.faxDate = rs.getString("FaxDate");
				faxBean.toWhom = rs.getString("ToWhom");
				faxBean.fromWhom = rs.getString("FromWhom");
				faxBean.faxTo = rs.getString("FaxTo");
				faxBean.phoneTo = rs.getString("PhoneTo");
				faxBean.pages = rs.getInt("Pages");
				faxBean.regarding = rs.getString("Regarding");
				faxBean.cc = rs.getString("CC");
				faxBean.faxMessage = rs.getString("Comments");
			}
		} catch (SQLException e) {
			logger.error("In PrintServer - Fax Not found for fax No: " + faxNo + " - " + e);
		}

		return faxBean;
	}

	public Connection getConnection() {
	    Connection con = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://bvassyst:3306/bvasDB");
		} catch (InstantiationException ex) {
			logger.error("Exception---"+ex);
		} catch (IllegalAccessException ex) {
			logger.error("Exception---"+ex);
		} catch (ClassNotFoundException ex) {
			logger.error("Exception---"+ex);
		} catch (SQLException ex) {
			logger.error("Exception---"+ex);
		}
		return con;
	}
}
