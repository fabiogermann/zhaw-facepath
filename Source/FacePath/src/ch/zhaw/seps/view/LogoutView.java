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

import ch.zhaw.seps.FacePath;

public class LogoutView extends JPanel implements ActionListener {

	private FacePath fp;

	private JButton logoutButton;

	/**
	 * Create the View.
	 */
	public LogoutView(FacePath fp) {
		this.fp = fp;
		initialize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logoutButton) {
			this.fp.showView("login");
		}
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		this.setBackground(Color.WHITE);
		this.setSize(150, 768);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel logoLabel = new JLabel("");
		logoLabel.setIcon(new ImageIcon(SearchView.class
		        .getResource("/ch/zhaw/seps/view/resources/facepath-logo-small.png")));
		GridBagConstraints gbc_logoLabel = new GridBagConstraints();
		gbc_logoLabel.insets = new Insets(10, 0, 5, 0);
		gbc_logoLabel.gridx = 0;
		gbc_logoLabel.gridy = 0;
		add(logoLabel, gbc_logoLabel);

		JLabel loginTypInfoLabel = new JLabel("Login-Typ:");
		GridBagConstraints gbc_loginTypInfoLabel = new GridBagConstraints();
		gbc_loginTypInfoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_loginTypInfoLabel.gridx = 0;
		gbc_loginTypInfoLabel.gridy = 1;
		add(loginTypInfoLabel, gbc_loginTypInfoLabel);

		JLabel loginTypLabel = new JLabel("Gast");
		GridBagConstraints gbc_loginTypLabel = new GridBagConstraints();
		gbc_loginTypLabel.insets = new Insets(0, 0, 5, 0);
		gbc_loginTypLabel.gridx = 0;
		gbc_loginTypLabel.gridy = 2;
		add(loginTypLabel, gbc_loginTypLabel);

		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(this);
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 3;
		add(logoutButton, gbc_logoutButton);
	}

}
