package de.pi.infodisplay.server.handler;

import de.pi.infodisplay.shared.handler.PacketEncoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketServerOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;
import de.pi.infodisplay.shared.packets.PacketServerOutInfoUpdate;
import de.pi.infodisplay.shared.packets.PacketServerOutInformation;
import de.pi.infodisplay.shared.packets.PacketServerOutInformationResult;

/**
 * Diese Klasse ist der spezielle Encoder des Servers.
 * 
 * @author piA
 *
 */
public class PacketServerEncoder extends PacketEncoder {

	/**
	 * Mithilfe dieser Methode werden die Packet-IDs den richtigen Wrapperklassen zugeordnet.
	 * Alle Packets mit dem Namen ServerOut sind hier gelistet.
	 * 
	 * @param id die ID des Packets
	 * @return Die Wrapperklasse des Packets.
	 */
	@Override
	protected Class<? extends Packet> getPacketTypeByID(int id) {
		switch(id) {
		case 0: return PacketServerOutInfo.class;
		case 101: return PacketServerOutAuthorizeUser.class;
		case 201: return PacketServerOutInfoUpdate.class;
		case 300: return PacketServerOutAddInformation.class;
		case 400: return PacketServerOutInformation.class;
		case 401: return PacketServerOutInformationResult.class;
		default: return null;
		}
	}

}
