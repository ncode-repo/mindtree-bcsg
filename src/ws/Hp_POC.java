package ws;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.util.Base64;
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
			listSvcOfferings(token_id, "");
			getOfferingDetails(token_id);
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}

	}

	public static String headerAuth(String credentials) {
		if (credentials != null) {
			String encoded = Base64.encode(credentials.getBytes());
			credentials = "Basic " + encoded;
		}
		return credentials;
	}

	public static void listSvcOfferings(String token, String filter) {
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
		try {
			JSONObject result = wsResponse.getEntity(JSONObject.class);
			System.out.println("Service offerings: " + result.toString());
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
	}

	public static void getOfferingDetails(String token) {
		Client client = Client.create();
		String url = "https://csa45.pocaas.hpintelco.org:8444/csa/api/mpp/mppoffering/8a83d7a44f1d0f21014f1d37683608a1";
		// String
		// url="https://213.30.160.29:8444/csa/api/mpp/mppoffering/8a83d7a44f1d0f21014f1d37683608a1";
		// String
		// catalogDetails="?catalogId=8a83d7a44f1d0f21014f23000f8816df&category=SIMPLE_SYSTEM";
		// String
		// catalogDetails="?catalogId=8a83d1be4e975737014ebb4d44485056&category=SIMPLE_SYSTEM";
		String catalogDetails = "?catalogId=8a83d1be4e53200a014e5336c0211537&category=SIMPLE_SYSTEM";

		WebResource webResource = client.resource(url + catalogDetails);
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
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Response message:" + wsResponse.toString());
		}
	}

}
