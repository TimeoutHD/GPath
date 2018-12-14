package de.pi.infodisplay.client.netty.handlers;

public class PacketHandler {
	
	private PacketDecoder decoder;
	private PacketEncoder encoder;
	
	public PacketHandler() {
		this.decoder = new PacketDecoder();
		this.encoder = new PacketEncoder();
	}
}
