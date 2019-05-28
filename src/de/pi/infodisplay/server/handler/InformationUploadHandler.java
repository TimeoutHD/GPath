package de.pi.infodisplay.server.handler;

import de.pi.infodisplay.server.Server;
import de.pi.infodisplay.shared.packets.PacketClientOutAddInformation;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class InformationUploadHandler extends ChannelHandlerAdapter {

	private Server parent;
	
	public InformationUploadHandler(Server parent) {
		this.parent = parent;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof PacketClientOutAddInformation) {
			PacketClientOutAddInformation packet = (PacketClientOutAddInformation) msg;
			if(parent.getClientManager())
		}
	}
	
	

}
