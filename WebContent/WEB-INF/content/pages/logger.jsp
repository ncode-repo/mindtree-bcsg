<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Finding Logs</title>
</head>
<body>
<h1 align="center">Finding Logs</h1>
<!-- 	<form action="RegisterService" method="POST">
 -->		<table align="center" cellpadding="3" cellspacing="3" border="2">
			<tr>
				<td>From Date  :</td>
				<td><input type="text" name="fromDate" tabindex="1"/><br></td>
			</tr>
			<tr>
				<td>To Date   :</td>
				<td><input type="text" name="toDate" tabindex="2"/><br></td>
			</tr>
			<tr>
				<td>Word To Be Search :</td>
				<td><input type="text" name="findString" tabindex="2"/><br></td>
				</tr>
<!-- 				<td><select name="salutation" tabindex="3">
						<option value="Mr">Mr</option>
						<option value="Mrs">Mrs</option>
						<option value="Prof">Prof</option>
						<option value="Ms">Ms</option>
				</select><br></td>
			</tr>
			<tr>
				<td>First name:</td>
				<td><input type="text" name="firstName" tabindex="4"/><br></td>
			</tr>
			<tr>
				<td>Last name:</td>
				<td><input type="text" name="lastName" tabindex="5"/><br></td>
			</tr>
			<tr>
				<td>Company:</td>
				<td><input type="text" name="companyName" tabindex="6"/><br></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><input type="text" name="add_1" tabindex="7"/><br></td>
			</tr>
			<tr>
				<td>City:</td>
				<td><input type="text" name="city" tabindex="8"/><br></td>
			</tr>
			<tr>
				<td>Zip code:</td>
				<td><input type="text" name="zip_code" maxlength="7" tabindex="9"/> e.g GIR 0AA<br></td>
			</tr>
			<tr>
				<td>State:</td>
				<td><input type="text" name="state" tabindex="10"/><br></td>
			</tr>
			<tr>
				<td>Telephone:</td>
				<td><input type="text" name="telephone" maxlength="15" tabindex="11"/><br></td>
			</tr>
			<tr>
				<td>Cell phone:</td>
				<td><input type="text" name="cell" maxlength="15" tabindex="12"/><br></td>
			</tr>
			<tr>
				<td>NUMBER_OF_DAYS:</td>
				<td><input type="text" name="numberOfDays" maxlength="15" tabindex="12"/><br></td>
			</tr>
			<tr>
				<td>CAMPAIGN_TYPE:</td>
				<td><input type="text" name="campaignType" maxlength="150" tabindex="12"/><br></td>
			</tr>
			<tr>
				<td>PRODUCT_NAME:</td>
				<td><input type="text" name="productName" maxlength="150" tabindex="12"/><br></td>
			</tr>
			<tr>
				<td>RatePlan:</td>
				<td><input type="text" name="ratePlan" maxlength="15" tabindex="12"/><br></td>
			</tr>
			<tr>
				<td>Bank name:</td>
				<td><input type="text" name="bankName" /><br></td>
			</tr>
			<tr>
				<td>Bank Account name:</td>
				<td><input type="text" name="bankAccName" /><br></td>
			</tr>
			<tr>
				<td>Sort code:</td>
				<td><input type="text" name="bankRouteNum" maxlength="6" /> e.g 201111<br></td>
			</tr>
			<tr>
				<td>Account no.:</td>
				<td><input type="text" name="bankAccNum" maxlength="8"/> e.g 11111111<br></td>
			</tr>
			<tr>
				<td>Bank post code:</td>
				<td><input type="text" name="bankPostCode" maxlength="7"/> e.g GIR 0AA<br></td>
			</tr>
			 -->
			<tr align="center">
				<td colspan="5"><input type="submit" name="submit" value="Find Logs" tabindex="13"/></td>
			</tr>
		</table>
	</form>
</body>
</html>
<%-- 

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
<p>Host		 :<html:text property="host" value="" /></p>
<p>UserName	 :<html:text property="userName" value="" /></p>
<p>Password  :<html:text property="password" value="" /></p>
<p>File To Be Saved :<INPUT NAME="file" TYPE="file"></p> 

<html:submit value="getlogs"/>
</body>
</html:form>
</html> --%>