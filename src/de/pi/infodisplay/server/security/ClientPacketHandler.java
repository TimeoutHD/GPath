package de.pi.infodisplay.server.security;

import de.pi.infodisplay.shared.handler.PacketHandler;

public class ClientPacketHandler extends PacketHandler {

	private ClientUser owner;
	
	public ClientPacketHandler(ClientUser owner) {
		super(owner);
		this.owner = owner;
	}
	
	public ClientUser getOwner() {
		return owner;
	}

}
