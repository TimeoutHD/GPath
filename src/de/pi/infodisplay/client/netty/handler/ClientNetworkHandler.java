package de.pi.infodisplay.client.netty.handler;

import java.util.logging.Level;

import io.netty.channel.ChannelHandlerContext;
import de.pi.infodisplay.Main;
import de.pi.infodisplay.shared.handler.NetworkHandler;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;

public class ClientNetworkHandler extends NetworkHandler {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if(channel != null) {
			if(packet instanceof PacketServerOutInfo) {
				Main.LOG.log(Level.INFO, ((PacketServerOutInfo)packet).getMessage());
			}
		}
	}

}
