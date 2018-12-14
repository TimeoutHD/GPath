package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketPlayOutInt extends Packet {

	private int number;
	
	public PacketPlayOutInt() {
		this.id = 0;
	}
	
	public PacketPlayOutInt(int number) {
		this.id = 0;
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
