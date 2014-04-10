<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<head>
<script type="text/javascript">
function addEvent(event){
	var action = document.forms[0].getAttribute('action');
	var newAction = action + '?event=' +event; 
	document.forms[0].setAttribute('action',newAction);
	return true;
}
</script>
<title>Mock JSP</title>
</head>
<body>
<form method="POST" action="">
{tag}
</form></body></html>
