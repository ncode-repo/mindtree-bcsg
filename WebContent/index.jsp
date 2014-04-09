<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
function myFunction()
{
    var tnl = document.getElementById("select2");  
    
    for(var i=0;i<tnl.length;i++){  
        if(tnl[i].selected == true){  
            alert(tnl[i].value);  
        }  
    }
    
	alert("in form selection");
    var fullPath = document.getElementById('formName').value;
    alert("fullPath "+fullPath);
}

function formSelection()
{  
	alert("in form selection");
    var fullPath = document.getElementById('formName').value;
    alert("fullPath "+fullPath);
}
</script>
</head>
<body>
    <logic:forward name="display"/>
<table width="300" border="1">
<tr>
<td>
Form selection:
</td>
<td>
<input type="file" id="formName" name="formName" onselect="formSelection()" />
</td>
</tr>
      <tr>
      <td><label>Multiple Selection </label>&nbsp;</td>
      <td><select name="select2" id="select2" size="3" multiple="multiple" tabindex="1">
        <option value="11">eleven</option>
        <option value="12">twelve</option>
        <option value="13">thirteen</option>
        <option value="14">fourteen</option>
        <option value="15">fifteen</option>
        <option value="16">sixteen</option>
        <option value="17">seventeen</option>
        <option value="18">eighteen</option>
        <option value="19">nineteen</option>
        <option value="20">twenty</option>
      </select>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="button" name="Submit" value="Submit" tabindex="2" onclick="myFunction()"  /></td>
    </tr>
  </table>
</body>
</html>