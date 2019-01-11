package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketPlayOutInt extends Packet {

	private int number;
	
	public PacketPlayOutInt() {
		super(0);
	}
	
	public PacketPlayOutInt(int number) {
		super(0);
		this.number = number;
	}
	
	
	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.number = byteBuf.readInt();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeInt(this.number);
	}

}
