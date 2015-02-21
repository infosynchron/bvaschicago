<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>MakeModelMaint.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetMakeModel() {
		document.forms[document.forms.length-1].elements[0].value = "Get";
	}
	function Previous() {
		document.forms[document.forms.length-1].elements[0].value = "Previous";
	}
	function Next() {
		document.forms[document.forms.length-1].elements[0].value = "Next";
	}
	function AddNew() {
		document.forms[document.forms.length-1].elements[0].value = "AddNew";
	}
	function Change() {
		document.forms[document.forms.length-1].elements[0].value = "Change";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function Remove() {
		document.forms[document.forms.length-1].elements[0].value = "Remove";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="MakeModelMaint.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">Make/Model Maintenance</TD>
				</TR>
				<TR>
					<TD align="left" width="520" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">Make/Model Code:</TD>
								<TD align="left"><html:text size="10"
										property="makeModelCode">
									</html:text> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetMakeModel()" value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">Make/Model Name:</TD>
								<TD align="left"><html:text size="30"
										property="makeModelName">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Manufacturer Id:</TD>
								<TD align="left"><html:text size="10"
										property="manufacturerId">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Inter Changeable Model:</TD>
								<TD align="left"><html:text size="30"
										property="interChangeModel">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("MakeModelMaintError") != null) ?
            						(ErrorBean) session.getAttribute("MakeModelMaintError") : new ErrorBean();
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
										onclick="Previous()" value="Previous"
										style="height: 60px;  width: 60px"></html:submit></TD>
								<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 90px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Change()" value="Change"
										style="height: 35px;  width: 90px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="AddNew()" value="Add New"
										style="height: 35px;  width: 65px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Remove()" value="Remove"
										style="height: 35px;  width: 65px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ReturnToMain()" value="Return To Main"
										style="height: 35px; width: 120px"></html:submit></TD>
								<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Next()" value="Next"
										style="height: 60px;  width: 60px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
