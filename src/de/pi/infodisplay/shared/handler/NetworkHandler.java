package de.pi.infodisplay.shared.handler;

import java.util.logging.Level;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.shared.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class NetworkHandler extends SimpleChannelInboundHandler<Packet> {
	
	protected Channel channel;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		this.channel = ctx.channel();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Main.LOG.log(Level.SEVERE, "Error while Connection: ", cause);
	}

}
