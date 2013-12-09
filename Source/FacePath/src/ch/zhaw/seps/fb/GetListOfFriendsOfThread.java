/**
 * Bezieht eine Liste der Freunde eines Profils, ohne diese genauer zu untersuchen
 * Ist ein Thread, sodass Freunde verschiedener Profile "gleichzeitig" gesucht werden k��nnen
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import ch.zhaw.seps.FacePath;

public class GetListOfFriendsOfThread implements Runnable {

	private PoolingHttpClientConnectionManager com;
	private HttpContext cont;
	private ConcurrentLinkedQueue<String> queue = null;
	private FacebookProfile user;
	private CloseableHttpClient httpClient;

	/**
	 * Konstruktor üsbergibt die notwendigen Informationen
	 */
	public GetListOfFriendsOfThread(PoolingHttpClientConnectionManager conmgr, HttpContext context,
	        ConcurrentLinkedQueue<String> returnqueue, FacebookProfile fbuser) {
		this.com = conmgr;
		this.queue = returnqueue;
		this.user = fbuser;
		this.cont = context;
	}

	/**
	 * Baut eine Verbindung zur Facebook Webseite auf
	 */
	private void connect() {
		httpClient = HttpClients.custom().setConnectionManager(this.com).build();
	}

	/**
	 * Bezieht die Liste der Freunde des beim Instanzieren angegebenen Profils
	 * 
	 * @param startIndex
	 *            Start Index der Freunde des zu beziehenden Freundes
	 */
	private void requestFriends(int startIndex) {
		HttpGet httpget = new HttpGet("http://m.facebook.com/" + user.getUserUIDString() + "?v=friends&startindex="
		        + startIndex);
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String str = null;

		try {
			response = httpClient.execute(httpget, cont);
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

		String regex = "(<a href=\"/)([0-9a-zA-Z.]*)(\\?fref)";

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
			item = item.replace("<a href=\"/", "").replace("?fref", "");

			if (FacePath.DEBUG >= 4) {
				System.out.println("GLOFOT-found: " + item);
			}

			// dep
			queue.add(item);

		}
		// uncomment for all friends
		// String nextPageRegex = "(<a href=\"/" + user.getUserUIDString()
		// +
		// "\\?v=friends&amp;mutual&amp;startindex=)([0-9]*)(&amp;refid=17\"><span>See More Friends</span></a>)";
		// Matcher nextPageMatcher =
		// Pattern.compile(nextPageRegex).matcher(str);
		// String match = null;
		// if (nextPageMatcher.find()) {
		// match = nextPageMatcher.group();
		// match = match
		// .replace("<a href=\"/" + user.getUserUIDString() +
		// "?v=friends&amp;mutual&amp;startindex=", "")
		// .replace("&amp;refid=17\"><span>See More Friends</span></a>", "");
		// requestFriends(Integer.parseInt(match));
		// }

	}

	@Override
	public void run() {
		this.connect();
		this.requestFriends(0);
	}
}
