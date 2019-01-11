package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketServerOutInfo extends Packet {
	
	private String msg;

	public PacketServerOutInfo() {
		super(0);
		this.msg = "";
	}
	
	public PacketServerOutInfo(String msg) {
		super(0);
		this.msg = msg;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		
		byte[] bytes = new byte[byteBuf.readableBytes()];
		
		byteBuf.readBytes(bytes);
		this.msg = String.valueOf(bytes);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeBytes(this.msg.getBytes());
	}
}
