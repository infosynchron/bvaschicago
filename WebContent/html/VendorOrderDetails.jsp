<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.utils.DateUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Vendor Order Details</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetOrder() {
		document.forms[document.forms.length-1].elements[0].value = "GetOrder";
	}
	function Change() {
		document.forms[document.forms.length-1].elements[0].value = "Change";
	}
	function PendingPayments() {
		document.forms[document.forms.length-1].elements[0].value = "PendingPayments";
	}
	function PaidPayments() {
		document.forms[document.forms.length-1].elements[0].value = "PaidPayments";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function BackToMenu() {
		document.forms[document.forms.length-1].elements[0].value = "BackToMenu";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	//String invoiceAvail = (String) session.getAttribute("InvoiceAvail");
	//String focusVal = "invoiceNumber";
	//if (invoiceAvail != null && !invoiceAvail.trim().equals("")) {
	//	session.removeAttribute("InvoiceAvail");
	//	focusVal = "payingAmount";
	//}
%>

<BODY bgcolor="#999999">
	<html:form action="VendorOrderDetails.do" focus="orderNo">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>
							<u>VENDOR ORDER DETAILS</u>
						</h2>
					</TD>
				</TR>
				<TR>
					<TD align="left">
						<TABLE align="center">
							<TR>
								<TD align="right">Order No :</TD>
								<TD align="left"><html:text size="5" property="orderNo">
									</html:text> &nbsp;&nbsp;&nbsp; <html:submit onclick="GetOrder()"
										value="Get..."></html:submit></TD>
								<TD align="right">Company Name :</TD>
								<TD align="left"><html:text size="15"
										property="companyName">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Supplier Inv No :</TD>
								<TD align="left"><html:text size="15" property="supplInvNo">
									</html:text></TD>
								<TD align="right">Container No :</TD>
								<TD align="left"><html:text size="15"
										property="containerNo">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Order Date :</TD>
								<TD align="left"><html:text size="15" property="orderDate">
									</html:text></TD>
								<TD align="right">Shipped Date :</TD>
								<TD align="left"><html:text size="15" property="shipDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Arrived Date :</TD>
								<TD align="left"><html:text size="15"
										property="arrivedDate">
									</html:text></TD>
								<TD align="right">Is Final :</TD>
								<TD align="left"><html:checkbox property="isFinal"></html:checkbox>
								</TD>
							</TR>
							<TR>
								<TD align="right">Total Items :</TD>
								<TD align="left"><html:text size="15" property="totalItems">
									</html:text></TD>
								<TD align="right">Order Total :</TD>
								<TD align="left"><html:text size="15" property="orderTotal">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Discount :</TD>
								<TD align="left"><html:text size="15" property="discount">
									</html:text></TD>
								<TD align="right">Sticker Charges :</TD>
								<TD align="left"><html:text size="15"
										property="stickerCharges">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Overhead Total :</TD>
								<TD align="left"><html:text size="15"
										property="overheadAmountsTotal">
									</html:text></TD>
								<TD align="right">Units Order On :</TD>
								<TD align="left"><html:text size="15"
										property="unitsOrderDoneDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Prices Done On :</TD>
								<TD align="left"><html:text size="15"
										property="pricesDoneDate">
									</html:text></TD>
								<TD align="right">Inventory Done On :</TD>
								<TD align="left"><html:text size="15"
										property="inventoryDoneDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Payment Terms :</TD>
								<TD align="left"><html:text size="15"
										property="paymentTerms">
									</html:text></TD>
								<TD align="right">Payment Date :</TD>
								<TD align="left"><html:text size="15"
										property="paymentDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">ETA Date :</TD>
								<TD align="left"><html:text size="15"
										property="estimatedArrivalDate">
									</html:text></TD>
								<TD align="right"></TD>
								<TD align="left"></TD>
							</TR>
							<TR>
								<TD align="center" colspan="4">
									<%
            ErrorBean errorBean = (session.getAttribute("VendorOrderDetailsError") != null) ?
            						(ErrorBean) session.getAttribute("VendorOrderDetailsError") : new ErrorBean();
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
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 100px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Change()" value="Change"
										style="height: 35px;  width: 100px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="PendingPayments()" value="Pending Payments"
										style="height: 35px;  width: 100px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="PaidPayments()" value="Paid Payments"
										style="height: 35px;  width: 100px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="BackToMenu()" value="Back"
										style="height: 35px; width: 100px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ReturnToMain()" value="Return To Main"
										style="height: 35px; width: 100px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
