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
    <script type="text/javascript" src="ui/js/profDetails.js"></script>
    <script type="text/javascript">
    	var jsonNew = <%=jsonArray.toString()%>;
		
		function fnProdDetails(this_Obj) {
			var thisObj = $(this_Obj);
			var prodid=thisObj.attr("id");
			var id=prodid.substring("prodid".length);
			
			
			$('#prodCategory').html(jsonNew.members[id].category.displayName);
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
			$('#prod-details').on('show.bs.mocdal', centerModal);
			var returnedData = '';
	    	$.each(jsonNew.members,function(y,z){
	    		returnedData += '<div class="row greyspace billing-item">';
	    		returnedData += '<div class="col-sm-3">';
	    		returnedData += '<input type="radio" class="prodcheck" value="prodselect'+y+'" name="prodsel" />';
	    		returnedData += '<input type="hidden"  value="'+z.id+'" name="svcId" />';
	    		returnedData += '<input type="hidden"  value="'+z.catalogId+'" name="catalogId" />';
	    		returnedData += '<input type="hidden"  value="'+z.category.name+'" name="categoryName" />';
	    		returnedData += '</div>';
	    		returnedData += '<div class="col-sm-5 margin-top-ten">';
	    		returnedData += z.displayName;
	    		returnedData += '</div>';
	    		returnedData += '<div class="col-sm-4">';
	    		returnedData += '<input type="button" value="Product Details" onclick="fnProdDetails(this)"';
	    		returnedData += 'id="prodid'+y+'"';
	    		returnedData += 'class="btn btn-primary no-margin-top no-margin-bottom" />';
	    		returnedData += '</div>';
	    		returnedData += '</div>';
    		});
	    	$('#resposediv').append(returnedData);
	    	$("input[name='prodsel']").click(function() { 
                 checkCkbox(); 
         	});
	    	$("#continueButton").attr('disabled', true);
		});
		function checkCkbox() {
            var numChkd = $("input[name='prodsel']:checked").length;
            if(numChkd == 0) {
                $("#continueButton").attr('disabled', true);
            } else {
                $("#continueButton").attr('disabled', false);
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
              			Product Name
            		</div>
            		<div class="col-sm-4">
              			Other Product Details 
            		</div>
          		</div>
          		<form name="services" action="getAppsDet.do" id="servicesForm"  method="post">
				<div class="row" id="resposediv">
				  
				</div>		  
	           <div class="row margin-bottom-eighty">
	           		<div class="col-sm-12 no-padding">
	           			<input type="button" disabled="disabled" id="continueButton" class="btn btn-primary" name="continueUser" value="Subscribe">
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
	                <h2 class="modal-title" >Product Details</h2>
	            </div>
	            <div class="modal-body">
	                <table class="table table-bordered">
	                    <tbody>
	                        <tr style="border-top-width: 0px;">
	                            <td>Category</td>
	                            <td><span id="prodCategory"></span></td>
	                        </tr>
	                        <tr>
	                            <td>Version</td>
	                            <td><span id="prodVerson"></span></td>
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