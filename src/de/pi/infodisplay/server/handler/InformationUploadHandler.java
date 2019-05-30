package de.pi.infodisplay.server.handler;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.shared.packets.PacketClientOutAddInformation;
import de.pi.infodisplay.shared.security.User;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class InformationUploadHandler extends ChannelHandlerAdapter {
	
	private static final Map<String, File> folderCache = new LinkedHashMap<String, File>();
	private static final File infoDisplayFolder = new File(System.getProperty("user.home"), ".informationdisplay");
	
	static {
		infoDisplayFolder.mkdirs();
	}
	
	private Server parent;
	
	public InformationUploadHandler(Server parent) {
		this.parent = parent;
	}
	
	private static void addFileToCache(String ip, File file) {
		if(folderCache.containsKey(ip)) folderCache.replace(ip, file);
		else folderCache.put(ip, file);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof PacketClientOutAddInformation) {
			PacketClientOutAddInformation packet = (PacketClientOutAddInformation) msg;
			User sender = User.getFromDatabaseByUUID(packet.getUserUniqueID());
			if(parent.getClientManager().isAuthorized(((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress(), sender)) {
				File infoFolder = new File(infoDisplayFolder, "infos");
				infoFolder.mkdirs();
				infoFolder = new File(infoFolder, "infos");
			
				for(int i = 0; i < Integer.MAX_VALUE; i++) {
					if(i < Integer.MAX_VALUE - 1) {
						File folder = new File(infoFolder, String.valueOf(i));
						if(folder.exists() && !folder.isDirectory()) Files.delete(folder.toPath());
						if(!folder.exists()) {
							folder.mkdirs();
							infoFolder = folder;
							break;
						}
					} else throw new IndexOutOfBoundsException("Too many informations. Please delete Information before create a new one");
				}
				
				addFileToCache(((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress(), infoFolder);
			}
		}
	}
	
	public static File readAndFlushCache(String ip) {
		File file = folderCache.get(ip);
		folderCache.remove(ip);
		return file;
	}

}
