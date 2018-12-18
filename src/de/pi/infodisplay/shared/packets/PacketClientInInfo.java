package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketClientInInfo extends Packet {
	
	private String message;
	
	public PacketClientInInfo(String message) {
		super(0);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		
	}

}
