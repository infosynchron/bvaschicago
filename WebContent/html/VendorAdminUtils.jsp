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
<TITLE>Vendor Admin Utils</TITLE>
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
	function ChangeUnitsOnOrder() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeUnitsOnOrder";
	}
	function DeleteTheOrder() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteTheOrder";
	}
	function DeleteItemsFromOrder() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteItemsFromOrder";
	}
	function CreateOrderForOthers() {
		document.forms[document.forms.length-1].elements[0].value = "CreateOrderForOthers";
	}
	function DeleteItemsFromOrder1() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteItemsFromOrder1";
	}
	function CompareForPrices() {
		document.forms[document.forms.length-1].elements[0].value = "CompareForPrices";
	}
	function MergeOrders() {
		document.forms[document.forms.length-1].elements[0].value = "MergeOrders";
	}
	function FindCuft() {
		document.forms[document.forms.length-1].elements[0].value = "FindCuft";
	}
	function ChangeOrder() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeOrder";
	}
	function MakeFinal() {
		document.forms[document.forms.length-1].elements[0].value = "MakeFinal";
	}
	function RemoveFinal() {
		document.forms[document.forms.length-1].elements[0].value = "RemoveFinal";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/VendorAdminUtils.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<table align="center">
			<tr>
				<td><u><h1>Vendor Administration Utilities</h1></u></td>
			</tr>
		</table>
		<table align="center">
			<TR>
				<TD align="center" colspan="2" height="50" style="font-size: 16pt">
					<%
            ErrorBean errorBean = (session.getAttribute("VendorAdminUtilsError") != null) ?
            						(ErrorBean) session.getAttribute("VendorAdminUtilsError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <B><LABEL style="border: 1px; color: white;"><%= error %></LABEL></B>

				</TD>
			</TR>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForUnitsOrder"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ChangeUnitsOnOrder()" value="Change Units On Order"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForDeleteOrder"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="DeleteTheOrder()" value="Delete This Order"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForDeleteItems"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="DeleteItemsFromOrder()"
											value="Delete Items From Order" style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No 1: <html:text
											property="orderNoForCreateOrder1" size="10"></html:text>&nbsp;&nbsp;
										Order No 2: <html:text property="orderNoForCreateOrder2"
											size="10"></html:text>&nbsp;&nbsp; Supplier Id: <html:text
											property="supplierIdForCreateOrder" size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="CreateOrderForOthers()"
											value="Create Order For Others" style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No 1: <html:text
											property="orderNoForDeleteItems1" size="10"></html:text>&nbsp;&nbsp;
										Order No 2: <html:text property="orderNoForDeleteItems2"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="DeleteItemsFromOrder1()"
											value="Delete Items From Order 1" style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text
											property="orderNoForComparePrices" size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="CompareForPrices()" value="Compare Prices"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No 1 : <html:text
											property="orderNoForMergeOrders1" size="10"></html:text>&nbsp;&nbsp;
										Order No 2 : <html:text property="orderNoForMergeOrders2"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="MergeOrders()" value="Merge To Order # 1"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForCuft"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="FindCuft()" value="Find Cuft" style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForChange"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="ChangeOrder()" value="ChangeOrder"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForMakeFinal"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="MakeFinal()" value="Make Final"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD>Order No : <html:text property="orderNoForRemoveFinal"
											size="10"></html:text>&nbsp;&nbsp;
									</TD>
									<TD align="center" height="30"><html:submit
											onclick="RemoveFinal()" value="Remove Final"
											style="width: 200px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<table align="center">
			<TR>
				<TD align="center" colspan="2" height="70"><html:submit
						onclick="ReturnToMain()" value="Retutn To Main Menu"
						style="height: 30px; width: 200px;"></html:submit></TD>
			</TR>
		</table>
	</html:form>
</BODY>
</html:html>
