<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>MakeModelMaint.jsp</TITLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetPart() {
		document.forms[document.forms.length-1].elements[0].value = "GetPart";
	}
	function ChangeInventory() {
		document.forms[document.forms.length-1].elements[0].value = "ChangeInventory";
	}
	function EnterNewSKUs() {
		document.forms[document.forms.length-1].elements[0].value = "EnterNewSKUs";
	}
	function ClearPart() {
		document.forms[document.forms.length-1].elements[0].value = "ClearPart";
	}
	function GoToAvail() {
		document.forms[document.forms.length-1].elements[0].value = "GoToAvail";
	}
	function DeletePart() {
		document.forms[document.forms.length-1].elements[0].value = "DeletePart";
	}
	function GoToHistory() {
		document.forms[document.forms.length-1].elements[0].value = "GoToHistory";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<%
	String partForMaintain = (String) session.getAttribute("PartForMaintain");
	String makeModelForMaintain = (String) session.getAttribute("MakeModelForMaintain");
	if (partForMaintain == null) partForMaintain = "";
	if (makeModelForMaintain == null) makeModelForMaintain = "";
	session.removeAttribute("PartForMaintain");
	session.removeAttribute("MakeModelForMaintain");
%>

<%
	String catSelected = "";
	Object catSel = session.getAttribute("SubCatSelected");
	if (catSel != null) {
		catSelected = (String) catSel;
	}
	Object catObject = session.getAttribute("AllSubCategories");
	Hashtable categories = null;
	if (catObject == null) {
	    categories = PartsBean.getSubCategories();
	    session.setAttribute("AllSubCategories", categories);
	} else {
	    categories = (Hashtable) catObject;
	}
%>

<BODY bgcolor="#999999">
	<html:form action="/InvenMaint.do" focus="partNo">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE width="550" border="1" align="center">
			<TBODY>
				<TR>
					<TD width="550" align="center" height="50">Inventory
						Maintenance Screen</TD>
				</TR>
				<TR>
					<TD width="550" align="center">
						<table align="center">
							<TR>
								<TD align="right" style="font-size: 11pt">Part No:</TD>
								<TD align="left">
									<%
               	    if (partForMaintain.trim().equals("")) {
               %> <html:text size="5" property="partNo">
									</html:text> <%
               	    } else {
               %> <html:text size="5" property="partNo"
										value="<%= partForMaintain %>">
									</html:text> <%
               	    }
               %>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetPart()" value="Get...">
									</html:submit>
								</TD>
							</TR>
							<TR>
								<TD align="right" style="font-size: 11pt">Interchangeable
									No:</TD>
								<TD align="left"><html:text size="30"
										property="interchangeNo" maxlength="50">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right" style="font-size: 11pt">Part Description:
								</TD>
								<TD align="left"><html:text size="60"
										property="partDescription" maxlength="50"></html:text></TD>
							</TR>
							<TR>
								<TD align="right" style="font-size: 11pt">Make/Model:</TD>
								<TD align="left" style="font-size: 11pt">
									<%
            	    if (makeModelForMaintain.trim().equals("")) {
            	%> <html:text size="30" property="makeModelName">
									</html:text> <%
            	    } else {
            	%> <html:text size="30" property="makeModelName"
										value="<%= makeModelForMaintain %>">
									</html:text> <%
            	    }
            	%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Year: &nbsp;&nbsp;<html:text
										size="10" property="year">
									</html:text>
								</TD>
							</TR>
							<!--            <TR>
            	<TD align="right" style="font-size: 10pt">
            	Year: 
            	</TD>
            	<TD align="left">
            	<html:text size="10" property="year" style="height: 18px;"> </html:text>
            	</TD>
            </TR>-->
							<TR>
								<TD align="right" style="font-size: 11pt">Units In Stock:</TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="15" property="unitsInStock">
									</html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									Units On Order: &nbsp;&nbsp; <html:text size="15"
										property="unitsOnOrder">
									</html:text></TD>
							</TR>
							<!--            <TR>
            	<TD align="right" style="font-size: 10pt">
            	Units On Order: 
            	</TD>
            	<TD align="left">
            	<html:text size="15" property="unitsOnOrder" style="height: 18px;"> </html:text>
            	</TD>
            </TR>-->
							<TR>
								<TD align="right" style="font-size: 11pt">Cost Price:</TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="15" property="costPrice">
									</html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									List Price: &nbsp;&nbsp; <html:text size="15"
										property="listPrice">
									</html:text></TD>
							</TR>
							<%
            	UserBean user = (UserBean) session.getAttribute("User");
            	if (user.getRole().trim().equalsIgnoreCase("high") || user.getUsername().trim().equalsIgnoreCase("Corrina")) {
            %>
							<TR>
								<TD align="right" style="font-size: 11pt"><B>Actual
										Price: </B></TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="15" property="actualPrice">
									</html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<B>Discount %: </B> &nbsp;&nbsp; <html:text size="15"
										property="discount">
									</html:text></TD>
							</TR>
							<%
            	}
            %>


							<!--            <TR>
            	<TD align="right" style="font-size: 10pt">
            	List Price: 
            	</TD>
            	<TD align="left">
            	<html:text size="15" property="listPrice" style="height: 18px;"> </html:text>
            	</TD>
            </TR>-->
							<TR>
								<TD align="right" style="font-size: 11pt">Supplier ID:</TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="10" property="supplierId">
									</html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									Re-Order Level: &nbsp;&nbsp; <html:text size="15"
										property="reorderLevel">
									</html:text></TD>
							</TR>
							<!--            <TR>
            	<TD align="right" style="font-size: 10pt">
            	Re-Order Level: 
            	</TD>
            	<TD align="left">
            	<html:text size="15" property="reorderLevel" style="height: 18px;"> </html:text>
            	</TD>
            </TR>-->
							
							<TR>
								<TD align="right" style="font-size: 11pt">Parts Link #:</TD>
								<TD align="left" style="font-size: 11pt"><html:text
										size="15" property="keystoneNumber">
									</html:text>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									OEM #: &nbsp;&nbsp; <html:text size="15" property="oemNumber">
									</html:text></TD>
							</TR>
							<!--            <TR>
            	<TD align="right" style="font-size: 10pt">
            	OEM #: 
            	</TD>
            	<TD align="left">
            	<html:text size="15" property="oemNumber" style="height: 18px;"> </html:text>
            	</TD>
            </TR>-->
							<TR>
								<TD align="right" style="font-size: 11pt">Location:</TD>
								<TD align="left"><html:text size="10" property="location">
									</html:text> &nbsp;&nbsp;&nbsp; Category: &nbsp; <SELECT
									name="CategorySelected">
										<%
			Hashtable reverseCat = new Hashtable();
			Enumeration ennum = categories.keys();
			while (ennum.hasMoreElements()) {
				String key = (String) ennum.nextElement();
				String val = (String) categories.get(key);
				reverseCat.put(val, key);
			}
			SortedMap sortMap = new TreeMap(reverseCat);
			Iterator iter = sortMap.keySet().iterator();
       			while (iter.hasNext()) {
       			    String sel = "";
       			    String catName = (String) iter.next();
       			    String catCd = (String) sortMap.get(catName);
       			    if (catCd.trim().equals(catSelected)) {
       			        sel = "SELECTED";
       			    }
		%>
										<OPTION value="<%= catCd %>" <%= sel %>>
											<%= catName %>
										</OPTION>
										<%
       			}
		%>
								</SELECT></TD>
							</TR>
						</table>
					</TD>
				</TR>
				<%
            String extraMsg = (String) session.getAttribute("InvenMaintExtraMessage");
            session.removeAttribute("InvenMaintExtraMessage");
            if (extraMsg == null) {
                extraMsg = "";
            }
            if (!extraMsg.trim().equals("")) {
        %>
				<TR>
					<TD align='center'>
						<TABLE>
							<TR>
								<TD><%= extraMsg %></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<% }
        %>
				<TR>
					<TD width="550" align="center" valign="middle">
						<TABLE>
							<TR>
								<TD>
									<%
            ErrorBean errorBean = (session.getAttribute("InvenMaintError") != null) ?
            						(ErrorBean) session.getAttribute("InvenMaintError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="550" align="center" height="50" valign="middle">
						<TABLE>
							<TR>
								<TD><html:submit onclick="ChangeInventory()"
										value="Change Inven" style="width: 90px;">
									</html:submit></TD>
								<TD><html:submit onclick="EnterNewSKUs()" value="Enter New"
										style="width: 80px;">
									</html:submit></TD>
								<%
				       	if (user.getRole().trim().equalsIgnoreCase("high")) {
				%>

								<TD><html:submit onclick="GoToHistory()" value="History"
										style="width: 50px;"></html:submit></TD>
								<%
					}
				%>
								<% if (user.getRole().trim().equalsIgnoreCase("High")) {
    	        	        %>
								<TD><html:submit onclick="ClearPart()" value="Clear"
										style="width: 40px;"></html:submit></TD>
								<% } else {
				%>
								<TD><html:submit onclick="ClearPart()" value="Clear"
										style="width: 100px;"></html:submit></TD>
								<% }
				%>
								<TD><html:submit onclick="GoToAvail()" value="Go To Avail"
										style="width: 80px;"></html:submit></TD>
								<TD><html:submit onclick="ReturnToMain()"
										value="Return To Main" style="width: 110px;"></html:submit></TD>
								<%
				       	if (user.getRole().trim().equalsIgnoreCase("high")) {
				%>
								<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="DeletePart()" value="Del" style="width: 25px;"></html:submit>
								</TD>
								<%
					}
				%>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
