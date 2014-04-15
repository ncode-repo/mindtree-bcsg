function validateForm() {
 	var x=document.getElementById("uploadBtn").value;
	if (x==null || x=="" || x.length == 0) {
		$('#errMsg').html("Enter the file name");
		$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
		return false;
	}
	var ext = getFileExtension(x);
	if(ext!="java") {
		$('#errMsg').html("Invalid File Format. Please upload a java file.");
		$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
		return false;
	}
	return true;
}

function getFileExtension(name) {
	var found = name.lastIndexOf('.') + 1;
	return (found > 0 ? name.substr(found) : "");
}

function msg(flag){
	if(flag=="true") {
		$('#errMsg').html("Jsp was created and written successfully");
		$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
	}
	else
		return true;
}

$(document).ready(function () {
	var counter =1;
	$('#add').click(function() {
		if($('#select1 option:selected').size()>=1){
			$('#buttons').css("display","block");
			$('#select2').css("display","block");
		}
		return !$('#select1 option:selected').remove().appendTo('#select2');  
	});  

	$('#remove').click(function() {
		!$('#select2 option:selected').remove().appendTo('#select1');
		$('#select2').css('display', 'none');
		if(document.getElementById("select2").length==0){
			$('#buttons').css("display","none");
			$('#TextBoxesGroup').css("display","none");   
		}
		return ;  
	});  

	$('#add_button').click(function() { 
		if(counter>10){
			$('#errMsg').html("Only 10 textboxes allow");
			$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
			return false;
		}   
		var newTextBoxDiv =  $(document.createElement('div')).attr("id", 'TextBoxDiv' + counter);
		newTextBoxDiv.html('<label>Caption #'+ counter + ' : </label>' +
	  		'<input type="text" name="caption" id="caption' + counter + '" value="" >&nbsp;&nbsp;');
		newTextBoxDiv.append('<label>Value #'+ counter + ' : </label>' +
	      '<input type="text" name="event" id="value' + counter + '" value="" > <br><br>');
		//$("#TextBoxesGroup").insertAfter(newTextBoxDiv);
		newTextBoxDiv.prependTo("#TextBoxesGroup");
		counter++;
		$('#TextBoxesGroup').css("display","block");
	}); 
		
	$('#rem_button').click(function() { 
		if(counter==0){
	  		$('#errMsg').html("No more textbox to remove");
			$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
	  		return false;
		}   
		counter--;	 
		$("#TextBoxDiv" + counter).remove();
	}); 
		
	$('#button1').click(function(){
		if(counter==0){
			$('#errMsg').html("Add some buttons");
			$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
	  		return false;
		}
		for ( var i = 1; i <= counter; i++ ) {
			if($('#caption'+i).val()==""||$('#value'+i).val()==""){
				$('#errMsg').html("please enter button captions and events");
				$.colorbox({inline:true, width: '500px', height: '200px', href:"#beanAltMsg"});
				return false;
			}
		}
	}); 
})