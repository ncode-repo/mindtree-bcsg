package ws.response.parser;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
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
}
