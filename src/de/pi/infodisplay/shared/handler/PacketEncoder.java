package de.pi.infodisplay.shared.handler;

import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf output) throws Exception {
		Class<? extends Packet> packetClass = getPacketTypeByID(packet.getID());
		if(packetClass != null) {
			packet.write(output);
		} else throw new IllegalArgumentException("Packet-ID is not in Use");
	}

	/**
	 * Returns the Class of the Packet, by it's ID.
	 * @param id the ID of the Packet
	 * @return Returns the Class of the Packet.
	 * It returns null, if the ID does not exist.
	 */
	private Class<? extends Packet> getPacketTypeByID(int id) {
		switch(id) {
		case 0: return PacketClientOutInfo.class;
		default: return null;
		}
	}
}
