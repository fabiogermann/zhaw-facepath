package ch.zhaw.seps.view;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookProvider;
import ch.zhaw.seps.fb.FacebookSearch;

/**
 * @author dominikstraub
 */

public class LoginView extends Panel implements ActionListener {

	// only for dev purpose
	private TextField api_key;

	private TextField email;
	private TextField password;
	private Checkbox guest;
	private Button helpBtn;
	private Button loginBtn;
	private FacePath fp;

	public LoginView(FacePath myFB) {
		this.setLayout(null);
		setLocation(0, 0);
		this.setSize(1024, 768);
		this.fp = myFB;

		Panel topPanel = (Panel) FacePath.addComponentToPanel(new Panel(null), this, 150, 40, 800, 240);

		ImageIcon facepathLogo = new ImageIcon("resources" + System.getProperty("file.separator") + "facepath-logo.png");
		FacePath.addComponentToPanel(new JLabel("", facepathLogo, JLabel.CENTER), topPanel, 0, 0, 240, 240);

		String info = "facepath bieten Ihnen die Möglichkeit,\n";
		info += "die Beziehung zwischen beliebigen\n";
		info += "Facebook-Benutzern zu ermitteln.\n";
		info += "\n";
		info += "Die Wege, über die die Benutzer verknüpft\n";
		info += "sind, lassen sich mit facepath grafisch darstellen.";
		FacePath.addComponentToPanel(new Label(info), topPanel, 280, 30, 450, 180);

		Panel loginForm = (Panel) FacePath.addComponentToPanel(new Panel(null), this, 70, 320, 830, 250);

		// only for dev purpose
		api_key = (TextField) FacePath.addComponentToPanel(new TextField(50), loginForm, 80, 200, 400, 30);

		FacePath.addComponentToPanel(new Label("Bitte geben Sie Ihren Facebook Login ein."), loginForm, 80, 40, 400, 30);
		FacePath.addComponentToPanel(new Label("E-Mail:"), loginForm, 80, 80, 80, 30);
		email = (TextField) FacePath.addComponentToPanel(new TextField(50), loginForm, 280, 80, 470, 30);
		FacePath.addComponentToPanel(new Label("Passwort:"), loginForm, 80, 120, 110, 30);
		password = (TextField) FacePath.addComponentToPanel(new TextField(50), loginForm, 280, 120, 470, 30);
		guest = (Checkbox) FacePath.addComponentToPanel(new Checkbox(
		        "Als Gast einloggen (Kein Facebook-Login benötigt)"), loginForm, 80, 160, 600, 30);
		helpBtn = (Button) FacePath.addComponentToPanel(new Button("Hilfe"), loginForm, 520, 200, 130, 50);
		helpBtn.addActionListener(this);
		loginBtn = (Button) FacePath.addComponentToPanel(new Button("Login"), loginForm, 680, 200, 130, 50);
		loginBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginBtn) {
			FacebookProvider fbProvider = new FacebookProvider(api_key.getText(), email.getText(), password.getText());
			fp.setFP(fbProvider);
			FacebookSearch fbSearch = new FacebookSearch(fbProvider);
			fp.setFS(fbSearch);
			FacePath.showView("search");
		}
	}
}