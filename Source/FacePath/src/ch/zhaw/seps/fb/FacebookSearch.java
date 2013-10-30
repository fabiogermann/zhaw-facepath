package ch.zhaw.seps.fb;

import java.util.Iterator;
import java.util.List;

import org.graphstream.ui.swingViewer.Viewer;

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
		    System.out.println(me.getUserID()+"-to-"+item.getUserID());
		}
	}
	
	public Viewer displayGraph() {
		return this.fbNetwork.getGraph().display();
	}

	public void setPersonOfInterestSource() {
		//TODO -> in Facebook
	}
	
	public void setPersonOfInterestDestination() {
		//TODO
	}
	
	public void searchIterate() {
		//TODO
	}
	
	public boolean pathFound() {
		return false;
		//TODO
	}
}
