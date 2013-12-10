/**
 * Verwaltet die Suche
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

import java.util.Collection;
import java.util.Stack;

import javax.swing.JOptionPane;

import org.graphstream.graph.Graph;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.view.SearchView;

public class FacebookSearch implements Runnable {

	private FacebookProvider fbProvider;
	private FacebookNetwork fbNetwork;
	// TODO only for testing
	private FacebookProfile me;

	private FacebookProfile source = null;
	private FacebookProfile target = null;
	private Stack<FacebookProfile> workStack = new Stack<FacebookProfile>();

	// options
	private boolean onlyLocal = false; // locality of startuser must be equal to
	                                   // the targetuser !!! check that
	private boolean withPictures = false;
	private boolean withLikes = false;
	private boolean withEvents = false;

	/**
	 * Konstruktor
	 * Legt den Provider fest und instanziert das Netzwerk
	 * @param 	provider		Facebook Provider
	 */
	public FacebookSearch(FacebookProvider provider) {
		this.fbProvider = provider;
		this.fbNetwork = new FacebookNetwork();
		// TODO only for testing purpses:
		// this.me = fbProvider.getMyProfile();
		// this too
		// this.source = me;
		// and this too
		// this.initializeNetwork(me, fbProvider.getMyFriends());
	}

	/*
	 * private void initializeNetwork(FacebookProfile me, List<FacebookProfile>
	 * friends) { // add myself to the graph fbNetwork.addVertice(me);
	 * 
	 * //add my friends to the graph for(Iterator<FacebookProfile> i =
	 * friends.iterator(); i.hasNext(); ) { FacebookProfile item = i.next();
	 * fbNetwork.addVertice(item); fbNetwork.addEdge(me, item);
	 * 
	 * if (FacePath.DEBUG){
	 * System.out.println(me.getUserID()+"--"+item.getUserID()); }
	 * this.workStack.add(item); } }
	 */

	/**
	 * Legt die von der Searchview übergebenen Suchoptionen fest
	 * @param 		lo		Nur nationale Suche
	 * @param 		pi		Profilbilder anzeigen
	 * @param 		li		Like-Pages miteinbeziehen
	 * @param 		ev		Event-Pages miteinbeziehen
	 */
	public void setOptions(boolean lo, boolean pi, boolean li, boolean ev) {
		this.onlyLocal = lo;
		this.withPictures = pi;
		this.withLikes = li;
		this.withEvents = ev;
	}

	@Deprecated
	private void submitProfileToNetwork(FacebookProfile fp) {
		this.getFbNetwork().addVertice(fp);
	}

	@Deprecated
	private void submitConnectionToNetwork(FacebookProfile from, FacebookProfile to) {
		this.getFbNetwork().addVertice(to);
		this.getFbNetwork().addEdge(from, to);
	}

	public Graph getGraph() {
		return this.getFbNetwork().getGraph();
	}

	/**
	 * Legt Startprofil für die Suche fest
	 * @param 		fp		Startprofil
	 */
	public void setPersonOfInterestSource(FacebookProfile fp) {
		this.source = fp;
		this.getFbNetwork().addVertice(fp);
		if (FacePath.DEBUG >= 1) {
			System.out.println("Start User: " + fp.getUserUIDString());
		}
	}

	/**
	 * Legt Zielprofil für die Suche fest
	 * @param 		fp		Zielprofil
	 */
	public void setPersonOfInterestDestination(FacebookProfile fp) {
		this.target = fp;
		this.getFbNetwork().addVertice(fp);
		if (FacePath.DEBUG >= 1) {
			System.out.println("End User: " + fp.getUserUIDString());
		}
	}

	/**
	 * Führt die Suche aus
	 * @throws FacebookPrivateProfileException 
	 */
	public void searchExecute() throws FacebookPrivateProfileException {
		// prepare to start the search
		this.searchInitiate();
		// search until path is found
		// do it once to set up the network
		this.searchIterate();
		
		if (this.source.getFriends().isEmpty() & this.target.getFriends().isEmpty()) {
			throw new FacebookPrivateProfileException();
		}
		int iteration = 1;
		
		if (iteration >= 5) {
			SearchView.notifyNoConnectionFound();
			return;
		}
		// if we found the connection - no need to look further
		while (!this.pathFound()) {
			// TODO abbruchkriterium
			System.out.println("we are at iteration: " + iteration);
			this.searchIterate();
			iteration++;
		}

		if (pathFound()) {
			System.out.println("gefunden");
			getFbNetwork().cleanupGraph();
			getFbNetwork().styleGraph();
		}
		this.fbProvider.getLikesForUser(source);
	}

	/**
	 * Teil der Suche, die eine Iteration durcharbeitet.
	 * Eine Iteration sucht die Freunde eines gefundenen/angegebenen Profils
	 */
	public void searchIterate() {
		Stack<FacebookProfile> todo = new Stack<FacebookProfile>();
		// copy all friends to work on to temporary stack
		todo.addAll(this.workStack);
		// clear the global workstack to allow addition of future results
		// without mixing with the current iteration
		this.workStack.removeAllElements();

		Collection<FacebookProfile> friendlist = null;
		// get all the friends of the friends in the temporary stack
		// the friends and connections are added directly to the FacebookNetwork
		// and to the graph
		friendlist = fbProvider.getFriendsOfThreaded(todo, this.getFbNetwork());
		// add the found profiles to the workstack
		for (FacebookProfile fp : friendlist) {
			this.workStack.addAll(fp.getFriends());
		}
		this.filterWorkstack();
	}
	
	private void filterWorkstack() {
		if ( this.onlyLocal ) {
			for( FacebookProfile f : this.workStack) {
				if ( (f.getLocation() != this.source.getLocation()) | (f.getLocation() != this.target.getLocation()) ) {
					this.workStack.removeElement(f);
				}
			}
		}
		
	}

	/**
	 * Teil der Suche, setzt die Suchparameter
	 */
	public void searchInitiate() {
		this.workStack.add(this.source);
		this.workStack.add(this.target);
	}

	/**
	 * Lässt im Graphen den Weg ermitteln und meldet, ob ein Ergebnis (Verbindung) gefunden wurde
	 * @return	Meldet, ob ein Ergebnis gefunden wurde
	 */
	public boolean pathFound() {
		this.getFbNetwork().findShortestPath(this.source, this.target);
		return this.getFbNetwork().pathFound();
	}

	@Override
	public void run() {
		try {
			this.searchExecute();
		} catch (FacebookPrivateProfileException e) {
			SearchView.notifyPrivateProfile();
			e.printStackTrace();
		}
	}

	public FacebookNetwork getFbNetwork() {
		return fbNetwork;
	}
}
