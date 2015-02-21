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
<TITLE>MissingParts.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetPart() {
		document.forms[document.forms.length-1].elements[0].value = "GetPart";
	}
	function Change() {
		document.forms[document.forms.length-1].elements[0].value = "Change";
	}
	function New() {
		document.forms[document.forms.length-1].elements[0].value = "New";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function List() {
		document.forms[document.forms.length-1].elements[0].value = "List";
	}
	function Back() {
		document.forms[document.forms.length-1].elements[0].value = "Back";
	}
	function Delete() {
		document.forms[document.forms.length-1].elements[0].value = "Delete";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<%
%>

<BODY bgcolor="#999999">
	<html:form action="/MissingParts.do" focus="partNo">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE width="550" border="1" align="center">
			<TBODY>
				<TR>
					<TD width="550" align="center" height="50" style="font-size: 24pt"><B>MISSING
							PARTS MAINTENANCE</B><BR></TD>
				</TR>
				<TR>
					<TD width="550" align="center">
						<table align="center">
							<TR>
								<TD align="right" style="font-size: 11pt">Part No:</TD>
								<TD align="left"><html:text size="5" property="partNo">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right" style="font-size: 11pt">Part Order:</TD>
								<TD align="left"><html:text size="5" property="partOrder">
									</html:text> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetPart()" value="Get...">
									</html:submit></TD>
							</TR>
							<TR>
								<TD align="right" style="font-size: 11pt">Part Description:
								</TD>
								<TD align="left"><html:text size="60"
										property="partDescription" maxlength="50"></html:text></TD>
							</TR>
							<TR>
								<TD align="right" style="font-size: 11pt">Cost Price:</TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="15" property="costPrice">
									</html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									Units In Stock: &nbsp;&nbsp; <html:text size="15"
										property="unitsInStock">
									</html:text></TD>
							</TR>
							<%
            	UserBean user = (UserBean) session.getAttribute("User");
            	if (user.getRole().trim().equalsIgnoreCase("high")) {
            %>
							<TR>
								<TD align="right" style="font-size: 11pt"><B>Actual
										Price: </B></TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="15" property="actualPrice">
									</html:text></TD>
							</TR>
							<%
            	}
            %>
							<TR>
								<TD align="right" style="font-size: 11pt">Missing Qty:</TD>
								<TD align="left"><html:text size="10" property="quantity"></html:text>
								</TD>
							</TR>

						</table>
					</TD>
				</TR>
				<TR>
					<TD width="550" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD>
									<%
            ErrorBean errorBean = (session.getAttribute("MissingPartsError") != null) ?
            						(ErrorBean) session.getAttribute("MissingPartsError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="550" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD>&nbsp;&nbsp; <html:submit onclick="Clear()"
										value="Clear" style="width: 70px;height: 30px"></html:submit>
								</TD>
								<TD>&nbsp;&nbsp; <html:submit onclick="New()" value="New"
										style="width: 70px;height: 30px">
									</html:submit>
								</TD>
								<TD>&nbsp;&nbsp; <html:submit onclick="Change()"
										value="Change" style="width: 70px;height: 30px">
									</html:submit>
								</TD>
								<TD>&nbsp;&nbsp; <html:submit onclick="List()" value="List"
										style="width: 70px;height: 30px"></html:submit>
								</TD>
								<TD>&nbsp;&nbsp; <html:submit onclick="Back()" value="Back"
										style="width: 70px;height: 30px"></html:submit>
								</TD>
								<TD>&nbsp;&nbsp; <html:submit onclick="ReturnToMain()"
										value="Return Main" style="width: 100px;height: 30px"></html:submit>
								</TD>
								<TD>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<html:submit onclick="Delete()" value="Found"
										style="width: 50px;height: 30px"></html:submit>
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
