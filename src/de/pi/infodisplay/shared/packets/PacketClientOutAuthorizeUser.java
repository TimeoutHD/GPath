package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketClientOutAuthorizeUser extends Packet {

	private int id;
	private String username;
	private String password;
	
	public PacketClientOutAuthorizeUser() {
		super(101);
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		int id = byteBuf.readInt();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf = Unpooled.unmodifiableBuffer(Unpooled.copiedBuffer(byteBuf), 
				Unpooled.copiedBuffer(username.getBytes(StandardCharsets.UTF_8)), 
				Unpooled.copiedBuffer(password.getBytes(StandardCharsets.UTF_8)));
		System.out.println(byteBuf);
	}
}
