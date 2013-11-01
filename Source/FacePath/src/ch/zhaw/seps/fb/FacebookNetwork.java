package ch.zhaw.seps.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
    
    public void addVertice(String userID, FacebookProfile user){
    	if(!this.graphCollection.containsKey(userID)) {
    		this.graph.addNode(userID);
        	this.graphCollection.put(userID, user);
    	}
    }
    
    public void addEdge(String name, String sourceID, String targetID){
    	try {
    		this.graph.addEdge(name, sourceID, targetID);
    	} catch(IdAlreadyInUseException | ElementNotFoundException | EdgeRejectedException err) {
    		err.printStackTrace();
    	}
    	
    }
    
    public Graph getGraph() {
    	return this.graph;
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
