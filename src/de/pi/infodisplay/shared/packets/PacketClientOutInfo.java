package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.logging.Level;

import de.pi.infodisplay.Main;

public class PacketClientOutInfo extends Packet {
	
	private String msg;
	
	public PacketClientOutInfo(String message) {
		super(0);
		this.msg = message;
	}

	public String getMessage() {
		return msg;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {

		byte[] bytes = new byte[byteBuf.readableBytes()];
		
		byteBuf.readBytes(bytes);
		this.msg = String.valueOf(bytes);
		
		Main.LOG.log(Level.INFO, this.msg);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeBytes(this.getMsg().getBytes());
	}
	

	public String getMsg() {
		return this.msg;
	}
}
