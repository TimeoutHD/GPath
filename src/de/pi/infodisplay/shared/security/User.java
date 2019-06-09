package de.pi.infodisplay.shared.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.server.security.ClientUser;
import de.timeout.libs.MySQL.Table;

public class User {
	
	private static final String SQL_ERROR = "Cannot result SQL-Statement";
		
	private UUID id;
	private String name;
	
	/**
	 * Erzeugt einen neuen User
	 * @param id Die ID des Users
	 * @param name Der Benutzername des Users
	 * @param password Das Anmeldepasswort des Users 
	 * @param db Die Datenbank, wo die User gespeichert werden
	 * @param isEncoded Ist das Passwort bereits encodiert ?
	 */
	public User(UUID id, String name) {
		this.id = id;
		this.name = name;
	}
		
	/**
	 * Gibt ein Userobjekt mit dem eingegebenen Namen aus der Datenbank zurück
	 * @param name Der Benutzername des Users
	 * @return Gibt neuen User aus
	 */
	public static User getFromDataBaseByName(String name) {
		try {
			Table table = Server.getMySQL().executeStatement("SELECT * FROM User WHERE name = ?", name);
			return new User(UUID.fromString(table.getValue("uuid", 0)),
					table.getValue("name", 0));
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, SQL_ERROR, e);
		}
		return null;
	}
	
	public static User getFromDatabaseByUUID(UUID uuid) {
		try {
			Table table = Server.getMySQL().executeStatement("SELECT * from User WHERE uuid = ?", uuid.toString());
			return new User(uuid, table.getValue("name", 0));
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, SQL_ERROR, e);
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
	
	public static void changePassword(User user, String newPassword, ClientUser operator) throws SQLException {
		if(operator.isAuthorized() && operator.getLoggedUser().isAdmin() && user.exists()) {
			Server.getMySQL().executeVoidStatement("UPDATE User SET password = ? WHERE uuid = ?", User.encode(newPassword), user.getUniqueId().toString());
		}
	}
	
	public void saveInDatabase() {
			try {
				if(!exists()) Server.getMySQL().executeVoidStatement("INSERT INTO User(id, name, password, admin) VALUES(?,?,?)", id.toString(), name, null, String.valueOf(0));
				else Server.getMySQL().executeVoidStatement("UPDATE name = ?, FROM User WHERE uuid = ?", name, id.toString());
			} catch (SQLException e) {
				Main.LOG.log(Level.SEVERE, SQL_ERROR, e);
			}
	}
		
	/**
	 * Überprüft, ob der User in der Datenbank existiert
	 * @return Existiert der User in der Datenbank
	 * @throws SQLException
	 */
	public boolean exists() throws SQLException {
		Table table = Server.getMySQL().executeStatement("SELECT uuid FROM User WHERE uuid = ?", id.toString());
		return !table.isEmpty();
	}
	
	/**
	 * Überprüft das Passwort
	 * @param password Passwort
	 * @return das Resultat
	 * @throws SQLException Wenn ein unerwarteter MySQL-Fehler passiert
	 */
	public boolean compare(String password) throws SQLException {
		return Server.getMySQL().executeStatement("SELECT password FROM User WHERE uuid = ?", id.toString()).getValue("password", 0).equals(password);
	}
	
	/**
	 * Setzt ein neues Passwort
	 * @param oldpw Altes Passwort
	 * @param newpw Neues Passwort
	 * @return Ob die Methode erfolgreoch ausgeführt wurde
	 * @throws SQLException Wenn ein unerwarteter MySQL-Fehler auftritt
	 */
	public boolean setPassword(String oldpw, String newpw) throws SQLException {
		if(compare(User.encode(oldpw))) {
			return Server.getMySQL().executeVoidStatement("UPDATE User SET password = ? WHERE id = ?", User.encode(newpw), id.toString());
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
		return Integer.valueOf(table.getValue("admin", 0)) == 1;
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
