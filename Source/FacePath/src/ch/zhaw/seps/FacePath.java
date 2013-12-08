/**
 * Startpunkt der Anwendung
 * Initiiert und ruft das GUI auf
 * 
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.zhaw.seps.fb.FacebookProvider;
import ch.zhaw.seps.fb.FacebookSearch;
import ch.zhaw.seps.view.GraphView;
import ch.zhaw.seps.view.LoginView;
import ch.zhaw.seps.view.LogoutView;
import ch.zhaw.seps.view.SearchView;

public class FacePath extends JFrame implements ActionListener, WindowListener {

	// set the DEBUG mode and verbosity level: 
	//		0 = OFF, 
	//		1 = Programm Status,
	//		2 = Detailed Programm Status,
	//		3 = Detailed Programm Status With Treads,
	//		4 = Detailed Programm Status With Detailed Treads
	
	public static final int DEBUG = 4;

	public static void main(String[] args) {
		new FacePath();
	}

	private Map<String, JPanel> viewMap = new HashMap<String, JPanel>();
	private FacebookProvider myFB = null;
	private FacebookSearch myFS = null;

	public void setFP(FacebookProvider fp) {
		this.myFB = fp;
	}

	public void setFS(FacebookSearch fs) {
		this.myFS = fs;
	}

	public FacebookProvider getFP() {
		return myFB;
	}

	public FacebookSearch getFS() {
		return myFS;
	}

	
	/**
	 * Zeigt einen Bereich des GUIs an
	 * @param		view	Name des Bereichs, der angezeigt werden soll
	 */
	public void showView(String view) {
		for (String viewString : this.viewMap.keySet()) {
			this.remove(this.viewMap.get(viewString));
		}
		viewMap.clear();
		if (view.equals("login")) {
			LoginView loginView = new LoginView(this);
			this.viewMap.put("loginView", loginView);
			this.add(loginView, BorderLayout.CENTER);
		}

		if (view.equals("search")) {
			LogoutView logoutView = new LogoutView(this);
			this.viewMap.put("logoutView", logoutView);
			this.add(logoutView, BorderLayout.WEST);

			SearchView searchView = new SearchView(this);
			this.viewMap.put("searchView", searchView);
			this.add(searchView, BorderLayout.CENTER);

		}

		if (view.equals("result")) {
			LogoutView logoutView = new LogoutView(this);
			this.viewMap.put("logoutView", logoutView);
			this.add(logoutView, BorderLayout.WEST);

			GraphView graphView = new GraphView(this);
			this.viewMap.put("graphView", graphView);
			this.add(graphView, BorderLayout.CENTER);
		}
		this.validate();
	}

	/**
	 * Konstruktor, setzt Fenstergrösse und den ersten Bereich des GUIs, der beim Starten der Applikation angezeigt wird.
	 */
	public FacePath() {
		this.setTitle("Facepath");
		setSize(1024, 768);
		this.addWindowListener(this);
		this.setLayout(new BorderLayout());
		this.showView("login");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void windowClosing(WindowEvent e) { this.dispose(); }
	
	public void actionPerformed(ActionEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

}