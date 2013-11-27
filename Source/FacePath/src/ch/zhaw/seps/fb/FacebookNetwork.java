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
	private Graph graph;
	private HashMap<String,FacebookProfile> graphCollection;
    private boolean pathFound = false;
    private ArrayList<List<Node>> dijkstraPaths = new ArrayList<>();
    private ArrayList<String> colors;
    
    public FacebookNetwork() {
    	this.graph = new SingleGraph("FacebookNetwork");
    	this.graphCollection = new HashMap<String, FacebookProfile>();
    	this.colors = getColorList();
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
		String colornames = "gold,blue,brown,cyan,darkblue,darkcyan,darkgrey,darkgreen,darkkhaki,darkmagenta,darkolivegreen,darkorange,darkorchid,darkred,darksalmon,darkviolet,fuchsia,green,indigo,khaki,lightblue,lightgreen,lightgrey,lightpink,lime,magenta";
		String[] colornamesArray = colornames.split(",");
		for (int i = 0; i < colornamesArray.length; i++) {
			colors.add("fill-color: "+colornamesArray[i]+";");
		}
		return colors;
	}
	
	public void cleanupGraph() {
		for(Node n : this.graph) {
			if(n.getDegree() < 2) {
				this.graph.removeNode(n);
			}
		}
		// TODO the magic
	}
}
