package de.pi.infodisplay.shared.handler;

import de.pi.infodisplay.client.netty.NettyClient;
import de.pi.infodisplay.client.netty.handler.PacketClientDecoder;
import de.pi.infodisplay.client.netty.handler.PacketClientEncoder;
import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.server.handler.PacketServerDecoder;
import de.pi.infodisplay.server.handler.PacketServerEncoder;
import de.pi.infodisplay.server.security.ClientUser;
import de.pi.infodisplay.shared.security.Operator;

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
	public PacketHandler(Operator operator) {
		if(operator instanceof NettyClient) {
			this.decoder = new PacketClientDecoder((NettyClient) operator);
			this.encoder = new PacketClientEncoder();
		} else if(operator instanceof Server) {
			this.decoder = new PacketServerDecoder((Server) operator);
			this.encoder = new PacketServerEncoder();
		} else if(operator instanceof ClientUser) {
			this.decoder = new PacketClientDecoder((ClientUser) operator);
			this.encoder = new PacketClientEncoder();
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
}
