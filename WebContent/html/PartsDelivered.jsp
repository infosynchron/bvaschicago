<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.InvoiceBean"%>
<%@ page import="com.bvas.utils.DateUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Parts Delivered Screen</TITLE>
</HEAD>
<%
	Object obj = session.getAttribute("DeliveryPendingInvoices");
	Vector invoices = null;
	if (obj != null) {
	    invoices = (Vector) obj;
	} else {
	    invoices = InvoiceBean.getDeliveryPendingInvoices();
	    if (invoices == null) {
	        response.sendRedirect("RoutingMenu.do");
	    } else {
	        session.setAttribute("DeliveryPendingInvoices", invoices);
	    }
	}
	int noOfInvoices = invoices.size();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function Change() {
		document.forms[document.forms.length-1].elements[0].value = "Change";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintThisReport";
	}
	function BackToRouting() {
		document.forms[document.forms.length-1].elements[0].value = "BackToRouting";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="PartsDelivered.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>Parts Delivered Entry Screen</h2>
					</TD>
				</TR>
				<TR>
					<TD width="520" height="50" align="center">
						<TABLE>
							<TR align="center">
								<TD align="center" valign="middle"><html:submit
										onclick="Change()" value="Enter"
										style="height: 35px;  width: 120px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="BackToRouting()" value="Back"
										style="height: 35px; width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ReturnToMain()" value="Return To Main"
										style="height: 35px; width: 120px"></html:submit></TD>
							</TR>
							<TR>
								<TD>Select Who Delivered <SELECT name="whoDelivered"
									style="width: 100px;">
										<!-- <OPTION value="Eddie" >Eddie</OPTION> -->

										<OPTION VALUE="Select" SELECTED>Select</OPTION>
										<OPTION value="Alfonso">Alfonso</OPTION>
										<OPTION value="Carlos">Carlos</OPTION>
										<OPTION value="Daniel">Daniel</OPTION>
										<OPTION value="Diaz">Diaz</OPTION>
										<OPTION value="Enrique">Enrique</OPTION>
										<OPTION value="Ernie">Ernie</OPTION>
										<OPTION value="Fuji">Fuji</OPTION>
										<OPTION value="Galo">Galo</OPTION>
										<OPTION value="Geovanny">Geovanny</OPTION>
										<OPTION value="John">John</OPTION>
										<OPTION value="Jose">Jose</OPTION>
										<OPTION value="Khalid">Khalid</OPTION>
										<OPTION value="Moises">Moises</OPTION>
										<OPTION value="Pedro.Jr.">Pedro.Jr.</OPTION>
										<OPTION value="Peter">Peter</OPTION>
										<OPTION value="Puma">Puma</OPTION>
										<OPTION value="Reyes">Reyes</OPTION>
										<OPTION value="Richard">Richard</OPTION>
										<OPTION value="Santiago">Santiago</OPTION>
										<OPTION value="Shannon">Shannon</OPTION>
										<OPTION value="Tobias">Tobias</OPTION>
										<OPTION value="Victorino">Victoriano</OPTION>
										<OPTION value="Vivi">Vivi</OPTION>
										<OPTION value="Others">Others</OPTION>
								</SELECT>
								</TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("PartsDeliveredError") != null) ?
            						(ErrorBean) session.getAttribute("PartsDeliveredError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="left" width="520" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">No Of Invoices :</TD>
								<TD align="left"><html:text size="10"
										property="pendingInvoices" value='<%= noOfInvoices + "" %>'>
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<table border="1">
										<%
            			
            			Enumeration ennum = invoices.elements();
            			InvoiceBean invoice = null;
            			int cnt = 0;
            			while (ennum.hasMoreElements()) {
            			    invoice = (InvoiceBean) ennum.nextElement();
            			    cnt++;
            			    String cbName = "PD" + cnt;
            			
            			if (cnt == 1) {
            			%>
										<TR>
											<TD><B>Invoice Number</B></TD>
											<TD><B>Check Box</B></TD>
											<TD><B>Order Date</B></TD>
											<TD><B>Inv Total</B></TD>
											<TD><B>Balance</B></TD>
										</TR>

										<%
				}
				%>

										<TR>
											<TD><%= invoice.getInvoiceNumber() %></TD>
											<TD align="center"><INPUT TYPE="CHECKBOX"
												NAME="<%= cbName %>"
												VALUE="<%= invoice.getInvoiceNumber() %>"></TD>
											<TD><%= invoice.getOrderDate() %></TD>
											<TD><%= invoice.getInvoiceTotal() %></TD>
											<TD><%= invoice.getBalance() %></TD>
										</TR>
										<%
				}
				%>
									</table>
								</TD>
							</TR>

						</TABLE>
					</TD>
				</TR>

			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
