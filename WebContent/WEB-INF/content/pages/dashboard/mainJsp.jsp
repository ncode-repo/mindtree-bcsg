<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP tool</title>
 <%-- <script type="text/javascript" src="<bean:message key="cdn.appimages.url"/>js/v3/jquery-1.7.1.min.js"></script> --%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
 <script type="text/javascript">  
  $().ready(function() {
	 var counter =1;
   $('#add').click(function() {
	   if($('#select1 option:selected').size()>=1){
	   $('#buttons').css("display","block");
	   $('#select2').css("display","block");
	   }
    return !$('#select1 option:selected').remove().appendTo('#select2');  
   });  
   $('#remove').click(function() {
	   !$('#select2 option:selected').remove().appendTo('#select1');
	   if(document.getElementById("select2").length==0){
		   $('#buttons').css("display","none");
		   $('#TextBoxesGroup').css("display","none");   
	   }
    return ;  
   });  
  /*  $('input[@type=submit]').click(function() {
	   $('#select2 *').attr('selected','selected');
	   }); */
   
   $('#add_button').click(function() { 
	   if(counter>10){
           alert("Only 10 textboxes allow");
           return false;
	}   

	var newTextBoxDiv = $(document.createElement('div'))
	     .attr("id", 'TextBoxDiv' + counter);

	newTextBoxDiv.html('<label>Caption #'+ counter + ' : </label>' +
		      '<input type="text" name="caption" id="caption' + counter + '" value="" >&nbsp;&nbsp;');

		newTextBoxDiv.append('<label>Value #'+ counter + ' : </label>' +
			      '<input type="text" name="event" id="value' + counter + '" value="" > <br><br>');
		//$("#TextBoxesGroup").insertAfter(newTextBoxDiv);
		newTextBoxDiv.prependTo("#TextBoxesGroup");
	counter++;
	 $('#TextBoxesGroup').css("display","block");
   }); 
   $('#rem_button').click(function() { 
	   if(counter==0){
	          alert("No more textbox to remove");
	          return false;
	       }   
	 
		counter--;
	 
	        $("#TextBoxDiv" + counter).remove();
   }); 
   $('#button1').click(function(){
	   if(counter==0){
		   alert("Add some buttons");
	          return false;
	   }
	   for ( var i = 1; i <= counter; i++ ) {
		   if($('#caption'+i).val()==""||$('#value'+i).val()==""){
				alert("please enter button captions and events");
				return false;
			}
	   }
     }); 
  }); 
 </script>  
</head>
<body>
<html:form action="/JspWrite" method="post">
<table width="300" border="1">
 <% if(request.getAttribute("params")!=null){
	 %>

    <tr>
      <td><label>Multiple Selection </label>&nbsp;</td>
      <td><select id="select1" name="select1" size="8" multiple="multiple" tabindex="1">
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
      <td><input type="button" name="Submit" value="add" id="add" tabindex="2" onclick=""/></td>
 <td><input type="button" id="remove" tabindex="2" value="remove" onclick="" /></td>
    </tr>
  </table>
  <br><br>
  <div id="buttons" style="display:none" >
  <input type="button" id="add_button" value="Add button" onclick="" />
  <input type="button" id="rem_button" value="Remove button" onclick="" />
  </div>
  <br><br>
  <div id='TextBoxesGroup' style="display:none">
    <html:submit styleId="button1">Submit</html:submit>
  </div>

   <%} %>
   </html:form>
</body>
</html>