package de.pi.infodisplay.server.coders;

import de.pi.infodisplay.shared.packets.InfoDisplayPacket;

public class RequestData {
	private InfoDisplayPacket[] contents;

	public InfoDisplayPacket[] getContents() {
		return contents;
	}

	public void setContents(InfoDisplayPacket[] contents) {
		this.contents = contents;
	}
}
