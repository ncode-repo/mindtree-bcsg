<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="java.io.File" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <title>find logs </title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.9.1.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script>
  $(function() {
    $( "#fromDate" ).datepicker();
  });
  </script>
  
  <script>
  $(function() {
    $( "#toDate" ).datepicker();
  });
  </script>
</head>
<%
  String selectedFolder = request.getParameter("selectedFolder");
  if(selectedFolder == null || "Please make a selection".equals(selectedFolder))selectedFolder = "not selected";
%>
<html:form action="/logs">
<body>
<p>From String  :<input type="text" name="fromString"/></p>
<p>To String   :<input type="text" name="toString"/></p>
<%-- <p>Host		 :<html:text property="host" value="" /></p>
<p>UserName	 :<html:text property="userName" value="" /></p>
<p>Password  :<html:text property="password" value="" /></p>
<p>File To Be Saved :<INPUT NAME="file" TYPE="file"></p>  --%>

<html:submit value="getlogs"/>
</body>
</html:form>
</html>