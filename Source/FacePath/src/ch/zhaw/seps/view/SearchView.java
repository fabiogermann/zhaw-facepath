package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookProfile;
import ch.zhaw.seps.fb.FacebookSearch;

public class SearchView extends JPanel implements ActionListener {

	private FacePath fp;

	private JComboBox<FacebookProfile> sourceComboBox;
	private JComboBox<FacebookProfile> destinationComboBox;
	private JButton userSearchButton;
	private JCheckBox profilePicsCheckBox;
	private JCheckBox nationalOnlyCheckBox;
	private JCheckBox allFriendsCheckBox;
	private JButton helpButton;
	private JButton searchButton;

	private DefaultComboBoxModel<FacebookProfile> sourceModel;
	private DefaultComboBoxModel<FacebookProfile> destinationModel;
	private JCheckBox chckbxNewCheckBox;

	/**
	 * Konstruktor Erstellt die Anzeige
	 */
	public SearchView(FacePath fp) {
		this.fp = fp;
		this.sourceModel = new DefaultComboBoxModel<FacebookProfile>();
		this.destinationModel = new DefaultComboBoxModel<FacebookProfile>();
		this.initialize();
	}

	/**
	 * Beim Drücken auf "Benutzer suchen" soll der korrekte Benutzer für die
	 * Suche ermittelt werden Beim Drücken auf "Suche" soll die Suche mit den
	 * beiden ausgewählten Profilen gestartet werden Übergibt die gewählte
	 * Suchkonfiguration
	 * 
	 * @see java.awt.event.ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO: es sollte gesucht werden nach dem klick auf suchen und der
		// Graph soll kontinuierlich aufgebaut werden. ist die suche komplett
		// soll erst die meldung erscheinen
		if (e.getSource() == this.userSearchButton || e.getActionCommand().equals("comboBoxEdited")) {
			this.searchUser(this.sourceComboBox, this.sourceModel);
			this.searchUser(this.destinationComboBox, this.destinationModel);
		}
		if (e.getActionCommand().equals("comboBoxChanged") || e.getSource() == this.userSearchButton
		        || e.getActionCommand().equals("comboBoxEdited")) {
			if (this.sourceComboBox.getSelectedIndex() > -1 && this.destinationComboBox.getSelectedIndex() > -1) {
				this.searchButton.setEnabled(true);
			} else {
				this.searchButton.setEnabled(false);
			}
		}
		if (e.getSource() == this.searchButton) {
			FacebookSearch fbSearch = new FacebookSearch(this.fp.getFP());
			this.fp.setFS(fbSearch);
			this.setPersonsOfInterest((FacebookProfile) sourceComboBox.getSelectedItem(),
			        (FacebookProfile) destinationComboBox.getSelectedItem());
			this.fp.getFS().setOptions(this.nationalOnlyCheckBox.isSelected(), this.profilePicsCheckBox.isSelected(),
			        false, false, this.allFriendsCheckBox.isSelected());
			this.fp.showView("result");
			Thread t = new Thread(this.fp.getFS());
			t.start();
		}
		if (e.getSource() == this.helpButton) {
			HelpView.getHelpView(this.getClass()).toFront();
		}
	}

	/**
	 * Bearbeitet die Eingabe des Profils (Parameter), sodass damit gesucht
	 * werden kann
	 * 
	 * @param usernameComboBox
	 *            Inhalt der Textbox, in der das Profil angegeben wird
	 * @param resultFacebookProfileModel
	 *            Übergibt die Stuktur, an die der Suchparameter angepasst wird
	 */
	private void searchUser(JComboBox<FacebookProfile> usernameComboBox,
	        DefaultComboBoxModel<FacebookProfile> resultFacebookProfileModel) {

		resultFacebookProfileModel.removeAllElements();
		String searchString = "";

		if (usernameComboBox.getSelectedIndex() > -1) {
			searchString = ((FacebookProfile) usernameComboBox.getSelectedItem()).getUserUIDString();
		} else if (usernameComboBox.getEditor().getItem() != null) {
			searchString = usernameComboBox.getEditor().getItem().toString();
		}

		List<FacebookProfile> userSearchResult = null;

		if (searchString == null || searchString.equals("")) {
			userSearchResult = new ArrayList<FacebookProfile>();
		} else {
			userSearchResult = this.fp.getFP().getUserFromSearch(searchString);
		}

		if (userSearchResult.size() > 0) {
			for (FacebookProfile fbp : userSearchResult) {
				resultFacebookProfileModel.addElement(fbp);
			}
			usernameComboBox.setSelectedIndex(0);
		}
	}

	/**
	 * Setzt den Suchparameter in der Suche, um mit die Suche beginnen zu können
	 * 
	 * @param sourceFacebookProfile
	 *            Startprofil
	 * @param destinationFacebookProfile
	 *            Zielprofil
	 */
	private void setPersonsOfInterest(FacebookProfile sourceFacebookProfile, FacebookProfile destinationFacebookProfile) {
		this.fp.getFS().setPersonOfInterestSource(sourceFacebookProfile);
		this.fp.getFS().setPersonOfInterestDestination(destinationFacebookProfile);
	}

	/**
	 * Initialisiert die Anzeige der Suchmaske
	 */
	private void initialize() {
		this.setBackground(Color.WHITE);
		this.setSize(874, 768);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JPanel searchFormPanel = new JPanel();
		searchFormPanel.setBorder(new TitledBorder(null, "Suche", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchFormPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_searchFormPanel = new GridBagConstraints();
		gbc_searchFormPanel.gridheight = 2;
		gbc_searchFormPanel.anchor = GridBagConstraints.NORTH;
		gbc_searchFormPanel.insets = new Insets(30, 30, 30, 30);
		gbc_searchFormPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchFormPanel.gridx = 0;
		gbc_searchFormPanel.gridy = 0;
		add(searchFormPanel, gbc_searchFormPanel);
		GridBagLayout gbl_searchFormPanel = new GridBagLayout();
		gbl_searchFormPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_searchFormPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
		gbl_searchFormPanel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_searchFormPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		searchFormPanel.setLayout(gbl_searchFormPanel);

		JLabel instructionsLabel = new JLabel("Geben Sie bitte die Benutzernamen der Start- und Ziel-Benutzer an\n");
		GridBagConstraints gbc_instructionsLabel = new GridBagConstraints();
		gbc_instructionsLabel.anchor = GridBagConstraints.WEST;
		gbc_instructionsLabel.gridwidth = 2;
		gbc_instructionsLabel.insets = new Insets(10, 30, 5, 30);
		gbc_instructionsLabel.gridx = 0;
		gbc_instructionsLabel.gridy = 0;
		searchFormPanel.add(instructionsLabel, gbc_instructionsLabel);

		JLabel sourceUsernameLabel = new JLabel("Start-Benutzernamen:");
		GridBagConstraints gbc_sourceUsernameLabel = new GridBagConstraints();
		gbc_sourceUsernameLabel.anchor = GridBagConstraints.WEST;
		gbc_sourceUsernameLabel.insets = new Insets(0, 30, 5, 5);
		gbc_sourceUsernameLabel.gridx = 0;
		gbc_sourceUsernameLabel.gridy = 1;
		searchFormPanel.add(sourceUsernameLabel, gbc_sourceUsernameLabel);

		sourceComboBox = new JComboBox<FacebookProfile>(sourceModel);
		sourceComboBox.setEditor(new ProfileItemEditor());
		sourceComboBox.setEditable(true);
		sourceComboBox.setRenderer(new ProfileCellRenderer());
		GridBagConstraints gbc_sourceComboBox = new GridBagConstraints();
		gbc_sourceComboBox.insets = new Insets(0, 0, 5, 30);
		gbc_sourceComboBox.fill = GridBagConstraints.BOTH;
		gbc_sourceComboBox.gridx = 1;
		gbc_sourceComboBox.gridy = 1;
		searchFormPanel.add(sourceComboBox, gbc_sourceComboBox);
		sourceComboBox.addActionListener(this);

		JLabel destinationUsernameLabel = new JLabel("Ziel-Benutzernamen:");
		GridBagConstraints gbc_destinationUsernameLabel = new GridBagConstraints();
		gbc_destinationUsernameLabel.anchor = GridBagConstraints.WEST;
		gbc_destinationUsernameLabel.insets = new Insets(0, 30, 5, 5);
		gbc_destinationUsernameLabel.gridx = 0;
		gbc_destinationUsernameLabel.gridy = 2;
		searchFormPanel.add(destinationUsernameLabel, gbc_destinationUsernameLabel);

		destinationComboBox = new JComboBox<FacebookProfile>(destinationModel);
		destinationComboBox.setEditor(new ProfileItemEditor());
		destinationComboBox.setEditable(true);
		destinationComboBox.setRenderer(new ProfileCellRenderer());
		GridBagConstraints gbc_destinationComboBox = new GridBagConstraints();
		gbc_destinationComboBox.insets = new Insets(0, 0, 5, 30);
		gbc_destinationComboBox.fill = GridBagConstraints.BOTH;
		gbc_destinationComboBox.gridx = 1;
		gbc_destinationComboBox.gridy = 2;
		searchFormPanel.add(destinationComboBox, gbc_destinationComboBox);
		destinationComboBox.addActionListener(this);

		userSearchButton = new JButton("Benutzer suchen");
		GridBagConstraints gbc_userSearchButton = new GridBagConstraints();
		gbc_userSearchButton.fill = GridBagConstraints.VERTICAL;
		gbc_userSearchButton.anchor = GridBagConstraints.EAST;
		gbc_userSearchButton.insets = new Insets(0, 0, 5, 30);
		gbc_userSearchButton.gridx = 1;
		gbc_userSearchButton.gridy = 3;
		searchFormPanel.add(userSearchButton, gbc_userSearchButton);
		userSearchButton.addActionListener(this);

		JLabel searchoptionsLabel = new JLabel("Suchoptionen:");
		GridBagConstraints gbc_searchoptionsLabel = new GridBagConstraints();
		gbc_searchoptionsLabel.gridwidth = 2;
		gbc_searchoptionsLabel.anchor = GridBagConstraints.WEST;
		gbc_searchoptionsLabel.insets = new Insets(0, 30, 5, 30);
		gbc_searchoptionsLabel.gridx = 0;
		gbc_searchoptionsLabel.gridy = 4;
		searchFormPanel.add(searchoptionsLabel, gbc_searchoptionsLabel);

		profilePicsCheckBox = new JCheckBox("Profilbilder anzeigen");
		GridBagConstraints gbc_profilePicsCheckBox = new GridBagConstraints();
		gbc_profilePicsCheckBox.gridwidth = 2;
		gbc_profilePicsCheckBox.anchor = GridBagConstraints.WEST;
		gbc_profilePicsCheckBox.insets = new Insets(0, 30, 5, 30);
		gbc_profilePicsCheckBox.gridx = 0;
		gbc_profilePicsCheckBox.gridy = 5;
		searchFormPanel.add(profilePicsCheckBox, gbc_profilePicsCheckBox);

		nationalOnlyCheckBox = new JCheckBox("Nur auf nationaler Ebene suchen");
		GridBagConstraints gbc_nationalOnlyCheckBox = new GridBagConstraints();
		gbc_nationalOnlyCheckBox.gridwidth = 2;
		gbc_nationalOnlyCheckBox.anchor = GridBagConstraints.WEST;
		gbc_nationalOnlyCheckBox.insets = new Insets(0, 30, 5, 30);
		gbc_nationalOnlyCheckBox.gridx = 0;
		gbc_nationalOnlyCheckBox.gridy = 6;
		searchFormPanel.add(nationalOnlyCheckBox, gbc_nationalOnlyCheckBox);

		allFriendsCheckBox = new JCheckBox("Alle Freunde in die Suche einbeziehen (beansprucht mehr Zeit)");
		GridBagConstraints gbc_allFriendsCheckBox = new GridBagConstraints();
		gbc_allFriendsCheckBox.gridwidth = 2;
		gbc_allFriendsCheckBox.anchor = GridBagConstraints.WEST;
		gbc_allFriendsCheckBox.insets = new Insets(0, 30, 5, 30);
		gbc_allFriendsCheckBox.gridx = 0;
		gbc_allFriendsCheckBox.gridy = 7;
		searchFormPanel.add(allFriendsCheckBox, gbc_allFriendsCheckBox);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_buttonsPanel = new GridBagConstraints();
		gbc_buttonsPanel.insets = new Insets(0, 0, 0, 30);
		gbc_buttonsPanel.anchor = GridBagConstraints.EAST;
		gbc_buttonsPanel.gridx = 1;
		gbc_buttonsPanel.gridy = 8;
		searchFormPanel.add(buttonsPanel, gbc_buttonsPanel);
		GridBagLayout gbl_buttonsPanel = new GridBagLayout();
		gbl_buttonsPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_buttonsPanel.rowHeights = new int[] { 0, 0 };
		gbl_buttonsPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_buttonsPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		buttonsPanel.setLayout(gbl_buttonsPanel);

		helpButton = new JButton("Hilfe");
		helpButton.addActionListener(this);
		GridBagConstraints gbc_helpButton = new GridBagConstraints();
		gbc_helpButton.fill = GridBagConstraints.BOTH;
		gbc_helpButton.insets = new Insets(0, 0, 0, 5);
		gbc_helpButton.gridx = 0;
		gbc_helpButton.gridy = 0;
		buttonsPanel.add(helpButton, gbc_helpButton);

		searchButton = new JButton("Suchen");
		GridBagConstraints gbc_searchButton = new GridBagConstraints();
		gbc_searchButton.fill = GridBagConstraints.BOTH;
		gbc_searchButton.gridx = 1;
		gbc_searchButton.gridy = 0;
		buttonsPanel.add(searchButton, gbc_searchButton);
		searchButton.setEnabled(false);
		searchButton.addActionListener(this);
	}

	/**
	 * Benachrichtigung bei privaten Profilen
	 */
	public static void notifyPrivateProfile() {
		JOptionPane
		        .showOptionDialog(
		                null,
		                "Eines oder beide der ausgewählten Profile sind Privat - die Suche kann aus diesem Grund nicht fortgeführt werden. Bitte wählen Sie andere Start/Ziel-Benutzer aus.",
		                "Privates Profil", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null,
		                null);
	}

	/**
	 * Benachrichtigung wenn keine Verbindung gefunden
	 */
	public static void notifyNoConnectionFound() {
		JOptionPane
		        .showOptionDialog(
		                null,
		                "Es wurde keine Verbindung zwischen den zwei angegebenen Profilen gefunden. Versuchen Sie eine neue Suche.",
		                "Keine Verbindung gefunden", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
		                null, null, null);
	}
}
