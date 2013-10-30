package ch.zhaw.seps;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ch.zhaw.seps.fb.FacebookProfile;

import com.restfb.types.User;

public class HttpParseFB {

	static final String USEREMAIL = "fabio.germann";
	static final String PASSWORD = "Th0r83991nDiGO";
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
		
		HttpGet httpget2 = new HttpGet("https://www.facebook.com/sadyhawk/friends");
		HttpResponse response2 = httpclient.execute(httpget2);
		HttpEntity entity2 = response2.getEntity();
		
		
		String str = EntityUtils.toString(entity2);
		String regex = "(https://www.facebook.com/)([0-9a-zA-Z.]*)(\\?fref=pb)";
		
		//Elements doc = Jsoup.parse(str, "UTF-8").getElementsByTag("a");
		//Elements links = doc; //.select("a");
		
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
		}

    
		httpclient.getConnectionManager().shutdown();
	}
}
