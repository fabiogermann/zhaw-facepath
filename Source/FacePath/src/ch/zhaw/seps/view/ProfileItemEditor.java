package ch.zhaw.seps.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.zhaw.seps.fb.FacebookProfile;
import ch.zhaw.seps.fb.FacebookProvider;

public class ProfileItemEditor implements ComboBoxEditor, DocumentListener {
	private JPanel editor;
	private JLabel profilePictureLabel;
	private JTextField profileStringTextField;

	private FacebookProfile currentSelectedProfile;

	public ProfileItemEditor() {
		this.currentSelectedProfile = null;

		this.editor = new JPanel();
		this.editor.setBackground(Color.WHITE);
		GridBagLayout gbl_editor = new GridBagLayout();
		gbl_editor.columnWidths = new int[] { 0, 0, 0 };
		gbl_editor.rowHeights = new int[] { 0, 0 };
		gbl_editor.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_editor.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		editor.setLayout(gbl_editor);

		this.profilePictureLabel = new JLabel();
		GridBagConstraints gbc_profilePictureLabel = new GridBagConstraints();
		gbc_profilePictureLabel.insets = new Insets(0, 0, 0, 0);
		gbc_profilePictureLabel.gridx = 0;
		gbc_profilePictureLabel.gridy = 0;
		this.editor.add(profilePictureLabel, gbc_profilePictureLabel);

		this.profileStringTextField = new JTextField();
		GridBagConstraints gbc_profileStringTextField = new GridBagConstraints();
		gbc_profileStringTextField.fill = GridBagConstraints.BOTH;
		gbc_profileStringTextField.gridx = 1;
		gbc_profileStringTextField.gridy = 0;
		this.editor.add(profileStringTextField, gbc_profileStringTextField);
		this.profileStringTextField.getDocument().addDocumentListener(this);
	}

	@Override
	public Component getEditorComponent() {
		return this.editor;
	}

	@Override
	public void setItem(Object anObject) {
		if (anObject != null && anObject instanceof FacebookProfile) {
			this.profileStringTextField.setText(anObject.toString());
			this.currentSelectedProfile = (FacebookProfile) anObject;
			this.profilePictureLabel.setIcon(FacebookProvider.getImageIconFromUsername(this.currentSelectedProfile
			        .getUserUIDString()));

		} else if (anObject != null && anObject instanceof String) {
			this.currentSelectedProfile = null;
			this.profilePictureLabel.setIcon(new ImageIcon());
			this.profileStringTextField.setText(anObject.toString());
		}
	}

	@Override
	public Object getItem() {
		if (this.currentSelectedProfile != null) {
			return this.currentSelectedProfile;
		}
		return this.profileStringTextField.getText();
	}

	@Override
	public void selectAll() {
	}

	@Override
	public void addActionListener(ActionListener l) {
		this.profileStringTextField.addActionListener(l);
	}

	@Override
	public void removeActionListener(ActionListener l) {
		this.profileStringTextField.removeActionListener(l);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (this.currentSelectedProfile != null
		        && !this.currentSelectedProfile.toString().equals(this.profileStringTextField.getText())) {
			this.currentSelectedProfile = null;
			this.profilePictureLabel.setIcon(new ImageIcon());
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (this.currentSelectedProfile != null
		        && !this.currentSelectedProfile.toString().equals(this.profileStringTextField.getText())) {
			this.currentSelectedProfile = null;
			this.profilePictureLabel.setIcon(new ImageIcon());
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		if (this.currentSelectedProfile != null
		        && !this.currentSelectedProfile.toString().equals(this.profileStringTextField.getText())) {
			this.currentSelectedProfile = null;
			this.profilePictureLabel.setIcon(new ImageIcon());
		}
	}

}