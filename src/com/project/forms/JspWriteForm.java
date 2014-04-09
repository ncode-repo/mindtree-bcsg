package com.project.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class JspWriteForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map map = new HashMap(); 
	private String[] values; // params_list
	/*private List button_list = null;// button_list
	 */

	private String[] event;
	private String[] caption;
	
	public void setValues(String[] values) {
		this.values = values;
	}

	/*public void setValue(String key, Object value) {
		System.out.println("map.putting(" + key + ", " + value + ")");

		map.put(key, value);

	}

	public Object getValue(String key) {
		System.out.println("map.getting(" + key + ", " + map.get(key) + ")");
		return map.get(key);
	}*/

	/*public Map getMap() {
		return map;
	}*/

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

	/*public List getButton_list() {
		return button_list;
	}

	public void setButton_list(List button_list) {
		this.button_list = button_list;
	}
	
	 // this is the method that will be called to save
    //  the indexed properties when the form is saved
    public Button getButtonItem(int index)
    {
        // make sure that orderList is not null
        if(this.button_list == null)
        {
            this.button_list = new ArrayList();
        }
 
        // indexes do not come in order, populate empty spots
        while(index >= this.button_list.size())
        {
            this.button_list.add(new Button());
        }
 
        // return the requested item
        return (Button) button_list.get(index);
    }*/
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.values = null;
		this.event = null;
		this.caption = null;
		super.reset(mapping, request);
	}
}
