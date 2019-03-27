package de.pi.infodisplay.server;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.security.ClientUser;
import de.pi.infodisplay.shared.security.AuthentificationKey;
import de.pi.infodisplay.shared.security.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;

public class ClientPool {

	private final List<ClientUser> clientConnections = new ArrayList<>();
	private final HashMap<User, List<AuthentificationKey>> usedKeys = new HashMap<>();
	
	private ChannelFuture serverChannel;
	
	public ClientPool(ChannelFuture future) {
		this.serverChannel = future;
	}
	
	public ClientUser connect(SocketChannel channel) {
		ClientUser user = new ClientUser(channel, null, null);
		clientConnections.add(user);
		return user;
	}
	
	public void authorize(SocketChannel channel, User loggedUser, AuthentificationKey key) {
		clientConnections.forEach(connection -> {
			if(connection.getChannel().equals(channel)) {
				try {
					connection.authorize(loggedUser, key);
					usedKeys.get(loggedUser).add(key);
				} catch (InvalidKeyException e) {
					Main.LOG.log(Level.SEVERE, "Illegal authorization: ", e);
				}
			}
		});
	}
	
	public void disconnect(ClientUser user) {
		user.disconnect();
		clientConnections.remove(user);
		if(user.getSecutityKey() != null)usedKeys.get(user.getLoggedUser()).remove(user.getSecutityKey());
	}
	
	public Collection<ClientUser> getClients() {
		return new ArrayList<>(clientConnections);
	}
	
	public ChannelFuture getServerChannel() {
		return serverChannel;
	}
}
