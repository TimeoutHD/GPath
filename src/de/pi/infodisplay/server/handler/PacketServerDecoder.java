package de.pi.infodisplay.server.handler;

import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;
import de.pi.infodisplay.shared.packets.PacketClientOutDisconnect;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;

/**
 * Diese Klasse ist der spiezielle Decoder des Servers.
 * 
 * @author piA
 *
 */
public class PacketServerDecoder extends PacketDecoder {

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
		case 777: return PacketClientOutDisconnect.class;
		default: return null;
		}
	}

}
