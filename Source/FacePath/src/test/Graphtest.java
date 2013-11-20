package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;

public class Graphtest {
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

	public static void main(String[] args) {
		Graph g = exampleGraph();
		g.display(false);

		findShortestPath(g, g.getNode(start), g.getNode(ziel));
	}

	public static void findShortestPath(Graph g, Node source, Node dest) {
		ArrayList<List<Node>> nodes = new ArrayList<>();
		int pathCount=0;
		Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, null, null);
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
		
		ArrayList<String> colors = new ArrayList<>();
		colors.add("fill-color: goldenrod;");
		colors.add("fill-color: red;");
		colors.add("fill-color: green;");
		colors.add("fill-color: blue;");
		colors.add("fill-color: fuchsia;");
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
}
