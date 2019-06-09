package de.pi.infodisplay.server.security;

import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.server.handler.PacketServerDecoder;
import de.pi.infodisplay.server.handler.PacketServerEncoder;
import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.handler.PacketEncoder;
import de.pi.infodisplay.shared.handler.PacketHandler;

public class ClientPacketHandler implements PacketHandler {

	private ClientUser user;
	private PacketServerDecoder decoder;
	private PacketServerEncoder encoder;
	
	public ClientPacketHandler(Server server, ClientUser user) {
		this.user = user;
		this.encoder = new PacketServerEncoder();
		this.decoder = new PacketServerDecoder(server, user);
	}

	public ClientUser getUser() {
		return user;
	}

	@Override
	public PacketDecoder getDecoder() {
		return decoder;
	}

	@Override
	public PacketEncoder getEncoder() {
		return encoder;
	}
}
