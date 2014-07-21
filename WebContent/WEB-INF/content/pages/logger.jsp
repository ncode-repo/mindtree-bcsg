
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
</head>

<body>
<form action="logs.do">
	<h1 align="center">Finding Logs</h1>
	<div align="center">
	select environment &nbsp &nbsp
		<a href="envSetup.do">envSetup</a>
	</div></br></br>
	<table align="center" cellpadding="15" cellspacing="10" border="5">
		<tr>
			<td>From Date :</td>
			<td><input type="text" name="fromDate" id="fromDate"
				tabindex="1" size="30"/> <a
				href="javascript:NewCal('fromDate','ddmmmyyyy',true,12)"> <img
					src="images/cal.gif" width="16" height="16" border="0"
					alt="Pick a date">
			</a><br></td>
		</tr>
		<tr>
			<td>To Date :</td>
			<td><input type="text" name="toDate" id="toDate" tabindex="2" size="30"/>
				<a href="javascript:NewCal('toDate','ddmmmyyyy',true,12)"> <img
					src="images/cal.gif" width="16" height="16" border="0"
					alt="Pick a date">
			</a><br></td>
		</tr>
		<tr>
			<td>Word To Be Search :</td>
			<td><input type="text" name="searchByWord" tabindex="3" size="30"/> <br></td>
		</tr>
		<tr>
			<td>no.of lines to display :</td>
			<td><input type="text" name="noOfLines" tabindex="4" size="30"/> <br></td>
		</tr>
		<tr align="center">
			<td colspan="5"><input type="submit" name="submit"
				value="Find Logs" tabindex="5" /></td>
		</tr>
	</table>
	</form>
	</body>

</html>


