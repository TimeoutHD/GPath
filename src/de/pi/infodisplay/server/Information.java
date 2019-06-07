package de.pi.infodisplay.server;

import java.io.File;
import java.nio.file.Paths;

import de.pi.infodisplay.shared.security.User;

public class Information {

	private File file;
	private int id;
	private User publisher;
	
	public Information(int id, String path, User publisher) {
		this.id = id;
		this.file = Paths.get(path).toFile();
		this.publisher = publisher;
	}

	public File getFile() {
		return file;
	}

	public int getID() {
		return id;
	}

	public User getPublisher() {
		return publisher;
	}
}
