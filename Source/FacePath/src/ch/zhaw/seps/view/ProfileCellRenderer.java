package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.Component;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ch.zhaw.seps.fb.FacebookProfile;

public class ProfileCellRenderer extends JLabel implements ListCellRenderer<FacebookProfile> {

	public ProfileCellRenderer() {
		this.setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends FacebookProfile> profileList,
	        FacebookProfile profile, int index, boolean isSelected, boolean cellHasFocus) {

		try {
			this.setIcon(new ImageIcon(new URL("https://graph.facebook.com/" + profile.getUserUIDString() + "/picture")));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setText(profile.toString());

		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = profileList.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

			background = Color.BLUE;
			foreground = Color.WHITE;

			// check if this cell is selected
		} else if (isSelected) {
			background = Color.BLUE;
			foreground = Color.WHITE;

			// unselected, and not the DnD drop location
		} else {
			background = Color.WHITE;
			foreground = Color.BLACK;
		}

		this.setBackground(background);
		this.setForeground(foreground);

		return this;
	}
}