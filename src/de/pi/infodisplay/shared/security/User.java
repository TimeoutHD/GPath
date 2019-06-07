package de.pi.infodisplay.shared.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pi.infodisplay.Main;
import de.timeout.libs.MySQL;
import de.timeout.libs.MySQL.Table;

public class User {
	
	private static final MySQL mysql = new MySQL("localhost", 3306, "informationdisplay");
	
	private UUID id;
	private String name;
	private String password;
	
	static {
		try {
			mysql.connect("pi", "piA");
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, "Cannot connect to MySQL-Database", e);
		}
	}
	
	public User(UUID id, String name, String password, boolean isEncoded) {
		this.id = id;
		this.name = name;
		this.password = isEncoded ? password : User.encode(password);
	}
	
	public static User getFromDataBaseByName(String name) {
		try {
			Table table = mysql.executeStatement("SELECT * FROM users WHERE name = ?", name);
			return new User(UUID.fromString((String) table.getElement("uuid", 0).getValue()),
					table.getElement("name", 0).getValue().toString(),
					table.getElement("password", 0).getValue().toString(), true);
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
		}
		return null;
	}
	
//	public static void setAdmin(User executor, String username, boolean admin) {
//		if(executor.isAdmin()) {
//			
//		}
//	}
	
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
	
	public void saveInDatabase() {
			try {
				if(!exists()) mysql.executeVoidStatement("INSERT INTO users(id, name, password, admin) VALUES(?,?,?)", id.toString(), name, password);
				else mysql.executeVoidStatement("UPDATE name = ?, password = ? FROM users WHERE uuid = ?", name, password, id.toString());
			} catch (SQLException e) {
				Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
			}
	}
	
	public boolean exists() throws SQLException {
		Table table = mysql.executeStatement("SELECT uuid FROM users WHERE uuid = ?", id.toString());
		return !table.isEmpty();
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
	
	public boolean isAdmin() {
		Table table = null;
		try {
			table = mysql.executeStatement("SELECT admin FROM User WHERE uuid = ?", id.toString());
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, "Critical Error with Database", e);
			return false;
		}
		return Integer.valueOf((String) table.getElement("admin", 0).getValue()) == 1;
	}
}
