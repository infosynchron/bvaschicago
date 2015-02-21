<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>
<%@ page import="com.bvas.beans.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Routing Menu</TITLE>
<STYLE type="text/css">
<!--
INPUT {
	height: 50px;
	width: 150px;
	vertical-align: sub
}
-->
</STYLE>
</HEAD>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
	function PartsPulled() {
		document.forms[document.forms.length-1].elements[0].value = "PartsPulled";
	}
	function PartsDelivered() {
		document.forms[document.forms.length-1].elements[0].value = "PartsDelivered";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/RoutingMenu.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" height="400" bgcolor="#999999" align="center">
			<TBODY>
				<TR>
					<TD bgcolor="#999999">
						<%	
			UserBean user = (UserBean) session.getAttribute("User");
			%>
						<TABLE>
							<TBODY>
								<TR>
									<TD width="500" colspan="2" align="center" height="60"
										style="font-size: 15pt;"><B>Routing Menu</B> <br /></TD>
								</TR>
								<TR>
									<%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high") ||
		            user.getUsername().trim().equalsIgnoreCase("Martha") ||
		            user.getUsername().trim().equalsIgnoreCase("Raj") ||
		            user.getUsername().trim().equalsIgnoreCase("Margarita") ||
		            user.getUsername().trim().equalsIgnoreCase("Martha") ||
		            user.getUsername().trim().equalsIgnoreCase("Edward") ||
		            user.getUsername().trim().equalsIgnoreCase("Saunak")) {
		        %>
									<TD align="center"><html:submit onclick="PartsPulled()"
											value="Parts Pulled"></html:submit></TD>
									<%
                        } 
                        %>
								</TR>
								<TR>
									<%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high") ||
		            user.getUsername().trim().equalsIgnoreCase("Martha") ||
		            user.getUsername().trim().equalsIgnoreCase("Margarita") ||
		            user.getUsername().trim().equalsIgnoreCase("Edward") ||
		            user.getUsername().trim().equalsIgnoreCase("Saunak")) {
		        %>
									<TD align="center"><html:submit onclick="PartsDelivered()"
											value="Parts Delivered"></html:submit></TD>
									<%
                        } 
                        %>
								</TR>
								<!-- <TR>
                        <TD align="center">
                        <%	// EVERY BODY CANN'T SEE THIS BUTTON
			if (user.getRole().trim().equalsIgnoreCase("high") ||
			 	user.getRole().trim().equalsIgnoreCase("Acct")) {
			%>
                        <html:submit onclick="EnterAmounts()" value="Enter Amounts"></html:submit>
                        <%
                        }
                        %>
                        </TD>
                        <%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high")) {
		        %>
                         <TD align="center">
                         <html:submit onclick="CloseInvoices()" value="Close Invoices"></html:submit>
                         </TD>
                        <%
                        } 
                        %>
                    </TR> -->
								<!-- <%	// EVERY BODY CANN'T SEE THIS BUTTON
		    if (user.getRole().trim().equalsIgnoreCase("high") ||
		        user.getUsername().trim().equalsIgnoreCase("Mary") ||
		        user.getUsername().trim().equalsIgnoreCase("Saunak") ||
			user.getRole().trim().equalsIgnoreCase("Acct")) {
		    %>
                    <TR>
                        <TD align="center">
                       	<html:submit onclick="AccountsReceivable()" value="Accounts Receivable"></html:submit>
                        </TD>
                        <%	// EVERY BODY CANN'T SEE THIS BUTTON
		        if (user.getRole().trim().equalsIgnoreCase("high")) {
		        %>
                         <TD align="center">
                         <html:submit onclick="SplitReceivable()" value="Ageing Receivables"></html:submit>
                         </TD>
                        <%
                        } 
                        %>
                    </TR>
                    <%
                    } 
                    %> -->
								<TR>
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("RoutingMenuError") != null) ?
            						(ErrorBean) session.getAttribute("RoutingMenuError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD colspan="2" align="center" height="50"><html:submit
											onclick="ReturnToMain()" value="Return To Main"
											style="height: 40px; width: 200px"></html:submit></TD>
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
