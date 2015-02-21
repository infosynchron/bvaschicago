<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>ClientMaint.jsp</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetClient() {
		document.forms[document.forms.length-1].elements[0].value = "GetClient";
	}
	function GoToInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "GoToInvoice";
	}
	function InventAvail() {
		document.forms[document.forms.length-1].elements[0].value = "InventAvail";
	}
	function ClearClient() {
		document.forms[document.forms.length-1].elements[0].value = "ClearClient";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function gotoFunction() {
		document.forms[document.forms.length-1].elements[0].value = "GetClient";
		document.forms[document.forms.length-1].elements[2].click();
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<%
	Hashtable ht = null;
	Object ox = application.getAttribute("AllCustomers");
	if (ox != null) {
		ht = (Hashtable) ox;
	} else {
		ht = CustomerBean.getAllCustomers();
		application.setAttribute("AllCustomers", ht);
	}
        
	String custSelected = "";
	Object custSelObj = session.getAttribute("LookupCustomer");
	if (custSelObj != null) {
		custSelected = (String) custSelObj;
	}

        Hashtable reverseCustomer = new Hashtable();
        Enumeration enumCust = ht.keys();
        while (enumCust.hasMoreElements()) {
        	String key = (String) enumCust.nextElement();
        	String val = (String) ht.get(key);
        	reverseCustomer.put(val+key, key);
        }
        SortedMap sortMap = new TreeMap(reverseCustomer);
        Iterator iter = sortMap.keySet().iterator();
%>

<BODY bgcolor="#d0cccc">
	<html:form action="/ClientLookup.do" focus="SelectClientName">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD style="font-size: 20pt;" ALIGN="left"><B>Customer
							Lookup Form</B></TD>
				</TR>
				<TR>
					<TD>
						<TABLE border="1" align="center">
							<TBODY>
								<TR>
									<TD valign="top">
										<TABLE>
											<TBODY>
												<TR>
													<TD align="left">Client Id:</TD>
												</TR>
												<TR>
													<TD align="center"><html:text size="50"
															property="clientId"></html:text></TD>
												</TR>
												<TR>
													<TD align="center"><html:submit onclick="GetClient()"
															value="Get..."></html:submit></TD>
												</TR>
												<TR>
													<TD align="left">Company Name:</TD>
												</TR>
												<TR>
													<TD>
														<!-- <SELECT name="SelectClientName"  onChange="gotoFunction();" style="width: 400px; font-size: 11pt"> -->
														<SELECT name="SelectClientName"
														style="width: 400px; font-size: 11pt">
															<OPTION value="0000000000">Select The Customer</OPTION>
															<%
        				String sel = "";
        				while (iter.hasNext()) {
        					String custVal = (String) iter.next();
        					String custKey = (String) sortMap.get(custVal);
        					
        					//custVal = custVal.substring(0, custVal.length()-10);
        					custVal = custVal.substring(0, custVal.length() - custKey.length());
        					
        					if (custSelected.trim().equals(custKey)) {
							sel = "SELECTED";
        					} else {
        						sel = "";
        					}
                                    %>
															<OPTION value="<%= custKey %>" <%= sel %>>
																<%= custVal %>
															</OPTION>
															<%
                                    	}
                                    %>
													</SELECT>
													</TD>
												</TR>
										</TABLE>
									</TD>
									<TD valign="top">
										<TABLE>
											<TBODY>
												<TR>
													<TD valign="top">
														<TABLE cellpadding="0" cellspacing="0">
															<TBODY>
																<TR>
																	<TD align="right">Recent Inv. :</TD>
																	<TD><html:text size="30"
																			property="mostRecentInvoice" style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Contact Name:</TD>
																	<TD><html:text size="30" property="contactName"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Contact Title:</TD>
																	<TD><html:text size="30" property="contactTitle"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Address 1:</TD>
																	<TD><html:text size="30" property="address1"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Address 2:</TD>
																	<TD><html:text size="30" property="address2"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">City:</TD>
																	<TD><html:text size="30" property="city"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">State:</TD>
																	<TD><html:text size="30" property="state"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Postal Code:</TD>
																	<TD><html:text size="30" property="postalCode"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Region:</TD>
																	<TD><html:text size="30" property="region"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Country:</TD>
																	<TD><html:text size="30" property="country"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Phone:</TD>
																	<TD><html:text size="15" property="phone"
																			style="font-size: 7pt;"></html:text> Ext: <html:text
																			size="5" property="ext" style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Fax:</TD>
																	<TD><html:text size="15" property="fax"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Terms:</TD>
																	<TD><html:text size="15" property="terms"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right">Tax Id # :</TD>
																	<TD><html:text size="15" property="taxIdNumber"
																			style="font-size: 7pt;"></html:text></TD>
																</TR>
															</TBODY>
														</TABLE>
													</TD>
												</TR>
												<TR>
													<TD colspan="2">Notes:<BR> <html:textarea
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
            ErrorBean errorBean = (session.getAttribute("ClientLookupError") != null) ?
            						(ErrorBean) session.getAttribute("ClientLookupError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>

						<TABLE align="center">
							<TR align="center">
								<%
            	UserBean user = (UserBean) session.getAttribute("User");
            	if (!user.getUsername().trim().equalsIgnoreCase("Warehouse")) {
            %>
								<TD><html:submit onclick="GoToInvoice()"
										value="Go To Invoice" style="width: 150px;">
									</html:submit></TD>
								<%
            	}
            %>
								<TD><html:submit onclick="InventAvail()"
										value="Inventory Availability" style="width: 150px;">
									</html:submit></TD>
								<TD><html:submit onclick="ClearClient()"
										value="Clear Client Info" style="width: 150px;">
									</html:submit></TD>
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
