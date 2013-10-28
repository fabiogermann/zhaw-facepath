package ch.zhaw.seps.fb;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

public class FacebookProvider<T> {
	
	private FacebookClient apiConnection;
	private DefaultHttpClient httpConnection;
	
	public FacebookProvider(String token, String email, String password) {
		this.connectToApi(token);
		try {
			this.connectHTTP(email, password);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connectToApi(String token) {
		apiConnection = new DefaultFacebookClient(token);
	}
	
	private void connectHTTP(String email, String password) throws ClientProtocolException, IOException {
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
		nvps.add(new BasicNameValuePair("email", email));
		nvps.add(new BasicNameValuePair("pass", password));

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
	}

	private void closeHTTP() {
		httpConnection.getConnectionManager().shutdown();
	}
	
	public List<FacebookProfile> getMyFriends() {
		Connection<User> myFriends = apiConnection.fetchConnection("me/friends", User.class);
		List<User> friendsList = myFriends.getData();
		List<FacebookProfile> result = new ArrayList<FacebookProfile>();
		
		for(Iterator<User> i = friendsList.iterator(); i.hasNext(); ) {
		    User item = i.next();
		    
		    User auser = apiConnection.fetchObject(item.getId(), User.class);
		    
		    FacebookProfile fbp = new FacebookProfile(auser.getUsername());
			fbp.setName(auser.getFirstName(), auser.getLastName());
			result.add(fbp);
			
		    //DEBUG
		    System.out.println("FacebookProvider-getMyFriends-> "+item.getId()+" "+auser.getUsername()+" added");
		}
		return result;
	}
	
	public FacebookProfile getMyProfile() {
		User auser = apiConnection.fetchObject("me", User.class);
		FacebookProfile fbp = new FacebookProfile(auser.getUsername());
		fbp.setName(auser.getFirstName(), auser.getLastName());
		System.out.println("FacebookProvider-getMyProfile-> "+auser.getUsername());
		return fbp;
	}
	
	public List<FacebookProfile> getFriendsOf(FacebookProfile profile) {
		return null;
		// TODO
		// here we hate to parse the information from the http page
		
	}
}
