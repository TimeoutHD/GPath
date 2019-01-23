package de.pi.infodisplay.server.handler;

import de.pi.infodisplay.shared.handler.PacketEncoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;

public class PacketServerEncoder extends PacketEncoder {

	@Override
	protected Class<? extends Packet> getPacketTypeByID(int id) {
		switch(id) {
		case 0: return PacketServerOutInfo.class;
		
		default: return null;
		}
	}

}
