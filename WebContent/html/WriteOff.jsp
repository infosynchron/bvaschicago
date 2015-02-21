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
<TITLE>Write Off</TITLE>
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
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "GetInvoice";
	}
	function WriteOff() {
		document.forms[document.forms.length-1].elements[0].value = "WriteOff";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function Delete() {
		document.forms[document.forms.length-1].elements[0].value = "Delete";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function BackToAcct() {
		document.forms[document.forms.length-1].elements[0].value = "BackToAcct";
	}
	function ShowReport() {
		document.forms[document.forms.length-1].elements[0].value = "ShowReport";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="WriteOff.do" focus="invoiceNumber">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h1>Write Off Screen</h1>
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
								<TD align="right">Balance :</TD>
								<TD align="left"><html:text size="10" property="balance">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Customer :</TD>
								<TD align="left"><html:text size="30"
										property="companyName">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Date :</TD>
								<TD align="left"><html:text size="10"
										property="writeOffDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Notes :</TD>
								<TD align="left"><html:text size="50" property="notes">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("WriteOffError") != null) ?
            						(ErrorBean) session.getAttribute("WriteOffError") : new ErrorBean();
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
										onclick="WriteOff()" value="Write Off"
										style="height: 35px;  width: 75px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Delete()" value="Delete"
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
				<TR>
					<TD width="520" height="50" align="center">
						<TABLE>
							<TR align="center">
								<TD align="right">From :</TD>
								<TD align="left"><html:text size="10"
										property="reportFromDate">
									</html:text></TD>
								<TD align="right">To :</TD>
								<TD align="left"><html:text size="10"
										property="reportToDate">
									</html:text></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ShowReport()" value="Show Report"
										style="height: 35px;  width: 100px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
