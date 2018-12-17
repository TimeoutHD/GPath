package de.pi.infodisplay.client;

import de.pi.infodisplay.client.netty.NettyClient;

public class Client {
	
	private NettyClient netty;
	
	public Client(String host, int port) {
		netty = new NettyClient(host, port);
	}
	
	public NettyClient getNettyClient() {
		return netty;
	}
}
