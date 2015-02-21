<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>OtherMaint.jsp</TITLE>
<STYLE type="text/css">
<!--
INPUT {
	height: 60px;
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
	function MakeModelMaint() {
		document.forms[document.forms.length-1].elements[0].value = "MakeModelMaint";
	}
	function Category() {
		document.forms[document.forms.length-1].elements[0].value = "Category";
	}
	function SubCategory() {
		document.forms[document.forms.length-1].elements[0].value = "SubCategory";
	}
	function MaintainManufac() {
		document.forms[document.forms.length-1].elements[0].value = "ManufacMaint";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/OtherMaint.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" bgcolor="#999999" align="center">
			<TBODY>
				<TR>
					<TD bgcolor="#999999">
						<TABLE>
							<TBODY>
								<TR>
									<TD width="500" align="center">Additional Maintenence
										Features<br />
									<br />
									<br />
									</TD>
								</TR>
								<TR>
									<TD>
										<TABLE>
											<TBODY>
												<TR>
													<TD><BR /></TD>
												</TR>
												<TR>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="MaintainManufac()"
															value="Maintain Manufacturers"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="MakeModelMaint()"
															value="Maintain Make/Models"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
												</TR>
												<TR>
													<TD><BR />
													<BR /></TD>
												</TR>
												<TR>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="Category()"
															value="Categories"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD><html:submit onclick="SubCategory()"
															value="Sub Categories"></html:submit></TD>
													<TD>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("ClientMaintError") != null) ?
            						(ErrorBean) session.getAttribute("ClientMaintError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
								<TR>
									<TD align="center"><html:submit onclick="ReturnToMain()"
											value="Return To Main Menu"
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
