import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

import javax.swing.*;

public class Test extends JFrame {
	final JList list = new JList(new String[] {"a","b","c"});
	list.addMouseMotionListener(new MouseMotionListener() {
	    public void mouseMoved(MouseEvent e) {
	        final int x = e.getX();
	        final int y = e.getY();
	        // only display a hand if the cursor is over the items
	        final Rectangle cellBounds = list.getCellBounds(0, list.getModel().getSize() - 1);
	        if (cellBounds != null && cellBounds.contains(x, y)) {
	            list.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        } else {
	            list.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	        }
	    }

	    public void mouseDragged(MouseEvent e) {
	    }
	});
	
}