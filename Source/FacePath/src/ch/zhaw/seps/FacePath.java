/**
 * 
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
	//		1 = ProgrammStatus,
	//		2 = DetailedProgrammStatus,
	//		3 = DetailedProgrammStatusWithTreads,
	//		4 = DetailedProgrammStatusWithDetailedTreads
	
	public static final int DEBUG = 3;

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

	public FacePath() {
		this.setTitle("Facepath");
		setSize(1024, 768);
		this.addWindowListener(this);
		this.setLayout(new BorderLayout());
		this.showView("login");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}