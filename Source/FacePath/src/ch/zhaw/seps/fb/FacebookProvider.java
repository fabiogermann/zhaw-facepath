package ch.zhaw.seps.fb;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import ch.zhaw.seps.FacePath;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

/* 
 https://developers.facebook.com/docs/reference/javascript/FB.login/
 FB.login(function(response) {
   // handle the response
 }, {scope: 'email,user_likes'});
 * {scope: 'user_aboutme,user_groups,user_likes,user_events,friends_about_me,friends_groups,friends_likes,friends_events'});
 * 
 */

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
		//DEBUG
		//System.out.println("Double check we've got right page " + EntityUtils.toString(entity));

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
	
	private FacebookProfile getUserFromAPI(String userN) {
		User auser = apiConnection.fetchObject(userN, User.class);
	    FacebookProfile fbp = new FacebookProfile(auser.getUsername(), auser.getId());
	    fbp.setName(auser.getFirstName(), auser.getLastName());
	    fbp.setLink(auser.getLink());
		return fbp;
	}
	
	public List<FacebookProfile> getMyFriends() {
		Connection<User> myFriends = apiConnection.fetchConnection("me/friends", User.class);
		List<User> friendsList = myFriends.getData();
		List<FacebookProfile> result = new ArrayList<FacebookProfile>();
		
		for(Iterator<User> i = friendsList.iterator(); i.hasNext(); ) {
		    User item = i.next();
		    FacebookProfile newuser = null;
		    if(item.getUsername() != null) {
		    	newuser = this.getUserFromAPI(item.getUsername());
		    } else {
		    	newuser = this.getUserFromAPI(item.getId());
		    }
			result.add(newuser);
		    //DEBUG
			if (FacePath.DEBUG){
				System.out.println("FacebookProvider-getMyFriends-> "+item.getId()+" "+newuser.getUserUIDString()+" added");
			}
		}
		return result;
	}
	
	public FacebookProfile getMyProfile() {
		FacebookProfile newuser = getUserFromAPI("me");
		//DEBUG
		if (FacePath.DEBUG){
			System.out.println("FacebookProvider-getMyProfile-> "+newuser.getUserID());
		}
		return newuser;
	}
	
	public List<FacebookProfile> getFriendsOf(FacebookProfile profile) throws ClientProtocolException, IOException {
		List<FacebookProfile> result = null;
		
		HttpGet httpget = new HttpGet(profile.getLink()+"/friends");
		HttpResponse response = httpConnection.execute(httpget);
		HttpEntity entity = response.getEntity();
		
		String str = EntityUtils.toString(entity);
		String regex = "(https://www.facebook.com/)([0-9a-zA-Z.]*)(\\?fref=pb)";
		
		Hashtable<String,String> allMatches = new Hashtable<String,String>();
		
		Matcher m = Pattern.compile(regex).matcher(str);
		while (m.find()) {
		   if(! allMatches.containsKey(m.group())) {
			   allMatches.put(m.group(), m.group());
		   }
		}	
		
		Collection<String> col = allMatches.values();
		
		for(Iterator<String> i = col.iterator(); i.hasNext(); ) {
		    String item = i.next();
		    item = item.replace("https://www.facebook.com/", "").replace("?fref=pb", "");
			System.out.println(item);
			FacebookProfile newuser = this.getUserFromAPI(item);
			result.add(newuser);
		}
		
		return result;
	}
}
