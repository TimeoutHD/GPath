package de.pi.infodisplay.server.handler;

import java.util.logging.Level;

import io.netty.channel.ChannelHandlerContext;
import de.pi.infodisplay.Main;
import de.pi.infodisplay.shared.handler.NetworkHandler;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;

public class ServerNetworkHandler extends NetworkHandler {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if(channel != null) {
			if(packet instanceof PacketClientOutInfo) {
				Main.LOG.log(Level.INFO, ((PacketClientOutInfo)packet).getMessage());
			}
		}
	}

}
