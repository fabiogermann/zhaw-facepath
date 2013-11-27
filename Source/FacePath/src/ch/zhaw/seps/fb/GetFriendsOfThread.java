package ch.zhaw.seps.fb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import ch.zhaw.seps.FacePath;


public class GetFriendsOfThread implements Runnable {

	/**
	 * @param args
	 */
	
	private PoolingHttpClientConnectionManager com;
	private HttpContext cont;
	private ConcurrentLinkedQueue<FacebookProfile> queue = null;
	private FacebookProfile user;
	private String ApiKey;
	private FacebookNetwork fN;
	
	public GetFriendsOfThread(	PoolingHttpClientConnectionManager conmgr, 
								HttpContext context, 
								String apikey, 
								ConcurrentLinkedQueue<FacebookProfile> returnqueue, 
								FacebookProfile fbuser,
								FacebookNetwork network) {
		this.com = conmgr;
		this.queue = returnqueue;
		this.user = fbuser;
		this.cont = context;
		this.ApiKey = apikey;
		this.fN = network;
	}

	@Override
	public void run() {
		// Get all the friends of a user
		Map<String,FacebookProfile> knownProfiles = fN.getKnownProfiles();
		ConcurrentLinkedQueue<String> stringqueue = new ConcurrentLinkedQueue<String>();
		ExecutorService preexecutor = Executors.newFixedThreadPool(2);
		preexecutor.execute(new GetListOfFriendsOfThread(com, cont, stringqueue, user));
		preexecutor.shutdown();
		try {
			while (!preexecutor.awaitTermination(4L, TimeUnit.SECONDS)) {
				if (FacePath.DEBUG){
					System.out.println("Still waiting for the preexecutor to finish");
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		List<Callable<Object>> apiRequests = new ArrayList<Callable<Object>>();
		ExecutorService executor = Executors.newFixedThreadPool(4);
		// create the fbps for each friend
		for( String userstring : stringqueue) {
			// if the profile has already been requested
			if(knownProfiles.containsKey(userstring)) {
				// get it from the known profiles collection
				queue.add(knownProfiles.get(userstring));
				if (FacePath.DEBUG){
			    	System.out.println("GFOT-added-from-collection: "+userstring);
			    }
			} else {
				// create the new profile
				apiRequests.add(Executors.callable(new GetUserFromAPIThread(ApiKey, queue, userstring)));
				if (FacePath.DEBUG){
			    	System.out.println("GFOT-created-API-request-for: "+userstring);
			    }
			}
		}
		
		try {
			executor.invokeAll(apiRequests);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
		
		try {
			while (!executor.awaitTermination(4L, TimeUnit.SECONDS)) {
				if (FacePath.DEBUG){
					System.out.println("Still waiting for the executor to finish");
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		fN.addVertice(user);
		for(FacebookProfile f : queue) {
			fN.addVertice(f);
			fN.addEdge(user, f);
		}
	}
}
