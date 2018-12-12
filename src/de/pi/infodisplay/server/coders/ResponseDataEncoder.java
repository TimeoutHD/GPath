package de.pi.infodisplay.server.coders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseDataEncoder extends MessageToByteEncoder<ResponseData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
		for(Object o : msg.getContents()) {
			for(char c : o.toString().toCharArray()) {
				out.writeChar(c);
			}
		}
	}
}
