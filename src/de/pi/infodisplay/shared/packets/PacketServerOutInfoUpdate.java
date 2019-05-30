package de.pi.infodisplay.shared.packets;

import java.io.IOException;

import de.pi.infodisplay.server.Information;
import de.pi.infodisplay.shared.handler.ImageHandler;
import io.netty.buffer.ByteBuf;

@Deprecated
public class PacketServerOutInfoUpdate extends Packet {

	private boolean lastInfo;
	private Information information;
	
	protected PacketServerOutInfoUpdate(Information info, boolean lastInfo) {
		super(201);
		this.information = info;
		this.lastInfo = lastInfo;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeInt(information.getID());
		Packet.encodeString(byteBuf, new ImageHandler().convertImageToConvertedImage(information.getFile()).toString());
		byteBuf.writeBoolean(lastInfo);
	}

}
