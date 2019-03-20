package de.pi.infodisplay.shared.security;

import java.util.Base64;

public class User {
	
	private static final Base64.Encoder encoder = Base64.getEncoder();
	
	private int id;
	private String name;
	private String password;
	
	public User(int id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = encoder.encodeToString(password.getBytes());
	}
	
	public boolean compare(String password) {
		return this.password == encoder.encodeToString(password.getBytes());
	}
	
	public boolean setPassword(String oldpw, String newpw) {
		if(compare(oldpw)) {
			this.password = encoder.encodeToString(newpw.getBytes());
			return true;
		}
		return false;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
}
