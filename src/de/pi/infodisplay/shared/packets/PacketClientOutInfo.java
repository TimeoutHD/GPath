package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.logging.Level;

import de.pi.infodisplay.Main;

public class PacketClientOutInfo extends Packet {
	
	private String msg;
	
	public PacketClientOutInfo() {
		super(0);
	}
	
	public PacketClientOutInfo(String message) {
		super(0);
		this.msg = message;
	}

	public String getMessage() {
		return msg;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.msg = Packet.decodeString(byteBuf);
		Main.LOG.log(Level.INFO, this.msg);
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, msg);
	}
	

	public String getMsg() {
		return this.msg;
	}
}
