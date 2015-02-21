<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.UserBean"%>
<%@ page import="java.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>CustomerHistory.jsp</TITLE>
</HEAD>
<%
   UserBean user = (UserBean) session.getAttribute("User");
    Object oo1 = session.getAttribute("CustHistoryDetails1");
    Vector custDetails1 = null;
    boolean gotCust1 = false;
    if (oo1 != null) {
        custDetails1 = (Vector) oo1;
        gotCust1 = true;
    } else {
        gotCust1 = false;
    }
    Object oo2 = session.getAttribute("CustHistoryDetails2");
    Vector custDetails2 = null;
    boolean gotCust2 = false;
    if (oo2 != null) {
        custDetails2 = (Vector) oo2;
        gotCust2 = true;
    } else {
        gotCust2 = false;
    }
    Object oo3 = session.getAttribute("CustHistoryDetails3");
    Vector custDetails3 = null;
    boolean gotCust3 = false;
    if (oo3 != null) {
        custDetails3 = (Vector) oo3;
        gotCust3 = true;
    } else {
        gotCust3 = false;
    }
%>
<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetCust() {
		document.forms[document.forms.length-1].elements[0].value = "GetCust";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function Back() {
		document.forms[document.forms.length-1].elements[0].value = "Back";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/CustomerHistory.do" focus="partNo">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center" style="font-size: 20pt;">Customer History</TD>
				</TR>
				<TR>
					<TD width="300" height="100" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD align="right">Customer Id:</TD>
								<TD align="left"><html:text property="customerId" size="15"
										maxlength="10"></html:text></TD>
								<TD align="left"><html:submit onclick="GetCust()"
										value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">From Date:</TD>
								<TD align="left"><html:text property="fromDate" size="10"
										maxlength="10"></html:text></TD>
							</TR>
							<TR>
								<TD align="right">To Date:</TD>
								<TD align="left"><html:text property="toDate" size="10"
										maxlength="10"></html:text></TD>
							</TR>
							<TR>
								<TD colspan="3">
									<%
            ErrorBean errorBean = (session.getAttribute("CustHistoryError") != null) ?
            						(ErrorBean) session.getAttribute("CustHistoryError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="300" align="center" valign="middle" colspan="3">
						<TABLE>
							<TR>
								<TD><html:submit onclick="Clear()" value="Clear"
										style="height: 25px; width: 65px"></html:submit></TD>
								<TD><html:submit onclick="Back()" value="Back"
										style="height: 25px; width: 65px"></html:submit></TD>
								<TD><html:submit onclick="ReturnToMain()"
										value="Return To Main Menu" style="height: 25px; width: 150px"></html:submit>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<%
if (gotCust1) {
%>
		<table>
			<tr>
				<td>&nbsp;<br />
				</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TR>
				<B>
					<TD align="center" style="font-size: 12pt;">CUSTOMER ID</TD>
					<TD align="center" style="font-size: 12pt;">COMPANY NAME</TD>
					<TD align="center" style="font-size: 12pt;">TERMS</TD>
					<TD align="center" style="font-size: 12pt;">TOTAL PURCHASE</TD>
					<TD align="center" style="font-size: 12pt;">CUR. BALANCE</TD>
					<TD align="center" style="font-size: 12pt;">WRITE OFF</TD>
				</B>
			</TR>
			<%
        Enumeration ennum = custDetails1.elements();
        while (ennum.hasMoreElements()) {
            String [] custDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= custDet[0] %></TD>
				<TD><%= custDet[1] %></TD>
				<TD><%= custDet[2] %></TD>
				<TD><%= custDet[3] %></TD>
				<TD><%= custDet[4] %></TD>
				<TD><%= custDet[5] %></TD>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
		<%
if (gotCust2) {
%>
		<table>
			<tr>
				<td>&nbsp;<br />
				</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TR>
				<TD align="center" style="font-size: 12pt;">Chk Id</TD>
				<TD align="center" style="font-size: 12pt;">System Dt</TD>
				<TD align="center" style="font-size: 12pt;">Check No & Dt</TD>
				<TD align="center" style="font-size: 12pt;">Amount</TD>
				<TD align="center" style="font-size: 12pt;">Cur Bal.</TD>
				<TD align="center" style="font-size: 12pt;">Payment Details</TD>
			</TR>
			<%
        Enumeration ennum = custDetails2.elements();
        while (ennum.hasMoreElements()) {
            String [] custDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= custDet[0] %></TD>
				<TD><%= custDet[1] %></TD>
				<TD><%= custDet[2] %></TD>
				<TD><%= custDet[3] %></TD>
				<TD><%= custDet[4] %></TD>
				<TD><%= custDet[5] %></TD>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
		<%
if (gotCust3) {
%>
		<table>
			<tr>
				<td>&nbsp;<br />
				</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TR>
				<TD align="center" style="font-size: 10pt;">INVOICE #</TD>
				<TD align="center" style="font-size: 10pt;">INV DATE</TD>
				<TD align="center" style="font-size: 10pt;">INV TOTAL</TD>
				<TD align="center" style="font-size: 10pt;">REMARKS</TD>
				<TD align="center" style="font-size: 10pt;">PAYMENT DETAILS</TD>
			</TR>
			<%
        Enumeration ennum = custDetails3.elements();
        while (ennum.hasMoreElements()) {
            String [] custDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD style="font-size: 10pt;"><%= custDet[0] %></TD>
				<TD style="font-size: 10pt;"><%= custDet[1] %></TD>
				<TD style="font-size: 10pt;"><%= custDet[2] %></TD>
				<TD style="font-size: 10pt;"><%= custDet[3] %></TD>
				<TD style="font-size: 8pt;"><%= custDet[4] %></TD>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
	</html:form>
</BODY>
</html:html>
