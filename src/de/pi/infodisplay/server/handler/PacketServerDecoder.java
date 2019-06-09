package de.pi.infodisplay.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.server.security.ClientUser;
import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketClientOutDisconnect;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;
import de.pi.infodisplay.shared.packets.PacketClientOutInfoUpdate;

/**
 * Diese Klasse ist der spiezielle Decoder des Servers.
 * 
 * @author piA
 *
 */
public class PacketServerDecoder extends PacketDecoder {
	
	private Server server;

	public PacketServerDecoder(Server server, ClientUser operator) {
		super(operator);
		this.server = server;
	}

	/**
	 * Mithilfe dieser Methode werden die Packet-IDs den richtigen Wrapperklassen zugeordnet.
	 * Alle Packets mit dem Namen ClientOut sind hier gelistet.
	 * 
	 * @param id Die ID des Packets.
	 * @return die Wrapperklasse des Packets
	 */
	@Override
	protected Class<? extends Packet> getPacketClassByID(int id) {
		switch(id) {
			case 0: return PacketClientOutInfo.class;
			case 101: return PacketClientOutAuthorizeUser.class;
			case 201: return PacketClientOutInfoUpdate.class;
			case 300: return PacketClientOutAddInformation.class;
			case 777: return PacketClientOutDisconnect.class;
			default: return null;
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> objects) throws Exception {
		Packet packet = getSendPacket(input);
		if(packet instanceof PacketClientOutAuthorizeUser) {
			server.getClientManager().channelRead(ctx, packet);
		} else if(packet instanceof PacketClientOutAddInformation || packet instanceof PacketClientOutInfoUpdate) {
			server.getInformationUploadManager().channelRead(ctx, packet);
		}
	}

	
}
