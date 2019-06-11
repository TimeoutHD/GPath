package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketServerOutAddInformation extends Packet {
	
	private boolean success;
	
	public PacketServerOutAddInformation()  {
		super(300);
	}
	
	public PacketServerOutAddInformation(boolean success) {
		this();
		this.success = success;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		/* LEER, da keine Attribute */
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		/* LEER, da keine Attribute */
	}
	
	public boolean success() {
		return success;
	}
}
