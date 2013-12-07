/**
 * Bezieht die Informationen eines Profils von Facebook
 * Ist ein Thread, sodass Informationen zu anderen Profilen "gleichzeitig" geladen werden können
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.zhaw.seps.FacePath;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;


public class GetUserFromAPIThread implements Runnable {
	
	private FacebookClient apiConnection;
	private String token;
	private ConcurrentLinkedQueue<FacebookProfile> queue = null;
	private String user;
	private User auser;
	
	/**
	 * Konstruktor
	 * Übergibt die notwendigen Informationen
	 */
	public GetUserFromAPIThread(String apikey, ConcurrentLinkedQueue<FacebookProfile> returnqueue, String username) {
		this.token = apikey;
		this.queue = returnqueue;
		this.user = username;
	}

	/**
	 * Bezieht die Informationen eines Profils von Facebook
	 */
	@Override
	public void run() {
		apiConnection = new DefaultFacebookClient(this.token);
		auser = apiConnection.fetchObject(user, User.class);
		
		FacebookProfile fbp = new FacebookProfile(auser.getUsername(), auser.getId());
		
	    fbp.setName(auser.getFirstName(), auser.getLastName());
	    fbp.setLink(auser.getLink());
	    fbp.setLocales(auser.getLocale());
	    
	    queue.add(fbp);
	    if (FacePath.DEBUG >= 4){
	    	System.out.println("GUFAPI-created: "+user);
	    }
	}
}
