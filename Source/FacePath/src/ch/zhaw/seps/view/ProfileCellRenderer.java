/**
 * Bearbeitet die Profile (Knoten) vor, damit diese im Ergebnis korrekt eingefügt werden können
 * 
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ch.zhaw.seps.fb.FacebookProfile;
import ch.zhaw.seps.fb.FacebookFacade;

public class ProfileCellRenderer extends JLabel implements ListCellRenderer<FacebookProfile> {

	public ProfileCellRenderer() {
		this.setOpaque(true);
	}

	/**
	 * Bearbeitet einen Knoten und gibt diesen bearbeitet zurück
	 * 
	 * @param		profileList		Liste mit Profilen
	 * @param		profile			Ein bestimmtes Profil, dessen Knoten bearbeitet werden muss
	 * @param		index			Stelle des angegebenen Profils in der Liste
	 * @param		isSelected		Markiert, ob der Knoten ausgewählt wurde (Ist Teil der Verbindung)
	 * @param		cellHasFocus	Bestimmt, ob dieser Knoten fokussiert wird (Parameter der Suche)
	 * @return		Bearbeitete Komponente
	 */
	public Component getListCellRendererComponent(JList<? extends FacebookProfile> profileList,
	        FacebookProfile profile, int index, boolean isSelected, boolean cellHasFocus) {

		this.setIcon(FacebookFacade.getImageIconFromUsername(profile.getUserUIDString()));
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