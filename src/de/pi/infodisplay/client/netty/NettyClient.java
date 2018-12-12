package de.pi.infodisplay.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NettyClient {
	
	private static final boolean EPOLL = Epoll.isAvailable();
	
	private int port;
	private String host;
	private ChannelFuture channel;

	public NettyClient(String host, int port) {
		this.port = port;
		this.host = host;
		try (EventLoopGroup group = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {
			channel = new Bootstrap() 
				.group(group)
				.channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel arg0) throws Exception {
						
					}
				}).connect(host, port).sync().channel().closeFuture().syncUninterruptibly();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, "Cannot create Netty Client", e);
		};
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
}
