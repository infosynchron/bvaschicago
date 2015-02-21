<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.bvas.formBeans.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>InvoiceArch.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "GetInvoice";
	}
	function GoToFinanceForm() {
		document.forms[document.forms.length-1].elements[0].value = "GoToFinanceForm";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function Previous() {
		document.forms[document.forms.length-1].elements[0].value = "Previous";
	}
	function Next() {
		document.forms[document.forms.length-1].elements[0].value = "Next";
	}
	function PreviousForCust() {
		document.forms[document.forms.length-1].elements[0].value = "PreviousForCust";
	}
	function NextForCust() {
		document.forms[document.forms.length-1].elements[0].value = "NextForCust";
	}
	function PreviousForPers() {
		document.forms[document.forms.length-1].elements[0].value = "PreviousForPers";
	}
	function NextForPers() {
		document.forms[document.forms.length-1].elements[0].value = "NextForPers";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#dddddd">
	<html:form action="/InvoiceArch.do" focus="invoiceNumber">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR align="center">
					<TD>
						<TABLE>
							<TBODY>
								<TR>
									<TD></TD>
									<TD>
										<TABLE cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD>Best Value Invoice Archives</TD>
												</TR>
												<TR>
													<TD>
														<TABLE border="1">
															<TBODY>
																<TR>
																	<TD align="center" style="font-size: 11pt">Invoice
																		Number</TD>
																	<TD align="center" style="font-size: 11pt">Order
																		Date</TD>
																	<TD align="center" style="font-size: 11pt">Customer
																		Id</TD>
																	<TD align="center" style="font-size: 11pt">Shipped</TD>
																	<TD align="center" style="font-size: 11pt">Sales
																		Rep</TD>
																	<TD align="center" style="font-size: 11pt">Invoice
																		Time</TD>
																	<TD align="center" style="font-size: 11pt">Customer</TD>
																</TR>
																<TR>
																	<TD align="center"><html:text
																			property="invoiceNumber" size="6"
																			style="font-size: 8pt;"></html:text> &nbsp; <html:submit
																			onclick="GetInvoice()" value="Get..."></html:submit>
																	</TD>
																	<TD align="center"><html:text property="orderDate"
																			size="9" style="font-size: 8pt;"></html:text></TD>
																	<TD align="center"><html:text
																			property="customerId" size="10"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="center"><html:text
																			property="shippedVia" size="8"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="center"><html:text
																			property="salesPerson" size="8"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="center"><html:text
																			property="invoiceTime" size="15"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="center"><html:text
																			property="companyName" size="21"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
															</TBODY>
														</TABLE> <BR />
													</TD>
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
					<TD align="center">
						<%
            ErrorBean errorBean = (session.getAttribute("InvoiceArchError") != null) ?
            						(ErrorBean) session.getAttribute("InvoiceArchError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

					</TD>
				</TR>
				<TR>
					<TD>
						<%
            String processDet = (session.getAttribute("InvoiceProcessDetails") != null) ?
            						(String) session.getAttribute("InvoiceProcessDetails") : "";
            session.removeAttribute("InvoiceProcessDetails");
            %> <LABEL style="border: 1px; color: red; font-size: 15pt;"><%= processDet %></LABEL>

					</TD>
				</TR>
				<TR>
					<TD>
						<TABLE cellpadding="0" cellspacing="0">
							<TBODY>
								<TR>
									<TD align="center" style="font-size: 11pt">Part No</TD>
									<TD align="center" style="font-size: 11pt">Make/Model</TD>
									<TD align="center" style="font-size: 11pt">Desc</TD>
									<TD align="center" style="font-size: 11pt">Cost</TD>
									<TD align="center" style="font-size: 11pt">List</TD>
									<TD align="center" style="font-size: 11pt">Qty</TD>
								</TR>
								<%	
	Enumeration ennum = session.getAttributeNames();
	String attrName = "";
	while (ennum.hasMoreElements()) {
		String attName = (String) ennum.nextElement();
		if (attName.indexOf("InvDet") != -1) {
			attrName = attName.trim();
			break;
		}
	}
	Object o = session.getAttribute(attrName);
	Vector v = null;
	boolean someValues = false;
	if (o == null) {
	} else {
		v = (Vector) o;
		if (v.size() != 0) {
			someValues = true;
		}
	}
	if (someValues == false) {
%>
								<TR>
									<TD align="center"><INPUT size="8" type="text"
										name="PartNo" style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="30" type="text"
										name="MakeModelName" style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="40" type="text"
										name="PartDescription" style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="10" type="text"
										name="CostPrice" style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="10" type="text"
										name="ListPrice" style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="6" type="text"
										name="Quantity" style="font-size: 8pt;"></TD>
								</TR>
								<%	
	} else {
		Enumeration enew = v.elements();
		while (enew.hasMoreElements()) {
			InvoiceDetailsForm iForm = (InvoiceDetailsForm) enew.nextElement();
%>
								<TR>
									<TD align="center"><INPUT size="8" type="text"
										name="PartNo" value="<%= iForm.getPartNo() %>"
										style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="30" type="text"
										name="MakeModelName" value="<%= iForm.getMakeModelName() %>"
										style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="40" type="text"
										name="PartDescription"
										value="<%= iForm.getPartDescription() %>"
										style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="10" type="text"
										name="CostPrice" value="<%= iForm.getCostPrice() %>"
										style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="10" type="text"
										name="ListPrice" value="<%= iForm.getListPrice() %>"
										style="font-size: 8pt;"></TD>
									<TD align="center"><INPUT size="10" type="text"
										name="Quantity" value="<%= iForm.getQuantity() %>"
										style="font-size: 8pt;"></TD>
								</TR>
								<%
		}
	}
%>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center"><BR />
						<TABLE>
							<TBODY>
								<TR>
									<TD align="center" style="font-size: 11pt">Invoice Total</TD>
									<TD align="center" style="font-size: 11pt">Applied Amount</TD>
									<TD align="center" style="font-size: 11pt">Discount</TD>
									<TD align="center" style="font-size: 11pt">Client Balance</TD>
								</TR>
								<TR>
									<TD align="center"><html:text size="15"
											property="invoiceTotal"></html:text></TD>
									<TD align="center"><html:text size="15"
											property="appliedAmount"></html:text></TD>
									<TD align="center"><html:text size="15"
											property="discount"></html:text></TD>
									<TD align="center"><html:text size="15"
											property="clientBalance"></html:text></TD>
								</TR>
							</TBODY>
						</TABLE> <BR /></TD>
				</TR>
				<TR>
					<TD align="center" height="50" valign="middle"><html:submit
							onclick="Previous()" value="<-- Prev"
							style="width: 60px; height: 25px"></html:submit> <html:submit
							onclick="PreviousForCust()" value="Prev - Cust"
							style="width: 90px; height: 25px"></html:submit> <html:submit
							onclick="PreviousForPers()" value="Prev - Pers"
							style="width: 90px; height: 25px"></html:submit> <!-- <html:submit onclick="GoToFinanceForm()" value="Finance"> </html:submit> -->
						<html:submit onclick="ReturnToMain()" value="Return To Main"
							style="width: 100px; height: 25px"></html:submit> <html:submit
							onclick="NextForPers()" value="Next - Pers"
							style="width: 90px; height: 25px"></html:submit> <html:submit
							onclick="NextForCust()" value="Next - Cust"
							style="width: 90px; height: 25px"></html:submit> <html:submit
							onclick="Next()" value="Next -->"
							style="width: 60px; height: 25px"></html:submit></TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
