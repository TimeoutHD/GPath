package de.pi.infodisplay.shared.packets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import de.pi.infodisplay.shared.security.User;
import io.netty.buffer.ByteBuf;

public class PacketClientOutAddInformation extends Packet {
	
	private String title;
	private byte[] fileData;
	private UUID userID;
	
	public PacketClientOutAddInformation(String title, User user, File file) throws IOException {
		super(300);
		this.title = title;
		this.userID = user.getUniqueId();
		this.fileData = Files.readAllBytes(file.toPath());
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		title = Packet.decodeString(byteBuf);
		userID = UUID.fromString(Packet.decodeString(byteBuf));
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, title);
		Packet.encodeString(byteBuf, userID.toString());
	}

	public UUID getUserUniqueID() {
		return this.userID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public File writeFile(File emptyFile) throws IOException {
		return Files.write(emptyFile.toPath(), fileData).toFile();
	}
}
