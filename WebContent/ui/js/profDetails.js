$(document).ready(function() {
	validateForm();
	
	$('#saveButton').click(function(){
		if($('#profileId').valid()) {
			//alert(" Sumo");	
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