package com.subscribe;

import org.apache.struts.action.ActionForm;

public class LoginForm  extends ActionForm {
/**
	 * 
	 */
	private static final long serialVersionUID = 5044300143881755331L;
private String userEmail;

public String getUserEmail() {
	return userEmail;
}

public void setUserEmail(String userEmail) {
	this.userEmail = userEmail;
}
}
