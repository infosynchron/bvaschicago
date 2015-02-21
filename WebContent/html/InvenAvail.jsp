<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.bvas.beans.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.utils.NumberUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>InvenAvail.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetValues() {
		document.forms[document.forms.length-1].elements[0].value = "GetValues";
	}
	function AddToInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "AddToInvoice";
	}
	function GetInterChangeable() {
		document.forms[document.forms.length-1].elements[0].value = "GetInterChangeable";
	}
	function AddToOrder() {
		document.forms[document.forms.length-1].elements[0].value = "AddToOrder";
	}
	function GoToInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "GoToInvoice";
	}
	function NewSearch() {
		document.forms[document.forms.length-1].elements[0].value = "NewSearch";
	}
	function ClientLookup() {
		document.forms[document.forms.length-1].elements[0].value = "ClientLookup";
	}
	function MaintainInventory() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainInventory";
	}
	function CheckOtherInventory() {
		document.forms[document.forms.length-1].elements[0].value = "CheckOtherInventory";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function gotoFunction() {
		document.forms[document.forms.length-1].elements[0].value = "GetValues";
		document.forms[document.forms.length-1].elements[1].click();
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	String focusVal = "SelectManufacName";
	String str = (String) session.getAttribute("FocusVal");
	session.removeAttribute("FocusVal");
	if (str != null && !str.equals("")) {
		focusVal = str;
	}
%>
<BODY bgcolor="#999999">
	<html:form action="/InvenAvail.do" focus="<%= focusVal %>">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">SKU Availability Search</TD>
				</TR>
				<TR>
					<TD align="center">
						<TABLE border="1">
							<TBODY>
								<TR>
									<TD valign="top">
										<%
                      	Hashtable manufac = null;
                      	Hashtable makeModel = null;
                      	Hashtable partDescs = null;
                      	Object o1 = session.getAttribute("InvenAvailManufac");
                      	Object o2 = session.getAttribute("InvenAvailMakeModel");
                      	Object o3 = session.getAttribute("InvenAvailPartsDescs");
                      	if (o1 != null) {
                      		manufac = (Hashtable) o1;
                      	}
                      	if (o2 != null) {
                      		makeModel = (Hashtable) o2;
                      	}
                      	if (o3 != null) {
                      		partDescs = (Hashtable) o3;
                      	}
                      	
                      	String manufacSelect = "";
                      	String makeModelSelect = "";
                      	String partDescsSelect = "";
                      	Object o4 = session.getAttribute("manufacSelected");
                      	Object o5 = session.getAttribute("makeModelSelected");
                      	Object o6 = session.getAttribute("partsDescsSelected");
                      	if (o4 != null) { manufacSelect = (String) o4; }
                      	if (o5 != null) { makeModelSelect = (String) o5; }
                      	if (o6 != null) { partDescsSelect = (String) o6; }
                      	
                      	
                      	if (manufac == null) {
                      		manufac = ManufacturerBean.getAllManufacturers();
                      		session.setAttribute("InvenAvailManufac", manufac);
                      	}
                      %>
										<TABLE border="1">
											<TBODY>
												<TR>
													<TD>
														<%
            ErrorBean errorBean = (session.getAttribute("InvenAvailError") != null) ?
            						(ErrorBean) session.getAttribute("InvenAvailError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

													</TD>
												</TR>
												<TR>
													<TD>Step 1: Select an Auto Manufacturer
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<html:submit onclick="GetValues()" value="Get..."></html:submit>
														<BR> <SELECT name="SelectManufacName"
														onChange="gotoFunction();"
														style="width: 400px; font-size: 11pt">
															<%
                       		Hashtable reverseManufac = new Hashtable();
                       		Enumeration ennum = manufac.keys();
                       		while (ennum.hasMoreElements()) {
                       			String key = (String) ennum.nextElement();
                       			String val = (String) manufac.get(key);
                       			reverseManufac.put(val, key);
                       		}
                       		SortedMap sortMap = new TreeMap(reverseManufac);
                       		Iterator iter = sortMap.keySet().iterator();
                       		while (iter.hasNext()) {
                       			String manufacVal = (String) iter.next();
                       			String manufacKey = (String) sortMap.get(manufacVal);
                       			String select1 = "";
                       			if (manufacSelect.trim().equals(manufacKey.trim())) {
                       				select1 = "SELECTED";
                       			}
                       		//Enumeration manufacEnumKeys = manufac.keys();
                       		//while (manufacEnumKeys.hasMoreElements()) {
                       		//	String manufacKey = (String) manufacEnumKeys.nextElement();
                       		//	String manufacVal = (String) manufac.get(manufacKey);
                       		//	String select1 = "";
                       		//	if (manufacSelect.trim().equals(manufacKey.trim())) {
                       		//		select1 = "SELECTED";
                       		//	}
                       %>
															<OPTION value="<%= manufacKey %>" <%= select1 %>><%= manufacVal %></OPTION>
															<%
                       		}
                       %>
													</SELECT><BR>
													</TD>
												</TR>
												<%
                       		if (makeModel != null) {
                       	%>
												<TR>
													<TD>Step 2: Select a Make Model <BR> <SELECT
														name="SelectMakeModel" onChange="gotoFunction();"
														style="width: 400px; font-size: 11pt">
															<%
                       		Hashtable reverseMakeModel = new Hashtable();
                       		Enumeration ennum2 = makeModel.keys();
                       		while (ennum2.hasMoreElements()) {
                       			String key = (String) ennum2.nextElement();
                       			String val = (String) makeModel.get(key);
                       			reverseMakeModel.put(val, key);
                       		}
                       		SortedMap sortMap2 = new TreeMap(reverseMakeModel);
                       		Iterator iter2 = sortMap2.keySet().iterator();
                       		while (iter2.hasNext()) {
                       			String makeModelVal = (String) iter2.next();
                       			String makeModelKey = (String) sortMap2.get(makeModelVal);
                       			String select2 = "";
                       			if (makeModelSelect.trim().equals(makeModelKey.trim())) {
                       				select2 = "SELECTED";
                       			}
                       		//Enumeration makeModelEnumKeys = makeModel.keys();
                       		//while (makeModelEnumKeys.hasMoreElements()) {
                       		//	String makeModelKey = (String) makeModelEnumKeys.nextElement();
                       		//	String makeModelVal = (String) makeModel.get(makeModelKey);
                       		//	String select2 = "";
                       		//	if (makeModelSelect.trim().equals(makeModelKey.trim())) {
                       		//		select2 = "SELECTED";
                       		//	}
                       %>
															<OPTION value="<%= makeModelKey %>" <%= select2 %>><%= makeModelVal %></OPTION>
															<%
                       		}
                       %>
													</SELECT><BR>
													</TD>
												</TR>
												<%
                          		}
                          %>
												<%
                       		PartsBean partNew = null;
                          	if (partDescs != null) {
                          %>
												<TR>
													<TD>Step 3: Select a Part Description <BR> <SELECT
														size="10" name="SelectPart" onChange="gotoFunction();"
														style="width: 400px; font-size: 11pt">
															<%
                       		Hashtable reversePartDescs = new Hashtable();
                       		Enumeration ennum3 = partDescs.keys();
                       		while (ennum3.hasMoreElements()) {
                       			String key = (String) ennum3.nextElement();
                       			String val = (String) partDescs.get(key)+key;
                       			reversePartDescs.put(val, key);
                       			
                       		}
                       		SortedMap sortMap3 = new TreeMap(reversePartDescs);
                       		Iterator iter3 = sortMap3.keySet().iterator();
                       		
                       		while (iter3.hasNext()) {
                       			String partDescsVal = (String) iter3.next();
                       			String partDescsKey = (String) sortMap3.get(partDescsVal);
                       			String select3 = "";
                       			if (partDescsSelect.trim().equals(partDescsKey.trim())) {
                       				if (session.getAttribute(partDescsSelect.trim()) == null) {
                       					partNew = PartsBean.getAvailPart(partDescsSelect.trim());
                       				}
                       				select3 = "SELECTED";
                       			}
                       		//Enumeration partDescsEnumKeys = partDescs.keys();
                       		//while (partDescsEnumKeys.hasMoreElements()) {
                       		//	String partDescsKey = (String) partDescsEnumKeys.nextElement();
                       		//	String partDescsVal = (String) partDescs.get(partDescsKey);
                       		//	String select3 = "";
                       		//	if (partDescsSelect.trim().equals(partDescsKey.trim())) {
                       		//		select3 = "SELECTED";
                       		//	}
                       %>
															<OPTION value="<%= partDescsKey %>" <%= select3 %>><%= partDescsVal.substring(0, partDescsVal.length()-5) %></OPTION>
															<%
                       		}
                       %>
													</SELECT><BR>
													</TD>
												</TR>
												<%
                           		}
                           %>
											</TBODY>
										</TABLE>
									</TD>
									<TD>
										<table align="center" cellpadding="0" cellspacing="0">
											<TR>
												<TD align="right" style="font-size: 9pt">Part No:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="partNo" value="<%= partNew.getPartNo() %>"
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="partNo"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
												<TD rowspan="2"><html:submit onclick="AddToInvoice()"
														value="Add To Invoice" style="width: 110px; height: 45px;"></html:submit>
												</TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">Interchange
													No:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="interchangeNo"
														value="<%= partNew.getInterchangeNo() %>"
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="interchangeNo"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<!-- <TR>
            	<TD align="right" style="font-size: 9pt">
            	Make/Model: 
            	</TD>
            	<TD align="left">
            	<% if (partNew != null) { %>
            	<html:text size="30" property="makeModelName" value="<%= MakeModelBean.getMakeModelName(partNew.getMakeModelCode()) %>" style="font-size: 9pt"> </html:text>
            	<% } else { %>
            	<html:text size="30" property="makeModelName" style="font-size: 9pt"> </html:text>
            	<% } %>
            	</TD>
            </TR> -->
											<TR>
												<TD align="right" style="font-size: 9pt">Year:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="year" value="<%= partNew.getYear() %>"
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="year"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">Units In
													Stock:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="unitsInStock"
														value='<%= partNew.getUnitsInStock() + "" %>'
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="unitsInStock"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
												<%
            	String showButton = (String) session.getAttribute("ShowInvenButton");
            	
            	if ((partNew != null && partNew.getUnitsInStock() < 1) || (showButton != null && showButton.trim().equalsIgnoreCase("True"))) {
            	%>
												<TD rowspan="2"><html:submit
														onclick="CheckOtherInventory()" value="Others Inventory"
														style="width: 110px; height: 45px;"></html:submit></TD>
												<%
            	}
            	%>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">Cost Price:</TD>
												<TD align="left">
													<% if (partNew != null) { 
			String custIdSelected = (String) session.getAttribute("CustFromLookup");
			if (custIdSelected == null) {
				custIdSelected = "";
			}
			if (!custIdSelected.trim().equals("1111111111")) {
				int lvl = CustomerBean.getCustomerLevel(custIdSelected);
				if (lvl != 0) {
		%> <html:text size="15" property="costPrice"
														value='<%= partNew.getCostPrice(lvl) + "" %>'
														style="font-size: 9pt">
													</html:text> <%
				} else {
		%> <html:text size="15" property="costPrice"
														value='<%= partNew.getCostPrice() + "" %>'
														style="font-size: 9pt">
													</html:text> <%
				}
			} else {
		%> <html:text size="15" property="costPrice"
														value='<%= partNew.getCostPrice() + "" %>'
														style="font-size: 9pt">
													</html:text> <%
			}
            	%> <% } else { %> <html:text size="15" property="costPrice"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">Walk-In Price:
												</TD>
												<TD align="left">
													<% if (partNew != null) { 
            		double walkPrice = partNew.getListPrice() * 0.8;
            		if (walkPrice != 0) {
            		    try {
            		    	walkPrice = Double.parseDouble(NumberUtils.cutFractions(walkPrice+""));
            		    } catch (Exception e) {}
            		}
            	%> <html:text size="15" property="walkinPrice"
														value='<%= walkPrice + "" %>' style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="walkinPrice"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
												<TD rowspan="2"><html:submit
														onclick="GetInterChangeable()" value="Get Inter Change"
														style="width: 110px; height: 45px;"></html:submit></TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">List Price:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="listPrice"
														value='<%= partNew.getListPrice() + "" %>'
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="listPrice"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<%
            	UserBean user = (UserBean) session.getAttribute("User");
            	if (user.getRole().trim().equalsIgnoreCase("high") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
            %>
											<TR>
												<TD align="right" style="font-size: 9pt"><B>Actual
														Price: </B></TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="actualPrice"
														value='<%= partNew.getActualPrice() + "" %>'
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="actualPrice"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<%
            	}
            	String etaDate = "";
            	if (partNew != null && partNew.getUnitsOnOrder()!=0) {
            	    etaDate = VendorOrderBean.getETADate(partNew);
            	} else {
            	    etaDate = (String) session.getAttribute("ETADate");
            	    session.removeAttribute("ETADate");
            	}
            	if (etaDate == null) etaDate = "";
            %>
											<TR>
												<TD align="right" style="font-size: 9pt">Units On
													Order:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="unitsOnOrder"
														value='<%= partNew.getUnitsOnOrder() + "" %>'
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="unitsOnOrder"
														style="font-size: 9pt">
													</html:text>
												<TD style="font-size: 9pt">&nbsp;&nbsp;<%= etaDate %>
												</TD>
												<% } %>
												</TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">Re-Order
													Level:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="reorderLevel"
														value='<%= partNew.getReorderLevel() + "" %>'
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="reorderLevel"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
												<TD rowspan="2"><html:submit onclick="AddToOrder()"
														value="Add To Order" style="width: 110px; height: 45px;"></html:submit>
												</TD>
											</TR>
											<!-- <TR>
            	<TD align="right" style="font-size: 9pt">
            	Supplier ID: 
            	</TD>
            	<TD align="left">
            	<html:text size="10" property="supplierId" style="font-size: 9pt"> </html:text>
            	</TD>
            </TR> -->
            <!--  chaange of removing compPrice from entire application -->
											<!-- TR>
												<TD align="right" style="font-size: 9pt">Comp. Price:</TD>
												<TD align="left"-->
													<%/* if (partNew != null) {*/ %> 
													<!--html:text size="15"
														property="compPrice"
														value='<%/*= partNew.getCompPrice1() + "" */%>'
														style="font-size: 9pt"-->
													<!-- /html:text--> <% /*} else {*/ %> 
													<!-- html:text size="15" property="compPrice"
														style="font-size: 9pt"-->
													<!-- /html:text--> <%/* } */%>
												<!-- /TD>
											</TR-->
											<TR>
												<TD align="right" style="font-size: 9pt">Parts Link #:
												</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="keystoneNumber"
														value="<%= partNew.getKeystoneNumber() %>"
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15"
														property="keystoneNumber" style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">OEM #:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="oemNumber" value="<%= partNew.getOemNumber() %>"
														style="font-size: 9pt">
													</html:text> <% } else { %> <html:text size="15" property="oemNumber"
														style="font-size: 9pt">
													</html:text> <% } %>
												</TD>
											</TR>
											<TR>
												<TD align="right" style="font-size: 9pt">Location:</TD>
												<TD align="left">
													<% if (partNew != null) { %> <html:text size="15"
														property="location" value="<%= partNew.getLocation() %>"
														style="font-size: 9pt"></html:text> <% } else { %> <html:text
														size="15" property="location" style="font-size: 9pt"></html:text>
													<% } %>
												</TD>
											</TR>
										</table>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<% 
           String pNo = "";
           String extraMessage = "";
           if (!partDescsSelect.trim().equals("")) {
               pNo = partDescsSelect;
               extraMessage = PartsBean.getPart(pNo, null).getExtraMessage();
           } else if (partNew != null) {
               pNo = partNew.getPartNo();
               extraMessage = partNew.getExtraMessage();
           }
           if (!extraMessage.trim().equals("")) {
        %>
				<TR>
					<TD align='center'>
						<TABLE>
							<TR>
								<TD><%= extraMessage %></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<%   }
        %>
				<TR>
					<TD align="center"><html:submit onclick="NewSearch()"
							value="New SKU Search" style="width: 140px; height: 30px;">
						</html:submit> <html:submit onclick="ClientLookup()" value="Client Lookup"
							style="width: 140px; height: 30px;">
						</html:submit> <%
            	if (!user.getUsername().trim().equalsIgnoreCase("Warehouse")) {
            %> <html:submit onclick="GoToInvoice()"
							value="Go To Invoice" style="width: 140px; height: 30px;">
						</html:submit> <%
            	}
            %> <%
            	if (user.getRole().trim().equalsIgnoreCase("medium") || user.getRole().trim().equalsIgnoreCase("high") || user.getUsername().trim().equalsIgnoreCase("Gabby") || user.getUsername().trim().equalsIgnoreCase("Rosie") || user.getUsername().trim().equalsIgnoreCase("Corrina") || user.getUsername().trim().equalsIgnoreCase("Jesse")  || user.getUsername().trim().equalsIgnoreCase("Isabel") || user.getUsername().trim().equalsIgnoreCase("Teresa")) {
            	
            %> <html:submit onclick="MaintainInventory()"
							value="Maintain Inventory" style="width: 140px; height: 30px;">
						</html:submit> <%
            	}
            %> <html:submit onclick="ReturnToMain()"
							value="Return To Main Menu" style="width: 140px; height: 30px;"></html:submit>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
