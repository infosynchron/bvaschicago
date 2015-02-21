<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.InvoiceBean"%>
<%@ page import="com.bvas.beans.BouncedChecksBean"%>
<%@ page import="com.bvas.utils.DateUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Customer Accounts Maintenance</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
	Object obj = session.getAttribute("CustomerAccountsInvoices");
	Vector invoices = null;
	if (obj != null) {
	    invoices = (Vector) obj;
	} else {
	    invoices = new Vector();
	}
	Object bChecks = session.getAttribute("CustomerAccountsBouncedChecks");
	Vector bouncedChecks = null;
	if (bChecks != null) {
	    bouncedChecks = (Vector) bChecks;
	}
	
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetCustomer() {
		document.forms[document.forms.length-1].elements[0].value = "GetCustomer";
	}
	function CreateStatement() {
		document.forms[document.forms.length-1].elements[0].value = "CreateStatement";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
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
	function BackToAcct() {
		document.forms[document.forms.length-1].elements[0].value = "BackToAcct";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="CustomerAccounts.do" focus="customerId">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>Customer Accounts Maintenance</h2>
					</TD>
				</TR>
				<TR>
					<TD align="left" width="520" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">Customer Id :</TD>
								<TD align="left"><html:text size="10" property="customerId">
									</html:text> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetCustomer()" value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">Company Name :</TD>
								<TD align="left"><html:text size="30"
										property="companyName">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Route # :</TD>
								<TD align="left"><html:text size="10" property="routeNo">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">No Of Pending Invoices :</TD>
								<TD align="left"><html:text size="10"
										property="pendingInvoices">
									</html:text></TD>
							</TR>
							<%
            		if (bouncedChecks != null) {
            		%>
							<TR>
								<TD align="right">Total Bounced Checks :</TD>
								<TD align="left"><html:text size="10"
										property="bouncedChecksAmount">
									</html:text></TD>
							</TR>
							<%
            		}
            		%>
							<TR>
								<TD align="right">Credit OR Balance :</TD>
								<TD align="left"><html:text size="10"
										property="amountPending">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<table border="1">
										<%
            			
            			Hashtable checkedInvoices = null;
            			Object oooo = session.getAttribute("CheckedInvoices");
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
											<TD><B>For Statement</B></TD>
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
											<TD align="center"><INPUT TYPE="CHECKBOX"
												NAME="<%= cbName %>"
												VALUE="<%= invoice.getInvoiceNumber() %>" <%= checked %>></TD>
										</TR>
										<%
            			}
            			%>
										<%
            			if (bouncedChecks != null) {
            			    Enumeration enum1 = bouncedChecks.elements();
            			    while (enum1.hasMoreElements()) {
            			        BouncedChecksBean bcheck = (BouncedChecksBean) enum1.nextElement();    
            			%>
										<TR>
											<TD><%= "BC" + bcheck.getCheckId() %></TD>
											<TD><%= bcheck.getCheckNo() + " / " + bcheck.getCheckDate() %></TD>
											<TD>&nbsp;</TD>
											<TD><%= bcheck.getBalance() %></TD>
											<TD align="center"><INPUT TYPE="CHECKBOX" NAME="BC1"
												VALUE="<%= bcheck.getCheckId() %>" CHECKED></TD>
										</TR>
										<%
            			    }
            			}
            			%>
									</table>
								</TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("CustomerAccountsError") != null) ?
            						(ErrorBean) session.getAttribute("CustomerAccountsError") : new ErrorBean();
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
										onclick="CreateStatement()" value="Create Statement"
										style="height: 35px;  width: 120px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 60px"></html:submit></TD>
								<!-- <TD align="center" valign="middle">
            	<html:submit onclick="Change()" value="Change" style="height: 35px;  width: 90px"></html:submit>
            	</TD> -->
								<!-- <TD align="center" valign="middle">
            	<html:submit onclick="Remove()" value="Remove" style="height: 35px;  width: 65px"></html:submit>
            	</TD> -->
								<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/SFN.html";
                            //String myPage = "C:/reports/123.html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; 
                        %>
								<TD align="center" height="30"><html:submit
										onclick="<%= printMethod %>" value="Print Statement"
										style="height: 35px;  width: 120px"></html:submit></TD>
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
