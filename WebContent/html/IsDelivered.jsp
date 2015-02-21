<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="com.bvas.utils.Getter"%>
<%@page import="com.bvas.beans.Driver"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.InvoiceBean"%>
<%@ page import="com.bvas.beans.InvoiceDeliveryBean"%>
<%@ page import="com.bvas.beans.CustomerBean"%>
<%@ page import="com.bvas.beans.UserBean"%>
<%@ page import="com.bvas.utils.DateUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Invoices Not Delivered</TITLE>
</HEAD>
<%
	UserBean user = (UserBean) session.getAttribute("User");
	
	String serverName = request.getHeader("Host").trim();
	Object obj = session.getAttribute("AllUnDeliveredInvoices");
	Vector invoices = null;
	if (obj != null) {
	    invoices = (Vector) obj;
	}  else {
	    invoices = InvoiceBean.getInvoicesNotDelivered2();
	    if (invoices == null) {
	        response.sendRedirect("TodaysOrders.do");
	    } else {
	        session.setAttribute("AllUnDeliveredInvoices", invoices);
	    }
	}
	int noOfInvoices = invoices.size();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function ChangeIsDelivered() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeIsDelivered";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}

	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintThisReport";
	}

	function BackToOrders() {
		document.forms[document.forms.length-1].elements[0].value = "BackToOrders";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="IsDelivered.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>Invoices Not Delivered</h2>
					</TD>
				</TR>


				<TR>
					<TD align="left" width="700" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">Total No Of Pending Invoices :</TD>
								<TD align="left"><html:text size="10"
										property="pendingInvoices" value='<%= noOfInvoices + "" %>'>
									</html:text></TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<table border="1">
										<%
            			
            			Hashtable checkedInvoices = null;
            			Object oooo = session.getAttribute("AllCheckedInvoicesForDel");
            			if (oooo == null) {
            			    checkedInvoices = new Hashtable();
            			} else {
            			    checkedInvoices = (Hashtable) oooo;
            			}
            			Enumeration ennum = invoices.elements();
            			InvoiceDeliveryBean invoice = null;
            			int cnt = 0;
            			while (ennum.hasMoreElements()) {
            			    invoice = (InvoiceDeliveryBean) ennum.nextElement();
            			    cnt++;
            			    String cbName = "CB" + cnt;
            			
            			if (cnt == 1) {
            			%>
										<TR>
											<TD><B>Invoice #</B></TD>
											<TD><B>Rep</B></TD>
											<TD><B>Customer</B></TD>
											<TD><B>Date/Time</B></TD>
											<TD><B>Notes</B></TD>
											<TD><B>Route</B></TD>
											
											<% 
					    if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Margarita") || user.getUsername().trim().equalsIgnoreCase("Edward") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("Jose") || user.getUsername().trim().equalsIgnoreCase("Juan") || user.getUsername().trim().equalsIgnoreCase("Diana") || user.getUsername().trim().equalsIgnoreCase("Carlos") || user.getUsername().trim().equalsIgnoreCase("Raj")) {
					%>
											<TD><B>Check</B></TD>
											<%
            				    }
            				%>
										</TR>

										<%
				}
				String checked = "";
				checked = (String) checkedInvoices.get(invoice.getInvoicenumber() + "");
				if (checked != null && !checked.trim().equals("")) {
				    checked = "CHECKED";
				} else {
				    checked = "";
				}
				%>

										<TR>
											<TD><%= invoice.getInvoicenumber() %></TD>
											<TD><%= invoice.getSalesperson() %></TD>
											<TD><%= invoice.getCustomername() %></TD>
											<TD><%= new java.util.Date(invoice.getInvoicetime()).toString().substring(0,16) %></TD>
											<TD><%= invoice.getNotes() %></TD>
											<TD><%= invoice.getRegion() %></TD>
											
											<% 
            				    if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Margarita") || user.getUsername().trim().equalsIgnoreCase("Edward") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("Jose") || user.getUsername().trim().equalsIgnoreCase("Juan") || user.getUsername().trim().equalsIgnoreCase("Diana") || user.getUsername().trim().equalsIgnoreCase("Carlos") || user.getUsername().trim().equalsIgnoreCase("Raj")) {
            				%>
											<TD align="center"><INPUT TYPE="CHECKBOX"
												NAME="<%= cbName %>"
												VALUE="<%= invoice.getInvoicenumber() %>" <%= checked %>></TD>
											<%
            				    }
            				%>
										</TR>
										<%
            			}
            			%>
									</table>
								</TD>
							</TR>


						</TABLE>
					</TD>
				</TR>
								<TR align="center">
					<TD width="520" height="50">
						<TABLE>
							<TR align="center">
								<% 
		    if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Margarita") || user.getUsername().trim().equalsIgnoreCase("Edward") || user.getUsername().trim().equalsIgnoreCase("Marcie") || user.getUsername().trim().equalsIgnoreCase("Jose") || user.getUsername().trim().equalsIgnoreCase("Juan") || user.getUsername().trim().equalsIgnoreCase("Diana") || user.getUsername().trim().equalsIgnoreCase("Carlos") || user.getUsername().trim().equalsIgnoreCase("Raj")) {
		%>
								<TD align="center" valign="middle"><html:submit
										onclick="ChangeIsDelivered()" value="Change Delivery"
										style="height: 35px;  width: 150px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 60px"></html:submit></TD>

								<% 
                            String rtSheet = (String) session.getAttribute("ForRouteSheet");
                            if (rtSheet != null && !rtSheet.trim().equals("")) {
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/RS" + rtSheet + ".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; 
                        %>
								<TD align="center" height="30"><html:submit
										onclick="<%= printMethod %>" value="Print"
										style="height: 35px;  width: 120px"></html:submit></TD>
								<%
	            	    }
	            	%>

								<%
            	    }
            	%>
								<TD align="center" valign="middle"><html:submit
										onclick="BackToOrders()" value="Back"
										style="height: 35px; width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ReturnToMain()" value="Return To Main"
										style="height: 35px; width: 120px"></html:submit></TD>
							</TR>
							<TR colspan="5" align="center">
								<TD>Select Who Delivered <SELECT name="whoDelivered"
									style="width: 100px;">
										<!-- <OPTION value="Eddie" >Eddie</OPTION> -->

										<OPTION VALUE="Select" SELECTED>Select</OPTION>
										<%
                                                                                    Getter getter=new Getter();
                                                                                    List<Driver> list=getter.populateDrivers();    
                                                                                    for(Driver staff:list){                                                                                        
                                                                                        %>
                                                                                        <OPTION VALUE="<%= staff.getDriverName() %>"><%=staff.getDriverName() %></OPTION>
                                                                                        <% } %>
                                                                                
										<!--
										<OPTION value="Obed">Obed</OPTION>
										<OPTION value="Hubert">Hubert</OPTION>
										<OPTION value="Alejandro">Alejandro</OPTION>
										<OPTION value="Baldo">Baldo</OPTION>
										
										<OPTION value="JohnC">JohnC</OPTION>
										<OPTION value="Daniel">Daniel</OPTION>
										<OPTION value="David">David</OPTION>
										<OPTION value="Eddy">Eddy</OPTION>
										<OPTION value="J.Enrique">J.Enrique</OPTION>
										<OPTION value="Juan">Juan</OPTION>
										<OPTION value="Aguilar">Aguilar</OPTION>
										<OPTION value="Ramos">Ramos</OPTION>
										<OPTION value="Marcos">Marcos</OPTION>
										<OPTION value="Milton">Milton</OPTION>
										<OPTION value="Moises">Moises</OPTION>
										<OPTION value="Rafael">Rafael</OPTION>
										<OPTION value="Pedro.Jr.">Pedro.Jr.</OPTION>
										<OPTION value="Puma">Puma</OPTION>
										<OPTION value="Santiago">Santiago</OPTION>
										<OPTION value="Gonzalo">Gonzalo</OPTION>
										<OPTION value="Tobias">Tobias</OPTION>
										<OPTION value="Ramirez">Ramirez</OPTION>
										<OPTION value="Victorino">Victoriano</OPTION>
										<OPTION value="Eduardo">Eduardo</OPTION>
										<OPTION value="Santos">Santos</OPTION>
										<OPTION value="Patricio">Patricio</OPTION>
										<OPTION value="Cruz">Cruz</OPTION>
										<OPTION value="Rolando">Rolando</OPTION>
										<OPTION value="PU">PU</OPTION>
										<OPTION value="Cancelled">Cancelled</OPTION>
										<OPTION value="Void">Void</OPTION>
										<OPTION value="DoubleOrder">DoubleOrder</OPTION>
										<OPTION value="Others">Others</OPTION>
                                                                                -->
								</SELECT>
								</TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("IsDeliveredError") != null) ?
            						(ErrorBean) session.getAttribute("IsDeliveredError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

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
