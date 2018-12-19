package de.pi.infodisplay.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.shared.handler.PacketHandler;

public class NettyClient {
	
	private static final boolean EPOLL = Epoll.isAvailable();
	
	private int port;
	private String host;
	private Channel channel;
	
	private PacketHandler handler;

	public NettyClient(String host, int port) {
		this.port = port;
		this.host = host;
		this.handler = new PacketHandler();
		try(EventLoopGroup group = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup()) {
			channel = new Bootstrap() 
					.group(group)
					.channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel channel) throws Exception {
							channel.pipeline()
								.addLast(handler.getDecoder())
								.addLast(handler.getEncoder());
						}
						
			}).connect(host, port).sync().channel();
		} catch (Exception e) {
			Main.getConsole().log(Level.SEVERE, "Failed to connect", e);
		}
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public Channel getChannel() {
		return channel;
	}
	
	public PacketHandler getPacketHandler() {
		return handler;
	}
}
