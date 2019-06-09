package de.pi.infodisplay.shared.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.logging.Level;

import de.pi.infodisplay.Main;

public class PacketServerOutInfo extends Packet {
	
	private String msg;

	public PacketServerOutInfo() {
		super(0);
	}
	
	public PacketServerOutInfo(String msg) {
		this();
		this.msg = msg;
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

	public String getMessage() {
		return this.msg;
	}
}
