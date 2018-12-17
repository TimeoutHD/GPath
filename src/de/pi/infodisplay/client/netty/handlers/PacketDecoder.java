package de.pi.infodisplay.client.netty.handlers;

import java.util.List;

import de.pi.infodisplay.shared.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf buf, List<Object> arg2) throws Exception {	
		Class<? extends Packet> packetClass = getPacketClassByID(buf.readInt());
		if(packetClass != null) {
			
		} else throw new IllegalArgumentException("Packet-ID is not in use");
	}

	private Class<? extends Packet> getPacketClassByID(int id) {
		switch(id) {
		case 0: return Packet.class;
		default: return null;
		}
	}
}

