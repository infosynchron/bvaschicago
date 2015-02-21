<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Category.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function IdGet() {
		document.forms[document.forms.length-1].elements[0].value = "IdGet";
	}
	function Previous() {
		document.forms[document.forms.length-1].elements[0].value = "Previous";
	}
	function Next() {
		document.forms[document.forms.length-1].elements[0].value = "Next";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function AddNew() {
		document.forms[document.forms.length-1].elements[0].value = "AddNew";
	}
	function Change() {
		document.forms[document.forms.length-1].elements[0].value = "Change";
	}
	function ShowAll() {
		document.forms[document.forms.length-1].elements[0].value = "ShowAll";
	}
	function ShowSub() {
		document.forms[document.forms.length-1].elements[0].value = "ShowSub";
	}
	function Remove() {
		document.forms[document.forms.length-1].elements[0].value = "Remove";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#d0cccc">
	<html:form action="/Category.do" focus="categoryCode">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" width="544" align="center">
			<TBODY>
				<TR>
					<TD width="520" align="center" style=""><b>CATEGORIES
							MAINTENANCE</b></TD>
				</TR>
				<TR>
					<TD width="520" height="200" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD align="right">Category Code:</TD>
								<TD align="left"><html:text property="categoryCode"
										size="10" maxlength="7"></html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="IdGet()" value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">Description:</TD>
								<TD align="left"><html:text property="categoryName"
										size="45" maxlength="30"></html:text></TD>
							</TR>
							<TR>
								<TD colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("CategoryError") != null) ?
            						(ErrorBean) session.getAttribute("CategoryError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="520" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD><html:submit onclick="Previous()" value="Previous"
										style="height: 60px; width: 60px"></html:submit>
									<!--<INPUT type="submit" name="Previous" value="Previous"> -->
								</TD>
								<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
								<TD><html:submit onclick="Clear()" value="Clear"
										style="height: 25px; width: 65px"></html:submit></TD>
								<TD><html:submit onclick="AddNew()" value="Add New"
										style="height: 25px; width: 65px"></html:submit></TD>
								<TD><html:submit onclick="Change()" value="Change"
										style="height: 25px;  width: 65px"></html:submit></TD>
								<TD><html:submit onclick="Remove()" value="Remove"
										style="height: 25px; width: 65px"></html:submit></TD>
								<TD><html:submit onclick="ShowAll()" value="Show All Cat's"
										style="height: 25px; width: 100px"></html:submit></TD>
								<TD><html:submit onclick="ShowSub()" value="Show Sub Cat's"
										style="height: 25px; width: 100px"></html:submit></TD>
								<TD><html:submit onclick="ReturnToMain()" value="Return"
										style="height: 25px; width: 50px"></html:submit></TD>
								<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
								<TD><html:submit onclick="Next()" value="Next"
										style="height: 60px; width: 60px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
