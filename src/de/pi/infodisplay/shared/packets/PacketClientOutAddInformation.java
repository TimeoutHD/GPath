package de.pi.infodisplay.shared.packets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.netty.buffer.ByteBuf;

public class PacketClientOutAddInformation extends Packet {
	
	private String title;
	private byte[] fileData;
	
	public PacketClientOutAddInformation(String title, File file) throws IOException {
		super(300);
		this.title = title;
		this.fileData = Files.readAllBytes(file.toPath());
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		title = Packet.decodeString(byteBuf);
		fileData = Packet.decodeByteArray(byteBuf);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, title);
		Packet.encodeByteArray(byteBuf, fileData);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public File writeFile(File emptyFile) throws IOException {
		return Files.write(emptyFile.toPath(), fileData).toFile();
	}
}
