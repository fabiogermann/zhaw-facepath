package ch.zhaw.seps.view;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.zhaw.seps.FacePath;

public class GraphView extends Panel implements ActionListener {

	private Button helpBtn;
	private Button newSearchBtn;

	public GraphView() {
		this.setLayout(null);
		setLocation(150, 0);
		this.setSize(874, 768);

		Panel resultForm = (Panel) FacePath.addComponentToPanel(new Panel(null), this, 0, 30, 830, 700);

		FacePath.addComponentToPanel(new Label("Gemeinsamkeiten:"), resultForm, 80, 500, 200, 30);
		FacePath.addComponentToPanel(new Label("Übereinstimmende \"Gefällt mir\" Angaben"), resultForm, 80, 520, 370,
		        30);
		FacePath.addComponentToPanel(new Label("- Coca Cola"), resultForm, 80, 540, 120, 30);
		FacePath.addComponentToPanel(new Label("- YouTube"), resultForm, 80, 560, 120, 30);

		helpBtn = (Button) FacePath.addComponentToPanel(new Button("Hilfe"), resultForm, 470, 640, 130, 50);
		helpBtn.addActionListener(this);
		newSearchBtn = (Button) FacePath.addComponentToPanel(new Button("Neue Suche"), resultForm, 630, 640, 180, 50);
		newSearchBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newSearchBtn) {
			FacePath.showView("search");
		}
	}
}
