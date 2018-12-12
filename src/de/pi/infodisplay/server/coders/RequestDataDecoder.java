package de.pi.infodisplay.server.coders;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class RequestDataDecoder extends ReplayingDecoder<RequestData> {
	
	private final Charset charset = Charset.forName("UTF-8");

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		RequestData data = new RequestData();
	}

}
