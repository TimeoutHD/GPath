package de.pi.infodisplay.client;

public enum Command {
	CONNECT("connect", "cnt"),
	DISCONNECT("disconnect", "dcnt"),
	KILL("kill", "kl"),
	SEND("send", "snd"),
	CLEAR("clear", "clr"),
	HELP("help", "?");
	
	private String fullCommand;
	private String shortCommand;
	
	private Command(String full, String shrt) {
		this.fullCommand = full;
		this.shortCommand = shrt;
	}
	
	public static Command getCommandByName(String name) {
		String cmdString = name.split(" ")[0];
		for(Command cmd : values()) {
			if(cmd.getFullCommand().equalsIgnoreCase(cmdString) || cmd.getShortCommand().equalsIgnoreCase(cmdString))
				return cmd;
		}
		return null;
	}
	
	public String getFullCommand() {
		return fullCommand;
	}
	
	public String getShortCommand() {
		return shortCommand;
	}
}
