package de.pi.infodisplay.client.netty.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.client.Information;
import de.pi.infodisplay.shared.packets.PacketServerOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketServerOutInfoUpdate;
import de.pi.infodisplay.shared.packets.PacketServerOutInformation;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class InformationHandler extends ChannelHandlerAdapter {
	
	private static final File infofolder = new File(System.getProperty("user.home"), "/.infodisplay");

	private List<Information> infos;
	
	private Client parent;
	
	public InformationHandler(Client parent) {
		this.parent = parent;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof PacketServerOutInfoUpdate) {
			// Erstelle neue Liste mit Informationen
			infos = new ArrayList<Information>(((PacketServerOutInfoUpdate)msg).getInformationLength());
		} else if(msg instanceof PacketServerOutInformation) {
			// Packet auslesen
			PacketServerOutInformation packet = (PacketServerOutInformation) msg;
			// Info zur Liste hinzufügen
			infos.add(new Information(packet.getTitle(), packet.writeFileData(getInformationFile())));
		} else if(msg instanceof PacketServerOutAddInformation) {
			boolean success = ((PacketServerOutAddInformation)msg).success();
		}
	}
	
	private File getInformationFile() throws IOException {
		// Liste darf nicht null sein
		if(infos != null) {
			// Für jede Datei in der Range
			for(int i = 0; i < infos.size(); i++) {
				// File deklarieren
				File file = new File(infofolder, i + ".png");
				// Lösche, wenn die Datei existiert und ein Ordner, oder leer ist
				if(file.exists() && (!file.isFile() || file.length() == 0)) Files.delete(file.toPath());
				// Wenn die Datei danach nicht existiert, erstelle sie und gib sie zurück.
				if(file.createNewFile()) return file;
			}
		}
		return null;
	}

	public List<Information> getInformations() {
		return infos != null ? new ArrayList<>(infos) : new ArrayList<>();
	}
	
	public Client getParent() {
		return parent;
	}
}
