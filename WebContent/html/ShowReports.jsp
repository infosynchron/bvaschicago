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
	Hashtable toShowReports = (Hashtable) session.getAttribute("toShowReports");
	String fileName = (String) toShowReports.get("FileName");
	String mainHeading = (String) toShowReports.get("MainHeading");
	Vector subHeadings = (Vector) toShowReports.get("SubHeadings");
	String [] strSubHeads = new String[subHeadings.size()];
	Vector data = (Vector) toShowReports.get("Data");
	String [][] totals = (String [][]) toShowReports.get("Totals");
	UserBean user = (UserBean) session.getAttribute("User");
	String backScreen = (String) toShowReports.get("BackScreen");
	session.setAttribute("BackScreen", backScreen);
	
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
	document.forms[document.forms.length-1].elements[0].value = "PrintThisReport";
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
					<TD align="Center" colspan="4"><H2><%= mainHeading.trim() %></H2></TD>
				</TR>
				<TR>
					<TD align="Center" colspan="4">
						<TABLE border="1">
							<TBODY>
								<TR>
									<%
                    		Enumeration enumSubHead = subHeadings.elements();
                    		int cnt = 0;
                    		while (enumSubHead.hasMoreElements()) {
                    			String subH = (String) enumSubHead.nextElement();
                    			strSubHeads[cnt] = subH;
                    			cnt++;
                    			
                    	%>
									<TD width="100"><H4><%= subH %></H4></TD>
									<%
                        	}
                        %>
								</TR>
								<%
                    	Enumeration enumData = data.elements();
                    	while (enumData.hasMoreElements()) {
                    		Hashtable innerData = (Hashtable) enumData.nextElement();
                    %>
								<TR>
									<%
                    		for (int i = 0; i < strSubHeads.length; i++) {
                    	%>
									<TD><%= (String) innerData.get(strSubHeads[i]) %></TD>
									<%
                    		}
                    	%>
								</TR>
								<%
                    	}
                    %>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="Right" colspan="4">
						<TABLE>
							<%
        	    	for (int i=0; i < totals.length; i++) {
        	    %>
							<TR>
								<TD><B><%= totals[i][0] %></B></TD>
								<TD><B><%= totals[i][1] %></B></TD>
							</TR>
							<%
        	    	}
        	    %>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center" colspan="4" height="50">
						<%
            ErrorBean errorBean = (session.getAttribute("ShowReportsError") != null) ?
            						(ErrorBean) session.getAttribute("ShowReportsError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

					</TD>
				</TR>
				<TR>
					<% 
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/" + fileName;
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; 
                        	if (user.getRole().trim().equalsIgnoreCase("High") || (user.getUsername().trim().equalsIgnoreCase("Margarita") && fileName.startsWith("Hist"))) {
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
