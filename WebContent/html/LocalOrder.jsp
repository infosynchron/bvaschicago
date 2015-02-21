<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.formBeans.*"%>
<%@ page import="com.bvas.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Local Orders</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintOrder";
	}
	
	function ClearOrder() {
		document.forms[document.forms.length-1].elements[0].value = "ClearOrder";
	}
	function DeleteOrder() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteOrder";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function CreateNewOrder() {
		document.forms[document.forms.length-1].elements[0].value = "CreateNewOrder";
	}
	function Get() {
		document.forms[document.forms.length-1].elements[0].value = "Get";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	String supId = (String) session.getAttribute("LocalSupplierId");
	String compName = (String) session.getAttribute("LocalCompanyName");
	String focusVal = "invoiceNo";
%>

<BODY bgcolor="#999999">
	<html:form action="/LocalOrder.do" focus="<%= focusVal %>">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE align="center" border="1">
			<TBODY>
				<TR>
					<TD align="center">
						<TABLE cellpadding="0" cellspacing="0">
							<TBODY>
								<TR>
									<TD style="font-size: 16pt"><B>Local Orders</B></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center">Supplier Id: <%= supId %>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Company Name: <%= compName.toUpperCase() %>
					</TD>
				</TR>
				<TR>
					<TD align="center">
						<TABLE>
							<TBODY>
								<TR>
									<TD align="center">
										<TABLE>
											<TBODY>
												<TR>
													<TD align="right">Invoice No</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="invoiceNo"
															size="12"></html:text></TD>
													<TD><html:submit onclick="Get()" value="Get..."
															style="width: 35px;"></html:submit></TD>
												</TR>
												<TR>
													<TD align="right">Date</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="dateEntered"
															size="12"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Part No</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="partNo"
															size="12"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Vendor Part No</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="localVendorNo"
															size="12"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Quantity</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="quantity"
															size="12"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Price</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="price" size="12"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Vendor Inv. No</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="vendInvNo"
															size="12"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Vendor Inv. Date</TD>
													<TD>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;</TD>
													<TD align="left"><html:text property="vendInvDate"
															size="12"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR align="center">
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("LocalOrderError") != null) ?
            						(ErrorBean) session.getAttribute("LocalOrderError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD><html:submit onclick="CreateNewOrder()"
											value="Create New" style="width: 80px; height:30px;"></html:submit>
									</TD>
									<TD><html:submit onclick="ClearOrder()" value="Clear"
											style="width: 80px; height:30px;"></html:submit></TD>
									<TD><html:submit onclick="DeleteOrder()" value="Delete"
											style="width: 80px;"></html:submit></TD>
									<%	// EVERY BODY CANN'T SEE THE PRINT BUTTON
                        UserBean user = (UserBean) session.getAttribute("User");
                        if (user.getRole().trim().equalsIgnoreCase("high")) {
                        %>
									<TD>
										<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/LocalOrders.html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
										<html:submit onclick="<%= printMethod %>" value="Print"
											style="width: 80px;"></html:submit>
									</TD>
									<%
                        }
                        %>
									<TD><html:submit onclick="ReturnToMain()"
											value="Return To Main" style="width: 100px; height:30px;"></html:submit>
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
