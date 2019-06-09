package de.pi.infodisplay.server.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.server.security.ClientUser;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketServerOutAuthorizeUser;
import de.pi.infodisplay.shared.security.AuthentificationKey;
import de.pi.infodisplay.shared.security.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class ClientPool {

	private static final List<ClientUser> clientConnections = new LinkedList<>();
	private static final Map<User, List<AuthentificationKey>> usedKeys = new ConcurrentHashMap<>(Integer.MAX_VALUE);
	
	private Server parent;
	private ChannelFuture serverChannel;
		
	public ClientPool(Server parent, ChannelFuture future) {
		this.serverChannel = future;
		this.parent = parent;
	}
	
	public ClientUser connect(SocketChannel channel) {
		return connect(channel, null);
	}
	
	public ClientUser connect(SocketChannel channel, User user) {
		ClientUser cUser = new ClientUser(channel, parent, user, null);
		clientConnections.add(cUser);
		return cUser;
	}
	
	public void authorize(SocketChannel channel, User loggedUser, AuthentificationKey key) {
		clientConnections.forEach(connection -> {
			if(connection.getChannel().equals(channel)) {
				try {
					connection.authorize(loggedUser, key);
					usedKeys.get(loggedUser).add(key);
				} catch (IllegalArgumentException e) {
					Main.LOG.log(Level.SEVERE, "Illegal authorization: ", e);
				}
			}
		});
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws SQLException {
		if(msg instanceof PacketClientOutAuthorizeUser) {
			PacketClientOutAuthorizeUser packet = (PacketClientOutAuthorizeUser) msg;
			User user = User.getFromDataBaseByName(packet.getUsername());
			synchronized (user) {
				PacketServerOutAuthorizeUser authorizeOut = new PacketServerOutAuthorizeUser(user.getUniqueId(), user.compare(packet.getPassword()));
				
				ctx.channel().writeAndFlush(authorizeOut, ctx.voidPromise());
			}

		}
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
	
	public boolean isAuthorized(String ip, User user) {
		ClientUser cUser = isConnected(ip, user);
		if(cUser != null) {
			List<AuthentificationKey> keys = usedKeys.get(cUser.getLoggedUser());
			return (keys != null && !keys.isEmpty() && keys.contains(cUser.getSecutityKey()));
		}
		return false;
	}
	
	public ClientUser isConnected(String ip, User user) {
		if(ip != null && user != null) {
			for(ClientUser cUser : clientConnections){
				if(cUser.getChannel().remoteAddress().getAddress().getHostAddress().equalsIgnoreCase(ip) &&
						cUser.getLoggedUser().equals(user))  
					return cUser;
			}
		}
		return null;
	}
	
	public User getUser(String ip) {
		for(ClientUser user : clientConnections) {
			if(user.getChannel().remoteAddress().getAddress().getHostAddress().equalsIgnoreCase(ip))
				return user.getLoggedUser();
		}
		return null;
	}
}
