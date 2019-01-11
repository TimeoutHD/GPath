package de.pi.infodisplay.client.netty.handler;

import de.pi.infodisplay.shared.handler.PacketDecoder;
import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketServerOutInfo;

public class PacketClientDecoder extends PacketDecoder {

	@Override
	protected Class<? extends Packet> getPacketClassByID(int id) {
		switch(id) {
		case 0: return PacketServerOutInfo.class;
		default: return null;
		}
	}
}
