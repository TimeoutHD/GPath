package de.pi.infodisplay.client.netty.handler;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.shared.handler.PacketHandler;
import de.pi.infodisplay.shared.handler.PacketHandler.NetworkType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ChannelNetworkInitializer extends ChannelInitializer<SocketChannel>{

	private PacketHandler packetHandler;
	private ClientNetworkHandler networkHandler;
	
	public ChannelNetworkInitializer(Client client) {
		this.packetHandler = new PacketHandler(NetworkType.CLIENT);
		this.networkHandler = new ClientNetworkHandler(client);
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		
		pipeline.addLast(packetHandler.getDecoder());
		pipeline.addLast(packetHandler.getEncoder());
		pipeline.addLast(networkHandler);
	}

}
