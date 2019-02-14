package de.pi.infodisplay.shared.handler;

import de.pi.infodisplay.client.netty.handler.PacketClientDecoder;
import de.pi.infodisplay.client.netty.handler.PacketClientEncoder;
import de.pi.infodisplay.server.handler.PacketServerDecoder;
import de.pi.infodisplay.server.handler.PacketServerEncoder;

/**
 * Die Klasse PacketHandler ist ein Konstrukt, dass den Decoder und Encoder
 * verallgemeinert als ein Objekt darstellen soll.
 * 
 * @author PI A
 *
 */
public class PacketHandler {
	
	/**
	 * Dieses Field ist das private Field für Decoder.
	 * Der Decoder soll die Packets, die reinkommen, decodieren,
	 * Quasi lesbar für den Java-Compiler machen.
	 * 
	 * Hier wird lediglich das Decoder-Field deklariert,
	 * welches im PacketHandler-Constructor initialisiert wird.
	 */
	private PacketDecoder decoder;
	
	/**
	 * Dieses Field ist das private Field für den Encoder.
	 * Der Encoder ist das Gegenstück vom Decoder und codiert die Packete zu
	 * einem ByteBuf, der dann über Netty versendet werden kann.
	 * 
	 * Auch hier wird lediglich das Encoder-Field deklariert,
	 * welches im PacketHandler initialisiert wird.
	 */
	private PacketEncoder encoder;
	
	/**
	 * Dieses Field ist der Standardkonstruktor dieser Klasse.
	 * Hier werden die oben genannten Fields initialisiert.
	 */
	public PacketHandler(NetworkType type) {
		if(type == NetworkType.CLIENT) {
			this.decoder = new PacketClientDecoder();
			this.encoder = new PacketClientEncoder();
		} else {
			this.decoder = new PacketServerDecoder();
			this.encoder = new PacketServerEncoder();
		}
	}
	
	/**
	 * Diese Methode gibt die Referenz des Decoders für dieses Objekt zur�ck.
	 * Dadurch ist es möglich, auf den Decoder aus externer Sichtweise zuzugreifen.
	 * 
	 * @return den Decoder als PacketDecoder
	 */
	public PacketDecoder getDecoder() {
		return decoder;
	}
	
	/**
	 * Diese Methode gibt die Referenz des Encoders für dieses Objekt zur�ck.
	 * Dadurch ist es möglich, auf den Encoder aus externer Sichtweise zuzugreifen.
	 * 
	 * @return den Encoder als PacketEncoder
	 */
	public PacketEncoder getEncoder() {
		return encoder;
	}
	
	/**
	 * Diese Klasse soll den Unterschied zwischen Server und Client des PacketHandlers
	 * definieren.
	 * 
	 * SERVER ist dabei das Enum, dass den Server definiert und CLIENT ist das Enum, dass den Client 
	 * definiert.
	 * 
	 * @author PI A
	 *
	 */
	public enum NetworkType {
		
		SERVER, CLIENT;
	}
}
