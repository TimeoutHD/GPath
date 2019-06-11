package de.pi.infodisplay.client.netty.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.client.Information;
import de.pi.infodisplay.shared.packets.PacketServerOutInfoUpdate;
import de.pi.infodisplay.shared.packets.PacketServerOutInformation;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class InformationHandler extends ChannelHandlerAdapter {
	
	private List<Information> infos;
	
	private Client parent;
	
	public InformationHandler(Client parent) {
		this.parent = parent;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof PacketServerOutInfoUpdate) {
			// Progressbar erschaffen
			parent.getMainWindowGUI().createProgressWindow(((PacketServerOutInfoUpdate) msg).getInformationLength());
		} else if(msg instanceof PacketServerOutInformation) {
			// Erstelle neue Liste mit Informationen wenn Liste null ist
			if(infos == null) infos = new LinkedList<>();
			// Packet auslesen
			PacketServerOutInformation packet = (PacketServerOutInformation) msg;
			// Info zur Liste hinzufügen
			infos.add(new Information(packet.getTitle(), packet.writeFileData(getInformationFile())));
			// Progressbar erhöhen
			parent.getMainWindowGUI().addProgress();
		}
	}
	
	private File getInformationFile() throws IOException {
		// Liste darf nicht null sein
		if(infos != null) {
			// File deklarieren und zurückgeben
			return Files.createTempFile("data", ".jpg").toFile();
		}
		return null;
	}

	public List<Information> getInformations() {
		List<Information> list = new LinkedList<>(infos);
		infos.clear();
		return list;
	}
	
	public Client getParent() {
		return parent;
	}
}
