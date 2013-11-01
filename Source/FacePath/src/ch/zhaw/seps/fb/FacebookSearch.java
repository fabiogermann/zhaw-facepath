package ch.zhaw.seps.fb;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;

import ch.zhaw.seps.FacePath;

import com.restfb.types.User;

public class FacebookSearch {
	
	private FacebookProvider fbProvider;
	private FacebookNetwork	fbNetwork;
	private FacebookProfile me;
	
	public FacebookSearch(FacebookProvider provider) {
		this.fbProvider = provider;
		this.fbNetwork = new FacebookNetwork();
		this.me = fbProvider.getMyProfile();
		this.initializeNetwork(me, fbProvider.getMyFriends());
		this.searchIterate();
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
		    fbNetwork.addToRootStack(item);
		}
	}
	
	public Graph getGraph() {
		return this.fbNetwork.getGraph();
	}

	public void setPersonOfInterestSource() {
		//TODO -> in Facebook
	}
	
	public void setPersonOfInterestDestination() {
		//TODO
	}
	
	public void searchIterate() {
		while(fbNetwork.getRootStack() != null) {
			FacebookProfile tmp = fbNetwork.getRootStack();
			List<FacebookProfile> tlist = null;
			try {
				tlist = fbProvider.getFriendsOf(tmp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(FacebookProfile user : tlist) {
				fbNetwork.addVertice(user.getUserID(), user);
				fbNetwork.addEdge(tmp.getUserID()+"-to-"+user.getUserID(), tmp.getUserID(), user.getUserID());
			}
		}
	}
	
	public boolean pathFound() {
		return false;
		//TODO
	}
}
