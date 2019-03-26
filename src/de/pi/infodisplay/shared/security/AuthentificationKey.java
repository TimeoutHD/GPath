package de.pi.infodisplay.shared.security;

import java.util.Base64;
import java.util.UUID;

import io.netty.util.CharsetUtil;

/**
 * Jeder User braucht einen Sicherheitskey, damit eine End-zu-End Anmeldung möglich ist und kein Crack funktioniert
 * Dazu werden drei UUIDs, die zufällig generiert werden mit Base64 verschlüsselt. 
 * Die Kodierung dieses Schlüssels ist wiederum UTF-8.
 * 
 * @author piA
 *
 */
public class AuthentificationKey {

	private static final Base64.Encoder encoder = Base64.getEncoder();
	
	private String key;
	
	/**
	 * Konstruktor zum Erstellen eines neuen Sicherheitsschlüssels für eine Client-Server Verbindung.
	 */
	public AuthentificationKey() {
		key = new String(encoder.encode(String.join("",
				UUID.randomUUID().toString(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), UUID.randomUUID().toString()).getBytes(CharsetUtil.UTF_8)), CharsetUtil.UTF_8);
	}
	
	/**
	 * Wrapper-Konstruktor zum Erstellen der Klasse mit einem bereits vorhandenen Schlüssel.
	 * @param key der bereits vorhandene Schlüssel.
	 */
	public AuthentificationKey(String key) {
		this.key = key;
	}
	
	/**
	 * Diese Methode überprüft, ob der Sicherheitsschlüssel des Parameters mit dem Sicherheitsschlüssel dieses Objektes
	 * übereinstimmt. Das Ergebnis wird nun als Boolean zurückgegeben.
	 * 
	 * @param key Dieses Argument ist der zu überprüfende Schlüssel
	 * @return Das Überprüfungsergebnis als Boolean
	 */
	public boolean matches(String key) {
		return this.key.equals(key);
	}
	
	/**
	 * Diese Methode gibt den aktuellen Sicherheitsschlüssel zurück.
	 * @return Der aktuelle Sicheheitsschlüssel
	 */
	public String getKeySet() {
		return key;
	}
}
