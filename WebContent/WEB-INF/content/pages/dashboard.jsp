<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.subscribe.LoginForm"%>
<%@ page import="org.codehaus.jettison.json.JSONObject"%>
<html>
<%
LoginForm userDetails = (LoginForm) session.getAttribute("login_details");
JSONObject jsonArray = (JSONObject) session.getAttribute("subscriptionList");
String cancel_id = (String)session.getAttribute("cancel_id");
%>
<head>
	<title> Product  Details </title>
	<link href="ui/css/bootstrap/bootstrap.css" rel="stylesheet">
	<link href="ui/css/style.css" rel="stylesheet">
	
	<script type="text/javascript" src="ui/js/jquery.1.9.0.js"></script>
	<script type="text/javascript" src="ui/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="ui/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="ui/js/profDetails.js"></script>
    <script type="text/javascript">
    	var jsonArray = <%=jsonArray%>
		
		function fnProdDetails(this_Obj) {
			var thisObj = $(this_Obj);
			var prodid=thisObj.attr("id");
			var id=prodid.substring("prodid".length);
			var dateString = jsonArray.members[id].subscriptionTerm.startDate;
			var dateArray = dateString.split('-');
			var dateStr = dateArray[2].substring(0,2) + "/" +
						  dateArray[1] + "/" + dateArray[0]; 
			$('#prodSubDate').html(dateStr);
 			$('#prodCategory').html(jsonArray.members[id].serviceName);
			$('#prodVerson').html(jsonArray.members[id].offeringVersion);
			$('#prodPrice').html('&#36;'+jsonArray.members[id].initPrice.price);
			$('#prodRecPrice').html('&#36;'+jsonArray.members[id].recurringPrice.price);
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
			$('#prod-details').on('show.bs.modal', centerModal);

			var returnedData = '';
	    	$.each(jsonArray.members,function(y,z){
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
	    	
	    	$('#cancelSubscription').click(function() {
	    		$('#cancelSucessMsg').show();
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
	<%if(cancel_id!=null){ %>
	<div id="cancelSucessMsg">
		<div class="message-green margin-top-forty">
			<h5 class="center">
				<span class="glyphicon glyphicon-ok green"></span>
				&nbsp;&nbsp;&nbsp;Your <b>Fully customized VM </b> service has been cancelled successfully.
			</h5>
		</div>
	</div>	
	<%} %>
	<logic:present name="error" scope="request">
		<div id="suvscirbeErrMsg" class="padding-top-thirty">
			<div class="message">
				<h5 class="center">
					<span class="glyphicon glyphicon-exclamation-sign red"></span>
					&nbsp;&nbsp;&nbsp;
					Error while creating subscription
				</h5>
			</div>
		</div>
	</logic:present>
	 
	<div class="container padding-top-thirty">
    	<div class="row">
        	<div class="col-sm-12">
        	
          		<h1 class="payment-select">
          			Subscription Details
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
          		<form name="services" action="cancel.do" id="servicesForm"  method="post">
				<div class="row" id="resposediv">
				  
				</div>		  
	           <div class="row margin-bottom-eighty">
	           		<div class="col-sm-12 no-padding">
	           			<input type="button" disabled="disabled" id="editSubscription" class="btn btn-primary" name="editSubscription" value="Edit Subscription">
	           			<input type="hidden" value="modify_page" id="event" name="event"/>
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