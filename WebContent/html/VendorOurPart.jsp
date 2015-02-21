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
	
	function ClearAll() {
		document.forms[document.forms.length-1].elements[0].value = "ClearAll";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function GoToCreateNewOrder() {
		document.forms[document.forms.length-1].elements[0].value = "GoToCreateNewOrder";
	}
	function Get() {
		document.forms[document.forms.length-1].elements[0].value = "GetOrGetSaved";
	}
	function Save() {
		document.forms[document.forms.length-1].elements[0].value = "SaveToDB";
	}
	function DoFinalSteps() {
		document.forms[document.forms.length-1].elements[0].value = "DoFinalSteps";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	long startTime = System.currentTimeMillis();
	int tmpNo = 0;
	try {
		tmpNo = Integer.parseInt((String) session.getAttribute("TempNumber"));
	} catch (Exception e) {
	}

	Vector ourPartDetailsForm = null;
	Object o = session.getAttribute("AllOurPartDetails");
	if (o == null) {
		ourPartDetailsForm = new Vector();
		
	} else {
		ourPartDetailsForm = (Vector) o;
	}
        int noParts = ourPartDetailsForm.size();
	String supId = (String) session.getAttribute("SupplierIdForOrder");
	String compName = (String) session.getAttribute("CompanyNameForOrder");
%>

<BODY bgcolor="#999999">
	<html:form action="/VendorOurPart.do">
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
													<TD align="center" style="font-size: 11pt">Temp Number</TD>
													<TD align="center" style="font-size: 11pt">Supplier ID</TD>
													<TD align="center" style="font-size: 11pt">Supplier
														Name</TD>
													<TD align="center" style="font-size: 11pt">Total Items</TD>
													<TD align="center" style="font-size: 11pt">No Ordering</TD>
												</TR>
												<TR>
													<TD align="center"><html:text property="tempNo"
															size="6"></html:text> <html:submit onclick="Get()"
															value="Get New Or Saved ..." style="width: 120px;"></html:submit>
													</TD>
													<TD align="center"><html:text property="supplierId"
															size="8" value="<%= supId %>"></html:text></TD>
													<TD align="center"><html:text property="supplierName"
															size="24" value="<%= compName %>"></html:text></TD>
													<TD align="center"><html:text property="totalItems"
															size="10"></html:text></TD>
													<TD align="center"><html:text property="noOrdering"
															size="10"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR align="center">
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("VendorOurPartError") != null) ?
            						(ErrorBean) session.getAttribute("VendorOurPartError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD>
										<%
                    Enumeration enumOurPartDetails = ourPartDetailsForm.elements();
                    if (noParts == 0)
                    {
                    %>
										<TABLE cellpadding="0" cellspacing="0" BORDER="1pt"
											align="center">
											<TBODY>
												<TR>
													<TD align="center" style="font-size: 9pt;"><U>Part
															No</U></TD>
													<!-- <TD align="center" style="font-size: 9pt;"><U>Make</U></TD>
                                    <TD align="center" style="font-size: 9pt;"><U>Model</U></TD> -->
													<TD align="center" style="font-size: 9pt;"><U>Description</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Year</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>List
															Price</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Units</U></TD>
													<TD align="center" style="font-size: 9pt;"><U>Order</U></TD>
												</TR>
												<TR>
													<TD><INPUT size="5" type="text" name="PartNo1""></TD>
													<!-- <TD><INPUT size="8" type="text" name="ManufacName1"></TD>
                                    <TD><INPUT size="20" type="text" name="MakeModelName1"></TD> -->
													<TD><INPUT size="30" type="text" name="Description1"></TD>
													<TD><INPUT size="4" type="text" name="Year1"></TD>
													<TD><INPUT size="4" type="text" name="ListPrice1"></TD>
													<TD><INPUT size="4" type="text" name="UnitsInStock1"></TD>
													<TD><INPUT size="4" type="text" name="QtyToOrder1"></TD>
												</TR>
											</TBODY>
										</TABLE> <%
                      } 
                      else
                      {
                      	int cnt = 1;
                      	String prevMake = "";
                      	String prevModel = "";
                      	while (enumOurPartDetails.hasMoreElements()) {
                      		OurPartDetailsForm ourPartDetails = (OurPartDetailsForm) enumOurPartDetails.nextElement();
                      		String unitsInStockStr = " ";
                      		if (ourPartDetails.unitsInStock != 0) {
                      			unitsInStockStr = ourPartDetails.unitsInStock + "";
                      		}
                      		String qtyToOrderStr = "";
                      		if (ourPartDetails.qtyToOrder != 0) {
                      			qtyToOrderStr = ourPartDetails.qtyToOrder + "";
                      		}
                      		String currMake = ourPartDetails.manufacName;
                      		String currModel = ourPartDetails.makeModelName;

                      		if (!currModel.trim().equals(prevModel.trim())) {
                      			if (cnt != 1) {
                      %>
									
								<TR>
									<TD COLSPAN="3"></TD>
									<TD align="right" colspan=3><html:submit onclick="Save()"
											value="Save To DB" style="width: 100px;"></html:submit></TD>
								</TR>
							</TBODY>
						</TABLE> <%
                      			}
                      %> <BR /> <BR />
						<TABLE cellpadding="0" cellspacing="0" BORDER="1pt" align="center">
							<TBODY>
								<TR>
									<TD colspan="6"><B><%= currMake %> :
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= currModel %></B>
									</TD>
								</TR>
								<TR>
									<TD align="center" style="font-size: 9pt;"><U>Part No</U></TD>
									<!-- <TD align="center" style="font-size: 9pt;"><U>Make</U></TD>
                                    <TD align="center" style="font-size: 9pt;"><U>Model</U></TD> -->
									<TD align="center" style="font-size: 9pt;"><U>Description</U></TD>
									<TD align="center" style="font-size: 9pt;"><U>Year</U></TD>
									<TD align="center" style="font-size: 9pt;"><U>List
											Price</U></TD>
									<TD align="center" style="font-size: 9pt;"><U>Units</U></TD>
									<TD align="center" style="font-size: 9pt;"><U>Order</U></TD>
								</TR>
								<%
                      		}
                      %>
								<TR>
									<INPUT TYPE="HIDDEN" NAME="<%= "PartNo"+cnt %>"
										VALUE="<%=  ourPartDetails.partNo %>" STYLE="FONT-SIZE: 9px" />
									<INPUT TYPE="HIDDEN" NAME="<%= "ManufacName"+cnt %>"
										VALUE="<%=  currMake %>" STYLE="FONT-SIZE: 9px" />
									<INPUT TYPE="HIDDEN" NAME="<%= "MakeModelName"+cnt %>"
										VALUE="<%=  currModel %>" STYLE="FONT-SIZE: 9px" />
									<INPUT TYPE="HIDDEN" NAME="<%= "Description"+cnt %>"
										VALUE="<%=  ourPartDetails.description %>"
										STYLE="FONT-SIZE: 9px" />
									<INPUT TYPE="HIDDEN" NAME="<%= "Year"+cnt %>"
										VALUE="<%=  ourPartDetails.year %>" STYLE="FONT-SIZE: 9px" />
									<INPUT TYPE="HIDDEN" NAME="<%= "ListPrice"+cnt %>"
										VALUE="<%=  ourPartDetails.listPrice %>"
										STYLE="FONT-SIZE: 9px" />
									<INPUT TYPE="HIDDEN" NAME="<%= "UnitsInStock"+cnt %>"
										VALUE="<%=  ourPartDetails.unitsInStock %>"
										STYLE="FONT-SIZE: 9px" />
									<TD align="center" width="40" style="font-size: 9pt;"><B><%= ourPartDetails.partNo %></B></TD>
									<TD align="center" width="240" style="font-size: 9pt;"><%= ourPartDetails.description %></TD>
									<TD align="center" width="30" style="font-size: 9pt;"><%= ourPartDetails.year %></TD>
									<TD align="center" width="60" style="font-size: 9pt;"><%= ourPartDetails.listPrice %></TD>
									<TD align="center" width="20" style="font-size: 9pt;"><%= unitsInStockStr %></TD>
									<TD width="20"><INPUT size="4" type="text"
										name="<%= "QtyToOrder"+cnt %>" value="<%= qtyToOrderStr %>"
										STYLE="FONT-SIZE: 9px"></TD>
								</TR>
								<%
                      		cnt++;
                      		prevModel = currModel;
                      	}
                      }
                      %>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="right"><html:submit onclick="Save()"
							value="Save To DB" style="width: 200px;"></html:submit></TD>
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
							<TD><html:submit onclick="GoToCreateNewOrder()"
									value="Go To Create The Order" style="width: 140px;"></html:submit>
							</TD>
							<TD><html:submit onclick="ClearAll()" value="Clear All"
									style="width: 100px;"></html:submit></TD>
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
		<%
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		double inSeconds = elapsedTime / 60;
		System.out.println("Time Taken for getting all the Parts from DB: " + elapsedTime + " Millis and " + inSeconds + " Seconds");
	%>
	</html:form>
</BODY>
</html:html>
