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

	/**
	 * Create the View.
	 */
	public LogoutView(FacePath fp) {
		setBackground(Color.WHITE);
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
		gbc_searchPanel.insets = new Insets(10, 10, 10, 10);
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
		logoLabel.setIcon(new ImageIcon(SearchView.class
		        .getResource("/ch/zhaw/seps/view/resources/facepath-logo-small.png")));

		JLabel loginTypInfoLabel = new JLabel("Login-Typ:");
		loginTypInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_loginTypInfoLabel = new GridBagConstraints();
		gbc_loginTypInfoLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginTypInfoLabel.anchor = GridBagConstraints.WEST;
		gbc_loginTypInfoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_loginTypInfoLabel.gridx = 0;
		gbc_loginTypInfoLabel.gridy = 1;
		searchPanel.add(loginTypInfoLabel, gbc_loginTypInfoLabel);

		JLabel loginTypLabel = new JLabel("Gast");
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
