package ch.zhaw.seps.fb;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import ch.zhaw.seps.FacePath;
import ch.zhaw.threadpool.ThreadPool;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;

public class FacebookProvider<T> {
	
	private FacebookClient apiConnection;
	
	@Deprecated
	private CloseableHttpClient httpConnection; //depricated
	
	private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	private HttpContext ctx = new BasicHttpContext();
	
	private static String SCOPE = "user_aboutme,user_groups,user_likes,user_events,friends_about_me,friends_groups,friends_likes,friends_events";
	private static String APP_ID = "676728905679775";
	private static String APP_SECRET = "72defc37e47548c7ee82f9f18c82ca56";
	private static String REDIRECT_URL = "http://klamath.ch/~fabio/seps/logonR.php";
	private String loginRequest = "https://www.facebook.com/dialog/oauth?client_id="+APP_ID+"&redirect_uri="+REDIRECT_URL+"&scope="+SCOPE;
	private String loginCode;
	private String loginAuthentication = "https://graph.facebook.com/oauth/access_token?client_id="+APP_ID+"&redirect_uri="+REDIRECT_URL+"&client_secret="+APP_SECRET+"&code=";
	private String authToken;
	
	public FacebookProvider(String email, String password) {
		try {
			this.connectHTTP(email, password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.getAuthToken();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.connectToApi(this.authToken);
	}
	
	private void connectToApi(String token) {
		apiConnection = new DefaultFacebookClient(token);
	}
	
	private void connectHTTP(String email, String password) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.custom()
		        .setConnectionManager(this.cm)
		        .build();
		
		// depr
		CloseableHttpClient httpclient = httpClient;
		// depr
		this.httpConnection = httpClient;
		
		
		HttpGet httpget = new HttpGet("http://www.facebook.com/login.php");

		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
            response = httpClient.execute(
                    httpget, this.ctx);
            try {
                entity = response.getEntity();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException ex) {
            // Handle protocol errors
        } catch (IOException ex) {
            // Handle I/O errors
        }
		
		if (FacePath.DEBUG){
			System.out.println("Login form get: " + response.getStatusLine());
		}

		HttpPost httpost = new HttpPost("https://www.facebook.com/login.php");

		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("email", email));
		nvps.add(new BasicNameValuePair("pass", password));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		response = httpclient.execute(httpost, this.ctx);
		entity = response.getEntity();
		
		if (FacePath.DEBUG){
			System.out.println("Login form get: " + response.getStatusLine());
		}
	}
	
	private void getAuthToken() throws ClientProtocolException, IOException {
		//TODO here we have a problem with the string encoding... if i use it directly it works, when I use the one completed above it doesnt
		
		CloseableHttpClient httpClient = HttpClients.custom()
		        .setConnectionManager(this.cm)
		        .build();
		
		HttpGet httpget = new HttpGet("https://www.facebook.com/dialog/oauth?client_id=676728905679775&redirect_uri=http://klamath.ch/~fabio/seps/logonR.php&scope=email,read_stream");	
		
		// TODO try/catch like above
		HttpResponse response = httpClient.execute(httpget, this.ctx);
		HttpEntity entity = response.getEntity();
		
		
		if (response.getStatusLine().getStatusCode() == 200) {
			this.loginCode = EntityUtils.toString(entity);
			if (FacePath.DEBUG){
				System.out.println(response.getStatusLine().getStatusCode());	
				System.out.println(this.loginCode);	
			}
			  
			boolean redirectView = false;
			// TODO implement the check
			if(redirectView) {
				//TODO open view and do the magic
			}
			  
			  // TODO: check if redirected to login or step1-code was returned
			  HttpGet httpget2 = new HttpGet("https://graph.facebook.com/oauth/access_token?client_id=676728905679775&redirect_uri=http://klamath.ch/~fabio/seps/logonR.php&client_secret=72defc37e47548c7ee82f9f18c82ca56&code="+this.loginCode);
			  HttpResponse response2 = httpClient.execute(httpget2, this.ctx);
			  HttpEntity entity2 = response2.getEntity();
			  this.authToken = EntityUtils.toString(entity2).substring(13).split("&")[0];
			  System.out.println(this.authToken);
		}
	}

	@Deprecated
	private void closeHTTP() {
		httpConnection.getConnectionManager().shutdown();
	}
	
	@Deprecated
	private FacebookProfile getUserFromAPI(String userN) {
		User auser = null;
		try {
			auser = apiConnection.fetchObject(userN, User.class);
			FacebookProfile fbp = new FacebookProfile(auser.getUsername(), auser.getId());
		    fbp.setName(auser.getFirstName(), auser.getLastName());
		    fbp.setLink(auser.getLink());
			return fbp;
		} catch(FacebookException e) {
			//nope
		}
		return null;
	    
	}
	
	public Collection<FacebookProfile> getUserFromThreadedAPI(Collection<String> users) {
		ConcurrentLinkedQueue<FacebookProfile> returnqueue = new ConcurrentLinkedQueue<FacebookProfile>();
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
		
		for( String user  : users) {
			tasks.add(Executors.callable(new GetUserFromAPIThread(authToken, returnqueue, user)));
		}
		
		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnqueue;
	}
	
	public Collection<String> getFriendsOfThreaded(Collection<FacebookProfile> users) {
		ConcurrentLinkedQueue<String> returnqueue = new ConcurrentLinkedQueue<String>();
		
		ExecutorService executor = Executors.newFixedThreadPool(2);//newCachedThreadPool();
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
		
		for( FacebookProfile user  : users) {
			tasks.add(Executors.callable(new GetFriendsOfThread(cm, ctx, returnqueue, user)));
			//executor.execute(new GetFriendsOfThread(cm, ctx, returnqueue, user));
		}
		
		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//TODO duplicate elimination
		return returnqueue;
	}
	
	@Deprecated
	public List<FacebookProfile> getMyFriends() {
		Connection<User> myFriends = apiConnection.fetchConnection("me/friends", User.class);
		List<User> friendsList = myFriends.getData();
		List<String> todo = new ArrayList<String>();
		
		List<FacebookProfile> result = new ArrayList<FacebookProfile>();
		
		for(User friend : friendsList) {
			todo.add(friend.getId());
		}
		
		Collection<FacebookProfile> value = this.getUserFromThreadedAPI(todo);
		
		for(FacebookProfile val : value) {
			result.add(val);
		}

		return result;
	}
	
	@Deprecated
	public FacebookProfile getMyProfile() {
		FacebookProfile newuser = getUserFromAPI("me");
		
		//DEBUG
		if (FacePath.DEBUG){
			System.out.println("FacebookProvider-getMyProfile-> "+newuser.getUserID());
		}
		return newuser;
	}
	
	@Deprecated
	public List<FacebookProfile> getFriendsOf(FacebookProfile profile) throws ClientProtocolException, IOException {
		List<FacebookProfile> result = new ArrayList<FacebookProfile>();
		
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
			FacebookProfile newuser = null;
			try {
				newuser = this.getUserFromAPI(item);
			} finally {
				//nix
			}
			
			if (newuser != null) {
				result.add(newuser);
			}
		}
		return result;
	}
}
