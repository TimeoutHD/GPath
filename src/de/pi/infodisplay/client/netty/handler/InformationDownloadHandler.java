package de.pi.infodisplay.client.netty.handler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;

public class InformationDownloadHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final List<File> infos = new LinkedList<File>();
	private static final File tempFolder = new File(System.getProperty("user.home"), "/InformationDisplay");
	
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if(request.decoderResult().isSuccess() && request.method() == HttpMethod.GET) {
			File file = getActualFile();
			CharSequence ifModifiedSince = request.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
			if(ifModifiedSince != null) {
				SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.GERMAN);
				Date ifModifiedSinceDate = format.parse((String) ifModifiedSince);
	            long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
	            long fileLastModifiedSeconds = file.lastModified() / 1000;
	            if(ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
	            	
	            }
			}
		}
	}
	
	private File getActualFile() {
		int i = 0;
		while(new File(tempFolder, i + ".png").exists())i++;
		
		return new File(tempFolder, i + ".png");
	}
}
