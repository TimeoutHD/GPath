package de.pi.infodisplay.client.gui;

public class Information {
	private String title;
	private String filepath;
	
	public Information(String title, String filepath) {
		this.title = title;
		this.filepath = filepath;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getFilePath() {
		return this.filepath;
	}
}
