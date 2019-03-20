package de.pi.infodisplay.shared.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
	
	private int id;
	private String name;
	private String password;
	
	public User(int id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = User.encode(password);
	}
	
	public boolean compare(String password) {
		return this.password.equals(User.encode(password));
	}
	
	public boolean setPassword(String oldpw, String newpw) {
		if(compare(oldpw)) {
			this.password = User.encode(newpw);
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
	
	private static String encode(String pw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pw.getBytes());
			byte[] bytes = md.digest();
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++)
	        {
	        	sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
