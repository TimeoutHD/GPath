package de.pi.infodisplay.shared.handler;

public class PacketHandler {
	
	private PacketDecoder decoder;
	private PacketEncoder encoder;
	
	public PacketHandler() {
		this.decoder = new PacketDecoder();
		this.encoder = new PacketEncoder();
	}
	
	public PacketDecoder getDecoder() {
		return decoder;
	}
	
	public PacketEncoder getEncoder() {
		return encoder;
	}
}
