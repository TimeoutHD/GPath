package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketServerOutInfoUpdate extends Packet {
	
	private int length;
	
	public PacketServerOutInfoUpdate(int length) {
		super(201);
		this.length = length;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.length = byteBuf.readInt();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeInt(length);
	}

	public int getInformationLength() {
		return length;
	}
}
