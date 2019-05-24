package de.pi.infodisplay.server.handler;

import java.io.File;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

public class FileUploadHandler extends SimpleChannelInboundHandler<HttpObject> {
	
	private final HttpRequestDecoder requestDecoder = new HttpRequestDecoder();
	private final HttpRequestEncoder requestEncoder = new HttpRequestEncoder();
	
	private File dataFolder;
	
	public FileUploadHandler(File dataFolder) {
		this.dataFolder = dataFolder;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
		// https://stackoverflow.com/questions/24113288/how-get-file-data-from-post-request-in-netty-4
		
	}
}
