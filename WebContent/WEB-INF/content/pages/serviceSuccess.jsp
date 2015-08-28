<!DOCTYPE html>
<html>
<head>
	<title> Services </title>
	<link href="ui/css/bootstrap/bootstrap.css" rel="stylesheet">
	<link href="ui/css/style.css" rel="stylesheet">
	
	<script type="text/javascript" src="ui/js/jquery.1.9.0.js"></script>
	<script type="text/javascript" src="ui/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="ui/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="ui/js/login.js"></script>
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
  		<div class="row forty-break-space">
	      	<div class="col-sm-12">
	      	</div>
    	</div>
 	</div>
	<div id="serviceSuccess">
		<div class="message-green margin-top-forty">
			<h5 class="center">
				<span class="glyphicon glyphicon-ok green"></span>
				&nbsp;&nbsp;&nbsp;
				Your <b>Fully customized VM</b> service has been subscribed successfully.
			</h5>
		</div>

	  <div class="container">
	    	<div class="row forty-break-space">
	        	<div class="col-sm-12">
	        	</div>
	      	</div>
	    </div>
		    
	    <div class="container">
	   		<div class="row">
	     		<div class="col-sm-12">
	       			<div class="profile-form center">
	         			<input type="button" class="btn btn-primary" id="susLogin" value="Login" />
	       			</div>
	     		</div>
	    	</div>
	   	</div>
	 </div>
	 
	 <div id="loginDiv" class="displayNone">
		 <div class="container">
			<div class="row">
				<div class="col-sm-12">
					<h1 class="payment-select">
						Login
					</h1>
					<hr>
				</div>
			</div>
		</div>
		<div class="container padding-top-thirty">
			<div class="row">
				<div class="col-sm-12">
					<form name="loginForm" action="login.do" id="loginId"  method="post" class="details-form payment-form">
					
						<!--  User Email  -->
						<label for="userEmail"> 
							Email address
						</label>
						<input type="text" name="userEmail" class="form-control" id="userEmail" maxlength="70" />
						<br />
						
						<input type="button" id="logUser" class="btn btn-primary no-margin-top margin-right-twenty" name="loginUser" value="Login">
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>