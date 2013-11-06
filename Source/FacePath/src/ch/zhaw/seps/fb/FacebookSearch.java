package ch.zhaw.seps.fb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
		//this.searchIterate();
	}
	
	private void initializeNetwork(FacebookProfile me, List<FacebookProfile> friends) {
		// add myself to the graph
		fbNetwork.addVertice(me.getUserID(), me);
		
		//add my friends to the graph
		for(Iterator<FacebookProfile> i = friends.iterator(); i.hasNext(); ) {
		    FacebookProfile item = i.next();
		    fbNetwork.addVertice(item.getUserID(), item);
		    fbNetwork.addEdge(me.getUserID()+"-to-"+item.getUserID(), me.getUserID(), item.getUserID());
		    //DEBUG
		    if (FacePath.DEBUG){
		    	System.out.println(me.getUserID()+"-to-"+item.getUserID());
		    }
		    this.workStack.add(item);
		}
	}
	
	private void submitProfileToNetwork(FacebookProfile fp) {
		this.fbNetwork.addVertice(fp.getUserID(), fp);
	}
	
	private void submitConnectionToNetwork(FacebookProfile from, FacebookProfile to) {
		this.fbNetwork.addVertice(to.getUserID(), to);
	    this.fbNetwork.addEdge(from.getUserID()+"-to-"+to.getUserID(), from.getUserID(), to.getUserID());
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
		Collection<String> buddylist = null;
		Collection<FacebookProfile> friendlist = null;
		
		while(!workStack.empty()) {
			tlist.add(workStack.pop());
		}
		
		buddylist = fbProvider.getFriendsOfThreaded(tlist);
		friendlist = fbProvider.getUserFromThreadedAPI(buddylist);
		workStack.addAll(friendlist);
		
		for(FacebookProfile f2 : tlist) {
			for(FacebookProfile fp : friendlist) {
				for(String s : f2.getCandidates()) {
					if(fp.getUserUIDString().contentEquals(s)) {
						System.out.println("Foooooooooooooo");
					}
				}
				
				if(f2.getCandidates().contains(fp.getUserUIDString())) {
					submitProfileToNetwork(fp);
					submitConnectionToNetwork(f2, fp);
				}
			}
		}
		
	}
	
	public boolean pathFound() {
		return false;
		//TODO
	}
}
