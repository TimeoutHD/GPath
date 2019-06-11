package de.pi.infodisplay.shared.packets;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
		long count = 0;
		// Berechne Bytelänge des Strings (Länge des Chars biszu 3 Bytes)
		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if(c < 0x80) count++;
			else if(c < 0x800) count += 2;
			else count += 3;
		}
		// ByteBuf-Größe anpassen
		source.capacity((int) (source.readerIndex() + 8 + count));
		// Schreibe Bytelänge vor den String
		source.writeLong(count);
		// Schreib den String in UTF-8 Kodierung in den ByteBuf
		ByteBufUtil.writeUtf8(source, string);
		return source;
	}
	
	/**
	 * Decodiert den String, der sich an der aktuellen Readerstelle im ByteBuf befindet nach dem Encodierungsverfahren von
	 * {@link Packet#encodeString(ByteBuf, String)}. Dabei wird zunächst die Länge des ByteStamps gelesen 
	 * und der daraus gezogene ByteArray zu einem String nach UTF-8 Kodierung decodiert.
	 * 
	 * @param source Dieses Argument repräsentiert den zu lesenen ByteArray mit der richtigen Lesestelle
	 * @return Den decodierten String nach UTF-8 Kodierung
	 */
	public static String decodeString(ByteBuf source) {
		// Lese ByteArray mit passender Länge aus.
		byte[] byteSet = decodeByteArray(source);
		// Decodieren des Byte-Arrays zu einem String
		return new String(byteSet, StandardCharsets.UTF_8);
	}
	
	/**
	 * Decodiert den ByteArray, der sich an der aktuellen Readerstelle im ByteBuf nach dem Encodierungsverfahren von
	 * {@link Packet#encodeByteArray(ByteBuf, byte[])}. Dabei wird zunächst die Länge des ByteStamps gelesen
	 * und der daraus gezogenene ByteArray zurückgegeben
	 * @param source Dieses Argument repräsentiert den zu lesenen ByteArray mit der richtigen Lesestelle
	 * @return Den decodierten ByteArray
	 */
	public static byte[] decodeByteArray(ByteBuf source) {
		int index = (int) source.readLong();
		// Lese ByteArray mit passender Länge aus und gibt die zurück
		return source.readBytes(index).array();
	}
	
	/**
	 * Encodiert einen ByteArray in den ByteBuf. Dabei wird zunächst die Länge dey ByteArrays als Int in den ByteBuf geschrieben und danach der ByteArray selbst.
	 * Schlussendlich wird der geschriebene ByteBuf zurückgegeben
	 * @param source der zu schreibene ByteBuf
	 * @param data der ByteArray
	 */
	public static ByteBuf encodeByteArray(ByteBuf source, byte[] data) {
		// ByteBuf-Größe anpassen:
		source.capacity(source.readerIndex() + 8 + data.length);
		// Länge des ByteArrays schreiben
		source.writeLong(data.length);
		// ByteArray Schreiben
		source.writeBytes(data);
		return source;
	}
	
	public static File zip(File file, File zipFile) throws IOException {
		FileOutputStream out = new FileOutputStream(zipFile.getAbsolutePath());
		try(ZipOutputStream zipOut = new ZipOutputStream(out); FileInputStream fileIn = new FileInputStream(file)) {
			ZipEntry entry = new ZipEntry(file.getName());
			zipOut.putNextEntry(entry);
			
			byte[] bytes = new byte[1024];
			int length;
			while((length = fileIn.read(bytes)) >= 0) zipOut.write(bytes, 0, length);
			
			zipOut.closeEntry();
		}
		
		return zipFile;
	}
	
	public static File unzip(File zipFile, File goal) throws IOException {
		try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry = zipIn.getNextEntry();
			while(entry != null) {
				try (BufferedOutputStream bOut = new BufferedOutputStream(new FileOutputStream(goal))) {
					byte[] bytesIn = new byte[4096];
					int read = 0;
					while((read = zipIn.read(bytesIn)) != -1) {
						bOut.write(bytesIn, 0, read);
					}
				}
			}
		}
		return goal;
	}
}
