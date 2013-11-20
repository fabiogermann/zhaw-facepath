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
	private FacebookProfile me;
	
	private FacebookProfile source = null;
	private FacebookProfile target = null;
	private Stack<FacebookProfile> workStack = new Stack<FacebookProfile>();
	
	public FacebookSearch(FacebookProvider provider) {
		this.fbProvider = provider;
		this.fbNetwork = new FacebookNetwork();
		this.me = fbProvider.getMyProfile();
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
		    //DEBUG
		    if (FacePath.DEBUG){
		    	System.out.println(me.getUserID()+"--"+item.getUserID());
		    }
		    this.workStack.add(item);
		}
	}
	
	private void submitProfileToNetwork(FacebookProfile fp) {
		this.fbNetwork.addVertice(fp);
	}
	
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
		//Collection<String> buddylist = null;
		Collection<FacebookProfile> friendlist = null;
		
		while(!workStack.empty()) {
			tlist.add(workStack.pop());
		}
		friendlist = fbProvider.getFriendsOfThreaded(tlist, this.fbNetwork);
		//friendlist = fbProvider.getUserFromThreadedAPI(buddylist);
		workStack.addAll(friendlist);
		
		for(FacebookProfile f2 : friendlist) {
			System.out.println(">"+f2.getUserID());
			for(FacebookProfile fp : f2.getFriends()) {
						System.out.println("-----"+fp.getUserID());
			}
		}
		
	}
	
	public boolean pathFound() {
		return false;
		//TODO
	}
}
