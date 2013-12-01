package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookApplicationAuthorizationException;
import ch.zhaw.seps.fb.FacebookLoginException;
import ch.zhaw.seps.fb.FacebookProvider;

public class LoginView extends JPanel implements ActionListener {

	private static final String GUEST_EMAIL = "facepath@jesus.ch";
	private static final String GUEST_PASSWORD = "seps2013";

	private FacePath fp;

	private JTextField emailTextField;
	private JPasswordField passwordField;
	private JCheckBox guestCheckBox;
	private JButton helpButton;
	private JButton loginButton;

	/**
	 * Create the View.
	 */
	public LoginView(FacePath fp) {
		this.fp = fp;
		this.initialize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton || e.getSource() == emailTextField || e.getSource() == passwordField) {
			FacebookProvider fbProvider = null;
			if (guestCheckBox.isSelected()) {
				try {
					fbProvider = new FacebookProvider(LoginView.GUEST_EMAIL, LoginView.GUEST_PASSWORD);
					this.fp.setFP(fbProvider);
					this.fp.showView("search");
				} catch (FacebookLoginException e1) {
					e1.printStackTrace();
				} catch (FacebookApplicationAuthorizationException e1) {
					e1.printStackTrace();
					this.openBrowserWindow(this.fp.getFP().getLoginRequest());
				}
			} else {
				try {
					fbProvider = new FacebookProvider(emailTextField.getText(), new String(passwordField.getPassword()));
					this.fp.setFP(fbProvider);
					this.fp.showView("search");
				} catch (FacebookLoginException e1) {
					e1.printStackTrace();
				} catch (FacebookApplicationAuthorizationException e1) {
					e1.printStackTrace();
					this.openBrowserWindow(this.fp.getFP().getLoginRequest());
				}
			}
		}
		if (e.getSource() == guestCheckBox) {
			if (guestCheckBox.isSelected()) {
				emailTextField.setEditable(false);
				passwordField.setEditable(false);
			} else {
				emailTextField.setEditable(true);
				passwordField.setEditable(true);
			}
		}
	}

	private void openBrowserWindow(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new URI(url));
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (URISyntaxException use) {
					use.printStackTrace();
				}
			} else {
				System.out.println("Browser not supported");
			}
		}
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		this.setBackground(Color.WHITE);
		this.setSize(1024, 768);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);

		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_infoPanel = new GridBagConstraints();
		gbc_infoPanel.anchor = GridBagConstraints.SOUTH;
		gbc_infoPanel.insets = new Insets(35, 150, 5, 150);
		gbc_infoPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_infoPanel.gridx = 0;
		gbc_infoPanel.gridy = 0;
		this.add(infoPanel, gbc_infoPanel);
		GridBagLayout gbl_infoPanel = new GridBagLayout();
		gbl_infoPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_infoPanel.rowHeights = new int[] { 0, 0 };
		gbl_infoPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_infoPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		infoPanel.setLayout(gbl_infoPanel);

		JLabel logoLabel = new JLabel("");
		logoLabel.setBackground(Color.WHITE);
		logoLabel
		        .setIcon(new ImageIcon(getClass().getResource("/ch/zhaw/seps/view/resources/facepath-logo-medium.png")));
		GridBagConstraints gbc_logoLabel = new GridBagConstraints();
		gbc_logoLabel.gridx = 0;
		gbc_logoLabel.gridy = 0;
		infoPanel.add(logoLabel, gbc_logoLabel);

		JTextArea infoTextArea = new JTextArea();
		infoTextArea.setEnabled(false);
		infoTextArea.setEditable(false);
		infoTextArea.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		infoTextArea.setBackground(new Color(255, 255, 255));
		infoTextArea
		        .setText("Facepath bietet Ihnen die M\u00F6glichkeit,\ndie Beziehung zwischen beliebigen\nFacebook-Benutzern zu ermitteln.\n\nDie Wege, \u00FCber welche die Benutzer verkn\u00FCpft\nsind, lassen sich mit facepath grafisch darstellen.");
		GridBagConstraints gbc_infoTextArea = new GridBagConstraints();
		gbc_infoTextArea.gridx = 1;
		gbc_infoTextArea.gridy = 0;
		infoPanel.add(infoTextArea, gbc_infoTextArea);

		JPanel loginFormPanel = new JPanel();
		loginFormPanel.setBackground(Color.WHITE);
		loginFormPanel.setBorder(BorderFactory.createTitledBorder("Login"));
		GridBagConstraints gbc_loginFormPanel = new GridBagConstraints();
		gbc_loginFormPanel.anchor = GridBagConstraints.NORTH;
		gbc_loginFormPanel.insets = new Insets(0, 80, 30, 80);
		gbc_loginFormPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginFormPanel.gridx = 0;
		gbc_loginFormPanel.gridy = 1;
		this.add(loginFormPanel, gbc_loginFormPanel);
		GridBagLayout gbl_loginFormPanel = new GridBagLayout();
		gbl_loginFormPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_loginFormPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_loginFormPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_loginFormPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		loginFormPanel.setLayout(gbl_loginFormPanel);

		JLabel instructionsLabel = new JLabel("Geben Sie Ihren Facebook Login ein");
		instructionsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		GridBagConstraints gbc_instructionsLabel = new GridBagConstraints();
		gbc_instructionsLabel.anchor = GridBagConstraints.WEST;
		gbc_instructionsLabel.gridwidth = 2;
		gbc_instructionsLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_instructionsLabel.insets = new Insets(10, 30, 5, 30);
		gbc_instructionsLabel.gridx = 0;
		gbc_instructionsLabel.gridy = 0;
		loginFormPanel.add(instructionsLabel, gbc_instructionsLabel);

		JLabel emailLabel = new JLabel("E-Mail:");
		emailLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		GridBagConstraints gbc_emailLabel = new GridBagConstraints();
		gbc_emailLabel.anchor = GridBagConstraints.WEST;
		gbc_emailLabel.insets = new Insets(0, 30, 5, 5);
		gbc_emailLabel.gridx = 0;
		gbc_emailLabel.gridy = 1;
		loginFormPanel.add(emailLabel, gbc_emailLabel);

		emailTextField = new JTextField();
		GridBagConstraints gbc_emailTextField = new GridBagConstraints();
		gbc_emailTextField.insets = new Insets(0, 0, 5, 30);
		gbc_emailTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailTextField.gridx = 1;
		gbc_emailTextField.gridy = 1;
		loginFormPanel.add(emailTextField, gbc_emailTextField);
		emailTextField.setColumns(10);
		emailTextField.addActionListener(this);

		JLabel passwordLabel = new JLabel("Passwort:");
		passwordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.WEST;
		gbc_passwordLabel.insets = new Insets(0, 30, 5, 5);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 2;
		loginFormPanel.add(passwordLabel, gbc_passwordLabel);

		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 30);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 2;
		loginFormPanel.add(passwordField, gbc_passwordField);
		passwordField.addActionListener(this);

		guestCheckBox = new JCheckBox("Als Gast einloggen (Kein Facebook-Login ben\u00F6tigt)");
		guestCheckBox.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		GridBagConstraints gbc_guestCheckBox = new GridBagConstraints();
		gbc_guestCheckBox.anchor = GridBagConstraints.WEST;
		gbc_guestCheckBox.insets = new Insets(0, 30, 5, 30);
		gbc_guestCheckBox.gridx = 0;
		gbc_guestCheckBox.gridy = 3;
		gbc_guestCheckBox.gridwidth = 2;
		loginFormPanel.add(guestCheckBox, gbc_guestCheckBox);
		guestCheckBox.addActionListener(this);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_buttonsPanel = new GridBagConstraints();
		gbc_buttonsPanel.gridwidth = 2;
		gbc_buttonsPanel.insets = new Insets(0, 30, 10, 30);
		gbc_buttonsPanel.anchor = GridBagConstraints.EAST;
		gbc_buttonsPanel.gridx = 0;
		gbc_buttonsPanel.gridy = 4;
		loginFormPanel.add(buttonsPanel, gbc_buttonsPanel);
		GridBagLayout gbl_buttonsPanel = new GridBagLayout();
		gbl_buttonsPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_buttonsPanel.rowHeights = new int[] { 0, 0 };
		gbl_buttonsPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_buttonsPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		buttonsPanel.setLayout(gbl_buttonsPanel);

		helpButton = new JButton("Hilfe");
		helpButton.addActionListener(this);
		GridBagConstraints gbc_helpButton = new GridBagConstraints();
		gbc_helpButton.gridx = 0;
		gbc_helpButton.gridy = 0;
		buttonsPanel.add(helpButton, gbc_helpButton);

		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.gridx = 1;
		gbc_loginButton.gridy = 0;
		buttonsPanel.add(loginButton, gbc_loginButton);

	}

}