package de.pi.infodisplay.client.netty.handler;

import de.pi.infodisplay.shared.handler.PacketEncoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;

public class PacketClientEncoder extends PacketEncoder {

	@Override
	protected Class<? extends Packet> getPacketTypeByID(int id) {
		switch(id) {
		case 0: return PacketClientOutInfo.class;
		default: return null;
		}
	}

}
