package ws.response.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ws.JsonConstants;

import com.subscribe.SubscribeActionForm;

public class JsonParser {

public static SubscribeActionForm parseJson(JSONObject result,int index){
	int records = 0;
	JSONArray members = new JSONArray();
	SubscribeActionForm form = new SubscribeActionForm();
	Iterator<String> i =result.keys();
	while(i.hasNext()){
		try {
			String key = i.next();
			   if(key.equalsIgnoreCase(JsonConstants.TOTAL_RESULTS))
			   records = (Integer) result.get(key);
			  if(key.equalsIgnoreCase(JsonConstants.MEMBERS)){
				  members = (JSONArray) result.get(key);
				  for(int k=0;k<members.length();k++){
					  if(k==index){
						  JSONObject j=  members.getJSONObject(k);
						  form.setSvcId((String)j.get("id"));  
						  form.setCatalogId((String)j.get("catalogId"));
						  form.setCategoryName(j.getJSONObject("category").getString("name"));
						  form.setDisplayName((String)j.get("displayName"));
						  break;
					  }
				  }
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return form;
}

public static JSONObject parseJson(JSONObject result,ArrayList<String> subNames){
	int records = 0;
	JSONArray sub_members= new JSONArray();
	JSONObject new_members = new JSONObject();
	Iterator<String> i =result.keys();
	while(i.hasNext()){
		try {
			String key = i.next();
			   if(key.equalsIgnoreCase(JsonConstants.TOTAL_RESULTS))
			   records = (Integer) result.get(key);
			  if(key.equalsIgnoreCase(JsonConstants.MEMBERS)){
				  JSONArray members  = (JSONArray) result.get(key);
				  for(int k=0;k<members.length();k++){
						  JSONObject j=  members.getJSONObject(k);
						  Iterator lst =   subNames.iterator();
						  while(lst.hasNext()){
							  if(j.getString("name").equalsIgnoreCase((String)lst.next())){
								  sub_members.put(j);  
							  }
						  }
				  }
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	try {
		new_members.put("members", sub_members);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return new_members;
}
public static JSONObject parseJson(JSONObject result,String cpus){
	int records = 0;
	JSONArray fields = new JSONArray();
	Iterator<String> i =result.keys();
	JSONObject fieldsForModify = new JSONObject();
	while(i.hasNext()){
		try {
			String key = i.next();
			  if(key.equalsIgnoreCase(JsonConstants.FIELDS)){
				  fields = (JSONArray) result.get(key);
				  for(int k=0;k<fields.length();k++){
						  JSONObject j=  fields.getJSONObject(k);
						  if(cpus.equalsIgnoreCase(j.getString(JsonConstants.DISPLAY_NAME))){
							  fieldsForModify.put(j.getString(JsonConstants.ID), true);
						  }
						  if(JsonConstants.NCPU.equalsIgnoreCase(j.getString(JsonConstants.DISPLAY_NAME))){
							  if(cpus.equalsIgnoreCase(j.getString(JsonConstants.VALUE)))
							  fieldsForModify.put(j.getString(JsonConstants.ID), cpus);
						  }
				  }
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return fieldsForModify;
}

}
