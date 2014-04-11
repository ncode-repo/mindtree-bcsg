function validateForm() {
 	var x=document.getElementById("file").value;
	if (x==null || x=="" || x.length == 0) {
		//alert("Enter the file name");
		$('.alertMsg').html("Enter the file name");
		$.colorbox({inline:true, width: 'auto', height: '200px', href:"#alertDialog"});
		return false;
	}
	var ext = getFileExtension(x);
	if(ext!="java") {
		alert("Invalid File Format. Please upload a java file.");
		return false;
	}
	return true;
}

function getFileExtension(name) {
	var found = name.lastIndexOf('.') + 1;
	return (found > 0 ? name.substr(found) : "");
}

function msg(flag){
	if(flag=="true")
		alert("Jsp was created and written successfully");
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
			alert("Only 10 textboxes allow");
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
	  		alert("No more textbox to remove");
	  		return false;
		}   
		counter--;	 
		$("#TextBoxDiv" + counter).remove();
	}); 
		
	$('#button1').click(function(){
		if(counter==0){
			alert("Add some buttons");
	  		return false;
		}
		for ( var i = 1; i <= counter; i++ ) {
			if($('#caption'+i).val()==""||$('#value'+i).val()==""){
				alert("please enter button captions and events");
				return false;
			}
		}
	}); 
})