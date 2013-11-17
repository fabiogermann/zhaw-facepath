package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookProfile;
import ch.zhaw.seps.fb.FacebookSearch;

public class SearchView extends JPanel implements ActionListener {

	private FacePath fp;

	private JTextField sourceUsernameTextField;
	private JTextField destinationUsernameTextField;
	private JCheckBox eventsCheckBox;
	private JCheckBox profilePicsCheckBox;
	private JCheckBox nationalOnlyCheckBox;
	private JButton helpButton;
	private JButton searchButton;
	private JButton resultButton;
	public JPanel resultFormPanel;

	private List<String> sourceUserList; // autocomplete test
	private List<String> destinationUserList; // autocomplete test

	/**
	 * Create the View.
	 */
	public SearchView(FacePath fp) {
		this.fp = fp;
		this.sourceUserList = new ArrayList<String>();
		this.destinationUserList = new ArrayList<String>();
		this.initialize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: es sollte gesucht werden nach dem klick auf suchen und der
		// Graph soll kontinuierlich aufgebaut werden. ist die suche komplett
		// soll erst die meldung erscheinen
		if (e.getSource() == searchButton || e.getSource() == sourceUsernameTextField
		        || e.getSource() == destinationUsernameTextField) {

			this.searchUser(sourceUsernameTextField, sourceUserList, true);
			this.searchUser(destinationUsernameTextField, destinationUserList, true);

			resultFormPanel.setVisible(true);
		}
		if (e.getSource() == resultButton) {
			FacebookSearch fbSearch = new FacebookSearch(this.fp.getFP());
			this.fp.setFS(fbSearch);

			this.searchUser(sourceUsernameTextField, sourceUserList, false);
			this.searchUser(destinationUsernameTextField, destinationUserList, false);

			this.fp.getFS().searchIterate();
			this.fp.showView("result");
		}
	}

	public void searchUser(JTextField usernameTextfield, List<String> resultStringList, boolean search) {
		List<FacebookProfile> userSearchResult = this.fp.getFP().getUserFromSearch(usernameTextfield.getText());
		if (userSearchResult.size() > 0) {
			FacebookProfile userProfile = userSearchResult.get(0);
			if (search) {
				for (FacebookProfile fbp : userSearchResult) {
					resultStringList.add(fbp.getUserUIDString());
				}
				usernameTextfield.setText(userProfile.getUserUIDString());
			} else {
				this.fp.getFS().setPersonOfInterestSource(userProfile);
			}
		}
	}

	/**
	 * Initialize the contents of the panel.
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
		gbc_searchFormPanel.anchor = GridBagConstraints.NORTH;
		gbc_searchFormPanel.insets = new Insets(30, 30, 5, 30);
		gbc_searchFormPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchFormPanel.gridx = 0;
		gbc_searchFormPanel.gridy = 0;
		add(searchFormPanel, gbc_searchFormPanel);
		GridBagLayout gbl_searchFormPanel = new GridBagLayout();
		gbl_searchFormPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_searchFormPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_searchFormPanel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_searchFormPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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

		sourceUsernameTextField = new JTextField();
		GridBagConstraints gbc_sourceUsernameTextField = new GridBagConstraints();
		gbc_sourceUsernameTextField.insets = new Insets(0, 0, 5, 30);
		gbc_sourceUsernameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sourceUsernameTextField.gridx = 1;
		gbc_sourceUsernameTextField.gridy = 1;
		searchFormPanel.add(sourceUsernameTextField, gbc_sourceUsernameTextField);
		sourceUsernameTextField.setColumns(10);
		sourceUsernameTextField.addActionListener(this);

		AutoCompleteDecorator.decorate(sourceUsernameTextField, sourceUserList, false);

		JLabel destinationUsernameLabel = new JLabel("Ziel-Benutzernamen:");
		GridBagConstraints gbc_destinationUsernameLabel = new GridBagConstraints();
		gbc_destinationUsernameLabel.anchor = GridBagConstraints.WEST;
		gbc_destinationUsernameLabel.insets = new Insets(0, 30, 5, 5);
		gbc_destinationUsernameLabel.gridx = 0;
		gbc_destinationUsernameLabel.gridy = 2;
		searchFormPanel.add(destinationUsernameLabel, gbc_destinationUsernameLabel);

		destinationUsernameTextField = new JTextField();
		GridBagConstraints gbc_destinationUsernameTextField = new GridBagConstraints();
		gbc_destinationUsernameTextField.insets = new Insets(0, 0, 5, 30);
		gbc_destinationUsernameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_destinationUsernameTextField.gridx = 1;
		gbc_destinationUsernameTextField.gridy = 2;
		searchFormPanel.add(destinationUsernameTextField, gbc_destinationUsernameTextField);
		destinationUsernameTextField.setColumns(10);
		destinationUsernameTextField.addActionListener(this);

		AutoCompleteDecorator.decorate(destinationUsernameTextField, destinationUserList, false);

		JLabel searchoptionsLabel = new JLabel("Suchoptionen:");
		GridBagConstraints gbc_searchoptionsLabel = new GridBagConstraints();
		gbc_searchoptionsLabel.gridwidth = 2;
		gbc_searchoptionsLabel.anchor = GridBagConstraints.WEST;
		gbc_searchoptionsLabel.insets = new Insets(0, 30, 5, 30);
		gbc_searchoptionsLabel.gridx = 0;
		gbc_searchoptionsLabel.gridy = 3;
		searchFormPanel.add(searchoptionsLabel, gbc_searchoptionsLabel);

		eventsCheckBox = new JCheckBox("Veranstaltungen miteinbeziehen");
		GridBagConstraints gbc_eventsCheckBox = new GridBagConstraints();
		gbc_eventsCheckBox.gridwidth = 2;
		gbc_eventsCheckBox.anchor = GridBagConstraints.WEST;
		gbc_eventsCheckBox.insets = new Insets(0, 30, 5, 30);
		gbc_eventsCheckBox.gridx = 0;
		gbc_eventsCheckBox.gridy = 4;
		searchFormPanel.add(eventsCheckBox, gbc_eventsCheckBox);

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

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_buttonsPanel = new GridBagConstraints();
		gbc_buttonsPanel.insets = new Insets(0, 30, 10, 30);
		gbc_buttonsPanel.anchor = GridBagConstraints.EAST;
		gbc_buttonsPanel.gridx = 1;
		gbc_buttonsPanel.gridy = 7;
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
		gbc_helpButton.insets = new Insets(0, 0, 0, 0);
		gbc_helpButton.gridx = 0;
		gbc_helpButton.gridy = 0;
		buttonsPanel.add(helpButton, gbc_helpButton);

		searchButton = new JButton("Suche");
		searchButton.addActionListener(this);
		GridBagConstraints gbc_searchButton = new GridBagConstraints();
		gbc_searchButton.insets = new Insets(0, 0, 0, 0);
		gbc_searchButton.gridx = 1;
		gbc_searchButton.gridy = 0;
		buttonsPanel.add(searchButton, gbc_searchButton);

		resultFormPanel = new JPanel();
		resultFormPanel
		        .setBorder(new TitledBorder(null, "Ergebnis", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		resultFormPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_resultFormPanel = new GridBagConstraints();
		gbc_resultFormPanel.anchor = GridBagConstraints.NORTH;
		gbc_resultFormPanel.insets = new Insets(0, 30, 30, 30);
		gbc_resultFormPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_resultFormPanel.gridx = 0;
		gbc_resultFormPanel.gridy = 1;
		add(resultFormPanel, gbc_resultFormPanel);
		GridBagLayout gbl_resultFormPanel = new GridBagLayout();
		gbl_resultFormPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_resultFormPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_resultFormPanel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_resultFormPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		resultFormPanel.setLayout(gbl_resultFormPanel);
		resultFormPanel.setVisible(false);

		JLabel resultLabel = new JLabel("Die Suche ergab mehere Treffer");
		GridBagConstraints gbc_resultLabel = new GridBagConstraints();
		gbc_resultLabel.gridwidth = 2;
		gbc_resultLabel.anchor = GridBagConstraints.WEST;
		gbc_resultLabel.insets = new Insets(10, 30, 5, 30);
		gbc_resultLabel.gridx = 0;
		gbc_resultLabel.gridy = 0;
		resultFormPanel.add(resultLabel, gbc_resultLabel);

		resultButton = new JButton("Ergebnis anzeigen");
		resultButton.addActionListener(this);
		GridBagConstraints gbc_resultButton = new GridBagConstraints();
		gbc_resultButton.insets = new Insets(0, 30, 10, 30);
		gbc_resultButton.gridwidth = 2;
		gbc_resultButton.anchor = GridBagConstraints.EAST;
		gbc_resultButton.gridx = 0;
		gbc_resultButton.gridy = 1;
		resultFormPanel.add(resultButton, gbc_resultButton);

	}
}
