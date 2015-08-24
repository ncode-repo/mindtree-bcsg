package ws.response.parser;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import ws.JsonConstants;

public class JsonParser {

public static JSONArray parseJson(JSONObject result){
	int records = 0;
	JSONArray members = new JSONArray();
	Iterator<String> i =result.keys();
	while(i.hasNext()){
		try {
			String key = i.next();
			   if(key.equalsIgnoreCase(JsonConstants.TOTAL_RESULTS))
			   records = (Integer) result.get(key);
			  if(key.equalsIgnoreCase(JsonConstants.MEMBERS)){
				  members = (JSONArray) result.get(key);
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return members;
}
}
