<!DOCTYPE html>
<%@ page import="org.codehaus.jettison.json.JSONObject"%>
<html>
<%
JSONObject subDetails = (JSONObject) session.getAttribute("subDetails");
%>
<head>
	<title> User Details </title>
	<link href="ui/css/bootstrap/bootstrap.css" rel="stylesheet">
	<link href="ui/css/style.css" rel="stylesheet">
	
	<script type="text/javascript" src="ui/js/jquery.1.9.0.js"></script>
	<script type="text/javascript" src="ui/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="ui/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="ui/js/profDetails.js"></script>
</head>
<body>
	<header>
		<div class="row padding-top-thirty">
			<div class="container">
		    	<div class="col-sm-12">
		    		<div class="pull-left">
		    			
		    		</div>
		    	</div>
		  	</div>
		</div>
	</header>

	<div class="container padding-top-thirty margin-top-twenty">
    	<div class="row">
       		<h1 class="payment-select">
       			Subscription Details
       		</h1>
       		<hr>
       	</div>
     </div>
	<div class="container padding-top-thirty">
    	<div class="row">
       		
			<table class="table table-bordered ">
		    <thead>
		        <tr>
		            <th>Row</th>
		            <th>Name</th>
		            <th>Value</th>
		            
		        </tr>
		    </thead>
		    <tbody>
		        <tr>
		            <td>1</td>
		            <td>c</td>
		            <td>Cloud Demo 81</td>
		        </tr>
		        <tr>
		            <td>2</td>
		            <td>directory backed up</td>
		            <td>/User</td>
		            
		        </tr>
		        <tr>
		            <td>3</td>
		            <td>domain</td>
		            <td>Cloud resources</td>
		        </tr>
		        <tr>
		            <td>4</td>
		            <td>HDD_SIZE </td>
		            <td>
		            	30 (GB)
		            </td>
		        </tr>
		        <tr>
		            <td>5</td>
		            <td>HOSTNAME  </td>
		            <td>4f64fd6569154a</td>
		        </tr>
		        
		        <tr>
		            <td>6</td>
		            <td>IPADDRESS  </td>
		            <td>10.3.87.212</td>
		        </tr>
		        
		        
		        <tr>
		            <td>7</td>
		            <td>MACADDRESS  </td>
		            <td>00:50:56:bc:49:67</td>
		        </tr>
		        
		        <tr>
		            <td>8</td>
		            <td>MEMORYINMB  </td>
		            <td>
		            512 MB
		            </td>
		        </tr>
		        
		        
		        <tr>
		            <td>9</td>
		            <td>Monitoring option subscribed  </td>
		            <td>None</td>
		        </tr>
		        
		        
		        <tr>
		            <td>10</td>
		            <td>NCPU   </td>
		            <td>
		            <form name="services" action="cancel.do" id="editForm"  method="post">
		            	<select id="cpu" name="cpu" class="form-control" style="width:90px">
		            		<option value="1">1</option>
		            		<option value="2">2</option>
		            		<option value="4">4</option>
		            	</select>
		            	<input type="hidden" value="modify" id="event" name="event"/>
		            	</form>
		            </td>
		        </tr>
		        
		        
		        <tr>
		            <td>11</td>
		            <td>NOOFSERVERS   </td>
		            <td>3</td>
		        </tr>
		        
		        
		        <tr>
		            <td>12</td>
		            <td>POWERSTATE   </td>
		            <td>poweredOn</td>
		        </tr>
		        
		        <tr>
		            <td>13</td>
		            <td>RECURRENCE   </td>
		            <td>
		           	 Weekly
		            	
		            </td>
		        </tr>
		        
		        <tr>
		            <td>14</td>
		            <td>vcenter template   </td>
		            <td>win2008</td>
		        </tr>
		        
		        <tr>
		            <td>15</td>
		            <td>VMID </td>
		            <td>vm-2224</td>
		        </tr>
		        
		        <tr>
		            <td>16</td>
		            <td>widows or linux </td>
		            <td>Windows</td>
		        </tr>
		        
		    </tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-4 no-padding">
				<input type="button" id="saveSubscritption" value="Save Subscritption" class="btn btn-primary no-margin-top no-margin-bottom" />;
				<input type="button" value="Cancel" class="btn btn-primary no-margin-top  margin-left-ten no-margin-bottom" />;
			</div>
			<div class="col-sm-4">
			</div>
			<div class="col-sm-4">
				
			</div>
			
		</div>
		
	</div>

</body>
</html>