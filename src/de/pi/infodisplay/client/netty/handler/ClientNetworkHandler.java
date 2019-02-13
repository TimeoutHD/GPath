package de.pi.infodisplay.client.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import de.pi.infodisplay.client.Client;

public class ClientNetworkHandler extends ChannelHandlerAdapter {
	
	private Client client;
	
	public ClientNetworkHandler(Client client) {
		this.client = client;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg.getClass().toString());
	}

}
