package de.pi.infodisplay;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.server.Server;

public class Main {

	public static void main(String[] args) {
		if(args.length > 1) {
			if("server".equalsIgnoreCase(args[0])) {
				try {
					new Server();
				} catch (Exception e) {
					Logger.getGlobal().log(Level.SEVERE, "Could not start Server", e);
				}
			} else if("client".equalsIgnoreCase(args[0])) {
				String host = "127.0.0.1";
				String port = "8000";
				for(int i = 1; i < (args.length -1); i++) {
					if("-h".equalsIgnoreCase(args[i])) host = args[i + 1];
					else if("-p".equalsIgnoreCase(args[i])) port = args[i + 1];
				}
				if(port.matches("[0-9]")) new Client(host, Integer.parseInt(port));
			}
		}
	}
}
