package ch.zhaw.seps.fb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import ch.zhaw.seps.FacePath;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;

public class FacebookProvider {

	private FacebookClient apiConnection;

	@Deprecated
	private CloseableHttpClient httpConnection;

	private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	private HttpContext ctx = new BasicHttpContext();

	private static String SCOPE = "email,read_stream";
	private static String APP_ID = "676728905679775";
	private static String APP_SECRET = "72defc37e47548c7ee82f9f18c82ca56";
	private static String REDIRECT_URL = "http://klamath.ch/~fabio/seps/logonR.php";
	private String loginRequest = "https://www.facebook.com/dialog/oauth?client_id=" + APP_ID + "&redirect_uri="
	        + REDIRECT_URL + "&scope=" + SCOPE;
	private String loginCode;
	private String loginAuthentication = "https://graph.facebook.com/oauth/access_token?client_id=" + APP_ID
	        + "&redirect_uri=" + REDIRECT_URL + "&client_secret=" + APP_SECRET + "&code=";
	private String authToken;

	public FacebookProvider(String email, String password) throws FacebookLoginException, FacebookApplicationAuthorizationException {
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

	public String getLoginRequest() {
		return loginRequest;
	}

	private void connectHTTP(String email, String password) throws FacebookLoginException, ClientProtocolException,
	        IOException {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();
		

		HttpGet httpget = new HttpGet("http://www.facebook.com/login.php");

		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			response = httpClient.execute(httpget, this.ctx);
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

		if (FacePath.DEBUG) {
			System.out.println("Login pageload: " + response.getStatusLine().getStatusCode());
		}

		HttpPost httpost = new HttpPost("https://www.facebook.com/login.php");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("email", email));
		nvps.add(new BasicNameValuePair("pass", password));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		response = httpClient.execute(httpost, this.ctx);
		entity = response.getEntity();

		int logincode = response.getStatusLine().getStatusCode();

		if (FacePath.DEBUG) {
			System.out.println("Login performed: " + logincode);
		}

		if (logincode != 302) {
			throw new FacebookLoginException();
		}
	}

	private void getAuthToken() throws FacebookApplicationAuthorizationException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

		HttpGet httpget = new HttpGet(this.loginRequest);
		
		httpget.setHeader("User-Agent", "FacepathAuthenticator");
		
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String content = null;
		try {
			response = httpClient.execute(httpget, this.ctx);
			try {
				entity = response.getEntity();
				content = EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException ex) {
			// Handle protocol errors
		} catch (IOException ex) {
			// Handle I/O errors
		}
		
		Integer appcode = response.getStatusLine().getStatusCode();
				
		if (content.contains("<") && content.contains("klamath.ch")) {
			throw new FacebookApplicationAuthorizationException();
		}
		
		if (response.getStatusLine().getStatusCode() == 200) {
			this.loginCode = content;
			if (FacePath.DEBUG) {
				System.out.println("AuthenticationCode: "+appcode);
			}

			HttpGet httpget2 = new HttpGet(this.loginAuthentication + this.loginCode);
			HttpResponse response2 = httpClient.execute(httpget2, this.ctx);
			HttpEntity entity2 = response2.getEntity();
			this.authToken = EntityUtils.toString(entity2).substring(13).split("&")[0];
			
			if (FacePath.DEBUG) {
				System.out.println("Authentication: OK");
			}
		}
	}

	@Deprecated
	private FacebookProfile getUserFromAPI(String userN) {
		User auser = null;
		try {
			auser = apiConnection.fetchObject(userN, User.class);
			FacebookProfile fbp = new FacebookProfile(auser.getUsername(), auser.getId());
			fbp.setName(auser.getFirstName(), auser.getLastName());
			fbp.setLink(auser.getLink());
			fbp.setLocales(auser.getLocale());
			return fbp;
		} catch (FacebookException e) {
			// nope
		}
		return null;
	}

	public Collection<FacebookProfile> getUserFromThreadedAPI(Collection<String> users) {
		ConcurrentLinkedQueue<FacebookProfile> returnqueue = new ConcurrentLinkedQueue<FacebookProfile>();
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

		for (String user : users) {
			tasks.add(Executors.callable(new GetUserFromAPIThread(authToken, returnqueue, user)));
		}

		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
		
		try {
			while (!executor.awaitTermination(10L, TimeUnit.SECONDS)) {
				if (FacePath.DEBUG){
					System.out.println("Still waiting for the executor to finish");
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		return returnqueue;
	}

	public Collection<FacebookProfile> getFriendsOfThreaded(Collection<FacebookProfile> users, FacebookNetwork fN) {
		ConcurrentLinkedQueue<FacebookProfile> returnqueue = null;

		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

		for (FacebookProfile user : users) {
			returnqueue = new ConcurrentLinkedQueue<FacebookProfile>();
			user.setFriends(returnqueue);
			tasks.add(Executors.callable(new GetFriendsOfThread(cm, ctx, authToken, returnqueue, user, fN)));
			if (FacePath.DEBUG) {
				System.out.println("FBP-searches-friends-of: " + user.getUserID());
			}
		}

		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
		return users;
	}

	@Deprecated
	public List<FacebookProfile> getMyFriends() {
		Connection<User> myFriends = apiConnection.fetchConnection("me/friends", User.class);
		List<User> friendsList = myFriends.getData();
		List<String> todo = new ArrayList<String>();

		List<FacebookProfile> result = new ArrayList<FacebookProfile>();

		for (User friend : friendsList) {
			todo.add(friend.getId());
		}

		Collection<FacebookProfile> value = this.getUserFromThreadedAPI(todo);

		for (FacebookProfile val : value) {
			result.add(val);
		}

		return result;
	}

	@Deprecated
	public FacebookProfile getMyProfile() {
		FacebookProfile newuser = getUserFromAPI("me");

		// DEBUG
		if (FacePath.DEBUG) {
			System.out.println("FacebookProvider-getMyProfile-> " + newuser.getUserID());
		}
		return newuser;
	}

	@Deprecated
	public List<FacebookProfile> getFriendsOf(FacebookProfile profile) throws ClientProtocolException, IOException {
		List<FacebookProfile> result = new ArrayList<FacebookProfile>();

		HttpGet httpget = new HttpGet(profile.getLink() + "/friends");
		HttpResponse response = httpConnection.execute(httpget);
		HttpEntity entity = response.getEntity();

		String str = EntityUtils.toString(entity);
		String regex = "(https://www.facebook.com/)([0-9a-zA-Z.]*)(\\?fref=pb)";

		Hashtable<String, String> allMatches = new Hashtable<String, String>();

		Matcher m = Pattern.compile(regex).matcher(str);
		while (m.find()) {
			if (!allMatches.containsKey(m.group())) {
				allMatches.put(m.group(), m.group());
			}
		}

		Collection<String> col = allMatches.values();

		for (Iterator<String> i = col.iterator(); i.hasNext();) {
			String item = i.next();
			item = item.replace("https://www.facebook.com/", "").replace("?fref=pb", "");
			System.out.println(item);
			FacebookProfile newuser = null;
			try {
				newuser = this.getUserFromAPI(item);
			} finally {
				// nix
			}

			if (newuser != null) {
				result.add(newuser);
			}
		}
		return result;
	}

	public List<FacebookProfile> getUserFromSearch(String searchQuery) {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

		HttpGet httpget = null;
		try {
			httpget = new HttpGet("https://www.facebook.com/search/results.php?q="
			        + URLEncoder.encode(searchQuery, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String str = null;

		try {
			response = httpClient.execute(httpget, this.ctx);
			try {
				entity = response.getEntity();
				str = EntityUtils.toString(entity);
			} finally {
				EntityUtils.consume(response.getEntity());
				response.close();
			}
		} catch (ClientProtocolException ex) {
			// Handle protocol errors
		} catch (IOException ex) {
			// Handle I/O errors
		}

		String regex = "(href=\"https://www.facebook.com/)([0-9a-zA-Z.]*)\"";

		Set<String> userURLSet = new HashSet<String>();
		List<String> usernameList = new ArrayList<String>();

		Matcher m = Pattern.compile(regex).matcher(str);
		while (m.find()) {
			if (!userURLSet.add(m.group())) {
				usernameList.add(m.group().replace("href=\"https://www.facebook.com/", "").replace("\"", ""));
			}
		}

		Collection<FacebookProfile> profileCollection = this.getUserFromThreadedAPI(usernameList);
		List<FacebookProfile> profileList = new ArrayList<FacebookProfile>(usernameList.size());
		for (int i = 0; i < usernameList.size(); i++) {
			profileList.add(null);
		}
		for (FacebookProfile profile : profileCollection) {
			profileList.set(usernameList.indexOf(profile.getUserUIDString()), profile);
		}
		while (profileList.remove(null)) {
		}

		return profileList;
	}

	public static ImageIcon getImageIconFromUsername(String username) {
		ImageIcon image = null;
		try {
			image = new ImageIcon(new URL("https://graph.facebook.com/" + username + "/picture"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return image;
	}
}