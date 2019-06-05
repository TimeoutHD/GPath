package de.pi.infodisplay.server.security;

import de.pi.infodisplay.shared.handler.PacketHandler;

public class ClientPacketHandler extends PacketHandler {

	private ClientUser user;
	
	public ClientPacketHandler(ClientUser user) {
		super(user);
		this.user = user;
	}

	public ClientUser getUser() {
		return user;
	}
}
