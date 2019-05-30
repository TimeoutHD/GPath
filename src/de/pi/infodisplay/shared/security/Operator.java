package de.pi.infodisplay.shared.security;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.function.BiFunction;

import de.pi.infodisplay.shared.packets.Packet;

@FunctionalInterface
public interface Operator extends BiFunction<Packet, Channel, ChannelFuture> {
	
}
