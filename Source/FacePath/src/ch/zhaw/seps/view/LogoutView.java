/**
 * Bereich des GUIs, der es ermöglicht, dass sich der Benutzer ausloggen kann
 * 
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ch.zhaw.seps.FacePath;

public class LogoutView extends JPanel implements ActionListener {

	private FacePath fp;

	private JButton logoutButton;
	private JLabel loginTypLabel;

	/**
	 * Konstruktor
	 * Erstellt die Anzeige
	 */
	public LogoutView(FacePath fp) {
		setBackground(Color.WHITE);
		this.fp = fp;
		this.initialize();
		String username = this.fp.getFP().getMyProfile().getUserUIDString();
		if (!username.equals("max.path.31")) {
			this.loginTypLabel.setText(username);
		} else {
			this.loginTypLabel.setText("Gast");
		}
	}

	/**
	 * Zeigt beim Klick auf den Button "Logout" den Login Bereich (Startpunkt der Anwendung) an.
	 * @see		java.awt.event.ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logoutButton) {
			this.fp.setFP(null);
			this.fp.setFS(null);
			this.fp.showView("login");
		}
	}

	/**
	 * Initialisiert den Inhalt der Anzeige
	 */
	private void initialize() {
		this.setSize(150, 768);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 150, 0 };
		gridBagLayout.rowHeights = new int[] { 768, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_searchPanel = new GridBagConstraints();
		gbc_searchPanel.insets = new Insets(35, 20, 30, 0);
		gbc_searchPanel.anchor = GridBagConstraints.NORTH;
		gbc_searchPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchPanel.gridx = 0;
		gbc_searchPanel.gridy = 0;
		add(searchPanel, gbc_searchPanel);
		GridBagLayout gbl_searchPanel = new GridBagLayout();
		gbl_searchPanel.columnWidths = new int[] { 0, 0 };
		gbl_searchPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_searchPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_searchPanel.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		searchPanel.setLayout(gbl_searchPanel);

		JLabel logoLabel = new JLabel("");
		logoLabel.setBackground(Color.WHITE);
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_logoLabel = new GridBagConstraints();
		gbc_logoLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_logoLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_logoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_logoLabel.gridwidth = 1;
		gbc_logoLabel.gridx = 0;
		gbc_logoLabel.gridy = 0;
		searchPanel.add(logoLabel, gbc_logoLabel);
		logoLabel
		        .setIcon(new ImageIcon(getClass().getResource("/ch/zhaw/seps/view/resources/facepath-logo-small.png")));

		JLabel loginTypInfoLabel = new JLabel("Benutzer:");
		loginTypInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_loginTypInfoLabel = new GridBagConstraints();
		gbc_loginTypInfoLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginTypInfoLabel.anchor = GridBagConstraints.WEST;
		gbc_loginTypInfoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_loginTypInfoLabel.gridx = 0;
		gbc_loginTypInfoLabel.gridy = 1;
		searchPanel.add(loginTypInfoLabel, gbc_loginTypInfoLabel);

		loginTypLabel = new JLabel();
		loginTypLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_loginTypLabel = new GridBagConstraints();
		gbc_loginTypLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginTypLabel.anchor = GridBagConstraints.EAST;
		gbc_loginTypLabel.insets = new Insets(0, 0, 5, 0);
		gbc_loginTypLabel.gridx = 0;
		gbc_loginTypLabel.gridy = 2;
		searchPanel.add(loginTypLabel, gbc_loginTypLabel);

		logoutButton = new JButton("Logout");
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.insets = new Insets(0, 0, 5, 0);
		gbc_logoutButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_logoutButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_logoutButton.gridwidth = 1;
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 3;
		searchPanel.add(logoutButton, gbc_logoutButton);
		logoutButton.addActionListener(this);
	}

}
