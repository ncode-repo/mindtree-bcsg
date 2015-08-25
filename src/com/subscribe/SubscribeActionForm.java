package com.subscribe;

import org.apache.struts.action.ActionForm;

public class SubscribeActionForm extends ActionForm {
/**
	 * 
	 */
	private static final long serialVersionUID = 4561061906572142984L;
private String svcId;
private String catalogId;
private String categoryName;
private String prodsel;
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
public String getProdsel() {
	return prodsel;
}
public void setProdsel(String prodsel) {
	this.prodsel = prodsel;
}

}
