package de.pi.infodisplay.server.security;

import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.shared.handler.PacketHandler;
import de.pi.infodisplay.shared.security.AuthentificationKey;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.security.Operator;
import de.pi.infodisplay.shared.security.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;

/**
 * Diese Klasse ist die Userinstanz für den Server. Sie vebindet die eigenen Coder, den AuthentificationKey und den User,
 * mitdem diese Verbindung angemeldet ist.
 * 
 * @author PI A
 *
 */
public class ClientUser implements Operator {

	/**
	 * Dieses Field ist der Channel zwischen Client und Server.
	 * Über den Channel werden die Pakete hin und her versendet.
	 */
	private SocketChannel channel;
	
	/**
	 * Dieses Field ist der angemeldete User. Um Änderungen am Server vorzunehmen, muss man angemeldet sein.
	 */
	private User loggedUser;
	
	/**
	 * Dieses Field ist der Sicherheitsschlüssel des ClientUsers.
	 * Der Sicherheitsschlüssel ist eimmalig und sichert die Client-Server Verbindung ab.
	 */
	private AuthentificationKey secutityKey;
	
	/**
	 * Dieses Field ist der clienteigene PacketHandler.
	 * Da jeder Handler nur für eine Verbindung braucht. 
	 */
	private PacketHandler packetHandler;
	
	public ClientUser(SocketChannel channel, Server server, User user, AuthentificationKey key) {
		this.channel = channel;
		this.loggedUser = user;
		this.secutityKey = key;
		this.packetHandler = new ClientPacketHandler(server, this);
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public AuthentificationKey getSecutityKey() {
		return secutityKey;
	}
	
	public boolean isAuthorized() {
		return secutityKey != null;
	}
	
	public PacketHandler getPacketHandler() {
		return packetHandler;
	}
	
	/**
	 * Diese Methode schließt die Nettyverbindung zwischen dem dazugehörigen Client und diesem Server.
	 */
	public void disconnect() {
		channel.close();
	}
	
	/**
	 * Diese Methode setzt den Benutzer und den Sicherheitsschlüssel zwischen Client und Server.
	 * <p>ACHTUNG<!p> Diese Methode funktioniert nur einmal. Bei einem weiteren Versuch wird die Verbindung getrennt, um Hackangriffe zu vermeiden.
	 * @param user der Benutzer des Clients
	 * @param securityKey der dazugehörige Client
	 */
	public void authorize(User user, AuthentificationKey securityKey) {
		// Wenn er nicht autorisiert ist
		if(!isAuthorized()) {
			// Setze Werte
			this.loggedUser = user;
			this.secutityKey = securityKey;
		} else {
			// SICHERHEITSRISIKO: Trenne Verbindung und 
			disconnect();
			Main.LOG.log(Level.WARNING, "Dieser Benutzer war schon autorisiert. WARNuNG: Diese Verbindung wird aus Sicherheitsgründen getrennt");
		}
	}

	/**
	 * Diese Methode sendet ein Packet über einen Channel
	 * @param packet Das zu sendene Packet
	 * @param channel Der Channel, über den das Packet versendet werden soll
	 * @return der Channel, der arbeitet. Für Synchronisation nützlich
	 */
	public ChannelFuture sendPacket(Packet packet, Channel channel) {
		return channel.writeAndFlush(packet, channel.voidPromise()).syncUninterruptibly();
	}

	/**
	 * Diese Methode sendet ein Packet über einen Channel. Siehe {@link ClientUser#sendPacket(Packet, Channel)} für mehr Infos
	 * @param packet Das zu sendene Packet
	 * @param channel Der Channel, über den das Packet versendet werden soll
	 * @return der Channel, der arbeitet. Für Synchronisation nützlich
	 */
	@Override
	public ChannelFuture apply(Packet packet, Channel channel) {
		return channel.writeAndFlush(packet, channel.voidPromise()).syncUninterruptibly();
	}
}
