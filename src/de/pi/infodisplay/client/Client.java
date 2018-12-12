package de.pi.infodisplay.client;

import de.pi.infodisplay.client.netty.NettyClient;

public class Client {
	
	private NettyClient netty;
	
	public Client() {
		netty = new NettyClient("127.0.0.1", 8000);
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
	public NettyClient getNettyClient() {
		return netty;
	}
}
