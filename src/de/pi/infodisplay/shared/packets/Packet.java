package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
	
	protected int id;
	
	public abstract void read(ByteBuf byteBuf) throws IOException;
	
	public abstract void write(ByteBuf byteBuf) throws IOException;
	
	public int getID() {
		return id;
	}
}
