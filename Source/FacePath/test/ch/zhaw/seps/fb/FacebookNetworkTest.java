/**
 * JUnit Test für ch.zhaw.seps.fb.FacebookNetwork
 */
package ch.zhaw.seps.fb;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author SEPS Gruppe 2
 * 
 */
public class FacebookNetworkTest {

	private FacebookNetwork fbNetwork;
	private FacebookProfile user1;
	private FacebookProfile user2;
	private FacebookProfile user3;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.fbNetwork = new FacebookNetwork();
		this.user1 = new FacebookProfile("max.path.31", "100006897191497");
		this.user2 = new FacebookProfile("peter.birrer", "100001982837335");
		this.user3 = new FacebookProfile("peter.meier.984", "1547788305");
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#addVertice(ch.zhaw.seps.fb.FacebookProfile)}
	 * .
	 */
	@Test
	public void testAddVertice() {
		this.fbNetwork.addVertice(this.user1);
		Assert.assertTrue(this.fbNetwork.getKnownProfiles().containsValue(this.user1));
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#removeVertice(org.graphstream.graph.Node)}
	 * .
	 */
	@Test
	public void testRemoveVertice() {
		this.fbNetwork.addVertice(this.user1);
		// Assert.assertTrue(this.fbNetwork.getKnownProfiles().containsValue(user));

		Node n = this.fbNetwork.getGraph().getNode(this.user1.getUserID());
		this.fbNetwork.removeVertice(n);
		Assert.assertTrue(!this.fbNetwork.getKnownProfiles().containsValue(this.user1));
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#addEdge(ch.zhaw.seps.fb.FacebookProfile, ch.zhaw.seps.fb.FacebookProfile)}
	 * .
	 */
	@Test
	public void testAddEdge() {
		this.fbNetwork.addVertice(this.user1);
		this.fbNetwork.addVertice(this.user2);
		this.fbNetwork.addEdge(this.user1, this.user2);

		Edge edge = this.fbNetwork.getGraph().getEdge(this.user1.getUserID() + "--" + this.user2.getUserID());

		Assert.assertNotNull(edge);
	}

	/**
	 * Test method for {@link ch.zhaw.seps.fb.FacebookNetwork#getGraph()}.
	 */
	@Test
	public void testGetGraph() {
		Assert.assertNotNull(this.fbNetwork.getGraph());
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#getKnownProfiles()}.
	 */
	@Test
	public void testGetKnownProfiles() {
		this.fbNetwork.addVertice(this.user1);
		this.fbNetwork.addVertice(this.user2);

		Assert.assertTrue(this.fbNetwork.getKnownProfiles().containsValue(this.user1)
		        && this.fbNetwork.getKnownProfiles().containsValue(this.user2));
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#getDijkstraPaths()}.
	 */
	@Test
	public void testGetDijkstraPaths() {
		Assert.assertNotNull(this.fbNetwork.getDijkstraPaths());
	}

	/**
	 * Test method for {@link ch.zhaw.seps.fb.FacebookNetwork#pathFound()}.
	 */
	@Test
	public void testPathFound() {
		Assert.assertTrue(!this.fbNetwork.pathFound());
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#findShortestPath(ch.zhaw.seps.fb.FacebookProfile, ch.zhaw.seps.fb.FacebookProfile)}
	 * .
	 */
	@Test
	public void testFindShortestPath() {
		this.fbNetwork.addVertice(this.user1);
		this.fbNetwork.addVertice(this.user2);
		this.fbNetwork.addEdge(this.user1, this.user2);

		List<Node> path = new ArrayList<Node>();
		Node n1 = this.fbNetwork.getGraph().getNode(this.user1.getUserID());
		Node n2 = this.fbNetwork.getGraph().getNode(this.user2.getUserID());
		path.add(n1);
		path.add(n2);
		ArrayList<List<Node>> dijkstraPaths = new ArrayList<List<Node>>();
		dijkstraPaths.add(path);

		this.fbNetwork.findShortestPath(this.user1, this.user2);

		boolean pathFound = this.fbNetwork.pathFound();
		boolean sizeOne = this.fbNetwork.getDijkstraPaths().size() == 1;
		boolean node1 = this.user1.getUserID() == n1.getId();
		boolean node2 = this.user2.getUserID() == n2.getId();

		Assert.assertTrue(pathFound && sizeOne && node1 && node2);
	}

	/**
	 * Test method for {@link ch.zhaw.seps.fb.FacebookNetwork#cleanupGraph()}.
	 */
	@Test
	public void testCleanupGraph() {
		this.fbNetwork.addVertice(this.user1);
		this.fbNetwork.addVertice(this.user2);
		this.fbNetwork.addVertice(this.user3);
		this.fbNetwork.addEdge(this.user1, this.user2);
		this.fbNetwork.addEdge(this.user1, this.user3);

		this.fbNetwork.findShortestPath(this.user1, this.user2);
		this.fbNetwork.setGraphViewer(this.fbNetwork.getGraph().display());
		this.fbNetwork.cleanupGraph();

		Node n1 = null;
		Node n2 = null;
		Node n3 = null;
		try {
			n1 = this.fbNetwork.getGraph().getNode(this.user1.getUserID());
			n2 = this.fbNetwork.getGraph().getNode(this.user2.getUserID());
			n3 = this.fbNetwork.getGraph().getNode(this.user3.getUserID());
		} catch (NullPointerException e) {
		}
		Assert.assertTrue(n1 != null && n2 != null && n3 == null);
	}

	/**
	 * Test method for {@link ch.zhaw.seps.fb.FacebookNetwork#styleGraph()}.
	 */
	@Test
	public void testStyleGraph() {
		this.fbNetwork.setGraphViewer(this.fbNetwork.getGraph().display());
		this.fbNetwork.styleGraph();
	}

	/**
	 * Test method for {@link ch.zhaw.seps.fb.FacebookNetwork#getGraphViewer()}.
	 */
	@Test
	public void testGetGraphViewer() {
		this.fbNetwork.setGraphViewer(this.fbNetwork.getGraph().display());
		Assert.assertNotNull(this.fbNetwork.getGraphViewer());
	}

	/**
	 * Test method for
	 * {@link ch.zhaw.seps.fb.FacebookNetwork#setGraphViewer(org.graphstream.ui.swingViewer.Viewer)}
	 * .
	 */
	@Test
	public void testSetGraphViewer() {
		this.fbNetwork.setGraphViewer(this.fbNetwork.getGraph().display());
		Assert.assertNotNull(this.fbNetwork.getGraphViewer());
	}

}