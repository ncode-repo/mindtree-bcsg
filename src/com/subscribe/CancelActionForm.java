package com.subscribe;

import org.apache.struts.action.ActionForm;

public class CancelActionForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4561061906572142984L;
private String svcId;
private String catalogId;
private String categoryName;
private String event;
private String cpu;
private String subName;
public String getSubName() {
	return subName;
}
public void setSubName(String subName) {
	this.subName = subName;
}
public String getSvcId() {
	return svcId;
}
public void setSvcId(String svcId) {
	this.svcId = svcId;
}
public String getCatalogId() {
	return catalogId;
}
public void setCatalogId(String catalogId) {
	this.catalogId = catalogId;
}
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
public String getEvent() {
	return event;
}
public void setEvent(String prodsel) {
	this.event = prodsel;
}
public String getCpu() {
	return cpu;
}
public void setCpu(String cpu) {
	this.cpu = cpu;
}

}

