package de.pi.infodisplay.server.handler;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.shared.packets.PacketClientOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketClientOutInfoUpdate;
import de.pi.infodisplay.shared.packets.PacketServerOutAddInformation;
import de.pi.infodisplay.shared.packets.PacketServerOutInfoUpdate;
import de.pi.infodisplay.shared.packets.PacketServerOutInformation;
import de.pi.infodisplay.shared.security.User;
import de.timeout.libs.MySQL.Table;
import io.netty.channel.ChannelHandlerContext;

public class FileHandler {
	
	private static final File tempFolder = new File(System.getProperty("user.home"), ".infodisplay");
	
	static {
		tempFolder.mkdirs();
	}
		
	private Server parent;
	
	public FileHandler(Server parent) {
		this.parent = parent;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException, SQLException {
		if(msg instanceof PacketClientOutInfoUpdate) {
			// Lese Informationen aus der MySQL
			Table table = Server.getMySQL().executeStatement("SELECT path, title FROM Information");
			
			synchronized (table) {
				// Erstelle Listen mit Daten
				final List<String> paths = table.getColumn("path").getValues();
				final List<File> files = new ArrayList<File>();
				
				// Fülle files mit Dateien
				paths.forEach(path -> files.add(new File(path)));
				
				// Erstelle ein Informationspaket und sende es an den Client zurück
				PacketServerOutInfoUpdate updatePacket = new PacketServerOutInfoUpdate(paths.size());
				this.parent.apply(updatePacket, ctx.channel());
				
				// Erstelle vereinzelte Packete mit den einzelnen Informationen und sende sie an den Client als Antwort
				for(int i = 0; i < paths.size(); i++) {
					File file = paths.get(i) != null ? new File(paths.get(i)) : null;
					this.parent.apply(new PacketServerOutInformation(file, table.getValue("title", i)), ctx.channel());
				}
			}
		} else if(msg instanceof PacketClientOutAddInformation) {
			// Lese Packet aus
			PacketClientOutAddInformation inPacket = (PacketClientOutAddInformation) msg;
			// Lese wichtige Informationen aus
			String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
			User creator = parent.getClientManager().getUser(ip);
			
			// Wenn der Benutzer autorisiert ist:
			if(parent.getClientManager().isAuthorized(ip, creator)) {
				System.out.println("Ist autorisiert");
				// Erstelle neue Datei
				File actualFile = createNewFileInformation();
				
				// Resultat für Antwort
				boolean success = false; 
				
				// Sobald alles sauber funktioniert hat
				if(actualFile != null) {
					// Schreiben von Daten in die File
					inPacket.writeFileData(actualFile);
					
					// Füge Daten in die Datenbank ein
					Server.getMySQL().executeVoidStatement("INSERT INTO Information(creatorID, title, path) VALUES (?, ?, ?)", creator.getUniqueId().toString(), inPacket.getTitle(), actualFile.getAbsolutePath());	
					success = true;
				}
				
				// Erstelle AntwortPaket und sende sie an den Client
				PacketServerOutAddInformation resultPacket = new PacketServerOutAddInformation(success);
				parent.apply(resultPacket, ctx.channel());
			}

		}
	}

	private File createNewFileInformation() throws IOException {
		File folder = new File(tempFolder, "infos");
		// Erstelle Ordner von InfoDisplay
		if(folder.mkdirs()) {
			// Ordner überprüfen
			for(int i = 0; i < Integer.MAX_VALUE; i++) {
				// Wenn der im Valid-Bereich ist
				if(i != Integer.MAX_VALUE -1) {
					// Ordner laden
					File infoFolder = new File(folder, String.valueOf(i));
					// Wenn die Daten nicht nutzbar sind, lösche diese
					if(infoFolder.exists() && !infoFolder.isDirectory()) Files.delete(infoFolder.toPath());
					
					// Wenn der Ordner nicht existiert, erstelle diesen und nutze ihn für die Methode weiter
					if(!infoFolder.exists()) {
						folder = infoFolder;
						infoFolder.mkdirs();
						break;
					}
				} else throw new IllegalStateException("Cannot create Information. Please delete Informations before creating a new one");
			}
			// Erstelle JPG file und gib diese zurück.
			File file = new File(folder, "data.jpg");
			Files.createFile(file.toPath());
			return file;
		} 
		return null;
	}
}
