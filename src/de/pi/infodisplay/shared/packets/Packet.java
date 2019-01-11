package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
	
	protected int id; 
	
	public Packet(int id) {
		this.id = id;
	}
	
	/**
	 * Liest das Paket aus einem ByteBuf.
	 * @param Das Argument byteBuf repräsentiert den ByteBuf.
	 * @throws Eine IOException wird geworfen, wenn der ByteBuf keinen passenden Paketinhalt hat.
	 */
	public abstract void read(ByteBuf byteBuf) throws IOException;

	/**
	 * Schreibt das Paket in einem ByteBuf.
	 * @param Das Argument byteBuf repräsentiert den ByteBuf.
	 * @throws Eine IOException wird geworfen, wenn der ByteBuf das Paket nicht unterstützt.
	 */
	public abstract void write(ByteBuf byteBuf) throws IOException;
	
	/**
	 * @return Gibt die ID des Pakets zurück.
	 */
	public int getID() {
		return id;
	}
}
