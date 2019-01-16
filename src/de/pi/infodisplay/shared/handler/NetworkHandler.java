package de.pi.infodisplay.shared.handler;

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

}
