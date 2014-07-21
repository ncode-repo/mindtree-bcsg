
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<body>
	<form method="post" action="envDetails.do">
		<h1 align="center">Finding Logs</h1>
		<div align="center">
			<table cellspacing="70" border="5">
				<tr>
					<td><input type="radio" name="envSetup" value="MBW Prod">
						MBW Prod &nbsp &nbsp &nbsp <input type="submit" value="edit">
						<br></td>
				</tr>
				<tr>
					<td><input type="radio" name="envSetup" value="MBW Stage">
						MBW Stage &nbsp &nbsp <input type="submit" value="edit"> <br></td>
				</tr>
				<tr>
					<td><input type="radio" name="envSetup" value="IBH Prod">
						IBH Prod &nbsp &nbsp &nbsp &nbsp &nbsp <input type="submit" value="edit"> <br></td>
				</tr>
				<tr>
					<td><input type="radio" name="envSetup" value="IBH Stage">
						IBH Stage &nbsp &nbsp &nbsp &nbsp <input type="submit" value="edit"> <br></td>
				</tr>

			</table>
		</div>


	</form>
</body>
</html>
