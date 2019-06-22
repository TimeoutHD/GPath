package de.pi.infodisplay.shared.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.security.Operator;
import de.timeout.libs.Reflections;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class PacketDecoder extends ByteToMessageDecoder {
	
	protected Operator operator;
	
	public PacketDecoder(Operator operator) {
		super();
		this.operator = operator;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> objects) throws Exception {
		getSendPacket(input);
	}
	
	protected Packet getSendPacket(ByteBuf input) throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
		if(input.isReadable()) {
			int id = input.readInt();
			Class<? extends Packet> packetClass = getPacketClassByID(id);
			if(packetClass != null) {
				Packet packet = (Packet) Reflections.getConstructor(packetClass).newInstance();
				packet.read(input);
				System.out.println("gelesen");
				return packet;
			}  throw new IllegalArgumentException("Packet-ID " + id + " is not in use");
		} 	
		throw new IllegalArgumentException("Cannot read Bytebuf");
	}

	protected abstract Class<? extends Packet> getPacketClassByID(int id);
}

