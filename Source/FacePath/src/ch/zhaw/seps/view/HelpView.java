/**
 * GUI der Hilfe
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HelpView extends JFrame implements ActionListener, WindowListener {

	private static HelpView singeltonHelpView = null;

	/**
	 * Singelton, gibt die View zurück
	 * 
	 * @param senderView
	 *            Die Klasse von der diese Methode aufgerufen wird.
	 *            this.getClass()
	 */
	public static HelpView getHelpView(Class senderView) {
		if (HelpView.singeltonHelpView == null) {
			HelpView.singeltonHelpView = new HelpView(senderView);
		}
		return HelpView.singeltonHelpView;
	}

	private JButton loginHelpButton;
	private JButton searchHelpButton;
	private JButton resultHelpButton;
	private JButton errorHelpButton;
	private JEditorPane helpTextEditorPane;

	/**
	 * Konstruktor, Erstellt die Anzeige Ist private wegen Singelton
	 * 
	 * @param senderView
	 *            Die Klasse von der diese Methode aufgerufen wird.
	 *            this.getClass()
	 */
	private HelpView(Class senderView) {
		this.initialize(senderView);
		this.setVisible(true);
	}

	/**
	 * Zeigt die Hilfe des geklickten Button an.
	 * 
	 * @see java.awt.event.ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == this.loginHelpButton) {
				this.helpTextEditorPane.setPage(getClass().getResource("/ch/zhaw/seps/view/resources/loginHelp.html"));
			} else if (e.getSource() == this.searchHelpButton) {
				this.helpTextEditorPane.setPage(getClass().getResource("/ch/zhaw/seps/view/resources/searchHelp.html"));
			} else if (e.getSource() == this.resultHelpButton) {
				this.helpTextEditorPane.setPage(getClass().getResource("/ch/zhaw/seps/view/resources/resultHelp.html"));
			} else if (e.getSource() == this.errorHelpButton) {
				this.helpTextEditorPane.setPage(getClass().getResource("/ch/zhaw/seps/view/resources/errorHelp.html"));
			} else {
				this.helpTextEditorPane.setText("");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Initialisiert den Inhalt der Anzeige
	 * 
	 * @param senderView
	 *            Die Klasse von der diese Methode aufgerufen wurde.
	 *            this.getClass()
	 */
	private void initialize(Class senderView) {
		this.setBackground(Color.WHITE);
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setLocation(this.getLocation().x + 500, this.getLocation().y);
		this.addWindowListener(this);

		getContentPane().setBackground(Color.WHITE);
		setTitle("Hilfe");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 10, 10, 10);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		loginHelpButton = new JButton("Hilfe zum Login");
		GridBagConstraints gbc_loginHelpButton = new GridBagConstraints();
		gbc_loginHelpButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginHelpButton.insets = new Insets(0, 0, 5, 0);
		gbc_loginHelpButton.gridx = 0;
		gbc_loginHelpButton.gridy = 0;
		panel.add(loginHelpButton, gbc_loginHelpButton);
		loginHelpButton.addActionListener(this);

		searchHelpButton = new JButton("Hilfe zur Suche");
		GridBagConstraints gbc_searchHelpButton = new GridBagConstraints();
		gbc_searchHelpButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchHelpButton.insets = new Insets(0, 0, 5, 0);
		gbc_searchHelpButton.gridx = 0;
		gbc_searchHelpButton.gridy = 1;
		panel.add(searchHelpButton, gbc_searchHelpButton);
		searchHelpButton.addActionListener(this);

		resultHelpButton = new JButton("Hilfe zum Ergebnis");
		GridBagConstraints gbc_resultHelpButton = new GridBagConstraints();
		gbc_resultHelpButton.insets = new Insets(0, 0, 5, 0);
		gbc_resultHelpButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_resultHelpButton.gridx = 0;
		gbc_resultHelpButton.gridy = 2;
		panel.add(resultHelpButton, gbc_resultHelpButton);
		resultHelpButton.addActionListener(this);

		errorHelpButton = new JButton("Fehleranalyse");
		GridBagConstraints gbc_errorHelpButton = new GridBagConstraints();
		gbc_errorHelpButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_errorHelpButton.gridx = 0;
		gbc_errorHelpButton.gridy = 3;
		panel.add(errorHelpButton, gbc_errorHelpButton);
		errorHelpButton.addActionListener(this);

		helpTextEditorPane = new JEditorPane("text/html", "");
		helpTextEditorPane.setContentType("text/html; charset=utf-8");
		helpTextEditorPane.setEditable(false);
		try {
			if (senderView != null) {
				if (senderView == LoginView.class) {
					this.helpTextEditorPane.setPage(getClass().getResource(
					        "/ch/zhaw/seps/view/resources/loginHelp.html"));
				} else if (senderView == SearchView.class) {
					this.helpTextEditorPane.setPage(getClass().getResource(
					        "/ch/zhaw/seps/view/resources/searchHelp.html"));
				} else if (senderView == GraphView.class) {
					this.helpTextEditorPane.setPage(getClass().getResource(
					        "/ch/zhaw/seps/view/resources/resultHelp.html"));
				} else {
					this.helpTextEditorPane.setText("");
				}
			} else {
				this.helpTextEditorPane.setText("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JScrollPane scrollPane = new JScrollPane(helpTextEditorPane);
		scrollPane.setBorder(null);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		getContentPane().add(scrollPane, gbc_textPane);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		HelpView.singeltonHelpView = null;
		this.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}