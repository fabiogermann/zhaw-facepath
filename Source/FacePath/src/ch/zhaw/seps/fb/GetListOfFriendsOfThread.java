package ch.zhaw.seps.fb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import ch.zhaw.seps.FacePath;


public class GetListOfFriendsOfThread implements Runnable {

	/**
	 * @param args
	 */
	
	private PoolingHttpClientConnectionManager com;
	private HttpContext cont;
	private ConcurrentLinkedQueue<String> queue = null;
	private FacebookProfile user;
	private CloseableHttpClient httpClient;
	
	public GetListOfFriendsOfThread(PoolingHttpClientConnectionManager conmgr, HttpContext context, ConcurrentLinkedQueue<String> returnqueue, FacebookProfile fbuser) {
		this.com = conmgr;
		this.queue = returnqueue;
		this.user = fbuser;
		this.cont = context;
	}
	
	private void connect() {
		httpClient = HttpClients.custom().setConnectionManager(this.com).build();
	}
	
	private void requestFriends() {
		HttpGet httpget = new HttpGet(user.getLink()+"/friends");
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String str = null;
		
		try {
            response = httpClient.execute(
            httpget, cont);
            try {
                entity = response.getEntity();
                str = EntityUtils.toString(entity);
            } finally {
            	EntityUtils.consume(response.getEntity()) ;
                response.close();
            }
        } catch (ClientProtocolException ex) {
            // Handle protocol errors
        } catch (IOException ex) {
            // Handle I/O errors
        }
		
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
			
		    if (FacePath.DEBUG >= 4){
		    	System.out.println("GLOFOT-found: "+item);
		    }

			// dep
			queue.add(item);

		}
	}
	
	@Override
	public void run() {
		this.connect();
		this.requestFriends();	
	}
}
