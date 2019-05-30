package de.pi.infodisplay.server.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import de.pi.infodisplay.Main;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

@Sharable
public class FileUploadHandler extends SimpleChannelInboundHandler<HttpObject> {
	
	private final HttpDataFactory factory = new DefaultHttpDataFactory(true);
	private final Map<String, File> folders = new LinkedHashMap<>();
	
	private HttpRequest request;
	private HttpPostRequestDecoder decoder;
	
	public FileUploadHandler() {
		this.request = null;
		this.decoder = null;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
		String remoteAddress = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
		folders.put(remoteAddress, InformationUploadHandler.readAndFlushCache(remoteAddress));
		if(httpObject instanceof HttpRequest) {
			request = (HttpRequest) httpObject;
			URI uri = new URI(request.uri());
			
			Main.LOG.log(Level.INFO, () -> "Bekomme URI: " + uri);
			if(request.method() == HttpMethod.POST) {
				decoder = new HttpPostRequestDecoder(factory, request);
				decoder.setDiscardThreshold(0);
			} else sendResponse(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, null);
		}
		
		if(decoder != null && httpObject instanceof HttpContent) {
			final HttpContent chunk = (HttpContent) httpObject;
			decoder.offer(chunk);
			readChunk(ctx);
				
			if(chunk instanceof LastHttpContent)
				resetPostRequestDecoder();
		}	
	}
	
	private void readChunk(ChannelHandlerContext ctx) {
		String remoteAddress = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
		while(decoder.hasNext()) {
			InterfaceHttpData data = decoder.next();
			if(data != null) {
				try {
					switch(data.getHttpDataType()) {
					case Attribute:
						break;
					case FileUpload:
						final File file = new File(folders.get(remoteAddress), "data.jpg");
						try { 
							copyData(ctx, file, (FileUpload) data);
						} catch (IOException e) {
							Main.LOG.log(Level.WARNING, "Kann die Datei " + file.getName() + " nicht erstellen", e);
						}
						folders.remove(remoteAddress);
						break;
					default:
						break;
					}
				} finally {
					data.release();
				}
			}
		}
	}
	
	private void copyData(ChannelHandlerContext ctx, File file, FileUpload fileUpload) throws IOException {
		if(file.createNewFile()) {
			Main.LOG.log(Level.INFO, "Datei " + file.getName() + " erstellt");
			try(FileChannel in = new FileInputStream(fileUpload.getFile()).getChannel();
					FileChannel out = new FileOutputStream(file).getChannel()) {
				out.transferFrom(in, 0, in.size());
				sendResponse(ctx, HttpResponseStatus.CREATED, "File-Name: " + file.getAbsolutePath());
			}
		}
	}
	
	private void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String message) {
		final FullHttpResponse response;
		if(message == null) message = "Fehler: " + status;
		message += " \r\n";
		
		final ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
		response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buffer);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	private void resetPostRequestDecoder() {
		request = null;
		decoder.destroy();
		decoder = null;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Main.LOG.log(Level.SEVERE, "Got Exception: ", cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(decoder != null) {
			decoder.cleanFiles();
		}
	}
	
	
}
