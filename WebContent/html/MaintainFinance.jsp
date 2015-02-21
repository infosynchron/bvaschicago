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
<TITLE>MaintainFinance.jsp</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetInvoice() {
		document.forms[document.forms.length-1].elements[0].value = "GetInvoice";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintFinanceNotice";
	}
	function NewSearch() {
		document.forms[document.forms.length-1].elements[0].value = "NewSearch";
	}
	function AddPayment() {
		document.forms[document.forms.length-1].elements[0].value = "AddPayment";
	}
	function PrintFinanceNotice() {
		document.forms[document.forms.length-1].elements[0].value = "PrintFinanceNotice";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
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

	String invSelected = "";
	Object invSelObj = session.getAttribute("FinanceInvoice");
	if (invSelObj != null) {
		invSelected = (String) invSelObj;
	}

	Hashtable hInv = null;
	Object oy = session.getAttribute("InvoiceForFin");
	if (oy != null) {
		hInv = (Hashtable) oy;
	} else if (invSelected != null && (!invSelected.trim().equals(""))) {
		FinMaintInv fin = new FinMaintInv();
		String custId = fin.getCustId(Integer.parseInt(invSelected));
		session.setAttribute("FinanceCustomer", custId);
		hInv = fin.getInvoices(custId);
		session.setAttribute("InvoiceForFin", hInv);
	}

	String custSelected = "";
	Object custSelObj = session.getAttribute("FinanceCustomer");
	if (custSelObj != null) {
		custSelected = (String) custSelObj;
	}
	
	InvoiceBean invBean = null;
	Object invoiceObj = session.getAttribute("InvoiceBean");
	if (invoiceObj == null && invSelected != null && (!invSelected.trim().equals(""))) {
		invBean = InvoiceBean.getInvoice(Integer.parseInt(invSelected));
		session.setAttribute("InvoiceBean", invBean);
	} else {
		invBean = (InvoiceBean) invoiceObj;
	}
	
%>
<BODY bgcolor="#999999">
	<html:form action="/MaintainFinance.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>Financial Management Panel</TD>
				</TR>
				<TR>
					<TD>
						<TABLE>
							<TR>
								<TD><SELECT NAME="customer" SIZE="5">
										<%
            				Hashtable reverseCustomer = new Hashtable();
            				Enumeration enumCust = ht.keys();
                       			while (enumCust.hasMoreElements()) {
                       				String key = (String) enumCust.nextElement();
                       				String val = (String) ht.get(key);
                       				reverseCustomer.put(val+key, key);
                       			}
            				SortedMap sortMap = new TreeMap(reverseCustomer);
                       			Iterator iter = sortMap.keySet().iterator();
            				String sel = "";
                       			while (iter.hasNext()) {
                       				String custVal = (String) iter.next();
                       				String custKey = (String) sortMap.get(custVal);
                       				custVal = custVal.substring(0, custVal.length()-10);
                       				if (custSelected.trim().equals(custKey)) {
							sel = "SELECTED";
            					}
            				//Enumeration enumCustKeys = ht.keys();
            				//String sel = "";
	            			
            				//while (enumCustKeys.hasMoreElements()) {
            				//	String custKey = (String) enumCustKeys.nextElement();
            				//	String custVal = (String) ht.get(custKey);
            				//	if (custSelected.trim().equals(custKey)) {
            				//		sel = "SELECTED";
            				//	}
            			%>
										<OPTION VALUE="<%= custKey %>" <%= sel %>><%= custVal %></OPTION>

										<%
            					sel = "";
            				}
            			%>
								</SELECT></TD>
								<TD>&gt;&gt;&gt;&gt;&gt;</TD>
								<TD><html:submit onclick="GetInvoice()" value="Get..."></html:submit>
								</TD>
								<TD>&lt;&lt;&lt;&lt;&lt;</TD>
								<TD><SELECT NAME="invoice" SIZE="5">
										<%
            			if (hInv == null) {
            			%>
										<OPTION value="">Select Customer and press Get</OPTION>
										<%
            			} else {
            				//Enumeration enumInvKeys = hInv.keys();
            				String sel1 = "";
	            			
            				SortedMap sortMap2 = new TreeMap(hInv);
                       			Iterator iter2 = sortMap2.keySet().iterator();
                       			while (iter2.hasNext()) {
            					String invKey = (String) iter2.next();
            					String invVal = (String) sortMap2.get(invKey);
            				//while (enumInvKeys.hasMoreElements()) {
            				//	String invKey = (String) enumInvKeys.nextElement();
            				//	String invVal = (String) hInv.get(invKey);
            					if (invSelected.trim().equals(invKey)) {
            						sel1 = "SELECTED";
            					}
            			%>
										<OPTION VALUE="<%= invKey %>" <%= sel1 %>><%= invVal %></OPTION>

										<%
            					sel1 = "";
            				}
            			}
            			%>
								</SELECT></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD>
						<TABLE border="1">
							<TBODY>
								<TR>
									<TD align="center">Part Number</TD>
									<TD align="center">Make/Model</TD>
									<TD align="center">PartDescription</TD>
									<TD align="center">Cost Price</TD>
									<TD align="center">Quantity</TD>
								</TR>
								<%
           if (invBean == null ) {
           %>
								<TR>
									<TD><INPUT size="8" type="text" name="PartNo"></TD>
									<TD><INPUT size="30" type="text" name="MakeModelName"></TD>
									<TD><INPUT size="30" type="text" name="PartDescription"></TD>
									<TD><INPUT size="10" type="text" name="Cost Price"></TD>
									<TD><INPUT size="10" type="text" name="Quantity"></TD>
								</TR>
								<%
           } else {
	           Vector v = invBean.getInvoiceDetails();
	           Enumeration vDet = v.elements();
	           if (v.size() == 0) {
	        %>
								<TR>
									<TD><INPUT size="8" type="text" name="PartNo"></TD>
									<TD><INPUT size="30" type="text" name="MakeModelName"></TD>
									<TD><INPUT size="30" type="text" name="PartDescription"></TD>
									<TD><INPUT size="10" type="text" name="Cost Price"></TD>
									<TD><INPUT size="10" type="text" name="Quantity"></TD>
								</TR>
								<%
	           } else {
	           while (vDet.hasMoreElements()) {
	           		
	           		InvoiceDetailsBean detBean = (InvoiceDetailsBean) vDet.nextElement();
	           		PartsBean part = PartsBean.getPart(detBean.getPartNumber(), null);
           %>
								<TR>
									<TD><INPUT size="8" type="text" name="PartNo"
										value="<%= detBean.getPartNumber() %>"></TD>
									<%
                        //This is for handling Damaged Discount Parts
                        if (!detBean.getPartNumber().startsWith("XX")) {
                        %>
									<TD><INPUT size="30" type="text" name="MakeModelName"
										value="<%= MakeModelBean.getMakeModelName(part.getMakeModelCode()) %>"></TD>
									<TD><INPUT size="30" type="text" name="PartDescription"
										value="<%= part.getPartDescription() %>"></TD>
									<%
                        } else {
                        %>
									<TD><INPUT size="30" type="text" name="MakeModelName"
										value="<%= "" %>"></TD>
									<TD><INPUT size="30" type="text" name="PartDescription"
										value="<%= "Damaged Discount" %>"></TD>
									<%
                        }
                        %>
									<TD><INPUT size="10" type="text" name="Cost Price"
										value="<%= detBean.getSoldPrice() %>"></TD>
									<TD><INPUT size="10" type="text" name="Quantity"
										value="<%= detBean.getQuantity() %>"></TD>
								</TR>
								<%
           		}
           		}
           }
           %>
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
										<TABLE>
											<TBODY>
												<TR align="center">
													<TD>Current Balance</TD>
													<TD>Applied Till Now</TD>
												</TR>
												<TR>
													<TD><html:text size="20" property="clientBalance"></html:text></TD>
													<TD><html:text size="20" property="appliedAmount"></html:text></TD>
												</TR>
												<TR valign="bottom" align="center">
													<TD>Add Payment</TD>
													<TD>Date Payment Made</TD>
												</TR>
												<TR>
													<TD><html:text size="20" property="addPayment"></html:text></TD>
													<TD><html:text size="20" property="datePaymentMade"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
									<TD>
										<TABLE>
											<TBODY>
												<TR>
													<TD>
														<%
            ErrorBean errorBean = (session.getAttribute("MaintainFinanceError") != null) ?
            						(ErrorBean) session.getAttribute("MaintainFinanceError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

													</TD>
												</TR>
											</TBODY>
										</TABLE>
										<TABLE border="1">
											<TBODY>
												<TR>
													<TD><html:submit onclick="NewSearch()"
															value="New Search" style="width: 150px;"></html:submit></TD>
													<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/FinanceNotice"+custSelected+".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
													<TD><html:submit onclick="<%= printMethod %>"
															value="Print Finance Notice" style="width: 150px;"></html:submit></TD>
												</TR>
												<TR>
													<TD><html:submit onclick="AddPayment()"
															value="Add Payment" style="width: 150px;"></html:submit></TD>
													<TD><html:submit onclick="ReturnToMain()"
															value="Return To Main Menu" style="width: 150px;"></html:submit></TD>
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
					<TD><U>All Applied Amounts Till Now</U> <BR /> <BR /> <%
        	if (invSelected!=null && !invSelected.trim().equals("") && invBean!=null) {
        		Vector hAppl = AppliedAmountBean.getAppliedAmounts(Integer.parseInt(invSelected));
        		Enumeration ennum = hAppl.elements();
        		while (ennum.hasMoreElements()) {
        			AppliedAmountBean applBean = (AppliedAmountBean) ennum.nextElement();
        %> <%= applBean.getAppliedAmount() %>&nbsp;&nbsp;&nbsp; ---
						&nbsp;&nbsp;&nbsp;<%=applBean.getAppliedDate() %> <BR /> <%
        		}
        	}
        %></TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
