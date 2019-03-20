package de.pi.infodisplay.server;

import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.server.handler.ServerNetworkHandler;
import de.pi.infodisplay.shared.handler.PacketHandler;
import de.pi.infodisplay.shared.handler.PacketHandler.NetworkType;
import de.pi.infodisplay.shared.packets.Packet;
import de.timeout.libs.MySQL;
import io.netty.bootstrap.ServerBootstrap;
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


public class Server {

	public static final boolean EPOLL = Epoll.isAvailable();
	
	private int port;
	private ChannelFuture channel;
	
	private PacketHandler handler;
	private MySQL sql;
	
	public Server(int port) {
		this.port = port;
		this.handler = new PacketHandler(NetworkType.SERVER);
		this.sql = new MySQL("localhost", 3304, "database", "pi", "piA");
		
		try(EventLoopGroup bossGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup(); 
				EventLoopGroup workerGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {		
			channel = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
			    
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline()
							.addLast("decoder", handler.getDecoder())
							.addLast("encoder", handler.getEncoder())
							.addLast("handler", new ServerNetworkHandler());
						Main.LOG.log(Level.INFO, "Connect -> " + channel.remoteAddress().getHostName() + ":" +
								channel.remoteAddress().getPort());
					}				
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.bind(port).sync().channel().closeFuture().sync();
			Main.LOG.log(Level.INFO, "Server is started successful.");
		} catch(Exception e) {
			Main.LOG.log(Level.SEVERE, "Cannot create Server", e);
		}
	}

	public int getPort() {
		return port;
	}

	public ChannelFuture getChannel() {
		return channel;
	}

	public PacketHandler getHandler() {
		return handler;
	}
	
	public void sendPacket(Packet packet) {
		this.channel.channel().writeAndFlush(packet, this.channel.channel().voidPromise());
	}
}
