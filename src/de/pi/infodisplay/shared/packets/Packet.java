package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
	
	protected int id;
	
	public Packet(int id) {
		this.id = id;
	}
	
	/**
	 * Read the Packet from ByteBuf
	 * @param byteBuf the ByteBuf
	 * @throws IOException if the ByteBuf is not acceptable with the Packet
	 */
	public abstract void read(ByteBuf byteBuf) throws IOException;

	public abstract void write(ByteBuf byteBuf) throws IOException;
	
	public int getID() {
		return id;
	}
}
