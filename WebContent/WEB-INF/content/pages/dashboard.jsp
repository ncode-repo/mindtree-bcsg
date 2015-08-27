<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.subscribe.UserDetailsActionForm"%>
<%@ page import="org.codehaus.jettison.json.JSONObject"%>
<html>
<%
UserDetailsActionForm userDetails = (UserDetailsActionForm) session.getAttribute("userDetails");
JSONObject jsonArray = (JSONObject) session.getAttribute("offerings");
%>
<head>
	<title> Product  Details </title>
	<link href="ui/css/bootstrap/bootstrap.css" rel="stylesheet">
	<link href="ui/css/style.css" rel="stylesheet">
	
	<script type="text/javascript" src="ui/js/jquery.1.9.0.js"></script>
	<script type="text/javascript" src="ui/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="ui/js/bootstrap.min.js"></script>
    
    <script type="text/javascript">
    	 
		var jsonNew = 
		{ 
		  "@type":"urn:x-hp:2012:software:cloud:data_model:service-offering:collection",
		  "@total_results":1,
		  "@start_index":0,
		  "@items_per_page":1,
		  "members":[
		 
		            {
						"@self": "/csa/api/mpp/mpp-subscription/8a83d7a44f280299014f2b952603012f",
		        		"@type": "urn:x-hp:2012:software:cloud:data_model:service-subscription",
		        		"name": "Fully customized VM (1.0.0)",
		              	"ext": {
		                	"csa_name_key": "Fully customized VM (1.0.0)"
		              	 },
		              	 "id": "8a83d7a44f280299014f2b952603012f",
		              	 "owner": "bcsguser",
		              	 "image": "csa/images/library/infrastructure.png",
		              	 "status": "ACTIVE",
		              	 "catalogId": "8a83d7a44f1d0f21014f23000f8816df",
		              	 "serviceId": "8a83d7a44f1d0f21014f1d37683608a1",
		              	 "serviceImage": "/csa/images/library/infrastructure.png",
		              	 "serviceName": "Fully customized VM",
		              	 "cancelable": true,
		              	 "transferable": false,
		              	 "modifiable": false,
		              	 "modifiableOptions": false,
		              	 "reorderable": false,
		              	 "deletable": false,
		              	 "subscriptionTerm": {
		                	"startDate": "2015-08-14T07:40:31.000Z"
		              	 },
		              	"initPrice": {
		                	"currency": "USD",
		                	"price": 0
		              	},
		              	"recurringPrice": {
		                	"currency": "USD",
		                	"price": 0,
		                	"basedOn": "Yearly"
		              	},
		              	"requestId": null,
		              	"showViewRequest": true,
		              	"instanceState": "ACTIVE",
		              	"offeringVersion": "1.0.0",
		              	"hideInitialPrice": false,
		              	"hideRecurringPrice": false
		            },
		            {
		                "@self": "/csa/api/mpp/mpp-subscription/8a83d7a44f280299014f5f2b8ad40867",
		                "@type": "urn:x-hp:2012:software:cloud:data_model:service-subscription",
		                "name": "sampleRequest",
		                "ext": {
		                  "csa_name_key": "sampleRequest"
		                },
		                "id": "8a83d7a44f280299014f5f2b8ad40867",
		                "owner": "bcsguser",
		                "image": "csa/images/library/infrastructure.png",
		                "status": "ACTIVE",
		                "catalogId": "8a83d7a44f1d0f21014f23000f8816df",
		                "serviceId": "8a83d7a44f1d0f21014f1d37683608a1",
		                "serviceImage": "/csa/images/library/infrastructure.png",
		                "serviceName": "Fully customized VM",
		                "cancelable": true,
		                "transferable": false,
		                "modifiable": false,
		                "modifiableOptions": false,
		                "reorderable": false,
		                "deletable": false,
		                "subscriptionTerm": {
		                  "startDate": "2015-06-21T05:32:17.000Z",
		                  "endDate": "2016-06-23T05:32:17.000Z"
		                },
		                "initPrice": {
		                  "currency": "USD",
		                  "price": 0
		                },
		                "recurringPrice": {
		                  "currency": "USD",
		                  "price": 0,
		                  "basedOn": "Yearly"
		                },
		                "requestId": null,
		                "showViewRequest": true,
		                "instanceState": "ACTIVE",
		                "offeringVersion": "1.0.0",
		                "hideInitialPrice": false,
		                "hideRecurringPrice": false
		              }
		       ]
		 }
		
		function fnProdDetails(this_Obj) {
			var thisObj = $(this_Obj);
			var prodid=thisObj.attr("id");
			var id=prodid.substring("prodid".length);
			var dateString = jsonNew.members[id].subscriptionTerm.startDate;
			var dateformat= new Date(Date.parse(dateString));
			console.log(" dateString " +dateString.split('-'));
			$('#prodCategory').html(jsonNew.members[id].serviceName);
			$('#prodVerson').html(jsonNew.members[id].offeringVersion);
			$('#prodPrice').html('&#36;'+jsonNew.members[id].initPrice.price);
			$('#prodRecPrice').html('&#36;'+jsonNew.members[id].recurringPrice.price);
			$('#prod-details').on('show.bs.mocdal', centerModal);
			$('#prod-details').modal("show");	
				
		}
		
		$('#prod-details').on('show.bs.modal', centerModal);
		function centerModal() {
		    $(this).css('display', 'block');
		    var $dialog = $(this).find(".modal-dialog");
		    var offset = ($(window).height() - $dialog.height()) / 2;
		    // Center modal vertically in window
		    $dialog.css("margin-top", offset);
		}

		$(window).on("resize", function () {
		    $('.modal:visible').each(centerModal);
		});
		
		$(document).ready(function() {
			

			var returnedData = '';
	    	$.each(jsonNew.members,function(y,z){
	    		
	    		
	    		if(z.status.toLowerCase() == 'ACTIVE'.toLowerCase()) {
		    		returnedData += '<div class="row greyspace billing-item">';
		    		returnedData += '<div class="col-sm-3">';
		    		returnedData += '<input type="radio" class="prodcheck" value="prodselect'+y+'" name="prodsel" />';
		    		returnedData += '<input type="hidden"  value="'+z.id+'" name="svcId" />';
		    		returnedData += '<input type="hidden"  value="'+z.catalogId+'" name="catalogId" />';
		    		returnedData += '<input type="hidden"  value="'+z.serviceName+'" name="categoryName" />';
		    		returnedData += '</div>';
		    		returnedData += '<div class="col-sm-5 margin-top-ten">';
		    		returnedData += z.name;
		    		returnedData += '</div>';
		    		returnedData += '<div class="col-sm-4">';
		    		returnedData += '<input type="button" value="Show Details" onclick="fnProdDetails(this)"';
		    		returnedData += 'id="prodid'+y+'"';
		    		returnedData += 'class="btn btn-primary no-margin-top no-margin-bottom" />';
		    		returnedData += '</div>';
		    		returnedData += '</div>';
	    		}
    		});
	    	$('#resposediv').append(returnedData);
	    	$("#editSubscription").attr('disabled', true);
			$("#cancelSubscription").attr('disabled', true);
	    	$("input[name='prodsel']").click(function() { 
                 checkCkbox(); 
         	});
			
		});
		function checkCkbox() {
            var numChkd = $("input[name='prodsel']:checked").length;
            if(numChkd == 0) {
                $("#editSubscription").attr('disabled', true);
        		$("#cancelSubscription").attr('disabled', true);
            } else {
                $("#editSubscription").attr('disabled', false);
        		$("#cancelSubscription").attr('disabled', false);
            }
        } 
		
		
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
	
	
	<div class="container padding-top-thirty">
    	<div class="row">
        	<div class="col-sm-12">
        	<logic:present name="error" scope="request">
					<strong>
					Error while creating subscription
					</strong>
				</logic:present> 
          		<h1 class="payment-select">
          			Application Details
          		</h1>
          		<hr>
          		<div class="row billing-title">
            		<div class="col-sm-3">
              			Product Select
            		</div>
            		<div class="col-sm-5">
              			Service Name
            		</div>
            		<div class="col-sm-4">
              			Other Service Details 
            		</div>
          		</div>
          		<form name="services" action="getAppsDet.do" id="servicesForm"  method="post">
				<div class="row" id="resposediv">
				  
				</div>		  
	           <div class="row margin-bottom-eighty">
	           		<div class="col-sm-12 no-padding">
	           			<input type="button" disabled="disabled" id="editSubscription" class="btn btn-primary" name="editSubscription" value="Edit Subscription">
	           			<input type="button" disabled="disabled" id="cancelSubscription" class="btn btn-primary margin-left-twenty" name="cancelSubscription" value="Cancel Subscription">
	           		</div>
	           </div>
	           </form>
        </div>
      </div>
    </div>

	<!-- Modal -->
	<div class="modal fade" id="prod-details" tabindex="-1" role="dialog" aria-labelledby="fee-details-label" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h2 class="modal-title" >Service Details</h2>
	            </div>
	            <div class="modal-body">
	                <table class="table table-bordered">
	                    <tbody>
	                        <tr style="border-top-width: 0px;">
	                            <td>Name</td>
	                            <td><span id="prodCategory"></span></td>
	                        </tr>
	                        <tr>
	                            <td>Version</td>
	                            <td><span id="prodVerson"></span></td>
	                        </tr>
	                         <tr>
	                            <td>Subscription Date</td>
	                            <td><span id="prodSubDate"></span></td>
	                        </tr>
	                        <tr>
	                            <td>Price</td>
	                            <td><span id="prodPrice"></span></td>
	                        </tr>
	                        <tr>
	                            <td>Recurring Price</td>
	                            <td><span id="prodRecPrice"></span></td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
</body>
</html>