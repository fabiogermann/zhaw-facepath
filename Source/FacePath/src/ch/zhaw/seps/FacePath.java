/**
 * 
 */
package ch.zhaw.seps;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import ch.zhaw.seps.view.GraphView;
import ch.zhaw.seps.view.LoginView;
import ch.zhaw.seps.view.LogoutView;
import ch.zhaw.seps.view.SearchView;

/**
 * @author fabiog
 * @author dominikstraub
 */

public class FacePath extends Frame implements ActionListener, WindowListener {

	public static void main(String[] args) {
		new FacePath();
	}

	public static Component addComponentToPanel(Component c, Panel p, int x, int y, int width, int height) {
		c.setLocation(x, y);
		c.setSize(width, height);
		p.add(c);
		return c;
	}

	private static Map<String, Panel> viewList = new HashMap<String, Panel>();

	public static void showView(String view) {
		if (view.equals("login")) {
			FacePath.viewList.get("loginView").setVisible(true);
			FacePath.viewList.get("logoutView").setVisible(false);
			FacePath.viewList.get("searchView").setVisible(false);
			FacePath.viewList.get("graphView").setVisible(false);
		}

		if (view.equals("search")) {
			FacePath.viewList.get("loginView").setVisible(false);
			FacePath.viewList.get("logoutView").setVisible(true);
			FacePath.viewList.get("searchView").setVisible(true);
			FacePath.viewList.get("graphView").setVisible(false);
		}

		if (view.equals("result")) {
			FacePath.viewList.get("loginView").setVisible(false);
			FacePath.viewList.get("logoutView").setVisible(true);
			FacePath.viewList.get("searchView").setVisible(false);
			FacePath.viewList.get("graphView").setVisible(true);
		}
	}

	public FacePath() {
		setTitle("facepath");
		setLocation(200, 200);
		setSize(1024, 768);

		this.addWindowListener(this);

		this.setLayout(new BorderLayout());

		// loginView
		LoginView loginView = new LoginView();
		FacePath.viewList.put("loginView", loginView);
		this.add(loginView, BorderLayout.CENTER);

		// logoutView
		LogoutView logoutView = new LogoutView();
		FacePath.viewList.put("logoutView", logoutView);
		this.add(logoutView, BorderLayout.WEST);

		// searchView
		SearchView searchView = new SearchView();
		FacePath.viewList.put("searchView", searchView);
		this.add(searchView, BorderLayout.CENTER);

		// GraphView
		GraphView graphView = new GraphView();
		FacePath.viewList.put("graphView", graphView);
		this.add(graphView, BorderLayout.CENTER);

		FacePath.showView("login");

		setVisible(true);
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