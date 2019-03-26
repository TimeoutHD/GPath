package de.pi.infodisplay.client.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @deprecated Unnütze Klasse. Soll entfernt werden.
 * @author piA
 */
@Deprecated
public class ClientNetworkHandler extends ChannelHandlerAdapter {

	@Deprecated
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg.getClass().toString() + " ist die angekommene Klasse vom ClientHandler");
	}

}
