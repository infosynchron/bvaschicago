<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Maintaining Vendor Items</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetItem() {
		document.forms[document.forms.length-1].elements[0].value = "GetItem";
	}
	function AddNewItem() {
		document.forms[document.forms.length-1].elements[0].value = "AddNewItem";
	}
	function ChangeItem() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeItem";
	}
	function DeleteItem() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteItem";
	}
	function ClearForm() {
		document.forms[document.forms.length-1].elements[0].value = "ClearForm";
	}
	function ClearDups() {
		document.forms[document.forms.length-1].elements[0].value = "ClearDups";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	String companyName = (String) session.getAttribute("CompanyNameForItem");
	String suppId = (String) session.getAttribute("SupplierIdForItem");
	if (suppId == null) suppId = "";
	if (companyName == null) companyName = "";
	
%>
<BODY bgcolor="#999999">
	<html:form action="/VendorItem.do" focus="vendorPartNo">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>Maintaining Vendor Items</TD>
				</TR>
				<TR>
					<TD align="center">Vendor Id: <html:text size="10"
							property="supplierId" value="<%= suppId %>"></html:text>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Company Name: <%= companyName.toUpperCase() %>
					</TD>
				</TR>
				<TR>
					<TD>
						<TABLE border="1" align="center">
							<TBODY>
								<TR>
									<TD>
										<TABLE>
											<TBODY>
												<TR>
													<TD align="right">Part No :</TD>
													<TD><html:text size="10" property="partNo"></html:text>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
															onclick="GetItem()" value="Get..."></html:submit></TD>
												</TR>
												<TR>
													<TD align="right">Vendor Item No:</TD>
													<TD><html:text size="15" property="vendorPartNo"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Item Description 1:</TD>
													<TD><html:text size="30" property="itemDesc1"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Item Description 2:</TD>
													<TD><html:text size="30" property="itemDesc2"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">PL No:</TD>
													<TD><html:text size="15" property="plNo"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
									<TD valign="top">
										<TABLE>
											<TBODY>
												<TR>
													<TD align="right">OEM No:</TD>
													<TD><html:text size="15" property="oemNo"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Item Price:</TD>
													<TD><html:text size="15" property="sellingRate"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">No. Of Pieces:</TD>
													<TD><html:text size="15" property="noOfPieces"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Item Size:</TD>
													<TD><html:text size="15" property="itemSize"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Units:</TD>
													<TD><html:text size="15" property="itemSizeUnits"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD>

						<TABLE align="center">
							<TR align="center">
								<TD>
									<%
            ErrorBean errorBean = (session.getAttribute("VendorItemError") != null) ?
            						(ErrorBean) session.getAttribute("VendorItemError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>

						<TABLE align="center">
							<TR align="center">
								<TD><html:submit onclick="AddNewItem()"
										value="Add a New Part" style="width: 150px;">
									</html:submit></TD>
								<TD><html:submit onclick="ChangeItem()"
										value="Change This Part" style="width: 150px;">
									</html:submit></TD>
								<TD><html:submit onclick="DeleteItem()"
										value="Delete This Part" style="width: 150px;">
									</html:submit></TD>
							</TR>
						</TABLE>
						<TABLE align="center">
							<TR align="center">
								<TD><html:submit onclick="ClearForm()" value="Clear"
										style="width: 150px;"></html:submit></TD>
								<TD><html:submit onclick="ReturnToMain()"
										value="Return To Main Menu" style="width: 150px;"></html:submit>
								</TD>
								<TD><html:submit onclick="ClearDups()" value="Clear Dups"
										style="width: 150px;">
									</html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<%
            Object oops = session.getAttribute("VendorSuggestedParts");
            Vector vv = null;
            if (oops != null) {
                vv = (Vector) oops;
            }
            if (vv != null && vv.size() > 0) {
        %>
				<TR>
					<TABLE BORDER="1" ALIGN="center">
						<TR>
							<TD>BV Part No</TD>
							<TD>OEM or PL</TD>
							<TD>BV Description</TD>
							<TD>Other Vendor's Desc</TD>
						</TR>
						<%
                Enumeration enumX = vv.elements();
                while (enumX.hasMoreElements()) {
                    String [] arrX = (String[]) enumX.nextElement();
                %>
						<TR>
							<TD><%= arrX[0] %></TD>
							<TD><%= arrX[1] %></TD>
							<TD><%= arrX[2] %></TD>
							<TD><%= arrX[3] %></TD>
						</TR>

						<%
                }
                %>
					</TABLE>
				</TR>
				<%
            }
        %>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
