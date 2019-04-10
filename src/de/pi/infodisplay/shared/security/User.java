package de.pi.infodisplay.shared.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.timeout.libs.MySQL;

public class User {
	
	private MySQL mysql;
	private UUID id;
	private String name;
	private String password;
	private boolean isAdmin;
	
	public User(UUID id, String name, String password, MySQL db, boolean isEncoded) {
		this.id = id;
		this.name = name;
		this.password = isEncoded ? password : User.encode(password);
		this.mysql = db;
	}
	
	public static User getFromDataBaseByName(MySQL db, String name) {
		try {
			String[][] table = db.executeStatement("SELECT * FROM users WHERE name = ?", name);
			return new User(UUID.fromString(table[0][0]), table[0][1], table[0][2], db, true);
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
		}
		return null;
	}
	
	public static void setAdmin(User excutor, String username, boolean admin) {
		
	}
	
	public void saveInDatabase() {
			try {
				if(!exists()) mysql.executeVoidStatement("INSERT INTO users(id, name, password, admin) VALUES(?,?,?)", id.toString(), name, password);
				else mysql.executeVoidStatement("UPDATE name = ?, password = ? FROM users WHERE uuid = ?", name, password, id.toString());
			} catch (SQLException e) {
				Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
			}
	}
	
	public boolean exists() throws SQLException{
		return mysql.hasResult("SELECT uuid FROM users WHERE uuid = ?", id.toString());
	}
	
	public boolean compare(String password) {
		return this.password.equals(password);
	}
	
	public boolean setPassword(String oldpw, String newpw) {
		if(compare(User.encode(oldpw))) {
			this.password = User.encode(newpw);
			return true;
		}
		return false;
	}

	public UUID getUniqueId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public static String encode(String pw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pw.getBytes());
			byte[] bytes = md.digest();
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++) 
	        	sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			Main.LOG.log(Level.SEVERE, "Cannot get encode code algorythm SHA-256", e);
			return null;
		}
	}
}
