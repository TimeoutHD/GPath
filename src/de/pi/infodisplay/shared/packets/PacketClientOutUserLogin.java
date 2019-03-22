package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketClientOutUserLogin extends Packet {
	
	private String name;
	private String password;
	
	public PacketClientOutUserLogin(String name, String password) {
		super(101);
		this.name = name;
		this.password = password;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, name);
		Packet.encodeString(byteBuf, password);
	}

}
