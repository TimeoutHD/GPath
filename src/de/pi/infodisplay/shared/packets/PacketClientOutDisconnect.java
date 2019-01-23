package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketClientOutDisconnect extends Packet {

	public PacketClientOutDisconnect() {
		super(777);
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeInt(id);
	}

}
