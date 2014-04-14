<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Iterator"%>
<html>
<head>
	<title>Upload Bean</title>
	<meta content="text/html; charset=us-ascii" http-equiv="Content-Type">
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/beanUtil.js"></script>
</head>
<% String attrib = null;
	if((request.getAttribute("jsp_write")!=null)){
		attrib = (String)request.getAttribute("jsp_write");
	}
	%>
<body onload="msg('<%=attrib%>');">	
	<div id="site">
		<div class="x" id="header">
			<a class="logo" id="logo"></a>
			<div class="links-nav"></div>
		</div>
		<div>
			<logic:messagesPresent message="true">
 	 			<html:messages property="success" id="message" message="true">
    				<bean:write name="message"/><br/>
   				</html:messages>
			</logic:messagesPresent>
		</div>
		<div class="box6">
			<html:form action="/parseBean" method="post"
				enctype="multipart/form-data">
				<table>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right"><font size="2">Form</font><html:file property="javaBean" /></td>
					</tr>
					<tr>
						<td align="right" colspan="2"><html:submit
								styleClass="button1">Upload</html:submit></td>
					</tr>
				</table>
			</html:form>
		</div>
	</div>
</body>
</html>