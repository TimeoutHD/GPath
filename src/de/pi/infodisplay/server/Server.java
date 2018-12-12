package de.pi.infodisplay.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Server {

	public static final boolean EPOLL = false;

	public Server() throws Exception {
		
		EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
		try{
			new ServerBootstrap()
				.group (eventLoopGroup)
				.channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
			    
					@Override
					protected void initChannel(Channel channel) throws Exception {
					
						channel.pipeline().addLast("default_channel_handler", new SimpleChannelInboundHandler<Object>(){
							
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								
								super.channelActive(ctx);
								System.out.println("Channel connected -> " + ctx.channel());
							}

							@Override
							protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
								// TODO Auto-generated method stub
								
							}
						});					
					}				
				}).bind(8000).sync().channel().closeFuture().syncUninterruptibly();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Server();	
	}
}
