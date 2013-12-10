/**
 * Verwaltet die Suche
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

import java.util.Collection;
import java.util.Stack;

import org.graphstream.graph.Graph;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.view.SearchView;

public class FacebookSearch implements Runnable {

	private FacebookProvider fbProvider;
	private FacebookNetwork fbNetwork;

	private FacebookProfile source = null;
	private FacebookProfile target = null;
	private Stack<FacebookProfile> workStack = new Stack<FacebookProfile>();

	private boolean onlyLocal = false;
	private boolean withPictures = false;
	private boolean withLikes = false;
	private boolean withEvents = false;
	private boolean withAllFriends = false;

	/**
	 * Konstruktor
	 * Legt den Provider fest und instanziert das Netzwerk
	 * @param 	provider		Facebook Provider
	 */
	public FacebookSearch(FacebookProvider provider) {
		this.fbProvider = provider;
		this.fbNetwork = new FacebookNetwork();
	}

	/**
	 * Legt die von der Searchview übergebenen Suchoptionen fest
	 * @param 		lo		Nur nationale Suche
	 * @param 		pi		Profilbilder anzeigen
	 * @param 		li		Like-Pages miteinbeziehen
	 * @param 		ev		Event-Pages miteinbeziehen
	 */
	public void setOptions(boolean lo, boolean pi, boolean li, boolean ev,boolean af) {
		this.onlyLocal = lo;
		this.withPictures = pi;
		this.withLikes = li;
		this.withEvents = ev;
		this.withAllFriends = af;
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
		boolean searchValid = true;
		
		// if we found the connection - no need to look further
		while (!this.pathFound() && searchValid) {
			if (FacePath.DEBUG >= 1) {
				System.out.println("We are at iteration: " + iteration);
			}
			this.searchIterate();
			iteration++;
			
			if (iteration >= 5) {
				SearchView.notifyNoConnectionFound();
				searchValid = false;
			}
		}

		if (pathFound()) {
			if (FacePath.DEBUG >= 1) {
				System.out.println("Path found");
			}
			getFbNetwork().cleanupGraph();
			getFbNetwork().styleGraph();
		}
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
		friendlist = fbProvider.getFriendsOfThreaded(todo, this.getFbNetwork(), this.withAllFriends);
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
