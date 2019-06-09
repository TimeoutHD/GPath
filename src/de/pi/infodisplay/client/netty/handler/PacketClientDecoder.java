package de.pi.infodisplay.client.netty.handler;

import java.util.List;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.client.netty.NettyClient;
import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketServerOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;
import de.pi.infodisplay.shared.packets.PacketServerOutInfoUpdate;
import de.pi.infodisplay.shared.packets.PacketServerOutInformation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Diese Klasse ist der spezielle Packet-Decoder für den Client.
 * 
 * @author piA
 *
 */
public class PacketClientDecoder extends PacketDecoder {

	public PacketClientDecoder(Client operator) {
		super(operator.getNettyClient());
	}

	/**
	 * Mithilfe dieser Methode werden die Packet-IDs den richtigen Wrapperklassen zugeordnet.
	 * Alle Packets mit dem Namen ServerOut sind hier gelistet.
	 * 
	 * @param id Die ID des Packets.
	 * @return die Wrapperklasse des Packets
	 */
	@Override
	protected Class<? extends Packet> getPacketClassByID(int id) {
		switch(id) {
		case 0: return PacketServerOutInfo.class;
		case 101: return PacketServerOutAuthorizeUser.class;
		case 201: return PacketServerOutInfoUpdate.class;
		case 300: return PacketServerOutAddInformation.class;
		case 400: return PacketServerOutInformation.class;
		default: return null;
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> objects) throws Exception {
		Packet packet = getSendPacket(input);
		if(packet instanceof PacketServerOutAuthorizeUser) {
			
		} else if(packet instanceof PacketServerOutInfoUpdate) {
			
		}
	}
	
	
}
