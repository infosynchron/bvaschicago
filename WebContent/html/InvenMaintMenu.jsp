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
<TITLE>Inventory Maintenance Menu</TITLE>
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
	function MaintainInventory() {
		document.forms[document.forms.length-1].elements[0].value = "MaintainInventory";
	}
	function ShowPattern() {
		document.forms[document.forms.length-1].elements[0].value = "ShowPattern";
	}
	function DamagedParts() {
		document.forms[document.forms.length-1].elements[0].value = "DamagedParts";
	}
	function MissingParts() {
		document.forms[document.forms.length-1].elements[0].value = "MissingParts";
	}
	function PartHistory() {
		document.forms[document.forms.length-1].elements[0].value = "PartHistory";
	}
	function PartChanges() {
		document.forms[document.forms.length-1].elements[0].value = "PartChanges";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/InvenMaintMenu.do">
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
										style="font-size: 15pt;"><B>Inventory Maintenance
											Menu</B> <br /></TD>
								</TR>
								<%
                        if ( user.getRole().trim().equalsIgnoreCase("High") ||
                             user.getUsername().trim().equalsIgnoreCase("Jesse") ||
                             user.getUsername().trim().equalsIgnoreCase("Bob") ||
                            user.getUsername().trim().equalsIgnoreCase("Corrina") ||
                            user.getUsername().trim().equalsIgnoreCase("Rosie")  ||
                            user.getUsername().trim().equalsIgnoreCase("Gabby")  ||
                            user.getUsername().trim().equalsIgnoreCase("Teresa")  ||
                            user.getUsername().trim().equalsIgnoreCase("Trainee")  ||
                            user.getUsername().trim().equalsIgnoreCase("Isabel") )
                            
                        		 {
                    %>
								<TR>
									<TD align="center"><html:submit
											onclick="MaintainInventory()" value="Maintain Inventory"></html:submit>
									</TD>
								</TR>
								<%
                        }
                    %>
								<%
                        if (user.getRole().trim().equalsIgnoreCase("High") || user.getUsername().trim().equalsIgnoreCase("Gabby") || user.getUsername().trim().equalsIgnoreCase("Isabel") || user.getUsername().trim().equalsIgnoreCase("Jesse") || user.getUsername().trim().equalsIgnoreCase("Teresa") || user.getUsername().trim().equalsIgnoreCase("Rosie") || user.getUsername().trim().equalsIgnoreCase("Trainee") ||
                            user.getUsername().trim().equalsIgnoreCase("Corrina")) {
                    %>
								<TR>
									<TD align="center"><html:submit onclick="ShowPattern()"
											value="Find New Part No's"></html:submit></TD>
								</TR>
								<%
                        }
                    %>
								<%
                        if (user.getRole().trim().equalsIgnoreCase("High") ||
                            user.getUsername().trim().equalsIgnoreCase("Jesse") ||
                            user.getUsername().trim().equalsIgnoreCase("Teresa") ||
                            user.getUsername().trim().equalsIgnoreCase("Rosie") ||
                            user.getUsername().trim().equalsIgnoreCase("Gabby") ||
                            user.getUsername().trim().equalsIgnoreCase("Isabel") ||
                            user.getUsername().trim().equalsIgnoreCase("Raj") ||
                            user.getUsername().trim().equalsIgnoreCase("Trainee")) {
                    %>
								<TR>
									<TD align="center"><html:submit onclick="DamagedParts()"
											value="Damaged Parts"></html:submit></TD>
								</TR>
								<%
                        }
                    %>
								<%
                        if (user.getRole().trim().equalsIgnoreCase("High") ||
                            user.getUsername().trim().equalsIgnoreCase("Jesse") ||
                            user.getUsername().trim().equalsIgnoreCase("Teresa") ||
                            user.getUsername().trim().equalsIgnoreCase("Rosie") ||
                            user.getUsername().trim().equalsIgnoreCase("Gabby") ||
                            user.getUsername().trim().equalsIgnoreCase("Isabel") ||
                            user.getUsername().trim().equalsIgnoreCase("Trainee") ||
                            user.getUsername().trim().equalsIgnoreCase("Raj") ||
                            user.getUsername().trim().equalsIgnoreCase("Eddie")) {
                    %>
								<TR>
									<TD align="center"><html:submit onclick="MissingParts()"
											value="Missing Parts"></html:submit></TD>
								</TR>
								<%
                        }
                    %>
								<%
                        if (user.getRole().trim().equalsIgnoreCase("High") ||
                            user.getUsername().trim().equalsIgnoreCase("Jesse") ||
                            user.getUsername().trim().equalsIgnoreCase("Bob") ||
                            user.getUsername().trim().equalsIgnoreCase("Corrina") ||
                            user.getUsername().trim().equalsIgnoreCase("Naveed") ||
                            user.getUsername().trim().equalsIgnoreCase("Raj") ||
                            user.getUsername().trim().equalsIgnoreCase("Rosie") ||
                            user.getUsername().trim().equalsIgnoreCase("Gabby") ||
                            user.getUsername().trim().equalsIgnoreCase("Teresa") ||
                            user.getUsername().trim().equalsIgnoreCase("Trainee") ||
                            user.getUsername().trim().equalsIgnoreCase("Warehouse") ||
                            user.getUsername().trim().equalsIgnoreCase("Isabel")) {
                    %>
								<TR>
									<TD align="center"><html:submit onclick="PartHistory()"
											value="Parts History"></html:submit></TD>
								</TR>
								<%
                        }
                    %>
								<%
                        if (user.getRole().trim().equalsIgnoreCase("High")) {
                    %>
								<TR>
									<TD align="center"><html:submit onclick="PartChanges()"
											value="Parts Changes"></html:submit></TD>
								</TR>
								<%
                        }
                    %>
								<TR>
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("InvenMaintMenuError") != null) ?
            						(ErrorBean) session.getAttribute("InvenMaintMenuError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD colspan="2" align="center" height="50"><html:submit
											onclick="ReturnToMain()" value="Return To Main Menu"
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
