package de.pi.infodisplay.server.coders;

import de.pi.infodisplay.shared.packets.InfoDisplayPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseDataEncoder extends MessageToByteEncoder<ResponseData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getContents().length);
		for(InfoDisplayPacket p : msg.getContents()) {
			out.writeInt(p.toString().length());
			for(char c : p.toString().toCharArray()) {
				out.writeChar(c);
			}
		}
	}
}
