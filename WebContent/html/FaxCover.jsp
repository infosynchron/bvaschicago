<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.bvas.beans.*"%>
<%@ page import="com.bvas.utils.ErrorBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>FaxCover.jsp</TITLE>
</HEAD>
<%
	String serverName = request.getHeader("Host").trim();
%>

<!-- JavaScript Functions Start ****************** -->

<script language="JavaScript">
<!--
javascript:window.history.forward(1);
//-->
	function GetFax() {
		document.forms[document.forms.length-1].elements[0].value = "GetFax";
	}
	function PrintFax() {
		document.forms[document.forms.length-1].elements[0].value = "PrintFax";
	}
	function CreateFax() {
		document.forms[document.forms.length-1].elements[0].value = "CreateFax";
	}
	function NewWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
	document.forms[document.forms.length-1].elements[0].value = "PrintFax";
	}
	function ReturnToMain() {
		document.forms[document.forms.length-1].elements[0].value = "ReturnToMain";
	}
</script>

<!-- JavaScript Functions End   ****************** -->

<BODY bgcolor="#999999">
	<html:form action="/FaxCover.do">
		<html:hidden property="buttonClicked" value=""></html:hidden>
		<TABLE border="1" align="center">
			<TBODY>
				<TR>
					<TD>Fax Cover Sheet Creator</TD>
				</TR>
				<TR>
					<TD>
						<TABLE>
							<TBODY>
								<TR style="height: 40px;" valign="top">
									<TD>Fax No.<html:text size="10" property="faxNo"></html:text>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit onclick="GetFax()"
											value="Get..."></html:submit>
									</TD>
								</TR>
								<TR>
									<TD>
										<TABLE>
											<TBODY>
												<TR>
													<TD></TD>
													<TD align="right">To:</TD>
													<TD><html:text size="25" property="toWhom"></html:text></TD>
													<TD align="right">From:</TD>
													<TD><html:text size="25" property="fromWhom"></html:text></TD>
												</TR>
												<TR>
													<TD></TD>
													<TD align="right">Fax To:</TD>
													<TD><html:text size="25" property="faxTo"></html:text></TD>
													<TD align="right">Phone To:</TD>
													<TD><html:text size="25" property="phoneTo"></html:text></TD>
												</TR>
												<TR>
													<TD></TD>
													<TD align="right">Pages:</TD>
													<TD><html:text size="25" property="pages"></html:text></TD>
													<TD align="right">Fax Date:</TD>
													<TD><html:text size="25" property="faxDate"></html:text></TD>
												</TR>
												<TR>
													<TD></TD>
													<TD>Fax Priority:</TD>
													<%
                                    	String faxSelected = (String) session.getAttribute("faxSelected");
                                    	session.removeAttribute("faxSelected");
                                    	String val1 = "";
                                    	String val2 = "";
                                    	String val3 = "";
                                    	String val4 = "";
                                    	String val5 = "";
                                    	if (faxSelected == null) {
                                    		val2 = "SELECTED";
                                    	} else if (faxSelected.trim().equals("1")) {
                                    		val1 = "SELECTED";
                                    	} else if (faxSelected.trim().equals("2")) {
                                    		val2 = "SELECTED";
                                    	} else if (faxSelected.trim().equals("3")) {
                                    		val3 = "SELECTED";
                                    	} else if (faxSelected.trim().equals("4")) {
                                    		val4 = "SELECTED";
                                    	} else if (faxSelected.trim().equals("5")) {
                                    		val5 = "SELECTED";
                                    	} else {
                                    		val2 = "SELECTED";
                                    	}
                                    %>
													<TD><html:select property="faxPriority">
															<OPTION value="1" <%= val1 %>>Urgent</OPTION>
															<OPTION value="2" <%= val2 %>>Reply ASAP</OPTION>
															<OPTION VALUE="3" <%= val3 %>>Please Comment</OPTION>
															<OPTION VALUE="4" <%= val4 %>>Please Review</OPTION>
															<OPTION VALUE="5" <%= val5 %>>For Your
																Information</OPTION>
														</html:select></TD>
													<TD align="right">Attention:</TD>
													<TD><html:text size="25" property="attention"></html:text></TD>
												</TR>
											</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
									<TD valign="top">Fax Message: Font Size: <html:text
											property="commentsSize" size="3"></html:text><BR />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<html:textarea rows="8" cols="50" property="comments"></html:textarea></TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center">
						<TABLE>
							<TBODY>
								<TR>
									<TD align="center">
										<%
            ErrorBean errorBean = (session.getAttribute("ClientMaintError") != null) ?
            						(ErrorBean) session.getAttribute("ClientMaintError") : new ErrorBean();
            String error = (errorBean.getError() != null) ? errorBean.getError() : "";
            %> <LABEL style="border: 1px; color: red;"><%= error %></LABEL>

									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD align="center">
						<TABLE>
							<TBODY>
								<TR>
									<TD align="center"><html:submit onclick="CreateFax()"
											value="Create Fax"></html:submit> <%	
                        	String faxNum = (String) session.getAttribute("faxNum");
                        	if (faxNum == null) {
                        		faxNum = "";
                        	}
                        	String myPage = "http://" + serverName + "/bvaschicago/html/reports/Fax"+faxNum+".html";
                        	String printMethod = "NewWindow('"+myPage+"', 'Ram','400','400','yes');return true;"; %>
										<html:submit onclick="<%= printMethod %>"
											value="Print This Fax"></html:submit> <html:submit
											onclick="ReturnToMain()" value="Return To Main Menu"></html:submit>
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
