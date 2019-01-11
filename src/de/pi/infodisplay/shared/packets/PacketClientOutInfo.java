package de.pi.infodisplay.shared.packets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import io.netty.buffer.ByteBuf;

public class PacketClientOutInfo extends Packet {
	
	private String message;
	
	public PacketClientOutInfo(String message) {
		super(0);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		Main.LOG.log(Level.INFO, byteBuf.readBytes(new byte[byteBuf.readableBytes()]).toString(StandardCharsets.UTF_8));
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		
	}

}
