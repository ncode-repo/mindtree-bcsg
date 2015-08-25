package com.subscribe;

import org.apache.struts.action.ActionForm;

public class UserDetailsActionForm extends ActionForm {
/**
	 * 
	 */
	private static final long serialVersionUID = 5915553533095240842L;
private String firstName;
private String lastName;
private String telephone;
private String userEmail;
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getTelephone() {
	return telephone;
}
public void setTelephone(String telephone) {
	this.telephone = telephone;
}
public String getUserEmail() {
	return userEmail;
}
public void setUserEmail(String userEmail) {
	this.userEmail = userEmail;
}
}

