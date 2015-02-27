<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.UserBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>General Reports Viewer</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
	String toShowReports = (String) session.getAttribute("toShowReports");	
	UserBean user = (UserBean) session.getAttribute("User");
	session.setAttribute("BackScreen", "TodaysOrders");
	session.removeAttribute("toShowReports");
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintCOGReport";
	}
	function Back() {
		document.forms[document.forms.length-1].elements[0].value = "Back";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/ShowReports.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE align="center">
			<TBODY>
				<TR>
					<TD align="Center" colspan="4"><H1>General Reports Viewer</H1></TD>
				</TR>
				<TR>
					<TD align="Center" colspan="4"><H2>Cost Of Goods Report</H2></TD>
				</TR>
				
				<%=toShowReports %>
				
				<TR>
					<TD align="center" colspan="5" height="50">
						<%
            ErrorBean errorBean = (session.getAttribute("ShowReportsError") != null) ?
            						(ErrorBean) session.getAttribute("ShowReportsError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

					</TD>
				</TR>
				<TR>
					<% 
							String fileName=(String)session.getAttribute("fileName");
							//System.err.println(fileName);
							session.removeAttribute("fileName");
							
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/" + fileName;
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; 
                        	if (user.getRole().trim().equalsIgnoreCase("High") || (user.getUsername().trim().equalsIgnoreCase("Margarita") )) {// && fileName.startsWith("Hist"))) {
                        %>
					<TD align="center" height="30"><html:submit
							onclick="<%= printMethod %>" value="Print This Report"
							style="width: 200px;"></html:submit></TD>
					<%
            			}
            		%>
					<TD align="center"><html:submit onclick="Back()" value="Back"
							style="width: 200px;"></html:submit></TD>
					<TD align="center"><html:submit onclick="ReturnToMain()"
							value="Retutn To Main Menu" style="width: 200px;"></html:submit></TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
