package de.pi.infodisplay.client.gui;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PNGFileChooser extends JFileChooser {
		
	private static final long serialVersionUID = -2335937386713979525L;
	
	public PNGFileChooser(InformationAddDialog parent) {
		super();
		addActionListener(action -> {
			parent.setPathField(this.getSelectedFile().getAbsolutePath());
			setVisible(false);
		});
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG-Bild", "png");
		this.addChoosableFileFilter(filter);
		this.setSize(450, 300);
		this.setVisible(true);
	}
	
}
