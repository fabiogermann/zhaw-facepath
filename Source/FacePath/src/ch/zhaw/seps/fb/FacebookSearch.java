package ch.zhaw.seps.fb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.http.client.ClientProtocolException;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.TreeNode;

import com.restfb.types.User;

public class FacebookSearch {
	
	private FacebookProvider fbProvider;
	private FacebookNetwork	fbNetwork;
	// TODO only for testing
	private FacebookProfile me;
	
	private FacebookProfile source = null;
	private FacebookProfile target = null;
	private Stack<FacebookProfile> workStack = new Stack<FacebookProfile>();
	
	public FacebookSearch(FacebookProvider provider) {
		this.fbProvider = provider;
		this.fbNetwork = new FacebookNetwork();
		// TODO only for testing purpses:
		this.me = fbProvider.getMyProfile();
		// this too
		this.source = me;
		// and this too
		this.initializeNetwork(me, fbProvider.getMyFriends());
	}
	
	private void initializeNetwork(FacebookProfile me, List<FacebookProfile> friends) {
		// add myself to the graph
		fbNetwork.addVertice(me);
		
		//add my friends to the graph
		for(Iterator<FacebookProfile> i = friends.iterator(); i.hasNext(); ) {
		    FacebookProfile item = i.next();
		    fbNetwork.addVertice(item);
		    fbNetwork.addEdge(me, item);

		    if (FacePath.DEBUG){
		    	System.out.println(me.getUserID()+"--"+item.getUserID());
		    }
		    this.workStack.add(item);
		}
	}
	
	@Deprecated
	private void submitProfileToNetwork(FacebookProfile fp) {
		this.fbNetwork.addVertice(fp);
	}
	
	@Deprecated
	private void submitConnectionToNetwork(FacebookProfile from, FacebookProfile to) {
		this.fbNetwork.addVertice(to);
	    this.fbNetwork.addEdge(from, to);
	}
	
	public Graph getGraph() {
		return this.fbNetwork.getGraph();
	}

	public void setPersonOfInterestSource(FacebookProfile fp) {
		this.source = fp;
	}
	
	public void setPersonOfInterestDestination(FacebookProfile fp) {
		this.target = fp;
	}
	
	public void searchIterate() {

		List<FacebookProfile> tlist = new ArrayList<FacebookProfile>();
		Collection<FacebookProfile> friendlist = null;
		
		//TODO dies schöner
		while(!workStack.empty()) {
			tlist.add(workStack.pop());
		}
		
		friendlist = fbProvider.getFriendsOfThreaded(tlist, this.fbNetwork);
		workStack.addAll(friendlist);
		for(FacebookProfile f2 : friendlist) {
			System.out.println(">"+f2.getUserID());
			this.target = f2; // for debug
			for(FacebookProfile fp : f2.getFriends()) {
						System.out.println("-----"+fp.getUserID());
			}
		}
		if (pathFound()) {
			this.fbNetwork.cleanupGraph();
			System.out.println("gefunden");
			//stop
		} else {
			//continue
		}
	}
	
	public void searchExecute() {
		// prepare to start the search
		this.searchInitiate();
		// search until path is found
		// TODO abbruchkriterium
		while(!this.pathFound()) {
			this.searchIterateNEW();
		}
		// TODO graph cleanup
		//this.fbNetwork.cleanupGraph();
	}
	
	public void searchIterateNEW() {
		Stack<FacebookProfile> todo = new Stack<FacebookProfile>();
		// copy all friends to work on to temporary stack
		todo.addAll(this.workStack);
		// clear the global workstack to allow addition of future results without mixing with the current iteration
		this.workStack.removeAllElements();
		
		Collection<FacebookProfile> friendlist = null;
		// get all the friends of the friends in the temporary stack
		// the friends and connections are added directly to the FacebookNetwork and to the graph
		friendlist = fbProvider.getFriendsOfThreaded(todo, this.fbNetwork);
		// add the found profiles to the workstack
		this.workStack.addAll(friendlist);
	}
	
	public void searchInitiate() {
		this.workStack.add(this.source);
		this.workStack.add(this.target);
	}
	
	public boolean pathFound() {
		this.fbNetwork.findShortestPath(source, target);
		return this.fbNetwork.pathFound();
	}
}
