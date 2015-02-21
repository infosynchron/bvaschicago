import java.io.*;
import java.util.*;

public class InvoicePrintBean implements Serializable {

	public int invNo;
	public String ordDate = null;
	public String custId = null;
	public String shipVia = null;
	public String salesPerson = null;
	public String notes = null;
	public double subTotal;
	public double tax;
	public double total;

	public String billCust = null;
	public String billAttention = null;
	public String billAddress = null;
	public String billCity = null;
	public String billState = null;
	public String billZip = null;
	public String billRegion = null;

	public String shipCust = null;
	public String shipAttention = null;
	public String shipAddress = null;
	public String shipCity = null;
	public String shipState = null;
	public String shipZip = null;
	public String shipRegion = null;

	Vector items = null;

}