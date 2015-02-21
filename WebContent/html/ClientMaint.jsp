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
<TITLE>ClientMaint.jsp</TITLE>
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
	function GetClient() {
		document.forms[document.forms.length-1].elements[0].value = "GetClient";
	}
	function AddNewClient() {
		document.forms[document.forms.length-1].elements[0].value = "AddNewClient";
	}
	function ChangeClient() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeClient";
	}
	function DeleteClient() {
		document.forms[document.forms.length-1].elements[0].value = "DeleteClient";
	}
	function ClearClient() {
		document.forms[document.forms.length-1].elements[0].value = "ClearClient";
	}
	function PrintClientListing() {
		document.forms[document.forms.length-1].elements[0].value = "PrintClientListing";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintClientListing";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/ClientMaint.do" focus="clientId">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>Client Administration Form (CAF)</TD>
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
													<TD align="right">Client Id:</TD>
													<TD><html:text size="10" property="clientId"></html:text>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
															onclick="GetClient()" value="Get..."></html:submit></TD>
												</TR>
												<TR>
													<TD align="right">Company Name:</TD>
													<TD><html:text size="30" property="companyName"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Address 1:</TD>
													<TD><html:text size="30" property="address1"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Address 2:</TD>
													<TD><html:text size="30" property="address2"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">City:</TD>
													<TD><html:text size="30" property="city"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">State:</TD>
													<TD><html:text size="30" property="state"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Postal Code:</TD>
													<TD><html:text size="30" property="postalCode"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Region:</TD>
													<TD><html:text size="30" property="region"></html:text></TD>
												</TR>
												<TR>
													<TD align="right">Country:</TD>
													<TD><html:text size="30" property="country"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
									<TD valign="top">
										<TABLE>
											<TBODY>
												<TR>
													<TD valign="top">
														<TABLE>
															<TBODY>
																<TR>
																	<TD align="right">Contact Name:</TD>
																	<TD><html:text size="30" property="contactName"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Contact Title:</TD>
																	<TD><html:text size="30" property="contactTitle"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Phone:</TD>
																	<TD><html:text size="15" property="phone"></html:text>
																		Ext: <html:text size="5" property="ext"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Fax:</TD>
																	<TD><html:text size="15" property="fax"></html:text></TD>
																</TR>
																<%
                                            	if (user.getRole().trim().equalsIgnoreCase("High")) {
                                            %>
																<TR>
																	<TD align="right">Balance:</TD>
																	<TD><html:text size="15" property="creditBalance"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Cr. Limit:</TD>
																	<TD><html:text size="15" property="creditLimit"></html:text>
																		Lvl: <html:text size="5" property="customerLevel"></html:text></TD>
																</TR>
																<%
                                            	}
                                            %>
																<TR>
																	<TD align="right">Tax Id:</TD>
																	<TD><html:checkbox property="taxId"></html:checkbox>
																		Tax Id # : <html:text size="15" property="taxIdNumber"></html:text></TD>
																</TR>
															</TBODY>
														</TABLE>
													</TD>
												</TR>
												<TR>
													<TD colspan="2"><U>Terms:</U> Cash:<html:radio
															property="paymentTerms" value='O'></html:radio>&nbsp;
														COD:<html:radio property="paymentTerms" value='C'></html:radio>&nbsp;
														Bi Wk:<html:radio property="paymentTerms" value='B'></html:radio>&nbsp;
														Wk:<html:radio property="paymentTerms" value='W'></html:radio>&nbsp;
														Mth:<html:radio property="paymentTerms" value='M'></html:radio>&nbsp;
													</TD>
												</TR>
												<TR>
													<TD colspan="2" valign="top">Notes: <html:textarea
															property="notes" cols="30" rows="2"></html:textarea>
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
					<TD>

						<TABLE align="center">
							<TR align="center">
								<TD>
									<%
            ErrorBean errorBean = (session.getAttribute("ClientMaintError") != null) ?
            						(ErrorBean) session.getAttribute("ClientMaintError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>

						<TABLE align="center">
							<TR align="center">
								<TD><html:submit onclick="AddNewClient()"
										value="Add a New Client" style="width: 150px;">
									</html:submit></TD>
								<TD><html:submit onclick="ChangeClient()"
										value="Change Client Info." style="width: 150px;">
									</html:submit></TD>
								<%
			if (user.getRole().trim().equalsIgnoreCase("High")) {
			%>
								<TD><html:submit onclick="DeleteClient()"
										value="Delete This Client" style="width: 150px;">
									</html:submit></TD>
								<%
			}
			%>
							</TR>
						</TABLE>
						<TABLE align="center">
							<TR align="center">
								<TD><html:submit onclick="ClearClient()"
										value="Clear Client Info" style="width: 150px;">
									</html:submit></TD>
								<%
			if (user.getRole().trim().equalsIgnoreCase("High")) {
			%>
								<TD>
									<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/ClientListing.html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
									<html:submit onclick="<%= printMethod %>"
										value="Print Client Listing" style="width: 150px;">
									</html:submit>
								</TD>
								<%
			}
			%>
								<TD><html:submit onclick="ReturnToMain()"
										value="Return To Main Menu" style="width: 150px;"></html:submit>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
