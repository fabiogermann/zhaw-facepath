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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class GetFriendsOfThread implements Runnable {

	/**
	 * @param args
	 */
	
	private DefaultHttpClient http;
	private ConcurrentLinkedQueue<String> queue = null;
	private FacebookProfile user;
	
	public GetFriendsOfThread(DefaultHttpClient httpc, ConcurrentLinkedQueue<String> returnqueue, FacebookProfile fbuser) {
		this.http = httpc;
		this.queue = returnqueue;
		this.user = fbuser;
	}

	@Override
	public void run() {
		HttpGet httpget = new HttpGet(user.getLink()+"/friends");
		
		HttpResponse response = null;
		
		try {
			response = http.execute(httpget);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpEntity entity = response.getEntity();
		
		String str = null;
		
		try {
			str = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			System.out.println(item);

				queue.add(item);
				user.addCandidate(item);

		}
	}
}
