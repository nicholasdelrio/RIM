package edu.utep.cybershare.rim.pipeline.sink;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class VLCLogin {
	
	private static final String PROTOCOL = "http";
	private static final String DOMAIN = "scidesign-test.utep.edu";
	private static final String PATH = "/services/rest/user/login.json";
	
	CloseableHttpClient httpClient;
	
	public VLCLogin(){
		//BasicCookieStore cookieStore = new BasicCookieStore();
		httpClient = HttpClients.createDefault();
	}
	
	
	private URL getLoginURL(){
		try{return new URL(PROTOCOL + "://" + DOMAIN + PATH);}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	private List<NameValuePair> getCredentialValuePairs(String username, String password){
		ArrayList<NameValuePair> credentialPairs = new ArrayList<NameValuePair>();
		
		
		credentialPairs.add(new BasicNameValuePair("username", username));
		credentialPairs.add(new BasicNameValuePair("password", password));
		return credentialPairs;
	}
	
	public CloseableHttpClient login(String username, String password){
		String response = this.postLogin(username, password);
		System.out.println(response);
		
		return httpClient;
	}
	
	private String postLogin(String username, String password){
		HttpPost httpPost = new HttpPost(getLoginURL().toString());
		
		httpPost.setEntity(new UrlEncodedFormEntity(this.getCredentialValuePairs(username, password), HTTP.DEF_CONTENT_CHARSET));
						
		String result = "Failure";
		try {
			// Execute the method.
			
			System.out.println(httpPost.getURI().toString());
			
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if(entity != null){result = EntityUtils.toString(entity);}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Release the connection.
			httpPost.releaseConnection();
		}
		return result;
	}
	
	public static void main(String[] args){
		VLCLogin login = new VLCLogin();
		login.login("ndel2", "ndel2");
	}
}
