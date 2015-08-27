$(document).ready(function() {
	validateForm();
	
	$('#logUser').click(function(){
		if($('#loginId').valid()) {
			$('#loginId').submit();
		}
	});
	
	$('#susLogin').click(function(){
		$('#serviceSuccess').hide();
		$('#loginDiv').show();
			
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
            }
        }
    });
}