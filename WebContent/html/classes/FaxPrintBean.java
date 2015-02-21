import java.io.*;

public class FaxPrintBean implements Serializable {
	public int faxNo;
	public String toWhom = null;
	public String fromWhom = null;
	public String faxTo = null;
	public String phoneTo = null;
	public int pages;
	public String faxDate = null;
	public String regarding = null;
	public String cc = null;
	public String priority = null;
	public String faxMessage = null;
}