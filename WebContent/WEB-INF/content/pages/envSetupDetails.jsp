
<%@page import="com.project.form.LoggerForm"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<%
	String envSetup = request.getParameter("envSetup");
	session.setAttribute("env", request.getParameter("envSetup"));
	request.getSession(true);
	//request.setAttribute("env", request.getParameter("envSetup"));
	//out.println("request"+request.getAttribute("env"));
%>

<body>
	<form action="welcome.do">
		<h1 align="center">Finding Logs</h1>
		<%-- <div align="center">
			selected env is
			<%=envSetup%> --%>
			<table align="center" cellpadding="15" cellspacing="2" border="5">
			<tr>
					<td>Environment Name :</td>
					<td><input type="text" name="envSetUp" tabindex="1" size="30" />
						<br></td>
				</tr>
				<tr>
					<td>Host Name :</td>
					<td><input type="text" name="hostName" tabindex="1" size="30" />
						<br></td>
				</tr>
				<tr>
					<td>Port No :</td>
					<td><input type="text" name="portNo" tabindex="1" size="30" />
						<br></td>
				</tr>
				<tr align="center">
					<td colspan="5">User Credentials</td>
				</tr>
				<tr>
					<td>User Name :</td>
					<td><input type="text" name="userName" tabindex="1" size="30" />
						<br></td>
				</tr>
				<tr>
					<td>password :</td>
					<td><input type="password" name="password" tabindex="1" size="30" />
						<br></td>
				</tr>
				<tr>
					<td>private Key File :</td>
					<td><input type="file" name="fileName"> <br></td>
				</tr>
				<tr align="center">
					<td colspan="5"><input type="submit" name="submit"
						value="Save Details" /></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>
