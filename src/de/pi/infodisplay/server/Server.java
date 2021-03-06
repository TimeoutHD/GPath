package de.pi.infodisplay.server;

import java.sql.SQLException;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.handler.ClientPool;
import de.pi.infodisplay.server.handler.FileHandler;
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
import io.netty.channel.FixedRecvByteBufAllocator;
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
		
	private FileHandler informationManager;
	
	/**
	 * Das ist das Field für die benutzte MySQL-Datenbank, wo der Server die benötigten Informationen abspeichert.
	 * Jegliche Benutzerdaten und Speicheradressen der Informationen werden hier zwischengespeichert.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private static final MySQL mysql = new MySQL("localhost", 3306, "informationdisplay");
	
	static {
		try {
			mysql.connect("pi", "piA");
		} catch (SQLException e) {
			Main.LOG.log(Level.SEVERE, "Could not connect to MySQL-Database", e);
		}
	}
	
	/**
	 * Erstellt einen NettyServer, der den angegebenen Port besetzt.
	 * Das Netzwerk arbeitet mit TCP und ist durchgehend abgesichert.
	 * 
	 * @param port der Port, den der Server besetzt.
	 */
	public Server(int port) {
		this.port = port;
		// Bootstrap für den Server
		try(EventLoopGroup bossGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup(); 
				EventLoopGroup workerGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {	
			// Neue Datenbanken sehen.
			initializeDatabases();
			clientManager = new ClientPool(this, serverChannel);
			informationManager = new FileHandler(this);
			serverChannel = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
			    
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						ClientUser user = clientManager.connect(channel);
						
						//Bytebuf vergrößern auf 10MB
						channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(100*1024*1024));
						
						// Handler initialisieren
						channel.pipeline().addLast(
								user.getPacketHandler().getEncoder(),
								user.getPacketHandler().getDecoder());
						Main.LOG.log(Level.INFO, "Connect -> " + channel.remoteAddress().getHostName() + ":" +
								channel.remoteAddress().getPort());
					}				
				})
				// Optionen festlegen.
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				// TCP aktivieren und Server starten
				.bind(port).sync().channel().closeFuture();
			Main.LOG.log(Level.INFO, "Server is started successful.");
			serverChannel.sync();
		} catch(Exception e) {
			Main.LOG.log(Level.SEVERE, "Cannot create Server", e);
		}
	}
	
	public static MySQL getMySQL() {
		return mysql;
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
	
	public void sendPacket(Packet packet) {
		clientManager.getClients().forEach(client -> this.apply(packet, client.getChannel()));
	}

	public ClientPool getClientManager() {
		return clientManager;
	}
	
	public FileHandler getInformationUploadManager() {
		return informationManager;
	}
	
	private void initializeDatabases() throws SQLException {
		if(mysql.isConnected()) {
			mysql.executeVoidStatement("CREATE TABLE IF NOT EXISTS User(uuid VARCHAR(36), name VARCHAR(100), password TEXT, admin TINYINT(1), PRIMARY KEY (uuid))");
			mysql.executeVoidStatement("CREATE TABLE IF NOT EXISTS Information(id INT(4) NOT NULL AUTO_INCREMENT, creatorID VARCHAR(36), title TEXT, path TEXT, PRIMARY KEY(id), FOREIGN KEY (creatorID) REFERENCES User(uuid))");
		}
	}

	@Override
	public synchronized ChannelFuture apply(Packet packet, Channel channel) {
		return channel.writeAndFlush(packet).syncUninterruptibly();
	}
}
