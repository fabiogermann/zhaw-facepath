package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;

public class Graphtest implements ViewerListener{
	
	Graph g = exampleGraph();
	Dijkstra d;
	ArrayList<List<Node>> nodes;
	// B---9--E
	// /| |
	// / | |
	// / | |
	// 14 2 6
	// / | |
	// / | |
	// A---9--C--11--F
	// \ | /
	// \ | /
	// 7 10 15
	// \ | /
	// \ | /
	// \|/
	// D G

	private static String start = "A", ziel = "F";

	public static Graph exampleGraph() {
		Graph g = new SingleGraph("example");
		g.addNode("A").addAttribute("xy", 0, 1);
		g.addNode("B").addAttribute("xy", 1, 2);
		g.addNode("C").addAttribute("xy", 1, 1);
		g.addNode("D").addAttribute("xy", 1, 0);
		g.addNode("E").addAttribute("xy", 2, 2);
		g.addNode("F").addAttribute("xy", 2, 1);
		g.addNode("G").addAttribute("xy", 2, 0);
		g.addEdge("AB", "A", "B");
		g.addEdge("AC", "A", "C");
		g.addEdge("AD", "A", "D");
		g.addEdge("BC", "B", "C");
		g.addEdge("BF", "B", "F");
		g.addEdge("CD", "C", "D");
		g.addEdge("BE", "B", "E");
		g.addEdge("CF", "C", "F");
		g.addEdge("DF", "D", "F");
		g.addEdge("EF", "E", "F");
		for (Node n : g)
			n.addAttribute("label", n.getId());
		return g;
	}
	
	boolean loop = true;
	
	public Graphtest() {
		findShortestPath(g, g.getNode(start), g.getNode(ziel));
		cleanupGraph();
		
        Viewer viewer = g.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(g);
        
        	
        
        while(loop) {
            fromViewer.pump();
        }

	}

	public static void main(String[] args) {
		
		Graphtest t = new Graphtest();
	}

	public void findShortestPath(Graph g, Node source, Node dest) {
		nodes = new ArrayList<>();
		int pathCount=0;
		d = new Dijkstra(Dijkstra.Element.EDGE, null, null);
		d = new Dijkstra();
		d.init(g);
		if (source == null) {
			System.out.println("istnull");
		}
		d.setSource(source);
		d.compute();
		Iterator<Path> paths = d.getAllPaths(dest).iterator();
		while (paths.hasNext()) {
			Path path = (Path) paths.next();
			//Stack s = path.getNodePath();
			nodes.add((List<Node>) path.getNodePath());
			pathCount++;
		}
		
		System.out.println("--------- node ausgabe");
		System.out.println("search path from "+source.getId()+" to "+dest.getId());
		
		ArrayList<String> colors = getColorList();
		int pathnr = 0;
		for (List<Node> l : nodes) {
			Node previousNode=null;
			for (Node n : l) {
				n.addAttribute("ui.style", colors.get(pathnr%3));
				if (previousNode!=null) {
					n.getEdgeBetween(previousNode).addAttribute("ui.style", colors.get(pathnr%3));
				}
				previousNode = n;
			}
			pathnr++;
		}
		if (nodes.size()==0) {
			System.out.println("no connection found");
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
	
	private void cleanupGraph () {
		LinkedList<Node> nodesToDelete = new LinkedList<>();
		ArrayList<Node> shortestPathnodes = new ArrayList<>();
		for(List<Node> l:nodes) {
			shortestPathnodes.addAll(l);
		}
		for (Node n:g.getNodeSet()) {
			int edgeCounter = 0;
			for(Edge n2:n.getEachEdge()) {
				edgeCounter++;
				if(edgeCounter>1) {
					break;
				}
			}
			if (edgeCounter<=1) {
				nodesToDelete.add(n);
			} else if (!shortestPathnodes.contains(n)) {
				nodesToDelete.add(n);
			}
		}
		shortestPathnodes.get(0).addAttribute("ui.style", "fill-mode:image-scaled-ratio-max;fill-image: url('src/test/i.png');");
		for (Node n:nodesToDelete) {
			g.removeNode(n);//needs cleaner delete
		}
		styleGraph();
	}
	
	private void styleGraph() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		g.removeAttribute("ui.stylesheet");
		g.addAttribute("ui.stylesheet", 
				"graph {fill-color:white;}" +
				"edge {shape:freeplane;size:2px;}" +
				"node {size:30px; shape:rounded-box;}");
	}

	public void viewClosed(String id) {}
 
    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);
    }
 
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }
}
