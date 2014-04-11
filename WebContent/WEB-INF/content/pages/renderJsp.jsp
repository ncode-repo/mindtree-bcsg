<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>JSP tool</title>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/beanUtil.js"></script>
</head>
<body>
	<html:form action="/jspWrite" method="post">
		<table width="300" border="1">
 <%
 	String attrib = null;
 		if (request.getAttribute("params") != null) {
 %>
	    	<tr>
	      		<td>
	      			<label>Multiple Selection </label>&nbsp;
	      		</td>
	      		<td>
	      			<select id="select1" name="select1" size="8" multiple="multiple" tabindex="1">
	      				<c:forEach items="${params}" var="current">   
	          				<option value="${current.key}">${current.key}</option>
	      				</c:forEach> 
	      			</select>
	      		</td>
	      		<td>
	      			<html:select property="values" multiple="true" size="8" styleId="select2" style="display:none"></html:select>  
	      		</td>
	    	</tr>
	    	<tr>
	      		<td>&nbsp;</td>
	      		<td>
	      			<input type="button" name="Submit" value="add" id="add" tabindex="2" onclick=""/>
	      		</td>
	 			<td>
	 				<input type="button" id="remove" tabindex="2" value="remove" onclick="" />
	 			</td>
	    	</tr>
		</table>
  		<br /><br />
  		<div id="buttons" style="display:none">
			<input type="button" id="add_button" value="Add button" onclick="" />
		  	<input type="button" id="rem_button" value="Remove button" onclick="" />
  		</div>
  		<br /><br />
  		<div id='TextBoxesGroup' style="display:none">
    		<html:submit styleId="button1">Submit</html:submit>
  		</div>
		<%
			} else if (request.getAttribute("jsp_write") != null) {
				attrib = (String) request.getAttribute("jsp_write");
				if (attrib != null && attrib.equalsIgnoreCase("false")) {
		%>
					<h2>Jsp Creation error. Please redo the steps</h2>
		<%
			}
				}
		%>
	</html:form>
</body>
</html>