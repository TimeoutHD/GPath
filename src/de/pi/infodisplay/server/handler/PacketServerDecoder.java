package de.pi.infodisplay.server.handler;

import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutDisconnect;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;

public class PacketServerDecoder extends PacketDecoder {

	@Override
	protected Class<? extends Packet> getPacketClassByID(int id) {
		switch(id) {
		case 0: return PacketClientOutInfo.class;
		case 777: return PacketClientOutDisconnect.class;
		default: return null;
		}
	}

}
