$(document).ready(function() {
	validateForm();
	
	$('#saveButton').click(function(){
		if($('#profileId').valid()) {
			alert(" Sumo");	
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