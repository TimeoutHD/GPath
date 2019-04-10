package de.pi.infodisplay.server;

import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.security.ClientUser;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.security.Operator;
import de.timeout.libs.MySQL;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Diese Klasse ist für die Nettyverbindung mit allen Clients verantworlich.
 * Sie kümmert sich um das Netzwerkprotokoll mit anderen Clients und steuert alle Handler,
 * die für den Server zur Verfügung gestellt wird
 * 
 * @author PI A
 *
 */
public class Server implements Operator {

	/**
	 * Dieses Field überprüft, ob der Server ein Linux-Betriebsystem besitzt.
	 * Je nachdem, welches Betriebsystem benutzt wird, muss abgewägt werden, welches Protokoll benutzt werden muss.
	 * Linux benutzt EPOLL, während Windows auf NIO vertraut.
	 * 
	 * Die Methode {@code Epoll#isAvailable()} überprüft auf EPOLL und gibt den Wahrheitswert zurück.
	 * Diese wird als Konstante gespeichert, da sich das Protokoll nicht ohne ein neues Betriebsystem
	 * zu installieren, nicht ändert
	 */
	public static final boolean EPOLL = Epoll.isAvailable();
	
	/**
	 * Das ist das Field für der Port, unterdem der Server erreichbar ist.
	 * Hier wird lediglich der Port des Serverts zwischengespeichert.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private int port;
	
	/**
	 * Das ist das Field des benutzten Netzwerk-Channels. Über diesen Channel werden
	 * Packete und andere Informationen zum Server gesendet und wieder empfangen.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private ChannelFuture serverChannel;
	
	private ClientPool clientManager;
	
	/**
	 * Das ist das Field für die benutzte MySQL-Datenbank, wo der Server die benötigten Informationen abspeichert.
	 * Jegliche Benutzerdaten und Speicheradressen der Informationen werden hier zwischengespeichert.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private MySQL mysql;
	
	/**
	 * Erstellt einen NettyServer, der den angegebenen Port besetzt.
	 * Das Netzwerk arbeitet mit TCP und ist durchgehend abgesichert.
	 * 
	 * @param port der Port, den der Server besetzt.
	 */
	public Server(int port) {
		this.port = port;
		this.mysql = new MySQL("localhost", 3304, "InformationDisplay", "pi", "piA");
		
		// Bootstrap für den Server
		try(EventLoopGroup bossGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup(); 
				EventLoopGroup workerGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {		
			serverChannel = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
			    
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						ClientUser user = clientManager.connect(channel);
						
						// Handler initialisieren
						channel.pipeline().addLast(
								user.getPacketHandler().getDecoder(),
								user.getPacketHandler().getEncoder());
						Main.LOG.log(Level.INFO, "Connect -> " + channel.remoteAddress().getHostName() + ":" +
								channel.remoteAddress().getPort());
					}				
				})
				// Optionen festlegen.
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				// TCP aktivieren und Server starten
				.bind(port).sync().channel().closeFuture();
			clientManager = new ClientPool(serverChannel);
			Main.LOG.log(Level.INFO, "Server is started successful.");
			serverChannel.sync();
		} catch(Exception e) {
			Main.LOG.log(Level.SEVERE, "Cannot create Server", e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	public ChannelFuture getServerChannel() {
		return serverChannel;
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public void sendPacket(Packet packet) {
		clientManager.getClients().forEach(client -> this.sendPacket(packet, client.getChannel()));
	}

	@Override
	public ChannelFuture sendPacket(Packet packet, Channel channel) {
		return channel.writeAndFlush(packet, channel.voidPromise()).syncUninterruptibly();
	}

	public ClientPool getClientManager() {
		return clientManager;
	}
}
