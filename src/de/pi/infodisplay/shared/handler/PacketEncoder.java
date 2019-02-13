package de.pi.infodisplay.shared.handler;

import de.pi.infodisplay.shared.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public abstract class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf output) throws Exception {
		Class<? extends Packet> packetClass = getPacketTypeByID(packet.getID());
		if(packetClass != null) {
			output.writeInt(packet.getID());
			packet.write(output);
			System.out.println("Ich habe encoded!");
		} else throw new IllegalArgumentException("Packet-ID is not in Use");
	}

	protected abstract Class<? extends Packet> getPacketTypeByID(int id);
}
