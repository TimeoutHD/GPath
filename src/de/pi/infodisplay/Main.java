package de.pi.infodisplay;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.server.Server;

public class Main {
	
	private static final Logger LOG = Logger.getLogger("InformationDisplay");

	public static void main(String[] args) {
		if(args.length > 1) {
			if("server".equalsIgnoreCase(args[0])) {
				try {
					String port = "8000";
					for(int i = 1; i < (args.length -1); i++) {
						if("-p".equalsIgnoreCase(port) && args[i +1].matches("\\d+")) port = args[i +1];
					}
					new Server(Integer.parseInt(port));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, "Could not start Server", e);
				}
			} else if("client".equalsIgnoreCase(args[0])) {
				String host = "127.0.0.1";
				String port = "8000";
				for(int i = 1; i < (args.length -1); i++) {
					if("-h".equalsIgnoreCase(args[i])) host = args[i + 1];
					else if("-p".equalsIgnoreCase(args[i])) port = args[i + 1];
				}
				if(port.matches("\\d+")) {
					new Client(host, Integer.parseInt(port));
				}
			} else sendHelp();
		} else sendHelp();
	}
	
	private static void sendHelp() {
		LOG.log(Level.INFO, "Please use the parameter: server / client");
		LOG.log(Level.INFO, "java -jar ./InformationDisplay.jar server");
		LOG.log(Level.INFO, "java -jar ./InformationDisplay.jar client");
		LOG.log(Level.INFO, "");
		LOG.log(Level.INFO, "param -p <Port>: Set the port of the Server");
		LOG.log(Level.INFO, "-h <Hostname>: Set the host of the Server");
	}
	
	public static Logger getConsole() {
		return LOG;
	}
}
