package de.pi.infodisplay.server.handler;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.server.security.ClientUser;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketClientOutDisconnect;
import de.pi.infodisplay.shared.packets.PacketServerOutAuthorizeUser;
import de.pi.infodisplay.shared.security.AuthentificationKey;
import de.pi.infodisplay.shared.security.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class ClientPool {

	private static final List<ClientUser> clientConnections = new LinkedList<>();
	private static final Map<User, List<AuthentificationKey>> usedKeys = new LinkedHashMap<>(Short.MAX_VALUE);
	
	private Server parent;
	private ChannelFuture serverChannel;
		
	public ClientPool(Server parent, ChannelFuture future) {
		this.serverChannel = future;
		this.parent = parent;
	}
	
	public ClientUser connect(SocketChannel channel) {
		return connect(channel, null);
	}
	
	/**
	 * Diese Methode verbindet einen Client mit einem Benutzer und loggt ihn als ClientUser ein
	 * @param channel der Channel zwischen Server und Client
	 * @param user der Benutzer des Clients
	 * @return der ClientUser
	 */
	public ClientUser connect(SocketChannel channel, User user) {
		ClientUser cUser = new ClientUser(channel, parent, user, null);
		clientConnections.add(cUser);
		return cUser;
	}
	
	public void authorize(SocketChannel channel, User loggedUser, AuthentificationKey key) {
		// Suche richtige Verbindung
		clientConnections.forEach(connection -> {
			if(connection.getChannel().equals(channel)) {
				try {
					// Setze Benutzer und Sicherheitsschlüssel
					connection.authorize(loggedUser, key);
					// Wenn der Cache leer ist, erstelle ihn
					if(usedKeys.get(loggedUser) == null) usedKeys.put(loggedUser, new ArrayList<>());
					// Setze Wert in den Cache
					usedKeys.get(loggedUser).add(key);
				} catch (IllegalArgumentException e) {
					Main.LOG.log(Level.SEVERE, "Illegal authorization: ", e);
				}
			}
		});
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws SQLException {
		if(msg instanceof PacketClientOutAuthorizeUser) {
			// Packet casten
			PacketClientOutAuthorizeUser packet = (PacketClientOutAuthorizeUser) msg;
			// User aus der Datenbank auslesen
			User user = User.getFromDataBaseByName(packet.getUsername());
			// Auf Datenbank warten
			synchronized (user) {			
				// Passwort vergleichen
				boolean authorize = user.compare(packet.getPassword());
				// Sicherheitsschlüssel definieren
				AuthentificationKey key = null;
				
				// Wenn Passwort richtig
				if(authorize) {
					// Autorisiere Client mit Benutzer
					key = new AuthentificationKey();
					authorize((SocketChannel) ctx.channel(), user, key);
				}
				
				// Erstelle und sende Antwortpacket an Client
				PacketServerOutAuthorizeUser authorizeOut = new PacketServerOutAuthorizeUser(user.getUniqueId(), user.compare(packet.getPassword()), key);
				parent.sendPacket(authorizeOut);
			}

		} else if(msg instanceof PacketClientOutDisconnect) {
			String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
			ClientUser user = isConnected(ip, getUser(ip));
			disconnect(user);
		}
	}
	
	/**
	 * Diese Methode meldet einen ClientUser vom Server ab
	 * @param user der Benutzer, der abgemeldet werden soll
	 */
	public void disconnect(ClientUser user) {
		// ClientUser abmelden
		user.disconnect();
		// ClientUser aus dem Cache entfernen
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
		// Wenn Benutzer nicht null ist
		if(cUser != null) {
			// Sicherheitsschlüssel miteinander vergleichen
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
