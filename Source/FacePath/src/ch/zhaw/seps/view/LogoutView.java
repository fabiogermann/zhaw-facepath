package ch.zhaw.seps.view;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookProvider;

public class LogoutView extends Panel implements ActionListener {

	private Button logoutBtn;
	private FacePath fp;

	public LogoutView(FacePath myFB) {
		this.setLayout(null);
		setLocation(0, 0);
		this.setSize(150, 768);
		this.fp = myFB;

		ImageIcon facepathLogo = new ImageIcon("resources" + System.getProperty("file.separator")
		        + "facepath-logo-small.png");
		FacePath.addComponentToPanel(new JLabel("", facepathLogo, JLabel.CENTER), this, 30, 25, 80, 80);

		String loginTypString = "Login-Typ:\n";
		loginTypString += "Gast";
		FacePath.addComponentToPanel(new Label(loginTypString), this, 30, 130, 85, 45);

		logoutBtn = (Button) FacePath.addComponentToPanel(new Button("Logout"), this, 20, 180, 130, 50);
		logoutBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logoutBtn) {
			this.fp.showView("login");
		}
	}
}
