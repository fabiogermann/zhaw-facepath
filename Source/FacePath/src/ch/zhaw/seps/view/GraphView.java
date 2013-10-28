package ch.zhaw.seps.view;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.zhaw.seps.FacePath;
import ch.zhaw.seps.fb.FacebookProvider;
import ch.zhaw.seps.fb.FacebookSearch;

public class GraphView extends Panel implements ActionListener {

	private Button helpBtn;
	private Button newSearchBtn;
	private FacePath fp;
	private FacebookSearch fs;

	public GraphView(FacePath myFB) {
		this.setLayout(null);
		setLocation(150, 0);
		this.setSize(874, 768);
		this.fp = myFB;

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
		// irgend sowas sollen wir machen um den graphen anzuzeigen
		//
		//if(fs != null){
		//	this.fs = fp.getFS();
		//	fs.displayGraph();
		//}
		
		// A LA
		//
		//import org.graphstream.ui.swingViewer.View;
		//import org.graphstream.ui.swingViewer.Viewer;
		// ....
		//Graph graph = new MultiGraph("embedded");
		//Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		// ...
		//View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
		// ...
		//myJFrame.add(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newSearchBtn) {
			FacePath.showView("search");
		}
	}
}
