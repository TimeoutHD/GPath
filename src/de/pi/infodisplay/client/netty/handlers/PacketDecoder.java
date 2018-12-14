package de.pi.infodisplay.client.netty.handlers;

import java.util.Arrays;
import java.util.List;

import de.pi.infodisplay.shared.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {
	
	private static final List<Class<? extends Packet>> IN_PACKETS = Arrays.asList();

	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {		
	}

}
