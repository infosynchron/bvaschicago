import javax.swing.*;
import javax.swing.table.*;

import java.awt.print.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.Dimension;
import java.rmi.*;

public class PrintInvoiceApplet extends JApplet implements Printable {

    JFrame frame;
	InvoicePrintBean invoice = null;

    public PrintInvoiceApplet() {
        frame = new JFrame("Printing Invoice");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
                {System.exit(0);}});

		int invNo = 0;
		try {
			invNo = Integer.parseInt(getParameter("InvNo"));
		} catch (Exception e) {
			invNo = 0;
		}

        getInvoice(invNo);

		getFrame();


		//frame.getContentPane().setLayout(new BorderLayout());
        //frame.getContentPane().add(BorderLayout.CENTER,panel);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        Insets insets = frame.getInsets();
        frame.setSize(500 + insets.left + insets.right, 500 + insets.top + insets.bottom);
        //
        frame.pack();
		frame.setVisible(true);

		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(false);

        PrinterJob pj=PrinterJob.getPrinterJob();

		pj.setPrintable(PrintInvoiceApplet.this);
		pj.printDialog();

		try{
	    	pj.print();
		}catch (Exception PrintException) {}

    }

    public int print(Graphics g, PageFormat pageFormat,
		int pageIndex) throws PrinterException {

        Graphics2D  g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		int fontHeight=g2.getFontMetrics().getHeight();
		int fontDesent=g2.getFontMetrics().getDescent();

		//leave room for page number
		double pageHeight = pageFormat.getImageableHeight();
		double pageWidth = pageFormat.getImageableWidth();

		double scale = 1;

		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		g2.scale(scale,scale);
		g2.setClip(0, 0, (int) pageWidth, (int) pageHeight);

		/*g2.drawString("Font Height = "+fontHeight, (int)pageWidth/2-35,
				(int) (pageHeight+fontHeight-(fontDesent*40)));
		g2.drawString("Font Desent = "+pageHeight, (int)pageWidth/2-35,
				(int) (pageHeight+fontHeight-(fontDesent*30)));
		g2.drawString("Page Height = "+pageHeight, (int)pageWidth/2-35,
				(int) (pageHeight+fontHeight-(fontDesent*20)));
		g2.drawString("Page Width = "+pageWidth, (int)pageWidth/2-35,
				(int) (pageHeight+fontHeight-(fontDesent*10)));*/
		g2.drawString(invoice.invNo+"", (int)pageWidth/2-35, (int) (pageHeight+fontHeight-(fontDesent*50)) );
		//g2.drawString(invoice.ordDate+"", );
		//g2.drawString(invoice.custId+"", );
		//g2.drawString(invoice.shipVia+"", );
		//g2.drawString(invoice.salesPerson+"", );

		//frame.paint(g2);
		if (pageIndex >= 1)
			return NO_SUCH_PAGE;
		else
			return Printable.PAGE_EXISTS;
	}

    public void init() {
    }


	public void getInvoice(int invNo) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            String name = "//BVASSYST/TheSystem";
            PrintInterface iFace = (PrintInterface) Naming.lookup(name);
            invoice = iFace.getInvoiceForPrint(invNo);
        } catch (Exception e) {
           logger.error("ComputePi exception: " +
                               e.getMessage());
            e.printStackTrace();
        }
	}

	public void getFrame() {


		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);

		JLabel invNo = new JLabel(invoice.invNo+"");
		JLabel ordDate = new JLabel(invoice.ordDate+"");
		JLabel custId = new JLabel(invoice.custId+"");
		JLabel shipVia = new JLabel(invoice.shipVia+"");
		JLabel salesPerson = new JLabel(invoice.salesPerson+"");

		JLabel billTo = new JLabel(invoice.billCust+"");
		JLabel billAttention = new JLabel(invoice.billAttention+"");
		JLabel billAddress = new JLabel(invoice.billAddress+"");
		JLabel billCity = new JLabel(invoice.billCity+"");
		JLabel billState = new JLabel(invoice.billState+"");
		JLabel billZip = new JLabel(invoice.billZip+"");
		JLabel billRegion = new JLabel(invoice.billRegion+"");

		JLabel shipTo = new JLabel(invoice.shipCust+"");
		JLabel shipAttention = new JLabel(invoice.shipAttention+"");
		JLabel shipAddress = new JLabel(invoice.shipAddress+"");
		JLabel shipCity = new JLabel(invoice.shipCity+"");
		JLabel shipState = new JLabel(invoice.shipState+"");
		JLabel shipZip = new JLabel(invoice.shipZip+"");
		JLabel shipRegion = new JLabel(invoice.shipRegion+"");

		contentPane.add(invNo);
		contentPane.add(ordDate);
		contentPane.add(custId);
		contentPane.add(shipVia);
		contentPane.add(salesPerson);

		contentPane.add(billTo);
		contentPane.add(billAttention);
		contentPane.add(billAddress);
		contentPane.add(billCity);
		contentPane.add(billState);
		contentPane.add(billZip);
		contentPane.add(billRegion);

		contentPane.add(shipTo);
		contentPane.add(shipAttention);
		contentPane.add(shipAddress);
		contentPane.add(shipCity);
		contentPane.add(shipState);
		contentPane.add(shipZip);
		contentPane.add(shipRegion);

		Insets insets = contentPane.getInsets();
        invNo.setBounds(50 + insets.left, 50 + insets.top, 75, 20);
        ordDate.setBounds(100 + insets.left, 50 + insets.top, 75, 20);
        custId.setBounds(200 + insets.left, 50 + insets.top, 75, 20);
        shipVia.setBounds(300 + insets.left, 50 + insets.top, 75, 20);
        salesPerson.setBounds(40 + insets.left, 50 + insets.top, 75, 20);

        billTo.setBounds(50 + insets.left, 150 + insets.top, 75, 20);
        billAddress.setBounds(50 + insets.left, 170 + insets.top, 75, 20);
        billCity.setBounds(50 + insets.left, 190 + insets.top, 75, 20);
        billState.setBounds(100 + insets.left, 190 + insets.top, 75, 20);
        billZip.setBounds(150 + insets.left, 190 + insets.top, 75, 20);
        billRegion.setBounds(50 + insets.left, 210 + insets.top, 75, 20);
        billAttention.setBounds(150 + insets.left, 210 + insets.top, 75, 20);

        shipTo.setBounds(300 + insets.left, 150 + insets.top, 75, 20);
        shipAddress.setBounds(300 + insets.left, 170 + insets.top, 75, 20);
        shipCity.setBounds(300 + insets.left, 190 + insets.top, 75, 20);
        shipState.setBounds(350 + insets.left, 190 + insets.top, 75, 20);
        shipZip.setBounds(400 + insets.left, 190 + insets.top, 75, 20);
        shipRegion.setBounds(300 + insets.left, 210 + insets.top, 75, 20);
        shipAttention.setBounds(400 + insets.left, 210 + insets.top, 75, 20);

	}
    public static void main(String[] args) {
       new PrintInvoiceApplet();
    }

}

