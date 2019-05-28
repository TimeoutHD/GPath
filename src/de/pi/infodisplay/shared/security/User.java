package de.pi.infodisplay.shared.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.Server;
import de.timeout.libs.MySQL;
import de.timeout.libs.MySQL.Table;

public class User {
		
	private UUID id;
	private String name;
	private String password;
	
	/**
	 * Erzeugt einen neuen User
	 * @param id Die ID des Users
	 * @param name Der Benutzername des Users
	 * @param password Das Anmeldepasswort des Users 
	 * @param db Die Datenbank, wo die User gespeichert werden
	 * @param isEncoded Ist das Passwort bereits encodiert ?
	 */
	public User(UUID id, String name, String password, MySQL db, boolean isEncoded) {

	}
	
	public User(UUID id, String name, String password, boolean isEncoded) {
		this.id = id;
		this.name = name;
		this.password = isEncoded ? password : User.encode(password);
	}
		
	/**
	 * Gibt ein Userobjekt mit dem eingegebenen Namen aus der Datenbank zurück
	 * @param name Der Benutzername des Users
	 * @return Gibt neuen User aus
	 */
	public static User getFromDataBaseByName(String name) {
		try {
			Table table = Server.getMySQL().executeStatement("SELECT * FROM users WHERE name = ?", name);
			return new User(UUID.fromString((String) table.getElement("uuid", 0).getValue()),
					table.getElement("name", 0).getValue().toString(),
					table.getElement("password", 0).getValue().toString(), true);
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
		}
		return null;
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
	
	public void saveInDatabase() {
			try {
				if(!exists()) Server.getMySQL().executeVoidStatement("INSERT INTO users(id, name, password, admin) VALUES(?,?,?)", id.toString(), name, password);
				else Server.getMySQL().executeVoidStatement("UPDATE name = ?, password = ? FROM users WHERE uuid = ?", name, password, id.toString());
			} catch (SQLException e) {
				Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
			}
	}
	
	/**
	 * Überprüft, ob der User in der Datenbank existiert
	 * @return Existiert der User in der Datenbank
	 * @throws SQLException
	 */
	public boolean exists() throws SQLException {
		Table table = Server.getMySQL().executeStatement("SELECT uuid FROM users WHERE uuid = ?", id.toString());
		return !table.isEmpty();
	}
	
	/**
	 * Überprüft das Passwort
	 * @param password Passwort
	 * @return
	 */
	public boolean compare(String password) {
		return this.password.equals(password);
	}
	
	/**
	 * Setzt ein neues Passwort
	 * @param oldpw Altes Passwort
	 * @param newpw Neues Passwort
	 * @return War das angegebene Passwort richtig?
	 */
	public boolean setPassword(String oldpw, String newpw) {
		if(compare(User.encode(oldpw))) {
			this.password = User.encode(newpw);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return ID des User-Objekts 
	 */
	public UUID getUniqueId() {
		return this.id;
	}

	/**
	 * 
	 * @return Name des User-Objekts 
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Encodiert ein Passwort mit Hilfe der SHA-256 Codierung
	 * @param Passwort
	 * @return Encodiertes Passwort
	 */
	public boolean isAdmin() {
		Table table = null;
		try {
			table = Server.getMySQL().executeStatement("SELECT admin FROM User WHERE uuid = ?", id.toString());
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, "Critical Error with Database", e);
			return false;
		}
		return Integer.valueOf((String) table.getElement("admin", 0).getValue()) == 1;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass().equals(this.getClass())) {
			User user = (User) obj;
			return user.getName().equalsIgnoreCase(name) && user.getUniqueId().toString().equalsIgnoreCase(id.toString());
		}
		return false;
	}
	
	
}
