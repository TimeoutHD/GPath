package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketServerOutInfoUpdate extends Packet {
	
	private int length;
	
	public PacketServerOutInfoUpdate() {
		super(201);
	}
	
	public PacketServerOutInfoUpdate(int length) {
		this();
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
