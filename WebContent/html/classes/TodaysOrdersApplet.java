import javax.swing.*;
import javax.swing.table.*;

import java.awt.print.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.Dimension;
import java.rmi.*;

public class TodaysOrdersApplet extends JApplet implements Printable {

    JFrame frame;
    JTable tableView;

    // This is a hack to avoid an ugly error message in 1.1.
    public TodaysOrdersApplet() {
        frame = new JFrame("Todays Orders");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
                {System.exit(0);}});

        final String[] headers = {"Invoice Number",
            "Customer ID", "Customer Name", "Part No", "Location",
            "Description", "Quantity", "Ship Via"};
        final Object[][] orders = getTodaysOrders();

        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return headers.length;
            }
            public int getRowCount() {
				return orders.length;
			}
            public Object getValueAt(int row, int col) {
				return orders[row][col];
			}

            public String getColumnName(int column) {
				return headers[column];
			}
            public Class getColumnClass(int col) {
				return getValueAt(0,col).getClass();
			}
            public boolean isCellEditable(int row, int col) {
				return (col==1);
			}
            public void setValueAt(Object aValue, int row, int column) {
                orders[row][column] = aValue;
            }
         };

		tableView = new JTable(dataModel);
        JScrollPane scrollpane = new JScrollPane(tableView);
		JLabel label1 = new JLabel("Best Value Auto Body Supply");
        scrollpane.setPreferredSize(new Dimension(500, 80));
		frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.NORTH,label1);
        frame.getContentPane().add(BorderLayout.CENTER,scrollpane);
        frame.pack();
		frame.setVisible(true);
		// for faster printing turn double buffering off

		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(false);

        PrinterJob pj=PrinterJob.getPrinterJob();

		pj.setPrintable(TodaysOrdersApplet.this);
		pj.printDialog();

		try{
	    	pj.print();
		}catch (Exception PrintException) {}

        frame.setVisible(true);
    }

    public int print(Graphics g, PageFormat pageFormat,
		int pageIndex) throws PrinterException {

        Graphics2D  g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		int fontHeight=g2.getFontMetrics().getHeight();
		int fontDesent=g2.getFontMetrics().getDescent();

		//leave room for page number
		double pageHeight =
	    pageFormat.getImageableHeight()-fontHeight;

		double pageWidth = pageFormat.getImageableWidth();
		double tableWidth = (double) tableView.getColumnModel().getTotalColumnWidth();
		double scale = 1;
		if (tableWidth >= pageWidth) {
			scale =  pageWidth / tableWidth;
		}

		double headerHeightOnPage= tableView.getTableHeader().getHeight()*scale;
		double tableWidthOnPage=tableWidth*scale;

		double oneRowHeight= (tableView.getRowHeight()+ tableView.getRowMargin())*scale;
		int numRowsOnAPage= (int)((pageHeight-headerHeightOnPage)/oneRowHeight);
		double pageHeightForTable=oneRowHeight*numRowsOnAPage;
		int totalNumPages= (int)Math.ceil(((double)tableView.getRowCount())/numRowsOnAPage);
		if(pageIndex>=totalNumPages) {
			return NO_SUCH_PAGE;
		}

		g2.translate(pageFormat.getImageableX(),
	    pageFormat.getImageableY());
		g2.drawString("Page: "+(pageIndex+1),(int)pageWidth/2-35,
			    (int)(pageHeight+fontHeight-fontDesent));//bottom center

		g2.translate(0f,headerHeightOnPage);
		g2.translate(0f,-pageIndex*pageHeightForTable);
		//TODO this next line treats the last page as a full page
		g2.setClip(0, (int)(pageHeightForTable*pageIndex),(int)
				    Math.ceil(tableWidthOnPage),
				    (int) Math.ceil(pageHeightForTable));

		g2.scale(scale,scale);
		tableView.paint(g2);
		g2.scale(1/scale,1/scale);
		g2.translate(0f,pageIndex*pageHeightForTable);
		g2.translate(0f, -headerHeightOnPage);
		g2.setClip(0, 0,(int) Math.ceil(tableWidthOnPage),
					(int)Math.ceil(headerHeightOnPage));
		g2.scale(scale,scale);
		tableView.getTableHeader().paint(g2);//paint header at top

		return Printable.PAGE_EXISTS;
	}

    public void init() {
        //new TodaysOrdersApplet();

/*        JLabel label = new JLabel(
                           "You are successfully running a Swing applet!");
        label.setHorizontalAlignment(JLabel.CENTER);

        //Add border.  Should use createLineBorder, but then the bottom
        //and left lines don't appear -- seems to be an off-by-one error.
        label.setBorder(BorderFactory.createMatteBorder(1,1,2,2,Color.black));

        getContentPane().add(label, BorderLayout.CENTER);*/
    }


	public Object[][] getTodaysOrders() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        Object[][] todaysOrders = null;
        try {
            String name = "//BVASSYST/TheSystem";
            PrintInterface iFace = (PrintInterface) Naming.lookup(name);
            todaysOrders = iFace.getTodaysOrders();
        } catch (Exception e) {
           logger.error("ComputePi exception: " +
                               e.getMessage());
            e.printStackTrace();
        }
        return todaysOrders;
	}
    public static void main(String[] args) {
       new TodaysOrdersApplet();
    }

}

