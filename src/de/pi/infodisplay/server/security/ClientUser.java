package de.pi.infodisplay.server.security;

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
	
	public ClientUser(SocketChannel channel, User user, AuthentificationKey key) {
		this.channel = channel;
		this.loggedUser = user;
		this.secutityKey = key;
		this.packetHandler = new ClientPacketHandler(this);
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
	
	public void disconnect() {
		channel.close(channel.voidPromise());
	}
	
	public void authorize(User user, AuthentificationKey securityKey) {
		if(!isAuthorized()) {
			this.loggedUser = user;
			this.secutityKey = securityKey;
		} else {
			disconnect();
			throw new IllegalArgumentException("User is already authorized. WARNING: This connection is disconnected due secutity reasons");
		}
	}

	public ChannelFuture sendPacket(Packet packet, Channel channel) {
		return channel.writeAndFlush(packet, channel.voidPromise()).syncUninterruptibly();
	}

	@Override
	public ChannelFuture apply(Packet packet, Channel channel) {
		return channel.writeAndFlush(packet, channel.voidPromise()).syncUninterruptibly();
	}
}
