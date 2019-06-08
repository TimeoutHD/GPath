package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketServerOutAddInformation extends Packet {
	
	private boolean result;

	public PacketServerOutAddInformation() {
		super(300);
	}
	
	public PacketServerOutAddInformation(boolean result) {
		super(300);
		this.result = result;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.result = byteBuf.readBoolean();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeBoolean(result);
	}

	public boolean success() {
		return result;
	}
}
