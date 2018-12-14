package de.pi.infodisplay.client.netty.handlers;

import de.pi.infodisplay.shared.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
		int id = packet.getID();
	}

}
