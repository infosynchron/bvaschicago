<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Accounting Menu</TITLE>
<STYLE type="text/css">
<!--
INPUT {
	height: 50px;
	width: 150px;
	vertical-align: sub
}
-->
</STYLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function MaintainFinance() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainFinance";
	}
	function EnterAmounts() {
		document.forms[document.forms.length-1].elements[0].value = "EnterAmounts";
	}
	function CustomerAccounts() {
		document.forms[document.forms.length-1].elements[0].value = "CustomerAccounts";
	}
	function SplitReceivable() {
		document.forms[document.forms.length-1].elements[0].value = "SplitReceivable";
	}
	function SplitReceivableNew() {
		document.forms[document.forms.length-1].elements[0].value = "SplitReceivableNew";
	}
	function AccountsReceivable() {
		document.forms[document.forms.length-1].elements[0].value = "AccountsReceivable";
	}
	function PendingInvoices() {
		document.forms[document.forms.length-1].elements[0].value = "PendingInvoices";
	}
	function AccountsPayable() {
		document.forms[document.forms.length-1].elements[0].value = "AccountsPayable";
	}
	function WriteOff() {
		document.forms[document.forms.length-1].elements[0].value = "WriteOff";
	}
	function BouncedChecks() {
		document.forms[document.forms.length-1].elements[0].value = "BouncedChecks";
	}
	function CloseInvoices() {
		document.forms[document.forms.length-1].elements[0].value = "CloseInvoices";
	}
	function CustomerHistory() {
		document.forms[document.forms.length-1].elements[0].value = "CustomerHistory";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#d0cccc">
	<html:form action="/AcctMenu.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="0" height="400" bgcolor="#999999" align="center">
			<TBODY>
				<TR>
					<TD bgcolor="#d0cccc">
						<%	// EVERY BODY CANN'T SEE THIS BUTTON
			UserBean user = (UserBean) session.getAttribute("User");
			%>
						<TABLE>
							<TBODY>
								<TR>
									<TD width="500" colspan="2" align="center" height="60"
										style="font-size: 15pt;"><B>Accounting Menu</B> <br /></TD>
								</TR>
								<TR>
									<TD align="center"><html:submit
											onclick="MaintainFinance()" value="Maintain Finance"></html:submit>
									</TD>
									<%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high")) {
		        %>
									<TD align="center"><html:submit onclick="BouncedChecks()"
											value="Bounced Checks"></html:submit></TD>
									<%
                        } 
                        %>
								</TR>
								<TR>
									<TD align="center">
										<%	// EVERY BODY CANN'T SEE THIS BUTTON
			if (user.getRole().trim().equalsIgnoreCase("high") ||
			 	user.getRole().trim().equalsIgnoreCase("Acct")) {
			%> <html:submit onclick="EnterAmounts()" value="Enter Amounts"></html:submit>
										<%
                        }
                        %>
									</TD>
									<%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high")) {
		        %>
									<TD align="center"><html:submit onclick="CloseInvoices()"
											value="Close Invoices"></html:submit></TD>
									<%
                        } 
                        %>
								</TR>
								<%	// EVERY BODY CANN'T SEE THIS BUTTON
		    if (user.getRole().trim().equalsIgnoreCase("high") ||
		        user.getUsername().trim().equalsIgnoreCase("Mary") ||
		        user.getUsername().trim().equalsIgnoreCase("Saunak") ||
			user.getRole().trim().equalsIgnoreCase("Acct")) {
		    %>
								<TR>
									<TD align="center"><html:submit
											onclick="CustomerAccounts()" value="Customer Accounts"></html:submit>
									</TD>
									<TD align="center"><html:submit
											onclick="PendingInvoices()" value="Pending Invoices"></html:submit>
									</TD>
								</TR>
								<TR>
									<TD align="center"><html:submit
											onclick="AccountsReceivable()" value="Accounts Receivable"></html:submit>
									</TD>
									<%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high")) {
		        %>
									<TD align="center"><html:submit
											onclick="SplitReceivableNew()" value="Ageing Receivables"></html:submit>
									</TD>
									<%
                        } 
                        %>
								</TR>
								<TR>
									<TD align="center"><html:submit
											onclick="AccountsPayable()" value="Accounts Payable"></html:submit>
									</TD>
									<%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high")) {
		        %>
									<TD align="center"><html:submit onclick="WriteOff()"
											value="Write Off Invoice"></html:submit></TD>
									<%
                        } 
                        %>
								</TR>
								<TR>
									<TD align="center"><html:submit
											onclick="CustomerHistory()" value="Customer History"></html:submit>
									</TD>
								</TR>
								<%
                    } 
                    %>
								<TR>
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("AcctMenuError") != null) ?
            						(ErrorBean) session.getAttribute("AcctMenuError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD colspan="2" align="center" height="50"><html:submit
											onclick="ReturnToMain()" value="Return To Main Menu"
											style="height: 40px; width: 200px"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
