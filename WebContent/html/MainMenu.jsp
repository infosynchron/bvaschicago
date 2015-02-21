<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.beans.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>MainMenu.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function CreateInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "CreateInvoice";
	}
	function InvoiceArchives() {
		document.forms[document.forms.length-1].elements[0].value = "InvoiceArchives";
	}
	function Reports() {
		document.forms[document.forms.length-1].elements[0].value = "Reports";
	}
	function InventoryAvailability() {
		document.forms[document.forms.length-1].elements[0].value = "InventoryAvailability";
	}
	function MaintainInventoryMenu() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainInventoryMenu";
	}
	function RoutingMenu() {
		document.forms[document.forms.length-1].elements[0].value = "RoutingMenu";
	}
	function OrderProcessMenu() {
		document.forms[document.forms.length-1].elements[0].value = "OrderProcessMenu";
	}
	function AcctMenu() {
		document.forms[document.forms.length-1].elements[0].value = "AcctMenu";
	}
	function LookupClient() {
		document.forms[document.forms.length-1].elements[0].value = "LookupClient";
	}
	function MaintainClients() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainClients";
	}
	function MaintainLocalVendors() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainLocalVendors";
	}
	function MaintainVendors() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainVendors";
	}
	function MaintainOther() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainOther";
	}
	function CreateFax() {
		document.forms[document.forms.length-1].elements[0].value = "CreateFax";
	}
	function LogOut() {
		document.forms[document.forms.length-1].elements[0].value = "LogOut";
	}
	function InvoiceFocus() {
		xxxLabel.value="You have selected xxx";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/MainMenu.do" method="POST">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE align="center">
			<TBODY>
				<TR>
					<%
        	String user = "";
        	Object o = session.getAttribute("User");
        	UserBean userBean = null;
        	if (o!=null) {
	        	userBean = (UserBean) o;
	        	user = (userBean.getUsername()!=null?userBean.getUsername():"");
	        } else {
	        	response.sendRedirect("/bvaschicago/html/Login.jsp");
	        	return;
	        }
        %>
					<TD style="font-size: 15pt;" ALIGN="CENTER" COLSPAN="2">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <B>WELCOME
							<%= user.toUpperCase() %> TO BEST VALUE, CHICAGO
					</B>
					</TD>
				</TR>
				<TR>
					<TD WIDTH="250" VALIGN="TOP">
						<TABLE>
							<%
			    if (!userBean.getUsername().trim().equalsIgnoreCase("Warehouse")) {
			%>
							<TR align="center">
								<TD><html:submit onclick="CreateInvoice()"
										value="Create Invoice" style="width: 150px; height: 40px"
										onmouseover="InvoiceFocus();"></html:submit></TD>
							</TR>
							<%
			    }
			%>
							<TR align="center">
								<TD><html:submit onclick="InvoiceArchives()"
										value="Invoice Archives" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<TR align="center">
								<TD><html:submit onclick="InventoryAvailability()"
										value="Inventory Availability"
										style="width: 150px; height: 40px"></html:submit></TD>
							</TR>
							<TR align="center">
								<TD><html:submit onclick="LookupClient()"
										value="Client Lookup" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<TR align="center">
								<TD><html:submit onclick="CreateFax()" value="Create Fax"
										style="width: 150px; height: 40px"></html:submit></TD>
							</TR>
							<TR align="center">
								<TD><html:submit onclick="Reports()" value="Reports"
										style="width: 150px; height: 40px"></html:submit></TD>
							</TR>
							<TR align="center">
								<TD><html:submit onclick="LogOut()" value="Log Out"
										style="width: 150px; height: 40px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
					<TD WIDTH="250" ALIGN="RIGHT" VALIGN="TOP">
						<TABLE>
							<%
			if (userBean.getRole() != null && !userBean.getRole().trim().equals("") &&
				(userBean.getRole().trim().equalsIgnoreCase("high") ||
					userBean.getUsername().trim().equalsIgnoreCase("Marcie") || 
					userBean.getUsername().trim().equalsIgnoreCase("Raj") || 
					userBean.getUsername().trim().equalsIgnoreCase("Margarita") || 
					userBean.getUsername().trim().equalsIgnoreCase("Martha") || 
					userBean.getUsername().trim().equalsIgnoreCase("Edward") || 
					userBean.getUsername().trim().equalsIgnoreCase("Saunak"))) {
			%>
							<TR align="center">
								<TD><html:submit onclick="RoutingMenu()"
										value="Routing System" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<%
			}
			%>

							<%
			if (userBean.getRole() != null && !userBean.getRole().trim().equals("") &&
				(userBean.getRole().trim().equalsIgnoreCase("high") ||
					userBean.getRole().trim().equalsIgnoreCase("medium") ||
					userBean.getRole().trim().equalsIgnoreCase("Bob") ||
					userBean.getUsername().trim().equalsIgnoreCase("Corrina") ||
					userBean.getUsername().trim().equalsIgnoreCase("Warehouse") ||
					userBean.getUsername().trim().equalsIgnoreCase("Raj") ||
					userBean.getUsername().trim().equalsIgnoreCase("Rosie")  ||
					userBean.getUsername().trim().equalsIgnoreCase("Gabby")  ||
					userBean.getUsername().trim().equalsIgnoreCase("Jesse") ||
					userBean.getUsername().trim().equalsIgnoreCase("Isabel") ||
					userBean.getUsername().trim().equalsIgnoreCase("Trainee") ||
					userBean.getUsername().trim().equalsIgnoreCase("Teresa"))) {
			%>
							<TR align="center">
								<TD><html:submit onclick="MaintainInventoryMenu()"
										value="Maintain Inven Menu" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<%
			}
			%>



							<%
			if (userBean.getRole() != null && !userBean.getRole().trim().equals("") &&
				(userBean.getRole().trim().equalsIgnoreCase("high") ||
					userBean.getRole().trim().equalsIgnoreCase("medium") ||
					userBean.getUsername().trim().equalsIgnoreCase("Mary") || 
					userBean.getUsername().trim().equalsIgnoreCase("Saunak") || 
					userBean.getRole().trim().equalsIgnoreCase("acct"))) {
			%>
							<TR align="center">
								<TD><html:submit onclick="AcctMenu()" value="Accounting"
										style="width: 150px; height: 40px"></html:submit></TD>
							</TR>
							<%
			}
			%>



							<%
			if (userBean.getRole() != null && !userBean.getRole().trim().equals("") &&
				(userBean.getRole().trim().equalsIgnoreCase("high") ||
					userBean.getRole().trim().equalsIgnoreCase("medium") ||
					userBean.getRole().trim().equalsIgnoreCase("acct") ||
					userBean.getUsername().trim().equalsIgnoreCase("Ram") ||
					userBean.getUsername().trim().equalsIgnoreCase("Ram"))) {
			%>
							<TR align="center">
								<TD><html:submit onclick="MaintainClients()"
										value="Maintain Clients" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<%
			}
			%>



							<%
			if (
					userBean.getRole() != null && !userBean.getRole().trim().equals("") && ( (userBean.getRole().trim().equalsIgnoreCase("high")) || (userBean.getUsername().trim().equalsIgnoreCase("Bob")) )
				
					) {
			%>
							<TR align="center">
								<TD><html:submit onclick="MaintainOther()"
										value="Maintain Make & Models"
										style="width: 150px; height: 40px"></html:submit></TD>
							</TR>
							<%
			}
			%>
							<%
			if (userBean.getRole() != null && !userBean.getRole().trim().equals("") &&
				(userBean.getRole().trim().equalsIgnoreCase("high") || userBean.getUsername().trim().equalsIgnoreCase("Gabby") || userBean.getUsername().trim().equalsIgnoreCase("Rosie") || userBean.getUsername().trim().equalsIgnoreCase("Isabel") || userBean.getUsername().trim().equalsIgnoreCase("Corrina") || userBean.getUsername().trim().equalsIgnoreCase("Nancy") || userBean.getUsername().trim().equalsIgnoreCase("Jesse") || userBean.getUsername().trim().equalsIgnoreCase("Teresa") || userBean.getUsername().trim().equalsIgnoreCase("Trainee"))) {
			%>
							<TR align="center">
								<TD><html:submit onclick="MaintainLocalVendors()"
										value="Local Vendor Menu" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<%
			}
			%>
							<%
			if (userBean.getRole() != null && !userBean.getRole().trim().equals("") &&
				(userBean.getRole().trim().equalsIgnoreCase("high") || userBean.getRole().trim().equalsIgnoreCase("medium") || userBean.getUsername().trim().equalsIgnoreCase("Gabby") || userBean.getUsername().trim().equalsIgnoreCase("Rosie") || userBean.getUsername().trim().equalsIgnoreCase("Isabel") || userBean.getUsername().trim().equalsIgnoreCase("Corrina") || userBean.getUsername().trim().equalsIgnoreCase("Teresa") || userBean.getUsername().trim().equalsIgnoreCase("Jesse") || userBean.getUsername().trim().equalsIgnoreCase("Trainee"))) {
			%>
							<TR align="center">
								<TD><html:submit onclick="MaintainVendors()"
										value="Vendor Menu" style="width: 150px; height: 40px"></html:submit>
								</TD>
							</TR>
							<%
			}
			%>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD ALIGN="CENTER" COLSPAN="2"><LABEL
						style="border: 1px; color: maroon;" id="xxxLabel"></LABEL> <%
            ErrorBean errorBean = (session.getAttribute("MainMenuError") != null) ?
            						(ErrorBean) session.getAttribute("MainMenuError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <FONT
						size="4"> <B><I>Date: <%= new java.util.Date() %></I></B>
					</FONT></TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
