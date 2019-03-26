package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketClientOutDisconnect extends Packet {
	
	private String ipv4;
	
	public PacketClientOutDisconnect() {
		super(777);
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.ipv4 = Packet.decodeString(byteBuf);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, ipv4);
	}
	
	public String getIPv4Adress() {
		return ipv4;
	}

}
