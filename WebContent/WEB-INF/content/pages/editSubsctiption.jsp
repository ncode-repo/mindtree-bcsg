<!DOCTYPE html>
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
    <script type="text/javascript">
    	var jsonArray = <%=jsonArray%>;
    
		$(document).ready(function() {
			var fieldList = [];
            var groupList = [];
			$.each(jsonArray.layout,function(c,d) {
				$.each(d.layout, function(e, f) {
					if(f.selected) {
						fieldList.push(d.layout[0].fieldId);
						groupList.push(f.group);
					}
				});
			});
			
			
			$.each(fieldList, function(l,m) {
			 	$.each(jsonArray.fields,function(y,z){
			 		if(z.id == m) {
			 			if(groupList[l]  == 'field_07CD9C75_47FB_9100_814D_E7F6613BF3E4_group') {
			 				$('#osVer').html(z.displayName);
			 			}
			 			if(groupList[l]  == 'field_08A6B20A_22AB_1E71_A6A2_0EF6568BA6C3_group') {
			 				$('#cpu').val(parseInt(z.displayName));
			 			}
			 			if(groupList[l]  == 'ield_4E0A3D51_1004_BE64_4957_04F0DF8B3437_group') {
				 			$('#memoryVal').html(z.displayName);
				 		}
			 			if(groupList[l]  == 'field_6FC9B127_46AC_4FA7_8888_0503EDDE5121_group') {
				 			$('#diskSize').html(z.displayName);
				 		}
			 			if(groupList[l]  == 'field_3219BBFF_A753_4265_E0D8_05064E8A45A6_group') {
				 			$('#networkType').html(z.displayName);
				 		}
			 			if(groupList[l]  == 'field_2CC704AC_3517_3DC6_0DFE_99F92CA176C1_group') {
				 			$('#virtualMon').html(z.displayName);
				 		}
			 		}
			 	});
			});
		});
    </script>
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
		            <td>OS version</td>
		            <td><span id="osVer"></span></td>
		        </tr>
		        <tr>
		            <td>2</td>
		            <td>Number of CPU</td>
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
		            <td>3</td>
		            <td>Memory Size</td>
		            <td><span id="memoryVal">512 MB</span></td>
		        </tr>
		        <tr>
		            <td>4</td>
		            <td>Additional Disk </td>
		            <td>
		            	<span id="diskSize"></span>
		            </td>
		        </tr>
		        <tr>
		            <td>5</td>
		            <td>Selected Network</td>
		            <td><span id="networkType"></span></td>
		        </tr>
		        
		        <tr>
		            <td>6</td>
		            <td>Virtual Machine Monitoring options  </td>
		            <td><span id="virtualMon"></span></td>
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