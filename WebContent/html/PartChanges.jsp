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
<TITLE>PartChanges.jsp</TITLE>
</HEAD>
<%
    UserBean user = (UserBean) session.getAttribute("User");
    Object oo1 = session.getAttribute("PartChangesDetails1");
    Vector partDetails1 = null;
    boolean gotParts1 = false;
    if (oo1 != null) {
        partDetails1 = (Vector) oo1;
        gotParts1 = true;
    } else {
        gotParts1 = false;
    }
%>
<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetPart() {
		document.forms[document.forms.length-1].elements[0].value = "GetPart";
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
	<html:form action="/PartChanges.do" focus="partNo">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center" style="font-size: 20pt;">Parts History</TD>
				</TR>
				<TR>
					<TD width="300" height="100" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD align="right">Part No:</TD>
								<TD align="left"><html:text property="partNo" size="10"
										maxlength="7"></html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetPart()" value="Get..."></html:submit></TD>
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
								<TD colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("PartHistoryError") != null) ?
            						(ErrorBean) session.getAttribute("PartHistoryError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="300" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD><html:submit onclick="Clear()" value="Clear"
										style="height: 25px; width: 65px"></html:submit>
									<!--<INPUT type="submit" name="Remove" value="Remove"> --></TD>
								<TD><html:submit onclick="Back()" value="Back"
										style="height: 25px; width: 65px"></html:submit>
									<!--<INPUT type="submit" name="Remove" value="Remove"> --></TD>
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
if (gotParts1) {
%>
		<table>
			<tr>
				<td>&nbsp;<br />
				</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TR>
				<TD align="center" style="font-size: 15pt;">PART NO</TD>
				<TD align="center" style="font-size: 15pt;">BY</TD>
				<TD align="center" style="font-size: 15pt;">DATE</TD>
				<TD align="center" style="font-size: 15pt;">REMARKS</TD>
			</TR>
			<%
        Enumeration ennum = partDetails1.elements();
        while (ennum.hasMoreElements()) {
            String [] partDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= partDet[0] %></TD>
				<TD><%= partDet[1] %></TD>
				<TD><%= partDet[2] %></TD>
				<TD><%= partDet[3] %></TD>
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
