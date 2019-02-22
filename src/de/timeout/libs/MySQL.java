package de.timeout.libs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
	 * Dabei wird ein leeres Void-Statement an die Datenbank gesendet. 
	 * Schlägt diese fehl, so wird eine Exception geschmissen, die schlussendlich false zurückgibt.
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
	 * 
	 * Gibt ein Objekt an einer gewissen Stelle der Tabelle zurück, die im Statement definiert wird.
	 * 
	 * @param preparedStatement Das SQL-Statement
	 * @param args Die Argumente, falls vorhanden
	 * @return Das gesuchte Object als String
	 * @throws SQLException Wenn das Statement falsch ist.
	 */
	public String getValue(String preparedStatement, String... args) throws SQLException {
		if(!isConnected()) connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			String ans = null;
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) ans = rs.getString(getDistinct(preparedStatement));
			}
			return ans;
		} catch(SQLException e) {
			throw new IllegalArgumentException(SQL_ERROR, e);
		}
	}
	
	/**
	 * Gibt den gesuchten Spaltennamen zurück
	 * @param preparedStatement Das Statement
	 * @return der gesuchte Spaltenname
	 * @throws SQLException Wenn das Statement falsch ist.
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
			ps.setString(index +1, args[index]);
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
	 * Diese Methode führt ein Void-Statement der Sprache SQL aus. 
	 * Void-Statements sind Befehle, die keinen Rückgabewert besitzen. (Beispiel: INSERT, UPDATE, DELETE)
	 * Sollte das Statement kein Void-Statement sein, so wird nichts passieren.
	 * @param preparedStatement
	 * @param args
	 */
	public void executeVoidStatement(String preparedStatement, String... args) throws SQLException {
		if(!isConnected())connect();
		if(!"SELECT".startsWith(preparedStatement.toUpperCase(Locale.getDefault()))) {
			try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
				ps.executeUpdate();
			} catch(SQLException e) {
				throw new IllegalArgumentException(SQL_ERROR, e);
			}
		}
	}
	
	/**
	 * Überprüft, ob ein bestimmter Wert in der Datenbank ist.
	 * Dabei ist die Genauigkeit vom Statement abhängig.
	 * Beispiel: SELECT <Spalte> FROM <Tabellenname> WHERE <Spalte eines bekannten Werts> = <Wert an dieser bestimmten Spalte>
	 * @param preparedStatement Das Statement
	 * @param args Die Argumente des Statements (optional)
	 * @return Das Ergebnis
	 * @throws SQLException Wenn das Statement falsch ist.
	 */
	public boolean hasResult(String preparedStatement, String... args) throws SQLException {
		if(!isConnected()) connect();
		try(ResultSet rs = convertStatement(preparedStatement, args).executeQuery()) {
			return rs.next();
		} catch(SQLException e) {
			throw new IllegalArgumentException(SQL_ERROR, e);
		}
	}
	
	/**
	 * Gibt die gesamte Tabelle als zweidimensionalen String Array aus. Sollte das Statement nicht falsch sein, wird ein
	 * leerer zweidimensionaler String Array zurückgegeben
	 * @param preparedStatement das Statement
	 * @param args die Argumente (optional)
	 * @return die Tabelle als String[][]
	 */
	public String[][] executeStatement(String preparedStatement, String... args) throws SQLException {
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
