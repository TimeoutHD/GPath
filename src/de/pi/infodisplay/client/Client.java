package de.pi.infodisplay.client;

import de.pi.infodisplay.client.gui.MainWindow;
import de.pi.infodisplay.client.netty.NettyClient;

/**
 * Diese Klasse soll den Client darstellen.
 * @author PI A
 *
 */
@SuppressWarnings("deprecation")
public class Client {
	
	/**
	 * Dieses Field ist eine Konstante für den IPv4-Regex.
	 * Ein Regex kann in jeder Programmiersprache Strings auf Formatierungen überprüfen.
	 * Dieser Regex überprüft, ob der String das Format einer IPv4-Adresse unterstützt.
	 */
	private static final String IP_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	
	/**
	 * Das ist das Field für den NettyClient. Der NettyClient
	 * ist für die Verbindung zum Server und das Netzwerk verantwortlich.
	 * Hierüber werden alle Packets gesendet und empfangen.
	 * 
	 * Hier wird das Field nur deklariert. Die Initialisierung findet im Constructor statt.
	 */
	private NettyClient netty;
	
	private MainWindow gui;
	
	/**
	 * Das ist der Constructor für die Clientklasse.
	 * Hier werden alle Werte mit den richtigen Werten initialisiert.
	 * Dabei werden die richtigen Werte aus dem Parametern benutzt.
	 * Sie werden nicht auf Richtogkeit überprüft.
	 * 
	 * @param host Die IPv4-Adresse des Servers
	 * @param port Der Port des Servers
	 */
	public Client() {
		// Initialisierung GUI-Interface
		this.gui = new MainWindow(this);
	}
	
	/**
	 * Diese Methode startet den NettyClient in einem parallelen Thread.
	 */
	private void runNettyClient() {
		new Thread(netty).start();
	}
	
	/**
	 * Diese Methode gibt den NettyClient zurück.
	 * 
	 * Hierbei ist der NettyClient der Teil des Clients, der sich um die Netzwerkprotokolle
	 * und die Packets kümmert.
	 * Zudem kümmert sich der NettyClient auch um das Encoden / Decoden der Packets.
	 * @return Den NettyClient des Clients.
	 */
	public NettyClient getNettyClient() {
		return netty;
	}
	
	public MainWindow getMainWindowGUI() {
		return gui;
	}
	
	public boolean connectToServer(String hostname, int port) {
		if(netty != null) netty.disconnect();
		if(hostname.matches(IP_REGEX) && (port >= 0 && port <= 65535)) {
			netty = new NettyClient(this, hostname, port);
			runNettyClient();
			return true;
		}
		return false;
	}
}
