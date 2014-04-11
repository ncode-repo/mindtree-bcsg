package com.project.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class JspWriteForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map map = new HashMap(); 
	private String[] values; // params_list
	private String[] event;
	private String[] caption;
	private FormFile javaBean;
	
	public FormFile getJavaBean() {
		return javaBean;
	}

	public void setJavaBean(FormFile javaBean) {
		this.javaBean = javaBean;
	}
	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getValues() {
		return values;
	}

	public String[] getCaption() {
		return caption;
	}

	public void setCaption(String[] caption) {
		this.caption = caption;
	}

	public String[] getEvent() {
		return event;
	}

	public void setEvent(String[] event) {
		this.event = event;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.values = null;
		this.event = null;
		this.caption = null;
		this.javaBean = null;
		super.reset(mapping, request);
	}
}
