package ch.zhaw.seps.fb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
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
    
    private boolean pathFound = false;
    private ArrayList<List<Node>> dijkstraPaths = new ArrayList<>();
    
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
    		this.graph.addEdge(source.getUserID()+"--"+destination.getUserID(), source.getUserID(), destination.getUserID());
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

	public ArrayList<List<Node>> getDijkstraPaths() {
		return dijkstraPaths;
	}

	public boolean pathFound() {
		return pathFound;
	}
	
	public void findShortestPath(FacebookProfile source, FacebookProfile target) {
		dijkstraPaths.clear();
		Node sourceNode = this.graph.getNode(source.getUserID());
		Node targetNode = this.graph.getNode(target.getUserID());
		Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, null, null);
		d = new Dijkstra();
		d.init(this.graph);
		d.setSource(sourceNode);
		d.compute();
		Iterator<Path> paths = d.getAllPaths(targetNode).iterator();
		while (paths.hasNext()) {
			Path path = (Path) paths.next();
			dijkstraPaths.add((List<Node>) path.getNodePath());
		}
		System.out.println("search path from "+sourceNode.getId()+" to "+targetNode.getId());
		
		ArrayList<String> colors = getColorList();
		int pathnr = 0;
		for (List<Node> l : dijkstraPaths) {
			Node previousNode=null;
			for (Node n : l) {
				n.addAttribute("ui.style", colors.get(pathnr%colors.size()));
				if (previousNode!=null) {
					n.getEdgeBetween(previousNode).addAttribute("ui.style", colors.get(pathnr%colors.size()));
				}
				previousNode = n;
			}
			pathnr++;
		}
		if (dijkstraPaths.size()==0) {
			this.pathFound = true;
		}
	}
	
	private ArrayList<String> getColorList() {
		ArrayList<String> colors = new ArrayList<>();
		colors.add("fill-color: goldenrod;");
		colors.add("fill-color: red;");
		colors.add("fill-color: green;");
		colors.add("fill-color: blue;");
		colors.add("fill-color: fuchsia;");
		return colors;
	}
}
