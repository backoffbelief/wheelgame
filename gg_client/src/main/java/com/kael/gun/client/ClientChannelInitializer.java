package com.kael.gun.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final GuiClient client;

	public ClientChannelInitializer(GuiClient client) {
		super();
		this.client = client;
	}


	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline cp = ch.pipeline();
		cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
		cp.addLast(new IoProtoBufDecoder());
		cp.addLast(new IoProtoBufEncoder());
		cp.addLast(new IoHandler(client));
	}

}
