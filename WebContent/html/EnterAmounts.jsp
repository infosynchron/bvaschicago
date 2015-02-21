<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.utils.DateUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Enter Cash & Checks</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
	
	Object oo = session.getAttribute("EnterAmountsPayments");
	String [][] payments = null;
	if (oo != null) {
	    payments = (String[][]) oo;
	}
	
	String reportCreated = (String) session.getAttribute("EnterAmountsReportCreated");
	if (reportCreated == null) {
	    reportCreated = "";
	}
	String reportCreated1 = (String) session.getAttribute("EnterAmountsReportCreated1");
	if (reportCreated1 == null) {
	    reportCreated1 = "";
	}
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "GetInvoice";
	}
	function AddNew() {
		document.forms[document.forms.length-1].elements[0].value = "AddNew";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function Checks() {
		document.forms[document.forms.length-1].elements[0].value = "Checks";
	}
	function Delete(no) {
		var buttName = "Delete" + no;
		document.forms[document.forms.length-1].elements[0].value = buttName;
	}
	function CreateStatement() {
		document.forms[document.forms.length-1].elements[0].value = "CreateStatement";
	}
	function AdjStatement() {
		document.forms[document.forms.length-1].elements[0].value = "AdjStatement";
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
<%
	String invoiceAvail = (String) session.getAttribute("InvoiceAvail");
	String focusVal = "invoiceNumber";
	if (invoiceAvail != null && !invoiceAvail.trim().equals("")) {
		session.removeAttribute("InvoiceAvail");
		focusVal = "payingAmount";
	}
%>

<BODY bgcolor="#999999">
	<html:form action="EnterAmounts.do" focus="<%= focusVal %>">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>Payments Maintenance Screen</h2>
					</TD>
				</TR>
				<TR>
					<TD align="left" width="520" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">Invoice No :</TD>
								<TD align="left"><html:text size="10"
										property="invoiceNumber">
									</html:text> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetInvoice()" value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">Customer :</TD>
								<TD align="left"><html:text size="30"
										property="companyName">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Total Credit Or Balance :</TD>
								<TD align="left"><html:text size="10"
										property="creditOrBalance">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Paid Till Now :</TD>
								<TD align="left"><html:text size="10"
										property="appliedAmount">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Current Balance :</TD>
								<TD align="left"><html:text size="10" property="balance">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Payment Amount :</TD>
								<TD align="left"><html:text size="10"
										property="payingAmount">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Check No :</TD>
								<TD align="left"><html:text size="10" property="checkNo">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("EnterAmountsError") != null) ?
            						(ErrorBean) session.getAttribute("EnterAmountsError") : new ErrorBean();
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
										onclick="AddNew()" value="Add New"
										style="height: 35px;  width: 75px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 60px"></html:submit></TD>
								<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/FS" + DateUtils.getNewUSDate() + ".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; 
                        %>
								<%
                if (reportCreated.trim().equalsIgnoreCase("Yes")) {
                %>
								<TD align="center" height="30"><html:submit
										onclick="<%= printMethod %>" value="Print Stmt"
										style="height: 35px;  width: 80px"></html:submit></TD>
								<%
	        } else {
	        %>
								<TD align="center" height="30"><html:submit
										onclick="CreateStatement()" value="Statement"
										style="height: 35px;  width: 80px"></html:submit></TD>
								<%
	        }
	        %>
								<% 
                        	String myPage1 = "http://" + serverName + "/bvaschicago/html/reports/FA" + DateUtils.getNewUSDate() + ".html";
                        	String printMethod1 = "NewWindow('"+myPage1+"', 'Ram','400','400','yes');return true;"; 
                        %>
								<%
                if (reportCreated1.trim().equalsIgnoreCase("Yes")) {
                %>
								<TD align="center" height="30"><html:submit
										onclick="<%= printMethod1 %>" value="Print"
										style="height: 35px;  width: 80px"></html:submit></TD>
								<%
	        } else {
	        %>
								<TD align="center" height="30"><html:submit
										onclick="AdjStatement()" value="Adjustments"
										style="height: 35px;  width: 80px"></html:submit></TD>
								<%
	        }
	        %>
								<TD align="center" valign="middle"><html:submit
										onclick="Checks()" value="Checks"
										style="height: 35px; width: 60px"></html:submit></TD>
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
				<%
        if (payments != null) {
        %>
				<tr>
					<TD width="520" align="center">
						<table border="1">
							<%
                for (int i=0; i<payments.length; i++) {
                %>
							<tr>
								<td><%= payments[i][0] %></td>
								<td><%= payments[i][1] %></td>
								<td><%= payments[i][2] %></td>
								<td><%= payments[i][3] %></td>
								<td><%= payments[i][4] %></td>
								<td><%= payments[i][5] %></td>
								<%
                    if (payments[i][1].trim().equals(DateUtils.getNewUSDate())) {
                        String buttName = "Delete(" + i + ")";
                    %>
								<td><html:submit onclick="<%= buttName %>" value="Delete"
										style="width: 60px"></html:submit></td>
								<%
                    }
                    %>
							
							<tr>
								<%
                }
                %>
							
						</table>
					</TD>
				</tr>
				<%
        }
        %>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
