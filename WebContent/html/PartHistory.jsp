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
<TITLE>PartHistory.jsp</TITLE>
</HEAD>
<%
   String partForHistory = (String) session.getAttribute("PartNoForPartHistory");
   session.removeAttribute("PartNoForPartHistory");
   UserBean user = (UserBean) session.getAttribute("User");
    Object oo1 = session.getAttribute("PartHistoryDetails1");
    Vector partDetails1 = null;
    boolean gotParts1 = false;
    if (oo1 != null) {
        partDetails1 = (Vector) oo1;
        gotParts1 = true;
    } else {
        gotParts1 = false;
    }
    Object oo2 = session.getAttribute("PartHistoryDetails2");
    Vector partDetails2 = null;
    boolean gotParts2 = false;
    if (oo2 != null) {
        partDetails2 = (Vector) oo2;
        gotParts2 = true;
    } else {
        gotParts2 = false;
    }
    Object oo3 = session.getAttribute("PartHistoryDetails3");
    Vector partDetails3 = null;
    boolean gotParts3 = false;
    if (oo3 != null) {
        partDetails3 = (Vector) oo3;
        gotParts3 = true;
    } else {
        gotParts3 = false;
    }
    Object oo4 = session.getAttribute("PartHistoryDetails4");
    Vector partDetails4 = null;
    boolean gotParts4 = false;
    if (oo4 != null) {
        partDetails4 = (Vector) oo4;
        gotParts4 = true;
    } else {
        gotParts4 = false;
    }
    Object oo5 = session.getAttribute("PartHistoryDetails5");
    Vector partDetails5 = null;
    boolean gotParts5 = false;
    if (oo5 != null) {
        partDetails5 = (Vector) oo5;
        gotParts5 = true;
    } else {
        gotParts5 = false;
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
	<html:form action="/PartHistory.do" focus="partNo">
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
								<TD align="left">
									<%
               	    if (partForHistory == null || partForHistory.trim().equals("")) {
               %> <html:text property="partNo" size="10" maxlength="7"></html:text>
									<%
               	    } else {
               %> <html:text size="5" property="partNo"
										value="<%= partForHistory %>">
									</html:text> <%
               	    }
               %>
								</TD>
								<TD align="left"><html:submit onclick="GetPart()"
										value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD colspan="3">
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
					<TD width="300" align="center" valign="middle" colspan="3">
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
				<TD align="center" style="font-size: 15pt;">YEAR & MODEL</TD>
				<TD align="center" style="font-size: 15pt;">DESCRIPTION</TD>
				<TD align="center" style="font-size: 15pt;">STOCK</TD>
				<TD align="center" style="font-size: 15pt;">COST</TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High")) {
        %>
				<TD align="center" style="font-size: 15pt;">ACTUAL</TD>
				<TD align="center" style="font-size: 15pt;">LOC</TD>
				<%
            }
        %>
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
				<TD><%= partDet[4] %></TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High")) {
        %>
				<TD><%= partDet[5] %></TD>
				<%
            }
        %>
				<TD><%= partDet[6] %></TD>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
		<%
if (gotParts2) {
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
				<TD align="center" style="font-size: 15pt;">DESCRIPTION</TD>
				<TD align="center" style="font-size: 15pt;">ORDER #</TD>
				<TD align="center" style="font-size: 15pt;">COMPANY</TD>
				<TD align="center" style="font-size: 15pt;">QTY</TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
        %>
				<TD align="center" style="font-size: 15pt;">PRICE</TD>
				<%
            }
        %>
			</TR>
			<%
        Enumeration ennum = partDetails2.elements();
        while (ennum.hasMoreElements()) {
            String [] partDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= partDet[0] %></TD>
				<TD><%= partDet[1] %></TD>
				<TD><%= partDet[2] %></TD>
				<TD><%= partDet[3] %></TD>
				<TD><%= partDet[4] %></TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
        %>
				<TD><%= partDet[5] %></TD>
				<%
            }
        %>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
		<%
if (gotParts5) {
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
				<TD align="center" style="font-size: 15pt;">ORDER #</TD>
				<TD align="center" style="font-size: 15pt;">COMPANY</TD>
				<TD align="center" style="font-size: 15pt;">QTY</TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
        %>
				<TD align="center" style="font-size: 15pt;">PRICE</TD>
				<%
            }
        %>
			</TR>
			<%
        Enumeration ennum = partDetails5.elements();
        while (ennum.hasMoreElements()) {
            String [] partDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= partDet[0] %></TD>
				<TD><%= partDet[1] %></TD>
				<TD><%= partDet[2] %></TD>
				<TD><%= partDet[3] %></TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
        %>
				<TD><%= partDet[4] %></TD>
				<%
            }
        %>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
		<%
if (gotParts3) {
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
				<TD align="center" style="font-size: 15pt;">INV #</TD>
				<TD align="center" style="font-size: 15pt;">DATE</TD>
				<TD align="center" style="font-size: 15pt;">COMPANY</TD>
				<TD align="center" style="font-size: 15pt;">QTY</TD>
				<TD align="center" style="font-size: 15pt;">PRICE</TD>
			</TR>
			<%
        Enumeration ennum = partDetails3.elements();
        while (ennum.hasMoreElements()) {
            String [] partDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= partDet[0] %></TD>
				<TD><%= partDet[1] %></TD>
				<TD><%= partDet[2] %></TD>
				<TD><%= partDet[3] %></TD>
				<TD><%= partDet[4] %></TD>
				<TD><%= partDet[5] %></TD>
			</TR>
			<%
        }
    %>
		</TABLE>
		<%
}
%>
		<%
if (gotParts4) {
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
				<TD align="center" style="font-size: 15pt;">INV #</TD>
				<TD align="center" style="font-size: 15pt;">QTY</TD>
				<TD align="center" style="font-size: 15pt;">SOLD</TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High")) {
        %>
				<TD align="center" style="font-size: 15pt;">ACTUAL</TD>
				<%
            }
        %>
			</TR>
			<%
        Enumeration ennum = partDetails4.elements();
        while (ennum.hasMoreElements()) {
            String [] partDet = (String []) ennum.nextElement();
    %>
			<TR>
				<TD><%= partDet[0] %></TD>
				<TD><%= partDet[1] %></TD>
				<TD><%= partDet[2] %></TD>
				<TD><%= partDet[3] %></TD>
				<%
            if (user.getRole().trim().equalsIgnoreCase("High")) {
        %>
				<TD><%= partDet[4] %></TD>
				<%
            }
        %>
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
