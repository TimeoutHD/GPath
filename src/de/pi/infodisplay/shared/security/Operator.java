package de.pi.infodisplay.shared.security;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import de.pi.infodisplay.shared.packets.Packet;

@FunctionalInterface
public interface Operator {

	public ChannelFuture sendPacket(Packet packet, Channel channel);
	
}
