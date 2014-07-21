package com.project.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LoggerForm extends ActionForm{
	private String fromDate="";
	private String toDate="";
	private String searchByWord="";
	private String noOfLines="";
	private String envSetUp="";
	private String hostName="";
	String portNo="";
	String fileName="";
	private String userName="";
	private String password="";
	
	
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
	public String getSearchByWord() {
		return searchByWord;
	}
	public void setSearchByWord(String searchByWord) {
		this.searchByWord = searchByWord;
	}
	public String getNoOfLines() {
		return noOfLines;
	}
	public void setNoOfLines(String noOfLines) {
		this.noOfLines = noOfLines;
	}
	public String getEnvSetUp() {
		return envSetUp;
	}
	public void setEnvSetUp(String envSetUp) {
		this.envSetUp = envSetUp;
	}

	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if("".equals(getEnvSetUp()) || getEnvSetUp()==null) {
		errors.add("envSetup", new ActionMessage("error.envSetup.required"));
		}
	/*
		if("".equals(getFromDate()) || getFromDate()==null) 
		{
			errors.add("fromDate",new ActionError("error.fromDate.required"));
		}
		if (password == null || password.length() < 1) {
		errors.add("password", new ActionMessage("error.password.required"));
		}
	*/	return errors;
		}
}
