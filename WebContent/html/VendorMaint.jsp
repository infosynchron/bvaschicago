<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>VendorMaint.jsp</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetVendor() {
		document.forms[document.forms.length-1].elements[0].value = "GetVendor";
	}
	function AddNewVendor() {
		document.forms[document.forms.length-1].elements[0].value = "AddNewVendor";
	}
	function ChangeVendor() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeVendor";
	}
	function DeleteVendor() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteVendor";
	}
	function PrintVendorListing() {
		document.forms[document.forms.length-1].elements[0].value = "PrintVendorListing";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
		document.forms[document.forms.length-1].elements[0].value = "PrintVendorListing";
	}

	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/VendorMaint.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>Vendor Administration Panel - VAP</TD>
				</TR>
				<TR>
					<TD>
						<TABLE align="center">
							<TBODY>
								<TR>
									<TD align="right">Supplier Id:</TD>
									<TD><html:text size="10" property="supplierId"></html:text>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
											onclick="GetVendor()" value="Get..."></html:submit></TD>
								</TR>
								<TR>
									<TD align="right">Company Name:</TD>
									<TD><html:text size="40" property="companyName"></html:text></TD>
									<TD></TD>
									<TD align="right">Phone:</TD>
									<TD><html:text size="20" property="phone"></html:text></TD>
								</TR>
								<TR>
									<TD align="right">Contact Name:</TD>
									<TD><html:text size="40" property="contactName"></html:text></TD>
									<TD></TD>
									<TD align="right">Fax:</TD>
									<TD><html:text size="20" property="fax"></html:text></TD>
								</TR>
								<TR>
									<TD align="right">Contact Title:</TD>
									<TD><html:text size="40" property="contactTitle"></html:text></TD>
									<TD></TD>
									<TD align="right">Home Page:</TD>
									<TD><html:text size="20" property="homePage"></html:text></TD>
								</TR>
								<TR>
									<TD align="right">Address 1:</TD>
									<TD><html:text size="40" property="address1"></html:text></TD>
									<TD></TD>
									<TD align="right">E-Mail:</TD>
									<TD><html:text size="20" property="email"></html:text></TD>
								</TR>
								<TR>
									<TD align="right">Address 2:</TD>
									<TD><html:text size="40" property="address2"></html:text></TD>
									<!--<TD>Client Comments</TD>-->
								</TR>
								<TR>
									<TD align="right">City:</TD>
									<TD><html:text size="20" property="city"></html:text></TD>
									<TD></TD>
									<TD COLSPAN="2" ROWSPAN="4">Client Comments:<BR />
									<html:textarea rows="4" cols="25" property="comments"></html:textarea></TD>
									<TD></TD>
								</TR>
								<TR>
									<TD align="right">Region:</TD>
									<TD><html:text size="20" property="region"></html:text></TD>
									<TD></TD>
									<TD></TD>
								</TR>
								<TR>
									<TD align="right">Postal Code:</TD>
									<TD><html:text size="10" property="postalCode"></html:text></TD>
									<TD></TD>
									<TD></TD>
								</TR>
								<TR>
									<TD align="right">Country:</TD>
									<TD><html:text size="25" property="country"></html:text></TD>
									<TD></TD>
									<TD></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD>
						<TABLE align="center">
							<TBODY>
								<TR align="center">
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("VendorMaintError") != null) ?
            						(ErrorBean) session.getAttribute("VendorMaintError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
							</TBODY>
						</TABLE>
						<TABLE align="center">
							<TBODY>
								<TR>
									<TD><html:submit onclick="AddNewVendor()"
											value="Add New Vendor" style="width: 140px;"></html:submit></TD>
									<TD><html:submit onclick="ChangeVendor()"
											value="Change Open Vendor" style="width: 140px;"></html:submit>
									</TD>
									<TD><html:submit onclick="DeleteVendor()"
											value="Delete Open Vendor" style="width: 140px;"></html:submit>
									</TD>
									<TD>
										<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/VendorListing.html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
										<html:submit onclick="<%= printMethod %>"
											value="Print Vendor Listing" style="width: 140px;"></html:submit>
									</TD>
									<TD><html:submit onclick="ReturnToMain()"
											value="Return To Main Menu" style="width: 140px;"></html:submit>
									</TD>
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
