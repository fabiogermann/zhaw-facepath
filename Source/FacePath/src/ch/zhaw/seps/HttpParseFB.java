package ch.zhaw.seps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpParseFB {

	static final String USEREMAIL = "foo@bar.com";
	static final String PASSWORD = "password";
	static final String TESTBUDDY = "marius.zingg";
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		//new FacePath();
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet("http://www.facebook.com/login.php");

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
		    entity.consumeContent();
		}
		System.out.println("Initial set of cookies:");
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		if (cookies.isEmpty()) {
		    System.out.println("None");
		} else {
		    for (int i = 0; i < cookies.size(); i++) {
		        System.out.println("- " + cookies.get(i).toString());
		    }
		}

		HttpPost httpost = new HttpPost("https://www.facebook.com/login.php");

		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("email", USEREMAIL));
		nvps.add(new BasicNameValuePair("pass", PASSWORD));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		response = httpclient.execute(httpost);
		entity = response.getEntity();
		System.out.println("Double check we've got right page " + EntityUtils.toString(entity));

		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
		    entity.consumeContent();
		}

		System.out.println("Post logon cookies:");
		cookies = httpclient.getCookieStore().getCookies();
		if (cookies.isEmpty()) {
		    System.out.println("None");
		} else {
		    for (int i = 0; i < cookies.size(); i++) {
		        System.out.println("- " + cookies.get(i).toString());
		    }
		}
		
		HttpGet httpget2 = new HttpGet("https://graph.facebook.com/" + TESTBUDDY);
		HttpResponse response2 = httpclient.execute(httpget2);
		HttpEntity entity2 = response2.getEntity();
		
		System.out.println(EntityUtils.toString(entity2));
		httpclient.getConnectionManager().shutdown();
	}
}
