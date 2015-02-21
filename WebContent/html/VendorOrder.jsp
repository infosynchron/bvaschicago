<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.formBeans.*"%>
<%@ page import="com.bvas.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.VendorOrderBean"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Order For Vendors</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintOrder";
	}
	
	function ClearOrder() {
		document.forms[document.forms.length-1].elements[0].value = "ClearOrder";
	}
	function ChangeOrder() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeOrder";
	}
	function ChangeOrderWO() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeOrderWO";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function CreateNewOrder() {
		document.forms[document.forms.length-1].elements[0].value = "CreateNewOrder";
	}
	function Get() {
		document.forms[document.forms.length-1].elements[0].value = "Get";
	}
	function GetNeeded() {
		document.forms[document.forms.length-1].elements[0].value = "GetNeeded";
	}
	function GetMatched() {
		document.forms[document.forms.length-1].elements[0].value = "GetMatched";
	}
	function GetUnMatched() {
		document.forms[document.forms.length-1].elements[0].value = "GetUnMatched";
	}
	function UpdatePrices() {
		document.forms[document.forms.length-1].elements[0].value = "UpdatePrices";
	}
	function GetOrUpdate() {
		document.forms[document.forms.length-1].elements[0].value = "GetOrUpdate";
	}
	function DoFinalSteps() {
		document.forms[document.forms.length-1].elements[0].value = "DoFinalSteps";
	}
	function changeFocusToQty(thisField) {
	    var varia1;
	    var leng = document.forms[0].elements.length;
	    for (var i=0; i<leng;i++) {
	        if (document.forms[0].elements[i].name == thisField.name) {
	            varia1 = i+7;
	        }
	    }
	    document.forms[0].elements[varia1].focus();
	}
	function changeFocusToPartNo(thisField) {
	    var varia1;
	    var leng = document.forms[0].elements.length;
	    for (var i=0; i<leng;i++) {
	        if (document.forms[0].elements[i].name == thisField.name) {
	            varia1 = i+2;
	        }
	    }
	    document.forms[0].elements[varia1].focus();
	}
	
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	int ordNo = 0;
	try {
		ordNo = Integer.parseInt((String) session.getAttribute("OrderNumber"));
	} catch (Exception e) {
		ordNo = 0;
	}

	Vector orderDetailsForm = null;
	Object o = session.getAttribute("OrderDetails");
	if (o == null) {
		orderDetailsForm = new Vector();
		
	} else {
		orderDetailsForm = (Vector) o;
	}
        int noParts = orderDetailsForm.size();
        
	String supId = (String) session.getAttribute("SupplierIdForOrder");
	String compName = (String) session.getAttribute("CompanyNameForOrder");
	String focusVal = "PartNo";
	if (noParts != 0) {
		int xxx = ++noParts;
		focusVal += xxx;
	} else {
		focusVal += "1";
	}
	boolean orderAvailable = false;
	if (ordNo == 0) {
	    orderAvailable = false;
	} else if (VendorOrderBean.isAvailable(ordNo)) {
	    orderAvailable = true;
	} else {
	    orderAvailable = false;
	}
%>

<BODY bgcolor="#999999">
	<html:form action="/VendorOrder.do" focus="<%= focusVal %>">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE align="center" border="1">
			<TBODY>
				<TR>
					<TD align="center">
						<TABLE cellpadding="0" cellspacing="0">
							<TBODY>
								<TR>
									<TD>Vendor Order Generator</TD>
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
									<TD align="center">
										<TABLE>
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 11pt" colspan="2">Order
														Number</TD>
													<TD align="center" style="font-size: 11pt">Order Date</TD>
													<TD align="center" style="font-size: 11pt">Supplier ID</TD>
													<TD align="center" style="font-size: 11pt">Supplier
														Name</TD>
													<TD align="center" style="font-size: 11pt">Total Items</TD>
													<TD align="center" style="font-size: 11pt">Total Price</TD>
												</TR>
												<TR>
													<%
                                	if (ordNo == 0) {
                                 %>
													<TD align="center"><html:text property="orderNo"
															size="6"></html:text></TD>
													<%
                                 	} else {
                                 %>
													<TD align="center"><html:text property="orderNo"
															size="6" value='<%= ordNo + "" %>'></html:text></TD>
													<%
                                 	}
                                 %>

													<TD><html:submit onclick="Get()" value="Get..."
															style="width: 35px;"></html:submit></TD>
													<TD align="center"><html:text property="orderDate"
															size="8"></html:text></TD>
													<TD align="center"><html:text property="supplierId"
															size="8" value="<%= supId %>"></html:text></TD>
													<TD align="center"><html:text property="supplierName"
															size="24" value="<%= compName %>"></html:text></TD>
													<TD align="center"><html:text property="totalItems"
															size="8"></html:text></TD>
													<TD align="center"><html:text property="orderTotal"
															size="14"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR align="center">
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("VendorOrderError") != null) ?
            						(ErrorBean) session.getAttribute("VendorOrderError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD>
										<TABLE cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 9pt;"><U>Part
															No</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Vendor
															No</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Description
															1</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Description
															2</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>St /
															Or / Re</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Price</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Min.</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Qty</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Tot
															Cost</U></TD>
												</TR>
												<%
                    Enumeration enumOrdDetails = orderDetailsForm.elements();
                    if (noParts == 0)
                    {
                    %>
												<TR>
													<TD><INPUT size="5" type="text" name="PartNo1"
														" style="font-size: 8pt;"></TD>
													<TD><INPUT size="15" type="text" name="VendorPartNo1"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="25" type="text"
														name="PartDescription1" style="font-size: 8pt;"></TD>
													<TD><INPUT size="35" type="text"
														name="PartDescription2" style="font-size: 8pt;"></TD>
													<TD><INPUT size="11" type="text" name="UnitsInStock1"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="5" type="text" name="SellingRate1"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="3" type="text" name="NoOfPieces1"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="4" type="text" name="Quantity1"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="6" type="text" name="PriceForUs1"
														style="font-size: 8pt;"></TD>
												</TR>
												<%
                      } 
                      else
                      {
                      	int cnt = 1;
                      	while (enumOrdDetails.hasMoreElements()) {
                      		OrderDetailsForm ordDetails = (OrderDetailsForm) enumOrdDetails.nextElement();
                      		String sellingRate = ordDetails.sellingRate+"";
                      		String priceForUs = ordDetails.priceForUs+"";
                      		if (sellingRate.indexOf(".") == sellingRate.length()-2) {
                      			sellingRate = sellingRate + "0";
                      		}
                      		if (priceForUs.indexOf(".") == priceForUs.length()-2) {
                      			priceForUs = priceForUs + "0";
                      		}
                      %>
												<TR>
													<TD><INPUT size="5" type="text"
														name="<%= "PartNo"+cnt %>"
														value="<%=  ordDetails.partNo %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="15" type="text"
														name="<%= "VendorPartNo"+cnt %>"
														value="<%= ordDetails.vendorPartNo %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="25" type="text"
														name="<%= "PartDescription1"+cnt %>"
														value="<%= ordDetails.partDescription1 %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="35" type="text"
														name="<%= "PartDescription2"+cnt %>"
														value="<%= ordDetails.partDescription2 %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="10" type="text"
														name="<%= "UnitsInStock"+cnt %>"
														value="<%= ordDetails.unitsInStock %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="5" type="text"
														name="<%= "SellingRate"+cnt %>" value="<%= sellingRate %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="4" type="text"
														name="<%= "NoOfPieces"+cnt %>"
														value="<%= ordDetails.noOfPieces %>"
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="4" type="text"
														name="<%= "Quantity"+cnt %>"
														value="<%= ordDetails.quantity %>" style="font-size: 8pt;"></TD>
													<TD><INPUT size="6" type="text"
														name="<%= "PriceForUs"+cnt %>" value="<%= priceForUs %>"
														style="font-size: 8pt;"></TD>
												</TR>
												<%
                      		cnt++;
                      	}
                      	int cntNew = cnt;
                      	for (int i=0; i<10; i++) {
                      %>
												<TR>
													<TD><INPUT size="5" type="text"
														name="<%= "PartNo"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="15" type="text"
														name="<%= "VendorPartNo"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="25" type="text"
														name="<%= "PartDescription1"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="35" type="text"
														name="<%= "PartDescription2"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="10" type="text"
														name="<%= "UnitsInStock"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="5" type="text"
														name="<%= "SellingRate"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="4" type="text"
														name="<%= "NoOfPieces"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="4" type="text"
														name="<%= "Quantity"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
													<TD><INPUT size="6" type="text"
														name="<%= "PriceForUs"+cntNew %>" value=""
														style="font-size: 8pt;"></TD>
												</TR>
												<%
                      	 cntNew++;
                         }
                      }
                      %>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD align="right">
										<%
                    	String xx = (String) session.getAttribute("ForAllCustomers");
                    	if (xx != null && xx.trim().equalsIgnoreCase("YES")) {
                    	%> <%	// EVERY BODY CANN'T SEE THE PRINT BUTTON
                        UserBean user = (UserBean) session.getAttribute("User");
                        if (user.getRole().trim().equalsIgnoreCase("high") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
                        %> <html:submit onclick="GetNeeded();"
											value="Get For Ordering" style="width: 200px;"></html:submit>
										<%
                    	}
                    	%> <html:submit onclick="GetMatched();"
											value="Get Matched Parts" style="width: 200px;"></html:submit>
										<html:submit onclick="GetUnMatched();"
											value="Get Un Matched Parts" style="width: 200px;"></html:submit>
										<%
                    	}
                    	%> <html:submit onclick="GetOrUpdate();"
											value="Update Order Fields" style="width: 200px;"></html:submit>
										<html:submit onclick="UpdatePrices();" value="Update Prices"
											style="width: 200px;"></html:submit>
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
									<%
                        if (!orderAvailable) {
                    %>
									<TD><html:submit onclick="CreateNewOrder();"
											value="Create New Order" style="height: 50px; width: 120px;"></html:submit>
									</TD>
									<%
                        } else {
                    %>
									<TD><html:submit onclick="ChangeOrderWO();"
											value="Change Order W/O" style="height: 50px; width: 120px;"></html:submit>
									</TD>
									<TD><html:submit onclick="ChangeOrder();"
											value="Change Order" style="height: 50px; width: 120px;"></html:submit>
									</TD>
									<%
                        }
                    %>
									<%	// EVERY BODY CANN'T SEE THE PRINT BUTTON
                        UserBean user = (UserBean) session.getAttribute("User");
                        if (user.getRole().trim().equalsIgnoreCase("high") || user.getUsername().trim().equalsIgnoreCase("Corrina") || user.getUsername().trim().equalsIgnoreCase("Gabby") || user.getUsername().trim().equalsIgnoreCase("Rosie") || user.getUsername().trim().equalsIgnoreCase("Trainee")) {
                        %>
									<TD>
										<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/VendorOrder"+ordNo+".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
										<html:submit onclick="<%= printMethod %>" value="Print"
											style="width: 100px;"></html:submit>
									</TD>
									<TD>
										<% 
                        	String myPage2 = "http://" + serverName + "/bvaschicago/html/reports/VendorOrderWOP"+ordNo+".html";
                        	String printMethod2 = "NewWindow('"+myPage2+"', 'Ram','400','400','yes');return true;"; %>
										<html:submit onclick="<%= printMethod2 %>" value="Print W/O"
											style="width: 100px;"></html:submit>
									</TD>
									<%
                        }
                        %>
									<TD><html:submit onclick="ClearOrder();" value="Clear"
											style="width: 100px;"></html:submit></TD>
									<%	// EVERY BODY CANN'T SEE THE DOFINALSTEPS BUTTON
                        
                        if (user.getRole().trim().equalsIgnoreCase("high")) {
                        %>
									<TD><html:submit onclick="DoFinalSteps();"
											value="Do Final Steps" style="width: 100px;"></html:submit></TD>
									<%
                        }
                        %>
									<TD><html:submit onclick="ReturnToMain();"
											value="Return To Main" style="width: 100px;"></html:submit></TD>
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
