package com.project.form;

import org.apache.struts.action.ActionForm;

public class LoggerForm extends ActionForm{
	String fromDate="";
	String toDate="";
	String findString="";
	String host="";
	String userName="";
	String password="";
	
	

	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromSDate) {
		this.fromDate = fromSDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFindString() {
		return findString;
	}
	public void setFindString(String findString) {
		this.findString = findString;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
