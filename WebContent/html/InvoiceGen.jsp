<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.formBeans.*"%>
<%@ page import="com.bvas.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>InvoiceGen.jsp</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
    out.println(serverName);
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function Apply() {
		document.forms[document.forms.length-1].elements[0].value = "Apply";
	}
	
	function NewWindow(mypage, myname, w, h, scroll) {
		window.event.srcElement.style.display = "none";
		document.all.item("MySpan5").style.display = "";
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintInvoice";
	}
	
	function PrintInvoice(prtInvNo, w, h, scroll) {
		myURL = "http://<%= serverName %>/bvaschicago/html/reports/Invoice" + prtInvNo + ".html";
		var winl = (screen.width - w) / 2;
		var wint = (screen.height - h) / 2;
		alert(myURL);
		winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
		alert("1");
		name="PrintingInvoice";
		alert("2");
		window.open(myURL, name, winprops);
		document.forms[document.forms.length-1].elements[0].value = "PrintInvoice";
	}
	function InvoiceArch() {
		window.event.srcElement.style.display = "none";
		document.all.item("MySpan4").style.display = "";
		document.forms[document.forms.length-1].elements[0].value = "InvoiceArch";
	}
	function GetMoreParts() {
		 /* var x = document.forms[document.forms.length-1]["customerId"].value;
		    if (x == null || x == "") {
		        alert("customer named cannot be empty!");
		        window.event.srcElement.style.display = "none";
				document.all.item("MySpan6").style.display = "";
				document.forms[document.forms.length-1].elements[0].value = "ClearInvoice";
		    }else{ */
		    	window.event.srcElement.style.display = "none";
				document.all.item("MySpan3").style.display = "";
				document.forms[document.forms.length-1].elements[0].value = "GetMoreParts";
		   // }
		
		
	}
	function ClearInvoice() {
		window.event.srcElement.style.display = "none";
		document.all.item("MySpan6").style.display = "";
		document.forms[document.forms.length-1].elements[0].value = "ClearInvoice";
	}
	function ChangeInvoice() {
		window.event.srcElement.style.display = "none";
		document.all.item("MySpan2").style.display = "";
		document.forms[document.forms.length-1].elements[0].value = "ChangeInvoice";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function CreateNewInvoice() {
		window.event.srcElement.style.display = "none";
		document.all.item("MySpan1").style.display = "";
		document.forms[document.forms.length-1].elements[0].value = "CreateNewInvoice";
	}
	function GetOrUpdate() {
		window.event.srcElement.style.display = "none";
		document.all.item("MySpan7").style.display = "";
		document.forms[document.forms.length-1].elements[0].value = "GetOrUpdate";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	UserBean user = (UserBean) session.getAttribute("User");
	
	int invNo = 0;
	try {
		invNo = Integer.parseInt((String) session.getAttribute("InvoiceNoForGen"));
		//session.removeAttribute("InvoiceNoForGen");
	} catch (Exception e) {
		//invNo =  InvoiceBean.getNewInvoiceNo(user.getUsername());
	}
	
	boolean invAvail = InvoiceBean.isAvailable(invNo);
	boolean isPrinted = false;
	if (invAvail) {
	    String str = InvoiceBean.getInvoice(invNo).getIsPrinted();
	    if (str.trim().equals("Y") && !user.getRole().trim().equalsIgnoreCase("Acct") && !user.getRole().trim().equalsIgnoreCase("High")) {
	        isPrinted = true;
	    } else {
	        isPrinted = false;
	    }
	}
	
	//session.setAttribute("InvoiceNoForGen", invNo+"");

	/*Hashtable ht = null;
	Object ox = application.getAttribute("AllCustomers");
	if (ox != null) {
		ht = (Hashtable) ox;
	} else {
		ht = CustomerBean.getAllCustomers();
		application.setAttribute("AllCustomers", ht);
	}*/
	
	String custIdSelected = (String) session.getAttribute("CustFromLookup");
	String custObj = (String) session.getAttribute("CustSelected");
	if (custIdSelected != null && !custIdSelected.trim().equals("")) {
		custObj = custIdSelected;
		session.removeAttribute("CustFromLookup");
	}

	String billCust = "";
	if (custObj != null && !custObj.trim().equals("")) {
		try {
			String cst = (String) custObj;
			billCust = CustomerBean.getCompanyName(cst);
		} catch (Exception e) {
		}
	}
	
	Hashtable invoiceDetailsForm = null;
	Object o = session.getAttribute("InvoiceDetails");
	if (o == null) {
		invoiceDetailsForm = new Hashtable();
		
	} else {
		invoiceDetailsForm = (Hashtable) o;
	}
	int noParts = invoiceDetailsForm.size();
	String focusVal = "PartNo";
	if (noParts != 0) {
		int xxx = noParts;
		xxx++;
		focusVal += xxx;
	} else {
		focusVal = "invoiceNumber";
	}
	boolean isPrinting = false;
	if (session.getAttribute("isPrinting") != null && session.getAttribute("isPrinting").equals("YES")) {
		isPrinting = true;
	}
	if (isPrinting) {
		focusVal="";
		session.removeAttribute("isPrinting");
	}
%>

<BODY bgcolor="#999999">
	<html:form action="/InvoiceGen.do" focus="<%= focusVal %>">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE align="center" border="1">
			<TBODY>
				<TR>
					<TD align="center">
						<TABLE cellpadding="0" cellspacing="0">
							<TBODY>
								<TR>
									<TD>Invoice Generator</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center">
						<TABLE cellpadding="0" cellspacing="0">
							<TBODY>
								<TR>
									<TD align="center">
										<TABLE cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 11pt">Invoice
														Number</TD>
													<TD align="center" style="font-size: 11pt">Order Date</TD>
													<TD align="center" style="font-size: 11pt">Customer ID</TD>
													<TD align="center" style="font-size: 11pt">Terms</TD>
													<TD align="center" style="font-size: 11pt">Sales
														Person</TD>
													<TD align="center" style="font-size: 11pt">Shipping
														Type</TD>
													<TD align="center" style="font-size: 11pt">Returned
														Invoice</TD>
												</TR>
												<TR>
													<TD align="center"><INPUT type="text"
														name="invoiceNumber" value="<%= invNo %>" size="10" /></TD>
													<TD align="center"><html:text property="orderDate"
															size="10"></html:text></TD>
													<TD align="center">
														<%
                           	if (custIdSelected != null && !custIdSelected.trim().equals("")) {
                           %> <html:text property="customerId"
															value="<%= custIdSelected %>" size="10"></html:text> <%
                           	} else {
                           %> <html:text property="customerId" size="10"></html:text>
														<%
                           	}
                           %> <%
           //                		Enumeration enum1 = ht.keys();
           //                		String custSelect = "";
           //                		String custFromSession = "";
           //                		Object custObj = session.getAttribute("CustSelected");
           //                		String billCust = "";
           //                		if (custObj != null) {
           //                			custFromSession = (String) custObj;
           //                		}
           //                		while (enum1.hasMoreElements()) {
           //                			Object obje = enum1.nextElement();
           //                			String cust = "";
           //                			if (obje != null) {
           //                				cust = (String) obje;
           //                			} else {
           //                				cust = "";
           //                			}
			//						Object ob = ht.get(cust);
			//						String custName = "";
			//						if (ob != null) {
			//							custName = (String) ob;
			//						} else {
			//							custName = "";
			//						}
           //                			if (cust.trim().equals(custFromSession.trim())) {
           //                				custSelect = "SELECTED";
           //                				billCust = custName;
           //                			} 
           //                         	<SELECT name="customerIds" style="width: 120px;">
           //                         		<OPTION VALUE="Select" SELECTED>Select</OPTION>
           //                         		<OPTION value="" 
            //                        	</SELECT>
            //               		}
                           %>
													</TD>
													<TD align="center"><html:text property="paymentTerms"
															size="5"></html:text></TD>
													<%
                                    String salesPers = "";
                                    try {
                                    	String origSalesPerson = (String) session.getAttribute("OrigSalesPerson");
                                    	if (origSalesPerson != null && !origSalesPerson.trim().equals("")) {
                                    		salesPers = origSalesPerson;
                                    	} else {
                                    		salesPers = user.getUsername();
                                    	}
                                    } catch (Exception e) {
                                    	salesPers = "";
                                    }
                                    %>
													<TD align="center"><html:text property="salesPerson"
															size="12" value="<%= salesPers.toUpperCase() %>"></html:text></TD>
													<TD align="center"><SELECT name="shippingMethods"
														style="width: 100px;">
															<OPTION VALUE="Select" SELECTED>Select</OPTION>
															<%
                          		String shipMeth = "";
                          		Object oops = session.getAttribute("ShipMethodSelected");
                          		if (oops != null) {
                          			shipMeth = (String) oops;
                          		} else {
                          			shipMeth = "";
                          		}
                          		String deliverShip = (shipMeth.trim().equals("Deliver")) ? "SELECTED" : "";
                          		String pickUpShip = (shipMeth.trim().equals("Pick-Up")) ? "SELECTED" : "";
                          		String upsShip = (shipMeth.trim().equals("UPS")) ? "SELECTED" : "";
                          		
                          			
                          %>
															<OPTION value="Deliver" <%= deliverShip %>>DELIVER</OPTION>
															<OPTION value="Pick-Up" <%= pickUpShip %>>PICK-UP</OPTION>
															<OPTION value="UPS" <%= upsShip %>>UPS</OPTION>
													</SELECT></TD>
													<TD align="center"><html:text
															property="returnedInvoice" size="10"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR align="center">
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("InvoiceGenError") != null) ?
            						(ErrorBean) session.getAttribute("InvoiceGenError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD align="center">
										<TABLE border="0" cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD style="font-size: 11pt">Bill To Address <%
                                    String woamt= (String) session.getAttribute("WriteOffAmount");
                                    double writeOffAmount = 0.0;
                                    if (woamt != null && !woamt.trim().equalsIgnoreCase("")) {
                                        try {
                                            writeOffAmount = Double.parseDouble(woamt);
                                        } catch (Exception e) {
                                            writeOffAmount = 0.0;
                                        }
                                        if (writeOffAmount > 0) {
                                    %> <blink>
															<LABEL style="border: 1px; color: red;">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Old
																Balance : <%= writeOffAmount %>
															</LABEL>
														</blink> <%
                                        }
                                    }
                                    %>
													</TD>
													<TD>
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td style="font-size: 11pt">Ship To Address</td>
																<td>
																	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																</td>
																<td><html:checkbox property="addressSame"></html:checkbox>
																</td>
																<TD style="font-size: 8pt">Check if same as Billing
																	Address</TD>
															</tr>
														</table>
													</TD>
												</TR>
												<TR>
													<TD>
														<TABLE cellpadding="0" cellspacing="0">
															<TBODY>
																<TR>
																	<TD align="right" style="font-size: 9pt">Customer:
																	</TD>
																	<TD><html:text property="billCustomerName"
																			value="<%= billCust %>" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Balance:
																	</TD>
																	<TD><html:text property="creditBalance" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">Attention:
																	</TD>
																	<TD><html:text property="billAttention" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Region:</TD>
																	<TD><html:text property="billRegion" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">Address1:
																	</TD>
																	<TD><html:text property="billAddress1" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">State:</TD>
																	<TD><html:text property="billState" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">Address2:
																	</TD>
																	<TD><html:text property="billAddress2" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Zip:</TD>
																	<TD><html:text property="billZip" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">City:</TD>
																	<TD><html:text property="billCity" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Country:
																	</TD>
																	<TD><html:text property="billCountry" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
															</TBODY>
														</TABLE>
													</TD>
													<TD>
														<TABLE cellpadding="0" cellspacing="0">
															<TBODY>
																<TR>
																	<TD align="right" style="font-size: 9pt">Customer:
																	</TD>
																	<TD><html:text property="shipCustomerName"
																			size="27" style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">Attention:
																	</TD>
																	<TD><html:text property="shipAttention" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Region:</TD>
																	<TD><html:text property="shipRegion" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">Address1:
																	</TD>
																	<TD><html:text property="shipAddress1" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">State:</TD>
																	<TD><html:text property="shipState" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">Address2:
																	</TD>
																	<TD><html:text property="shipAddress2" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Zip:</TD>
																	<TD><html:text property="shipZip" size="14"
																			style="font-size: 8pt;"></html:text></TD>
																</TR>
																<TR>
																	<TD align="right" style="font-size: 9pt">City:</TD>
																	<TD><html:text property="shipCity" size="27"
																			style="font-size: 8pt;"></html:text></TD>
																	<TD align="right" style="font-size: 9pt">Country:
																	</TD>
																	<TD><html:text property="shipCountry" size="14"
																			style="font-size: 8pt;"></html:text></TD>
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
								</TR>
								<TR>
									<TD align="center">
										<TABLE border="0.5" cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 9pt;">Part No</TD>
													<TD align="center" style="font-size: 9pt;">Make/Model</TD>
													<TD align="center" style="font-size: 9pt;">Part
														Description</TD>
													<TD align="center" style="font-size: 9pt;">Year</TD>
													<TD align="center" style="font-size: 9pt;">Stock</TD>
													<TD align="center" style="font-size: 9pt;">Cost</TD>
													<TD align="center" style="font-size: 9pt;">List</TD>
													<TD align="center" style="font-size: 9pt;">Quantity</TD>
												</TR>
												<%
                    //int noParts = invoiceDetailsForm.size();
                    SortedMap sortMap = new TreeMap(invoiceDetailsForm);
                    Iterator iter = sortMap.keySet().iterator();
                    //Enumeration enumInvDetails = invoiceDetailsForm.elements();
                      int cnt = 1;
                      if (noParts != 0)
                      {
                      	
                      	while (iter.hasNext()) {
                      		String partKey = (String) iter.next();
                      		InvoiceDetailsForm invDetails = (InvoiceDetailsForm) sortMap.get(partKey);
                      %>
												<TR>
													<TD><INPUT size="6" type="text"
														name="<%= "PartNo"+cnt %>"
														value="<%=  invDetails.getPartNo() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="19" type="text"
														name="<%= "MakeModelName"+cnt %>"
														value="<%= invDetails.getMakeModelName() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="38" type="text"
														name="<%= "PartDescription"+cnt %>"
														value="<%= invDetails.getPartDescription() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="8" type="text"
														name="<%= "Year"+cnt %>"
														value="<%= invDetails.getYear() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="7" type="text"
														name="<%= "UnitsInStock"+cnt %>"
														value="<%= invDetails.getUnitsInStock() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="8" type="text"
														name="<%= "CostPrice"+cnt %>"
														value="<%= invDetails.getCostPrice() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="8" type="text"
														name="<%= "ListPrice"+cnt %>"
														value="<%= invDetails.getListPrice() %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="6" type="text"
														name="<%= "Quantity"+cnt %>"
														value="<%= invDetails.getQuantity() %>"
														style="font-size: 8pt;"></TD>
												</TR>
												<%
                      		cnt++;
                      	}
                      }
                      %>
												<TR>
													<TD><INPUT size="6" type="text"
														name="<%= "PartNo"+cnt %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="19" type="text"
														name="<%= "MakeModelName"+cnt %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="38" type="text"
														name="<%= "PartDescription"+cnt %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="8" type="text"
														name="<%= "Year"+cnt %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="7" type="text"
														name="<%= "UnitsInStock"+cnt %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="8" type="text"
														name="<%= "CostPrice"+cnt %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="8" type="text"
														name="<%= "ListPrice"+cnt %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="6" type="text"
														name="<%= "Quantity"+cnt %>" style="font-size: 8pt;"></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR align="center">
									<TD align="center">
										<TABLE cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 9pt">Invoice
														Total</TD>
													<TD align="center" style="font-size: 9pt">Invoice Tax</TD>
													<!--	|| user.getUsername().trim().equalsIgnoreCase("Bob")  -->
													<%
                                    if ((invAvail) || (user.getRole().trim().equalsIgnoreCase("High") 
                                    	|| user.getRole().trim().equalsIgnoreCase("Acct"))) {
                                    %>
													<TD align="center" style="font-size: 9pt">Discount</TD>
													<%
                                    }
                                    %>
													<TD align="center" style="font-size: 9pt">Amount Owed</TD>

												</TR>
												<TR>
													<TD><html:text property="invoiceTotal" size="17"></html:text></TD>
													<TD><html:text property="invoiceTax" size="17"></html:text></TD>
													<%
                                    if ((invAvail) || (user.getRole().trim().equalsIgnoreCase("High") 
                                    	|| user.getRole().trim().equalsIgnoreCase("Acct"))) {
                                    %>
													<TD><html:text property="discount" size="17"></html:text></TD>
													<%
                                    }
                                    %>
													<TD><html:text property="amountVowed" size="17"></html:text></TD>
													<TD><html:submit onclick="GetOrUpdate()"
															value="Get Or Update" style="width: 100px;"></html:submit><span
														id=MySpan7 style="display: none">Wait</span></TD>
													<!-- <TD><html:submit onclick="Apply()" value="Apply"></html:submit></TD> -->
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
						<TABLE>
							<TBODY>
								<TR>


									<%
                    		if (!invAvail) {
                    	%>
									<TD><html:submit onclick="CreateNewInvoice()"
											value="New Invoice" style="width: 120px; height: 35px;"></html:submit><span
										id=MySpan1 style="display: none">Wait</span></TD>
									<%
                        	}
                        %>


									<%
                                if ((invAvail) && (user.getRole().trim().equalsIgnoreCase("High") || user.getRole().trim().equalsIgnoreCase("Acct") || user.getUsername().trim().equalsIgnoreCase("Marcie")) && (!user.getUsername().trim().equalsIgnoreCase("Mady")) && (!user.getUsername().trim().equalsIgnoreCase("Tonia"))) {
                        %>
									<TD><html:submit onclick="ChangeInvoice()" value="Modify"
											style="width: 120px; height: 35px;"></html:submit><span
										id=MySpan2 style="display: none">Wait</span></TD>
									<%
                                }
                        %>


									<TD><html:submit onclick="GetMoreParts()"
											value="Add More Parts" style="width: 100px; height: 25px;"></html:submit><span
										id=MySpan3 style="display: none">Wait</span></TD>


									<TD><html:submit onclick="InvoiceArch()" value="Archives"
											style="width: 100px; height: 25px;"></html:submit><span
										id=MySpan4 style="display: none">Wait</span></TD>


									<%
                    		if (invAvail && !isPrinted) {
                    	%>
									<TD>
										<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/Invoice"+invNo+".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; 
                        %> <html:submit onclick="<%= printMethod %>"
											value="Print" style="width: 100px; height: 25px;"></html:submit><span
										id=MySpan5 style="display: none">Wait</span>
									</TD>
									<%
                        	}
                        %>


									<TD><html:submit onclick="ClearInvoice()" value="Clear"
											style="width: 100px; height: 25px;" accesskey="C"></html:submit><span
										id=MySpan6 style="display: none">Wait</span></TD>


									<TD><html:submit onclick="ReturnToMain()"
											value="Return To Main" style="width: 120px; height: 35px;"></html:submit>
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
									<TD>Notes:&nbsp;&nbsp;<html:text property="notes"
											size="100"></html:text>
									</TD>
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
