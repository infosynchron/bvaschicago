<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.bvas.beans.PartsBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>SubCategory.jsp</TITLE>
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
	function ShowParts() {
		document.forms[document.forms.length-1].elements[0].value = "ShowParts";
	}
	function ShowInven() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInven";
	}
	function Remove() {
		document.forms[document.forms.length-1].elements[0].value = "Remove";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<%
	String catSelected = "";
	Object catSel = session.getAttribute("CatSelected");
	if (catSel != null) {
		catSelected = (String) catSel;
	}
	Object catObject = session.getAttribute("AllCategories");
	Hashtable categories = null;
	if (catObject == null) {
	    categories = PartsBean.getCategories();
	    session.setAttribute("AllCategories", categories);
	} else {
	    categories = (Hashtable) catObject;
	}
%>

<BODY bgcolor="#999999">
	<html:form action="/SubCategory.do" focus="subCategoryCode">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" width="544" align="center">
			<TBODY>
				<TR>
					<TD width="520" align="center" style=""><b>SUB CATEGORIES
							MAINTENANCE</b></TD>
				</TR>
				<TR>
					<TD width="520" height="200" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD align="right">Sub Category Code:</TD>
								<TD align="left"><html:text property="subCategoryCode"
										size="10" maxlength="7"></html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="IdGet()" value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">Description:</TD>
								<TD align="left"><html:text property="subCategoryName"
										size="45" maxlength="30"></html:text></TD>
							</TR>
							<TR>
								<TD align="right">Categories:</TD>
								<TD align="left"><SELECT name="CategorySelected">
										<%
            			Enumeration ennum = categories.keys();
            			while (ennum.hasMoreElements()) {
            			    String sel = "";
            			    String catCd = (String) ennum.nextElement();
            			    if (catCd.trim().equals(catSelected)) {
            			        sel = "SELECTED";
            			    }
            			    String catName = (String) categories.get(catCd);
            			%>
										<OPTION value="<%= catCd %>" <%= sel %>>
											<%= catName %>
										</OPTION>
										<%
            			}
            			%>
								</SELECT></TD>
							</TR>
							<TR>
								<TD colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("SubCategoryError") != null) ?
            						(ErrorBean) session.getAttribute("SubCategoryError") : new ErrorBean();
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
								<TD><html:submit onclick="ShowAll()" value="All"
										style="height: 25px; width: 50px"></html:submit></TD>
								<TD><html:submit onclick="ShowParts()" value="Parts"
										style="height: 25px; width: 50px"></html:submit></TD>
								<TD><html:submit onclick="ShowInven()" value="Inven"
										style="height: 25px; width: 50px"></html:submit></TD>
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
