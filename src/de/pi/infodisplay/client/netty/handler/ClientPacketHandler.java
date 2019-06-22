package de.pi.infodisplay.client.netty.handler;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.shared.handler.PacketHandler;

public class ClientPacketHandler implements PacketHandler {
	
	private PacketClientDecoder decoder;
	private PacketClientEncoder encoder;
	
	public ClientPacketHandler(Client operator) {
		decoder = new PacketClientDecoder(operator);
		encoder = new PacketClientEncoder();
	}

	@Override
	public PacketClientDecoder getDecoder() {
		return decoder;
	}

	@Override
	public PacketClientEncoder getEncoder() {
		return encoder;
	}

}
