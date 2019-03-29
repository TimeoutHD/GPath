package de.pi.infodisplay.shared.packets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public abstract class Packet {
	
	protected int id; 
	
	protected Packet(int id) {
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
	
	/**
	 * Encodiert einen String in den ByteBuf. Dabei wird zuerst die Bytelänge des Strings (UTF-Kodierung) abgespeichert.
	 * Danach wird der UTF-8 kodierte String in den Bytebuf geschrieben und der Bytebuf zurückgegeben.
	 * 
	 * @param source der zu schreibene ByteBuf
	 * @param string der String
	 * @return der fertiggeschriebene ByteBuf
	 */
	public static ByteBuf encodeString(ByteBuf source, String string) {
		int count = 0;
		// Berechne Bytelänge des Strings (Länge des Chars biszu 3 Bytes)
		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if(c < 0x80) count++;
			else if(c < 0x800) count += 2;
			else count += 3;
		}
		
		// Schreibe Bytelänge vor den String
		source.writeInt(count);
		// Schreib den String in UTF-8 Kodierung in den ByteBuf
		ByteBufUtil.writeUtf8(source, string);
		return source;
	}
	
	/**
	 * Decodiert den String, der sich an der aktuellen Readerstelle im ByteBuf befindet nach dem Encodierungsverfahren von
	 * {@link Packet#encodeString(ByteBuf, String)}. Dabei wird zunächst die Länge des ByteStamps gelesen 
	 * und der daraus gezogene ByteArray zu einem String nach UTF-8 Kodierung decodiert.
	 * 
	 * @param source Dieses Argument repräsendtiert den zu lesenen ByteArray mit der richtigen Lesestelle
	 * @return Den decodierten String nach UTF-8 Kodierung
	 */
	public static String decodeString(ByteBuf source) {
		// Lese ByteArray mit passender Länge aus.
		byte[] byteSet = source.readBytes(source.readInt()).array();
		// Decodieren des Byte-Arrays zu einem String
		return new String(byteSet, StandardCharsets.UTF_8);
	}
}
