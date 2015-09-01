$(document).ready(function() {
	validateForm();
	
	$('#saveButton').click(function(){
		if($('#profileId').valid()) {
			//alert(" Sumo");
			$('#event').val("");
			$('#profileId').submit();
		}
	});
	$('#nextButton').click(function(){
		if($('#profileId').valid()) {
			//alert(" Sumo");
			$('#event').val("next");
			$('#profileId').submit();
		}
	});
	$('#continueButton').click(function(){
			//alert(" Last Form");	
			$('#servicesForm').submit();
	});
	$('#cancelSubscription').click(function(){
		//alert(" Last Form");
		$('#event').val("abc");
		$('#servicesForm').submit();
});
	$('#editSubscription').click(function(){
		//alert(" Last Form");
		//$('#servicesForm').attr("action","editSub.do");
		
		$('#servicesForm').submit();
});
	$('#saveSubscritption').click(function(){
		//alert(" Last Form");	
		$('#editForm').submit();
});
	$('#userEmail').focusout(function(){
		 if ($('#userEmail').valid()) {
		$('#event').val("chkEmail");
		var form_elem=$("#profileId").serializeArray();
		 $.ajax({ 
			 type: "POST", 
		     url: "profile.do", 	
		     data: form_elem,
		     success: function(msg){
		    	 if(msg!=null){
		    	 $('#userId').val(msg.user_id);
		    	 $('#firstName').val(msg.first_name);
		    	 $('#lastName').val(msg.last_name);
		    	 $('#telephone').val(msg.telephone);
		    	
		    	 $('#saveButton').hide();
		    	 $('#nextButton').show();
		    	 }else{
		    		 $('#userId').val("");
			    	 $('#firstName').val("");
			    	 $('#lastName').val("");
			    	 $('#telephone').val("");
		    		 $('#saveButton').show();
			    	 $('#nextButton').hide(); 
		    	 }
		    	 }
		     });
	}else{
		 $('#saveButton').show();
    	 $('#nextButton').hide(); 
	}
		 
});
});
function validateForm() {
    var validator = $("form").validate({
	  highlight:function(element,errorClass){
		  $(element).css({"border":"1px solid red"});
		  $(element).addClass('focused');
	  },
	  unhighlight:function(element,errorClass){
		  $(element).css({"border":"1px solid #CCCCCC"});
		  $(element).removeClass('focused');
	  },
        rules : {
        	userEmail : {
                emailidreq : true,
                email : true,
                maxseventycharacter : 70
            },
            firstName :{
            	firstnamereq : true,
            	mincharacterlength : 2,
            	namespacespl : true
            },
            lastName :{
            	lastnamereq : true,
            	mincharacterlength : 2,
            	namespacespl : true
            },
            telephone :{
            	telephonereq :true,
            	phonenumvalid : true,
            	mintenlength : 10,
	            maxfifteencharacter : 15
			}
        }
    });
}