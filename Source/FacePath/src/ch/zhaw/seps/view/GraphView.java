package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.graphstream.ui.swingViewer.View;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookSearch;

public class GraphView extends JPanel implements ActionListener {

	private FacePath fp;
	private FacebookSearch fs;

	private JPanel resultPanel;
	private JTextArea reportTextArea;
	private JButton helpButton;
	private JButton newSearchButton;

	/**
	 * Create the View.
	 */
	public GraphView(FacePath fp) {
		this.fp = fp;
		this.initialize();
		if (fs == null) {
			this.fs = fp.getFS();
		}
		View view = fs.getGraph().display().addDefaultView(false);
		GridBagConstraints gbc_view = new GridBagConstraints();
		gbc_view.fill = GridBagConstraints.BOTH;
		gbc_view.insets = new Insets(10, 30, 5, 30);
		gbc_view.gridx = 0;
		gbc_view.gridy = 0;
		resultPanel.add(view, gbc_view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newSearchButton) {
			this.fp.showView("search");
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
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		resultPanel = new JPanel();
		resultPanel.setBorder(new TitledBorder(null, "Ergebnis", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		resultPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_resultPanel = new GridBagConstraints();
		gbc_resultPanel.insets = new Insets(30, 30, 30, 30);
		gbc_resultPanel.fill = GridBagConstraints.BOTH;
		gbc_resultPanel.gridx = 0;
		gbc_resultPanel.gridy = 0;
		add(resultPanel, gbc_resultPanel);
		GridBagLayout gbl_resultPanel = new GridBagLayout();
		gbl_resultPanel.columnWidths = new int[] { 0, 0 };
		gbl_resultPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_resultPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_resultPanel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		resultPanel.setLayout(gbl_resultPanel);

		reportTextArea = new JTextArea("Gemeinsamkeiten:\n...\n...");
		GridBagConstraints gbc_reportTextArea = new GridBagConstraints();
		gbc_reportTextArea.fill = GridBagConstraints.HORIZONTAL;
		gbc_reportTextArea.insets = new Insets(0, 30, 5, 30);
		gbc_reportTextArea.gridx = 0;
		gbc_reportTextArea.gridy = 1;
		resultPanel.add(reportTextArea, gbc_reportTextArea);

		JPanel buttonsPanel = new JPanel();
		GridBagConstraints gbc_buttonsPanel = new GridBagConstraints();
		gbc_buttonsPanel.insets = new Insets(0, 30, 10, 30);
		gbc_buttonsPanel.anchor = GridBagConstraints.EAST;
		gbc_buttonsPanel.gridx = 0;
		gbc_buttonsPanel.gridy = 2;
		resultPanel.add(buttonsPanel, gbc_buttonsPanel);
		buttonsPanel.setBackground(Color.WHITE);
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

		newSearchButton = new JButton("Neue Suche");
		newSearchButton.addActionListener(this);
		GridBagConstraints gbc_newSearchButton = new GridBagConstraints();
		gbc_newSearchButton.gridx = 1;
		gbc_newSearchButton.gridy = 0;
		buttonsPanel.add(newSearchButton, gbc_newSearchButton);

	}

}
