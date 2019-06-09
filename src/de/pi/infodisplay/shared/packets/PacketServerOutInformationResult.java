package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketServerOutInformationResult extends Packet {

	public PacketServerOutInformationResult() {
		super(401);
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		/* LEER, DA KEINE ATTRIBUTE */
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		/* LEER, DA KEINE ATTRIBUTE */
	}

}
