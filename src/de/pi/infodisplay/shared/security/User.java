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
	
	
	/**
	 * Erstellt einen neuen User
	 * @param id ID des Users
	 * @param name Name
	 * @param password Passwort
	 * @param db Datenbank
	 * @param isEncoded Ist das Passwort schon encodiert?
	 */
	public User(UUID id, String name, String password, MySQL db, boolean isEncoded) {
		this.id = id;
		this.name = name;
		this.password = isEncoded ? password : User.encode(password);
		this.mysql = db;
	}
	
	
	/**
	 * Lädt einen User aus der angegebenen Datenbank
	 * @param db Datenbank, aus der gelesen werden soll
	 * @param name Name des Users
	 * @return Das gefundene User-Objekt
	 */
	public static User getFromDataBaseByName(MySQL db, String name) {
		try {
			String[][] table = db.executeStatement("SELECT * FROM users WHERE name = ?", name);
			return new User(UUID.fromString(table[0][0]), table[0][1], table[0][2], db, true);
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
		}
		return null;
	}
	
	/**
	 * Speichert den User in der Datenbank
	 */
	public void saveInDatabase() {
			try {
				if(!exists()) mysql.executeVoidStatement("INSERT INTO users(uuid, name, password) VALUES(?,?,?)", id.toString(), name, password);
				else mysql.executeVoidStatement("UPDATE name = ?, password = ? FROM users WHERE uuid = ?", name, password, id.toString());
			} catch (SQLException e) {
				Main.LOG.log(Level.SEVERE, "Cannot result SQL-Statement", e);
			}
	}
	
	/**
	 * Überprüft, ob der User in der Datenbank existiert
	 * @return Existiert der User in der Datenbank
	 * @throws SQLException
	 */
	public boolean exists() throws SQLException{
		return mysql.hasResult("SELECT uuid FROM users WHERE uuid = ?", id.toString());
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
	 * @return Wahr das angegebene Passwort richtig?
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
