package de.pi.infodisplay.server;

import de.pi.infodisplay.shared.handler.PacketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Server {

	public static final boolean EPOLL = Epoll.isAvailable();
	
	private int port;
	private ChannelFuture channel;
	
	private PacketHandler handler;

	public Server(int port) throws Exception {
		this.port = port;
		this.handler = new PacketHandler();
		
		try (EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {
			channel = new ServerBootstrap()
				.group (eventLoopGroup)
				.channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
			    
					@Override
					protected void initChannel(Channel channel) throws Exception {
						channel.pipeline()
							.addLast(handler.getDecoder())
							.addLast(handler.getEncoder());
					}				
				}).bind(port).sync().channel().closeFuture().syncUninterruptibly();
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
	
	
}
