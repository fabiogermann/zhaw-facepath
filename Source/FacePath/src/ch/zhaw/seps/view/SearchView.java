package ch.zhaw.seps.view;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookProvider;

public class SearchView extends Panel implements ActionListener {

	private Panel searchForm;

	private TextField startUser;
	private TextField endUser;

	private Checkbox events;
	private Checkbox profilePics;
	private Checkbox nationalOnly;

	private Button helpBtn;
	private Button searchBtn;

	private Button resultBtn;

	private Panel resultForm;
	
	private FacePath fp;

	public SearchView(FacePath myFB) {
		this.setLayout(null);
		setLocation(150, 0);
		this.setSize(874, 768);
		this.fp = myFB;

		searchForm = (Panel) FacePath.addComponentToPanel(new Panel(null), this, 0, 30, 830, 500);

		FacePath.addComponentToPanel(new Label("Bitte geben Sie die Usernamen der Start- und Ziel-Benutzer ein."),
		        searchForm, 80, 40, 620, 30);
		FacePath.addComponentToPanel(new Label("Start-Benutzer:"), searchForm, 80, 80, 160, 30);
		startUser = (TextField) FacePath.addComponentToPanel(new TextField(50), searchForm, 280, 80, 470, 30);
		FacePath.addComponentToPanel(new Label("Ziel-Benutzer:"), searchForm, 80, 120, 150, 30);
		endUser = (TextField) FacePath.addComponentToPanel(new TextField(50), searchForm, 280, 120, 470, 30);

		FacePath.addComponentToPanel(new Label("Suchoptionen:"), searchForm, 80, 220, 160, 30);
		events = (Checkbox) FacePath.addComponentToPanel(new Checkbox("Veranstaltungen miteinbeziehen"), searchForm,
		        80, 240, 420, 30);
		profilePics = (Checkbox) FacePath.addComponentToPanel(new Checkbox("Profilbilder anzeigen"), searchForm, 80,
		        260, 290, 30);
		nationalOnly = (Checkbox) FacePath.addComponentToPanel(new Checkbox("Nur auf nationaler Ebene suchen"),
		        searchForm, 80, 280, 430, 30);

		helpBtn = (Button) FacePath.addComponentToPanel(new Button("Hilfe"), searchForm, 520, 450, 130, 50);
		helpBtn.addActionListener(this);
		searchBtn = (Button) FacePath.addComponentToPanel(new Button("Suche"), searchForm, 680, 450, 130, 50);
		searchBtn.addActionListener(this);

		resultForm = (Panel) FacePath.addComponentToPanel(new Panel(null), this, 0, 580, 830, 135);
		resultForm.setVisible(false);

		FacePath.addComponentToPanel(new Label("Die Suche ergab mehere Treffer"), resultForm, 80, 40, 370, 30);
		resultBtn = (Button) FacePath
		        .addComponentToPanel(new Button("Ergebnis anzeigen"), resultForm, 510, 80, 280, 50);
		resultBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchBtn) {
			resultForm.setVisible(true);
		}
		if (e.getSource() == resultBtn) {
			FacePath.showView("result");
		}
	}
}
