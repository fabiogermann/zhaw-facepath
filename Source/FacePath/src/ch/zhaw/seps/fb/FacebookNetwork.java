package ch.zhaw.seps.fb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;

import ch.zhaw.seps.TreeNode;

public class FacebookNetwork {
	
	// THE EASY WAY
	private Graph graph;
	private HashMap<String,FacebookProfile> graphCollection;
	public Lock lockObject;
	
	// THE HARD WAY
	// Datastructure for the simulation of the facebook network
	
    private TreeNode<FacebookProfile> root;
    private TreeNode<FacebookProfile> currentRootChild;
    private Stack<TreeNode<FacebookProfile>> rootStack;
    private List<TreeNode<FacebookProfile>> rootChildren;
    private HashMap<String,FacebookProfile> rootDatacollection;
    
    private TreeNode<FacebookProfile> target;
    private TreeNode<FacebookProfile> currentTargetChild;
    private Stack<TreeNode<FacebookProfile>> targetStack;
    private List<TreeNode<FacebookProfile>> targetChildren;
    private HashMap<String,FacebookProfile> targetDatacollection;
    
    public FacebookNetwork() {
    	// the easy way
    	this.graph = new SingleGraph("FacebookNetwork");
    	this.graphCollection = new HashMap<String, FacebookProfile>();
    	
    	//the hard way
    	this.rootStack = new Stack<TreeNode<FacebookProfile>>();
    	this.targetStack = new Stack<TreeNode<FacebookProfile>>();
    }
    
    public synchronized void addVertice(FacebookProfile user){
    	if(!this.graphCollection.containsKey(user.getUserID())) {
    		this.graph.addNode(user.getUserID());
        	this.graphCollection.put(user.getUserID(), user);
    	}
    }
    
    public synchronized void addEdge(FacebookProfile source, FacebookProfile destination){
    	try {
    		this.graph.addEdge(source.getUserID()+"-to-"+destination.getUserID(), source.getUserID(), destination.getUserID());
    	} catch(IdAlreadyInUseException | ElementNotFoundException | EdgeRejectedException err) {
    		err.printStackTrace();
    	}
    	
    }
    
    public Graph getGraph() {
    	return this.graph;
    }
    
    public Map<String, FacebookProfile> getKnownProfiles() {
    	return Collections.unmodifiableMap(this.graphCollection);
    }
    
    public void addToRootStack(FacebookProfile fp) {
    	this.rootStack.add(new TreeNode<FacebookProfile>(fp.getUserID(), fp));
    }
    
    public void setRoot() {
    	// TODO Auto-generated method stub
    }
    
    public void setTarget() {
    	// TODO Auto-generated method stub
    }
    
    public void getRoot() {
    	// TODO Auto-generated method stub
    }
    
    public void getTarget() {
    	// TODO Auto-generated method stub
    }
    
    public void insertItem() {
		// TODO Auto-generated method stub
	}
    
    public void getItem() {
		// TODO Auto-generated method stub
	}

	public FacebookProfile getRootStack() {
		if(!this.rootStack.isEmpty()) {
			return this.rootStack.pop().getData();
		} else {
			return null;
		}
	}

}
