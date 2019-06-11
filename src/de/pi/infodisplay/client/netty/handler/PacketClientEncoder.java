package de.pi.infodisplay.client.netty.handler;

import de.pi.infodisplay.shared.handler.PacketEncoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketClientOutDisconnect;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;
import de.pi.infodisplay.shared.packets.PacketClientOutInfoUpdate;

/**
 * Diese Klasse ist der spezielle Encoder des Clients.
 * 
 * @author piA
 *
 */
public class PacketClientEncoder extends PacketEncoder {

	/**
	 * Mithilfe dieser Methode werden die Packet-IDs den richtigen Wrapperklassen zugeordnet.
	 * Alle Packets mit dem Namen ClientOut sind hier gelistet.
	 * 
	 * @param id die ID des Packets
	 * @return Die Wrapperklasse des Packets.
	 */
	@Override
	protected Class<? extends Packet> getPacketTypeByID(int id) {
		switch(id) {
		case 0: return PacketClientOutInfo.class;
		case 101: return PacketClientOutAuthorizeUser.class;
		case 201: return PacketClientOutInfoUpdate.class;
		case 300: return PacketClientOutAddInformation.class;
		case 777: return PacketClientOutDisconnect.class;
		default: return null;
		}
	}
}
