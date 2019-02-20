package de.timeout.libs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a hook into a MySQL-Database.
 * 
 * 
 * @author Timeout
 */
public class MySQL {
	
	private static final String SQL_ERROR = "Could not create Statement";

	private String host, database, username, password;
	private int port;
	
	private Connection connection;
	
	public MySQL(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;

		connect();
	}
	
	/**
	 * Verbindet zur MySQL-Datenbank
	 */
	private void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, "Verbindung zur Datenbank kann nicht aufgebaut werden", e);
		}
	}
	
	/**
	 * Trennt doe Verbindung zur MySQL-Datenbank
	 */
	public void close() {
		if(isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.getGlobal().log(Level.SEVERE, "Die Verbindung konnte nicht geschlossen werden", e);
			}
		}
	}
	
	/**
	 * Überprüft, ob die Verbindung aufgebaut ist.
	 * @return ist verbunden
	 */
	public boolean isConnected() {
		try {
			connection.createStatement();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Gibt die MySQL-Verbindung zurück
	 * @return die Verbindung
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Gibt ein Objekt an einer gewissen Stelle der Tabelle zurück, die im Statement definiert wird.
	 * Gibt null zurück, wenn ein Fehler auftritt.
	 * @param preparedStatement Das SQL-Statement
	 * @param args Die Parameter, falls vorhanden
	 * @return Das Objekt. Oder null, wenn es ein Fehler gab
	 */
	public Object getValue(String preparedStatement, String... args) {
		if(!isConnected()) connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			Object ans = null;
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) ans = rs.getObject(getDistinct(preparedStatement));
			}
			return ans;
		} catch(SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, SQL_ERROR, e);
		}
		return null;
	}
	
	/**
	 * Gibt den gesuchten Spaltennamen zurück
	 * @param preparedStatement Das Statement
	 * @return der gesuchte Spaltenname
	 */
	private String getDistinct(String preparedStatement) {
		String[] split = preparedStatement.split(" ");
		List<String> args = new ArrayList<String>();
		
		boolean copy = false;
		for(int i = 0; i < split.length; i++) {
			if(copy) {
				if("FROM".equalsIgnoreCase(split[i])) break;
				else args.add(split[i]);
			} else if("SELECT".equalsIgnoreCase(split[i])) copy = true;
		}
		return String.join(" ", args);
	}
	
	/**
	 * Fügt die Parameter (Argumente) in das Statement ein und gibt dieses zurück
	 * @param statement das Statement
	 * @param args die Argumente
	 * @return Das konvertierte Statement
	 * @throws SQLException Wenn es ein Fehler bei der Konvertierung gab
	 */
	private PreparedStatement convertStatement(String statement, String[] args) throws SQLException {
		//Do not close this Statement here!!
		PreparedStatement ps = connection.prepareStatement(statement);
		int index = 0;
		for(int i = 0; i < args.length; i++) {
			ps.setObject(index +1, args[index]);
			index++;
		}
		return ps;
	}
	
	/**
	 * Gibt den Tabellennamen des Statements zurück
	 * @param preparedStatement Das Statement
	 * @return der Tabellenname
	 * @throws SQLException Wenn kein Tabellenname existiert
	 */
	public String getTableName(String preparedStatement) throws SQLException {
		String[] split = preparedStatement.split(" ");
		for(int i = 0; i < split.length; i++) {
			if("FROM".equalsIgnoreCase(split[i]))return split[i +1];
		}
		throw new IllegalArgumentException("Could not find tablename");
	}
	
	/**
	 * Der MySQL-Update Befehl. Damit können Werte aktualisiert (überschrieben) werden.
	 * @param preparedStatement Das Statement
	 * @param args Die Argumente (optional)
	 */
	public void update(String preparedStatement, String... args) {
		if(!isConnected())connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			ps.executeUpdate();
		} catch(SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, SQL_ERROR, e);			
		}
	}
	
	/**
	 * Der MySQL-Delete Befehl. Damit kann ein Wert in der Tabelle gelöscht werden.
	 * @param preparedStatement Das Statement
	 * @param args Die Argumente (optional)
	 */
	public void delete(String preparedStatement, String... args) {
		if(!isConnected()) connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			ps.execute();
		} catch(SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, SQL_ERROR, e);
		}
	}
	
	/**
	 * Der MySQL-Insert Befehl. Damit kann eine neue Spalte in der Tabelle erstellt werden,
	 * die Werte aus dem Statement übernimmt
	 * @param preparedStatement Das Statement
	 * @param args Die Argumente (optional).
	 */
	public void insert(String preparedStatement, String... args) {
		if(!isConnected())connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			ps.execute();
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, SQL_ERROR, e);
		}
	}
	
	/**
	 * Überprüft, ob ein bestimmter Wert in der Datenbank ist.
	 * Dabei ist die Genauigkeit vom Statement abhängig.
	 * Beispiel: SELECT <Spalte> FROM <Tabellenname> WHERE <Spalte eines bekannten Werts> = <Wert an dieser bestimmten Spalte>
	 * @param preparedStatement Das Statement
	 * @param args Die Argumente des Statements (optional)
	 * @return Das Ergebnis
	 */
	public boolean hasResult(String preparedStatement, String... args) {
		if(!isConnected()) connect();
		try(ResultSet rs = convertStatement(preparedStatement, args).executeQuery()) {
			return rs.next();
		} catch(SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, SQL_ERROR, e);
		}
		return false;
	}
	
	/**
	 * Gibt die gesamte Tabelle als zweidimensionalen String Array aus.
	 * @param preparedStatement das Statement
	 * @param args die Argumente (optional)
	 * @return die Tabelle als String[][]
	 */
	public String[][] executeStatement(String preparedStatement, String... args) {
		if(!isConnected()) connect();
		try(ResultSet rs = convertStatement(preparedStatement, args).executeQuery()) {
			int columnCount = rs.getMetaData().getColumnCount();
			Queue<String[]> rows = new LinkedBlockingQueue<>();
			while(rs.next()) {
				String[] array = new String[columnCount];
				for(int i = 0; i < array.length; i++) array[i] = rs.getString(i);
				rows.add(array);
			}
			
			String[][] map = new String[rows.size()][columnCount];
			for(int i = 0; i < rows.size(); i++) map[i] = rows.poll();
			return map;
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, SQL_ERROR, e);
		}
		return new String[0][0];
	}
}
