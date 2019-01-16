package de.pi.infodisplay;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.server.Server;

/**
 * Die Klasse Main ist die Mainklasse des Java-Programms.
 * Ohne diese Klasse ist das fertige JAR-Archiv nicht ausf�hrbar.
 * 
 * @author PI A
 *
 */
public class Main {
	
	/**
	 * Dieses Field ist die Konstante für Logs innerhalb der JVM.
	 * 
	 * Logger sind eine bessere Möglichkeit, Konsolenausgaben zu machen,
	 * da diese mit einer Uhrzeit und ihrer Wichtigkeit gesendet werden.
	 * Außerdem können Throwables z.B. Exceptions damit gesendet werden.
	 * 
	 * Die statische Methode {@link Logger#getLogger(String)} erstellt dabei einen neuen Logger
	 * innerhalb der JVM mit dem Namen, der im Parameter eingegeben wird.
	 */
	public static final Logger LOG = Logger.getLogger("InformationDisplay");
	
	/**
	 * Dieses Field ist eine Konstante für den IPv4-Regex.
	 * Ein Regex kann in jeder Programmiersprache Strings auf Formatierungen überprüfen.
	 * Dieser Regex überprüft, ob der String das Format einer IPv4-Adresse unterstützt.
	 */
	private static final String IP_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	/**
	 * Das ist die Main-Methode innerhalb der Mainklasse, die aufgerufen wird, 
	 * sobaled die JAR ausgeführt wird. Dabei werden die Argumente, die hinter die JAR-Datei
	 * beim Ausführen gesetzt werden, in dieser Methode genutzt, um das Programm allgemein
	 * nutzbar zu machen.
	 * 
	 * Eine typische Nutzung f�r den Start des Servers wäre dies:
	 * {@code java -jar InformationDisplay.jar server -p 8800}
	 * Diese Zeile erstellt einen Server auf dem ausgeführten PC mit dem Port 8800.
	 * Sollte -p nicht angegeben werden, so wird der Standardport 8000 verwendet.
	 * 
	 * Eine typische Nutzung für den Start des Clients wäre dies:
	 * {@code java -jar InformationDisplay.jar client -h 192.168.178.12 -p 8800}
	 * Diese Zeile erstellt einen Client auf dem auszuführenden PC,
	 * der sich auf den Server mit der IPv4-Adresse "192.168.178.12" auf den Port 8800 verbindet.
	 * Sollte -h nicht angegeben werden, wird der localhost als Server verwendet. Bei Falscheingabe wird kein Client gestartet.
	 * Sollte -p nicht angegeben werden, wird der Port 8000 verwendet. Bei Falscheingabe wird kein Client gestartet.
	 * @param args die n�tigen Argumente
	 */
	public static void main(String[] args) {
		if(args.length > 1) {
			String port = "8000";
			// Wenn der erste Parameter "server" ist.
			if("server".equalsIgnoreCase(args[0])) {
				// Argumente einlesen
				for(int i = 1; i < (args.length -1); i++) {
					// Wenn der Port eine vern�nftige Zahlenkombination ist
					if("-p".equalsIgnoreCase(args[i]) && args[i +1].matches("\\d+")) port = args[i +1];
				}
				// Erstellt neuen Server
				int p = Integer.parseInt(port);
				// Wenn die Portzahl im möglichen Bereich ist, starte den Server...
				new Server(p >= 0 && p <= 65535 ? p : 8000);
			// Wenn der erste Parameter "client" ist.
			} else if("client".equalsIgnoreCase(args[0])) {
				String host = "127.0.0.1";
				// Argumente einlesen
				for(int i = 1; i < (args.length -1); i++) {
					if("-h".equalsIgnoreCase(args[i])) host = args[i + 1];
					else if("-p".equalsIgnoreCase(args[i])) port = args[i + 1];
				}
				// Wenn der Port eine Zahl ist.
				if(port.matches("\\d+")) {
					int p = Integer.parseInt(port);
					// Wenn alle Eingeben korrekt sind, starte den Client. Sonst beenden...
					if(host.matches(IP_REGEX) && p >= 0 && p <= 65535)
						new Client(host, Integer.parseInt(port));
					else LOG.log(Level.SEVERE, "The Host- and Port-Values are incorrect. JVM will terminate now.");
				}
			// Hilfestellung senden.
			} else sendHelp();
		// Hilfestellung senden.
		} else sendHelp();
	}
	
	/**
	 * Diese Methode soll über die Argumente und deren Verwendung eine kleine Information senden.
	 * Hier wurde kein Logger verwendet, da dies eine normale Systemausgabe ohne Uhrzeit und Wichtigkeit
	 * sein sollte.
	 */
	private static void sendHelp() {
		System.out.println("[InformationDisplay] Please use the parameter: server / client");
		System.out.println("[InformationDisplay] java -jar ./InformationDisplay.jar server");
		System.out.println("[InformationDisplay] java -jar ./InformationDisplay.jar client");
		System.out.println("[InformationDisplay] ");
		System.out.println("[InformationDisplay] -p <Port>: Set the port of the Server");
		System.out.println("[InformationDisplay] -h <Hostname>: Set the host of the Server");
	}
}
