package de.pi.infodisplay.shared.packets;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import io.netty.buffer.ByteBuf;

public class PacketClientOutAddInformation extends Packet {
	
	private String title;
	private byte[] fileData;
	
	public PacketClientOutAddInformation() {
		super(300);
	}
	
	public PacketClientOutAddInformation(String title, File file) throws IOException {
		this();
		this.title = title;
		this.fileData = FileUtils.readFileToByteArray(file);
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.title = Packet.decodeString(byteBuf);
		this.fileData = Packet.decodeByteArray(byteBuf);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, title);
		Packet.encodeByteArray(byteBuf, fileData);
	}

	public void writeFileData(File target) throws IOException {
		FileUtils.writeByteArrayToFile(target, fileData);
	}
	
	public String getTitle() {
		return title;
	}
}
