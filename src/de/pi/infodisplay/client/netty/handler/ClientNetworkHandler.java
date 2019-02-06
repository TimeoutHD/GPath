package de.pi.infodisplay.client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.shared.handler.NetworkHandler;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;

public class ClientNetworkHandler extends NetworkHandler {
	
	private Client client;
	
	public ClientNetworkHandler(Client client) {
		this.client = client;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if(channel != null) {
			if(packet instanceof PacketServerOutInfo) {
				this.client.getTerminal().printLine(((PacketServerOutInfo)packet).getMessage());
			}
		}
	}
	
	

}
