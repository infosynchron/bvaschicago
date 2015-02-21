
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintInterface extends Remote {
    Object[][] getTodaysOrders() throws RemoteException;
    InvoicePrintBean getInvoiceForPrint(int in) throws RemoteException;
    FinancePrintBean getFinance(int fa) throws RemoteException;
    Object[][] getClients() throws RemoteException;
    Object[][] getVendors() throws RemoteException;
	FaxPrintBean getFax(int in) throws RemoteException;
}

