package de.pi.infodisplay.client.netty.handler;

import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;

/**
 * Diese Klasse ist der spezielle Packet-Decoder für den Client.
 * 
 * @author piA
 *
 */
public class PacketClientDecoder extends PacketDecoder {

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
		default: return null;
		}
	}
}
