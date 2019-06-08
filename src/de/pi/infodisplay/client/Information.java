package de.pi.infodisplay.client;

import java.io.File;

public class Information {

	private String title;
	private File infoFile;
	
	public Information(String title, File infoFile) {
		this.title = title;
		this.infoFile = infoFile;
	}

	public String getTitle() {
		return title;
	}

	public File getInfoFile() {
		return infoFile;
	}
	
}
