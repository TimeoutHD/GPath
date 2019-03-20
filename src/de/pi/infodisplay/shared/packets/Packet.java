package de.pi.infodisplay.shared.packets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

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
	 * Schreibt das Paket in einen ByteBuf.
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
	
	public static ByteBuf encodeString(ByteBuf source, String string) {
		int count = 0;
		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if(c < 0x80) count++;
			else if(c < 0x800) count += 2;
			else count += 3;
		}
		
		source.writeInt(count);
		ByteBufUtil.writeUtf8(source, string);
		return source;
	}
	
	public static String decodeString(ByteBuf source, int stringlength) {
		byte[] byteSet = source.readBytes(stringlength).array();
		return new String(byteSet, StandardCharsets.UTF_8);
	}
}
