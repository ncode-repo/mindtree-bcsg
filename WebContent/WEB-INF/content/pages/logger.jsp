
<%@page import="com.project.form.LoggerForm"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="javascript" type="text/javascript"
	src="js/datetimepicker.js">
	//Date Time Picker script- by TengYong Ng of http://www.rainforestnet.com
	//Script featured on JavaScript Kit (http://www.javascriptkit.com)
	//For this script, visit http://www.javascriptkit.com
</script>
<script language="JavaScript">
	function submitForLogs() {
		document.forms[0].action = '/find_logs/logs.do';
		document.forms[0].submit();
		return (true);
	}
	function getEnv() {
		alert("select environment");
		document.forms[0].action = '/find_logs/envSetup.do';
		document.forms[0].submit();
		return (true);
	}
</script>
</head>


<%
	request.getSession(true);
	String env = (String) session.getAttribute("env");
	LoggerForm form = new LoggerForm();
	form.setEnvSetUp(env);
	//out.println("form"+form.getEnvSetUp());
	//out.println("session is new "+session.isNew());
	request.getSession().removeAttribute("env");
%>
<body>
	<form name="myform">
		<h1 align="center">Finding Logs</h1>
		<div align="center">
			select environment &nbsp &nbsp <a href="envSetup.do">envSetup</a>
		</div>
		</br> </br>
		<table align="center" cellpadding="15" cellspacing="10" border="5">
			<tr>
				<td>From Date :</td>
				<td><input type="text" name="fromDate" id="fromDate"
					tabindex="1" size="30" /> <a
					href="javascript:NewCal('fromDate','ddmmmyyyy',true,12)"> <img
						src="images/cal.gif" width="16" height="16" border="0"
						alt="Pick a date">
				</a><br></td>
			</tr>
			<tr>
				<td>To Date :</td>
				<td><input type="text" name="toDate" id="toDate" tabindex="2"
					size="30" /> <a
					href="javascript:NewCal('toDate','ddmmmyyyy',true,12)"> <img
						src="images/cal.gif" width="16" height="16" border="0"
						alt="Pick a date">
				</a><br></td>
			</tr>
			<tr>
				<td>Word To Be Search :</td>
				<td><input type="text" name="searchByWord" tabindex="3"
					size="30" /> <br></td>
			</tr>
			<tr>
				<td>no.of lines to display :</td>
				<td><input type="text" name="noOfLines" tabindex="4" size="30" />
					<br></td>
			</tr>
			<%
				if (null != form.getEnvSetUp()) {
			%>
			<tr align="center">
				<td colspan="5"><input type="submit" name="submit"
					onclick="submitForLogs()" value="Find Logs" tabindex="5" /></td>
			</tr>
			<%
				} else {
			%>
			<tr align="center">
				<td colspan="5"><input type="submit" name="submit"
					onclick="getEnv()" value="Find Logs" tabindex="5" /></td>
			</tr>
			<%
				}
			%>
		</table>
	</form>
</body>

</html>


