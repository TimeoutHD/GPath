package de.pi.infodisplay.shared.packets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.netty.buffer.ByteBuf;

public class PacketServerOutInformation extends Packet {
	
	private byte[] infoFile;
	private String title;

	public PacketServerOutInformation() {
		super(400);
	}
	
	public PacketServerOutInformation(File file, String title) throws IOException {
		super(400);
		this.infoFile = Files.readAllBytes(file.toPath());
		this.title = title;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.title = Packet.decodeString(byteBuf);
		this.infoFile = Packet.decodeByteArray(byteBuf);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, title);
		Packet.encodeByteArray(byteBuf, infoFile);
	}
	
	public File writeFileData(File emptyFile) throws IOException {
		return Files.write(emptyFile.toPath(), infoFile).toFile();
	}
	
	public String getTitle() {
		return title;
	}
}
