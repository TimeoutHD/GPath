package de.pi.infodisplay.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.client.netty.handler.ClientNetworkHandler;
import de.pi.infodisplay.shared.handler.PacketHandler;
import de.pi.infodisplay.shared.handler.PacketHandler.NetworkType;
import de.pi.infodisplay.shared.packets.Packet;

/**
 * Diese Klasse ist f�r die Nettyverbindungen mit dem Server verantwortlich.
 * Sie kümmert sich um das Netzwerkprotokoll und um das Decoden / Encoden der eingehenden und
 * ausgehenden Packets.
 * 
 * @author PI A
 *
 */
public class NettyClient {
	
	/**
	 * Dieses Field überprüft, ob der Client ein Unix-Betriebsystem besitzt.
	 * Je nachdem, welches Betriebsystem benutzt wird, muss abgewägt werden, welches Protokoll benutzt werden muss.
	 * Unix benutzt EPOLL während Windows auf NIO vertraut.
	 * 
	 * Die Methode {@code Epoll#isAvailable()} überprüft auf EPOLL und gibt den Wahrheitswert zurück.
	 * Diese wird als Konstante gespeichert, da sich das Protokoll nicht ohne ein neues Betriebsystem
	 * zu installieren, nicht ändert
	 */
	private static final boolean EPOLL = Epoll.isAvailable();
	
	/**
	 * Das ist das Field für den Port des Servers. 
	 * Hier wird lediglich der Port des Servers zwischengespeichert.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private int port;
	
	/**
	 * Das ist das Field f�r die IPv4-Adresse des Servers.
	 * Hier wird die IPv4-Adresse zwischengespeichert.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private String host;
	
	/**
	 * Das ist das Field des benutzten Netzwerk-Channels. über diesen Channel werden
	 * Pakete und andere Informationen zum Server gesendet und wieder empfangen.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private ChannelFuture channel;
	
	/**
	 * Das ist das Field für den PacketHandler. Diese Klasse handelt das Server-Client 
	 * Netzwerk.
	 * 
	 * Auch hier wird das Attribut nur deklariert.
	 */
	private PacketHandler handler;

	/**
	 * Erstellt eine NettyClient mit einer Verbindung zur Adresse, die als
	 * Parameter angegeben werden.
	 * 
	 * @param host die IPv4-Adresse des Servers
	 * @param port der Port des Servers
	 */
	public NettyClient(String host, int port) {
		this.port = port;
		this.host = host;
		this.handler = new PacketHandler(NetworkType.CLIENT);
		// EventLoopGroup definieren.
		try(EventLoopGroup group = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {
			// Bootstrap erstellen 
			channel = new Bootstrap()
					// Mit LoopGroup linken
					.group(group)
					// Richtige Class angeben
					.channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					// Handler registrieren.
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline()
								.addLast(handler.getEncoder())
								.addLast(handler.getDecoder())
								.addLast(new ClientNetworkHandler());
							Main.LOG.log(Level.INFO, "Connected to Server -> " + host);
						}
						
			}).connect(host, port).sync().channel().closeFuture().sync();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
//			String line;
//			while((line = reader.readLine().toLowerCase(Locale.GERMAN)) != null) {
//				if(!line.isEmpty()) {
//					if("ping".startsWith(line)) {
//						PacketClientOutInfo info = new PacketClientOutInfo("Hey, ich bin ein Test");
//						sendPacket(info);
//					}
//				}
//			}
		} catch (Exception e) {
			Main.LOG.log(Level.SEVERE, "Failed to connect", e);
		}
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public ChannelFuture getChannel() {
		return channel;
	}
	
	public PacketHandler getPacketHandler() {
		return handler;
	}
	
	public void sendPacket(Packet packet) {
		this.channel.channel().writeAndFlush(packet, channel.channel().voidPromise());
	}
}
