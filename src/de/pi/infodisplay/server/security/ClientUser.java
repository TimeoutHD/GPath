package de.pi.infodisplay.server.security;

import java.security.InvalidKeyException;

import de.pi.infodisplay.security.AuthentificationKey;
import de.pi.infodisplay.shared.handler.PacketHandler;
import de.pi.infodisplay.shared.security.User;
import io.netty.channel.socket.SocketChannel;

public class ClientUser {

	private SocketChannel channel;
	private User loggedUser;
	private AuthentificationKey secutityKey;
	
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
	
	public void authorize(User user, AuthentificationKey securityKey) throws InvalidKeyException {
		if(!isAuthorized()) {
			this.loggedUser = user;
			this.secutityKey = securityKey;
		} else {
			disconnect();
			throw new IllegalArgumentException("User is already authorized. WARNING: This connection is disconnected due secutity reasons");
		}
	}
}
