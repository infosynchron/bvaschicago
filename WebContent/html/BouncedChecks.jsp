<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.utils.DateUtils"%>
<%@ page import="java.io.File"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Bounced Checks Maintenance</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetTheCheck() {
		document.forms[document.forms.length-1].elements[0].value = "GetTheCheck";
	}
	function AddNew() {
		document.forms[document.forms.length-1].elements[0].value = "AddNew";
	}
	function Change() {
		document.forms[document.forms.length-1].elements[0].value = "Change";
	}
	function CreateReport() {
		document.forms[document.forms.length-1].elements[0].value = "CreateReport";
	}
	function Clear() {
		document.forms[document.forms.length-1].elements[0].value = "Clear";
	}
	function PendingChecks() {
		document.forms[document.forms.length-1].elements[0].value = "PendingChecks";
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
	function BackToAcct() {
		document.forms[document.forms.length-1].elements[0].value = "BackToAcct";
	}
</script>

<!-- JavaScript Functions End   ****************** -->
<%
	//String invoiceAvail = (String) session.getAttribute("InvoiceAvail");
	//String focusVal = "invoiceNumber";
	//if (invoiceAvail != null && !invoiceAvail.trim().equals("")) {
	//	session.removeAttribute("InvoiceAvail");
	//	focusVal = "payingAmount";
	//}
%>

<BODY bgcolor="#d0cccc">
	<html:form action="BouncedChecks.do" focus="checkId">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD align="center">
						<h2>
							<u>Bounced Checks Maintenance</u>
						</h2>
					</TD>
				</TR>
				<TR>
					<TD align="left" width="520" height="150">
						<TABLE align="center">
							<TR>
								<TD align="right">Check Id :</TD>
								<TD align="left"><html:text size="15" property="checkId">
									</html:text> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit
										onclick="GetTheCheck()" value="Get..."></html:submit></TD>
							</TR>
							<TR>
								<TD align="right">Customer Id :</TD>
								<TD align="left"><html:text size="15" property="customerId">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Company Name :</TD>
								<TD align="left"><html:text size="40"
										property="companyName">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Total Balance :</TD>
								<TD align="left"><html:text size="15"
										property="totalBalance">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Date :</TD>
								<TD align="left"><html:text size="15"
										property="enteredDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Check No :</TD>
								<TD align="left"><html:text size="15" property="checkNo">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Check Date :</TD>
								<TD align="left"><html:text size="15" property="checkDate">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Amount Bounced :</TD>
								<TD align="left"><html:text size="15"
										property="bouncedAmount">
									</html:text> &nbsp;&nbsp;&nbsp;Returns Check Fee: <html:text size="12"
										property="returnsCheckFee">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Amount Paid :</TD>
								<TD align="left"><html:text size="15" property="paidAmount">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Balance :</TD>
								<TD align="left"><html:text size="15" property="balance">
									</html:text></TD>
							</TR>
							<TR>
								<TD align="right">Is Check Cleared :</TD>
								<TD align="left"><html:checkbox property="isCleared"></html:checkbox>
								</TD>
							</TR>
							<TR>
								<TD align="center" colspan="2">
									<%
            ErrorBean errorBean = (session.getAttribute("BouncedChecksError") != null) ?
            						(ErrorBean) session.getAttribute("BouncedChecksError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="520" height="50" align="center">
						<TABLE>
							<TR align="center">
								<TD align="center" valign="middle"><html:submit
										onclick="AddNew()" value="Add New"
										style="height: 35px;  width: 75px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Clear()" value="Clear"
										style="height: 35px;  width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="Change()" value="Change"
										style="height: 35px;  width: 70px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="CreateReport()" value="Create Report"
										style="height: 35px;  width: 110px"></html:submit></TD>
								<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/SFN.html";
                        	//String myPage = "c:/Tomcat/webapps/bvaschicago/html/reports/SFN.html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;";
                        	       		                      	
                    		
                        %>
								<TD align="center" height="30"><html:submit
										onclick="<%= printMethod %>" value="Print Report"
										style="height: 35px;  width: 110px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="PendingChecks()" value="Pending"
										style="height: 35px;  width: 75px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="BackToAcct()" value="Back"
										style="height: 35px; width: 60px"></html:submit></TD>
								<TD align="center" valign="middle"><html:submit
										onclick="ReturnToMain()" value="Return"
										style="height: 35px; width: 50px"></html:submit></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
