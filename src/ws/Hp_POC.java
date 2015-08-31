package ws;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.apache.catalina.util.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Hp_POC {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject(); //
		// Create a trust manager that does not validate certificate chains
		/*
		 * TrustManager[] trustAllCerts = new TrustManager[] { new
		 * X509TrustManager() { public X509Certificate[] getAcceptedIssuers() {
		 * return null; }
		 * 
		 * public void checkClientTrusted(X509Certificate[] certs, String
		 * authType) { }
		 * 
		 * public void checkServerTrusted(X509Certificate[] certs, String
		 * authType) { } } };
		 * 
		 * // Install the all-trusting trust manager try { SSLContext sc =
		 * SSLContext.getInstance("TLS"); sc.init(null, trustAllCerts, new
		 * SecureRandom()); HttpsURLConnection
		 * .setDefaultSSLSocketFactory(sc.getSocketFactory()); } catch
		 * (Exception e) { ; }
		 */
		Client client = Client.create();
		try {
			JSONObject jsonCred = new JSONObject();
			jsonCred.put("username", "bcsguser");
			jsonCred.put("password", "cloud");
			json.put("passwordCredentials", jsonCred);
			json.put("tenantName", "BCSG");
			System.out.println("Request body: " + json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		WebResource webResource = client
				.resource("https://csa45.pocaas.hpintelco.org:8444/idm-service/v2.0/tokens");
		// WebResource webResource =
		// client.resource("https://213.30.160.29:8444/idm-service/v2.0/tokens");

		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);

		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("Authorization", authorization)
				.post(ClientResponse.class, json);
		try {
			JSONObject result = wsResponse.getEntity(JSONObject.class);
			JSONObject token = result.getJSONObject("token");
			String token_id = token.getString("id");
			System.out.println("Token ID: " + token_id);
			modifySubscription(token_id, "8a83d7a44f280299014f6a3f17f13c4b", "8a83d7a44f1d0f21014f23000f8816df","4");
			//listSvcOfferings(token_id, "");
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}

	}
public static String generateToken(){
	JSONObject json = new JSONObject(); //
	Client client = Client.create();
	try {
		JSONObject jsonCred = new JSONObject();
		jsonCred.put("username", "bcsguser");
		jsonCred.put("password", "cloud");
		json.put("passwordCredentials", jsonCred);
		json.put("tenantName", "BCSG");
		System.out.println("Request body: " + json.toString());
	} catch (JSONException e) {
		e.printStackTrace();
	}

	WebResource webResource = client
			.resource("https://csa45.pocaas.hpintelco.org:8444/idm-service/v2.0/tokens");
	// WebResource webResource =
	// client.resource("https://213.30.160.29:8444/idm-service/v2.0/tokens");

	String authorization = "idmTransportUser" + ":" + "idmTransportUser";
	authorization = headerAuth(authorization);

	ClientResponse wsResponse = webResource
			.accept(MediaType.APPLICATION_JSON)
			.type(MediaType.APPLICATION_JSON)
			.header("Authorization", authorization)
			.post(ClientResponse.class, json);
	String token_id=null;
	try {
		JSONObject result = wsResponse.getEntity(JSONObject.class);
		JSONObject token = result.getJSONObject("token");
		 token_id = token.getString("id");
		System.out.println("Token ID: " + token_id);
	} catch (Exception e) {
		System.out.println("Exception message: " + e.getMessage());
	}
	return token_id;
}
	public static String headerAuth(String credentials) {
		if (credentials != null) {
			String encoded = Base64.encode(credentials.getBytes());
			credentials = "Basic " + encoded;
		}
		return credentials;
	}

	public static JSONObject listSvcOfferings(String token, String filter) {
		JSONObject json = new JSONObject();
		Client client = Client.create();
		try {
			json.put("approval", "ALL");
			System.out.println("Request body: " + json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		WebResource webResource = client
				.resource("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-offering/filter");
		// WebResource webResource =
		// client.resource("https://213.30.160.29:8444/csa/api/mpp/mpp-offering/filter");
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("Authorization", authorization)
				.header("X-Auth-token", token).post(ClientResponse.class, json);
		JSONObject result = null;
		try {
			result = wsResponse.getEntity(JSONObject.class);
			//JSONArray members = JsonParser.parseJson(result);
			System.out.println("Service offerings: " + result.toString());
			//getOfferingDetails(token,members,"SIMPLE_SYSTEM");
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
		return result;
	}

	public static void getOfferingDetails(String token,JSONArray members, String category){
		StringBuilder uri = new StringBuilder();
		uri.append("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-offering/");//<svcOfferingId>?catalogId=<ctId>&category=<ctgry>");
		for(int i=0;i<=members.length();i++){
			try {
				JSONObject memBr = (JSONObject) members.get(i);
				JSONObject memBrCategory =	(JSONObject) memBr.get("category");
				if(category.equalsIgnoreCase((String)memBrCategory.get("name"))){
					uri.append(memBr.get("id"));
					uri.append("?catalogId="+memBr.get("catalogId")+"&category=");
					uri.append(category);
					System.out.println("URI: "+uri.toString());
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Client client = Client.create();
		WebResource webResource = client
				.resource(uri.toString());
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("Authorization", authorization)
				.header("X-Auth-token", token).get(ClientResponse.class);
		try {
			JSONObject result = wsResponse.getEntity(JSONObject.class);
			System.out.println("Offerings Details: " + result.toString());
			//createSubscription(token,"SIMPLE_SYSTEM");
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
	}

	public static String createSubscription(String token,String id,String catalogId,String categoryName,String subName){
		JSONObject json = new JSONObject();
		JSONObject fields = new JSONObject(); 
		Client client = Client.create();
		StringBuilder s = new StringBuilder();
		StringBuilder uri = new StringBuilder();
		uri.append("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-request/");
		uri.append(id+"?catalogId=");
		uri.append(catalogId);
		s.append("--Abcdefgh \n");
		s.append("Content-Disposition: form-data; name=\"requestForm\" \n\n");
		try {
			json.put("categoryName",categoryName);
			json.put("subscriptionName", subName);
			Calendar cal = Calendar.getInstance(); 
			Date date = cal.getTime();
			cal.add(Calendar.MONTH, 1);
			Date newDate = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
			json.put("startDate", dateFormatter.format(date));
			json.put("endDate", dateFormatter.format(newDate));
			fields.put("field_8a83d7a44f1d0f21014f1d37689308ad", "win2008");
			fields.put("field_8a83d7a44f1d0f21014f1d37689308e3", 1);
			fields.put("field_8a83d7a44f1d0f21014f1d3768a308fd", 2048);
			fields.put("field_8a83d7a44f1d0f21014f1d3768a3091a", "Gold");
			json.put("fields", fields);
			json.put("action", "ORDER");
			s.append(json.toString()+"\n --Abcdefgh--");
			System.out.println("Request body: " + s.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//"https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-request/8a83d7a44f1d0f21014f1d37683608a1?catalogId=8a83d7a44f1d0f21014f23000f8816df"
		WebResource webResource = client
				.resource(uri.toString());
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.MULTIPART_FORM_DATA+";boundary=Abcdefgh")
				.header("Authorization", authorization)
				.header("X-Auth-token", token).post(ClientResponse.class,s.toString());
		JSONObject result = new JSONObject();
		String sub_id = "";
		try {
			if(wsResponse.getStatus()==200){
			result = wsResponse.getEntity(JSONObject.class);
			sub_id  = result.getString("id");
			}
			System.out.println("Offerings Details: " + result.toString());
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
	return sub_id;
	}

	public static JSONObject getSubscriptionList(String token){
		JSONObject json = new JSONObject();
		Client client = Client.create();
		try {
			json.put("user_id", "bcsguser");
			System.out.println("Request body: " + json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		WebResource webResource = client
				.resource("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-subscription/filter");
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("Authorization", authorization)
				.header("X-Auth-token", token).post(ClientResponse.class, json);
		JSONObject result = null;
		try {
			result = wsResponse.getEntity(JSONObject.class);
			//JSONArray members = JsonParser.parseJson(result);
			System.out.println("Subscription List: " + result.toString());
			//getOfferingDetails(token,members,"SIMPLE_SYSTEM");
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
		return result;
	}

	public static String cancelSubscription(String token,String sub_id,String catalogId){
		JSONObject json = new JSONObject();
		Client client = Client.create();
		StringBuilder s = new StringBuilder();
		StringBuilder uri = new StringBuilder();
		uri.append("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-request/");
		uri.append(sub_id+"?catalogId=");
		uri.append(catalogId);
		s.append("--Abcdefgh \n");
		s.append("Content-Disposition: form-data; name=\"requestForm\" \n\n");
		try {
			json.put("action", "CANCEL_SUBSCRIPTION");
			s.append(json.toString()+"\n --Abcdefgh--");
			System.out.println("Request body: " + s.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		WebResource webResource = client
				.resource(uri.toString());
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.MULTIPART_FORM_DATA+";boundary=Abcdefgh")
				.header("Authorization", authorization)
				.header("X-Auth-token", token).post(ClientResponse.class,s.toString());
		JSONObject result = new JSONObject();
		String cancel_id = "";
		try {
			if(wsResponse.getStatus()==200){
			result = wsResponse.getEntity(JSONObject.class);
			cancel_id  = result.getString("id");
			}
			System.out.println("Cancellation id: " + result.toString());
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
	return cancel_id;
	}
	public static String modifySubscription(String token,String sub_id,String catalogId,String cpus){
		JSONObject json = new JSONObject();
		JSONObject fields = new JSONObject();
		Client client = Client.create();
		StringBuilder s = new StringBuilder();
		StringBuilder uri = new StringBuilder();

		uri.append("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-request/");
		uri.append(sub_id+"?catalogId=");
		uri.append(catalogId);
		s.append("--Abcdefgh \n");
		s.append("Content-Disposition: form-data; name=\"requestForm\" \n\n");
		try {
			json.put("subscriptionName", "sampleRequest");
			json.put("subscriptionDescription", "Sample");
			json.put("fields", fields);
			fields.put("field_08A6B20A_22AB_1E71_A6A2_0EF6568BA6C3_group", true);
			fields.put("field_69a7a604_1169_4622_b14f_dd66b124e099", cpus);
			json.put("action", "MODIFY_SUBSCRIPTION");
			s.append(json.toString()+"\n --Abcdefgh--");
			System.out.println("Request body: " + s.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		WebResource webResource = client
				.resource(uri.toString());
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.MULTIPART_FORM_DATA+";boundary=Abcdefgh")
				.header("Authorization", authorization)
				.header("X-Auth-token", token).post(ClientResponse.class,s.toString());
		JSONObject result = new JSONObject();
		String modify_id = "";
		try {
			if(wsResponse.getStatus()==200){
			result = wsResponse.getEntity(JSONObject.class);
			modify_id  = result.getString("id");
			}
			System.out.println("Modification id: " + result.toString());
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
	return modify_id;
	}

	public static JSONObject getSubscriptionDetails(String token,String sub_id){
		StringBuilder uri = new StringBuilder();
		uri.append("https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mpp-subscription/");
		uri.append(sub_id);
		uri.append("/modify");
		Client client = Client.create();
		WebResource webResource = client
				.resource(uri.toString());
		String authorization = "idmTransportUser" + ":" + "idmTransportUser";
		authorization = headerAuth(authorization);
		ClientResponse wsResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("Authorization", authorization)
				.header("X-Auth-token", token).get(ClientResponse.class);
		JSONObject result = new JSONObject();
		try {
			result = wsResponse.getEntity(JSONObject.class);
			System.out.println("Subscription Details: " + result.toString());
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
		return result;
	}

}
