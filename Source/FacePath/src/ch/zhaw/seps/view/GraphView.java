/**
 * GUI Bereich, der zuständig für die Anzeige des Ergebnis ist
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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookSearch;

public class GraphView extends JPanel implements ActionListener {

	private FacePath fp;
	private FacebookSearch fs;

	private JPanel resultPanel;
	private JTextArea reportTextArea;
	private JButton helpButton;
	private JButton newSearchButton;
	
	private Thread pumpThread;
	private GraphEventPump pump;

	/**
	 * Konstruktor
	 * Erstellt die Anzeige
	 */
	public GraphView(FacePath fp) {
		this.fp = fp;
		this.initialize();
		if (fs == null) {
			this.fs = fp.getFS();
		}
		Viewer viewer = new Viewer(fs.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		fs.getFbNetwork().setGraphViewer(viewer);
		View view = viewer.addDefaultView(false);
		
		pump = new GraphEventPump(this, fs.getFbNetwork().getGraph(),viewer);
		pumpThread = new Thread(pump);
        pump.start();
        pumpThread.start();
		
		GridBagConstraints gbc_view = new GridBagConstraints();
		gbc_view.fill = GridBagConstraints.BOTH;
		gbc_view.insets = new Insets(10, 30, 5, 30);
		gbc_view.gridx = 0;
		gbc_view.gridy = 0;
		resultPanel.add(view, gbc_view);
	}

	/**
	 * Ruft bei Drücken des Buttons "Suche" den Such-Bereich auf
	 * Reagiert auf Nodeklicks im Graph
	 * @see		java.awt.event.ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newSearchButton) {
			pump.stop();
			pumpThread.stop();
			this.fp.showView("search");
		} else if (e.getSource() == pump && e.getID()==1) {
			//do something with nodeevent
			System.out.println(fs.getFbNetwork().getGraphCollection().get(e.getActionCommand()).getLastName()+" clicked");
		} else if (e.getSource() == this.helpButton) {
			HelpView.getHelpView(this.getClass()).toFront();;
		}
	}

	/**
	 * Initialisiert den Inhalt der Anezige
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
