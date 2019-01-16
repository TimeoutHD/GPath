package de.pi.infodisplay.shared.handler;

import java.util.List;

import de.pi.infodisplay.shared.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class PacketDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> objects) throws Exception {
		if(input.isReadable()) {
			int id = input.readInt();
			Class<? extends Packet> packetClass = getPacketClassByID(id);
			if(packetClass != null) {
				Packet packet = packetClass.getConstructor(int.class).newInstance(id);
				packet.read(input);
			} else throw new IllegalArgumentException("Packet-ID " + id + " is not in use");
		}
	}

	protected abstract Class<? extends Packet> getPacketClassByID(int id);
}

