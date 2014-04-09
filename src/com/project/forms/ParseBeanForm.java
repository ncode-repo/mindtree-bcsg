package com.project.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;


/**
 * Form bean for Struts File Upload.
 *
*/
public class ParseBeanForm  extends ActionForm{


  private FormFile theFile;
  private String selOption;
  private String ctoken;

  //Added ctoken for CSRF check
  public String getCtoken() {
	return ctoken;
}
public void setCtoken(String ctoken) {
	this.ctoken = ctoken;
}
/**
   * @return Returns the theFile.
   */
  public FormFile getTheFile() {
    return theFile;
  }
  /**
   * @param theFile The FormFile to set.
   */
  public void setTheFile(FormFile theFile) {
    this.theFile = theFile;
  }
  public String getSelOption() {
	return selOption;
  }
  public void setSelOption(String selOption) {
 	this.selOption = selOption;
  }
	  
  
} 