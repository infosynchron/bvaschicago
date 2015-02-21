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
<TITLE>Close Invoices Screen</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
	Object obj = session.getAttribute("AllPendingInvoices");
	Vector invoices = null;
	if (obj != null) {
	    invoices = (Vector) obj;
	} else {
	    invoices = InvoiceBean.getPendingInvoices();
	    if (invoices == null) {
	        response.sendRedirect("AcctMenu.do");
	    } else {
	        session.setAttribute("AllPendingInvoices", invoices);
	    }
	}
	int noOfInvoices = invoices.size();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function CloseInvoices() {
		document.forms[document.forms.length-1].elements[0].value = "CloseInvoices";
	}
	function CloseAll() {
		document.forms[document.forms.length-1].elements[0].value = "CloseAll";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function History(invno) {
		var gopage = "History" + invno;
		document.forms[document.forms.length-1].elements[0].value = gopage;
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintThisReport";
	}
	function BackToAcct() {
		document.forms[document.forms.length-1].elements[0].value = "BackToAcct";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="CloseInvoices.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>Closing Invoices Screen</h2>
					</TD>
				</TR>
				<TR>
					<TD align="left" width="520" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">No Of Pending Invoices :</TD>
								<TD align="left"><html:text size="10"
										property="pendingInvoices" value='<%= noOfInvoices + "" %>'>
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<table border="1">
										<%
            			
            			Hashtable checkedInvoices = null;
            			Object oooo = session.getAttribute("AllCheckedInvoices");
            			if (oooo == null) {
            			    checkedInvoices = new Hashtable();
            			} else {
            			    checkedInvoices = (Hashtable) oooo;
            			}
            			Enumeration ennum = invoices.elements();
            			InvoiceBean invoice = null;
            			int cnt = 0;
            			while (ennum.hasMoreElements()) {
            			    invoice = (InvoiceBean) ennum.nextElement();
            			    cnt++;
            			    String cbName = "CB" + cnt;
            			
            			if (cnt == 1) {
            			%>
										<TR>
											<TD><B>Invoice Number</B></TD>
											<TD><B>Order Date</B></TD>
											<TD><B>Invoice Total</B></TD>
											<TD><B>Balance</B></TD>
											<TD><B>Changes</B></TD>
											<TD><B>Close Invoices</B></TD>
										</TR>

										<%
				}
				String checked = "";
				checked = (String) checkedInvoices.get(invoice.getInvoiceNumber() + "");
				if (checked != null && !checked.trim().equals("")) {
				    checked = "CHECKED";
				} else {
				    checked = "";
				}
				%>

										<TR>
											<TD><%= invoice.getInvoiceNumber() %></TD>
											<TD><%= invoice.getOrderDate() %></TD>
											<TD><%= invoice.getInvoiceTotal() %></TD>
											<TD><%= invoice.getBalance() %></TD>
											<TD>
												<%
            				if (invoice.getHistory().trim().equals("Y")) {
            				    String histButt = "History(" + invoice.getInvoiceNumber() + ")";
            				%> <html:submit onclick="<%= histButt %>"
													value="History" style="width: 60px"></html:submit> <%
            				} else {
            				%> &nbsp; <%
            				}
            				%>
											</TD>
											<TD align="center"><INPUT TYPE="CHECKBOX"
												NAME="<%= cbName %>"
												VALUE="<%= invoice.getInvoiceNumber() %>" <%= checked %>></TD>
										</TR>
										<%
            			}
            			%>
									</table>
								</TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("CloseInvoicesError") != null) ?
            						(ErrorBean) session.getAttribute("CloseInvoicesError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="520" height="50" align="center">
						<TABLE>
							<TR align="center">
								<TD align="center" valign="middle"><html:submit
										onclick="CloseAll()" value="Close All 0's"
										style="height: 35px;  width: 120px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="CloseInvoices()" value="Close Invoices"
										style="height: 35px;  width: 120px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="BackToAcct()" value="Back"
										style="height: 35px; width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ReturnToMain()" value="Return To Main"
										style="height: 35px; width: 120px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
