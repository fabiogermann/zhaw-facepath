package ch.zhaw.seps.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
/**
 * Verarbeitet Events auf dem Graphen parallel zur Ausführung des restlichen Programms
 * 
 * @author		SEPS Gruppe 2
 */
public class GraphEventPump implements Runnable, ViewerListener {
	
	private boolean run;
	private ActionListener al;
	private ViewerPipe pipe;
	
	
	public GraphEventPump(ActionListener al, Graph graph, Viewer viewer) {
		ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
		this.al = al;
		this.pipe = fromViewer;
	}

	@Override
	public void run() {
		while(run) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pipe.pump();
		}
	}
	
	public void start() {
		run = true;
	}
	
	public void stop() {
		run = false;
	}

	@Override
	public void buttonPushed(String id) {
		al.actionPerformed(new ActionEvent(this,0,id));
	}

	@Override
	public void buttonReleased(String id) {
		al.actionPerformed(new ActionEvent(this,1,id));
	}

	@Override
	public void viewClosed(String id) {
		this.stop();
	}

}
