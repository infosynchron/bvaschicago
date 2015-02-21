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
	document.forms[document.forms.length-1].elements[0].value = "PrintOrderFinal";
	}
	
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function DoFinalSteps() {
		document.forms[document.forms.length-1].elements[0].value = "DoFinalSteps";
	}
	function GetOrUpdate() {
		document.forms[document.forms.length-1].elements[0].value = "GetOrUpdate";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	int ordNo = 0;
	try {
		ordNo = Integer.parseInt((String) session.getAttribute("OrderNumber"));
	} catch (Exception e) {
	}
	String ordNoStr = ordNo + "";
	Vector orderFinalExtrasForm = null;
	Object o = session.getAttribute("VendorOrderExtras");
	if (o == null) {
		orderFinalExtrasForm = new Vector();
		
	} else {
		orderFinalExtrasForm = (Vector) o;
	}
	String supId = (String) session.getAttribute("SupplierIdForOrder");
	String compName = (String) session.getAttribute("CompanyNameForOrder");
%>

<BODY bgcolor="#999999">
	<html:form action="/VendorOrderFinal.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE align="center" border="1">
			<TBODY>
				<TR>
					<TD align="center">
						<TABLE cellpadding="0" cellspacing="0">
							<TBODY>
								<TR>
									<TD>Final Steps For Order</TD>
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
													<TD align="center" style="font-size: 11pt">Order
														Number</TD>
													<TD align="center" style="font-size: 11pt">Order Date</TD>
													<TD align="center" style="font-size: 11pt">Supplier ID</TD>
													<TD align="center" style="font-size: 11pt">Supplier
														Name</TD>
													<TD align="center" style="font-size: 11pt">Order Price</TD>
													<TD align="center" style="font-size: 11pt">Total Price</TD>
												</TR>
												<TR>
													<TD align="center"><html:text property="orderNo"
															size="6" value="<%= ordNoStr %>"></html:text></TD>
													<TD align="center"><html:text property="orderDate"
															size="8"></html:text></TD>
													<TD align="center"><html:text property="supplierId"
															size="8" value="<%= supId %>"></html:text></TD>
													<TD align="center"><html:text property="supplierName"
															size="24" value="<%= compName %>"></html:text></TD>
													<TD align="center"><html:text property="orderPrice"
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
            ErrorBean errorBean = (session.getAttribute("VendorOrderFinalError") != null) ?
            						(ErrorBean) session.getAttribute("VendorOrderFinalError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD align="center">
										<TABLE cellpadding="0" cellspacing="0">
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 9pt;"><U>Reason</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Date</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Amount</U></TD>
												</TR>
												<%
                    int noParts = orderFinalExtrasForm.size();
                    Enumeration enumExtraDetails = orderFinalExtrasForm.elements();
                    if (noParts == 0)
                    {
                    %>
												<TR>
													<TD><INPUT size="30" type="text" name="Reason1"
														value=""></TD>
													<TD><INPUT size="11" type="text" name="Date1" value=""></TD>
													<TD><INPUT size="11" type="text" name="Amount1"
														value=""></TD>
												</TR>
												<%
                      } 
                      else
                      {
                      	int cnt = 1;
                      	while (enumExtraDetails.hasMoreElements()) {
                      		OrderFinalExtrasForm extraDetails = (OrderFinalExtrasForm) enumExtraDetails.nextElement();
                      %>
												<TR>
													<TD><INPUT size="30" type="text"
														name="<%= "Reason"+cnt %>"
														value="<%=  extraDetails.extraReason %>"></TD>
													<TD><INPUT size="11" type="text"
														name="<%= "Date"+cnt %>"
														value="<%= extraDetails.addedDate %>"></TD>
													<TD><INPUT size="11" type="text"
														name="<%= "Amount"+cnt %>"
														value="<%= extraDetails.extraAmount %>"></TD>
												</TR>
												<%
                      		cnt++;
                      	}
                      %>
												<TR>
													<TD><INPUT size="30" type="text"
														name="<%= "Reason"+cnt %>" value=""></TD>
													<TD><INPUT size="11" type="text"
														name="<%= "Date"+cnt %>" value=""></TD>
													<TD><INPUT size="11" type="text"
														name="<%= "Amount"+cnt %>" value=""></TD>
												</TR>
												<%
                      }
                      %>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD align="right"><html:submit onclick="GetOrUpdate()"
											value="Update Order Expenditure" style="width: 200px;"></html:submit>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center">Find Errors: &nbsp;&nbsp; <html:checkbox
							property="findErrors"></html:checkbox>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Do Inventory Update: &nbsp;&nbsp; <html:checkbox
							property="doInvenUpdate"></html:checkbox>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Calculate Prices: &nbsp;&nbsp; <html:checkbox
							property="doPriceCal"></html:checkbox>
					</TD>
				</TR>
				<TR>
					<TD align="center">
						<TABLE>
							<TBODY>
								<TR>
									<TD><html:submit onclick="DoFinalSteps()"
											value="Add These Details" style="width: 140px;"></html:submit>
									</TD>
									<TD>
										<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/VendorOrderFinal"+ordNo+".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
										<html:submit onclick="<%= printMethod %>" value="Print Order"
											style="width: 100px;"></html:submit>
									</TD>
									<TD><html:submit onclick="ReturnToMain()"
											value="Return To Main Menu" style="width: 140px;"></html:submit>
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
