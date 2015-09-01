<!DOCTYPE html>
<html>
<head>
	<title> User Details </title>
	<link href="ui/css/bootstrap/bootstrap.css" rel="stylesheet">
	<link href="ui/css/style.css" rel="stylesheet">
	
	<script type="text/javascript" src="ui/js/jquery.1.9.0.js"></script>
	<script type="text/javascript" src="ui/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="ui/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="ui/js/profDetails.js"></script>
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
	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<h1 class="payment-select">
					User Details
				</h1>
				<hr>
			</div>
		</div>
	</div>
	<div class="container padding-top-thirty">
		<div class="row">
			<div class="col-sm-12">
				<form name="pofileForm" action="profile.do" id="profileId"  method="post" class="details-form payment-form">
				
					<!--  User Email  -->
					<label for="userEmail"> 
						Email address
					</label>
					<input type="text" name="userEmail" class="form-control" id="userEmail" maxlength="70" />
					
					
					<!--  First Name  -->
					<label for="firstName"> 
						First name
					</label>
					<input type="text" name="firstName" class="form-control" id="firstName"  maxlength="30"/>
					
					<!--  Last Name -->
					<label for="lastName"> 
						Last name
					</label>
					<input type="text" name="lastName" class="form-control" id="lastName"  maxlength="50"/>
					
					
					<!--  Last Name -->
					<label for="lastName"> 
						Phone  number
					</label>
					<input type="text" name="telephone" class="form-control" id="telephone"  maxlength="15"/>
					<input type="hidden" value="" id="event" name="event"/>
					<input type="hidden" value="" id="userId" name="userId"/>
					<br />	
					<input type="button" id="saveButton" class="btn btn-primary no-margin-top margin-right-twenty" name="saveUser" value="Save User">
					<input type="button" id="nextButton" class="btn btn-primary no-margin-top margin-right-twenty displayNone" name="nxtUser" value="Next">
				</form>
			</div>
			
		</div>
	</div>

	
</body>
</html>