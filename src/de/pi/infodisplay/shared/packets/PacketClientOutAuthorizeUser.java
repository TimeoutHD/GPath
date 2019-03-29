package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketClientOutAuthorizeUser extends Packet {

	private String username;
	private String password;
	
	private PacketClientOutAuthorizeUser() {
		super(101);
	}
	
	public PacketClientOutAuthorizeUser(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.username = Packet.decodeString(byteBuf);
		this.password = Packet.decodeString(byteBuf);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, username);
		Packet.encodeString(byteBuf, password);
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
}
