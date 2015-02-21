<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Vendor Maintanance Menu</TITLE>
<STYLE type="text/css">
<!--
INPUT {
	height: 60px;
	width: 150px;
	vertical-align: sub
}
-->
</STYLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function VendorMaint() {
		document.forms[document.forms.length-1].elements[0].value = "VendorMaint";
	}
	function VendorItem() {
		document.forms[document.forms.length-1].elements[0].value = "VendorItem";
	}
	function VendorOrder() {
		document.forms[document.forms.length-1].elements[0].value = "VendorOrder";
	}
	function VendorOurPart() {
		document.forms[document.forms.length-1].elements[0].value = "VendorOurPart";
	}
	function AdminUtilities() {
		document.forms[document.forms.length-1].elements[0].value = "AdminUtilities";
	}
	function OrderDetails() {
		document.forms[document.forms.length-1].elements[0].value = "OrderDetails";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<%
	java.util.ArrayList vendors = VendorBean.getAllVendors();
	pageContext.setAttribute("allVendorsBean", vendors);
%>

<BODY bgcolor="#999999">
	<html:form action="/VendorMenu.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" height="400" bgcolor="#999999" align="center">
			<TBODY>
				<TR>
					<TD bgcolor="#999999">
						<TABLE>
							<TBODY>
								<TR>
									<TD width="500" align="center" height="60">Vendor
										Maintainance Menu<br /> <br /> <br />
									</TD>
								</TR>
								<TR>
									<TD>
										<TABLE>
											<TBODY>
												<TR>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="VendorMaint()"
															value="Maintain Vendors"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="VendorItem()"
															value="Maintain Vendor Parts"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD>
														<%	// EVERY BODY CANN'T SEE THIS BUTTON
				    UserBean user = (UserBean) session.getAttribute("User");
				    if (user.getRole().trim().equalsIgnoreCase("high")) {
				    %> <html:submit onclick="VendorOurPart()"
															value="Order By Our Part Nos."></html:submit> <%
                                    } else {
                                    %>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%
                                    }
                                    %>
													</TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="VendorOrder()"
															value="Order For Vendors"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD>
														<%	// EVERY BODY CANN'T SEE THIS BUTTON
				    if (user.getRole().trim().equalsIgnoreCase("high") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
				    %> <html:submit onclick="AdminUtilities()"
															value="Admin Utilities"></html:submit> <%
                                    } else {
                                    %>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%
                                    }
                                    %>
													</TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD>
														<%	// EVERY BODY CANN'T SEE THIS BUTTON
				    if (user.getRole().trim().equalsIgnoreCase("high")) {
				    %> <html:submit onclick="OrderDetails()" value="Order Details"></html:submit>
														<%
                                    } else {
                                    %>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%
                                    }
                                    %>
													</TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD align="center">
										<TABLE>
											<TBODY>
												<TR>
													<TD style="width: 150px;"><html:select
															property="vendorSelect">
															<html:options collection="allVendorsBean"
																property="value" labelProperty="label" />
														</html:select></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("VendorMenuError") != null) ?
            						(ErrorBean) session.getAttribute("VendorMenuError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD align="center" height="50"><html:submit
											onclick="ReturnToMain()" value="Return To Main Menu"
											style="height: 40px; width: 200px"></html:submit></TD>
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
