package de.pi.infodisplay.shared.handler;

/**
 * Die Klasse PacketHandler ist ein Konstrukt, dass den Decoder und Encoder
 * verallgemeinert als ein Objekt darstellen soll.
 * 
 * @author PI A
 *
 */
public abstract interface PacketHandler {
	
	/**
	 * Diese Methode gibt die Referenz des Decoders für dieses Objekt zur�ck.
	 * Dadurch ist es möglich, auf den Decoder aus externer Sichtweise zuzugreifen.
	 * 
	 * @return den Decoder als PacketDecoder
	 */
	public PacketDecoder getDecoder();
	
	/**
	 * Diese Methode gibt die Referenz des Encoders für dieses Objekt zur�ck.
	 * Dadurch ist es möglich, auf den Encoder aus externer Sichtweise zuzugreifen.
	 * 
	 * @return den Encoder als PacketEncoder
	 */
	public PacketEncoder getEncoder();
}
