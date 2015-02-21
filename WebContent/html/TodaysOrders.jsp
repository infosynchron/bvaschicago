<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.UserBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>TodaysOrders.jsp</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
	UserBean user = (UserBean) session.getAttribute("User");
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "JustPrintAll";
	}
	function ShowDaysSales() {
		document.forms[document.forms.length-1].elements[0].value = "ShowDaysSales";
	}
	function ShowSalesForPers() {
		document.forms[document.forms.length-1].elements[0].value = "ShowSalesForPers";
	}
	function ShowSalesForRt() {
		document.forms[document.forms.length-1].elements[0].value = "ShowSalesForRt";
	}
	function ShowCustomerSales() {
		document.forms[document.forms.length-1].elements[0].value = "ShowCustomerSales";
	}
	function ShowPartsSold() {
		document.forms[document.forms.length-1].elements[0].value = "ShowPartsSold";
	}
	function ShowReturns() {
		document.forms[document.forms.length-1].elements[0].value = "ShowReturns";
	}
	function OldShowSalesForPers() {
		document.forms[document.forms.length-1].elements[0].value = "OldShowSalesForPers";
	}
	function ShowInvoices() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInvoices";
	}
	function PartsPending() {
		document.forms[document.forms.length-1].elements[0].value = "PartsPending";
	}
	function ShowCODPending() {
		document.forms[document.forms.length-1].elements[0].value = "ShowCODPending";
	}
	function ShowOtherPending() {
		document.forms[document.forms.length-1].elements[0].value = "ShowOtherPending";
	}
	function ShowAllPending() {
		document.forms[document.forms.length-1].elements[0].value = "ShowAllPending";
	}
	function ShowDeposits() {
		document.forms[document.forms.length-1].elements[0].value = "ShowDeposits";
	}
	function AnalyseInvoices() {
		document.forms[document.forms.length-1].elements[0].value = "AnalyseInvoices";
	}
	function CostOfGoodsSold() {
		document.forms[document.forms.length-1].elements[0].value = "CostOfGoodsSold";
	}
	function InventoryOnHand() {
		document.forms[document.forms.length-1].elements[0].value = "InventoryOnHand";
	}
	function InventoryOnHandByCat() {
		document.forms[document.forms.length-1].elements[0].value = "InventoryOnHandByCat";
	}
	function ShowInvoicesNotPrinted() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInvoicesNotPrinted";
	}
	function ShowInvoicesNotDelivered() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInvoicesNotDelivered";
	}
	function ShowInvoicesNotPickedUp() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInvoicesNotPickedUp";
	}
	function ShowInvoicesNotPickedUpAccts() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInvoicesNotPickedUpAccts";
	}
	function PrintByCustomer() {
		document.forms[document.forms.length-1].elements[0].value = "PrintByCustomer";
	}
	function PrintByShipMethod() {
		document.forms[document.forms.length-1].elements[0].value = "PrintByShipMethod";
	}
	function PrintByPartNo() {
		document.forms[document.forms.length-1].elements[0].value = "PrintByPartNo";
	}
	function VendorPurchases() {
		document.forms[document.forms.length-1].elements[0].value = "VendorPurchases";
	}
	function LocalPurchases() {
		document.forms[document.forms.length-1].elements[0].value = "LocalPurchases";
	}
	function LocalPurchasesByVend() {
		document.forms[document.forms.length-1].elements[0].value = "LocalPurchasesByVend";
	}
	function GetCustomers() {
		document.forms[document.forms.length-1].elements[0].value = "GetCustomers";
	}
	function ShowInvoiceHistory() {
		document.forms[document.forms.length-1].elements[0].value = "ShowInvoiceHistory";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/TodaysOrders.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>Todays Orders Selection Panel</TD>
				</TR>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>
										<%	
                        		if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("CorrinaNY") || user.getUsername().trim().equalsIgnoreCase("Nancy") || user.getUsername().trim().equalsIgnoreCase("Jose")) {
                        	%> From : <html:text property="fromTodaysDate"
											size="10"></html:text>&nbsp;&nbsp; <%
                        		}
                        	%> Date : <html:text property="toTodaysDate"
											size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%	
                        		if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("CorrinaNY") || user.getUsername().trim().equalsIgnoreCase("Nancy") || user.getUsername().trim().equalsIgnoreCase("Jose")) {
                        	%> <html:text property="salesPerson" size="10"></html:text>
										<%
                        		}
                        	%>
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowDaysSales()" value="Show Sales"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Marcie")) {
                    %>
								<TR>
									<TD>
										<%	
                    			if (user.getRole().trim().equalsIgnoreCase("High")) {
                    		%> From : <html:text property="fromDateForPers"
											size="10"></html:text>&nbsp;&nbsp; <%	
                    			}
                    		%> Date : <html:text property="toDateForPers"
											size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowSalesForPers()" value="All Sales Person's Sales"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("Jose")) {
                    %>
								<TR>
									<TD>
										<%	
                    			if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("Jose")) {
                    		%> From : <html:text property="fromDateForRt"
											size="10"></html:text>&nbsp;&nbsp; <%	
                    			}
                    		%> Date : <html:text property="toDateForRt"
											size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowSalesForRt()" value="Sales By Region"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim().equalsIgnoreCase("Medium") || user.getUsername().trim().equalsIgnoreCase("Marcie")) {
                    %>
								<TR>
									<TD>
										<%	
                    			if (user.getRole().trim().equalsIgnoreCase("High")) {
                    		%> From : <html:text property="fromDateForReturns"
											size="10"></html:text>&nbsp;&nbsp; <%	
                    			}
                    		%> Date : <html:text property="toDateForReturns"
											size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowReturns()" value="Show Returns"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>
										<%	
                        		if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Nancy")) {
                        	%> From : <html:text
											property="invoiceOrderFromDate" size="10"></html:text>&nbsp;&nbsp;
										<%
                        		}
                        	%> Date : <html:text
											property="invoiceOrderToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<%	
                        		if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Eddie")) {
                        	%> <html:text property="invoiceSalesPerson"
											size="10"></html:text> <%
                        		}
                        	%>
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowInvoices()" value="Show Invoices"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowInvoicesNotPrinted()"
											value="Invoices Not Printed" style="width: 200px;"></html:submit></TD>
								</TR>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowInvoicesNotDelivered()"
											value="Invoices Not Delivered" style="width: 200px;"></html:submit></TD>
								</TR>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>

								<%
			if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Raj") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("Jose")) {
			%>
								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="PartsPending()" value="Parts Pending"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
			}
			%>

								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>

								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowInvoicesNotPickedUp()"
											value="Pending Pickup COD's & CASH" style="width: 200px;"></html:submit></TD>
								</TR>
								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowInvoicesNotPickedUpAccts()"
											value="Pending Pickup Accounts" style="width: 200px;"></html:submit></TD>
								</TR>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text
											property="analyseInvoiceFromOrderDate" size="10"></html:text>&nbsp;&nbsp;
										To : <html:text property="analyseInvoiceToOrderDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="AnalyseInvoices()" value="Analyse Invoices"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="fromDateForCust" size="10"></html:text>&nbsp;&nbsp;
										Date : <html:text property="toDateForCust" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowCustomerSales()" value="Customer Sales"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="fromDateForParts"
											size="10"></html:text>&nbsp;&nbsp; Date : <html:text
											property="toDateForParts" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowPartsSold()" value="Show Parts Sold"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="InventoryOnHand()" value="Inventory On Hand"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
									<TD align="center" height="30"><html:submit
											onclick="InventoryOnHandByCat()" value="Inventory By Cat"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="costOfGoodsFromOrderDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="costOfGoodsToOrderDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="CostOfGoodsSold()" value="Show Cost Of Goods Sold"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="vendorOrderFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="vendorOrderToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="VendorPurchases()" value="Show Vendor Purchases"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="localOrderFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="localOrderToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="LocalPurchases()" value="Show Local Purchases"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>


								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="localOrderByVendFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="localOrderByVendToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Vendor : <html:text property="vendorNo" size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="LocalPurchasesByVend()"
											value="Local Purchases By Vendor" style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>


								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="custFromDate" size="10"></html:text>&nbsp;&nbsp;
										To : <html:text property="custToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Bef Fm Dt : <html:text property="befThisDate" size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="GetCustomers()" value="Get Customers"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>

								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim().equalsIgnoreCase("Medium")) {
                    %>
								<TR>
									<TD>
										<%	
                    			if (user.getRole().trim().equalsIgnoreCase("High")) {
                    		%> From : <html:text property="fromDateForOldPers"
											size="10"></html:text>&nbsp;&nbsp; <%	
                    			}
                    		%> Date : <html:text property="toDateForOldPers"
											size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="OldShowSalesForPers()"
											value="Old Sales Person's Sales" style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Margarita") || user.getUsername().trim().equalsIgnoreCase("Edward")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="invoiceHistoryFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="invoiceHistoryToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowInvoiceHistory()" value="Show Invoice History"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>
								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim().equalsIgnoreCase("Acct")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="codPendingFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="codPendingToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowCODPending()" value="Cash & COD Pending Inv"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>


								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim().equalsIgnoreCase("Acct")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="otherPendingFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="otherPendingToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowOtherPending()" value="Accounts Pending Inv"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>

								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim().equalsIgnoreCase("Acct")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="allPendingFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="allPendingToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowAllPending()" value="All Pending Invoices"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>

								<%	
                    	if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Mullick") || user.getUsername().trim().equalsIgnoreCase("Shailesh")) {
                    %>
								<TR>
									<TD align="center" height="10" colspan="2">&nbsp;</TD>
								</TR>
								<TR>
									<TD>From : <html:text property="depositsFromDate"
											size="10"></html:text>&nbsp;&nbsp; To : <html:text
											property="depositsToDate" size="10"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ShowDeposits()" value="Show Deposits"
											style="width: 200px;"></html:submit></TD>
								</TR>
								<%
                    	}
                    %>


								<TR>
									<TD align="center" colspan="2" height="50">
										<%
            ErrorBean errorBean = (session.getAttribute("TodaysOrdersError") != null) ?
            						(ErrorBean) session.getAttribute("TodaysOrdersError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD align="center" colspan="2" height="70"><html:submit
											onclick="ReturnToMain()" value="Retutn To Main Menu"
											style="width: 200px;"></html:submit></TD>
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
