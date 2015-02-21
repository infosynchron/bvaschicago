<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Welcome to Best Value Auto Body System</TITLE>
</HEAD>
<BODY bgcolor="#999999">
	<html:form action="/Login.do" focus="username">
		<TABLE align="center">
			<TBODY>
				<TR>
					<TD colspan="2"><IMG
						src="/bvaschicago/images/BestValueChicagoLogo.jpg" border="0"
						alt="WELCOME TO BVAS SYSTEM" height="390" width="780"></TD>
				</TR>
				<TR>
					<TD colspan="2" align="center"
						style="font-size: 24pt; color: white"><B>BEST VALUE
							SYSTEM - CHICAGO</B></TD>
				</TR>
				<TR>
					<TD><BR /> <BR /> <BR /></TD>
				</TR>
				<TR>
					<TD align="right" style="font-size: 18pt; color: white;"><B>Login
							Name :&nbsp;&nbsp;&nbsp;</B></TD>
					<TD><html:text property="username" size="20" /></TD>
				</TR>
				<TR>
					<TD style="font-size: 18pt; color: white;" align="right"><B>Password
							:&nbsp;&nbsp;&nbsp;</B></TD>
					<TD><html:password property="password" size="20" /></TD>
				</TR>
				<TR>
					<TD><BR /></TD>
				</TR>
				<TR>
					<TD align="right"><html:submit property="Submit"
							value="Submit" style="width: 120px;" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
					<TD><html:reset style="width: 120px;" /></TD>
				</TR>
				<TR>
					<TD colspan="2">
						<%
            ErrorBean errorBean = (session.getAttribute("LoginError") != null) ?
            						(ErrorBean) session.getAttribute("LoginError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>
					</TD>
				</TR>

			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>