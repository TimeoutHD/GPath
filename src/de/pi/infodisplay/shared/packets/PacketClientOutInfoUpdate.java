package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketClientOutInfoUpdate extends Packet {

	protected PacketClientOutInfoUpdate() {
		super(201);
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		/* BRAUCHT NICHT GEFÜLLT WERDEN, hat keine Attribute */
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		/* BRAUCHT NICHT GEFÜLLT WERDEN, hat keine Attribute */
	}

}
