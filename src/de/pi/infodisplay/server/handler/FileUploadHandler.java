package de.pi.infodisplay.server.handler;

import java.io.File;

import de.timeout.libs.MySQL;
import io.netty.channel.ChannelHandlerAdapter;

public class FileUploadHandler extends ChannelHandlerAdapter {
	
	private static MySQL mysql = new MySQL("localhost", 3306, "informationdisplay");
	
	static {
		
	}
	
	private File dataFolder;
	
	public FileUploadHandler(File dataFolder) {
		
	}

}
