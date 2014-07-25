
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
	function chkradio() {
		var elem = document.forms['radioform'].elements['envSetup'];
		len = elem.length - 1;
		chkvalue = '';
		for (i = 0; i <= len; i++) {
			if (elem[i].checked)
				chkvalue = elem[i].value;
		}
		/* if (chkvalue == '') {
			alert('No button checked.');
			//document.forms[0].action = "/find_logs/envSetup.do";
			return false;
		} */
		//alert('value of checked button is ' + chkvalue);
		document.forms[0].action = "/find_logs/envDetails.do";
		return true;
	}
</script>
</head>

<body>
	<h1 align="center">Finding Logs</h1>
	<div align="center">
		<table border="5" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<form method="post" name="radioform" onsubmit="chkradio()">
						<br>
						<!-- <div align="center">Select Any of the Environment</div>
 -->
						<div align="center">
							<table align="center" cellspacing="50">

								<tr>
									<th colspan="2">Select Any of the Environment <br></th>
								</tr>
								<tr>
									<td><input type="radio" name="envSetup" value="MBW Prod">
										MBW Prod &nbsp &nbsp &nbsp</td>
									<td><input type="submit" value="edit"> <br></td>
								</tr>
								<tr>
									<td><input type="radio" name="envSetup" value="MBW Stage">
										MBW Stage &nbsp &nbsp <br></td>
									<td><input type="submit" value="edit"> <br></td>
								</tr>
								<tr>
									<td><input type="radio" name="envSetup" value="IBH Prod">
										IBH Prod &nbsp &nbsp &nbsp &nbsp &nbsp<br></td>
									<td><input type="submit" value="edit"> <br></td>
								</tr>
								<tr>
									<td><input type="radio" name="envSetup" value="IBH Stage">
										IBH Stage &nbsp &nbsp &nbsp &nbsp <br></td>
									<td><input type="submit" value="edit"> <br></td>
								</tr>
								<tr>
									<td><input type="submit" value="Save Environment">
										<br></td>
									<td><input type="submit" value="Add New Environment">
										<br></td>
								</tr>
							</table>
						</div>
					</form>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
